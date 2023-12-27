package com.example.sgbusandlocationalarm.Notifier;

import com.example.sgbusandlocationalarm.Accounts.AccountsManager;
import com.example.sgbusandlocationalarm.Constants;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotifierRepo {

    // VARIABLES //
    private static volatile NotifierRepo instance;
    private AccountsManager accountsManager = AccountsManager.getInstance();

    private static final String COLLECTION_ACCOUNTS = Constants.COLLECTION_ACCOUNTS;
    private static final String COLLECTION_NOTIFIERS = Constants.COLLECTION_NOTIFIERS;

    // CONSTRUCTORS //
    private NotifierRepo() {}

    /** Get instance of NotifierRepo class */
    public static NotifierRepo getInstance() {
        NotifierRepo result = instance;
        if (result != null) {
            return result;
        }
        synchronized (NotifierRepo.class) {
            if (instance == null) {
                instance = new NotifierRepo();
            }
            return instance;
        }
    }

    // COLLECTION REFERENCES //
    /** Get the Collection reference to 'chat_rooms' */
    public CollectionReference getCollectionNotifiers(){
        FirebaseUser user = accountsManager.getCurrentUser();

        if (user != null) {
            String id = user.getUid();
            return FirebaseFirestore.getInstance()
                    .collection(COLLECTION_ACCOUNTS)
                    .document(id)
                    .collection(COLLECTION_NOTIFIERS);
        } else {return null;}
    }

    /** ////////////
     // 1. CREATE //
     /////////// */

    /** Create 1to1 chat room doc, set a composite uid for the doc using both account's uid */
    public void createNotifier(NotifierModel notifier) {
        FirebaseUser currentUser = accountsManager.getCurrentUser();

        if (currentUser!= null) {

            // VARIABLES //

            // reference or create chat room doc
            CollectionReference colRef = this.getCollectionNotifiers();
            // Create doc object.
            DocumentReference docRef = colRef.document();
            // task
            docRef.set(notifier);
        }
    }

    /** ///////////////
     // 2. RETRIEVE //
     ///////////// */

    /** Retrieves chat rooms of current user. */
    public Query queryAllNotifiers() {
        FirebaseUser user = accountsManager.getCurrentUser();
        if (user != null) {
            return getCollectionNotifiers();
        } else {return null;}
    }

    /** Retrieve a chat room with the chat room uid.
     Used for 1to1 chat rooms.
     Chat room uid is a composite uid of both accounts.
     * */
    public Task<DocumentSnapshot> getNotifier(String notifierUid) {
        FirebaseUser user = accountsManager.getCurrentUser();
        if (user != null) {
            return getCollectionNotifiers().document(notifierUid).get();
        } else {return null;}
    }

    // 3. UPDATE //

    //TODO

    // 4. DESTROY //


}
