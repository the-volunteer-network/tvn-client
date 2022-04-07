package edu.cnm.deepdive.tvnclient.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PermissionsRepository {

  private final Set<String> permissions;

  private PermissionsRepository() {
    permissions = new HashSet<>();
  }

  public static PermissionsRepository getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public boolean add(String permission) {
    return permissions.add(permission);
  }

  public boolean remove(String permission) {
    return permissions.remove(permission);
  }

  public Set<String> getPermissions() {
    return Collections.unmodifiableSet(permissions);
  }

  private static class InstanceHolder {

    private static final PermissionsRepository INSTANCE = new PermissionsRepository();
  }
}
