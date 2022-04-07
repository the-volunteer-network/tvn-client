package edu.cnm.deepdive.tvnclient.controller;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import edu.cnm.deepdive.tvnclient.R;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class PermissionsExplanationFragment extends DialogFragment {

  private String[] permissions;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    permissions = PermissionsExplanationFragmentArgs.fromBundle(getArguments()).getPermissions();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    String packageName = getContext().getPackageName();
    Resources res = getResources();
    String message = Arrays
        .stream(permissions)
        .map((permission) -> {
          String[] parts = permission.split("\\.");
          String permissionKey = parts[parts.length - 1].toLowerCase() + "_explanation";
          int id = res.getIdentifier(permissionKey, "string", packageName);
          return (id != 0) ? getString(id) : null;
        })
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.joining("\n"));
    OnAcknowledgeListener listener = ((OnAcknowledgeListener) getActivity());
    return new AlertDialog.Builder(getContext())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(R.string.permissions_dialog_title)
        .setMessage(message)
        .setNeutralButton(android.R.string.ok, (d, w) -> listener.onAcknowledge(permissions))
        .create();
  }

  public interface OnAcknowledgeListener{
    void onAcknowledge(String[] permissions);
  }
}
