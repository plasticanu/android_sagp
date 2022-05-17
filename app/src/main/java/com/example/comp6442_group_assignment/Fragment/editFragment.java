package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

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
 * Use the {@link editFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int editPostId;
    private String editPostContent;


    public editFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editFragment newInstance(String param1, String param2) {
        editFragment fragment = new editFragment();
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
    /**
     * A override onCreateView method handle the edit fragment.
     * It gets bundle passed from details fragment, and then
     * pass to edit fragment.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getParentFragmentManager().setFragmentResultListener("editForm1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int postId = result.getInt("postId");
                String postContent = result.getString("postContent");

                String postIdString = String.format("%08d",postId);

                EditText editText;
                Button button_save_edit;

                editText=getActivity().findViewById(R.id.editText_edit_post);
                button_save_edit = getActivity().findViewById(R.id.button_save_edit);

                editText.setText(postContent);

                button_save_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String editContent = editText.getText().toString();
                        editPostId = Integer.parseInt(postIdString);
                        editPostContent = editContent.replaceAll("[-+^:\\;\\|]","");

                        AsyncAction action = new AsyncAction();
                        homeFragment.cmd = "ep " +postIdString+" "+editPostContent;
                        action.execute(homeFragment.cmd);

                    }
                });

            }
        });




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
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
         * get edit response from the server to check if edit success.
         * If success, update the edited post.
         * @Author Jiyuan Chen u7055573
         */
        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground
            System.out.println(result);

            //check if edit successful
            //if success, update local recyclerView.
            if(result.split(";")[0].compareTo("eps")==0){
                homeFragment fragh = new homeFragment();
                //update the local recyclerView with edited content
                fragh.updateEditRecyclerView(editPostId,editPostContent);
                ((MainActivity) getActivity()).replaceFragment(new homeFragment());

            }else{
                editPostId=0;
                editPostContent="";
            }

        }
    }
}