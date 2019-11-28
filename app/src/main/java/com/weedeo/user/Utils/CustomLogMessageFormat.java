package com.weedeo.user.Utils;

import android.content.Context;

import com.hypertrack.hyperlog.LogFormat;

public class CustomLogMessageFormat extends LogFormat {

    public CustomLogMessageFormat(Context context) {
        super(context);
    }

    @Override
    public String getFormattedLogMessage(String logLevelName, String tag, String message, String timeStamp, String senderName, String osVersion, String deviceUUID) {
        return timeStamp + " : " + logLevelName + "/" + tag + " : " + deviceUUID + " : " + message;
    }
}
