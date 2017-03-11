package hsy.barmiapp_android_third.recipe;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hsy.barmiapp_android_third.R;
import hsy.barmiapp_android_third.develope.DevelopeActivity;
import hsy.barmiapp_android_third.notice.NoticeInfoActivity;
import hsy.barmiapp_android_third.privacy.PrivacyInfoActivity;

/**
 * Created by hsy on 2016. 12. 29..
 */

public class RecipeInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Intent myintent;

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("바르미");

        ViewPager viewPager = (ViewPager)findViewById(R.id.pager_recipe);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapterViewPager = new MyRecipePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs_recipe);
        tabLayout.setupWithViewPager(viewPager);
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
                myintent = new Intent(RecipeInfoActivity.this, PopularActivity.class);
                myintent.putExtra("filterHash", s);
                startActivity(myintent);

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

    private static class MyRecipePagerAdapter extends FragmentPagerAdapter {
        private final static int NUM_ITEMS = 3;

        SearchActivity searchActivity = null;
        RecentActivity recentActivity = null;
        PopularActivity popularActivity = null;
        FavoriteActivity favoriteActicity = null;

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public MyRecipePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "일반검색";
                case 1:
                    return "최근검색";
                case 2:
                    return "즐겨찾기";
//                case 3:
//                    return "인기검색";
                default:
                    return null;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    if (searchActivity == null){
                        searchActivity = SearchActivity.newInstance(0);
                        return searchActivity;
                    }else {
                        return searchActivity;
                    }

                case 1:
                    if (recentActivity == null){
                        recentActivity = RecentActivity.newInstance(0);
                        return recentActivity;
                    }else {
                        return recentActivity;
                    }

                case 2:
                    if (favoriteActicity == null){
                        favoriteActicity = FavoriteActivity.newInstance(1);
                        return favoriteActicity;
                    }else {
                        return favoriteActicity;
                    }

//                case 4:
//                    if (popularActivity == null){
//                        popularActivity = PopularActivity.newInstance(2);
//                        return popularActivity;
//                    }else {
//                        return popularActivity;
//                    }
                default:
                    return null;
            }
        }
    }

}
