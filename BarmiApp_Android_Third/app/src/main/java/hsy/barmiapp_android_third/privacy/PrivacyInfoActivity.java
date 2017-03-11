package hsy.barmiapp_android_third.privacy;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
import hsy.barmiapp_android_third.intro.LoginActivity;
import hsy.barmiapp_android_third.notice.NoticeInfoActivity;
import hsy.barmiapp_android_third.recipe.RecipeInfoActivity;

public class PrivacyInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<HashMap<String, String>> personList = null;
    JSONArray person = null;
    String myJSON;
    HashMap<String,String> persons;

    private Intent myintent;
    private ImageButton modifyButton;
    private Button logoutButton;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID="id";
    private static final String TAG_NAME="name";
    private static final String TAG_MAN="man";
    private static final String TAG_BIRTH="birth";
    private static final String TAG_AGE="age";
    private static final String TAG_WEIGHT="weight";
    private static final String TAG_HEIGHT="height";
    private static final String TAG_BLOOD="blood";

    private TextView txtName;
    private TextView txtMan;
    private TextView txtBirth;
    private TextView txtAge;
    private TextView txtWeight;
    private TextView txtHeight;
    private TextView txtBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_info);
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

        modifyButton = (ImageButton) findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(listener);

        logoutButton = (Button) findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(listener);

        personList = new ArrayList<HashMap<String,String>>();
        getData("http://35.161.222.253/person_display.php");
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

                showList();
                Log.d("abc", "second"+String.valueOf(personList));

                txtName = (TextView)findViewById(R.id.textName);
                txtMan = (TextView)findViewById(R.id.textMan);
                txtBirth = (TextView)findViewById(R.id.textBirth);
                txtAge = (TextView)findViewById(R.id.textAge);
                txtWeight = (TextView)findViewById(R.id.textWeight);
                txtHeight = (TextView)findViewById(R.id.textHeight);
                txtBlood = (TextView)findViewById(R.id.textBlood);

                txtName.setText(personList.get(0).get("name"));
                txtMan.setText(personList.get(0).get("man"));
                txtBirth.setText(personList.get(0).get("birth"));
                txtAge.setText(personList.get(0).get("age"));
                txtWeight.setText(personList.get(0).get("weight"));
                txtHeight.setText(personList.get(0).get("height"));
                txtBlood.setText(personList.get(0).get("blood"));

                //     personText(personList);

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    // 서버데이터를 Jason으로 변환
    public void showList(){
        Log.d("abc", "first"+String.valueOf(myJSON));
        try {
            JSONObject jsonobj = new JSONObject(myJSON);
            person = jsonobj.getJSONArray(TAG_RESULTS);

            Log.d("abc","person.length = "+person.length());
            for(int i=0;i<person.length();i++){
                // for(int i=0;i<1;i++){
                JSONObject c = person.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String man = c.getString(TAG_MAN);
                String birth = c.getString(TAG_BIRTH);
                String age = c.getString(TAG_AGE);
                String weight = c.getString(TAG_WEIGHT);
                String height = c.getString(TAG_HEIGHT);
                String blood = c.getString(TAG_BLOOD);

                persons = new HashMap<String,String>();

                persons.put(TAG_ID,id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_MAN,man);
                persons.put(TAG_BIRTH,birth);
                persons.put(TAG_AGE,age);
                persons.put(TAG_WEIGHT,weight);
                persons.put(TAG_HEIGHT,height);
                persons.put(TAG_BLOOD,blood);

                personList.add(persons);

                Log.d("abc","personList : "+String.valueOf(personList));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void personText(ArrayList<HashMap<String, String>> personList1){

        // Log.d("abc", String.valueOf(personList1));

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
        getMenuInflater().inflate(R.menu.privacy_info, menu);
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

    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    ImageButton.OnClickListener listener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.modify_button:
                    myintent = new Intent(getApplicationContext(), PrivacyModifyActivity.class);
                    startActivity(myintent);
                    break;
                case R.id.logout_btn:
                    onClickLogout();
                    break;

            }
        }
    };
}
