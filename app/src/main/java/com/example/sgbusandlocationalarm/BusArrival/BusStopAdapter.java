package com.example.sgbusandlocationalarm.BusArrival;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgbusandlocationalarm.HttpClient;
import com.example.sgbusandlocationalarm.R;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.example.sgbusandlocationalarm.databinding.ViewholderBusStopsBinding;
import com.google.android.material.divider.MaterialDividerItemDecoration;

import java.util.List;

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusViewHolder>{

    /** onClickListener for each item in adapter */
    public interface OnItemClickListener {
        void onItemClick(int position, BusStopModel.BusStopDetails item);
    }

    private List<BusStopModel.BusStopDetails> listBusStopDetails;
    private final OnItemClickListener listener;

    ///////////////////
    // CONSTRUCTORS //
    //////////////////
    public BusStopAdapter(List<BusStopModel.BusStopDetails> listBusStopDetails, OnItemClickListener listener){
        this.listBusStopDetails = listBusStopDetails;
        this.listener = listener;
    }

    ////////////////
    // FUNCTIONS //
    ///////////////
    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate view BusStopModel layout
        View itemsList =
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_bus_stops, parent, false);
        return new BusViewHolder(itemsList);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        // get model for current item
        BusStopModel.BusStopDetails model = listBusStopDetails.get(position);
        // set UI with bus stop details
        holder.setUpUI(model);
        // bind item listener
        holder.bind(model, listener);
        // set nested recyclerview expand function
        expandNestedList(model, holder);

//        MaterialDividerItemDecoration dividerItemDecoration = new MaterialDividerItemDecoration(holder.binding.rvBusArrivalDetails.getContext(),
//                MaterialDividerItemDecoration.VERTICAL | MaterialDividerItemDecoration.HORIZONTAL);
//        dividerItemDecoration.setDividerColorResource(holder.binding.rvBusArrivalDetails.getContext(), R.color.navy);
//        dividerItemDecoration.setDividerThickness(10);
//        holder.binding.rvBusArrivalDetails.addItemDecoration(dividerItemDecoration);
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

    public BusStopModel.BusStopDetails getItem(int position) {
        return listBusStopDetails.get(position);
    }

    /** Sets a filtered list for from SearchView */
    public void setFilteredList(List<BusStopModel.BusStopDetails> filteredList) {
        this.listBusStopDetails = filteredList;
        notifyDataSetChanged();
    }

    /** Expand the nested layout */
    public void expandNestedList(BusStopModel.BusStopDetails model, BusViewHolder holder) {

        //TODO SET IMAGE BUTTONS
//            if (isExpandable){
//                holder.mArrowImage.setImageResource(R.drawable.arrow_up);
//            }else{
//                holder.mArrowImage.setImageResource(R.drawable.arrow_down);
//            }

        holder.binding.layoutBusStops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isExpandable = model.isExpandable();

                if (!isExpandable) {
                    holder.binding.rlNestedItems.setVisibility(View.VISIBLE);
                    // get bus stop code from item
//                    holder.getBusArrivalDetails(model.getBusStopCode());

                    // Go HTTP class
                    HttpClient.getBusArrivalDetails(v.getContext(), model.getBusStopCode(), holder.binding.rvBusArrivalDetails);
                    model.setExpandable(true);

                } else {
                    holder.binding.rlNestedItems.setVisibility(View.GONE);
                    model.setExpandable(false);
                }
            }
        });
    }

    //////////////////////
    // VIEWHOLDER CLASS //
    /////////////////////

    /** BusViewHolder (Inner Class) */
    public static class BusViewHolder extends RecyclerView.ViewHolder {

        ////////////////
        // VARIABLES //
        ///////////////

        ///////////////
        // BINDINGS //
        //////////////
        ViewholderBusStopsBinding binding;

        //////////////////
        // CONSTRUCTORS //
        //////////////////
        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewholderBusStopsBinding.bind(itemView);
        }

        ////////////////
        // FUNCTIONS //
        ///////////////
        /** set up onClickListener */
        public void bind(final BusStopModel.BusStopDetails item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, item);
                    }
                }
            });
        }

        /** Set the details of bus stop */
        public void setUpUI(BusStopModel.BusStopDetails model) {
            Utils.setTextView(binding.tvDescAndCode, model.getDescription() + " (" +model.getBusStopCode()+")");
            Utils.setTextView(binding.tvRoadName, model.getRoadName());

            String first;
            String second;
            String name = model.getDescription();
            String[] splitArray= name.split("\\s+");

            if (splitArray.length > 1) {
                first = splitArray[0].charAt(0)+"";
                second = splitArray[1].charAt(0)+"";
            } else {
                first = splitArray[0].charAt(0)+"";
                second = splitArray[0].charAt(1)+"";
            }

            Utils.setTextView(binding.tvAbbreviatedName, first+second);
        }
    }
}
