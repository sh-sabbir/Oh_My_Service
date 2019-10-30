package com.codeian.ohmyservice.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codeian.ohmyservice.R;
import com.codeian.ohmyservice.customer.SearchActivity;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    private MaterialCardView sAppRepair,sCarRepair,sCarTuning,
            sCarRent,sShiftingService,sCarWash,sCleanService,
            sEventManagement,sCatering;

    public HomeFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sAppRepair = root.findViewById(R.id.sAppRepair);
        sCarRepair = root.findViewById(R.id.sCarRepair);
        sCarTuning = root.findViewById(R.id.sCarTuning);
        sCarRent   = root.findViewById(R.id.sCarRent);
        sShiftingService = root.findViewById(R.id.sShiftingService);
        sCarWash = root.findViewById(R.id.sCarWash);
        sCleanService = root.findViewById(R.id.sCleanService);
        sEventManagement = root.findViewById(R.id.sEventManagement);
        sCatering = root.findViewById(R.id.sCatering);

        sAppRepair.setOnClickListener(triggerSearch);
        sCarRepair.setOnClickListener(triggerSearch);
        sCarTuning.setOnClickListener(triggerSearch);
        sCarRent.setOnClickListener(triggerSearch);
        sShiftingService.setOnClickListener(triggerSearch);
        sCarWash.setOnClickListener(triggerSearch);
        sCleanService.setOnClickListener(triggerSearch);
        sEventManagement.setOnClickListener(triggerSearch);
        sCatering.setOnClickListener(triggerSearch);

        return root;
    }

    private View.OnClickListener triggerSearch = new View.OnClickListener() {
        public void onClick(View v) {

            String userAddress = "Amborkhana";
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("user_address",userAddress);
            switch (v.getId()) {
                case R.id.sAppRepair:

                    intent.putExtra("active_service","Appliance Repair");
                    startActivity(intent);

                    break;
                case R.id.sCarRepair:

                    intent.putExtra("active_service","Car Repair");
                    startActivity(intent);

                    break;
                case R.id.sCarTuning:

                    intent.putExtra("active_service","Car Tuning");
                    startActivity(intent);

                    break;

                case R.id.sCarRent:

                    intent.putExtra("active_service","Rent Service");
                    startActivity(intent);

                    break;
                case R.id.sShiftingService:

                    intent.putExtra("active_service","Shifting Service");
                    startActivity(intent);

                    break;
                case R.id.sCarWash:

                    intent.putExtra("active_service","Car Wash");
                    startActivity(intent);

                    break;

                case R.id.sCleanService:

                    intent.putExtra("active_service","Cleaning Service");
                    startActivity(intent);

                    break;
                case R.id.sEventManagement:

                    intent.putExtra("active_service","Event Management");
                    startActivity(intent);

                    break;
                case R.id.sCatering:

                    intent.putExtra("active_service","Catering Service");
                    startActivity(intent);

                    break;
                default:
                    break;
            }


        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_notification:
                Toast.makeText(getActivity(), "Notification", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}