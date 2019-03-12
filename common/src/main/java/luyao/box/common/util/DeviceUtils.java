package luyao.box.common.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * Created by luyao
 * on 2018/10/9 13:19
 */
public class DeviceUtils {

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getDisplay() {
        return Build.DISPLAY;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getBootloader() {
        return Build.BOOTLOADER;
    }

    public static String getHardware() {
        return Build.HARDWARE;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String[] getSupportedAbis() {
        return Build.SUPPORTED_ABIS;
    }

    public static String getRadioVersion() {
        return Build.getRadioVersion();
    }

    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getKernelVersion() {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("cat /proc/version", false);
        return commandResult.result == 0 ? commandResult.successResult : commandResult.errorResult;
    }

    public static int getSDK() {
        return Build.VERSION.SDK_INT;
    }

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @RequiresPermission(READ_PHONE_STATE)
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) return "";
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? telephonyManager.getImei() : telephonyManager.getDeviceId();
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    @RequiresPermission(READ_PHONE_STATE)
    public static String getMEID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) return "";
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? telephonyManager.getMeid() : telephonyManager.getDeviceId();
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(READ_PHONE_STATE)
    public static String getSN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? Build.getSerial() : Build.SERIAL;
    }


    public static String getMacAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;
            byte[] addresses = networkInterface.getHardwareAddress();
            if (addresses == null || addresses.length == 0) return "";
            StringBuilder builder = new StringBuilder();
            for (byte b : addresses) {
                builder.append(String.format(Locale.getDefault(), "%02x:", b));
            }
            return builder.deleteCharAt(builder.length() - 1).toString();
        }
        return "";
    }

    public static int getDeviceWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String getRamInfo(Context context){
        long totalSize;
        long availableSize;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        totalSize=memoryInfo.totalMem;
        availableSize=memoryInfo.availMem;

        return String.format("%s / %s", Formatter.formatFileSize(context,availableSize),Formatter.formatFileSize(context,totalSize));
    }

    public static String getRomInfo(Context context){
        long totalSize= Environment.getExternalStorageDirectory().getTotalSpace();
        long availableSize=Environment.getExternalStorageDirectory().getFreeSpace();

        return String.format("%s / %s", Formatter.formatFileSize(context,availableSize),Formatter.formatFileSize(context,totalSize));
    }


}
