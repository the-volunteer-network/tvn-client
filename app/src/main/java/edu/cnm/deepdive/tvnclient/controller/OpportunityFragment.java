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
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.databinding.FragmentOpportunityBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Defines, manages and inflates the {@code fragment_organization.xml} layout. Handles its layout
 * lifecycle and input events.
 */
public class OpportunityFragment extends DialogFragment implements OnShowListener, TextWatcher {

  private OrganizationViewModel organizationViewModel;
  private FragmentOpportunityBinding binding;
  private UUID organizationId;
  private UUID opportunityId;
  private Opportunity opportunity;
  private AlertDialog dialog;
  private boolean displayed;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    OpportunityFragmentArgs opportunityFragmentArgs =
        OpportunityFragmentArgs.fromBundle(getArguments());
    organizationId = opportunityFragmentArgs.getOrganizationId();
    opportunityId = opportunityFragmentArgs.getOpportunityId();
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getContext());
    binding = FragmentOpportunityBinding.inflate(inflater, null, false);
    binding.name.addTextChangedListener(this);
    binding.description.addTextChangedListener(this);
    dialog = new AlertDialog.Builder(getContext())
        .setIcon(R.drawable.ic_baseline_info_24)
        .setTitle(R.string.opportunity_details_title)
        .setView(binding.getRoot())
        .setNeutralButton(android.R.string.ok, (d, w) -> {
        })
        .setNegativeButton(android.R.string.cancel, (d, w) -> {
        })
        .setPositiveButton(android.R.string.ok, (d, w) -> {
          Organization org = (Organization) binding.organizations.getSelectedItem();
          String name = binding.name.getText().toString().trim();
          opportunity.setName(name);
          opportunity.setTitle(name);
          opportunity.setDescription(binding.description.getText().toString().trim());
          opportunity.setNeededSkill("");
          if (opportunityId == null) {
            organizationViewModel.addOpportunity(org.getId(), opportunity);
          } else {
            organizationViewModel.modifyOpportunity(organizationId, opportunityId, opportunity);
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
    organizationViewModel
        .getOrganizations()
        .observe(getViewLifecycleOwner(), (orgs) -> {
          List<Organization> myOrgs = orgs
              .stream()
              .filter((org) -> org.isOwned())
              .collect(Collectors.toList());
          ArrayAdapter<Organization> adapter = new ArrayAdapter<>(getContext(),
              android.R.layout.simple_spinner_item, myOrgs);
          binding.organizations.setAdapter(adapter);
        });
    organizationViewModel.fetchAllOrganizations();
    if (opportunityId != null) {
      organizationViewModel.fetchAllOpportunities(organizationId);
    } else {
      opportunity = new Opportunity();
      binding.name.setText("");
      binding.description.setText("");
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
    if (displayed && opportunity != null) {
      if (opportunityId == null) {
        this.dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        this.dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.VISIBLE);
        this.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
        binding.name.setEnabled(true);
        binding.description.setEnabled(true);
        checkSubmitConditions();
      } else {
        this.dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
        this.dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        this.dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        binding.name.setEnabled(false);
        binding.description.setEnabled(false);
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
                && !binding.description.getText().toString().trim().isEmpty()
        );
  }
}
