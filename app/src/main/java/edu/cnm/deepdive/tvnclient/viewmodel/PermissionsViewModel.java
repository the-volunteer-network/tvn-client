package edu.cnm.deepdive.tvnclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.tvnclient.service.PermissionsRepository;
import java.util.Set;

/**
 * Provides access to user's phone location to provide local organizations.
 */
public class PermissionsViewModel extends ViewModel {

  private final PermissionsRepository repository = PermissionsRepository.getInstance();
  private final MutableLiveData<Set<String>> permissions = new MutableLiveData<>();

  /**
   * Holds a value of this instance.
   * @return
   */
  public MutableLiveData<Set<String>> getPermissions() {
    return permissions;
  }

  /**
   * Adding permission to location.
   * @param permission
   * @return
   */
  public boolean add(String permission) {
    boolean added = repository.add(permission);
    if (added) {
      permissions.setValue(repository.getPermissions());
    }
    return added;
  }

  /**
   * Removing permission to location.
   * @param permission
   * @return
   */
  public boolean remove(String permission) {
    boolean removed = repository.remove(permission);
    if (removed) {
      permissions.setValue(repository.getPermissions());
    }
    return removed;
  }
}
