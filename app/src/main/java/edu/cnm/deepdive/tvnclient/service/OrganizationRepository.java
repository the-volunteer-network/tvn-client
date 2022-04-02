package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
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

  public Single<List<Organization>> getAll() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.getAllOrganizations(token));
  }

  public Single<Boolean> getFavorite(UUID organizationId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.isFavorite(organizationId, token));
  }

  public Single<Boolean> setFavorite(UUID organizationId, boolean favorite) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.setFavorite(organizationId, favorite, token));
  }

  public Single<Organization> addOrganization(Organization organization) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.addOrganization(organization, token));
  }

  public Single<List<Organization>> getAllOrganizations(Organization organization) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.findOrganizations(String.valueOf(organization), token));
  }

  public Single<List<Organization>> findOrgnizations(Organization organization) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.findOrganizations(String.valueOf(organization) , token));
  }


  public Single<Organization> modifyOrganization(UUID organizationId, Organization organization) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.modifyOrganization(organizationId, organization, token));

  }

  public Single<Organization> getOrganization(UUID organizationId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.getOrganization(organizationId, token));
  }


  public Single<String> getOrganizationName(UUID organizationId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.getOrganizationName(organizationId, token));
  }

  public Single<String> setOrganizationName(UUID organizationId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.getOrganizationName(organizationId, token));
  }


  public Completable deleteOrganization(UUID organizationId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMapCompletable((token) -> serviceProxy.deleteOrganization(organizationId, token));
  }

  public Single<Opportunity> addOpportunity(UUID organizationId, Opportunity opportunity) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.addOpportunity(organizationId, opportunity, token));
  }

  public Single<Opportunity> modifyOpportunity(UUID organizationId, UUID opportunityId,
      Opportunity opportunity) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap(
            (token) -> serviceProxy.modifyOpportunity(organizationId, opportunityId, opportunity,
                token));
  }

  public Single<Opportunity> getOpportunity(UUID organizationId, UUID opportunityId) {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io())
        .flatMap((token) -> serviceProxy.getOpportunity(organizationId, opportunityId, token));
  }
}
