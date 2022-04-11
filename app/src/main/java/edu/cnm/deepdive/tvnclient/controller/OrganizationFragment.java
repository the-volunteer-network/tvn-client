package edu.cnm.deepdive.tvnclient.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.tvnclient.databinding.FragmentOrganizationBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import java.util.UUID;

/**
 * Defines, manages and inflates the {@code fragment_organization.xml} layout.
 * Handles its layout lifecycle and input events.
 */
public class OrganizationFragment extends DialogFragment {

  private OrganizationViewModel organizationViewModel;
  private FragmentOrganizationBinding binding;
  private UUID organizationId;
  private Organization organization;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    organizationId = OrganizationFragmentArgs.fromBundle(getArguments()).getOrganizationId();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    // TODO Use alert dialog builder to create the dialog to be returned
    return super.onCreateDialog(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
// TODO Return view already inflated in onCreateDialog
    return super.onCreateView(inflater, container, savedInstanceState);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // TODO Connect to organization view model and user view model (we need to know who the current user is) and retrieve organization with specified ID
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}
