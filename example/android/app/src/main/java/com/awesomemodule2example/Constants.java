package com.awesomemodule2example;

import android.os.Environment;

public class Constants {
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().replace("/mnt",
            "");
    public static final String FOLDER_NAME = "TestReactnative_files";
    public static final String FOLDER_PATH = SDCARD_PATH + "/" + FOLDER_NAME;
    public static final String LOG_FULLPATH = FOLDER_PATH + "/TestRNLogFile.txt";

}
