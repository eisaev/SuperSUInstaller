# SuperSU Installer
This is a program for obtaining root permissions on a smart watch based on the Unisoc/Spreadtrum SP9820E SoC. This method will presumably work on all existing clones, such as A36E, Wonlex KT11, Lemfo     G4H 4G, etc. The installer uses the manufacturer's backdoor for engineering mode.

## HowTo
### Enable USB Debugging
1. Download [Shortcut Creator](https://play.google.com/store/apps/details?id=com.alextern.shortcuthelper) (apk file is available for download for example on [4pda](https://4pda.to/forum/index.php?showtopic=887333))
2. Send apk file via Bluetooth to your watch.
3. Open File Explorer (Settings->Clear tools->File Explorer) and find downloaded file (Local->bluetooth).
4. Install apk and run it immediately after installation by clicking "Open".
5. In Shortcut Creator, select "Settings" and find "Developer options".
6. Click the triangle in the upper-right corner.
7. In the list that appears, enable the "USB debugging" item.

Now you can work with the watch using adb.
### Installing SuperSUInstaller
1. [Download](https://github.com/eisaev/SuperSUInstaller/releases/download/v0.1b/SuperSUInstaller.apk) or build SuperSUInstaller.
2. Install apk to the watch:
```
adb install SuperSUInstaller.apk
```
### Installing SuperSU
1. Disable WiFi (Settings->Wifi).
2. Open the dialer (Phone).
3. Enter `*#*#83781#*#*` (you do not need to press the green button).
4. In the engineering menu that opens, switch to the "CONNECTIVITY" tab.
5. Click "Start Service"
6. Select "Wifi eut" and click "OK"
7. Run the SuperSUInstaller using adb:
```
adb shell 'am start -n ru.eisaev.supersuinstaller/.MainActivity'
```
8. Wait for the message "Hello World!" to appear.
9. Open File Explorer (Settings->Clear tools->File Explorer) and find SuperSU-v2.82-SR5.apk file (Local).
10. Install SuperSU-v2.82-SR5.apk and run it immediately after installation by clicking "Open".
11. Confirm the binary update using the "Normal" mode.
12. After the update is complete, confirm that the device is rebooted.
13. After booting the device, run the SuperSU GUI again:
```
adb shell 'am start -n eu.chainfire.supersu/.MainActivity'
```
14. Select "SETTINGS" tab and switch "Default access" to "Grant" (unfortunately, the prompt does not appear on these devices).

Now you can use applications that require root permissions, as well as use the root console using adb:
```
$ adb shell
shell@G4E:/ $ su
root@G4E:/ # id
uid=0(root) gid=0(root) context=kernel
```

