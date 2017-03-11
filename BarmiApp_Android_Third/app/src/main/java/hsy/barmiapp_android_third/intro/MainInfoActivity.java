package hsy.barmiapp_android_third.intro;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

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
import hsy.barmiapp_android_third.develope.DevelopeActivity;
import hsy.barmiapp_android_third.notice.NoticeInfoActivity;
import hsy.barmiapp_android_third.privacy.PrivacyInfoActivity;
import hsy.barmiapp_android_third.recipe.RecipeInfoActivity;
import hsy.barmiapp_android_third.recipe_detail.RecipeDetailActivity;

public class MainInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton searchButton;
    private Intent myintent;

    private ViewFlipper flipper;
    private ToggleButton toggle_Flipping;

    String myJSON;

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

    WebView webView;

    JSONArray recipe = null;
    ArrayList<HashMap<String, String>> recipeList;
    ListView list;
    ListAdapter adapterA;
    HashMap<String,String> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(listener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ViewFlipper 객체 참조
        flipper= (ViewFlipper)findViewById(R.id.flipper);

        // list = (ListView) findViewById(R.id.listview_recipe);
        recipeList = new ArrayList<HashMap<String,String>>();
        getData("http://35.161.222.253/recipe_display.php");

    }

    public void webViewMove(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "https://www.youtube.com/user/honeykkicook"  ));
        startActivity(intent);
    }

    public void mainFlipper() {

        //총 10개의 이미지 중 3개만 XML에서 ImageView로 만들었었음.
        //소스코드에서 ViewFipper에게 나머지 7개의 이미지를 추가하기위해
        //ImageView 7개를 만들어서 ViewFlipper에게 추가함
        //layout_width와 layout_height에 대한 특별한 지정이 없다면
        //기본적으로 'match_parent'로 설정됨.
        for (int i = 0; i < 10; i++) {
            ImageButton imgbtn = new ImageButton(this);
            imgbtn.setImageResource(imgs[i]);
            final int finalI = i;
            imgbtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    HashMap<String, String> recipeHash = new HashMap<String, String>();
                    recipeHash = recipeList.get(finalI);
                    int recipeimg = imgs[finalI];
                    Intent intentBD = new Intent(getApplicationContext(), RecipeDetailActivity.class);
                    intentBD.putExtra("recipeImg",recipeimg);
                    intentBD.putExtra("recipeHash", recipeHash);
                    startActivity(intentBD);
                }
            });
            flipper.addView(imgbtn);
        }


        //ViewFlipper가 View를 교체할 때 애니메이션이 적용되도록 설정
        //애니메이션은 안드로이드 시스템이 보유하고 있는  animation 리소스 파일 사용.
        //ViewFlipper의 View가 교체될 때 새로 보여지는 View의 등장 애니메이션
        //AnimationUtils 클래스 : 트윈(Tween) Animation 리소스 파일을 Animation 객체로 만들어 주는 클래스
        //AnimationUtils.loadAnimaion() - 트윈(Tween) Animation 리소스 파일을 Animation 객체로 만들어 주는 메소드
        //첫번째 파라미터 : Context
        //두번재 파라미터 : 트윈(Tween) Animation 리소스 파일(여기서는 안드로이드 시스템의 리소스 파일을 사용
        //                    (왼쪽에서 슬라이딩되며 등장)
        Animation showIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        //ViewFlipper에게 등장 애니메이션 적용
        flipper.setInAnimation(showIn);

        //ViewFlipper의 View가 교체될 때 퇴장하는 View의 애니메이션
        //오른쪽으로 슬라이딩 되면 퇴장하는 애니메이션 리소스 파일 적용.
        //위와 다른 방법으로 애니메이션을 적용해봅니다.
        //첫번째 파라미터 : Context
        //두번재 파라미터 : 트윈(Tween) Animation 리소스 파일(오른쪽으로 슬라이딩되며 퇴장)
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);

        //1초의 간격으로 ViewFlipper의 View 자동 교체
        flipper.setFlipInterval(2000);//플리핑 간격(1000ms)
        flipper.startFlipping();//자동 Flipping 시작

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
        getMenuInflater().inflate(R.menu.main_info, menu);
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

    // 서버데이터를 Jason으로 변환
    public void baseJsonData(){
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
                recipes.put(TAG_IMAGE1, String.valueOf(imgs[i]));
                recipes.put(TAG_IMAGE2,image2);

                recipeList.add(recipes);
            }

        } catch (JSONException e) {
           // e.printStackTrace();
        }
    }

    protected void showList(){

        baseJsonData();

//        adapterA = new SimpleAdapter(
//                MainInfoActivity.this, recipeList, R.layout.cell_main,
//                new String[]{ TAG_NAME,TAG_IMAGE1},
//                new int[]{R.id.main_info_cell_title_textview, R.id.main_cell_recipe_image}
//        );
    }

    // 클릭시 OutputDetailView로 데이터 전달 및 화면 전환
//    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long l_position) {
//            HashMap<String, String> recipeHash= new HashMap<String, String>();
//            recipeHash = recipeList.get(position);
//            Intent intent = new Intent(MainInfoActivity.this, RecipeDetailActivity.class);
//            intent.putExtra("recipeHash", recipeHash);
//            startActivity(intent);
//        }
//    };


    protected void getData(String url) {
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
                mainFlipper();
//                list.setAdapter(adapterA);
//                list.setOnItemClickListener(mItemClickListener);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    ImageButton.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.search_button:
                    myintent = new Intent(getApplicationContext(), RecipeInfoActivity.class);
                    startActivity(myintent);
                    break;

            }
        }
    };
}
