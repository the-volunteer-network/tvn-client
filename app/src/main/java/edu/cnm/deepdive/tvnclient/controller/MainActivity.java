package edu.cnm.deepdive.tvnclient.controller;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import edu.cnm.deepdive.tvnclient.R;
import edu.cnm.deepdive.tvnclient.databinding.ActivityMainBinding;
import edu.cnm.deepdive.tvnclient.viewmodel.UserViewModel;

/**
 * Main activity Class for the TVN android project.
 */
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private AppBarConfiguration appBarConfiguration;
  private NavController navController;
  private UserViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_profile, R.id.navigation_organization, R.id.navigation_schedule, R.id.navigation_search_organization)
        .setOpenableLayout(binding.drawerLayout)
        .build();
    navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    NavigationUI.setupWithNavController(binding.navView, navController);
    viewModel = new ViewModelProvider(this).get(UserViewModel.class);
    viewModel
        .getCurrentUser()
        .observe(this, (user) -> {
          if (user != null) {
            // TODO Personalize the UI
          }

        });
    viewModel
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
      viewModel.signOut();
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
}