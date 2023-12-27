package com.example.sgbusandlocationalarm;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sgbusandlocationalarm.Accounts.AccountsManager;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Arrays;
import java.util.List;

public class FirebaseUtils {

        // INITIALISATIONS //
        private static AccountsManager accountsManager = AccountsManager.getInstance();

        // Authentication Providers
        private static List<AuthUI.IdpConfig> providers = Arrays.asList(
                // google
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                // email
                new AuthUI.IdpConfig.EmailBuilder().build(),
                // anonymous
                new AuthUI.IdpConfig.AnonymousBuilder().build());

        // 1. FIREBASE AUTH UI //

        // Custom UI
        public static AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.layout_sign_in)
                .setGoogleButtonId(R.id.btnGoogle)
                .setEmailButtonId(R.id.btnEmail)
                .setAnonymousButtonId(R.id.btnAnonymous)
                //TODO
                // .setTosAndPrivacyPolicyId(R.id.baz)
                .build();

        /** Starts sign in activity */
        public static void startSignInActivity(AppCompatActivity activity) {
            // 1. Create sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .enableAnonymousUsersAutoUpgrade()
                    //TODO
                    // Keep Smart Lock's "hints" but disable the saving/retrieving of credentials
                    //.setIsSmartLockEnabled(false, true)
                    .setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.ic_launcher_background)
                    .setTheme(R.style.SignInTheme)
                    .setAuthMethodPickerLayout(customLayout)
//                .setTosAndPrivacyPolicyUrls(
//                        "https://example.com/terms.html",
//                        "https://example.com/privacy.html")
                    .build();

            // 2. Create ActivityResultLauncher and configure
            ActivityResultLauncher<Intent> signInLauncher = activity.registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    result -> onLoginInResult(result, activity)
            );

            // 3. Use ActivityResultLauncher to launch sign in intent
            signInLauncher.launch(signInIntent);
        }

//        public static Intent createSignInIntent() {
//            Intent signInIntent = AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setAvailableProviders(providers)
//                    .enableAnonymousUsersAutoUpgrade()
//                    //TODO
//                    // Keep Smart Lock's "hints" but disable the saving/retrieving of credentials
//                    //.setIsSmartLockEnabled(false, true)
//                    .setIsSmartLockEnabled(false)
//                    .setLogo(R.drawable.ic_launcher_background)
//                    .setTheme(R.style.SignInTheme)
//                    .setAuthMethodPickerLayout(customLayout)
////                .setTosAndPrivacyPolicyUrls(
////                        "https://example.com/terms.html",
////                        "https://example.com/privacy.html")
//                    .build();
//            return signInIntent;
//        }

        public static ActivityResultLauncher<Intent> createSignInLauncher(AppCompatActivity activity) {
            // 2. Create ActivityResultLauncher and configure
            ActivityResultLauncher<Intent> signInLauncher = activity.registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onLoginInResult(result, activity);
                        }
                    }
            );
            return signInLauncher;
        }

        public static void startSignInActivity(Intent intent, ActivityResultLauncher launcher) {
            launcher.launch(intent);
        }

        /** Get login result and handle responses */
        private static void onLoginInResult(FirebaseAuthUIAuthenticationResult result, Activity activity) {
            IdpResponse response = result.getIdpResponse();
            // 1. Logged in
            if (result.getResultCode() == RESULT_OK) {
                activity.startActivity(new Intent(activity, MainActivity.class));
                accountsManager.saveAccountToFirestore();
                activity.finish();
                // Show 'welcome' dialog if it is new user
                if (response.isNewUser()) {
                    // TODO SHOW WELCOME
                }
            }

            //TODO
            // store account in firestore
            // accountsManager.saveAccountToFirestore();

            // 2. Unable to login
            else {
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(activity, R.string.Signin_Cancelled, Toast.LENGTH_SHORT).show();
                    return;
                }
                // No Internet
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(activity, R.string.No_Internet_Connection, Toast.LENGTH_SHORT).show();
                    return;
                }
                // Unknown Error
                Toast.makeText(activity, R.string.Unknown_Error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
}
