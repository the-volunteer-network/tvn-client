package edu.cnm.deepdive.tvnclient.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.tvnclient.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Provide data to the view so the ew view can display those data on screen,
 * Allows the user to interact with data and change the data.
 */
public class UserViewModel extends AndroidViewModel implements DefaultLifecycleObserver{

  private final GoogleSignInService signInService;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  /**
   * Initializes this instance of LoginViewModel.
   * @param application
   */
  public UserViewModel(@NonNull Application application) {
    super(application);
    signInService = GoogleSignInService.getInstance();
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    refresh();
  }

  /**
   * Observes the Google sign-in process and notifies if any changes occurs.
   * @return
   */
  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  /**
   * Notifies if an error occurs during the process.
   * @return
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Refresh the sign-in process if the user has previously logged in.
   */
  public void refresh() {
    throwable.setValue(null);
    Disposable disposable = signInService
        .refresh()
        .subscribe(
            account::postValue,
            (throwable) -> account.postValue(null)

        );
    pending.add(disposable);
  }

  /**
   * Complete the sign-in process and display the completion on screen.
   * @param result
   */
  public void completeSignIn(ActivityResult result) {
    throwable.setValue(null);
    Disposable disposable = signInService
        .completeSignIn(result)
        .subscribe(
            account::postValue,
            this::postThrowable
        );
    pending.add(disposable);
  }

  /**
   * Launches the sign-in process and display it on screen.
   * @param launcher
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    signInService.startSignIn(launcher);
  }

  /**
   *  Provides the user the sign-out option and display the sign-out on screen.
   */
  public void signOut() {
    throwable.setValue(null);
    Disposable disposable = signInService
        .signOut()
        .doFinally(() -> account.postValue(null))
        .subscribe(
            () -> {
            },
            this::postThrowable
        );
    pending.add(disposable);
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
