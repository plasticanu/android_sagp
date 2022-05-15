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

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link registerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class registerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public registerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment registerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static registerFragment newInstance(String param1, String param2) {
        registerFragment fragment = new registerFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
    /**
     *
     * A override method to handle register action.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText regi_username,regi_password,regi_email,regi_firstname,regi_lastname,regi_phone;

        regi_username = view.findViewById(R.id.regi_username);
        regi_password = view.findViewById(R.id.regi_password);
        regi_email = view.findViewById(R.id.regi_email);
        regi_firstname = view.findViewById(R.id.regi_firstname);
        regi_lastname = view.findViewById(R.id.regi_lastname);
        regi_phone = view.findViewById(R.id.regi_phone);

        Button button_register = view.findViewById(R.id.regi_button);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String username = regi_username.getText().toString();
                String password = regi_password.getText().toString();
                String email = regi_email.getText().toString();
                String firstname = regi_firstname.getText().toString();
                String lastname = regi_lastname.getText().toString();
                String phone = regi_phone.getText().toString();


                //send register information to server.
                if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty()&& !firstname.isEmpty()&& !lastname.isEmpty()&& !phone.isEmpty()){
                    //li user1 qwerty
                    AsyncAction action = new AsyncAction();
                    homeFragment.cmd = "rg " +username+" "+ password+" "+ email+" "+ firstname+" "+ lastname+" "+ phone;
                    action.execute(homeFragment.cmd);
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


            //Check if registration success.
            //If registration done successful, then save the user information.
            //As logged in details.
            if(result.split(";")[0].compareTo("rjs")==0){
                ((MainActivity) getActivity()).replaceFragment(new profileFragment());

                loginFragment.isLogged=true;
                loginFragment.loggedUsername= result.split(";")[1];
                loginFragment.loggedFullname= result.split(";")[2]+" "+result.split(";")[3];
                loginFragment.loggedEmail= result.split(";")[4];
                loginFragment.loggedPhone= result.split(";")[5];

                System.out.println(loginFragment.loggedUsername);
                System.out.println(loginFragment.loggedFullname);
                System.out.println(loginFragment.loggedEmail);
                System.out.println(loginFragment.loggedPhone);
            }

        }
    }
}