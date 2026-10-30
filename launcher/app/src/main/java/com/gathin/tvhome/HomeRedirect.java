package com.gathin.tvhome;

import java.util.Set;

/** Exact HOME component matching; never infer Home from a package or class-name substring. */
final class HomeRedirect {
    static final String[] LAUNCHER_PACKAGES = {
        "com.google.android.apps.tv.launcherx", "com.google.android.tvlauncher"
    };

    static boolean isLauncher(CharSequence packageName) {
        if (packageName == null) return false;
        for (String pkg : LAUNCHER_PACKAGES) {
            if (pkg.contentEquals(packageName)) return true;
        }
        return false;
    }

    static String component(String pkg, String activity) {
        if (activity.startsWith(".")) activity = pkg + activity;
        else if (activity.indexOf('.') < 0) activity = pkg + "." + activity;
        return pkg + "/" + activity;
    }

    static boolean isHome(Set<String> homes, CharSequence packageName, CharSequence className) {
        return isLauncher(packageName) && className != null
                && homes.contains(component(packageName.toString(), className.toString()));
    }

    private HomeRedirect() {}
}
