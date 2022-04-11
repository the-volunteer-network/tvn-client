package edu.cnm.deepdive.tvnclient.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.service.OrganizationRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;
import java.util.UUID;
import edu.cnm.deepdive.tvnclient.controller.SearchOrganizationFragment;
import  edu.cnm.deepdive.tvnclient.model.dto.User;


/**
 *  Prepares and manages the data for the {@link Organization },  {@link SearchOrganizationFragment} and {@link Opportunity} fragment .
 *  Handles the communication of the {@link Organization },  {@link SearchOrganizationFragment} and {@link Opportunity}with the rest of the application.
 */
@SuppressWarnings("Convert2MethodRef")
public class OrganizationViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final OrganizationRepository organizationRepository;
  private final MutableLiveData<List<Organization>> organizations;
  private final MutableLiveData<UUID> organizationId;
  private final MutableLiveData<Organization> organization;
  private final MutableLiveData<Opportunity> opportunity;
  private final MutableLiveData<UUID> opportunityId;
  private final MutableLiveData<List<Opportunity>> opportunities;
  private final MutableLiveData<Boolean>  favorites;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  /**
   *Initialize this instance of {@link OrganizationViewModel} with the injected parameters.
   * @param application
   */
  public OrganizationViewModel(
      @NonNull Application application) {
    super(application);
    organizationRepository = new OrganizationRepository(application);
    organizations = new MutableLiveData<>();
    organizationId = new MutableLiveData<>();
    organization = new MutableLiveData<>();
    opportunity = new MutableLiveData<>();
    opportunityId = new MutableLiveData<>();
    opportunities = new MutableLiveData<>();
    favorites = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
//    fetchAllOrganizations(); // TODO Decide whether search fragment should start with ALL orgs or no orgs.
  }

  /**
   * Retrieves all the {@link Organization} from the database.
   */
  public void fetchAllOrganizations() {
    throwable.postValue(null);
    organizationRepository
        .getAll()
        .subscribe(
            (orgs) -> organizations.postValue(orgs),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Retrieves all of the  {@link Organization} from the database specific to {@link User }
   */
  public void fetchMyOrganizations() {
    throwable.postValue(null);
    organizationRepository
        .getMyOrganizations()
        .subscribe(
            (orgs) -> organizations.postValue(orgs),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Retrieves a List of all {@link Organization}
   * @return
   */
  public LiveData<List<Organization>> getOrganizations() {
    return organizations;
  }

  /**
   * Searches and retrieves the specific {@link Organization} specific to {@code keyword}
   * @param keyword
   */
  public void findOrganizations(String keyword) {
    throwable.setValue(null);
    Disposable disposable = organizationRepository
        .searchOrganizations(keyword)
        .subscribe(
            organizations::postValue,
            this::postThrowable
        );
    pending.add(disposable);
  }

  /**
   * Searches and retrieves the specific {@link Opportunity} specific to {@code keyword}
   * @param keyword
   */
  public void findOpportunity(String keyword) {
    throwable.setValue(null);
    Disposable disposable = organizationRepository
        .searchOpportunities(keyword)
        .subscribe(
            (opp) ->  {
              opportunities.postValue(opp);},
                  (throwable) -> postThrowable(throwable),
            pending

  );
  }

  /**
   * Sets the current instance of {@link Organization} as {@code favorite}
   * @param id      id passed to set this instance
   * @param organization passed to set this instance
   * @param favorite
   */
  public void setFavorite(UUID id, Organization organization, boolean favorite){
    throwable.setValue(null);

    organizationRepository
        .setFavorite(id, favorite)
        .subscribe(
            (fav) ->{
              organization.setFavorite(fav);
              this.organization.postValue(organization);
              organizations.postValue(organizations.getValue());
            } ,
            (throwable)-> postThrowable(throwable),
            pending
        );
  }

  /**
   * Sets the current instance of {@link Organization} as {@code favorite}
   * @param id      id passed to set this instance
   * @param organization passed to set this instance
   * @param volunteer
   */
  public void setVolunteer(UUID id, Organization organization, boolean volunteer){
    throwable.setValue(null);
    organizationRepository
        .setVolunteer(id, volunteer)
        .subscribe(
            (vol) ->{
              organization.setVolunteer(vol);
              this.organization.postValue(organization);
              organizations.postValue(organizations.getValue());
            } ,
            (throwable)-> postThrowable(throwable),
            pending
        );
  }

  /**
   * Retrieves the specific instance of {@link Organization}
   * @return
   */
  public LiveData<Organization> getOrganization() {
    return organization;
  }

  /**
   * Adds the specified instance of {@link Organization} to the database
   * @param org
   */
  public void addOrganization(Organization org) {
    throwable.setValue(null);
    organizationRepository
        .addOrganization(org)
        .subscribe(
            (orga) -> {
              organization.postValue(orga);
              organizationId.postValue(orga.getId());
              fetchAllOrganizations(); // TODO This is the approach where the list of organizations is refreshed if we change 1 organization
            },
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Returns a LiveData of the {@code id} of the specified {@link Organization}
   * @param id
   * @return
   */
  public LiveData<UUID> getOrganizationId(UUID id) {
    return organizationId;
  }

  /**
   * Returns the specified {@link Organization}
   * @param id
   */
  public void fetchOrganization(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .getOrganization(id)
        .subscribe(
            (org) -> organization.postValue(org),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Modifies the specified instance of {@link  Organization}
   * @param id
   * @param org
   */
  public void modifyOrganization(UUID id, Organization org) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .modifyOrganization(id, org)
        .subscribe(
//            (orga) -> organization.postValue(orga),
            () -> {
              /* TODO Decide if there is a necessary action after successful modification*/
              fetchAllOrganizations(); // TODO This is the approach where the list of organizations is refreshed if we change 1 organization
            },
            (throwable) -> postThrowable(throwable),
            pending
        );

  }

  /**
   * Deletes the specified {@link Organization}
   * @param id
   */
  public void deleteOrganization(UUID id) {
    throwable.setValue(null);
    organizationRepository
        .deleteOrganization(id)
        .subscribe(
            () -> fetchAllOrganizations(),
            // TODO This is the approach where the list of organizations is refreshed if we change 1 organization
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Returns the name of an {@link Organization} specific to its {@code id}.
   * @param id
   */
  // TODO Decide if you want to modify one prop at time? If not, remove the 2 below.
  public void getOrganizationName(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .getOrganizationName(id)
        .subscribe(

        );
  }

  /**
   * Sets the name of an {@link Organization} specific to its {@code id}.
   * @param id
   */
  public void setOrganizationName(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .setOrganizationName(id)
        .subscribe(

        );

  }

  /**
   * Retrieves all of the {@link Opportunity} from the database
   * @param organizationId
   */
  public void fetchAllOpportunities(UUID organizationId) {
    throwable.postValue(null);
    Disposable disposable = organizationRepository
        .getAllOpportunities(organizationId)
        .subscribe(
            (opps) -> opportunities.postValue(opps),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Retrieves a List of all {@link Opportunity}
   * @return
   */
  public LiveData<List<Opportunity>> getOpportunities() {
    return opportunities;
  }

  /**
   * Adds the specified instance of {@link Organization} to the database specific to its{@code orgId}
   * @param orgId
   * @param opp
   */
  public void addOpportunity(UUID orgId, Opportunity opp) {
    throwable.setValue(null);
    organizationRepository
        .addOpportunity(orgId, opp)
        .subscribe(
            (opport) -> {
              opportunity.postValue(opport);
              opportunityId.postValue(opport.getId());
              fetchAllOpportunities(orgId);
            },
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Retrieves the specific instance of {@link Opportunity}
   * @return
   */
  public LiveData<Opportunity> getOpportunity() {
    return opportunity;
  }

  /**
   * Returns the specified {@link Opportunity}
   * @param oppId
   * @param orgId
   */
  public void fetchOpportunity(UUID oppId, UUID orgId) {
    throwable.setValue(null);
    opportunityId.setValue(oppId);
    organizationRepository
        .getOpportunity(oppId, orgId)
        .subscribe(
            (opp) -> opportunity.postValue(opp),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  /**
   * Modifies the specified instance of {@link  Organization} specific to its organization id and opportunity id.
   * @param orgId
   * @param oppId
   * @param opp
   */
  public void modifyOpportunity(UUID orgId, UUID oppId, Opportunity opp) {
    throwable.setValue(null);
    opportunityId.setValue(oppId);
    organizationRepository
        .modifyOpportunity(orgId, oppId, opp)
        .subscribe(
            (oppo) -> {
              opportunity.postValue(oppo);
              fetchAllOpportunities(orgId);
            },
            (throwable) -> postThrowable(throwable),
            pending
        );
  }
// TODO If we don't want to do anything upon a successful result (above), then change repository method to return completable

  /**
   * Deletes the specified {@link Opportunity}
   * @param orgId
   * @param oppId
   */
  public void deleteOpportunity(UUID orgId, UUID oppId) {
    throwable.setValue(null);
    organizationRepository
        .deleteOpportunity(orgId, oppId)
        .subscribe(
            () -> fetchAllOpportunities(orgId),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }


  /**
   * Returns a LiveData of {@code Throwable}
   * @return
   */
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
