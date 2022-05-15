package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link postFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class postFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Post post;

    public postFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment postFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static postFragment newInstance(String param1, String param2) {
        postFragment fragment = new postFragment();
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
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    /**
     * A override method use to perform new post action.
     * User can perform new post action, if and only if
     * User is logged in. If User is not logged in, user will
     * be redirect to the login page.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button newPost = view.findViewById(R.id.button_new_post);
        EditText postText = view.findViewById(R.id.editText_post);

        newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postString = postText.getText().toString();
                if(!postString.isEmpty()){

                    AsyncAction action = new AsyncAction();
                    homeFragment.cmd = "cp " +postString;
                    action.execute(homeFragment.cmd);


                    ((MainActivity) getActivity()).replaceFragment(new homeFragment());
                }
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
            System.out.println(result);

            //check from server's response. Check if new post created successful.
            if(result.split(";")[0].compareTo("cps")==0){
                homeFragment fragh = new homeFragment();
                post = fragh.setupPost(result).get(0);
                fragh.updateRecyclerView(post);

            }
        }
    }
}