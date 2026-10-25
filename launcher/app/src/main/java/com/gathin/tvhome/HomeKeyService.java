package com.gathin.tvhome;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;

/** Optional Google TV / Android TV Home redirect. Receives package-name changes only, with no window or key access. */
public class HomeKeyService extends AccessibilityService {
    private long lastLaunch;
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return;
        CharSequence name = event.getPackageName();
        if (name == null) return;
        String pkg = name.toString();
        if (!pkg.equals("com.google.android.apps.tv.launcherx")
                && !pkg.equals("com.google.android.tvlauncher")) return;
        long now = SystemClock.uptimeMillis();
        if (lastLaunch != 0 && now - lastLaunch < 700) return;
        lastLaunch = now;
        try {
            startActivity(new Intent(this, WiiMenuActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        } catch (RuntimeException ignored) {
            // Leave the working system home accessible if launch is temporarily restricted.
        }
    }
    @Override public void onInterrupt() {}
}
