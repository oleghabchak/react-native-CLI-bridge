package com.seedmobile;

import android.os.Environment;

import com.restock.serialdevicemanager.utilssio.UtilsSDM;

public class Constants {
  public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().replace("/mnt",
    "");
  public static final String FOLDER_NAME = "TestReactnative_files";
  public static final String FOLDER_PATH = SDCARD_PATH + "/" + FOLDER_NAME;
  public static final String LOG_FULLPATH = FOLDER_PATH + "/TestRNLogFile.txt";
  public static void createFolder(String path) {
    UtilsSDM.createFolder(path);
  }
}
