package com.example.sgbusandlocationalarm.Accounts;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.Query;

public class AccountsManager {

    // VARIABLES //
    private static volatile AccountsManager instance;
    private AccountsRepo accountsRepo;

    // CONSTRUCTORS //
    private AccountsManager() {
        accountsRepo = AccountsRepo.getInstance();
    }

    /** Get instance of AccountsManager class */
    public static AccountsManager getInstance() {
        AccountsManager result = instance;
        if (result != null) {return result;}
        synchronized(AccountsRepo.class) {
            if (instance == null) {instance = new AccountsManager();}
            return instance;}
    }

    /**
     ////////////////////////////////
     // 1. User Account Management //
     ///////////////////////////////
     */

    /** Get current user. */
    public FirebaseUser getCurrentUser(){
        return accountsRepo.getCurrentUser();
    }

    /** Checks if user is logged in */
    public boolean isCurrentUserLoggedIn(){
        return (this.getCurrentUser() != null);
    }

    /** Checks if it is anonymous account */
    public Boolean isAnonymous(){return accountsRepo.isAnonymous();}

    /** Sign out user */
    public Task<Void> signOutAccount(Context context){
        // delete account in Auth if it is anonymous user
        if (accountsRepo.isAnonymous()) {
            accountsRepo.deleteAccountFromAuth(context);
        }
        return accountsRepo.signOutAccount(context);
    }

    /** Re-authenticate current user */
    public Task<Void> reAuthenticateCurrentUser(String email, String password) {
        return accountsRepo.reAuthenticateCurrentUser("password", email, password);
    }

    /** Checks if user is verified */
    public Boolean isVerified(){
        return accountsRepo.isVerified();
    }

    /** Send verification email */
    public Task<Void> sendVerificationEmail(){
        return accountsRepo.sendVerificationEmail();
    }

    /** Send password reset email to registered email of current user */
    public Task<Void> sendPasswordResetEmail(String emailToSend){
        return accountsRepo.sendPasswordResetEmail();
    }

    ///////////////////////
    // 2. Firestore CRUD //
    ///////////////////////

    //
    // i. CREATE
    //

    /** Save AccountModel of user account to Firestore */
    public void saveAccountToFirestore(){
        accountsRepo.saveAccountToFirestore();
    }


    //
    // ii. RETRIEVE
    //

    /** Retrieve current account data from Firestore by uid and casting it to AccountModel object */
    public Task<AccountsModel> getCurrentAccount(){
        return accountsRepo.getCurrentAccount().continueWith(task ->
                task.getResult().toObject(AccountsModel.class));
    }

    // TODO obviosuly wont listen for updates because it will??
    // use query to get latest list of accounts when user enters activity instead of running task at back
    /** Retrieve arraylist of 'connections' accounts of current account from Firestore */
//    public Query getConnections(){
//        return accountsRepo.getConnections();
//    }

    /** Retrieve arraylist of 'following' accounts of current account from Firestore */
//    public Query getFollowings(){
//        return accountsRepo.getFollowings();
//    }


    //
    // iii. UPDATE //
    //

    // Auth
    /** Update user account profile detail(s) (username &/or profile pic) in Auth. */
    public Task<Void> updateProfileInAuth(UserProfileChangeRequest updatedDetails){
        return accountsRepo.updateProfileInAuth(updatedDetails);
    }

    /** Update user account email in Auth. */
    public Task<Void> updateEmailInAuth(String updatedEmail) {
        return accountsRepo.updateEmailInAuth(updatedEmail);
    }

    /** Update user account password in Auth. */
    public Task<Void> updatePasswordInAuth(String updatedPassword) {
        return accountsRepo.updatePasswordInAuth(updatedPassword);
    }

    // Firestore
    /** Update an user account detail in Firestore. */
    public Task<Void> updateProfileInFirestore(String field, String value, Object... moreFieldsAndValues){
        return accountsRepo.updateProfileInFirestore(field, value, moreFieldsAndValues);
    }

    //
    // iv. DESTROY //
    //

    /** Delete account from Auth and Firestore. */
    public Task<Void> deleteAccount(Context context){
        // Delete account from firestore
        return accountsRepo.deleteAccountFromFirestore().addOnCompleteListener(task -> {
            // once done, delete account data from auth
            accountsRepo.deleteAccountFromAuth(context);
        });
    }


    /**
     /////////////////////////////
     // 2. Accounts Management //
     ///////////////////////////
     */

//    public Task<List<AccountModel>> getAllAccounts(){
//        // Get accounts from Firestore and cast it to a List of AccountModel Objects
//        return accountsRepo.getAllAccounts().continueWith(task ->
//                task.getResult().toObjects(AccountModel.class));
//    }
//
//    /** Return get list of DocumentSnapshots from QuerySnapshot task */
//    public Task<List<DocumentSnapshot>> getAllAccountsDocSnapshots() {
//        return accountsRepo.getAllAccounts().continueWith(task ->
//                task.getResult().getDocuments());
//    }
//
//    public Task<List<AccountModel>> getAllAccounts(String field, String condition){
//        // Get accounts from Firestore and cast it to a List of AccountModel Objects
//        return accountsRepo.getAccountsWithQuery(field, condition).continueWith(task ->
//                task.getResult().toObjects(AccountModel.class)
//        );
//    }

    /** Query to get all accounts in Firestore */
    public Query queryGetAllAccounts() {
        return accountsRepo.queryGetAllAccounts();
    }
}
