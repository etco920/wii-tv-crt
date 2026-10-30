package com.gathin.tvhome;

import static org.junit.Assert.*;
import java.util.Set;
import org.junit.Test;

public class HomeRedirectTest {
    private static final String GOOGLE = "com.google.android.apps.tv.launcherx";
    private static final String ANDROID = "com.google.android.tvlauncher";
    private final Set<String> homes = Set.of(
            HomeRedirect.component(GOOGLE, ".home.HomeActivity"),
            HomeRedirect.component(ANDROID, ".MainActivity"));

    @Test public void registeredHomeActivitiesStillRedirect() {
        assertTrue(HomeRedirect.isHome(homes, GOOGLE, GOOGLE + ".home.HomeActivity"));
        assertTrue(HomeRedirect.isHome(homes, ANDROID, ANDROID + ".MainActivity"));
    }

    @Test public void remoteSettingsDashboardDoesNotRedirect() {
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, GOOGLE + ".dashboard.DashboardHandler"));
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, "android.app.Dialog"));
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, "android.widget.FrameLayout"));
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, GOOGLE + ".home.HomeActivitySettings"));
        // Opening or closing the dashboard must not suppress the next real Home event.
        assertTrue(HomeRedirect.isHome(homes, GOOGLE, GOOGLE + ".home.HomeActivity"));
    }

    @Test public void unknownOrIncompleteEventsLeaveCurrentAppAlone() {
        assertFalse(HomeRedirect.isHome(homes, null, null));
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, null));
        assertFalse(HomeRedirect.isHome(Set.of(), GOOGLE, GOOGLE + ".home.HomeActivity"));
        assertFalse(HomeRedirect.isHome(homes, "com.android.tv.settings", GOOGLE + ".home.HomeActivity"));
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, ""));
    }

    @Test public void componentNamesAreNormalizedWithoutSubstringMatching() {
        assertEquals(GOOGLE + "/" + GOOGLE + ".home.HomeActivity",
                HomeRedirect.component(GOOGLE, ".home.HomeActivity"));
        assertTrue(HomeRedirect.isHome(homes, ANDROID, "MainActivity"));
        assertFalse(HomeRedirect.isHome(homes, GOOGLE, "HomeActivity"));
    }
}
