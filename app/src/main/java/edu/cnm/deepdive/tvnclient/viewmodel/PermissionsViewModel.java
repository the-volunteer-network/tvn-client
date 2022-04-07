package edu.cnm.deepdive.tvnclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.tvnclient.service.PermissionsRepository;
import java.util.Set;

public class PermissionsViewModel extends ViewModel {

  private final PermissionsRepository repository = PermissionsRepository.getInstance();
  private final MutableLiveData<Set<String>> permissions = new MutableLiveData<>();

  public MutableLiveData<Set<String>> getPermissions() {
    return permissions;
  }

  public boolean add(String permission) {
    boolean added = repository.add(permission);
    if (added) {
      permissions.setValue(repository.getPermissions());
    }
    return added;
  }

  public boolean remove(String permission) {
    boolean removed = repository.remove(permission);
    if (removed) {
      permissions.setValue(repository.getPermissions());
    }
    return removed;
  }
}
