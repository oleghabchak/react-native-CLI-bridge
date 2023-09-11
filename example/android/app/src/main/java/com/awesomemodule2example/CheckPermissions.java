package com.seedmobile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CheckPermissions {
  private static final int REQUEST_ALL_PERMISSION = 751;
  static final int PERMISSION_REQUEST_CODE = 1571;

  int CNT_PERMISS = 3;
  public final String messageWritePermission = "Storage permission is needed to write the log file and send it to cloud-in-hand.com";
  // public final String messageWritePermission = "Storage permission is necessary
  // to write grids databases and the log file, and for keep the history of
  // connected devices after re-install app";
  public final String messagePhoneStatePermission = "When log uploaded, this app uses device ID to identify the log.\nThe permission 'manage phone calls' is need to get a device ID";
  // public final String messagePhoneStatePermission = "The permission 'manage
  // phone calls' is necessary to get a device ID.\nWhen log uploaded, this app
  // uses device ID to identify the log."; //
  public final String messageLocationPermission = "Location permission is necessary for searching BLE devices";
  public final String messageSendSmsPermission = "'Send SMS' permission is necessary to send SMS in some user profiles";

  public final String messageBackgroundLocationPermission = "Background location is necessary for searching BLE devices in background";

  TPermission massPermiss[];
  String[] mPERMISSIONS;

  private Context mContext;
  private Activity activity;
  private View snack_wiev;

  public interface iCheckPermission {
    void init_app();
  }

  iCheckPermission mInterface;

  // https://stackoverflow.com/questions/33407250/checkselfpermission-method-is-not-working-in-targetsdkversion-22
  public CheckPermissions(Context ctx, View view_snack, iCheckPermission interf) {
    mContext = ctx;
    activity = (Activity) ctx;

    snack_wiev = view_snack;
    mInterface = interf;

    massPermiss = new TPermission[CNT_PERMISS];
    massPermiss[0] = new TPermission(1, Manifest.permission.ACCESS_FINE_LOCATION,
        messageLocationPermission);
    massPermiss[1] = new TPermission(2, Manifest.permission.WRITE_EXTERNAL_STORAGE, messageWritePermission);
    massPermiss[2] = new TPermission(3, Manifest.permission.READ_PHONE_STATE, messagePhoneStatePermission);
    // massPermiss[3] = new TPermission(4,
    // android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    // messageBackgroundLocationPermission);
    // massPermiss[3] = new TPermission(4, android.Manifest.permission.SEND_SMS,
    // messageSendSmsPermission);

    mPERMISSIONS = new String[CNT_PERMISS];
    for (int i = 0; i < CNT_PERMISS; i++) {
      mPERMISSIONS[i] = massPermiss[i].mPermission;
    }
  }

  public boolean checkListOfPermissions() {
    boolean bRet = hasPermissions(mContext, mPERMISSIONS);
    if (!bRet) {
      String[] sPermissions = needPermissions(mContext, mPERMISSIONS);
      if (sPermissions != null) {
        ActivityCompat.requestPermissions(activity, sPermissions, REQUEST_ALL_PERMISSION);
      }
    }
    return bRet;
  }

  public static boolean hasPermissions(Context context, String... permissions) {
    boolean bRet = true;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (PermissionChecker.checkSelfPermission(context,
            permission) != PermissionChecker.PERMISSION_GRANTED) {
          bRet = false;
          break;
        }
      }
    }
    return bRet;
  }

  public static boolean hasPermissions(Context context, String permissions) {
    return hasPermissions(context, new String[] { permissions });
  }

  public static boolean hasLocationPermissions(Context context) {
    return hasPermissions(context, new String[] { Manifest.permission.ACCESS_FINE_LOCATION });
  }

  public static boolean hasWriteExternalStorePermissions(Context context) {
    return hasPermissions(context, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE });
  }

  public static boolean hasReadPhoneStatePermissions(Context context) {
    return hasPermissions(context, new String[] { Manifest.permission.READ_PHONE_STATE });
  }

  public static boolean hasCameraPermissions(Context context) {
    return hasPermissions(context, new String[] { Manifest.permission.CAMERA });
  }

  private String[] needPermissions(Context context, String... permissions) {
    String sPermissions[] = null;
    ArrayList<String> listPermissions = new ArrayList<>();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
      for (String permission : permissions) {
        if (PermissionChecker.checkSelfPermission(context,
            permission) != PermissionChecker.PERMISSION_GRANTED) {
          listPermissions.add(permission);
        }
      }
    }
    if (listPermissions.size() > 0) {
      sPermissions = new String[listPermissions.size()];
      sPermissions = listPermissions.toArray(sPermissions);
    }
    return sPermissions;
  }

  // requestPerms(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
  private void requestPerms(String[] permissions, int iCode) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      activity.requestPermissions(permissions, iCode);
    }
  }

  private void openApplicationSettings() {
    Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + mContext.getPackageName()));
    activity.startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
  }

  private void requestWithRationale(final TPermission permiss) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permiss.mPermission)) {
      permiss.iCntRequests++;
      Snackbar sb = Snackbar.make(snack_wiev, permiss.sMessage, Snackbar.LENGTH_INDEFINITE).setAction("OK",
          new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              requestPerms(new String[] { permiss.mPermission }, permiss.iREQUEST_CODE);
            }
          });
      View snackbarView = sb.getView();
      TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
      textView.setMaxLines(5); // show multiple line
      sb.show();
    } else {
      requestPerms(new String[] { permiss.mPermission }, permiss.iREQUEST_CODE);
    }
  }

  private void showNoPermissionSnackbar(String s) {
    Snackbar sb = Snackbar.make(snack_wiev, s, Snackbar.LENGTH_INDEFINITE).setAction("SETTINGS",
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            openApplicationSettings();
            Toast.makeText(activity.getApplicationContext(),
                "Please open Permissions and grant permissions", Toast.LENGTH_LONG).show();
          }
        });
    View snackbarView = sb.getView();
    TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
    textView.setMaxLines(20); // show multiple line
    sb.show();
  }

  public void onRequestPermissionsResult(final int requestCode, String permissions[], int[] grantResults) {
    // there are all permissions
    if (hasPermissions(mContext, mPERMISSIONS)) {
      mInterface.init_app();
      return;
    }
    TPermission permiss;
    String messageAllRestPermission = "";

    // all permissions were denied
    int iRequests = 0;
    for (int i = 0; i < CNT_PERMISS; i++) {
      permiss = massPermiss[i];
      if (permiss.iCntRequests > 0)
        iRequests++;
    }
    if (iRequests == CNT_PERMISS) {
      // last request - ask to show Settings and set Permission manually
      messageAllRestPermission = getRestPermissions();
      showNoPermissionSnackbar(messageAllRestPermission);
      return;
    }

    // the first request of bunch of permissions
    if (requestCode == REQUEST_ALL_PERMISSION) {
      for (int i = 0; i < CNT_PERMISS; i++) {
        permiss = massPermiss[i];
        if (!hasPermissions(mContext, new String[] { permiss.mPermission })) {
          requestWithRationale(permiss);
          return;
        }
      }
    }

    for (int i = 0; i < CNT_PERMISS; i++) {
      permiss = massPermiss[i];
      if (requestCode == permiss.iREQUEST_CODE) {
        for (int j = i + 1; j < CNT_PERMISS; j++) {
          permiss = massPermiss[j];
          if (!hasPermissions(mContext, new String[] { permiss.mPermission }) && permiss.iCntRequests == 0) {
            requestWithRationale(permiss);
            return;
          }
        }
        // last request - ask to show Settings and set Permission manually
        messageAllRestPermission = getRestPermissions();
        showNoPermissionSnackbar(messageAllRestPermission); // ???????
        // mInterface.init_app();

      }
    }
  }

  private String getRestPermissions() {
    TPermission permiss;
    String sRestPerm = "We need such permissions:";
    int n = 0;
    for (int i = 0; i < CNT_PERMISS; i++) {
      permiss = massPermiss[i];
      if (!hasPermissions(mContext, new String[] { permiss.mPermission })) {
        sRestPerm += String.format("\n%d) %s", ++n, permiss.sMessage);
      }
    }
    return sRestPerm;
  }
}
