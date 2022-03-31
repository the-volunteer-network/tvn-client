package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.UUID;

public class OrganizationRepository {

  private final Context context;

  private final GoogleSignInService signInService;

  private final TVNServiceProxy serviceProxy;

  public OrganizationRepository(Context context) {
    this.context = context;
    signInService = GoogleSignInService.getInstance();
    serviceProxy = TVNServiceProxy.getInstance();
  }

  // TODO Add methods for creating, modifying and deleting organizations.

  public Single<Boolean> getFavorite(UUID organizationId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.isFavorite(organizationId, token));
  }

  public Single<Boolean> setFavorite (UUID organizationId, boolean favorite) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.setFavorite(organizationId,favorite,  token));
  }
}
