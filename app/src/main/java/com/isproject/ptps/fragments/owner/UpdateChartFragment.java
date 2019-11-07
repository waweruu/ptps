package com.isproject.ptps.fragments.owner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.isproject.ptps.R;
import com.isproject.ptps.Routes;
import com.isproject.ptps.SubRoute;


public class UpdateChartFragment extends Fragment {

    TextView routeNumber,routeStart,routeFinish,number, start, finish;
    Spinner subrouteStart,subrouteFinish;
    EditText price;
    Button updateSubroute,back;

    public UpdateChartFragment() {
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
        View view= inflater.inflate(R.layout.fragment_update_chart, container, false);

        routeNumber=view.findViewById(R.id.textviewRouteNumber);
        routeStart=view.findViewById(R.id.textviewRouteStart);
        routeFinish=view.findViewById(R.id.textviewRouteFinish);

        subrouteStart=view.findViewById(R.id.spinnerUpdateFirstSubroute);
        subrouteFinish=view.findViewById(R.id.spinnerUpdateLastSubroute);

        price=view.findViewById(R.id.editTextUpdatePrice);

        updateSubroute=view.findViewById(R.id.buttonUpdateSubroute);
        back=view.findViewById(R.id.buttonBackFromUpdateChart);

        number = view.findViewById(R.id.textviewRouteNumberUpdate);

        start = view.findViewById(R.id.textviewRouteStartUpdate);

        finish = view.findViewById(R.id.textviewRouteFinishUpdate);




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        final String licencePlate = bundle.getString("LICENCE PLATE");
        final String startSubRoute = bundle.getString("START");
        final String endSubRoute = bundle.getString("FINISH");
        final String routeprice = bundle.getString("PRICE");
        String routeNumber = bundle.getString("ROUTE_NUMBER");
        String routeBegin = bundle.getString("ROUTE_START");
        String routeEnd = bundle.getString("ROUTE_FINISH");

//
//        subrouteStart.setSelection(0);
//        subrouteFinish.setSelection(0);
//        price.setSelection(0);
        updateSubroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String userUID = current_user.getUid();
                SubRoute subRoute=new SubRoute();
                subRoute.setSubrouteStart(subrouteStart.getSelectedItem().toString());
                subRoute.setSubrouteFinish(subrouteFinish.getSelectedItem().toString());
                subRoute.setSubroutePrice(price.getText().toString());
            }
        });


        number.setText(routeNumber);
        start.setText(routeBegin);
        finish.setText(routeEnd);



        if(routeNumber == "1"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route1_));
        }
        else if(routeNumber=="2") {
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route2_));
        }
        else if(routeNumber=="3"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route3_));
        }
        else if(routeNumber=="4"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route4));
        }
        else if(routeNumber=="6"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, Routes.route6));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route6));
        }
        else if(routeNumber=="7c"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route7c));
        }
        else if(routeNumber=="8"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route8));
        } else if(routeNumber=="9"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route9));
        } else if(routeNumber=="11"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route11));
        }
        else if(routeNumber=="15"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route15));
        }
        else if(routeNumber=="14"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route14));
        }
        else if(routeNumber=="17B"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route17B));
        }
        else if(routeNumber=="23"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route23));
        }
        else if(routeNumber=="24"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route24));
        }
        else if(routeNumber=="25"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route25));
        }
        else if(routeNumber=="33"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33));
        }
        else if(routeNumber=="33B"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route33B));
        }
        else if(routeNumber=="34"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34));
        }
        else if(routeNumber=="34(buses)"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route34_Buses));
        } else if(routeNumber=="35/60"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route35_60_));
        }
        else if(routeNumber=="44"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route44));
        }
        else if(routeNumber=="45"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route45));
        }
        else if(routeNumber=="58"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route58));
        }
        else if(routeNumber=="100"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route100));
        }
        else if(routeNumber=="102"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route102));
        }
        else if(routeNumber=="105"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route105_));
        }
        else if(routeNumber=="106"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route106));
        }
        else if(routeNumber=="110"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route110_));
        }
        else if(routeNumber=="111"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route111_));
        }
        else if(routeNumber=="125/126"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route125_126));
        }
        else if(routeNumber=="146"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route146));
        }
        else if(routeNumber=="237"){
            subrouteStart.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
            subrouteFinish.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,Routes.route237));
        }
        else{
            Toast.makeText(getContext(),"Invalid Selection",Toast.LENGTH_SHORT).show();
        }
    }

}
