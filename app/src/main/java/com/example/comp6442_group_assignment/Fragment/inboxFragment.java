package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp6442_group_assignment.Comment;
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
 * Use the {@link inboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inboxFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayAdapter<String> notiAdapter;
    private ListView inbox;

    private boolean firstInitial = false;
    public inboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static inboxFragment newInstance(String param1, String param2) {
        inboxFragment fragment = new inboxFragment();
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
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstInitial = true;
        setUpNotification();
    }

    public void initialNotification(){
        AsyncAction action = new AsyncAction();
        homeFragment.cmd = "un";
        action.execute(homeFragment.cmd);
    }


    /**
     * A method for setup the user notifications.
     * The return list will only show when user got >=2 notifications
     * @Author Jiyuan Chen u7055573
     */
    private void setUpNotification(){
        inbox = getActivity().findViewById(R.id.listView_inbox);

        AsyncAction action = new AsyncAction();
        homeFragment.cmd = "un";
        action.execute(homeFragment.cmd);


        if(loginFragment.loggedNotifications.size()>1){

            notiAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    loginFragment.loggedNotifications);
            inbox.setAdapter(notiAdapter);
            notiAdapter.notifyDataSetChanged();
        }

        Button clearButton = getActivity().findViewById(R.id.button_clear_notification);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncAction action = new AsyncAction();
                homeFragment.cmd = "cn";
                action.execute(homeFragment.cmd);
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
         * An override onPostExecute method, to check if get notification success
         * update ui and local inbox listview.
         * @Author Jiyuan Chen u7055573
         */
        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground
            System.out.println(result);
            if (result.substring(0,3).compareTo("uns")==0){


                String[] tempString = result.split(";");

                if(tempString.length-1!=loginFragment.loggedNotifications.size()){
                    loginFragment.loggedNotifications.clear();
                    for(int i =1;i<tempString.length;i++){
                        loginFragment.loggedNotifications.add(tempString[i]);
                    }
                }

                if(firstInitial==true) {
                    if (loginFragment.loggedNotifications.size() > 1) {

                        notiAdapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_list_item_1,
                                loginFragment.loggedNotifications);
                        inbox.setAdapter(notiAdapter);
                        notiAdapter.notifyDataSetChanged();
                    }
                }
                System.out.println(loginFragment.loggedNotifications.toString());


            }

            if (result.substring(0,3).compareTo("cns")==0){

                loginFragment.loggedNotifications.clear();

                notiAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        loginFragment.loggedNotifications);
                inbox.setAdapter(notiAdapter);
                notiAdapter.notifyDataSetChanged();
                System.out.println(loginFragment.loggedNotifications.toString());
            }
        }


    }
}