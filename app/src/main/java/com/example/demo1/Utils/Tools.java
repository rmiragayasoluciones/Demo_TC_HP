package com.example.demo1.Utils;

import java.io.IOException;

public class Tools {
    private static final String TAG = "Tools";
    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

//    public static void setSystemBarColor(Activity act) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = act.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
//        }
//    }
//
//    public static void setSystemBarColor(Activity act, @ColorRes int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = act.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(act.getResources().getColor(color));
//        }
//    }
//
//    public static void setSystemBarColorDialog(Context act, Dialog dialog, @ColorRes int color) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = dialog.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(act.getResources().getColor(color));
//        }
//    }
//
//    public static void setSystemBarLight(Activity act) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View view = act.findViewById(android.R.id.content);
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
//        }
//    }
//
//    public static void setSystemBarLightDialog(Dialog dialog) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View view = dialog.findViewById(android.R.id.content);
//            int flags = view.getSystemUiVisibility();
//            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            view.setSystemUiVisibility(flags);
//        }
//    }
}
