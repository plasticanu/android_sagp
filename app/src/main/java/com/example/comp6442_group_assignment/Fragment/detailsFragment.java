package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6442_group_assignment.Comment;
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
 * Use the {@link detailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public detailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment detailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static detailsFragment newInstance(String param1, String param2) {
        detailsFragment fragment = new detailsFragment();
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
     * A override onCreateView method handle the details fragment.
     * It gets bundle passed from recyclerView Item.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getParentFragmentManager().setFragmentResultListener("dataForm1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Post post = (Post) result.getSerializable("post");

                //receive information from the bundle.
                TextView details_user,details_date,details_content,details_like_count,details_commen_count;
                ListView details_comments;
                List<Comment> comments = post.getComments();
                ArrayList<String> comments_String = new ArrayList<>();
                ImageButton details_delete,details_edit;


                details_user = getActivity().findViewById(R.id.details_user);
                details_date = getActivity().findViewById(R.id.details_date);
                details_content = getActivity().findViewById(R.id.details_content);
                details_like_count = getActivity().findViewById(R.id.textView_like_count2);
                details_commen_count = getActivity().findViewById(R.id.textView_comment_count2);
                details_comments = getActivity().findViewById(R.id.details_comment);
                details_user.setText(post.getAuthor());
                details_date.setText((post.getCreateTime()));
                details_content.setText(post.getContent());
                details_like_count.setText(String.valueOf(post.getLikes().size()));
                details_commen_count.setText(String.valueOf(post.getComments().size()));

                for(int i = 0;i<comments.size();i++){
                    comments_String.add(comments.get(i).toString());
                }
                ArrayAdapter<String> commentAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        comments_String);
                details_comments.setAdapter(commentAdapter);


                //Handle the post deletion, User can only delete post that created by user self.
                details_delete = getActivity().findViewById(R.id.button_delete_post);
                if(post.getAuthor().compareTo(loginFragment.loggedUsername)==0){
                    details_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int postId = Integer.parseInt(post.getPostId());

                            System.out.println(postId);

                            homeFragment fragh = new homeFragment();
                            fragh.findFromRecyclerView(postId);

                            AsyncAction action = new AsyncAction();
                            homeFragment.cmd = "dp " +post.getPostId();
                            action.execute(homeFragment.cmd);

                            ((MainActivity) getActivity()).replaceFragment(new homeFragment());
                        }
                    });


                }else{
//                    details_delete.setEnabled(false);
                    details_delete.setVisibility(View.GONE);
                }

                details_edit = getActivity().findViewById(R.id.button_edit_post);
                if(post.getAuthor().compareTo(loginFragment.loggedUsername)==0){
                    details_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int postId = Integer.parseInt(post.getPostId());
                            String postContent = post.getContent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("postId",postId);
                            bundle.putString("postContent",postContent);
                            getParentFragmentManager().setFragmentResult("editForm1",bundle);

                            ((MainActivity) getActivity()).replaceFragment(new editFragment());

//
//                            System.out.println(postId);
//
//                            homeFragment fragh = new homeFragment();
//                            fragh.findFromRecyclerView(postId);
////
//                            AsyncAction action = new AsyncAction();
//                            homeFragment.cmd = "ep " +post.getPostId()+" "+;
//                            action.execute(homeFragment.cmd);
//
//                            ((MainActivity) getActivity()).replaceFragment(new homeFragment());
                        }
                    });


                }else{
//                    details_delete.setEnabled(false);
                    details_edit.setVisibility(View.GONE);
                }



            }
        });
        return inflater.inflate(R.layout.fragment_details, container, false);
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

        }
    }
}