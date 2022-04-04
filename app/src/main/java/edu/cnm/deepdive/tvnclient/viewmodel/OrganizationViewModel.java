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
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchAll();
  }

  public LiveData<List<Organization>> getAllOrganizations() {
    throwable.setValue(null);
    Disposable disposable = organizationRepository
        .getAll()
        .subscribe(
            value -> organizations.postValue(value),
            throwable -> postThrowable(throwable)
        );
    pending.add(disposable);
    return organizations;
  }

  public LiveData<List<Organization>> findOrganizations() {
    throwable.setValue(null);
    Disposable disposable = organizationRepository
        .getAll()
        .subscribe(
            organizations::postValue,
            this::postThrowable
        );
    pending.add(disposable);
    return organizations;
  }
  public void getOrganization (UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .getOrganization(id)
        .subscribe(
            (org) -> organization.postValue(org),
            (throwable) -> postThrowable(throwable)
        );
  }

  public void addOrganization( Organization org) {
    throwable.setValue(null);
    organizationRepository
        .addOrganization(org)
        .subscribe(
            (orga) -> organization.postValue(orga ),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public LiveData<UUID> getOrganizationId(UUID id) {
    return organizationId;
  }

/*  public void setOrganizationId(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .getOrganization(id)
        .subscribe(
            (org) -> organization.postValue(org),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }*/

  public void modifyOrganizationId(UUID id, Organization org) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .modifyOrganization(id, org)
        .subscribe(
            (orga) -> organization.postValue(orga),
            (throwable) -> postThrowable(throwable),
            pending
        );

  }

  public void deleteOrganization(UUID id) {
    throwable.setValue(null);
    organizationRepository
        .deleteOrganization(id)
        .subscribe(
            () -> {/* Do nothing */},
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

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



  public LiveData<List<Opportunity>> getAllOpportunities() {
    throwable.setValue(null);
    Disposable disposable = organizationRepository
        .getAll()
        .subscribe(


        );
    return opportunities;
  }

  public void  addOpportunity( UUID orgId, Opportunity opp) {
   throwable.setValue(null);

   organizationRepository
       .addOpportunity(orgId, opp)
       .subscribe(
           (opport) -> opportunity.postValue(opport),
           (throwable)->postThrowable(throwable),
           pending
       );


  }

 /* public void getOpportunity(UUID oppId, UUID orgId) {
    throwable.setValue(null);
    opportunityId.setValue(oppId);
    organizationRepository
        .getOpportunity(oppId,orgId)
        .subscribe(
            (opp) -> opportunity.postValue(opp),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }*/

  public void modifyOpportunity(UUID orgId,UUID oppId, Opportunity opp) {
    throwable.setValue(null);
    opportunityId.setValue(oppId);
    organizationRepository
        .modifyOpportunity(orgId,oppId, opp)
        .subscribe(
            (oppo) -> opportunity.postValue(oppo),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public void deleteOpportunity(UUID orgId, UUID oppId) {
    throwable.setValue(null);
    organizationRepository
        .deleteOpportunity(orgId, oppId)
        .subscribe(
            () -> {/* Do nothing */},
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void fetchAll() {
    throwable.setValue(null);
    organizationRepository
        .getAll()
        .subscribe(
            (orgs) -> organizations.postValue(orgs),
            (throwable) -> postThrowable(throwable),
            pending
        );
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
