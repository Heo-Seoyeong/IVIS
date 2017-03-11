package hsy.barmiapp_android_third.notice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import hsy.barmiapp_android_third.privacy.PrivacyInfoActivity;
import hsy.barmiapp_android_third.recipe.RecipeInfoActivity;

public class NoticeInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_TITLE="title";
    private static final String TAG_USER="user";
    private static final String TAG_DATE="date";
    private static final String TAG_VIEW="view";
    private static final String TAG_CONT="cont";

    private ImageButton noticeButton;
    private Intent myintent;


    private ArrayList<HashMap<String, String>> noticeList = null;
    JSONArray notice = null;
    String myJSON;
    HashMap<String,String> notices;
    private ListView noticeListView = null;
    private SimpleAdapter noticeAdapter = null;

    int[] imgs = {R.drawable.rec_image1, R.drawable.rec_image2, R.drawable.rec_image3, R.drawable.rec_image4, R.drawable.rec_image5, R.drawable.rec_image6, R.drawable.rec_image7, R.drawable.rec_image8, R.drawable.rec_image9, R.drawable.rec_image10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        noticeButton = (ImageButton) findViewById(R.id.modify_notice_button);
        noticeButton.setOnClickListener(listener);

        noticeListView = (ListView) findViewById(R.id.notice_list);
        noticeList = new ArrayList<HashMap<String,String>>();
        getData("http://35.161.222.253/board_disply.php");
    }

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
                Log.d("abbb",result);
                showList();
                Log.d("abc","2" + String.valueOf(noticeListView));
                Log.d("abc","5" + String.valueOf(noticeList));
                Log.d("abc","6" + String.valueOf(noticeAdapter));
                noticeListView.setAdapter(noticeAdapter);
                Log.d("abc","3");
                noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> noticeHash= new HashMap<String, String>();
                        noticeHash = noticeList.get(position);
                        int noticeimg = imgs[position];
                        Intent intentBD = new Intent(view.getContext(), NoticeDetailActivity.class);
                        intentBD.putExtra("noticeImg",noticeimg);
                        intentBD.putExtra("noticeHash", noticeHash);
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
            notice = jsonobj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<notice.length();i++){
                JSONObject c = notice.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String user = c.getString(TAG_USER);
                String date = c.getString(TAG_DATE);
                String view = c.getString(TAG_VIEW);
                String cont = c.getString(TAG_CONT);
                notices = new HashMap<String,String>();

                notices.put(TAG_ID,id);
                notices.put(TAG_TITLE,title);
                notices.put(TAG_USER,user);
                notices.put(TAG_DATE,date);
                notices.put(TAG_VIEW,view);
                notices.put(TAG_CONT,cont);
                noticeList.add(notices);
                Log.d("abc","1:"+noticeList);
                noticeAdapter = new SimpleAdapter(
                        NoticeInfoActivity.this, noticeList, R.layout.cell_notice,
                        new String[]{TAG_TITLE,TAG_USER,TAG_DATE},
                        new int[]{R.id.notice_cell_title, R.id.notice_cell_name, R.id.notice_cell_date}
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
        getMenuInflater().inflate(R.menu.notice_info, menu);
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

    ImageButton.OnClickListener listener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.modify_notice_button:
                    Log.d("abc","aaa");
                    myintent = new Intent(getApplicationContext(), NoticeModifyActivity.class);
                    startActivity(myintent);
                    break;

            }
        }
    };
}