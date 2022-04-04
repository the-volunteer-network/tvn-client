package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.tvnclient.databinding.FragmentSearchOrganizationDetailsBinding;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;

public class SearchOrganizationFragmentDetails extends Fragment {

  private FragmentSearchOrganizationDetailsBinding binding;

  private OrganizationViewModel organizationViewModel;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSearchOrganizationDetailsBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }
}


