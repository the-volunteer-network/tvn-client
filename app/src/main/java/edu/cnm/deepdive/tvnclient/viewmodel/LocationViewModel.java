package edu.cnm.deepdive.tvnclient.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.tvnclient.service.LocationRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * Prepares and manages the data for the display and use of the Google map;
 * Handles the communication of the display of the map with test of the application.
 */
public class LocationViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final LocationRepository locationRepository;

  public LocationViewModel(@NonNull Application application) {
    super(application);
    locationRepository = LocationRepository.getInstance();
  }

  public LiveData<Location> getLocation() {
    return locationRepository.getLocation();
  }

  @Override
  public void onResume(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onResume(owner);
    locationRepository.start();
  }

  @Override
  public void onPause(@NonNull LifecycleOwner owner) {
    locationRepository.stop();
    DefaultLifecycleObserver.super.onPause(owner);
  }
}
