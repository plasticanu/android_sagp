package com.example.comp6442_group_assignment.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.R;

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
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    /**
     * Userd to handle the profile fragment.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView userName,userEmail,userFullname;
        Button loggout,inbox,deleteUser;

        userName = view.findViewById(R.id.profile_username);
        userName.setText(loginFragment.loggedUsername);

        userEmail = view.findViewById(R.id.profile_email);
        userEmail.setText(loginFragment.loggedEmail);

        userFullname = view.findViewById(R.id.profile_fullname);
        userFullname.setText(loginFragment.loggedFullname);

        loggout = view.findViewById(R.id.button_logout);
        inbox = view.findViewById(R.id.button_inbox);
        //define a click listener to perform loggout action
        loggout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncAction action = new AsyncAction();
                homeFragment.cmd = "lo";
                action.execute(homeFragment.cmd);
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(new inboxFragment());
            }
        });

        deleteUser = getActivity().findViewById(R.id.button_delete_user);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Are you sure you want to delete account?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "User deleted!", Toast.LENGTH_SHORT).show();
                                AsyncAction action = new AsyncAction();
                                homeFragment.cmd = "da";
                                action.execute(homeFragment.cmd);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
    }



    /**
     * An AsyncTask class, used to make connection and,
     * communicate with server.
     * @Author Jiyuan Chen u7055573, Support by: Peicheng Liu u7294212
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

        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground

            if (result.substring(0,3).compareTo("los")==0){
                //reset the user information
                loginFragment.isLogged=false;
                loginFragment.loggedUsername = "";
                loginFragment.loggedFullname = "";
                loginFragment.loggedEmail = "";
                loginFragment.loggedPhone = "";
                loginFragment.loggedNotifications=new ArrayList<>();
                //disable the profile fragment, return to login page.
                ((MainActivity) getActivity()).replaceFragment(new loginFragment());
            }
            if (result.substring(0,3).compareTo("das")==0){
                Toast.makeText(getActivity(), "User Deleted!", Toast.LENGTH_SHORT).show();
                //reset the user information
                loginFragment.isLogged=false;
                loginFragment.loggedUsername = "";
                loginFragment.loggedFullname = "";
                loginFragment.loggedEmail = "";
                loginFragment.loggedPhone = "";
                loginFragment.loggedNotifications=new ArrayList<>();
                //disable the profile fragment, return to login page.
                ((MainActivity) getActivity()).replaceFragment(new loginFragment());
            }
            if(result.substring(0,3).compareTo("daf")==0){
                Toast.makeText(getActivity(), "Delete User Failed!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}