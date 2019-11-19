package com.isproject.ptps.fragments.owner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.isproject.ptps.DataModelsAdapter;
import com.isproject.ptps.DataObject;
import com.isproject.ptps.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPaymentHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button goBackButton;
    private ArrayList<DataObject> dummy;
    OnGoBackButtonClicked mButton;

    public interface OnGoBackButtonClicked {
        void sendResponse();
    }

    public ViewPaymentHistoryFragment (ArrayList<DataObject> dummy) {
        this.dummy = dummy;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachedToParent(getParentFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_payment_history, container, false);
        recyclerView = view.findViewById(R.id.viewPaymentHistoryRecyclerView);
        goBackButton = view.findViewById(R.id.buttonGoBack);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButton.sendResponse();

            }
        });

        DataModelsAdapter adapter = new DataModelsAdapter(dummy, null, getContext());
        LinearLayoutManager lean = new LinearLayoutManager(getContext());
        lean.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lean);
        recyclerView.setAdapter(adapter);

    }

    /*@Override
    public void sendDetails(ArrayList<DataObject> mDataModels) {
        arrayList = mDataModels;
    }*/

    public void onAttachedToParent(Fragment fragment) {
        try {
            mButton = (OnGoBackButtonClicked) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnLicencePlateClickedListener"
            );
        }
    }
}
