package com.example.demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.demo.fragment.HowOldFragment;
import com.example.demo.fragment.MainFragment;
import com.example.demo.fragment.RecyclerViewFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Map<String, Fragment> fragments;
    FragmentManager fragmentManager;
    FloatingActionButton fab ;
    Toolbar toolbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setFragments(fragments.get("mainFragment"));

    }

    private void setFragments(Fragment mainFragment) {
        fragmentManager.beginTransaction().replace(R.id.id_framelayout, mainFragment).commit();
    }


    private void initViews() {
        fragmentManager = getSupportFragmentManager();
        fragments = new HashMap<String, Fragment>();

        MainFragment mainFragment = new MainFragment();
        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        HowOldFragment howoldFragment = new HowOldFragment();
        fragments.put("howoldFragment",howoldFragment);
        fragments.put("recyclerViewFragment", recyclerViewFragment);
        fragments.put("mainFragment", mainFragment);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //这里不能添加子Fragment的菜单项，否则，子Fragment的菜单项会失效

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_howold) {
            setFragments(fragments.get("howoldFragment"));
            fab.setVisibility(View.INVISIBLE);
            toolbar.setTitle("HowOld");
        }  else if (id == R.id.nav_recyclerview) {

            setFragments(fragments.get("recyclerViewFragment"));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            fab.setVisibility(View.VISIBLE);
            toolbar.setTitle("RecyclerView");

        } else if (id == R.id.nav_main) {

            setFragments(fragments.get("mainFragment"));
            fab.setVisibility(View.INVISIBLE);
            toolbar.setTitle("Main");
        }
        //fragmentManager.beginTransaction().replace(R.id.id_framelayout, fragments.get("mainFragment")).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
