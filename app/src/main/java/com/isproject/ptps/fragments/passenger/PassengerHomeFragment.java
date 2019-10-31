package com.isproject.ptps.fragments.passenger;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isproject.ptps.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class PassengerHomeFragment extends Fragment {

    private OnCardClickedListener mListener;

    private CardView scanCode, paymentHistory, account;

    public PassengerHomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_passenger_home, container, false);
        scanCode = view.findViewById(R.id.cardScanCode);
        paymentHistory = view.findViewById(R.id.cardPaymentHistory);
        account = view.findViewById(R.id.cardAccount);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.goToPassengerFragment("scan");
                }
            }
        });

        paymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.goToPassengerFragment("history");
                }
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null) {
                    mListener.goToPassengerFragment("account");
                }
            }
        });
    }

    public interface OnCardClickedListener {
        void goToPassengerFragment(String fragment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnCardClickedListener) {
            mListener = (OnCardClickedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCardClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
