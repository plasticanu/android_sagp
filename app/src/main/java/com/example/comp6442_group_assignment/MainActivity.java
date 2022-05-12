package com.example.comp6442_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.comp6442_group_assignment.Fragment.homeFragment;
import com.example.comp6442_group_assignment.Fragment.loginFragment;
import com.example.comp6442_group_assignment.Fragment.profileFragment;
import com.example.comp6442_group_assignment.Fragment.searchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Author Zhidong Piao u7139999
    // This field let other classes (non activity class) access the application context.
    // For accessing the resource and assets folder.
    private static Context applicationContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applicationContext = getApplicationContext();
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
//                ActionBar actionBar = getSupportActionBar();

                //change fragment view depends on the menu item, then change the action bar title.
                switch (id){
                    case R.id.homeFragment:
                        replaceFragment(new homeFragment());
                        break;
                    case R.id.searchFragment:
                        replaceFragment(new searchFragment());
                        break;
                    case R.id.profileFragment:
                        if(loginFragment.isLogged == false){
                            replaceFragment(new loginFragment());
                            break;
                        }else{
                            replaceFragment(new profileFragment());
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
    public void replaceFragment(Fragment fragment){
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

    /**
     * A method used to read data from a file in the assets folder
     * @Author Jun Cheng Zhang u7297753
     * @param fileName the file's name
     * @return the data in the file
     */
    public String loadData(String fileName){
        String data = "";
        try {
            InputStream inputStream = getAssets().open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            data = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public static Context getContext() {
        return applicationContext;
    }
}