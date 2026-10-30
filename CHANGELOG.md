# Changelog

## v1.0.1 — Remote Settings button fix

- Fixed the optional Home service redirecting Google TV's quick-settings dashboard to Wii Home. Previously, pressing Settings could make the panel flash and disappear, or pull you out of another app.
- Redirect only window events whose package and activity match a registered Google TV or Android TV Home activity. Dashboard, dialog, and unknown-window events are left alone.
- Keep the service's existing limited access: no screen-content retrieval or key filtering.
- Add regression tests for Home matching and for ignoring the settings dashboard and incomplete events.

Validation: 148 JVM unit tests passed; release APK built; Android lint completed with 0 errors and 72 warnings. Installed as an update on a TCL 65QM6K running Android 14. ADB-generated Settings presses kept the dashboard open from Wii Home and YouTube; Back returned to the same YouTube activity, and Home returned to Wii CRT. The TV owner also confirmed the physical remote Settings button works from both Wii Home and an app. Other TV firmware and reboot behavior have not been retested for this patch.

The signed APK updates v1.0.0 in place without uninstalling or clearing launcher preferences.
