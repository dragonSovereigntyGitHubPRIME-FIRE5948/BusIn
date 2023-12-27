package com.example.sgbusandlocationalarm.Notifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sgbusandlocationalarm.Accounts.AccountsManager;
import com.example.sgbusandlocationalarm.R;
import com.example.sgbusandlocationalarm.databinding.ViewholderNotifierBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotifierAdapter extends FirestoreRecyclerAdapter<NotifierModel, NotifierAdapter.NotifierViewHolder> {

    ////////////////////////
    // 1. Message Adapter //
    ///////////////////////

    ///////////////
    // VARIABLES //
    //////////////

    private Context context;
    private AccountsManager accountsManager;

    /**
     * Constructor
     */
    public NotifierAdapter(@NonNull FirestoreRecyclerOptions<NotifierModel> options, Context context, AccountsManager accountsManager) {
        super(options);
        this.context = context;
        this.accountsManager=accountsManager;
    }

    /**
     * onBindViewHolder
     */
    @Override
    protected void onBindViewHolder(@NonNull NotifierViewHolder holder, int position, NotifierModel model) {
        // bind the MessageModel object to the MessageViewHolder
        holder.updateUI(model);
//        holder.setUpItemListener(holder, context, model);
    }

    /**
     * onCreateViewHolder
     */
    @Override
    public NotifierViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new instance of the ViewHolder
        // layout called R.layout.message for each item
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.viewholder_notifier, parent, false);

        return new NotifierViewHolder(inflatedView, context);
    }

    ////////////////////////////////////////
    // 2. ChatViewHolder (Inner Class) //
    ///////////////////////////////////////

    public static class NotifierViewHolder extends RecyclerView.ViewHolder {

        // VARIABLES //
        private final ViewholderNotifierBinding binding;
        private Context context;

        // CONSTRUCTOR //
        public NotifierViewHolder(@NonNull View chatView, Context context) {
            super(chatView);
            this.context = context;
            // binds the ViewHolder object field to root view of argument (messageView) using bind func created below
            binding = ViewholderNotifierBinding.bind(chatView);
        }

        // FUNCTIONS

        /** */
        private void updateUI(NotifierModel model) {
            // views
            setNotifierDetails(model);
        }

        private void setNotifierDetails(NotifierModel model) {

            binding.tvName.setText(model.getName());
            binding.tvLocation.setText(model.getLocationNames().get(0));

            if (model.getActive()) {
                binding.switchNotifierActive.setChecked(true);
            } else {
                binding.switchNotifierActive.setChecked(false);
            }
        }

        /** Set up individual item listeners */
//        private void setUpItemListener(ChatAdapter.ChatViewHolder holder, Context context, ChatRoomModel model) {
//            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("chat_room", model);
//                context.startActivity(intent);
//                //TODO CHECK
//                ((Activity) context).finish();
//            });
//        }
    }
}
