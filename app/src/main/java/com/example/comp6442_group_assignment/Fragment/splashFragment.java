package com.example.comp6442_group_assignment.Fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link splashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class splashFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public splashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment splashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static splashFragment newInstance(String param1, String param2) {
        splashFragment fragment = new splashFragment();
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
     * Used to handle the splash screen.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //disable the button navigation bar while in splash screen.
        BottomNavigationView navBar = getActivity().findViewById(R.id.buttomNavigationView);
        navBar.setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    /**
     * Used to handle the splash screen.
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button skip = getActivity().findViewById(R.id.button_continue);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(new homeFragment());
                BottomNavigationView navBar = getActivity().findViewById(R.id.buttomNavigationView);
                navBar.setVisibility(View.VISIBLE);
            }
        });
    }
}