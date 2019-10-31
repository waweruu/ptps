package com.isproject.ptps;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class UserHolder extends RecyclerView.ViewHolder {

    public TextView textFirstName, textLastName, textUserType, textPhoneNumber;
    public TextView labelFirstName, labelLastName, labelUserType, labelPhoneNumber;

    public UserHolder(View view) {
        super(view);

        textFirstName = view.findViewById(R.id.textFirstName);
        textLastName = view.findViewById(R.id.textLastName);
        textPhoneNumber = view.findViewById(R.id.textPhoneNumber);
        textUserType = view.findViewById(R.id.textUserType);

        labelFirstName = view.findViewById(R.id.labelFirstName);
        labelLastName = view.findViewById(R.id.labelLastName);
        labelPhoneNumber = view.findViewById(R.id.labelPhoneNumber);
        labelUserType = view.findViewById(R.id.labelUserType);
    }

    public void setFirstName(String firstName) {
        textFirstName.setText(firstName);
    }

    public void setLastName(String lastName) {
        textLastName.setText(lastName);
    }

    public void setUserType(String userType) {
        textUserType.setText(userType);
    }

    public void setPhoneNumber(String phoneNumber) {
        textPhoneNumber.setText(phoneNumber);
    }
}
