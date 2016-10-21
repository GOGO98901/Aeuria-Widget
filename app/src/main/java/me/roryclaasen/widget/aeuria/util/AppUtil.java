package me.roryclaasen.widget.aeuria.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
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

	public static String getLongestHour(Context context) {
		return context.getResources().getString(R.string.seven);
	}

	public static String getLongestMinuet(Context context) {
		return context.getResources().getString(R.string.twenty) + "-" + context.getResources().getString(R.string.seven);
	}
}
