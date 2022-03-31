package edu.cnm.deepdive.tvnclient.service;

import android.content.Context;

public class UserRepository {


  private final Context context;
 // private final TVNServiceProxy serviceProxy;
  private final GoogleSignInService signInService;


  public  UserRepository(Context context) {
    this.context = context;
   // serviceProxy = TVNServiceProxy.getInstance;
    signInService = GoogleSignInService.getInstance();
  }

}
