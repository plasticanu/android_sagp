package com.example.comp6442_group_assignment.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.comp6442_group_assignment.Comment;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
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
    //global variables used to connect to server.
    public static Socket socket = null;
    public static InputStreamReader inputStreamReader = null;
    public static OutputStreamWriter outputStreamWriter = null;
    public static BufferedReader bufferedReader = null;
    public static BufferedWriter bufferedWriter = null;
    public static String cmd="";
    public static List<Post> postModels= null;
    private static LinearLayoutManager linearLayoutManager;
    private static RecyclerView recyclerView;;
    public static Post_RecyclerViewAdapter adapter;
    private static SwipeRefreshLayout swipeRefreshLayout;
    private static boolean isInitial = false;

    public static String itemId;
    public static int loadedNum;
    public static int lastVisitPosition;


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

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Use this to define the recyclerView in homeFragment.
     * This recyclerView display a card view contains Author of the post,
     * Date of thet post, content of the post and number of likes of the post.
     *
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //use to perform swipe to refresh action.
        //everytime you swipe you will get 10 older posts.
        swipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setupRecyclerView();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


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
            AsyncAction action = new AsyncAction();
            cmd = "hm 0";
            action.execute(cmd);
            loadedNum = 10;
        }else{
            System.out.println("post model is not null on resume");
            setupRecyclerView();
            adapter.notifyDataSetChanged();
            adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastVisitPosition);

        }

    }



    /**
     * Use this to setup the post models list.
     * Deconstruct the response from the server into Post object
     * then return it to the recyclerView.
     * @Author Jiyuan Chen u7055573
     */
    public List<Post> setupPost(String response){
        List<Post> pModels = new ArrayList<>();

        String regexC = "(?![^)(]*\\([^)(]*?\\)\\))\\|(?![^\\[]*\\])";
        String regexE = "(?![^)(]*\\([^)(]*?\\)\\))=(?![^\\[]*\\])";
        String regexCC = "(\\|)(?=(?:[^\\}]|\\{[^\\{]*\\})*$)";
        String regexS = "(?![^)(]*\\([^)(]*?\\)\\))\\;(?![^\\[]*\\])";

        String str = response;
        String[] newStr = str.split(regexS);
        for(int i = 1;i<newStr.length;i++){
            System.out.println(newStr[i]);
            String[] temp = newStr[i].substring(5,newStr[i].length()-1).split(regexC);


            String postId = temp[0].split(regexE)[1];
            postId=postId.substring(1,postId.length()-1);
            String content = temp[1].split(regexE)[1];
            content=content.substring(1,content.length()-1);
            String author = temp[2].split(regexE)[1];
            author=author.substring(1,author.length()-1);
            String createTime = temp[4].split(regexE)[1];
            createTime=createTime.substring(1,createTime.length()-1);
            String[] likeString = temp[3].split(regexE)[1].substring(1,temp[3].split(regexE)[1].length()-1).split(",");

            List<String> likes = new ArrayList<>();

            for (int j=0;j<likeString.length;j++){
                if(likeString[j].trim().compareTo("")!=0) {
                    likes.add(likeString[j].trim());
                }
            }
            String[] observersString = temp[6].split(regexE)[1].substring(1,temp[6].split(regexE)[1].length()-1).split(",");



            List<String> observers = new ArrayList<>();
            for(int k = 0;k<observersString.length;k++){
                observers.add(observersString[k].trim());
                System.out.println(observersString[k]);
            }



            String[] commentString;
            List<Comment> comments = new ArrayList<>();
            if(temp[5].split(regexE)[1].length()>2){
                commentString = temp[5].split(regexE)[1].substring(1,temp[5].split(regexE)[1].length()-1).split(regexS);
                for(int k=0;k<commentString.length;k++){
                    String newtempComment = commentString[k].substring(8,commentString[k].length()-1);
                    String[] oneComment = newtempComment.split(regexCC);

                    String tempContent = oneComment[0].split(regexE)[1].substring(1,oneComment[0].split(regexE)[1].length()-1);
                    String tempAuthor = oneComment[1].split(regexE)[1].substring(1,oneComment[1].split(regexE)[1].length()-1);
                    String tempTime = oneComment[2].split(regexE)[1].substring(1,oneComment[2].split(regexE)[1].length()-1);
                    Comment comment = new Comment(tempContent,tempAuthor,tempTime);
                    comments.add(comment);


                }

            }

            Post post = new Post(postId, content, author, likes, createTime, comments, observers); //FIXME: read observer
            pModels.add(post);
        }
        if(searchFragment.isSearching){
            return pModels;
        }else {
            List<Post> postCopy = pModels.subList(0, pModels.size());
            Collections.reverse(postCopy);
            return postCopy;
        }

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

                    Post post = postModels.get(position);
                    itemId = post.getPostId();
                    String postId = post.getPostId();
                    AsyncAction action = new AsyncAction();
                    cmd = "lp "+postId;
                    action.execute(cmd);
                }else{
                    Toast.makeText(getActivity(), "Please login first!", Toast.LENGTH_SHORT).show();
                }

            }

            /**
             * An override method for a custom RecyclerViewInterface.
             * when you click the button at the end of recyclerView,
             * client will request 10 older posts from server, then added it
             * to the end of exist recyclerView. Then update the variable
             * loadedNum by 10. Which means next time you click the loadMore Button,
             * user will get 10 posts that older than previous 10 old posts.
             * @Author Jiyuan Chen u7055573
             */
            @Override
            public void onLoadMore(int position) {
                Toast.makeText(getActivity(), "Load More!", Toast.LENGTH_SHORT).show();
                AsyncAction action = new AsyncAction();
                lastVisitPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                cmd = "hm "+String.valueOf(loadedNum);
                action.execute(cmd);
                loadedNum+=10;
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
//        adapter.setStateRestorationPolicy(recyclerView.getAdapter().setStateRestorationPolicy());

        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * A Custom method used to perform deletion from the local environment.
     * It will be called once server confirms the post deleted from the server end.
     * Then the method will perform client end deletion.
     * Use to change the fragment view.
     * @Author Jiyuan Chen u7055573
     */
    public void updateRemovedRecyclerView(int postId) {
        for (int i = 0; i < postModels.size(); i++) {
            if (Integer.parseInt(postModels.get(i).getPostId()) == postId) {
                postModels.remove(i);
                adapter.notifyItemRemoved(i);
            }
        }
    }

    /**
     * Use to insert new post to the local environment.
     * It will be called once the new post has been created from the server end.
     * @Author Jiyuan Chen u7055573
     */
    public void updateInsertedRecyclerView(Post post){
        postModels.add(0,post);
        adapter.notifyItemInserted(0);
    }

    /**
     * Use to update edited post to the local environment.
     * It will be called once the post has been edited from the server end.
     * @Author Jiyuan Chen u7055573
     */
    public void updateEditRecyclerView(int postId, String editContent){

        for (int i = 0; i < postModels.size(); i++) {
            if (Integer.parseInt(postModels.get(i).getPostId()) == postId) {
                postModels.get(i).setContent(editContent);
                adapter.notifyItemChanged(i);
            }
        }
    }

    /**
     * Use to update like post to the local environment.
     * It will be called once the post has been liked from the server end.
     * @Author Jiyuan Chen u7055573
     */
    public void updateLikeRecyclerView(int postId){

        for (int i = 0; i < postModels.size(); i++) {
            if (Integer.parseInt(postModels.get(i).getPostId()) == postId) {
                System.out.println(postModels.get(i).getLikes());
                System.out.println(postModels.get(i).getLikes().size());
                postModels.get(i).getLikes().add(loginFragment.loggedUsername);
                adapter.notifyItemChanged(i);
                System.out.println(loginFragment.loggedUsername);
                System.out.println(postModels.get(i).getLikes());
                System.out.println(postModels.get(i).getLikes().size());

            }
        }

    }

    /**
     * Use to update comment to the post in local environment.
     * It will be called once the post has been liked from the server end.
     * @Author Jiyuan Chen u7055573
     */
    public void updateCommentRecyclerView(int postId,Comment comment){

        for (int i = 0; i < postModels.size(); i++) {
            if (Integer.parseInt(postModels.get(i).getPostId()) == postId) {
                postModels.get(i).getComments().add(comment);
                adapter.notifyItemChanged(i);
            }
        }

    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLikeClick(int position) {

    }

    @Override
    public void onLoadMore(int position) {

    }

    @Override
    public void onGoTop(int position) {

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
                if(socket == null){
                    socket = new Socket("10.0.2.2", 6060);
                }
                if(inputStreamReader == null){
                    inputStreamReader = new InputStreamReader(socket.getInputStream());
                }
                if(outputStreamWriter == null){
                    outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                }
                if(bufferedReader == null){
                    bufferedReader = new BufferedReader(inputStreamReader);
                }
                if(bufferedWriter == null){
                    bufferedWriter = new BufferedWriter(outputStreamWriter);
                }

                bufferedWriter.write(args[0]);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                response = bufferedReader.readLine();
                System.out.println("Server: " + response);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        /**
         * An override onPostExecute method, to handle like post or get post.
         * And then update ui and local recyclerView.
         * @Author Jiyuan Chen u7055573
         */
        protected void onPostExecute(String result) {
            //resultis the data returned from doInbackground


            //check if the cmd sent to server was like post
            if(cmd.substring(0,2).compareTo("lp")!=0){
                if(postModels== null){
                    postModels= setupPost(result);
                }else if(postModels != null){

                    List<Post> tempList = setupPost(result);
                    for(int i =0; i<tempList.size();i++){
                        postModels.add(tempList.get(i));
                    }
                }
                setupRecyclerView();
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(lastVisitPosition);

            }else if (cmd.substring(0,2).compareTo("lp")==0){
                if (result.substring(0,3).compareTo("lps")==0) {

                    updateLikeRecyclerView(Integer.parseInt(itemId));
                    //check if previous fragment was searchFragment,
                    //If it's check for searchFragment postModels, if find the same post
                    //update the like number of that post in searchFragment.
                    if(searchFragment.postModels!=null){
                        for (int i = 0; i <searchFragment.postModels.size(); i++) {
                            if (Integer.parseInt(searchFragment.postModels.get(i).getPostId()) == Integer.parseInt(itemId)) {
                                searchFragment.postModels.get(i).getLikes().add(loginFragment.loggedUsername);
                                searchFragment.adapter.notifyItemChanged(i);
                            }
                        }
                    }

                }
            }
        }
    }
}