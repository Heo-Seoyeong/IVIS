package hsy.barmiapp_android_third.notice;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

import java.util.HashMap;

import hsy.barmiapp_android_third.R;

public class NoticeDetailActivity extends AppCompatActivity {


    private TextView txtID;
    private String txtName;

    private ImageView noticeImage;
    private TextView noticeTitle;
    private TextView noticeName;
    private TextView noticeDate;
    private TextView noticeNumviews;
    private TextView noticeContent;

    static HashMap<String, String> hashMap = null;
    int noticeImg;
    String noticeId;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        intent = getIntent();
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("noticeHash");
        noticeImg = (int) intent.getSerializableExtra("noticeImg");


        noticeImage = (ImageView) findViewById(R.id.notice_detail_profileimg);
        noticeImage.setImageResource(noticeImg);

        noticeTitle = (TextView) findViewById(R.id.notice_detail_title);
        noticeTitle.setText(hashMap.get("title"));
        noticeName = (TextView) findViewById(R.id.notice_detail_name);
        noticeName.setText(hashMap.get("user"));
        noticeDate = (TextView) findViewById(R.id.notice_detail_date);
        noticeDate.setText(hashMap.get("date"));
        noticeNumviews = (TextView) findViewById(R.id.notice_detail_numviews);
        noticeNumviews.setText(hashMap.get("view"));
        noticeContent = (TextView) findViewById(R.id.notice_detail_content);
        noticeContent.setText(hashMap.get("cont"));


        //txtID = (TextView) findViewById(R.id.id);
        //txtID.setText(hashMap.get("id"));

        txtName= hashMap.get("user");
        getSupportActionBar().setTitle((CharSequence) txtName);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notice_detail, menu);
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

}
