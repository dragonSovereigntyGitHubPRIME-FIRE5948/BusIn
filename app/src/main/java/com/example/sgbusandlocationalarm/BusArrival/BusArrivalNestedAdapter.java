package com.example.sgbusandlocationalarm.BusArrival;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgbusandlocationalarm.Constants;
import com.example.sgbusandlocationalarm.R;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.example.sgbusandlocationalarm.databinding.ViewholderNestedBusArrivalBinding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BusArrivalNestedAdapter extends RecyclerView.Adapter<BusArrivalNestedAdapter.BusArrivalNestedViewHolder> {

    // VARIABLES //
    private List<BusArrivalModel.BusArrivalDetails> listBusArrivalModel;

    // CONSTRUCTORS //
    public BusArrivalNestedAdapter(List<BusArrivalModel.BusArrivalDetails> listBusArrivalDetails) {
        this.listBusArrivalModel = listBusArrivalDetails;
    }

    @NonNull
    @Override
    public BusArrivalNestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemsList =
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.viewholder_nested_bus_arrival, parent, false);
        return new BusArrivalNestedViewHolder(itemsList);
    }

    @Override
    public void onBindViewHolder(@NonNull BusArrivalNestedViewHolder holder, int position) {
        BusArrivalModel.BusArrivalDetails model = listBusArrivalModel.get(position);
        holder.setUpUI(model);
    }

    @Override
    public int getItemCount() {
        if (listBusArrivalModel != null && listBusArrivalModel.size() > 0) {
            return listBusArrivalModel.size();
        } else return 0;
    }

    // VIEW HOLDER CLASS //

    /**
     * BusArrivalNestedViewHolder (Inner Class)
     */
    public static class BusArrivalNestedViewHolder extends RecyclerView.ViewHolder {

        // PROPERTIES //
        ViewholderNestedBusArrivalBinding binding;

        // CONSTRUCTORS //
        public BusArrivalNestedViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewholderNestedBusArrivalBinding.bind(itemView);
        }

        // FUNCTIONS //
        public void setArrivalDuration(Integer arrivalDuration, TextView tvDuration, TextView tvTime) {
            if (arrivalDuration != null) {
                if (arrivalDuration < 1) {
                    Utils.setTextView(tvDuration, "Arr");
                    tvDuration.setTextColor(Color.rgb(64, 149, 49));
                    tvTime.setTextColor(Color.rgb(64, 149, 49));
                } else {
                    Utils.setTextView(tvDuration, arrivalDuration + "");
                }
            } else {
                Utils.setTextView(tvDuration, "-");
            }
        }

        public void setUpUI(BusArrivalModel.BusArrivalDetails model) {

            Utils.setTextView(binding.tvBusNumber, model.getServiceNo());

            // Get arrival duration
            Integer arrivalDuration1 = Utils.timeDifferenceInMinutes(model.getNextBus().getEstimatedArrival(), Utils.getCurrentDateTime());
            Integer arrivalDuration2 = Utils.timeDifferenceInMinutes(model.getNextBus2().getEstimatedArrival(), Utils.getCurrentDateTime());
            Integer arrivalDuration3 = Utils.timeDifferenceInMinutes(model.getNextBus3().getEstimatedArrival(), Utils.getCurrentDateTime());

            // Arrival Duration
            setArrivalDuration(arrivalDuration1, binding.tvDuration1, binding.tvTime1);
            setArrivalDuration(arrivalDuration2, binding.tvDuration2, binding.tvTime2);
            setArrivalDuration(arrivalDuration3, binding.tvDuration3, binding.tvTime3);

            // Arrival Time
            Utils.setTextView(binding.tvTime1, Utils.formatDate(model.getNextBus().getEstimatedArrival()));
            Utils.setTextView(binding.tvTime2, Utils.formatDate(model.getNextBus2().getEstimatedArrival()));
            Utils.setTextView(binding.tvTime3, Utils.formatDate(model.getNextBus3().getEstimatedArrival()));

            if (model.getNextBus().getLoad().equals(Constants.SEATS_AVAILABLE)) {
                binding.ivSeats1.setBackgroundResource(R.drawable.ic_circle_green);
            } else if (model.getNextBus().getLoad().equals(Constants.STANDING_AVAILABLE)) {
                binding.ivSeats1.setBackgroundResource(R.drawable.ic_circle_yellow);
            } else if (model.getNextBus().getLoad().equals(Constants.STANDING_LIMITED)) {
                binding.ivSeats1.setBackgroundResource(R.drawable.ic_circle_red);
            } else {
                binding.ivSeats1.setVisibility(View.INVISIBLE);
            }

            if (model.getNextBus().getType().equals(Constants.SINGLE_DECK)) {
                binding.ivBusType1.setBackgroundResource(R.drawable.ic_bus);

            } else if (model.getNextBus().getType().equals(Constants.DOUBLE_DECK)) {
                binding.ivBusType1.setBackgroundResource(R.drawable.ic_double_bus);
            } else if (model.getNextBus().getType().equals(Constants.BENDY)) {
                binding.ivBusType1.setBackgroundResource(R.drawable.ic_bus);
            } else {
                binding.ivBusType1.setVisibility(View.INVISIBLE);
            }

            if (model.getNextBus().getFeature().equals(Constants.WHEELCHAIR_ACCESSIBLE)) {
                binding.ivWheelchair1.setBackgroundResource(R.drawable.ic_wheelchair);
            } else {
                if (model.getNextBus3().getType().equals("")) {
                    binding.ivWheelchair3.setVisibility(View.INVISIBLE);
                } else {
                    binding.ivWheelchair3.setBackgroundResource(R.drawable.ic_wheelchair_no);
                }
            }

            if (model.getNextBus2().getLoad().equals(Constants.SEATS_AVAILABLE)) {
                binding.ivSeats2.setBackgroundResource(R.drawable.ic_circle_green);
            } else if (model.getNextBus2().getLoad().equals(Constants.STANDING_AVAILABLE)) {
                binding.ivSeats2.setBackgroundResource(R.drawable.ic_circle_yellow);
            } else if (model.getNextBus2().getLoad().equals(Constants.STANDING_LIMITED)) {
                binding.ivSeats2.setBackgroundResource(R.drawable.ic_circle_red);
            } else {
                binding.ivSeats2.setVisibility(View.INVISIBLE);
            }

            if (model.getNextBus2().getType().equals(Constants.SINGLE_DECK)) {
                binding.ivBusType2.setBackgroundResource(R.drawable.ic_bus);

            } else if (model.getNextBus2().getType().equals(Constants.DOUBLE_DECK)) {
                binding.ivBusType2.setBackgroundResource(R.drawable.ic_double_bus);
            } else if (model.getNextBus2().getType().equals(Constants.BENDY)) {
                binding.ivBusType2.setBackgroundResource(R.drawable.ic_bus);
            } else {
                binding.ivBusType2.setVisibility(View.INVISIBLE);
            }


            if (model.getNextBus2().getFeature().equals(Constants.WHEELCHAIR_ACCESSIBLE)) {
                binding.ivWheelchair2.setBackgroundResource(R.drawable.ic_wheelchair);
            } else {
                if (model.getNextBus3().getType().equals("")) {
                    binding.ivWheelchair3.setVisibility(View.INVISIBLE);
                } else {
                    binding.ivWheelchair3.setBackgroundResource(R.drawable.ic_wheelchair_no);
                }
            }


            if (model.getNextBus3().getLoad().equals(Constants.SEATS_AVAILABLE)) {
                binding.ivSeats3.setBackgroundResource(R.drawable.ic_circle_green);
            } else if (model.getNextBus3().getLoad().equals(Constants.STANDING_AVAILABLE)) {
                binding.ivSeats3.setBackgroundResource(R.drawable.ic_circle_yellow);
            } else if (model.getNextBus3().getLoad().equals(Constants.STANDING_LIMITED)) {
                binding.ivSeats3.setBackgroundResource(R.drawable.ic_circle_red);
            } else {
                binding.ivSeats3.setVisibility(View.INVISIBLE);
            }

            if (model.getNextBus3().getType().equals(Constants.SINGLE_DECK)) {
                binding.ivBusType3.setBackgroundResource(R.drawable.ic_bus);

            } else if (model.getNextBus3().getType().equals(Constants.DOUBLE_DECK)) {
                binding.ivBusType3.setBackgroundResource(R.drawable.ic_double_bus);
            } else if (model.getNextBus3().getType().equals(Constants.BENDY)) {
                binding.ivBusType3.setBackgroundResource(R.drawable.ic_bus);
            } else {
                binding.ivBusType3.setVisibility(View.INVISIBLE);
            }

            if (model.getNextBus3().getFeature().equals(Constants.WHEELCHAIR_ACCESSIBLE)) {
                binding.ivWheelchair3.setBackgroundResource(R.drawable.ic_wheelchair);
            }
            else  {
                if (model.getNextBus3().getType().equals("")) {
                    binding.ivWheelchair3.setVisibility(View.INVISIBLE);
                } else {
                    binding.ivWheelchair3.setBackgroundResource(R.drawable.ic_wheelchair_no);
                }
            }
        }
    }
}
