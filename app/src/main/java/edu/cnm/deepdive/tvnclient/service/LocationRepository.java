package edu.cnm.deepdive.tvnclient.service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LocationRepository {

  private static Application context;

  private final FusedLocationProviderClient locationClient;

  private Callback callback;

  private LocationRepository() {
    locationClient = LocationServices.getFusedLocationProviderClient(context);
  }

  public static void setContext(Application context) {
    LocationRepository.context = context;
  }

  public static LocationRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  @SuppressLint("MissingPermission") // TODO request location permission from the user
  public Single<Location> getLastLocation() {
    return Single
        .create((SingleOnSubscribe<Location>) (emitter) ->
            locationClient
                .getLastLocation()
                .addOnSuccessListener((location) -> emitter.onSuccess(location))
                .addOnFailureListener((throwable) -> emitter.onError(throwable))
        )
        .observeOn(Schedulers.io());

  }

  @SuppressLint("MissingPermission")
  public Observable<Location> getCurrentLocation() {
    return Observable
        .create((ObservableEmitter<Location> emitter) -> {
          LocationRequest request = LocationRequest.create();
          request.setInterval(15000); // FIXME Read from constants or preferences.
          request.setFastestInterval(5000); // FIXME Read from constants or preferences.
          request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
          LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
              .addLocationRequest(request)
              .build();
          SettingsClient client = LocationServices.getSettingsClient(context);
          client
              .checkLocationSettings(settingsRequest)
              .addOnSuccessListener(
                  (response) -> {/*  TODO use response to see what services are available*/})
              .addOnFailureListener((throwable) -> emitter.onError(throwable));
          callback = new Callback(emitter);
          locationClient.requestLocationUpdates(request, callback, Looper.myLooper());

        })
        .observeOn(Schedulers.io());
  }

  private static class Callback extends LocationCallback {

    private final ObservableEmitter emitter;

    private Callback(ObservableEmitter emitter) {
      this.emitter = emitter;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
      super.onLocationResult(locationResult);
      emitter.onNext(locationResult.getLastLocation());
    }

    @Override
    public void onLocationAvailability(LocationAvailability locationAvailability) {
      super.onLocationAvailability(locationAvailability);
    }
  }

  private static class InstanceHolder {

    private static final LocationRepository INSTANCE = new LocationRepository();
  }
}
