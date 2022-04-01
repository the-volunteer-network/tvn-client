package edu.cnm.deepdive.tvnclient.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.service.OrganizationRepository;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("Convert2MethodRef")
public class OrganizationViewModel extends AndroidViewModel implements DefaultLifecycleObserver {

  private final OrganizationRepository organizationRepository;
  private final MutableLiveData<List<Organization>> organizations;
  private final MutableLiveData<UUID> organizationId;
  private final MutableLiveData<Organization> organization;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;


  public OrganizationViewModel(
      @NonNull Application application) {
    super(application);
    organizationRepository = new OrganizationRepository(application);
    organizations = new MutableLiveData<>();
    organizationId = new MutableLiveData<>();
    organization = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    fetchAll();
  }

  public LiveData<List<Organization>> getOrganizations() {
    return organizations;
  }

  public LiveData<UUID> getOrganizationId() {
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
