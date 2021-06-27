package com.dadi590.assist_c_a.GlobalUtils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.dadi590.assist_c_a.GlobalUtils.ExtClasses.SystemAppChecker;
import com.dadi590.assist_c_a.BroadcastRecvs.DeviceAdmin.DeviceAdminRecv;

import java.io.File;

/**
 * <p>Global app-related utilities.</p>
 */
public final class UtilsApp {

    /**
     * <p>Private empty constructor so the class can't be instantiated (utility class).</p>
     */
    private UtilsApp() {
    }

    /**
     * <p>Gets the app ready to be shut down. For example might save (NOT stop) some important processing as fast as
     * possible for the app to be shut down.</p>
     * <br>
     * <p>This method should be useful if a shut down or reboot is detected or if the user clicks Force Stop on the
     * Settings app (if it's available).</p>
     * <br>
     * <p>NOTE: this does NOT shut down the app. The only supposed way to stop the app is the system by shutting down
     * the phone, and nothing else.</p>
     * <br>
     * <p>This method does not stop ongoing tasks because any system app can send the shut down broadcast or something,
     * and that could be useful in a malicious way. Not stopping anything and only saving <em>in case</em> the app is
     * shut down prevents that.</p>
     * <br>
     * <p><u><i><b>CURRENTLY THIS METHOD DOES NOTHING</b></i></u></p>
     */
    public static void prepareShutdown() {
        // todo PUT THE APP RESETTING THE NORMAL CHARGING MODE ON SHUT DOWN AND UNINSTALLATION OR IT'S DEATH!!!!!!
        // (in case the stop charging is implemented)
    }

    /**
     * <p>Gets the app ready to be uninstalled. For example might reset important things it changed on the system - an
     * example could be reset the charging in case it has been disabled because the battery is at 80%.</p>
     * <br>
     * <p>This method should be useful if it's requested to the assistant that the app should be uninstalled, or in case
     * the user clicks the Uninstall button on the Settings app (if it's available).</p>
     * <br>
     * <p>NOTE: this does NOT uninstall the app. The only supposed way to uninstall the app is by telling the assistant
     * to do it, and nothing else.</p>
     * <br>
     * <p><u><i><b>CURRENTLY THIS METHOD DOES NOTHING</b></i></u></p>
     */
    public static void prepareUninstall() {
        // todo PUT THE APP RESETTING THE NORMAL CHARGING MODE ON SHUT DOWN AND UNINSTALLATION OR IT'S DEATH!!!!!!
        // (in case the stop charging is implemented)

        // todo See if the method used to detect the uninstallation is 100% secure, as opposite to detecting a shut down
        // If it's not that much secure, reset for a period of time and then put the settings/files back, or something.
        // Try to think of anything better than that, as that seems like a security hole.

        // If it's to be uninstalled, will be shut down too, so get ready for that too, but in the end.
        prepareShutdown();
    }

    /**
     * Deletes the app cache.
     *
     * @param context a context
     */
    public static void deleteAppCache(@NonNull final Context context) {
        final File dir = context.getCacheDir();
        UtilsGeneral.deletePath(dir);
    }

    /**
     * <p>Checks if an app is installed on the device or not.</p>
     *
     * @param context a context
     * @param packageName The name of the package of the app to be checked
     *
     * @return true if the app is installed, false otherwise
     */
    public static boolean isAnAppInstalled(@NonNull final Context context, @NonNull final String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (final PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * <p>Checks if an installed app is enabled or not. Check if the app is installed before calling this function.</p>
     *
     * @param context a context
     * @param packageName the name of the package of the app to be checked
     *
     * @return true if the app is installed, false otherwise
     */
    public static boolean isAppEnabled(@NonNull final Context context, @NonNull final String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        final int appEnabledSetting = packageManager.getApplicationEnabledSetting(packageName);

        return appEnabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED &&
                appEnabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER;
    }

    public static final int NORMAL = 0;
    public static final int SYSTEM_WITHOUT_UPDATES = 1;
    public static final int SYSTEM_WITH_UPDATES = 2;
    /**
     * <p>Checks if the app is installed as normal app (with or without updates), as system app without updates, or
     * system app with updates.</p>
     * <br>
     * <p><u>---CONSTANTS---</u></p>
     * <p>- {@link #NORMAL} --> returned if the app is a normal app (with or without updates)</p>
     * <p>- {@link #SYSTEM_WITHOUT_UPDATES} --> returned if the app is a system app without updates</p>
     * <p>- {@link #SYSTEM_WITH_UPDATES} --> returned if the app is a system app with updates</p>
     * <p><u>---CONSTANTS---</u></p>
     *
     * @param context a context
     *
     * @return one of the constants
     */
    public static int appInstallationType(@NonNull final Context context) {
        if (SystemAppChecker.isSystemApp(context)) {
            if (SystemAppChecker.isSystemUpdatedAppByFLAG(context)) {
                System.out.println("---------------SYSTEM WITH UPDATES---------------");
                return SYSTEM_WITH_UPDATES;
            } else {
                System.out.println("---------------SYSTEM WITHOUT UPDATES---------------");
                return SYSTEM_WITHOUT_UPDATES;
            }
        } else {
            System.out.println("---------------NORMAL---------------");
            return NORMAL;
        }
    }

    /**
     * <p>Checks if the app is a Device Administrator.</p>
     *
     * @param context a context
     *
     * @return true if it's a Device Administrator, false otherwise
     */
    public static boolean isDeviceAdmin(@NonNull final Context context) {
        final DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        final ComponentName mAdminName = new ComponentName(context, DeviceAdminRecv.class);

        return mDPM.isAdminActive(mAdminName);
    }
}
