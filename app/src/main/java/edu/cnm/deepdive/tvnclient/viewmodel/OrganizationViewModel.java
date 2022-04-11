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

  public LiveData<List<Organization>> getOrganizations() {
    return organizations;
  }


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


  public LiveData<Organization> getOrganization() {
    return organization;
  }

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

  public LiveData<UUID> getOrganizationId(UUID id) {
    return organizationId;
  }

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

  // TODO Decide if you want to modify one prop at time? If not, remove the 2 below.
  public void getOrganizationName(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .getOrganizationName(id)
        .subscribe(

        );
  }

  public void setOrganizationName(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .setOrganizationName(id)
        .subscribe(

        );

  }


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

  public LiveData<List<Opportunity>> getOpportunities() {
    return opportunities;
  }

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

  public LiveData<Opportunity> getOpportunity() {
    return opportunity;
  }

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
