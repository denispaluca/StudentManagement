package com.harryfultz.studentmanager.appConfigAndDB

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.provider.Settings


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun AirplaneMode(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) !== 0
    } else {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) !== 0
    }
}