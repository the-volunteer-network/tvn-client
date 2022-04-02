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
  private final MutableLiveData<Opportunity> opportunityId;
  private final MutableLiveData<MutableLiveData<Opportunity>> opportunities;
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
    opportunities = new MutableLiveData<MutableLiveData<Opportunity>>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchAll();
  }

  public LiveData<List<Organization>> getAllOrganizations() {
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

  public void addOrganizationId(UUID id) {
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

  public LiveData<UUID> getOrganizationId(UUID id) {
    return organizationId;
  }

  public void setOrganizationId(UUID id) {
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

  public void modifyOrganizationId(UUID id) {
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
            (org) -> organization.postValue(org),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public void setOrganizationName(UUID id) {
    throwable.setValue(null);
    organizationId.setValue(id);
    organizationRepository
        .setOrganizationName(id)
        .subscribe(
            (org) -> organization.postValue(org),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public void addOrganizationName(UUID id) {
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

  public LiveData<Opportunity> getAllOpportunities(UUID id) {
    return opportunities;
  }

  public LiveData<Opportunity> addOpportunity(UUID id) {
    throwable.setValue(null);
    organization.setValue(id);
    organizationRepository
        .getOrganizationName(id)
        .subscribe(
            (opp) -> opportunities.postValue(opportunity),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public void getOpportunity(UUID id) {
    throwable.setValue(null);
    opportunityId.setValue(id);
    organizationRepository
        .getOrganizationName(id)
        .subscribe(
            (org) -> organization.postValue(org),
            (throwable) -> postThrowable(throwable),
            pending
        );
  }

  public void modifyOpportunity(UUID id) {
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

  public void deleteOpportunity(UUID id) {
    throwable.setValue(null);
    organizationRepository
        .deleteOpportunity(id)
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
