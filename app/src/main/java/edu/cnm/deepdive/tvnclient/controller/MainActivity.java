package edu.cnm.deepdive.tvnclient.controller;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import edu.cnm.deepdive.tvnclient.NavigationMapDirections;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.databinding.ActivityMainBinding;
import edu.cnm.deepdive.tvnclient.viewmodel.PermissionsViewModel;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;
import java.util.LinkedList;
import java.util.List;

/**
 * Main activity Class for the TVN android project.
 */
public class MainActivity extends AppCompatActivity implements
    PermissionsExplanationFragment.OnAcknowledgeListener {

  private static final int PERMISSIONS_REQUEST_CODE = 42;

  private ActivityMainBinding binding;
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;
  private UserViewModel userViewModel;
  private PermissionsViewModel permissionsViewModel;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_profile,
         R.id.navigation_opportunity,
        R.id.navigation_search_organization)
        .setOpenableLayout(binding.drawerLayout)
        .build();
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    NavigationUI.setupWithNavController(binding.navView, navController);
    userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    userViewModel
        .getCurrentUser()
        .observe(this, (user) -> {
          if (user != null) {
            // TODO Personalize the UI
          }

        });
    userViewModel
        .getAccount()
        .observe(this, (account) -> {
          if (account != null) {
            // TODO Personalize the UI based on their login account
          } else {
            Intent intent = new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
          }
        });
    permissionsViewModel = new ViewModelProvider(this).get(PermissionsViewModel.class);
    permissionsViewModel
        .getPermissions()
        .observe(this, (permissions) -> Log.d(getClass().getSimpleName(), permissions.toString()));
    checkPermissions();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled;
    if (item.getItemId() == R.id.sign_out) {
      handled = true;
      userViewModel.signOut();
    } else {
      handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == PERMISSIONS_REQUEST_CODE) {
      for (int i = 0; i > permissions.length; i++) {
        String permission = permissions[i];
        int result = grantResults[i];
        if (result == PackageManager.PERMISSION_GRANTED) {
          permissionsViewModel.add(permission);
        } else {
          permissionsViewModel.remove(permission);
        }
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void checkPermissions() {
    try {
      PackageInfo info = getPackageManager()
          .getPackageInfo(getPackageName(),
              PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
      String[] permissions = info.requestedPermissions;
      List<String> permissionsToRequest = new LinkedList<>();
      List<String> permissionsToExplain = new LinkedList<>();
      for (String permission : permissions) {
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
          permissionsToRequest.add(permission);
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            permissionsToExplain.add(permission);
          }
        } else {
          permissionsViewModel.add(permission);
        }
      }
      if (!permissionsToExplain.isEmpty()) {
        navController.navigate(NavigationMapDirections.explainPermissions(
            permissionsToExplain.toArray(new String[0]),
            permissionsToRequest.toArray(new String[0])));
      } else if (!permissionsToRequest.isEmpty()) {
        onAcknowledge(permissionsToRequest.toArray(new String[0]));
      }
    } catch (NameNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void onAcknowledge(String[] permissions) {
    ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
  }
}