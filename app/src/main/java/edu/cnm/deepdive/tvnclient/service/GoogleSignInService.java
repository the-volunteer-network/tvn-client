package edu.cnm.deepdive.tvnclient.service;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import edu.cnm.deepdive.tvnclient.BuildConfig;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Provides and defines the Google sign-in authentication process.
 */
public class GoogleSignInService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private static Application context;

  private final GoogleSignInClient client;

  private GoogleSignInService() {

    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .requestProfile()
        .requestIdToken(BuildConfig.CLIENT_ID) // not necessary fir solitaire game
        .build();

    client = GoogleSignIn.getClient(context, options);

  }

  /**
   * Sets the context of this instance og Google sign in.
   * @param context
   */
  public static void setContext(Application context) {
    GoogleSignInService.context = context;
  }

  public static GoogleSignInService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public Single<GoogleSignInAccount> refresh() {
    return Single
        .create((SingleEmitter<GoogleSignInAccount> emitter) ->
            client
                .silentSignIn()
                .addOnSuccessListener((this::logAccount) )
                .addOnSuccessListener(t -> emitter.onSuccess(t))
                .addOnFailureListener(emitter::onError)

        )
        .observeOn(Schedulers.io());
  }

  /**
   * Refresh the bearer token of this application.
   * @return
   */
  public Single<String> refreshBearerToken() {
    return refresh()
        .map(this::getBearerToken);
  }

  @NonNull
  private String getBearerToken(GoogleSignInAccount account) {
    return String.format(BEARER_TOKEN_FORMAT, account.getIdToken());
  }

  private void logAccount(GoogleSignInAccount account) {

    if (account != null ) {
      Log.d(getClass().getSimpleName(), (account.getIdToken() != null ? getBearerToken(account): "(none)" ));

    }

  }

  /**
   * Starts  and launches the Google sign-in process.
   * @param launcher
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent());

  }

  /**
   * Complete the Google sign-in process
   * @param result
   * @return
   */
  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) {
    return Single
        .create((SingleEmitter<GoogleSignInAccount> emitter) -> {
          try {
            Task<GoogleSignInAccount> task =
                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            GoogleSignInAccount account = task.getResult(ApiException.class);
            logAccount(account);
            emitter.onSuccess(account);
          } catch (ApiException e) {
            emitter.onError(e);
          }
        })
        .observeOn(Schedulers.io());
  }

  /**
   * Terminates this instance of the Google sign-in and signs the user of the application out.
   * @return
   */
  public Completable signOut() {
    return Completable
        .create((emitter) ->
            client
                .signOut()
                .addOnSuccessListener((ignored) -> emitter.onComplete())
                .addOnFailureListener(emitter::onError)
        )
        .subscribeOn(Schedulers.io());
  }

  private static class InstanceHolder {

    private static final GoogleSignInService INSTANCE = new GoogleSignInService();
  }

}

