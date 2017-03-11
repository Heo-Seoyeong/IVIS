package hsy.barmiapp_android_third.recipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import hsy.barmiapp_android_third.R;
import hsy.barmiapp_android_third.recipe_detail.RecipeDetailActivity;

/**
 * Created by hsy on 2016. 12. 29..
 */

public class SearchActivity extends Fragment {

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MATERIAL = "material";
    private static final String TAG_RECIPE ="recipe";
    private static final String TAG_TIME ="time";
    private static final String TAG_CALORIE ="calorie";
    private static final String TAG_IMAGE1 ="image1";
    private static final String TAG_IMAGE2 ="image2";

    int[] imgs = {R.drawable.rec_image1, R.drawable.rec_image2, R.drawable.rec_image3, R.drawable.rec_image4, R.drawable.rec_image5, R.drawable.rec_image6, R.drawable.rec_image7, R.drawable.rec_image8, R.drawable.rec_image9, R.drawable.rec_image10};

    private ArrayList<HashMap<String, String>> recipeList = null;
    JSONArray recipe = null;
    String myJSON;
    HashMap<String,String> recipes;
    private ListView searchInfoListView = null;
    private SearchAdapter searchAdapter = null;

    public static SearchActivity newInstance(int page){
        SearchActivity searchActivity = new SearchActivity();
        Bundle args = new Bundle();
        args.putInt("someInt",page);
        searchActivity.setArguments(args);

        return searchActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        searchInfoListView = (ListView)view.findViewById(R.id.search_info_listview);

        recipeList = new ArrayList<HashMap<String,String>>();
        getData("http://35.161.222.253/recipe_display.php", view);

        return view;
    }


    protected void getData(String url, final View view) {
        class GetDataJSON extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
                searchAdapter = new SearchAdapter(view.getContext(), recipeList, imgs);
                searchInfoListView.setAdapter(searchAdapter);
                searchInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> recipeHash= new HashMap<String, String>();
                        recipeHash = recipeList.get(position);
                        int recipeimg = imgs[position];
                        Intent intentBD = new Intent(view.getContext(), RecipeDetailActivity.class);
                        intentBD.putExtra("recipeImg",recipeimg);
                        intentBD.putExtra("recipeHash", recipeHash);
                        startActivity(intentBD);
                    }
                });
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    // 서버데이터를 Jason으로 변환
    public void showList(){
        try {
            JSONObject jsonobj = new JSONObject(myJSON);
            recipe = jsonobj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<recipe.length();i++){
                JSONObject c = recipe.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String material = c.getString(TAG_MATERIAL);
                String recipe = c.getString(TAG_RECIPE);
                String time = c.getString(TAG_TIME);
                String calorie = c.getString(TAG_CALORIE);
                String image1 = c.getString(TAG_IMAGE1);
                String image2 = c.getString(TAG_IMAGE2);



                recipes = new HashMap<String,String>();

                recipes.put(TAG_ID,id);
                recipes.put(TAG_NAME,name);
                recipes.put(TAG_MATERIAL,material);
                recipes.put(TAG_RECIPE,recipe);
                recipes.put(TAG_TIME,time);
                recipes.put(TAG_CALORIE,calorie);
                recipes.put(TAG_IMAGE1,image1);
                recipes.put(TAG_IMAGE2,image2);

                recipeList.add(recipes);
                Log.d("abc", String.valueOf(recipeList));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}