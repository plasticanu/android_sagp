package com.example.comp6442_group_assignment.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.comp6442_group_assignment.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link otherProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class otherProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public otherProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment otherProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static otherProfileFragment newInstance(String param1, String param2) {
        otherProfileFragment fragment = new otherProfileFragment();
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
     * Check if the user profile is public or not
     * if public, then show information of that user.
     * if unpublic, show a text "User profile is not public"
     * @Author Jiyuan Chen u7055573
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getParentFragmentManager().setFragmentResultListener("profileForm1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                TextView userName,firstName,lastName,email,phone,unpublic;
                boolean isPublic = result.getBoolean("isPublic");
                userName = getActivity().findViewById(R.id.textView_u_name);
                firstName = getActivity().findViewById(R.id.textView_u_first);
                lastName = getActivity().findViewById(R.id.textView_u_last);
                email = getActivity().findViewById(R.id.textView_u_email);
                phone = getActivity().findViewById(R.id.textView_u_phone);
                unpublic = getActivity().findViewById(R.id.textView_unpublic);
                if(isPublic){

                    userName.setText(result.getString("userName"));
                    firstName.setText(result.getString("userFirst"));
                    lastName.setText(result.getString("userLast"));
                    email.setText(result.getString("userEmail"));
                    phone.setText(result.getString("userPhone"));
                    userName.setVisibility(View.VISIBLE);
                    firstName.setVisibility(View.VISIBLE);
                    lastName.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    phone.setVisibility(View.VISIBLE);

                }else{

                    unpublic.setVisibility(View.VISIBLE);
                }

            }
        });



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_profile, container, false);
    }
}