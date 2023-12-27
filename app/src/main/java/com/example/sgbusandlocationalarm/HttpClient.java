package com.example.sgbusandlocationalarm;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgbusandlocationalarm.BusArrival.BusArrivalModel;
import com.example.sgbusandlocationalarm.BusArrival.BusArrivalNestedAdapter;
import com.example.sgbusandlocationalarm.BusArrival.BusStopAdapter;
import com.example.sgbusandlocationalarm.BusArrival.BusStopModel;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClient {

    /**
         1. Retrieve bus stops from LTA DataMall server in new thread.
         2. Update ui accordingly in main thread.
     */
    public static List<BusStopModel.BusStopDetails> getBusStops(RecyclerView rv, BusStopAdapter.OnItemClickListener listener) {

        // variables
        List<BusStopModel.BusStopDetails> listBusStops = new ArrayList();

        Executors.newSingleThreadExecutor().execute(() -> {
            //initialisations
            // update ui in main thread
            // main thread handler
            Handler mainHandler = new Handler(Looper.getMainLooper());
            // build OkHttp client
            OkHttpClient client = new OkHttpClient().newBuilder().build();

            // 0 - 5000
            List<Integer> arr = Arrays.asList(0, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000);
            //async calculations
            for (int i = 0; i < arr.size(); i++) {
                // code block to be executed
                Request request = new Request.Builder()
                        .url("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip="+arr.get(i))
                        // authenticated query parameters
                        .addHeader("AccountKey", Utils.getLtaDataMallApiKey())
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    ResponseBody body = response.body();
                    // ObjectMapper object
                    ObjectMapper objectMapper = new ObjectMapper();
                    // map json string to model
                    BusStopModel model = objectMapper.readValue(body.string(), BusStopModel.class);
                    // pass mapped json array for bus stop details into list
                    listBusStops.addAll(model.getBusStopDetails());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            mainHandler.post(() -> {
                BusStopAdapter busStopAdapter;
                // layout manager
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rv.getContext());
                rv.setLayoutManager(linearLayoutManager);
                // adapter
                busStopAdapter = new BusStopAdapter(listBusStops, listener);
                rv.setAdapter(busStopAdapter);
                rv.setHasFixedSize(true);
            });
        });
        return listBusStops;
    }

    /**
        1. Retrieve real-time bus arrival details from LTA DataMall server in new thread.
        2. Update ui accordingly in main thread.
     */
    public static void getBusArrivalDetails(Context context, String busStopCode, RecyclerView rv) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<BusArrivalModel.BusArrivalDetails> listBusArrivalDetails;

            // main thread handler
            Handler mainHandler = new Handler(Looper.getMainLooper());

            Request request = new Request.Builder()
                    // append bus stop code at the end
                    .url("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode="+busStopCode)
                    // authenticated query parameters
                    .addHeader("AccountKey", Utils.getLtaDataMallApiKey())
                    .build();

            // build OkHttp client
            OkHttpClient client = new OkHttpClient().newBuilder().build();

            try (Response response = client.newCall(request).execute()) {
                // get response body from server
                ResponseBody body = response.body();
                // ObjectMapper object
                ObjectMapper objectMapper = new ObjectMapper();
                // map json string to model
                BusArrivalModel model = objectMapper.readValue(body.string(), BusArrivalModel.class);
                // pass mapped json array for bus stop details into list
                listBusArrivalDetails = model.getBusArrivalDetails();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mainHandler.post(() -> {
                //Update UI
                BusArrivalNestedAdapter adapter = new BusArrivalNestedAdapter(listBusArrivalDetails);
                rv.setLayoutManager(new LinearLayoutManager(context));
                rv.setHasFixedSize(true);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });

        });
    }
}
