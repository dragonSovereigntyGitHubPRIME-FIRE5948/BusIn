package com.example.sgbusandlocationalarm.Accounts;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sgbusandlocationalarm.Constants;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccountsRepo {

    // VARIABLES //
    private static volatile AccountsRepo instance;

    // CONSTRUCTORS //
    private AccountsRepo() {}

    /** Get instance of AccountsRepo class */
    public static AccountsRepo getInstance() {
        AccountsRepo result = instance;
        if (result != null) {
            return result;
        }
        synchronized (AccountsRepo.class) {
            if (instance == null) {
                instance = new AccountsRepo();
            }
            return instance;
        }
    }

    /**
     /////////////////////////////////
     // 1. User Account Management //
     ///////////////////////////////
     */

    /** Get current user */
    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /** Get current user private uid */
    @Nullable
    public String getCurrentUserUID() {
        return this.getCurrentUser().getUid();
    }

    /** Get current user and check if it is anonymous account */
    public Boolean isAnonymous() {
        return FirebaseAuth.getInstance().getCurrentUser().isAnonymous();
    }

    /** Get current user to sign out */
    public Task<Void> signOutAccount(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    //TODO PUT IN FIREBASE UTILS

    /**
         * Re-authenticate current user.
         * Some security-sensitive actions—such as deleting an account, setting a primary email address, and
         * changing a password—require that the user has recently signed in. If you perform one of these actions,
         * and the user signed in too long ago, the action fails and throws FirebaseAuthRecentLoginRequiredException
         * <p>
         * EmailAuthProviderID: password
         * PhoneAuthProviderID: phone
         * GoogleAuthProviderID: google.com
         * FacebookAuthProviderID: facebook.com
         * TwitterAuthProviderID: twitter.com
         * GitHubAuthProviderID: github.com
         * AppleAuthProviderID: apple.com
         * YahooAuthProviderID: yahoo.com
         * MicrosoftAuthProviderID: hotmail.com
     */
    //TODO CHANGE
    public Task<Void> reAuthenticateCurrentUser(String provider, String userInputEmail, String userInputPassword) {

        FirebaseUser currentUser = getCurrentUser();

        switch (provider) {
            case "password":
                AuthCredential credential = EmailAuthProvider.getCredential(userInputEmail, userInputPassword);
                return currentUser.reauthenticate(credential);
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Log.d(TAG, "User re-authenticated.");
//                            }
//                        });
            default:
                return null;
        }
    }

    /** Checks if user is verified */
    public Boolean isVerified() {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.isEmailVerified();
        } else {
            return null;
        }
    }

    /** Send verification email */
    public Task<Void> sendVerificationEmail() {
        FirebaseUser currentUser = getCurrentUser();
//        if (!isVerified()) {
        return currentUser.sendEmailVerification();
//        }else {return null;}
    }

    /** Send password reset email to registered email of current user */
    public Task<Void> sendPasswordResetEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth != null) {
            FirebaseUser currentUser = getCurrentUser();
            if (!currentUser.isAnonymous()) {
                return auth.sendPasswordResetEmail(currentUser.getEmail());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

//    auth.confirmPasswordReset()
    //verifyPasswordResetCode()

    /** Send verification code to phone */
//    PhoneAuthOptions options =
//            PhoneAuthOptions.newBuilder(mAuth)
//                    .setPhoneNumber(phoneNumber)       // Phone number to verify
//                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                    .setActivity(this)                 // (optional) Activity for callback binding
//                    // If no activity is passed, reCAPTCHA verification can not be used.
//                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                    .build();
//  PhoneAuthProvider.verifyPhoneNumber(options);


    //////////////////////////////////
    // 2. Firestore CRUD //
    // Retrieves data to CRUD User Account and Accounts
    //////////////////////////////////

    // COLLECTION REFERENCES //
    private static final String COLLECTION_ACCOUNTS = Constants.COLLECTION_ACCOUNTS;

    private CollectionReference getCollectionAccounts() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_ACCOUNTS);
    }

    // i. CREATE //

    /**
         Save AccountModel of user account to Firestore.
         Only read/create account doc if user is new by comparing metadata.
     */
    public void saveAccountToFirestore() {
        FirebaseUser userAccount = getCurrentUser();
        Task<DocumentSnapshot> userAccountDocRef = getCurrentAccount();

        if (userAccount != null) {

            // get user account meta deta
            FirebaseUserMetadata metadata = userAccount.getMetadata();

            // check if account is new user, if yes, save it to Firestore
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                for (UserInfo profile : userAccount.getProviderData()) {
                    //TODO
                    // Id of the provider (ex: google.com)
                    // String providerId = profile.getProviderId();
                    // combine with send email or sms
                    // username
                    String username = (profile.getDisplayName() != null) ? profile.getDisplayName() : "";
                    //email
                    String email = (profile.getEmail() != null) ? profile.getEmail() : "";
                    // profile pic
                    String urlProfilePic = (profile.getPhotoUrl() != null) ? profile.getPhotoUrl().toString() : null;
                    // uid
                    String uid = userAccount.getUid();
                    // last seen
                    long lastSeen = metadata.getLastSignInTimestamp();
                    Date date = new Date(lastSeen);

                    // create AccountModel object
                    AccountsModel accountToCreate = new AccountsModel(uid, username, email, urlProfilePic);

                    userAccountDocRef.addOnCompleteListener(docSnapshot -> {
                        if (docSnapshot.isSuccessful()) {
                            // only creates account doc if it does not exist
                            if (!docSnapshot.getResult().exists()) {
                                // to prevent overwriting details with the new AccountModel instance
                                getCollectionAccounts().document(uid).set(accountToCreate);
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", docSnapshot.getException());
                        }
                    });
                }
            }
        } else {
            //TODO HANDLE ERROR
//            Log.d(TAG, "Failed with: ", task.getException());
        }
    }

    // ii. RETRIEVE //
    /**
     Retrieve user account Doc from Firestore by uid.
     The doc is casted into AccountModel class in the manager.
     */
    public Task<DocumentSnapshot> getCurrentAccount() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getCollectionAccounts().document(uid).get();
        } else {
            return null;
        }
    }

    // TODO QUERY, RUN ASYNC TASK, CAST IT INTO ARRAYLIST OF ACCOUNT MODEL
    /** Query for 'connections' accounts of current account from Firestore */
//    public Query getConnections(){
//        String uid = this.getCurrentUserUID();
//        // C accounts > D user uid >
//        return this.getCollectionAccounts()
//                .document(uid)
//                .collection(COLLECTION_CONNECTIONS);
//    }

    /** Query for 'following' accounts of current account from Firestore */
//    public Query getFollowings(){
//        String uid = this.getCurrentUserUID();
//        // C accounts > D user uid >
//        return this.getCollectionAccounts()
//                .document(uid)
//                .collection(COLLECTION_FOLLOWING);
//    }

    // iii. UPDATE //

    ///////////
    // AUTH //
    /////////

    /**
     Update user account profile detail(s) (username &/or profile pic) in Auth.

     Important:
     This is a security sensitive operation that requires the user to have recently signed in.
     If this requirement isn't met, ask the user to authenticate again and later call reauthenticate(AuthCredential).

     In addition, note that the original email address recipient will receive an email that allows them to
     revoke the email address change, in order to protect them from account hijacking.
     */
    public Task<Void> updateProfileInAuth(UserProfileChangeRequest updatedDetails) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.updateProfile(updatedDetails);
        } else {
            return null;
        }
    }

    /**
     Update user account email in Auth.
     Important:
     This is a security sensitive operation that requires the user to have recently signed in.
     If this requirement isn't met, ask the user to authenticate again and later call reauthenticate(AuthCredential).

     In addition, note that the original email address recipient will receive an email that allows them to
     revoke the email address change, in order to protect them from account hijacking.
     */
    public Task<Void> updateEmailInAuth(String updatedEmail) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.updateEmail(updatedEmail);
        } else {
            return null;
        }
    }

    /**
     Update user account password in Auth.

     Important:
     This is a security sensitive operation that requires the user to have recently signed in.
     If this requirement isn't met, ask the user to authenticate again and later call reauthenticate(AuthCredential).

     In addition, note that the original email address recipient will receive an email that allows them to
     revoke the email address change, in order to protect them from account hijacking.
     */
    public Task<Void> updatePasswordInAuth(String updatedPassword) {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.updatePassword(updatedPassword);
        } else {
            return null;
        }
    }

    //TODO user.updatePhoneNumber(updatedDetail);

    ////////////////
    // FIRESTORE //
    //////////////

    /** Returns Task to update an user account detail by uid in Firestore. */
    public Task<Void> updateProfileInFirestore(String field, @Nullable Object value, Object... moreFieldsAndValues) {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getCollectionAccounts().document(uid).update(field, value, moreFieldsAndValues);
        } else {
            return null;
        }
    }

    // iv. DESTROY //
    /** Delete account from Firebase Auth */
    public void deleteAccountFromAuth(Context context) {
        AuthUI.getInstance().delete(context);
    }

    /** Delete account from Firestore */
    public Task<Void> deleteAccountFromFirestore() {
        String uid = getCurrentUserUID();
        if (uid != null) {
            return this.getCollectionAccounts().document(uid).delete();
        } else {
            return null;
        }
    }

    // TODO delete friend, following
    // check if it auto updates


    /**
     //////////////////////////
     // 2. Accounts Management //
     ////////////////////////
     */

    // RETRIEVE //
    /** Query to get all accounts in Firestore */
    public Query queryGetAllAccounts() {
        return this.getCollectionAccounts()
                .limit(50);
    }

    /**
     ///////////////////////////////
     // 3. Social Network Management //
     //////////////////////////////
     */


    // Storing uid in doc
    // 1. Using all fields
    // 2. can simple query using whereArrayContains to check if user connected/connected (1 read)
    //

    // Storing AccountModel or creating docs to store each account
    // 1. Ideal for scalability and makes 1 less read
    // 2. Issue is when account updates his/her details, need to update the one stored in the network collection as well

    // Storing uid as an array field in AccountModel
    // not scalable
    // very limited size as uid has a lot of character
    // don't need to read doc to check connection/following status

    // In terms of number of reads, not much difference between the 2 as you are still reading same number of docs (aka
    // retrieving n number of accounts)

    // Write Approach
    // storing AccountModel doc in a collection

    // Since app will check if user is following/connected to account every time user enters ProfileActivity
    // it would make sense to just overwrite the latest account details
    // instead of just checking networking status (read)

    // profile will be updated already
    // from user end, if connected/following just update with latest detail
    // if not, there is no doc to update anyways

    // Storing as array field and storing in sub-collection, makes same number of reads/writes but array field very limited

    // iv. DESTROY //
    /** Removes AccountModel of account the user is connected to from connected collection */
//    public Task<Void> removeConnection(AccountModel account) {
//        FirebaseUser currentUser = getCurrentUser();
//        if (currentUser != null) {
//            return this.getCollectionConnections()
//                    .document(account.getUid())
//                    .delete();
//        } else {return null;}
//    }
}
