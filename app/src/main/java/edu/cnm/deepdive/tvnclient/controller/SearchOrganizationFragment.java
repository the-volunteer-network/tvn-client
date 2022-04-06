package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter;
import edu.cnm.deepdive.tvnclient.databinding.FragmentSearchOrganizationBinding;
import edu.cnm.deepdive.tvnclient.viewmodel.LocationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;

public class SearchOrganizationFragment extends Fragment implements OnMapReadyCallback {

  private FragmentSearchOrganizationBinding binding;
  private OrganizationViewModel organizationViewModel;
  private LocationViewModel locationViewModel;
  private SearchOrganizationAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = FragmentSearchOrganizationBinding.inflate(inflater, container, false);
    binding.searchButton.setOnClickListener((v) ->
        organizationViewModel.findOrganizations(binding.searchBar.getText().toString().trim()));
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(
        R.id.map);
    mapFragment.getMapAsync(this);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    organizationViewModel = new ViewModelProvider(this).get(OrganizationViewModel.class);
    organizationViewModel
        .getOrganizations()
        .observe(getViewLifecycleOwner(), (orgs) -> {
          adapter = new SearchOrganizationAdapter(getContext(), orgs);
          // TODO create an instance of recyclerview adapter, pass orgs to it,
          // TODO Attach adapter to recyclerView.
          binding.organizations.setAdapter(adapter);
        });
    locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    locationViewModel
        .getLocation()
        .observe( getViewLifecycleOwner(), (location) -> {
          // TODO use location to update the map display.
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    LatLng start = new LatLng(35.691544, -105.944183);
    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    CameraUpdate initialSetting = CameraUpdateFactory.newLatLngZoom(start, 6);
    googleMap.moveCamera(initialSetting);
//    googleMap.getUiSettings().isMyLocationButtonEnabled();
    googleMap.getUiSettings().setZoomControlsEnabled(true);
//    googleMap.getUiSettings().

  }
}
