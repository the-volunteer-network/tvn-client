package edu.cnm.deepdive.tvnclient.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter.OnDetailsClickListener;
import edu.cnm.deepdive.tvnclient.databinding.FragmentSearchOrganizationBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.viewmodel.LocationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import java.util.List;

/**
 * Defines, manages and inflates the {@code fragment_search_organization.xml} layout.
 * Extends OnMapReadyCallback, and displaying og the map.
 * Handles its layout lifecycle and input events.
 */
public class SearchOrganizationFragment extends Fragment implements OnMapReadyCallback {

  private FragmentSearchOrganizationBinding binding;
  private OrganizationViewModel organizationViewModel;
  private LocationViewModel locationViewModel;
  private SearchOrganizationAdapter adapter;
  private GoogleMap googleMap;
  private List<Organization> organizations;
  private OnDetailsClickListener listener;

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
    binding.organizations.setOnClickListener((v) -> {

    });
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    organizationViewModel = new ViewModelProvider(this).get(OrganizationViewModel.class);
    organizationViewModel
        .getOrganizations()
        .observe(getViewLifecycleOwner(), (orgs) -> {
          organizations = orgs;
          adapter = new SearchOrganizationAdapter(getContext(), orgs,
              (position, org) -> {
                //Todo display details of org
                Navigation
                    .findNavController(binding.getRoot())
                    .navigate(SearchOrganizationFragmentDirections.editOrganization());
              },
              ((position, organization, favorite) -> {
                organizationViewModel.setFavorite(organization.getId(), organization, favorite);
              }),
              ((position, organization, volunteer) -> {
               organizationViewModel.setVolunteer(organization.getId(), organization, volunteer);
              }),
              ((position, organization) -> {
               showOrganization(organization);
              }));

          binding.organizations.setAdapter(adapter);
  //        showOrganizations();
        });
    locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    getLifecycle().addObserver(locationViewModel);
    locationViewModel
        .getLocation()
        .observe(getViewLifecycleOwner(), (location) -> {
          LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
          CameraUpdate locationChange = CameraUpdateFactory.newLatLng(latLng);
          googleMap.moveCamera(locationChange);
        });
  }

  private void showOrganizations() {
    if (organizations != null && googleMap != null) {
      googleMap.clear();
      for (Organization org : organizations) {
        LatLng location = new LatLng(org.getLatitude(), org.getLongitude());
        googleMap.addMarker(new MarkerOptions()
            .title(org.getName())
            .position(location)
        );
      }
    }
  }
  private void showOrganization(Organization org) {
      googleMap.clear();
        LatLng location = new LatLng(org.getLatitude(), org.getLongitude());
        googleMap.addMarker(new MarkerOptions()
            .title(org.getName())
            .position(location)
        );
      }




  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    this.googleMap = googleMap;
    LatLng start = new LatLng(35.691544, -105.944183);
    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    CameraUpdate initialSetting = CameraUpdateFactory.newLatLngZoom(start, 8);
    googleMap.moveCamera(initialSetting);
//    googleMap.getUiSettings().isMyLocationButtonEnabled();
    googleMap.getUiSettings().setZoomControlsEnabled(true);
//    googleMap.getUiSettings().
    showOrganizations();
  }
}
