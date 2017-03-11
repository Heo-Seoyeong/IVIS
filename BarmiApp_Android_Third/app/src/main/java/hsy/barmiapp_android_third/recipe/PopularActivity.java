package hsy.barmiapp_android_third.recipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hsy.barmiapp_android_third.R;
import hsy.barmiapp_android_third.develope.DevelopeActivity;
import hsy.barmiapp_android_third.notice.NoticeDetailActivity;
import hsy.barmiapp_android_third.notice.NoticeInfoActivity;
import hsy.barmiapp_android_third.notice.NoticeModifyActivity;
import hsy.barmiapp_android_third.privacy.PrivacyInfoActivity;
import hsy.barmiapp_android_third.recipe_detail.RecipeDetailActivity;

/**
 * Created by hsy on 2016. 12. 29..
 */

public class PopularActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Intent myintent;
    private ListView popularListView;
    int[] imgs = {R.drawable.rec_image1, R.drawable.rec_image2, R.drawable.rec_image3, R.drawable.rec_image4, R.drawable.rec_image5, R.drawable.rec_image6, R.drawable.rec_image7, R.drawable.rec_image8, R.drawable.rec_image9, R.drawable.rec_image10};

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_NAME = "name";
    private static final String TAG_MATERIAL = "material";
    private static final String TAG_RECIPE ="recipe";
    private static final String TAG_TIME ="time";
    private static final String TAG_CALORIE ="calorie";
    private static final String TAG_IMAGE1 ="image1";
    private static final String TAG_IMAGE2 ="image2";

    private ArrayList<HashMap<String, String>> popularList = null;
    JSONArray popular = null;
    String myJSON;
    HashMap<String,String> populars;
    private SimpleAdapter popularAdapter = null;

    String searchdata;

    Intent intent;
    HashMap<String, String> hashMap;

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        intent = getIntent();
        searchdata = getIntent().getStringExtra("filterHash");
        Log.d("abc","검색 전달 : " + searchdata);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        popularListView = (ListView) findViewById(R.id.popular_info_listview);
        popularList = new ArrayList<HashMap<String,String>>();
        getData("http://35.161.222.253/recipe_search.php", searchdata);

    }


    protected void getData(String url, String searchdata) {
        class GetDataJSON extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String search = params[1];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);

                    String searchdata = search;

                    Log.d("abc","검색 씽크내부: " + searchdata);

                    String data  = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(searchdata, "UTF-8");

                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

//                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//                    StringBuilder sb = new StringBuilder();
//                    String line = null;


                    HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                    StringBuilder sb1 = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(conn1.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb1.append(json + "\n");
                    }

                    return sb1.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
                Log.d("abc","2" + String.valueOf(popularListView));
                Log.d("abc","5" + String.valueOf(popularList));
                Log.d("abc","6" + String.valueOf(popularAdapter));
                popularListView.setAdapter(popularAdapter);
                Log.d("abc","3");
                popularListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> popularHash = new HashMap<String, String>();
                        popularHash = popularList.get(position);
                        int recipeimg = imgs[position];
                        Intent intentBD = new Intent(getApplicationContext(), RecipeDetailActivity.class);
                        intentBD.putExtra("recipeImg",recipeimg);
                        intentBD.putExtra("recipeHash", popularHash);
                        startActivity(intentBD);
                    }
                });

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url, searchdata);
    }

    // 서버데이터를 Jason으로 변환
    public void showList(){
        try {
            JSONObject jsonobj = new JSONObject(myJSON);
            popular = jsonobj.getJSONArray(TAG_RESULTS);


            for(int i=0;i<popular.length();i++){
                JSONObject c = popular.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String material = c.getString(TAG_MATERIAL);
                String recipe = c.getString(TAG_RECIPE);
                String time = c.getString(TAG_TIME);
                String calorie = c.getString(TAG_CALORIE);
                String image1 = c.getString(TAG_IMAGE1);
                String image2 = c.getString(TAG_IMAGE2);

                populars = new HashMap<String,String>();

                populars.put(TAG_ID,id);
                populars.put(TAG_NAME,name);
                populars.put(TAG_MATERIAL,material);
                populars.put(TAG_RECIPE,recipe);
                populars.put(TAG_TIME,time);
                populars.put(TAG_CALORIE,calorie);
                populars.put(TAG_IMAGE1, String.valueOf(imgs[Integer.parseInt(id)-1]));
                populars.put(TAG_IMAGE2,image2);


                popularList.add(populars);
                Log.d("abc","1:"+popularList);
                popularAdapter = new SimpleAdapter(
                        PopularActivity.this, popularList, R.layout.cell_popular,
                        new String[]{TAG_NAME,TAG_IMAGE1},
                        new int[]{R.id.popular_cell_recipe_name, R.id.popular_cell_recipe_image}
                );
                Log.d("abc","4");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recipe_info, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("요리, 재료를 검색해보세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                System.out.println(s);
                Intent intent = new Intent(PopularActivity.this, PopularActivity.class);
                intent.putExtra("filterHash", s);
                startActivity(intent);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {


                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            myintent = new Intent(getApplicationContext(), PrivacyInfoActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_notice) {
            myintent = new Intent(getApplicationContext(), NoticeInfoActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_recipe) {
            myintent = new Intent(getApplicationContext(), RecipeInfoActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_manage) {
            myintent = new Intent(getApplicationContext(), DevelopeActivity.class);
            startActivity(myintent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
