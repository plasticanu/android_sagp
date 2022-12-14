package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.Post_RecyclerViewAdapter;
import com.example.comp6442_group_assignment.R;
import com.example.comp6442_group_assignment.RecyclerViewInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static List<Post> postModels= null;
    private static LinearLayoutManager linearLayoutManager;
    private static RecyclerView recyclerView;;
    public static Post_RecyclerViewAdapter adapter;
    private EditText searchBar;
    private Button searchButton;
    public static boolean isSearching = false;
    public static int lastVisitPosition;
    public static boolean accessFromSearch = false;

    public static String itemId;

    public searchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchFragment newInstance(String param1, String param2) {
        searchFragment fragment = new searchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSearch();
        setupRecyclerView();

    }

    /**
     * A Custom method used to setup everything,
     * that a search needs.
     * @Author Jiyuan Chen u7055573
     */
    private void setUpSearch(){


        searchButton = getActivity().findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar = getActivity().findViewById(R.id.search_bar);
                String searchContent = searchBar.getText().toString();

                if(loginFragment.isLogged==false){
                    Toast.makeText(getActivity(), "Please login first!", Toast.LENGTH_SHORT).show();
                }else {

                    if (postModels != null) {
                        postModels.clear();
                        adapter.notifyDataSetChanged();
                    }

                    AsyncAction action = new AsyncAction();
                    homeFragment.cmd = "sr " + searchContent;
                    action.execute(homeFragment.cmd);
                }
            }
        });

    }


    /**
     * A Custom method used to setup everything,
     * that a RecyclerView needs.
     * @Author Jiyuan Chen u7055573
     */
    private void setupRecyclerView(){
        recyclerView = getActivity().findViewById(R.id.mRecyclerView);
        //custom recyclerView adapter.
        //This onItemClick used to pass bundle to details page.
        adapter = new Post_RecyclerViewAdapter(getActivity(), postModels, new RecyclerViewInterface() {
            /**
             * An override method for a custom RecyclerViewInterface.
             * Used to handle the onItemClick for recyclerView Items.
             * User will be redirect to details fragment.
             * @Author Jiyuan Chen u7055573
             */
            @Override
            public void onItemClick(int position) {
                ((MainActivity) getActivity()).replaceFragment(new detailsFragment());
                accessFromSearch = true;
                Bundle bundle = new Bundle();
                Post post = postModels.get(position);
                bundle.putSerializable("post",post);
                getParentFragmentManager().setFragmentResult("dataForm1",bundle);
            }
            /**
             * An override method for a custom RecyclerViewInterface.
             * when user clicks the like button in recyclerView item.
             * user will request a like post action. user can like the post,
             * if user is logged in, user is not liking owned post and user has not
             * already liked the post.
             * @Author Jiyuan Chen u7055573
             */
            @Override
            public void onLikeClick(int position) {
                if(loginFragment.isLogged == true){
                    Toast.makeText(getActivity(), "Like from user: "+loginFragment.loggedUsername, Toast.LENGTH_SHORT).show();
                    accessFromSearch = true;
                    Post post = postModels.get(position);
                    itemId = post.getPostId();
                    String postId = post.getPostId();
                    AsyncAction action = new AsyncAction();
                    homeFragment.cmd = "lp "+postId;
                    action.execute(homeFragment.cmd);
                }else{
                    Toast.makeText(getActivity(), "Please login first!", Toast.LENGTH_SHORT).show();
                }

            }

            //disable loadmore in search,because all search results
            //are displayed one time
            @Override
            public void onLoadMore(int position) {

            }
            /**
             * An override method for a custom RecyclerViewInterface.
             * When user click go top, the RecyclerView will change scroll
             * to the top.
             * @Author Jiyuan Chen u7055573
             */
            @Override
            public void onGoTop(int position) {
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(0);
            }
        });

        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

        lastVisitPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        System.out.println("State pause...");
    }

    /**
     * A override onResume method.
     * Use to save the fragment state,
     * and initialize when the fragment created first time.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public void onResume() {
        super.onResume();
        if(postModels == null){
            System.out.println("first initial...");

        }else{
            System.out.println("post model is not null on resume");
            setupRecyclerView();
            adapter.notifyDataSetChanged();
            adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastVisitPosition);

        }

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
            //flag that indicate if its a success searching action.
            //if success, update the recyclerView in searchFragment
            if(result.split(";")[0].compareTo("srs")==0){
                homeFragment fragh = new homeFragment();
                isSearching = true;
                postModels = fragh.setupPost(result);
                isSearching = false;
                setupRecyclerView();
            }else if (homeFragment.cmd.substring(0,2).compareTo("lp")==0){
                if (result.substring(0,3).compareTo("lps")==0) {

                    for (int i = 0; i < postModels.size(); i++) {
                        if (Integer.parseInt(postModels.get(i).getPostId()) == Integer.parseInt(itemId)) {
                            postModels.get(i).getLikes().add(loginFragment.loggedUsername);
                            adapter.notifyItemChanged(i);
                        }
                    }
                    if(homeFragment.postModels!=null){
                        for (int i = 0; i <homeFragment.postModels.size(); i++) {
                            if (Integer.parseInt(homeFragment.postModels.get(i).getPostId()) == Integer.parseInt(itemId)) {
                                homeFragment.postModels.get(i).getLikes().add(loginFragment.loggedUsername);
                                homeFragment.adapter.notifyItemChanged(i);
                            }
                        }
                    }

                }
            }
            System.out.println(result);
        }
    }
}