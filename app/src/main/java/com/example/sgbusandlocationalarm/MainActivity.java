package com.example.sgbusandlocationalarm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sgbusandlocationalarm.Bus.BusArrivalFragment;
import com.example.sgbusandlocationalarm.Bus.BusServicesFragment;
import com.example.sgbusandlocationalarm.Bus.Data.BusServiceDao;
import com.example.sgbusandlocationalarm.Bus.Data.BusStopDao;
import com.example.sgbusandlocationalarm.Bus.Models.BusService.BusServiceViewModel;
import com.example.sgbusandlocationalarm.Bus.Models.BusStop.BusStopViewModel;
import com.example.sgbusandlocationalarm.Helpers.DatabaseHelper;
import com.example.sgbusandlocationalarm.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ApiService apiService;
    private DatabaseHelper dbHelper;
    private BusStopDao busStopDao;
    private BusServiceDao busServiceDao;

    // private Fragment HomeFragment;
    private final BusArrivalFragment busArrivalFragment = new BusArrivalFragment();
    private final BusServicesFragment busServicesFragment = new BusServicesFragment();
    //    private final AllNotifiersFragment allNotifiersFragment = new AllNotifiersFragment();
    // Bundle Keys
    private BusStopViewModel busStopsViewModel;
    private BusServiceViewModel busServiceViewModel;

    // TODO
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize DBHelper & DAOs
        dbHelper = DatabaseHelper.getInstance(this);
        busStopDao = new BusStopDao(this);
        busServiceDao = new BusServiceDao(this);

        // Insert bus data if it does not exist
        // TODO check if origina nd estinaiton null cause 911
        if (!busStopDao.hasData() || !busServiceDao.hasData()) getBusDataAndInsertDb();

        // Initialize ViewModel
        busStopsViewModel = new ViewModelProvider(this).get(BusStopViewModel.class);
        busServiceViewModel = new ViewModelProvider(this).get(BusServiceViewModel.class);
        setViewModels();

        // Fragment Management
        setupFragments();
        tabListener();
    }

    // Fragment Set Up & Management
    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int fragmentContainer = binding.fragmentContainer.getId();

        // Add all fragments and hide them initially except the first one
        fragmentTransaction.add(fragmentContainer, busArrivalFragment, "Bus Arrival");
        fragmentTransaction.add(fragmentContainer, busServicesFragment, "Bus Services").hide(busServicesFragment);
//        fragmentTransaction.add(fragmentContainer, allNotifiersFragment, "All Notifiers").hide(allNotifiersFragment);
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
                        fragmentToHide1 = busServicesFragment;
//                        fragmentToHide2 = allNotifiersFragment;
                        break;
                    case 1:
                        fragmentToShow = busServicesFragment;
                        fragmentToHide1 = busArrivalFragment;
//                        fragmentToHide2 = allNotifiersFragment;
                        break;
                    case 2:
                        // TODO
                        fragmentToShow = busServicesFragment;
                        fragmentToHide1 = busArrivalFragment;
                        fragmentToHide2 = busServicesFragment;
                        break;
                    default:
                        return;
                }

                getSupportFragmentManager().beginTransaction()
                        .show(fragmentToShow)
                        .hide(fragmentToHide1)
//                        .hide(fragmentToHide2)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    // DB Read
    private void setViewModels() {
        // Create CompletableFutures for each read operation
        CompletableFuture<Void> busStopsFuture = busStopDao.read().thenAccept(busStops ->
                runOnUiThread(() -> {
                    // TODO ui
                    // Set data in ViewModel
                    busStopsViewModel.setBusStops(busStops);
                })
        ).exceptionally(throwable -> {
            runOnUiThread(() -> {
                // binding.progressBarCyclic.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to Load Data", Toast.LENGTH_SHORT).show();
            });
            return null;
        });

        CompletableFuture<Void> busServicesFuture = busServiceDao.read().thenAccept(busServices ->
                runOnUiThread(() -> {
                    // TODO ui
                    // Set data in ViewModel
                    busServiceViewModel.setDataList(busServices);
                })
        ).exceptionally(throwable -> {
            runOnUiThread(() -> {
                // binding.progressBarCyclic.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Failed to Load Data", Toast.LENGTH_SHORT).show();
            });
            return null;
        });

        // Combine both futures into a single future that completes when both are done
        CompletableFuture<Void> allOf = CompletableFuture.allOf(busStopsFuture, busServicesFuture);

        // After all futures complete, close the database
        allOf.whenComplete((result, throwable) -> dbHelper.close());

        // TODO fail show retry button
        // TODO Weekly API calls to update data
    }

    // DB Insert
    // *** Make sure to Close
    private void getBusDataAndInsertDb() {
        // Instantiate
        // TODO ensure instance is destroyed after usage
        // TODO how to use singleton pattern
        apiService = ApiService.getInstance();

        // TODO check implementation
        try {
            // Bus Stops
            apiService.fetchBusStops()
                    .thenCompose(busStops -> {
                        busStopDao.create(busStops);
                        // Chain the fetchBusRoutes call
                        return apiService.fetchBusRoutes()
                                .thenCompose(busRoutes -> {
                                    // Chain the fetchBusServices call
                                    return apiService.fetchBusServices()
                                            .thenAccept(busServices -> {
                                                // Create bus services in DAO
                                                busServiceDao.create(busServices, busRoutes, busStops);
                                            });
                                });
                    })
                    .exceptionally(throwable -> {
                        // Handle exceptions
                        throwable.printStackTrace();
                        return null;
                    })
                    .whenComplete((result, throwable) -> {
                        // This block will run whether the previous operations succeeded or failed
                        dbHelper.close();
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get the binding
    public ActivityMainBinding getBinding() {return binding;}
}