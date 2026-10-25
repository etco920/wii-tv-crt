package com.gathin.tvhome;

import android.content.Context;
import android.content.SharedPreferences;

/** Independent, persistent appearance choices for the Wii menu. */
final class VisualOptions {
    static final String PREFS = "wii_appearance";
    static final String CRT = "crt_enabled";
    static final String DARK = "dark_mode";

    private VisualOptions() {}

    static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    static boolean crt(Context context) {
        return preferences(context).getBoolean(CRT, true);
    }

    static boolean dark(Context context) {
        return preferences(context).getBoolean(DARK, true);
    }
}
