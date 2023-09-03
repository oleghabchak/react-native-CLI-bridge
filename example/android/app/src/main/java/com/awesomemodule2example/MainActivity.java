package com.awesomemodule2example;

import android.content.Context;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.react.defaults.DefaultReactActivityDelegate;
import com.restock.serialdevicemanager.utilssio.UtilsSDM;

import java.util.logging.Logger;

public class MainActivity extends ReactActivity implements CheckPermissions.iCheckPermission {
  CheckPermissions mCheckPermissions;
  boolean bInit = false;
  public Context mContext;
  Logger gLogger;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = this;
    mCheckPermissions = new CheckPermissions(mContext, getWindow().getDecorView(), this);
    if (mCheckPermissions.checkListOfPermissions()) {
      initapp();
    }
  }

  @Override
  public void init_app() {
    initapp();
  }

  void initapp() {
    if (bInit) return;
    bInit = true;
    UtilsSDM.createFolder(Constants.FOLDER_PATH);
  }

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "AwesomeModule2Example";
  }

  /**
   * Returns the instance of the {@link ReactActivityDelegate}. Here we use a util class {@link
   * DefaultReactActivityDelegate} which allows you to easily enable Fabric and Concurrent React
   * (aka React 18) with two boolean flags.
   */
  @Override
  protected ReactActivityDelegate createReactActivityDelegate() {
    return new DefaultReactActivityDelegate(
      this,
      getMainComponentName(),
      // If you opted-in for the New Architecture, we enable the Fabric Renderer.
      DefaultNewArchitectureEntryPoint.getFabricEnabled());
  }
}
