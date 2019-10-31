package com.isproject.ptps;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class OperatorHolder extends RecyclerView.ViewHolder {
    private TextView labelFirstName, labelLastName, labelIdNumber, labelPhoneNumber;
    private TextView textFirstName, textLastName, textIdNumber, textPhoneNumber;

    public OperatorHolder(View itemView) {
        super(itemView);

        textFirstName = itemView.findViewById(R.id.textFirstName);
        textLastName = itemView.findViewById(R.id.textLastName);
        textPhoneNumber = itemView.findViewById(R.id.textPhoneNumber);
        textIdNumber = itemView.findViewById(R.id.textUserType);

        labelFirstName = itemView.findViewById(R.id.labelFirstName);
        labelLastName = itemView.findViewById(R.id.labelLastName);
        labelPhoneNumber = itemView.findViewById(R.id.labelPhoneNumber);
        labelIdNumber = itemView.findViewById(R.id.labelUserType);
    }

    public void setTextFirstName(TextView textFirstName) {
        this.textFirstName = textFirstName;
    }

    public void setTextLastName(TextView textLastName) {
        this.textLastName = textLastName;
    }

    public void setTextIdNumber(TextView textIdNumber) {
        this.textIdNumber = textIdNumber;
    }

    public void setTextPhoneNumber(TextView textPhoneNumber) {
        this.textPhoneNumber = textPhoneNumber;
    }
}
