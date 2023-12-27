package com.example.sgbusandlocationalarm;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.maps.model.zzg;

@Reserved({1})
@Class(
        creator = "LatLngCreator"
)
public final class MyLatLng extends AbstractSafeParcelable implements ReflectedParcelable {
    @KeepForSdk
    @NonNull
    public static final Parcelable.Creator<com.google.android.gms.maps.model.LatLng> CREATOR;
    @Field(
            id = 2
    )
    public final double latitude;
    @Field(
            id = 3
    )
    public final double longitude;

    public int hashCode() {
        long var1 = Double.doubleToLongBits(this.latitude);
        long var3 = Double.doubleToLongBits(this.longitude);
        return ((int)(var1 ^ var1 >>> 32) + 31) * 31 + (int)(var3 ^ var3 >>> 32);
    }

    @NonNull
    public String toString() {
        double var1 = this.latitude;
        double var3 = this.longitude;
        StringBuilder var5 = new StringBuilder();
        var5.append("lat/lng: (");
        var5.append(var1);
        var5.append(",");
        var5.append(var3);
        var5.append(")");
        return var5.toString();
    }

    static {
        zzg var0 = new zzg();
        CREATOR = var0;
    }

    @Constructor
    public MyLatLng(@Param(id = 2) double latitude, @Param(id = 3) double longitude) {
        if (!(longitude < -180.0) && longitude < 180.0) {
            this.longitude = longitude;
        } else {
            this.longitude = ((longitude + -180.0) % 360.0 + 360.0) % 360.0 + -180.0;
        }

        this.latitude = Math.max(-90.0, Math.min(90.0, latitude));
    }

    public MyLatLng() {
        latitude=0;
        longitude=0;
    }

    public void writeToParcel(@NonNull Parcel out, int var2) {
        var2 = SafeParcelWriter.beginObjectHeader(out);
        SafeParcelWriter.writeDouble(out, 2, this.latitude);
        SafeParcelWriter.writeDouble(out, 3, this.longitude);
        SafeParcelWriter.finishObjectHeader(out, var2);
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof com.google.android.gms.maps.model.LatLng)) {
            return false;
        } else {
            com.google.android.gms.maps.model.LatLng o1 = (com.google.android.gms.maps.model.LatLng)o;
            return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(o1.latitude) && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(o1.longitude);
        }
    }
}
