package com.isproject.ptps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DataModelsAdapter extends RecyclerView.Adapter {

    private ArrayList<? extends DataModels> mDataModelsList;
    private ArrayList<SubRoute> mSubRoutesList;
    private Context context;
    private DataPasser mListener;

    public interface DataPasser {
        void passData(SubRoute subRoute);
    }

    public void setmListener(DataPasser mListener) {
        this.mListener = mListener;
    }

    public DataModelsAdapter(ArrayList<? extends DataModels> mDataModelsList, ArrayList<SubRoute> mSubRoutesList, Context context) {
        this.mDataModelsList = mDataModelsList;
        this.mSubRoutesList = mSubRoutesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case DataModels.MODEL_VEHICLE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vehicle_info, parent, false);
                return new VehicleViewHolder(itemView);
            case DataModels.MODEL_FARE_CHART:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fare_chart_info, parent, false);
                return new FareChartViewHolder(itemView);
            /*case DataModels.MODEL_SUBROUTES:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.subroute_info, parent, false);
                return new SubRouteViewHolder(itemView);*/
            //TODO: check viability
            case DataModels.MODEL_LIST:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.subroutes_layout, parent, false);
                return new SubRoutesListViewHolder(itemView);
            case DataModels.MODEL_PAYMENT_RECEIPTS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.payment_receipt, parent, false);
                return  new PaymentReceiptViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.operator_info, parent, false);
                return new OperatorViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case DataModels.MODEL_VEHICLE:
                ((VehicleViewHolder) holder).bindView(position);
                final Vehicle vehicle = (Vehicle) mDataModelsList.get(position);
                holder.itemView.findViewById(R.id.labelVehicleDetails).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean expanded = vehicle.isExpanded();
                        vehicle.setExpanded(!expanded);
                        notifyItemChanged(position);
                    }
                });
                break;
            case DataModels.MODEL_OPERATOR:
                ((OperatorViewHolder) holder).bindView(position);
                final Operator operator = (Operator) mDataModelsList.get(position);
                holder.itemView.findViewById(R.id.labelOperatorDetails).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean expanded = operator.isExpanded();
                        operator.setExpanded(!expanded);
                        notifyItemChanged(position);
                    }
                });
                break;
            case DataModels.MODEL_FARE_CHART:
                ((FareChartViewHolder) holder).bindView(position);
                final FareChart fareChart = (FareChart) mDataModelsList.get(position);
                holder.itemView.findViewById(R.id.labelRouteInfo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean expanded = fareChart.isExpanded();
                        fareChart.setExpanded(!expanded);
                        notifyItemChanged(position);
                    }
                });
                break;
            case DataModels.MODEL_LIST:
                ((SubRoutesListViewHolder) holder).bindView(position);
                final SubRoutesList subRoutesList = (SubRoutesList) mDataModelsList.get(position);
                holder.itemView.findViewById(R.id.labelRoutesLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean expanded = subRoutesList.isExpanded();
                        subRoutesList.setExpanded(!expanded);
                        notifyItemChanged(position);
                    }
                });
                break;
            case DataModels.MODEL_PAYMENT_RECEIPTS:
                ((PaymentReceiptViewHolder) holder).bindView(position);
                break;
            /*case DataModels.MODEL_SUBROUTES:
                ((SubRouteViewHolder) holder).bindView(position);
                break;*/
        }
    }

    @Override
    public int getItemCount() {
        if(mDataModelsList == null) {
            return 0;
        } else {
            return mDataModelsList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataModelsList.get(position).getModelType();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /*public void setmDataModelsList(List<? extends DataModels> dataModelsList) {
        if(mDataModelsList == null) {
            mDataModelsList = new ArrayList<>();
        }
        mDataModelsList.clear();
        mDataModelsList.addAll(dataModelsList);
        notifyDataSetChanged();
    }*/

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        private TextView textVehicleCapacity, textVehicleType;
        private View subItem;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            textVehicleType = itemView.findViewById(R.id.textVehicleType);
            textVehicleCapacity = itemView.findViewById(R.id.textVehicleCapacity);
            subItem = itemView.findViewById(R.id.subItemVehicle);
        }

        public void bindView(int position) {
            Vehicle vehicle = (Vehicle) mDataModelsList.get(position);

            boolean expanded = vehicle.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            textVehicleType.setText(vehicle.getVehicleType());
            textVehicleCapacity.setText(vehicle.getVehicleCapacity());
        }
    }

    public class OperatorViewHolder extends RecyclerView.ViewHolder {

            private TextView textFirstName, textLastName, textIdNumber, textPhoneNumber;
            private View subItem;

        public OperatorViewHolder(View itemView) {
            super(itemView);

            textFirstName = itemView.findViewById(R.id.textFirstName);
            textLastName = itemView.findViewById(R.id.textLastName);
            textPhoneNumber = itemView.findViewById(R.id.textPhoneNumber);
            textIdNumber = itemView.findViewById(R.id.textIdNumber);
            subItem = itemView.findViewById(R.id.subItemOperator);
        }

        public void bindView(int position) {
            Operator operator = (Operator) mDataModelsList.get(position);

            boolean expanded = operator.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            textFirstName.setText(operator.getFirstName());
            textLastName.setText(operator.getLastName());
            textPhoneNumber.setText(operator.getPhoneNumber());
            textIdNumber.setText(operator.getIdNumber());
        }
    }

    public class FareChartViewHolder extends RecyclerView.ViewHolder {

        private TextView textRouteNumber, textRouteStart, textRouteFinish;
        private View subItem;

        public FareChartViewHolder(View itemView) {
            super(itemView);

            textRouteNumber = itemView.findViewById(R.id.textRouteNumber);
            textRouteStart = itemView.findViewById(R.id.textRouteStart);
            textRouteFinish = itemView.findViewById(R.id.textRouteFinish);
            subItem = itemView.findViewById(R.id.subItemFareChart);
        }

        public void bindView(int position) {
            FareChart fareChart = (FareChart) mDataModelsList.get(position);

            boolean expanded = fareChart.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            textRouteNumber.setText(fareChart.getRouteNumber());
            textRouteStart.setText(fareChart.getRouteStart());
            textRouteFinish.setText(fareChart.getRouteFinish());
        }
    }

    public class SubRoutesListViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerView;
        private View subItem;

        public SubRoutesListViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.subRoutesList);
            subItem = itemView.findViewById(R.id.subItemSubRoutes);
        }

        public void bindView(int position) {
            SubRoutesList subRoutesList = (SubRoutesList) mDataModelsList.get(position);

            boolean expanded = subRoutesList.isExpanded();
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);


            SubRouteAdapter subRouteAdapter = new SubRouteAdapter(mSubRoutesList, context);
            subRouteAdapter.setOnSubRouteSelectedListener(new SubRouteAdapter.OnSubRouteSelectedListener() {
                @Override
                public void paymentDetails(SubRoute subRoute) {
                    mListener.passData(subRoute);
                }
            });
            LinearLayoutManager lean = new LinearLayoutManager(context);
            lean.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(lean);
            recyclerView.setAdapter(subRouteAdapter);
        }
    }

    public class PaymentReceiptViewHolder extends RecyclerView.ViewHolder {

        private TextView mpesaReceiptNumber, start, finish, amount, licencePlate, transactionDate, transactionTime;

        public PaymentReceiptViewHolder(@NonNull View itemView) {
            super(itemView);
            mpesaReceiptNumber = itemView.findViewById(R.id.mpesaReceiptNumber);
            start = itemView.findViewById(R.id.start);
            finish = itemView.findViewById(R.id.finish);
            amount = itemView.findViewById(R.id.amount);
            licencePlate = itemView.findViewById(R.id.licencePlate);
            transactionDate = itemView.findViewById(R.id.transactionDate);
            transactionTime = itemView.findViewById(R.id.transactionTime);
        }

        public void bindView(int position) {
            PaymentReceipt paymentReceipt = (PaymentReceipt) mDataModelsList.get(position);

            String dateAndTime = String.valueOf(paymentReceipt.getTransactionDate());
            String date = dateAndTime.substring(0, 8);
            String time = dateAndTime.substring(8);

            String date2 = date.substring(6) + "/" + date.substring(4, 6) + "/" + date.substring(2, 4);
            String time2 = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4);

            mpesaReceiptNumber.setText(paymentReceipt.getMpesaReceiptNumber());
            start.setText(paymentReceipt.getStart());
            finish.setText(paymentReceipt.getFinish());
            amount.setText(String.valueOf(paymentReceipt.getAmount()));
            licencePlate.setText(paymentReceipt.getLicencePlate());
            transactionDate.setText(date2);
            transactionTime.setText(time2);
        }
    }

    /*public class SubRouteViewHolder extends RecyclerView.ViewHolder {

        private TextView subrouteStart, subrouteFinish, subroutePrice;


        public SubRouteViewHolder(@NonNull View itemView) {
            super(itemView);

            subrouteStart = itemView.findViewById(R.id.textRouteStart);
            subrouteFinish = itemView.findViewById(R.id.textRouteFinish);
            subroutePrice = itemView.findViewById(R.id.textRoutePrice);
        }

        public void bindView(int position) {
            SubRoute subRoute = (SubRoute) mDataModelsList.get(position);

            subrouteStart.setText(subRoute.getSubrouteStart());
            subrouteFinish.setText(subRoute.getSubrouteFinish());
            subroutePrice.setText(subRoute.getSubroutePrice());
        }
    }*/

}
