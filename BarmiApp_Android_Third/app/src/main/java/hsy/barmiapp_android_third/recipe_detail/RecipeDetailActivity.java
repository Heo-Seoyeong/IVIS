package hsy.barmiapp_android_third.recipe_detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import hsy.barmiapp_android_third.R;
import hsy.barmiapp_android_third.notice.NoticeModifyActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView txtID;
    private String txtName;

    private ImageView recipeImage;

    private Button favoritButton;
    private ImageButton matrecButton;
    private ImageButton nutriinfoButton;

    int mCurrentFragmentIndex;
    public final static int FRAGMENT_ONE = 0;
    public final static int FRAGMENT_TWO = 1;
    static HashMap<String, String> hashMap = null;
    int recipeImg;
    String recipeId;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("recipeHash");
        recipeImg = (int) intent.getSerializableExtra("recipeImg");

        String id = hashMap.get("id");
        String name = hashMap.get("name");
        String material = hashMap.get("material");
        String recipe = hashMap.get("recipe");
        String time = hashMap.get("time");
        String calorie = hashMap.get("calorie");

        insertToDatabase(id, name, material, recipe, time, calorie, "b");

        recipeImage = (ImageView) findViewById(R.id.img_cook);
        recipeImage.setImageResource(recipeImg);

        //txtID = (TextView) findViewById(R.id.id);
        //txtID.setText(hashMap.get("id"));

        txtName= hashMap.get("name");
        getSupportActionBar().setTitle((CharSequence) txtName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        favoritButton = (Button) findViewById(R.id.favorit_button);
        favoritButton.setOnClickListener(listener);
        matrecButton = (ImageButton) findViewById(R.id.btn_matrec);
        matrecButton.setOnClickListener(listener);
        nutriinfoButton = (ImageButton) findViewById(R.id.btn_nutriinfo);
        nutriinfoButton.setOnClickListener(listener);

        mCurrentFragmentIndex = FRAGMENT_ONE;
        fragmentReplace(mCurrentFragmentIndex);
    }

    public void fragmentReplace(int reqNewFragmentIndex) {

        Fragment newFragment = null;

        Log.d("RecipeDetailActivity", "fragmentReplace " + reqNewFragmentIndex);

        newFragment = getFragment(reqNewFragmentIndex);

        // replace fragment
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.ll_fragment, newFragment);

        // Commit the transaction
        transaction.commit();

    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case FRAGMENT_ONE:
                newFragment = new MatrecFragmentR();
                break;
            case FRAGMENT_TWO:
                newFragment = new NutriFragmentR();
                break;
            default:
                Log.d("RecipeDetailActivity", "Unhandle case");
                break;
        }

        return newFragment;
    }

    public static class MatrecFragmentR extends Fragment{
        private TextView txtMaterial;
        private  TextView txtRecipe;
        private  TextView txtTime;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_matrec, container, false);


            txtMaterial = (TextView) v.findViewById(R.id.frag_material);
            txtMaterial.setText(hashMap.get("material"));

            txtRecipe = (TextView) v.findViewById(R.id.frag_recipe);
            txtRecipe.setText(hashMap.get("recipe"));

            txtTime = (TextView) v.findViewById(R.id.frag_time);
            txtTime.setText(hashMap.get("time")+"분");

//        Button button = (Button) v.findViewById(R.id.bt_ok);
//        button.setOnClickListener(this);

            return v;
        }
    }

    public static class NutriFragmentR extends Fragment{

        private TextView txtCalorie;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_nutri, container, false);

            txtCalorie = (TextView) v.findViewById(R.id.frag_calorie);
            txtCalorie.setText(hashMap.get("calorie")+"Kcal");


//        Button button = (Button) v.findViewById(R.id.bt_ok);
//        button.setOnClickListener(this);

            return v;
        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recipe_detail, menu);
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

    ImageButton.OnClickListener listener = new Button.OnClickListener()
    {
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.favorit_button:
                    AlertDialog.Builder alert = new AlertDialog.Builder(RecipeDetailActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("즐겨찾기에 추가 되었습니다");
                    alert.show();

                    String id = hashMap.get("id");
                    String name = hashMap.get("name");
                    String material = hashMap.get("material");
                    String recipe = hashMap.get("recipe");
                    String time = hashMap.get("time");
                    String calorie = hashMap.get("calorie");

                    insertToDatabase(id, name, material, recipe, time, calorie, "a");

                    break;
                case R.id.btn_matrec:
                    mCurrentFragmentIndex = FRAGMENT_ONE;
                    fragmentReplace(mCurrentFragmentIndex);
                    break;
                case R.id.btn_nutriinfo:
                    mCurrentFragmentIndex = FRAGMENT_TWO;
                    fragmentReplace(mCurrentFragmentIndex);
                    break;

            }
        }
    };

    private void insertToDatabase(String id, String name, String material, String recipe, String time, String calorie, String bool_a){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RecipeDetailActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String id = (String)params[0];
                    String name = (String)params[1];
                    String material = (String)params[2];
                    String recipe = (String)params[3];
                    String time = (String)params[4];
                    String calorie = (String)params[5];
                    String bool_a = (String)params[6];

                    String link="http://35.161.222.253/recipe_recent.php";

                    Log.d("abc",bool_a);

                    if (bool_a == "a"){  link="http://35.161.222.253/recipe_popular.php";}

                    Log.d("abc",link);

                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("material", "UTF-8") + "=" + URLEncoder.encode(material, "UTF-8");
                    data += "&" + URLEncoder.encode("recipe", "UTF-8") + "=" + URLEncoder.encode(recipe, "UTF-8");
                    data += "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    data += "&" + URLEncoder.encode("calorie", "UTF-8") + "=" + URLEncoder.encode(calorie, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(id, name, material, recipe, time, calorie, bool_a);
    }
}
