package com.example.sgbusandlocationalarm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sgbusandlocationalarm.BusArrival.BusArrivalFragment;
import com.example.sgbusandlocationalarm.BusArrival.BusRouteFragment;
import com.example.sgbusandlocationalarm.BusArrival.Data.BusServiceDao;
import com.example.sgbusandlocationalarm.BusArrival.Data.BusStopDao;
import com.example.sgbusandlocationalarm.BusArrival.Models.BusStop.BusStopViewModel;
import com.example.sgbusandlocationalarm.Notifier.AllNotifiersFragment;
import com.example.sgbusandlocationalarm.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    // TODO close db on destroy

    private ActivityMainBinding binding;

    private ApiService apiService;

    private BusStopDao busStopDao;
    private BusServiceDao busServiceDao;

    // private Fragment HomeFragment;
    private final BusArrivalFragment busArrivalFragment = new BusArrivalFragment();
    private final BusRouteFragment busRouteFragment = new BusRouteFragment();
    private final AllNotifiersFragment allNotifiersFragment = new AllNotifiersFragment();

    // Bundle Keys
    private BusStopViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize DAOs
        busStopDao = new BusStopDao(this);
        busServiceDao = new BusServiceDao(this);

        // Insert bus data if it does not exist
        if (!busStopDao.hasData() && !busServiceDao.hasData()) getBusDataAndInsertDb();

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(BusStopViewModel.class);
        setBusStopViewModel();

        // Fragment Management
        setupFragments();
        tabListener();
    }

    // TODO
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO
        // Close database connections
        if (busStopDao != null) {
            //busStopDao.close();
        }
        if (busServiceDao != null) {
            //busServiceDao.close();
        }
    }

    // Fragment Set Up & Management
    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int fragmentContainer = binding.fragmentContainer.getId();

        // Add all fragments and hide them initially except the first one
        fragmentTransaction.add(fragmentContainer, busArrivalFragment, "Bus Arrival");
        fragmentTransaction.add(fragmentContainer, busRouteFragment, "Bus Route").hide(busRouteFragment);
        fragmentTransaction.add(fragmentContainer, allNotifiersFragment, "All Notifiers").hide(allNotifiersFragment);
        fragmentTransaction.commit();
    }

    // Behaviour:
    // By toggling visibility, users would not have to deal with going back through the tabs when they press the back button
    // because there will only be 1 fragment at the top of the stack
    // App will exit when back button pressed.

    // Fragment Reuse: By initializing fragments once and only managing their visibility, you avoid the overhead of repeatedly creating and destroying fragments. This is especially beneficial if the fragments have complex layouts or need to initialize data.
    //
    //State Preservation: Since the fragments are not recreated every time a tab is selected, their state is preserved, which means any UI state or data loaded in the fragment remains intact. This reduces the need to reinitialize components or reload data, improving responsiveness.
    //
    //Reduced UI Redraws: Hiding and showing fragments rather than replacing them minimizes UI redraws, leading to a smoother user experience, especially on devices with limited resources.

    //By using the show() and hide() methods, you effectively prevent the back button from navigating through fragments. Here's why:
    //
    //No Back Stack Entries: Since fragments are not added to the back stack during tab switching, pressing the back button will not pop back to previously selected fragments. Instead, it will exit the activity if no other back stack entries are present.
    //
    //Controlled Navigation: You maintain control over fragment navigation, ensuring that the user experience aligns with the tabbed interface design. If users switch tabs, they stay on the currently visible tab until they explicitly change it.
    private void tabListener() {
        binding.actionBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragmentToShow;
                Fragment fragmentToHide1;
                Fragment fragmentToHide2;

                switch (tab.getPosition()) {
                    case 0:
                        fragmentToShow = busArrivalFragment;
                        fragmentToHide1 = busRouteFragment;
                        fragmentToHide2 = allNotifiersFragment;
                        break;
                    case 1:
                        fragmentToShow = busRouteFragment;
                        fragmentToHide1 = busArrivalFragment;
                        fragmentToHide2 = allNotifiersFragment;
                        break;
                    case 2:
                        fragmentToShow = allNotifiersFragment;
                        fragmentToHide1 = busArrivalFragment;
                        fragmentToHide2 = busRouteFragment;
                        break;
                    default:
                        return;
                }

                getSupportFragmentManager().beginTransaction()
                        .show(fragmentToShow)
                        .hide(fragmentToHide1)
                        .hide(fragmentToHide2)
                        .commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    // DB Read
    private void setBusStopViewModel() {
        // Start a new single thread sending a request to LTA Server
        busStopDao.read().thenAccept(busStops -> runOnUiThread(() -> {
            // TODO ui
            // Set data in ViewModel
            viewModel.setBusStops(busStops);
        })).exceptionally(throwable -> {
            runOnUiThread(() -> {
                // binding.progressBarCyclic.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to Load Data", Toast.LENGTH_SHORT).show();
            });
            return null;
        });

        // TODO close after reading
        // TODO fail show retry button
        // TODO Weekly API calls to update data
    }

    // DB Insert
    private void getBusDataAndInsertDb() {
        // Instantiate
        // TODO make sure only 1 instance
        apiService = ApiService.getInstance();

        // Bus Stops
        apiService.fetchBusStops().thenAccept(busStops -> {

            // Bus Routes
            apiService.fetchBusRoutes().thenAccept(busRoutes -> {
                busServiceDao.create(busRoutes, busStops);
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });

            busStopDao.create(busStops);

        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });

        // Bus Services
        apiService.fetchBusServices().thenAccept(busServices -> {
            busServiceDao.create(busServices);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}