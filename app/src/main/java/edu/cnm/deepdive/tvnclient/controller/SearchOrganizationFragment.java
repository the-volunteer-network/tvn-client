package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.tvnclient.databinding.FragmentSearchOrganizationBinding;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;

public class SearchOrganizationFragment extends Fragment {

  private FragmentSearchOrganizationBinding binding;
  private OrganizationViewModel organizationViewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSearchOrganizationBinding.inflate(inflater, container, false);
    binding.searchButton.setOnClickListener( (v) ->
        organizationViewModel.findOrganizations(binding.searchBar.getText().toString().trim()));

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    organizationViewModel = new ViewModelProvider(this).get(OrganizationViewModel.class);
    organizationViewModel
        .getOrganizations()
        .observe(getViewLifecycleOwner(), (orgs) -> {
          // TODO create an instance of recyclerview adapter, pass orgs to it,
          // TODO Attach adapter to recyclerView.
        });

  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}
