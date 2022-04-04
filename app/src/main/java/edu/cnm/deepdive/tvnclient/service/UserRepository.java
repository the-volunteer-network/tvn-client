package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.model.dto.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.UUID;

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
    return refreshToken()
        .flatMap((token) -> serviceProxy.getCurrentUser(token));

  }

  public Completable modifyCurrentUser(UUID organizationId, Organization organization) {
    return refreshToken()
        .flatMap(
            (token) -> serviceProxy.modifyOrganization(organizationId, organization, token)) //;
        .ignoreElement();
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

  private Single<String> refreshToken() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io());

  }


}
