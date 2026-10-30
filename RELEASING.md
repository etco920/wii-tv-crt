# Releasing Wii TV CRT

The public package is `dev.wiicrt.launcher`. It installs alongside the upstream app and earlier private TV customizations.

1. Run the checks from `launcher/`:
   ```sh
   ./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease
   ```
2. Test CRT on/off and dark/light mode on a real TV, including leaving the menu, reopening it, and restarting the app. With the Home service enabled, test the remote Settings button from Wii Home and another app: the panel must stay open, Back must return to the underlying app, and Home must still return to Wii CRT. Keep the stock launcher enabled during testing.
3. Sign the APK using the maintainer's existing private release keystore, kept outside this repository. Reuse that key for every public release so Android accepts updates. For example:
   ```sh
   apksigner sign --ks /private/path/release.jks --ks-key-alias release \
     --ks-pass env:WII_CRT_STORE_PASSWORD --key-pass env:WII_CRT_STORE_PASSWORD \
     --out wii-tv-crt-v1.0.1.apk launcher/app/build/outputs/apk/release/app-release.apk
   apksigner verify --verbose --print-certs wii-tv-crt-v1.0.1.apk
   ```
4. Calculate and publish the APK's SHA-256 checksum. Check the staged files for device addresses, account information, backups, signing keys, and local build files.
5. Publish a GitHub release from the `crt-dark-mode` branch with the signed APK and checksum. State the tested TV/Android versions and any limitations.

GitHub Actions artifacts use a temporary debug certificate. They are test builds, not interchangeable updates for the signed public release. No private signing material is stored in Git or uploaded by CI.
