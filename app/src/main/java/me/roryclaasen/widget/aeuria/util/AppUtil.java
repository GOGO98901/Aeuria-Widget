package me.roryclaasen.widget.aeuria.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import me.roryclaasen.widget.aeuria.R;

public class AppUtil {

	public static void openUrl(Activity activity, String url) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setPackage("com.android.chrome");
		try {
			activity.startActivity(i);
		} catch (ActivityNotFoundException e) {
			i.setPackage(null);
			activity.startActivity(i);
		}
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

    public static final PendingIntent getClockIntent(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

        String clockImpls[][] = {
                {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
                {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
                {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
                {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
                {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"} ,
                {"Sony Ericsson Xperia Z", "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock" },
                {"ASUS Tablets", "com.asus.deskclock", "com.asus.deskclock.DeskClock"}
        };

        boolean foundClockImpl = false;

        for(int i=0; i<clockImpls.length; i++) {
            String vendor = clockImpls[i][0];
            String packageName = clockImpls[i][1];
            String className = clockImpls[i][2];
            try {
                ComponentName cn = new ComponentName(packageName, className);
                ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                alarmClockIntent.setComponent(cn);
                foundClockImpl = true;
            } catch (PackageManager.NameNotFoundException e) {}
        }

        if (foundClockImpl) return PendingIntent.getActivity(context, 0, alarmClockIntent, 0);
        else return null;
    }

    public static float getDisplayDensity(Context context) {
        final DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

	public static String getLongestHour(Context context) {
		return context.getResources().getString(R.string.seven);
	}

	public static String getLongestMinute(Context context) {
		return context.getResources().getString(R.string.twenty) + "-" + context.getResources().getString(R.string.seven);
	}
}
