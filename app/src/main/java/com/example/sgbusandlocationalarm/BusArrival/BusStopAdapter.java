package com.example.sgbusandlocationalarm.BusArrival;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgbusandlocationalarm.ApiService;
import com.example.sgbusandlocationalarm.BusArrival.Models.BusArrival.BusArrivalModel;
import com.example.sgbusandlocationalarm.BusArrival.Models.BusStop.BusStopModel;
import com.example.sgbusandlocationalarm.R;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.example.sgbusandlocationalarm.databinding.ViewholderBusStopBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

/**
 * Contains 2 classes: Adapter and ViewModel
 */
public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusViewHolder> implements
        BusArrivalAdapter.OnItemClickListener {

    private final ApiService apiService = ApiService.getInstance();
    private List<BusStopModel> listBusStopDetails;
    private final OnItemClickListener listener;
    // Get main thread
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onItemClick(int position, BusArrivalModel item) {
    }

    // Interface
    public interface OnItemClickListener {
        void onItemClick(int position, BusStopModel item);
    }

    // Constructor
    public BusStopAdapter(List<BusStopModel> listBusStopDetails, OnItemClickListener listener) {
        this.listBusStopDetails = listBusStopDetails;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate view BusStopModel layout
        View itemsList = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewholder_bus_stop,
                parent,
                false);
        return new BusViewHolder(itemsList);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        // get model for current item
        BusStopModel model = listBusStopDetails.get(position);

        //
        // Functions
        //
        onClickListeners(model, holder);
        // set UI with bus stop details
        holder.setUpUI(model);
        // bind item listener
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return listBusStopDetails.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //
    // FUCCTIONS
    //

    public void setFilteredList(List<BusStopModel> filteredList) {
        this.listBusStopDetails = filteredList;
        notifyDataSetChanged();
    }

    public void onClickListeners(BusStopModel model, BusViewHolder holder) {

        holder.binding.refresh.setOnClickListener(v -> {
            if (model.getIsExpandable()) {
                return;
            }

            ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                    holder.binding.refresh,
                    "rotation",
                    0f, 360f);
            rotateAnimator.setDuration(500);
            rotateAnimator.start();

            holder.binding.progressBarCyclic.setVisibility(View.VISIBLE);
            holder.binding.rvBusArrivalDetails.setVisibility(View.GONE);

            apiService.fetchBusArrivalDetails(model.getBusStopCode())
                    .thenAccept(busArrivalDetails -> mainHandler.post(() -> {

                        if (busArrivalDetails.isEmpty()) {
                            holder.binding.progressBarCyclic.setVisibility(View.GONE);
                            holder.binding.tvNoData.setVisibility(View.VISIBLE);
                            return;
                        }

                        // Get adapter from the RecyclerView
                        BusArrivalAdapter nestedAdapter =
                                (BusArrivalAdapter) holder.binding.rvBusArrivalDetails.getAdapter();

                        if (nestedAdapter != null) {
                            nestedAdapter.updateData(busArrivalDetails);
                        } else {
                            FlexboxLayoutManager layout = new FlexboxLayoutManager(v.getContext());
                            layout.setFlexDirection(FlexDirection.ROW);
                            layout.setFlexWrap(FlexWrap.WRAP);
                            layout.setJustifyContent(JustifyContent.SPACE_BETWEEN);

                            BusArrivalAdapter adapter = new BusArrivalAdapter(busArrivalDetails, layout, this);
                            holder.binding.rvBusArrivalDetails.setLayoutManager(layout);
                            holder.binding.rvBusArrivalDetails.setAdapter(adapter);
                        }

                        holder.binding.progressBarCyclic.setVisibility(View.GONE);
                        holder.binding.rvBusArrivalDetails.setVisibility(View.VISIBLE);

                    }))
                    .exceptionally(throwable -> {
                        mainHandler.post(() -> {
                            // TODO ui for no data
                            holder.binding.progressBarCyclic.setVisibility(View.GONE);
                            Toast.makeText(v.getContext(), "Failed to Load Data", Toast.LENGTH_SHORT).show();
                        });
                        return null;
                    });
        });

        // Item (Bus Stop) onClickListener
        holder.binding.layoutBusStop.setOnClickListener(v -> {

            if (model.getIsExpandable()) {
                holder.binding.arrow.setVisibility(View.GONE);
                holder.binding.refresh.setVisibility(View.VISIBLE);

                // Show Parent layout hosting the RecyclerView / Progress Indicator
                holder.binding.layoutNestedItems.setVisibility(View.VISIBLE);

                apiService.fetchBusArrivalDetails(model.getBusStopCode())
                        .thenAccept(busArrivalDetails -> mainHandler.post(() -> {

                            if (busArrivalDetails.isEmpty()) {
                                holder.binding.progressBarCyclic.setVisibility(View.GONE);
                                holder.binding.tvNoData.setVisibility(View.VISIBLE);
                                return;
                            }

                            // Update RecyclerView or other UI components here
                            FlexboxLayoutManager layout = new FlexboxLayoutManager(v.getContext());
                            layout.setFlexDirection(FlexDirection.ROW);
                            layout.setFlexWrap(FlexWrap.WRAP);
                            layout.setJustifyContent(JustifyContent.SPACE_BETWEEN);

                            holder.binding.rvBusArrivalDetails.setLayoutManager(layout);
                            holder.binding.rvBusArrivalDetails.setHasFixedSize(true);

                            // ADAPTER //
                            // Update UI
                            BusArrivalAdapter adapter = new BusArrivalAdapter(busArrivalDetails, layout, this);
                            holder.binding.rvBusArrivalDetails.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            holder.binding.progressBarCyclic.setVisibility(View.GONE);
                            holder.binding.rvBusArrivalDetails.setVisibility(View.VISIBLE);

                        }))
                        .exceptionally(throwable -> {
                            mainHandler.post(() -> {
                                // TODO ui for no data
                                holder.binding.progressBarCyclic.setVisibility(View.GONE);
                                Toast.makeText(v.getContext(), "Failed to Load Data", Toast.LENGTH_SHORT).show();
                            });
                            return null;
                        });
                model.setExpandable(false);

            }
            else {
                holder.binding.arrow.setVisibility(View.VISIBLE);
                holder.binding.refresh.setVisibility(View.GONE);
                holder.binding.layoutNestedItems.setVisibility(View.GONE);
                model.setExpandable(true);
            }
        });
    }

    //////////////////////
    // VIEWHOLDER CLASS // ======================================
    /////////////////////

    public static class BusViewHolder extends RecyclerView.ViewHolder {

        ///////////////
        // BINDINGS //
        //////////////
        ViewholderBusStopBinding binding;

        //////////////////
        // CONSTRUCTORS //
        //////////////////
        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewholderBusStopBinding.bind(itemView);
        }

        ////////////////
        // FUNCTIONS //
        ///////////////

        /**
         * set up onClickListener
         */
        public void bind(final BusStopModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, item);
                }
            });
        }

        /**
         * Set the details of bus stop
         */
        public void setUpUI(BusStopModel model) {
            Utils.setTextView(binding.tvDesc, model.getDescription());
            Utils.setTextView(binding.tvCode, "(" + model.getBusStopCode() + ")");
            Utils.setTextView(binding.tvRoadName, model.getRoadName());

//            String first;
//            String second;
//            String name = model.getDescription();
//            String[] splitArray= name.split("\\s+");
//
//            if (splitArray.length > 1) {
//                first = splitArray[0].charAt(0)+"";
//                second = splitArray[1].charAt(0)+"";
//            } else {
//                first = splitArray[0].charAt(0)+"";
//                second = splitArray[0].charAt(1)+"";
//            }

//            Utils.setTextView(binding.tvAbbreviatedName, first+second);
        }
    }
}
