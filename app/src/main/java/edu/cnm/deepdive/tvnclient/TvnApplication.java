package edu.cnm.deepdive.tvnclient;

import android.app.Application;
import edu.cnm.deepdive.tvnclient.service.GoogleSignInService;

/**
 *
 */
public class TvnApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInService.setContext(this);
  }
}
