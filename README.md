# Wii TV CRT

A Wii-style home screen for Android TV and Google TV, with **CRT on/off** and **dark/light mode** you can change using your TV remote.

This is a fork of [MauroProto/wii-tv](https://github.com/MauroProto/wii-tv), built on Gathin's [cleanyourtv](https://github.com/Gathin23/cleanyourtv). It is a customized Android launcher, not replacement TV firmware. Google TV and your streaming apps still run normally.

**[Download the latest release](https://github.com/etco920/wii-tv-crt/releases/latest)** · [Original upstream documentation](UPSTREAM.md)

## What's new in v1.0.1

Fixed the remote Settings button closing immediately or sending you back to Wii Home from another app when the optional Home service is enabled. Google TV's quick-settings dashboard now stays open, and Back returns to the app underneath. Home still opens Wii CRT.

Verified on a TCL 65QM6K running Android 14, from Wii Home and YouTube. See the [changelog](CHANGELOG.md) for details.

## Appearance choices

Open the round **wiiOS** button at the bottom left, then choose **Appearance**. A remote with a Menu button can open Appearance directly.

| Setting | On | Off |
| --- | --- | --- |
| CRT screen effect | Scanlines, a subtle RGB phosphor texture, and an edge vignette over the whole launcher, including app logos | Clean picture with no CRT overlay |
| Dark mode | Charcoal-blue menu chrome with pale clock/date text and blue highlights | The original light Wii-style menu chrome |

The switches are independent: dark CRT, dark clean, light CRT, and light clean all work. Choices save automatically and survive restarting the app. Press Back to return to the menu. Logos keep their own artwork and colors. CRT also applies inside the Mii channel; dark/light changes the main menu chrome. Streaming apps retain their own appearance after opening.

The player-one pointer uses the same Wii-style sprite as the original TV customization, tilted toward the selected channel's lower-right corner. See [asset credits](THIRD_PARTY_ASSETS.md) for the sprite source; the upstream code license does not imply a separate license grant for that artwork.

## Requirements

- Android TV / Google TV 8.0 or newer, with a 16:9 display.
- A remote with arrows, OK, Back, and Home.
- A way to sideload an APK. A computer with Google's [Android platform tools](https://developer.android.com/tools/releases/platform-tools) is one option.

The customization was developed and checked on a TCL 65QM6K running Android 14 at a 1080p UI resolution. Other models may handle Home and HDMI switching differently. This is a fan project, not affiliated with Nintendo, Google, or TCL.

## Install the ready-made APK

1. Download `wii-tv-crt-v1.0.1.apk` from **Releases**, plus the checksum if you want to verify the download.
2. Transfer it to the TV and install it with a file manager, or use the computer method below.
3. Open **Wii CRT** from the TV's app list. It can be tried as an ordinary app before changing Home behavior.
4. Open **wiiOS → Appearance** to select CRT and dark/light mode.

Already using the signed v1.0.0 release? Install v1.0.1 over it as an update; no uninstall is needed. Your saved launcher preferences are retained.

### Install from a computer

Enable Developer options on the TV: Settings → System → About → select **Android TV OS build** seven times. Then enable USB/ADB debugging or Wireless debugging, depending on what your TV offers. The TV and computer must share a network.

For a TV that exposes ADB on its network address:

```sh
adb connect TV_IP:PORT
adb -s TV_IP:PORT install -r PathToAPK
```

Some TVs expose port `5555` when USB/ADB debugging is enabled. Others use Wireless debugging: select **Pair device with pairing code**, then run:

```sh
adb pair TV_IP:PAIRING_PORT
```

Enter the code shown on the TV. Press Back to the main Wireless debugging page and use its **connection** address and port for `adb connect`. The pairing port and connection port are different. Accept the TV's debugging authorization if asked. Replace the placeholders with your own TV's details.

You can also launch it directly:

```sh
adb -s TV_IP:PORT shell am start -n dev.wiicrt.launcher/com.gathin.tvhome.WiiMenuActivity
```

This installer does not remove apps, erase app data, disable Google TV, or change your picture brightness. The inherited upstream `build-and-install` and debloating scripts target the upstream package and are not this fork's installation method; use the release APK steps above.

## Make Home open Wii CRT (optional)

First use Android's **Default apps → Home app** setting if your TV offers it. Choose Wii CRT.

On Google TVs that keep redirecting Home to the stock launcher, an optional service is included:

1. Open Android **Settings → Accessibility**.
2. Enable **Home button** belonging to **Wii CRT**.
3. Press Home and check that Wii CRT opens.

This fork's service receives only window-change events from Google's TV launchers. It cannot retrieve screen contents or filter keystrokes. It matches the event's activity against Android's registered Home activities before opening the Wii menu, so Google TV's remote Settings dashboard is left alone. Google's launcher remains enabled and may briefly appear during the redirect. It does not replace Android or remove Google services.

### TCL: Home works until a full restart

Some TCL firmware separately blocks automatic startup. If the service is enabled but fails after reboot, inspect its app-specific permission:

```sh
adb -s TV_IP:PORT shell appops get dev.wiicrt.launcher
```

If this reports `AUTO_START: ignore`, allow that one permission and explicitly save it:

```sh
adb -s TV_IP:PORT shell appops set dev.wiicrt.launcher AUTO_START allow
adb -s TV_IP:PORT shell appops write-settings
```

Some firmware calls the same permission `APP_AUTO_START`; use the name your device actually reports. Then toggle the Wii CRT service off/on in Accessibility and restart the TV to verify it. If neither permission exists, stop and use the TV's own startup settings. Do not disable setup/recovery or other system components to work around this.

Once setup is verified, debugging can be switched off again. It is not required for normal use.

## Remote controls

| Button | Action |
| --- | --- |
| Arrows | Move the pointer; right at the page edge changes pages |
| OK | Open the selected app channel or button |
| Hold OK | Pick up a channel, move it, and press OK to drop it |
| Channel + / − | Change pages |
| Menu, if present | Open Appearance |
| Settings / gear | Open the TV's native settings panel; on the tested TCL this is Google TV's quick-settings dashboard |
| wiiOS button | Appearance or Android TV settings |
| SD button | TV media player, where supported |
| Envelope button | Inputs; uses TCL's native source selector on TCL TVs |
| Home | Return to the launcher if selected as Home or the optional service is enabled |

## Performance

The release is built with the upstream R8/resource optimizations. The menu keeps idle live tiles to about 20 FPS, retains normal navigation animation scheduling, and stops scheduling new frames when hidden. The CRT overlay uses cached textures/gradients with no blur, frame capture, or independent animation timer. Turn CRT off for the lowest rendering cost.

A custom launcher is not a guaranteed system-wide speedup. Streaming app performance, buffering, and HDMI input lag are separate. The CRT effect adds some rendering work.

## Undo

If you enabled the Home service, turn it off under Android Accessibility. Restore your previous default Home app if you changed it. Then uninstall **Wii CRT** normally through Android Apps settings. Your original apps and their data are untouched.

The package is `dev.wiicrt.launcher`, separate from upstream `com.gathin.tvhome`. An upstream or private-customization APK cannot be installed over it as an update.

## Build and contribute

Use JDK 17+, Android SDK 34/build-tools 34.0.0, and the included Gradle wrapper:

```sh
git clone --branch crt-dark-mode https://github.com/etco920/wii-tv-crt.git
cd wii-tv-crt/launcher
./gradlew --no-daemon testReleaseUnitTest lintRelease assembleRelease
```

On Windows use `gradlew.bat`. Point `ANDROID_HOME` or an untracked `launcher/local.properties` at your SDK. Output: `launcher/app/build/outputs/apk/release/app-release.apk`. Local/CI builds are debug-signed; see [RELEASING.md](RELEASING.md) for stable signed releases.

The custom branch starts from upstream revision `4a763c7cb03d95fcc7ba39dbab41727df12d4d35`. Upstream history is retained. Newer upstream work remains on `main`; this branch is the tested CRT customization.

## Credits and license

- **MauroProto**: wii-tv and its Wii-style interface, Mii channel, and supporting tools.
- **Gathin**: cleanyourtv, the original launcher base and tools.
- **Soffizlly**: bundled audio remakes, included under upstream's permission/license.
- **Crayon / Wii-Tac-Toe**: source of the player-one pointer sheet; see THIRD_PARTY_ASSETS.md.
- **Outfit Project Authors**: Outfit font, SIL Open Font License 1.1.
- **etco920's customization**: CRT overlay, dark/light configuration, corner-targeted pointer styling, and reversible install documentation.

The upstream MIT license and font/audio credits are preserved. See [LICENSE](LICENSE). Wii is a Nintendo trademark. No Wii firmware, ROMs, or game files are included.
