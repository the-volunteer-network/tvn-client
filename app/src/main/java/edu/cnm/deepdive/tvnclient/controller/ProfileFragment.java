package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.tvnclient.databinding.FragmentProfileBinding;
import edu.cnm.deepdive.tvnclient.model.dto.User;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;

public class ProfileFragment extends Fragment implements TextWatcher {

  private UserViewModel userViewModel;
  // TODO In order to access a User's owned, favorite, volunteered organizations, need to provide in appropriate view model.
  private FragmentProfileBinding binding;
  private User user;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentProfileBinding.inflate(inflater, container, false);
    binding.update.setOnClickListener((v) -> {
      user.setDisplayName(binding.displayName.getText().toString().trim());
      user.setEmail(binding.email.getText().toString().trim());
      user.setLocation(binding.location.getText().toString().trim());
      user.setPhoneNumber(binding.phone.getText().toString().trim());
      // TODO Set other properties of user from view objects on screen (more fields to edit; email, phone, etc...)
      userViewModel.modifyCurrentUser(user);
    });
    binding.displayName.addTextChangedListener(this);
    // TODO Set this as the text changed listener for ALL other fields.
    return binding.getRoot();
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(getViewLifecycleOwner(), (user) -> {
          this.user = user;
          binding.displayName.setText(user.getDisplayName());
          // TODO Populate other fields based on the user object
          checkSubmissionConditions();
        });
    // TODO Get and observe live data containing the list of user's organizations; observe the live data to populate a recycler view adapter
  }

  // TODO Add methods that allows user to modify the aspects of their profile in one action

  public void checkSubmissionConditions() {
    binding.update.setEnabled(
        !binding.displayName.getText().toString().trim().isEmpty()
        && !binding.displayName.getText().toString().trim().equals(user.getDisplayName())
        // TODO Check that other required fields are not blank, and that at least 1 field has been modified, otherwise, do not send to server.
    );
  }


  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    // Do nothing.
  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    // Do nothing
  }

  @Override
  public void afterTextChanged(Editable s) {
   checkSubmissionConditions();
  }
}
