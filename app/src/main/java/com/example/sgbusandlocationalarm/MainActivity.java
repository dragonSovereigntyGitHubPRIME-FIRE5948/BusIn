package com.example.sgbusandlocationalarm;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sgbusandlocationalarm.Geofence.NotifierMapsActivity;
import com.example.sgbusandlocationalarm.Notifier.NotifierManager;
import com.example.sgbusandlocationalarm.Notifier.NotifierModel;
import com.example.sgbusandlocationalarm.Utils.GoogleUtils;
import com.example.sgbusandlocationalarm.Utils.PermissionUtils;
import com.example.sgbusandlocationalarm.Utils.PlayServicesUtils;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.example.sgbusandlocationalarm.databinding.ActivityMainBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.protobuf.InvalidProtocolBufferException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

}