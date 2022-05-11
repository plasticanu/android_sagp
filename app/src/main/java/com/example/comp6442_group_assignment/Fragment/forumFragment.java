package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.comp6442_group_assignment.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public forumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static forumFragment newInstance(String param1, String param2) {
        forumFragment fragment = new forumFragment();
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
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.button_connect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncAction().execute();


            }

        });


    }


    private class AsyncAction extends AsyncTask<String, Void, String> {
        public Socket socket = null;
        public InputStreamReader inputStreamReader = null;
        public OutputStreamWriter outputStreamWriter = null;
        public BufferedReader bufferedReader = null;
        public BufferedWriter bufferedWriter = null;
        protected String doInBackground(String... args) {



            try {
                socket = new Socket("10.0.2.2", 6060);
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

                bufferedReader = new BufferedReader(inputStreamReader);
                bufferedWriter = new BufferedWriter(outputStreamWriter);

//                Scanner scanner = new Scanner(System.in); // for user input

                Button login = getActivity().findViewById(R.id.button_login);
                Button logout = getActivity().findViewById(R.id.button_logout);

                while (true) {

//                    String input = scanner.nextLine();
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                bufferedWriter.write("li user1 qwerty");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                bufferedWriter.write("lo");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    bufferedWriter.write("");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    System.out.println("Server: " + bufferedReader.readLine());


                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStreamWriter != null) {
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

//            return null;//returns what you want to pass to the onPostExecute()
            return null;
        }

        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground


        }
    }
}