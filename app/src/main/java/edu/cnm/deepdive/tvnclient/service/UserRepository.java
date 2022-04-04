package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;
import edu.cnm.deepdive.tvnclient.model.dto.User;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserRepository {


  private final Context context;
  private final TVNServiceProxy serviceProxy;
  private final GoogleSignInService signInService;


  public UserRepository(Context context) {
    this.context = context;
    serviceProxy = TVNServiceProxy.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  public Single<User> getProfile() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.getCurrentUser(token));

  }

  public Single<User> updateProfile(User user) {
/*
    return  signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.)
*/
    return null; // TODO Finish when endpoint is available.
  }

}
