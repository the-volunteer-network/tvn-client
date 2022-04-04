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
    return refreshToken()
        .flatMap((token) -> serviceProxy.getAllOrganizations(token));
  }

  public Single<Boolean> getFavorite(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.isFavorite(organizationId, token));
  }

  public Single<Boolean> setFavorite(UUID organizationId, boolean favorite) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.setFavorite(organizationId, favorite, token));
  }

  public Single<Organization> addOrganization(Organization organization) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.addOrganization(organization, token));
  }

  public Single<List<Organization>> searchOrganizations(String keyword) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.findOrganizations(keyword, token));
  }

  // TODO Decide if we want the result, or just the fact that it was completed.
  //  public Single<Organization> modifyOrganization(UUID organizationId, Organization organization) {
  public Completable modifyOrganization(UUID organizationId, Organization organization) {
    return refreshToken()
        .flatMap(
            (token) -> serviceProxy.modifyOrganization(organizationId, organization, token)) //;
        .ignoreElement();
  }

  public Single<Organization> getOrganization(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getOrganization(organizationId, token));
  }


  public Single<String> getOrganizationName(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getOrganizationName(organizationId, token));
  }

  public Single<String> setOrganizationName(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.setOrganizationName(organizationId, token));
  }


  public Completable deleteOrganization(UUID organizationId) {
    return refreshToken()
        .flatMapCompletable((token) -> serviceProxy.deleteOrganization(organizationId, token));
  }

  public Single<List<Opportunity>> getAllOpportunities(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getAllOpportunities(organizationId, token));
  }

  public Single<Opportunity> addOpportunity(UUID organizationId, Opportunity opportunity) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.addOpportunity(organizationId, opportunity, token));
  }

  // TODO Decide if we want the result, or just the fact that it was completed.
  public Single<Opportunity> modifyOpportunity(UUID organizationId, UUID opportunityId,
      Opportunity opportunity) {
    return refreshToken()
        .flatMap((token) ->
            serviceProxy.modifyOpportunity(organizationId, opportunityId, opportunity, token));
  }

  public Single<Opportunity> getOpportunity(UUID organizationId, UUID opportunityId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getOpportunity(organizationId, opportunityId, token));
  }

  public Completable deleteOpportunity(UUID organizationId, UUID opportunityId) {
    return refreshToken()
        .flatMapCompletable((token) ->
            serviceProxy.deleteOpportunity(organizationId, opportunityId, token));
  }

  private Single<String> refreshToken() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io());

  }

}
