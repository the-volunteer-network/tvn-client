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

public class LocationViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final LocationRepository locationRepository;
  private final MutableLiveData<Location> location;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  public LocationViewModel(@NonNull Application application) {
    super(application);
    locationRepository = LocationRepository.getInstance();
    location = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    subscribeToLocation();
  }

  public LiveData<Location> getLocation() {
    return location;
  }

  public void subscribeToLocation() {
    throwable.postValue(null);
    locationRepository
        .getCurrentLocation()
        .subscribe(
            (loc) -> location.postValue(loc),
            (throwable) -> postThrowable(throwable),
            () -> {
            },
            pending
        );
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStop(owner);
    pending.clear();
  }

  private void postThrowable(Throwable throwable) {
    Log.e(getClass().getSimpleName(), throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
