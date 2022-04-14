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
import androidx.navigation.Navigation;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.adapter.OpportunityAdapter;
import edu.cnm.deepdive.tvnclient.databinding.FragmentSearchOpportunityBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;

/**
 * Defines, manages and inflates the {@code fragment_search_opportunity.xml} layout. Handles its
 * layout lifecycle and input events.
 */
public class SearchOpportunityFragment extends Fragment {

  private OrganizationViewModel organizationViewModel;
  private FragmentSearchOpportunityBinding binding;
  private OpportunityAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSearchOpportunityBinding.inflate(inflater, container, false);
    binding.searchButton.setOnClickListener((v) -> {
      Organization org = (Organization) binding.spinnerOrganizations.getSelectedItem();
      String fragment = binding.searchBar.getText().toString().trim();
      if (org.getId() == null) {
        organizationViewModel.findOpportunities(fragment);
      } else {
        organizationViewModel.findOpportunities(fragment, org);
      }
    });
    binding.addOpportunity.setOnClickListener((v) -> Navigation
        .findNavController(binding.getRoot())
        .navigate(SearchOpportunityFragmentDirections.editOpportunity())
    );
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    organizationViewModel = new ViewModelProvider(this).get(OrganizationViewModel.class);
    organizationViewModel
        .getOpportunities()
        .observe(getViewLifecycleOwner(), (opps) -> {
          adapter = new OpportunityAdapter(getContext(), opps,
              binding.spinnerOrganizations.getSelectedItemPosition() == 0);
          binding.opportunities.setAdapter(adapter);

        });
    organizationViewModel
        .getOrganizations()
        .observe(getViewLifecycleOwner(), (orgs) -> {
          Organization noneSelected = new Organization();
          noneSelected.setName(getString(R.string.all_organizations_name));
          orgs.add(0, noneSelected);
          ArrayAdapter<Organization> adapter = new ArrayAdapter<>(getContext(),
              android.R.layout.simple_spinner_item, orgs);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          binding.spinnerOrganizations.setAdapter(adapter);
        });
    organizationViewModel
        .fetchAllOrganizations();
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}
