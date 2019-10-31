package com.isproject.ptps.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.isproject.ptps.R;
import com.isproject.ptps.fragments.operator.ProvideDetailsFragment;

public class SignInOptionsFragment extends Fragment {
    private Button passengerSignIn, operatorSignIn;

    private OnSignInOptionSelected mListener;

    public SignInOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_options, container, false);
        passengerSignIn = view.findViewById(R.id.buttonPassengerSignIn);
        operatorSignIn = view.findViewById(R.id.buttonOperatorSignIn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        passengerSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.actionSignInSelected("Passenger");
            }
        });

        operatorSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.actionSignInSelected("Operator");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignInOptionSelected) {
            mListener = (OnSignInOptionSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignInOptionSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSignInOptionSelected {
        void actionSignInSelected(String option);
    }
}
