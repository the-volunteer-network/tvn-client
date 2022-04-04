package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.cnm.deepdive.tvnclient.model.dto.Opportunity;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;

public class ProfileFragment extends Fragment {

private UserViewModel userViewModel;
private OrganizationViewModel organizationViewModel;
private Opportunity opportunity;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  // TODO Add methods that allows user to modify the aspects of their profile in one action

  public void updateProfile() {

  }


}
