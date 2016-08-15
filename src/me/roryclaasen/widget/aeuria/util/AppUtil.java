package me.roryclaasen.widget.aeuria.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

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
}
