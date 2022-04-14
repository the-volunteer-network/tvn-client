package edu.cnm.deepdive.tvnclient.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.databinding.FragmentOrganizationBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import java.util.UUID;

/**
 * Defines, manages and inflates the {@code fragment_organization.xml} layout. Handles its layout
 * lifecycle and input events.
 */
public class OrganizationFragment extends DialogFragment implements OnShowListener, TextWatcher {

  private OrganizationViewModel organizationViewModel;
  private FragmentOrganizationBinding binding;
  private UUID organizationId;
  private Organization organization;
  private AlertDialog dialog;
  private boolean displayed;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    OrganizationFragmentArgs organizationFragmentArgs =
        OrganizationFragmentArgs.fromBundle(getArguments());
    organizationId = organizationFragmentArgs.getOrganizationId();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    binding = FragmentOrganizationBinding.inflate(inflater, null, false);
    binding.name.addTextChangedListener(this);
    binding.about.addTextChangedListener(this);
    binding.mission.addTextChangedListener(this);
    dialog = new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_baseline_info_24)
        .setTitle(R.string.organization_details_title)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.ok, (d, w) -> {
        })
        .setNegativeButton(android.R.string.cancel, (d, w) -> {
        })
        .setPositiveButton(android.R.string.ok, (d, w) -> {
          organization.setName(binding.name.getText().toString().trim());
          organization.setAbout(binding.about.getText().toString().trim());
          organization.setMission(binding.mission.getText().toString().trim());
          if (organizationId == null) {
            organizationViewModel.addOrganization(organization);
          } else {
            organizationViewModel.modifyOrganization(organizationId, organization);
          }
        })
        .create();
    dialog.setOnShowListener(this);
    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return binding.getRoot();
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    organizationViewModel = new ViewModelProvider(this).get(OrganizationViewModel.class);
    if (organizationId != null) {
      organizationViewModel
          .getOrganization()
          .observe(getViewLifecycleOwner(), (org) -> {
            organization = org;
            binding.name.setText(org.getName());
            binding.about.setText(org.getAbout());
            binding.mission.setText(org.getMission());
            showButtons();
          });
      organizationViewModel.fetchOrganization(organizationId);
      organizationViewModel
          .getOpportunities()
          .observe(getViewLifecycleOwner(), (opps) -> {
            // TODO Create an instance of opportunity adapter and attach to binding.opportunities
          });
      organizationViewModel.fetchAllOpportunities(organizationId);
    } else {
      organization = new Organization();
      binding.name.setText("");
      binding.about.setText("");
      binding.mission.setText("");
      showButtons();
    }
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onShow(DialogInterface dialog) {
    displayed = true;
    showButtons();
  }

  private void showButtons() {
    if (displayed && organization != null) {
      if (organizationId == null || organization.isOwned()) {
        this.dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        this.dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
        this.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
        binding.name.setEnabled(true);
        binding.about.setEnabled(true);
        binding.mission.setEnabled(true);
        checkSubmitConditions();
      } else {
        this.dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
        this.dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        this.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        binding.name.setEnabled(false);
        binding.about.setEnabled(false);
        binding.mission.setEnabled(false);
      }
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    // No action necessary.
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    // No action necessary.
  }

  @Override
  public void afterTextChanged(Editable s) {
    if (displayed) {
      checkSubmitConditions();
    }
  }


  private void checkSubmitConditions() {
    dialog
        .getButton(AlertDialog.BUTTON_POSITIVE)
        .setEnabled(
            !binding.name.getText().toString().trim().isEmpty()
                && !binding.about.getText().toString().trim().isEmpty()
                && !binding.mission.getText().toString().trim().isEmpty()
        );
  }
}
