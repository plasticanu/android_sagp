package com.example.comp6442_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.comp6442_group_assignment.Fragment.forumFragment;
import com.example.comp6442_group_assignment.Fragment.homeFragment;
import com.example.comp6442_group_assignment.Fragment.loginFragment;
import com.example.comp6442_group_assignment.Fragment.profileFragment;
import com.example.comp6442_group_assignment.Fragment.searchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public static boolean isLogged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Home");
        myNavigation();
    }


    /**
     * A Custom fragment navigation controller.
     * Use to change the fragment view.
     * @Author Jiyuan Chen u7055573
     */
    private void myNavigation(){
        //Initialize the bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttomNavigationView);

        //Navigation bar click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                ActionBar actionBar = getSupportActionBar();

                //change fragment view depends on the menu item, then change the action bar title.
                switch (id){
                    case R.id.homeFragment:
                        replaceFragment(new homeFragment());
                        actionBar.setTitle("Home");
                        break;
                    case R.id.forumFragment:
                        replaceFragment(new forumFragment());
                        actionBar.setTitle("Forum");
                        break;
                    case R.id.searchFragment:
                        replaceFragment(new searchFragment());
                        actionBar.setTitle("Search");
                        break;
                    case R.id.profileFragment:
                        if(isLogged == false){
                            replaceFragment(new loginFragment());
                            actionBar.setTitle("Login");
                            break;
                        }else{
                            replaceFragment(new profileFragment());
                            actionBar.setTitle("My Profile");
                            break;
                        }
                    default:
                        return true;
                };


                return true;
            }
        });
    }


    /**
     * A method used to replace fragment view.
     * @Author Jiyuan Chen u7055573
     * @param fragment new fragment that you want to change to
     */
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
        fragmentTransaction.commit();
    }

    /**
     * A method used to change the current action bar title.
     * You can call it from fragment activity
     * @Author Jiyuan Chen u7055573
     * @param title a title text
     */
    public void setActionBarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

}