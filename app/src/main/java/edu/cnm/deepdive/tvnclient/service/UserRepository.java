package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.model.dto.User;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public Single<User> modifyProfile(User user) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.modifyCurrentUser(user, token));

  }

  public Completable modifyCurrentUser(UUID organizationId, Organization organization) {
    return refreshToken()
        .flatMap(
            (token) -> serviceProxy.modifyOrganization(organizationId, organization, token)) //;
        .ignoreElement();
  }

  public Single<List<Organization>> getMyOrganizations() {
    return refreshToken()
        .flatMap((token) ->
            Single
                .zip(
                    serviceProxy.getFavorites(token),
                    serviceProxy.getVolunteers(token),
//                    serviceProxy.getOwnedOrganizations(token),
                    (favorites, volunteers /* TODO add the list return for my OWNED organizations */) -> {
                      return Stream
                          .concat(
                              favorites.stream(),
                              volunteers.stream() // TODO add the list return for my OWNED organizations as argument
                          )
                          .distinct()
                          .sorted((orgA, orgB) -> orgA.getName().compareToIgnoreCase(orgB.getName()))
                          .collect(Collectors.toList());
                    }
                )
            );
  }

  private Single<String> refreshToken() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io());
  }


}
