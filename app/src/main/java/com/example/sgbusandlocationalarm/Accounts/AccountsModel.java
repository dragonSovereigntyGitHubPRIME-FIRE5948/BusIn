package com.example.sgbusandlocationalarm.Accounts;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class AccountsModel implements Parcelable {

    // PROPERTIES //
    private String uid;
    private String username;
    private String email;
    @Nullable
    private String urlProfilePic;

    // CONSTRUCTOR //

    // java.lang.RuntimeException: Could not deserialize object. Class com.example.collabbees.Accounts.AccountModel
    // does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped
    //Because to deserialize using firestore requires no-arg constructor
    public AccountsModel(){}

    public AccountsModel(String uid, String username, String email, String urlProfilePic) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlProfilePic = urlProfilePic;
    }

    // GETTERS, SETTERS //
    public String getUsername() {return username;}
    @Nullable public String getUrlProfilePic() {return urlProfilePic;}
    public String getEmail() {return email;}
    public String getUid() {return uid;}

    // PARCELABLE //
    @Override
    public int describeContents() {return 0;}

    protected AccountsModel(Parcel in) {
        uid = in.readString();
        username = in.readString();
        email = in.readString();
        urlProfilePic = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(urlProfilePic);
    }

    public static final Creator<AccountsModel> CREATOR = new Creator<AccountsModel>() {
        @Override
        public AccountsModel createFromParcel(Parcel in) {
            return new AccountsModel(in);
        }

        @Override
        public AccountsModel[] newArray(int size) {
            return new AccountsModel[size];
        }
    };

}
