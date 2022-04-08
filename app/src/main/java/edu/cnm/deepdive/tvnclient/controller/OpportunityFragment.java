package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.tvnclient.adapter.OpportunityAdapter;
import edu.cnm.deepdive.tvnclient.databinding.FragmentOpportunityBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;

public class OpportunityFragment extends Fragment {

  private OrganizationViewModel organizationViewModel;
  private FragmentOpportunityBinding binding;
  private UserViewModel userViewModel;
  private Opportunity opportunity;
  private OpportunityAdapter adapter;
  private Spinner spinner;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentOpportunityBinding.inflate(inflater,container, false);
    binding.searchButton.setOnClickListener((v) ->
        organizationViewModel.findOpportunity(binding.searchBar.getText().toString().trim()));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    organizationViewModel = new ViewModelProvider(this).get(OrganizationViewModel.class);
    organizationViewModel
        .getOpportunities()
        .observe(getViewLifecycleOwner(), (opps) -> {
          adapter = new OpportunityAdapter(getContext(), opps);
          binding.opportunities.setAdapter(adapter);

        });
    organizationViewModel
        .getOrganizations()
        .observe(getViewLifecycleOwner(), (orgs) ->{
          ArrayAdapter<Organization> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,orgs);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          binding.spinnerOrganizations.setAdapter(adapter);
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}
