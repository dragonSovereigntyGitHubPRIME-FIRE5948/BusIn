package com.example.sgbusandlocationalarm.Notifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sgbusandlocationalarm.Accounts.AccountsManager;
import com.example.sgbusandlocationalarm.databinding.ActivityNotifiersBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotifiersActivity extends AppCompatActivity {

    // VARIABLES
    private RecyclerView rvNotifiers;
    private NotifierAdapter notifierAdapter;
    private LinearLayoutManager linearLayoutManager;
    // MANAGERS//
    private NotifierManager notifierManager = NotifierManager.getInstance();
    private AccountsManager accountsManager = AccountsManager.getInstance();
    // BINDINGS //
    private ActivityNotifiersBinding binding;

    // LIFECYCLE //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifiersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar Listeners
        setUpAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (notifierAdapter != null) {notifierAdapter.startListening();}
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notifierAdapter == null) {notifierAdapter.stopListening();}
    }

    // FUNCTIONS //

    /** Set up ChatAdapter */
    private void setUpAdapter() {
        // get RecyclerView binding
        rvNotifiers = binding.rvNotifiers;
        // create, config and set layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.canScrollVertically();
        // set layout manager to recyclerview
        rvNotifiers.setLayoutManager(linearLayoutManager);
        // set up FirebaseRecyclerOptions
        FirestoreRecyclerOptions<NotifierModel> options =
                new FirestoreRecyclerOptions.Builder<NotifierModel>()
                        .setQuery(notifierManager.queryAllNotifiers(), NotifierModel.class)
                        .setLifecycleOwner(this) // start listening when activity starts
                        .build();
        // create, config and set adapter
        notifierAdapter = new NotifierAdapter(options, this, accountsManager);
        // set adapter to recyclerview
        rvNotifiers.setAdapter(notifierAdapter);

        // set up listener for adapter
        // Scroll to bottom on new messages
//        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                binding.rvMessages.smoothScrollToPosition(messageAdapter.getItemCount());
//            }
//        });
    }

    /** Set up tool bar listeners */

}