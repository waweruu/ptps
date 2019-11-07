package com.isproject.ptps.fragments.operator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isproject.ptps.R;

public class OperatorHomeFragment extends Fragment {

    private OnOperatorCardClicked onOperatorCardClicked;

    private CardView payments, account;

    public interface OnOperatorCardClicked {
        void goToOperatorFragment(String fragment);
    }

    public OperatorHomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_operator_home, container, false);
        payments = view.findViewById(R.id.passengerPaymentCard);
        account = view.findViewById(R.id.operatorAccountCard);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onOperatorCardClicked != null) {
                    onOperatorCardClicked.goToOperatorFragment("payments");
                }
            }
        });


        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onOperatorCardClicked != null) {
                    onOperatorCardClicked.goToOperatorFragment("account");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnOperatorCardClicked) {
            onOperatorCardClicked = (OnOperatorCardClicked) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnOperatorCardClicked");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onOperatorCardClicked = null;
    }
}
