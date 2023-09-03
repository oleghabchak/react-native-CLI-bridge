package com.awesomemodule2example;

/**
 package com.awesomemodule2example;
 * Created by Nikolay Bliznyuk on 28.08.2018.
 */
public class TPermission {
    String sMessage;
    int iREQUEST_CODE;
    String mPermission;
    int iCntRequests;

    public TPermission(int REQUEST_CODE, String mPermiss, String sMess) {
        iCntRequests = 0;
        sMessage = sMess;
        iREQUEST_CODE = REQUEST_CODE;
        mPermission = mPermiss;
    }
}
