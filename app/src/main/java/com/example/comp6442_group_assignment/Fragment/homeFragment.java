package com.example.comp6442_group_assignment.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

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
        View myview = inflater.inflate(R.layout.fragment_home, container, false);
        Button b = (Button) myview.findViewById(R.id.testButton);
        EditText et = (EditText) myview.findViewById(R.id.testInput);
//        Search search = new Search();
//        search.setFilePath("posts.xml");
//        search.insertToContentTree();
//
//        b.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                String input = et.getText().toString();
//                search.rankContent(input);
//                ArrayList<Integer> result =search.getContentRank();
//
//                Log.i("search result: ", String.valueOf(result.get(0)));
//                for(Post p : search.getPosts()){
//                    if(p.getLikes() == result.get(0)){
//                        Log.i("Content: " , p.getContent());
//                        System.out.println( p.getContent());
//                    }
//                }
//            }
//        });

        return myview;

    }

}