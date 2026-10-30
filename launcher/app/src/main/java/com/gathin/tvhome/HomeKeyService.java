package com.gathin.tvhome;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.view.accessibility.AccessibilityEvent;
import java.util.HashSet;
import java.util.Set;

/** Optional Home redirect using event metadata only, with no screen-content or key access. */
public class HomeKeyService extends AccessibilityService {
    private long lastLaunch;

    private Set<String> homeActivities = new HashSet<>();

    @Override protected void onServiceConnected() {
        super.onServiceConnected();
        refreshHomeActivities();
    }

    private void refreshHomeActivities() {
        Set<String> resolved = new HashSet<>();
        Intent home = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME);
        for (String pkg : HomeRedirect.LAUNCHER_PACKAGES) {
            home.setPackage(pkg);
            for (ResolveInfo info : getPackageManager().queryIntentActivities(
                    home, PackageManager.MATCH_DEFAULT_ONLY)) {
                ActivityInfo activity = info.activityInfo;
                if (activity == null || !pkg.equals(activity.packageName)) continue;
                resolved.add(HomeRedirect.component(pkg, activity.name));
                if (activity.targetActivity != null) {
                    resolved.add(HomeRedirect.component(pkg, activity.targetActivity));
                }
            }
        }
        homeActivities = resolved;
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return;
        // Google TV also owns DashboardHandler (the remote's Settings panel).
        // A package match alone steals focus from that panel and from the app behind it.
        // Refresh for launcher updates; unknown windows are deliberately left alone.
        if (!HomeRedirect.isLauncher(event.getPackageName())) return;
        refreshHomeActivities();
        if (!HomeRedirect.isHome(homeActivities, event.getPackageName(), event.getClassName())) return;
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
