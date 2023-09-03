package com.awesomemodule2example;

import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.restock.loggerlib.Logger;
import com.restock.loggerlib.LoggerSinglton;
import com.restock.loggerlib.iLogger;
import com.restock.scanners.Scanner;
import com.restock.scanners.ScannerHandler;
import com.restock.scanners.ScannerParams;
import com.restock.scanners.utils.NDEFMessageParser;
import com.restock.scanners.utils.NDEFMessageTLVCreator;
import com.restock.scanners.utils.NDEFMessageTVLParser;
import com.restock.scanners.utils.NDEFRecordCreator;
import com.restock.serialdevicemanager.builtinreaders.BuiltInReader;
import com.restock.serialdevicemanager.devicemanager.BuiltInScannerSettings;
import com.restock.serialdevicemanager.devicemanager.ConstantsSdm;
import com.restock.serialdevicemanager.devicemanager.RootActivity;
import com.restock.serialdevicemanager.devicemanager.ScannerCommonParams;
import com.restock.serialdevicemanager.devicemanager.SdmError;
import com.restock.serialdevicemanager.devicemanager.SdmHandler;
import com.restock.serialdevicemanager.devicemanager.SdmSingleton;
import com.restock.serialdevicemanager.devicemanager.SioDevice;
import com.restock.serialdevicemanager.devicemanager.iOurCallback;
import com.restock.serialdevicemanager.devicemanager.iSdmCallbackDiscoverBluetooth;
import com.restock.serialdevicemanager.devicemanager.iSdmCallbacks;
import com.restock.serialdevicemanager.devicemanager.iSdmHandler;
import com.restock.serialdevicemanager.devicemanager.iSdmHandlerDiscoverBluetooth;
import com.restock.serialdevicemanager.nf4credentialsprofile.Nf4CredTypeBaseFormat;
import com.restock.serialdevicemanager.pacsprofiles.BaseFormat;
import com.restock.serialdevicemanager.pacsprofiles.PACSProfileEngine;
import com.restock.serialdevicemanager.utilssio.SearchableList;
import com.restock.serialdevicemanager.utilssio.UtilsSDM;

import androidx.annotation.Nullable;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class SDMModule extends ReactContextBaseJavaModule implements iSdmCallbacks {
    private static ReactApplicationContext reactContext;
    Logger gLogger;

    // Instance of interface to send commands to SDM
    iSdmHandler sdmHandler;
    // Instance of interface to do Discovering of Bluetooth devices
    iSdmHandlerDiscoverBluetooth sdmHandlerDiscoverBT;
    // Class to process callbacks from SDM
    // SdmCallbacks sdmCallbacks;
    protected String m_strFolderPath;

    Runnable runShowDeviceManager = new Runnable() {
        public void run() {
            gLogger.putt("SDMModule.showDeviceManager - UI thread\n");
            sdmHandler.showDeviceManager();
        }
    };

    Runnable runInitSDM = new Runnable() {
        public void run() {
            gLogger.putt("SDMModule.initSDM - UI thread\n");
            initSDM();
        }
    };

    Runnable runDeinitSDM = new Runnable() {
        public void run() {
            gLogger.putt("SDMModule.DeinitSDM - UI thread\n");
            DeinitSDM();
        }
    };

    SDMModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        // UtilsSDM.createFolder(Constants.FOLDER_PATH);

        gLogger = LoggerSinglton.getInstance();

        sdmHandler = SdmSingleton.getInstance();
        String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath().replace("/mnt", "");
        m_strFolderPath = sdCardPath + "/TestReactnative_files";
        String logPath = m_strFolderPath + "/reactTestLog.txt";
        sdmHandler.setLogging(context, true, logPath);
        gLogger.putt("SDMModule.Constructor initialized\n");

        // runOnUiThread(runInitSDM);
        Looper.prepare();
        initSDM();

    }

    @Override
    public String getName() {
        return "SDMModule";
    }

    @ReactMethod
    public void showDeviceManager() {
        gLogger.putt("SDMModule.showDeviceManager\n");
        // runOnUiThread(runShowDeviceManager);
        sdmHandler.showDeviceManager();
    }

    @ReactMethod
    public void sendCommand(String address, ReadableArray command) {
        gLogger.putt("SDMModule.sendCommand to %s\n", address);
        byte data[] = readableArrayToByteBoolArray(command);
        gLogger.putHex(data);
        sdmHandler.sendCommand(address, data);
    }

    public static byte[] readableArrayToByteBoolArray(ReadableArray readableArray) {
        byte[] bytesArr = new byte[readableArray.size()];
        for (int i = 0; i < bytesArr.length; i++) {
            bytesArr[i] = (byte) readableArray.getInt(i);
        }

        return bytesArr;
    }

    @Override
    public void onConnectionStatus(String address, int status) {
        gLogger.putt("SDMModule.onConnectionStatus: %s - %d\n", address, status);
        sendDeviceStateToRN(address, status);
    }

    @Override
    public void onReceiveData(String address, int type, int iBuiltInType, int antenna, Object data) {
        String strData = "";
        switch (type) {
            case iSdmCallbacks.TYPE_STRING: {
                strData += (String) data;
                break;
            }

            case iSdmCallbacks.TYPE_BYTES_ARRAY:
            case iSdmCallbacks.TYPE_RAW_BYTES: {
                byte[] bData = (byte[]) data;
                if (bData != null) {
                    for (int i = 0; i < bData.length; i++) {
                        String strBuf = String.format("<%02X>", (byte) (bData[i] & 0xFF));
                        strData += strBuf;
                    }
                }
                break;
            }
        }
        String sAnt = "";
        if (antenna >= 0) {
            sAnt = String.format("[ant:%d]", antenna);
        }
        gLogger.putt("SDMModule.onReceiveData: %s - %s\n", address, (String) strData);
        sendDataToRN(address, strData);
    }

    protected void initSDM() {
        int iRes = sdmHandler.init(reactContext.getApplicationContext(), this, m_strFolderPath);
    }

    @ReactMethod
    public void DeinitSDM() {
        gLogger.putt("SDMModule.DeinitSDM\n");
        sdmHandler.deinit();
    }

    private void sendEvent(ReactContext reactContext,
            String eventName,
            @Nullable WritableMap params) {
        try {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        } catch (Exception e) {
            Log.e("ReactNative", "Caught Exception: " + e.getMessage());
        }
    }

    private void sendDataToRN(String address, String data) {
        WritableMap map = Arguments.createMap();
        map.putString("address", address);
        map.putString("data", data);
        sendEvent(reactContext, "sdmEventNewData", map);
    }

    private void sendDeviceStateToRN(String address, int state) {
        WritableMap map = Arguments.createMap();
        map.putString("address", address);
        map.putInt("state", state);
        sendEvent(reactContext, "sdmEventDeviceState", map);
    }
}
