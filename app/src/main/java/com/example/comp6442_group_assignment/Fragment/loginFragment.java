package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //user's state and logging state.
    public static boolean isLogged = false;
    public static String loggedUsername = "";
    public static String loggedFullname = "";
    public static String loggedEmail = "";
    public static String loggedPhone = "";
    public static List<String> loggedNotifications = new ArrayList<>();

    public loginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * A override method for checking user's login identity.
     * @Author Jiyuan Chen u7055573, Support by: Peicheng Liu u7294212
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText username,password;
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);


        //Take user's login identities, and then pass to server.
        Button loginButton = view.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userString = username.getText().toString();
                String passString = password.getText().toString();
                System.out.println(userString);
                System.out.println(passString);

                //check if username and password are not empty, then send
                //logging details to server.
                if(!userString.isEmpty() && !passString.isEmpty()){
                    AsyncAction action = new AsyncAction();
                    homeFragment.cmd = "li " +userString+" "+ passString;
                    action.execute(homeFragment.cmd);

                }

            }
        });


        //A button will take user to register page.
        Button regiButton = view.findViewById(R.id.button_register);
        regiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(new registerFragment());
            }
        });
    }




    /**
     * An AsyncTask class, used to make connection and,
     * communicate with server.
     * @Author Jiyuan Chen u7055573
     */
    private class AsyncAction extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... args) {

            //response used to store the message from the server.
            String response = "";

            try {
                if(homeFragment.socket == null){
                    homeFragment.socket = new Socket("10.0.2.2", 6060);
                }
                if(homeFragment.inputStreamReader == null){
                    homeFragment.inputStreamReader = new InputStreamReader(homeFragment.socket.getInputStream());
                }
                if(homeFragment.outputStreamWriter == null){
                    homeFragment.outputStreamWriter = new OutputStreamWriter(homeFragment.socket.getOutputStream());
                }
                if(homeFragment.bufferedReader == null){
                    homeFragment.bufferedReader = new BufferedReader(homeFragment.inputStreamReader);
                }
                if(homeFragment.bufferedWriter == null){
                    homeFragment.bufferedWriter = new BufferedWriter(homeFragment.outputStreamWriter);
                }

                homeFragment.bufferedWriter.write(args[0]);
                homeFragment.bufferedWriter.newLine();
                homeFragment.bufferedWriter.flush();
                response = homeFragment.bufferedReader.readLine();
                System.out.println("Server: " + response);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        /**
         * get login response from the server to check if logging success.
         * If success, update the current user's details, and logging state.
         * @Author Jiyuan Chen u7055573
         */
        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground

            //check the response from server to check if it contains
            //flag that indicate if its a success logging action.
            //If server return a success logging, then store user's details.
            if(result.split(";")[0].compareTo("lis")==0){
                ((MainActivity) getActivity()).replaceFragment(new profileFragment());
                isLogged=true;
                loggedUsername = result.split(";")[1];
                loggedFullname = result.split(";")[2]+" "+result.split(";")[3];
                loggedEmail = result.split(";")[4];
                loggedPhone = result.split(";")[5];

            }else{
                Toast.makeText(getActivity(), "Wrong Username or Password!", Toast.LENGTH_SHORT).show();
            }
            System.out.println(result);
        }
    }

}