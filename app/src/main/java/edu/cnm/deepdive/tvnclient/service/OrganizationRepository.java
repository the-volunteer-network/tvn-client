package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides support for asynchronicity for services regarding the relationships of users, organizations, and opportunities.
 */
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

  /**
   * Gets a list of all organizations.
   * @return
   */
  public Single<List<Organization>> getAll() {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getAllOrganizations(token));
  }

  /**
   * Gets a favorited organization based on organization id
   * @param organizationId
   * @return
   */
  public Single<Boolean> getFavorite(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.isFavorite(organizationId, token));
  }

  /**
   * Gets list of organizations user has favorited, volunteered for, and owner of.
   * @return
   */
  public Single<List<Organization>> getMyOrganizations() {
    return refreshToken()
        .flatMap((token) ->
            Single
                .zip(
                    serviceProxy.getFavorites(token),
                    serviceProxy.getVolunteers(token),
                    serviceProxy.getOwnedOrganizations(token),
                    (favorites, volunteers, owned) -> {
                      return Stream
                          .concat(
                              favorites.stream(),
                              Stream.concat(volunteers.stream(), owned.stream())
                          )
                          .distinct()
                          .sorted((orgA, orgB) -> orgA.getName().compareToIgnoreCase(orgB.getName()))
                          .collect(Collectors.toList());
                    }
                )
        );
  }

  /**
   * Sets organization as favorite.
   * @param organizationId
   * @param favorite
   * @return
   */
  public Single<Boolean> setFavorite(UUID organizationId, boolean favorite) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.setFavorite(organizationId, favorite, token));
  }

  /**
   * Sets user as volunteer for organization.
   * @param organizationId
   * @param volunteer
   * @return
   */
  public Single<Boolean> setVolunteer(UUID organizationId, boolean volunteer) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.setVolunteer(organizationId, volunteer, token));
  }

  /**
   *
   * @param organization
   * @return
   */
  public Single<Organization> addOrganization(Organization organization) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.addOrganization(organization, token));
  }

  /**
   * Provides a list of organizations that the user can view.
   * @param keyword
   * @return
   */
  public Single<List<Organization>> searchOrganizations(String keyword) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.findOrganizations(keyword, token));
  }

  /**
   * Provides a list of opportunities for the user to view.
   * @param keyword
   * @return
   */
  public Single<List<Opportunity>> searchOpportunities(String keyword){
    return refreshToken()
        .flatMap((token) -> serviceProxy.findOpportunities(keyword, token));
  }

  // TODO Decide if we want the result, or just the fact that it was completed.
  //  public Single<Organization> modifyOrganization(UUID organizationId, Organization organization) {

  /**
   * Modification of an organization does not invoke any visible action.
   * @param organizationId
   * @param organization
   * @return
   */
  public Completable modifyOrganization(UUID organizationId, Organization organization) {
    return refreshToken()
        .flatMap(
            (token) -> serviceProxy.modifyOrganization(organizationId, organization, token)) //;
        .ignoreElement();
  }

  /**
   * Gets organization based on id.
   * @param organizationId
   * @return
   */
  public Single<Organization> getOrganization(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getOrganization(organizationId, token));
  }

  /**
   * Gets organization based on name.
   * @param organizationId
   * @return
   */
  public Single<String> getOrganizationName(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getOrganizationName(organizationId, token));
  }

  /**
   *
   * @param organizationId
   * @return
   */
  public Single<String> setOrganizationName(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.setOrganizationName(organizationId, token));
  }

  /**
   * Delete organization using id.
   * @param organizationId
   * @return
   */
  public Completable deleteOrganization(UUID organizationId) {
    return refreshToken()
        .flatMapCompletable((token) -> serviceProxy.deleteOrganization(organizationId, token));
  }

  /**
   * Acquires a list of all opportunities.
   * @param organizationId
   * @return
   */
  public Single<List<Opportunity>> getAllOpportunities(UUID organizationId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getAllOpportunities(organizationId, token));
  }

  /**
   * Adds an opportunity to the list.
   * @param organizationId
   * @param opportunity
   * @return
   */
  public Single<Opportunity> addOpportunity(UUID organizationId, Opportunity opportunity) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.addOpportunity(organizationId, opportunity, token));
  }

  // TODO Decide if we want the result, or just the fact that it was completed.

  /**
   * Modification of opportunity.
   * @param organizationId
   * @param opportunityId
   * @param opportunity
   * @return
   */
  public Single<Opportunity> modifyOpportunity(UUID organizationId, UUID opportunityId,
      Opportunity opportunity) {
    return refreshToken()
        .flatMap((token) ->
            serviceProxy.modifyOpportunity(organizationId, opportunityId, opportunity, token));
  }

  /**
   * View an opportunity of an organization.
   * @param organizationId
   * @param opportunityId
   * @return
   */
  public Single<Opportunity> getOpportunity(UUID organizationId, UUID opportunityId) {
    return refreshToken()
        .flatMap((token) -> serviceProxy.getOpportunity(organizationId, opportunityId, token));
  }

  /**
   * Deletes opportunity of an organization.
   * @param organizationId
   * @param opportunityId
   * @return
   */
  public Completable deleteOpportunity(UUID organizationId, UUID opportunityId) {
    return refreshToken()
        .flatMapCompletable((token) ->
            serviceProxy.deleteOpportunity(organizationId, opportunityId, token));
  }

  /**
   * Refreshes bearertoken for continuous service.
   * @return
   */
  private Single<String> refreshToken() {
    return signInService
        .refreshBearerToken()
        .observeOn(Schedulers.io());

  }

}
