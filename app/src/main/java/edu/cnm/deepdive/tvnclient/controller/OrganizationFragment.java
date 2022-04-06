package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.tvnclient.databinding.FragmentOrganizationBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;

public class OrganizationFragment extends Fragment {

  private OrganizationViewModel organizationViewModel;
  private FragmentOrganizationBinding binding;
  private Organization organization;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentOrganizationBinding.inflate(inflater, container, false);
    binding.update.setOnClickListener((v) -> {
    });

    return super.onCreateView(inflater, container, savedInstanceState);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
