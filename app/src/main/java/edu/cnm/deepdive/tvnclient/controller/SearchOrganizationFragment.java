package edu.cnm.deepdive.tvnclient.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter;
import edu.cnm.deepdive.tvnclient.adapter.SearchOrganizationAdapter.OnDetailsClickListener;
import edu.cnm.deepdive.tvnclient.databinding.FragmentSearchOrganizationBinding;
import edu.cnm.deepdive.tvnclient.model.dto.Organization;
import edu.cnm.deepdive.tvnclient.model.dto.User;
import edu.cnm.deepdive.tvnclient.viewmodel.LocationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.OrganizationViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.PermissionsViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;
import java.util.List;
import java.util.Set;

/**
 * Defines, manages and inflates the {@code fragment_search_organization.xml} layout. Extends
 * OnMapReadyCallback, and displaying og the map. Handles its layout lifecycle and input events.
 */
public class SearchOrganizationFragment extends Fragment implements OnMapReadyCallback,
    OnCameraMoveListener {

  private static final String CAMERA_ZOOM_KEY = "camera_zoom";
  private static final String CAMERA_LATITUDE_KEY = "camera_latitude";
  private static final String CAMERA_LONGITUDE_KEY = "camera_longitude";
  public static final String SEARCH_FRAGMENT_KEY = "search_fragment";

  private FragmentSearchOrganizationBinding binding;
  private OrganizationViewModel organizationViewModel;
  private LocationViewModel locationViewModel;
  private PermissionsViewModel permissionsViewModel;
  private SearchOrganizationAdapter adapter;
  private GoogleMap googleMap;
  private List<Organization> organizations;
  private SharedPreferences preferences;
  private Set<String> permissions;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = FragmentSearchOrganizationBinding.inflate(inflater, container, false);
    binding.searchButton.setOnClickListener((v) -> {
      String fragment = binding.searchBar.getText().toString().trim();
      preferences
          .edit()
          .putString(SEARCH_FRAGMENT_KEY, fragment)
          .apply();
      organizationViewModel.findOrganizations(fragment);
    });
    binding.addOrganization.setOnClickListener((v) -> Navigation
        .findNavController(binding.getRoot())
        .navigate(SearchOrganizationFragmentDirections.editOrganization())
    );
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
          organizations = orgs;
          adapter = new SearchOrganizationAdapter(getContext(), orgs,
              (position, org) -> Navigation
                  .findNavController(binding.getRoot())
                  .navigate(
                      SearchOrganizationFragmentDirections
                          .editOrganization()
                          .setOrganizationId(org.getId())
                  ),
              (position, organization, favorite) ->
                  organizationViewModel.setFavorite(organization.getId(), organization, favorite),
              (position, organization, volunteer) ->
                  organizationViewModel.setVolunteer(organization.getId(), organization, volunteer),
              (position, organization) -> showOrganization(organization)
          );

          binding.organizations.setAdapter(adapter);
          //        showOrganizations();
        });
    if (preferences.contains(SEARCH_FRAGMENT_KEY)) {
      String fragment = preferences.getString(SEARCH_FRAGMENT_KEY, "");
      binding.searchBar.setText(fragment);
      organizationViewModel.findOrganizations(fragment);
    } else {
      organizationViewModel.fetchAllOrganizations();
    }
/*
    locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
    getLifecycle().addObserver(locationViewModel);
    locationViewModel
        .getLocation()
        .observe(getViewLifecycleOwner(), (location) -> {
          LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//          CameraUpdate locationChange = CameraUpdateFactory.newLatLng(latLng);
//          googleMap.moveCamera(locationChange);
        });
*/
    permissionsViewModel = new ViewModelProvider(getActivity()).get(PermissionsViewModel.class);
    permissionsViewModel
        .getPermissions()
        .observe(getViewLifecycleOwner(), (permissions) -> {
          this.permissions = permissions;
          showLocationIndicator();
        });
  }

  @SuppressLint("MissingPermission")
  private synchronized void showLocationIndicator() {
    if (googleMap != null && permissions != null) {
      googleMap.setMyLocationEnabled(
          permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION));
    }
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
    googleMap.setOnCameraMoveListener(this);
    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    float zoomLevel = preferences.getFloat(CAMERA_ZOOM_KEY, 9);
    CameraUpdate initialSetting;
    if (preferences.contains(CAMERA_LATITUDE_KEY) && preferences.contains(CAMERA_LONGITUDE_KEY)) {
      float latitude = preferences.getFloat(CAMERA_LATITUDE_KEY, 0);
      float longitude = preferences.getFloat(CAMERA_LONGITUDE_KEY, 0);
      LatLng latLng = new LatLng(latitude, longitude);
      initialSetting = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel);
    } else {
      initialSetting = CameraUpdateFactory.zoomTo(zoomLevel);
    }
    googleMap.moveCamera(initialSetting);
    googleMap.getUiSettings().setZoomControlsEnabled(true);
    showLocationIndicator();
    showOrganizations();
  }

  @Override
  public void onCameraMove() {

    CameraPosition position = googleMap.getCameraPosition();
    float zoomLevel = position.zoom;
    double latitude = position.target.latitude;
    double longitude = position.target.longitude;
    preferences
        .edit()
        .putFloat(CAMERA_ZOOM_KEY, zoomLevel)
        .putFloat(CAMERA_LATITUDE_KEY, (float) latitude)
        .putFloat(CAMERA_LONGITUDE_KEY, (float) longitude)
        .apply();


  }
}
