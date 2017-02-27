package me.roryclaasen.widget.aeuria;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;

import me.roryclaasen.widget.aeuria.render.FancyClockFace;
import me.roryclaasen.widget.aeuria.util.AppUtil;

public class AeuriaWidget extends AppWidgetProvider {
	private static final String TAG = AeuriaWidget.class.getSimpleName();

	private FancyClockFace clock;
	private boolean mFirst = true;
	private Bitmap mCached = null;

	public static String ACTION_UPDATE = "me.roryclaasen.widget.aeuria.ACTION_CLOCK_UPDATE";

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		startTicking(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);

		final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		alarmManager.cancel(createUpdate(context));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		final String action = intent.getAction();
        // Log.i(TAG, action);

		if (ACTION_UPDATE.equals(action) || action.equals("android.appwidget.action.APPWIDGET_UPDATE") || Intent.ACTION_TIME_CHANGED.equals(action) || Intent.ACTION_TIMEZONE_CHANGED.equals(action)) {
			final ComponentName appWidgets = new ComponentName(context.getPackageName(), getClass().getName());
			final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			final int ids[] = appWidgetManager.getAppWidgetIds(appWidgets);
			if (ids.length > 0) {
				onUpdate(context, appWidgetManager, ids);
			}
		}
	}

	protected float getSize(Context context) {
		return 453f * AppUtil.getDisplayDensity(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

		if (clock == null) {
			clock = new FancyClockFace(context);

			final int s = (int) getSize(context);
			clock.onSizeChanged(s, s, 0, 0);
			clock.measure(s, s);
			clock.layout(0, 0, s, s);
			clock.setDrawingCacheEnabled(true);

			final PendingIntent intent = AppUtil.getClockIntent(context);
			if (intent != null) {
                Intent openClockIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                openClockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				rv.setOnClickPendingIntent(R.id.clock, PendingIntent.getActivity(context, 0, openClockIntent, 0));
			}

			mCached = clock.getDrawingCache(true);
		}

		boolean shouldRecycle = false;

		if (mCached == null) {
			final int s = (int) getSize(context);
			mCached = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888);
			final Canvas c = new Canvas(mCached);
			clock.draw(c);

			mCached = Bitmap.createBitmap(mCached);
			shouldRecycle = true;
		}

		if (mCached != null) {
			rv.setImageViewBitmap(R.id.clock, mCached);
		} else {
			Log.e(TAG, "Could not render widget to bitmap");
		}

		if (!mFirst && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			appWidgetManager.partiallyUpdateAppWidget(appWidgetIds, rv);
		} else {
			appWidgetManager.updateAppWidget(appWidgetIds, rv);
			mFirst = false;
		}
		if (shouldRecycle) {
			mCached.recycle();
		}
	}

	private void startTicking(Context context) {
		// Log.i(TAG, "startTicking");
		final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.MINUTE, 1);
		alarmManager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), 1000 * 60, createUpdate(context));
	}

	private PendingIntent createUpdate(Context context) {
		return PendingIntent.getBroadcast(context, 0, new Intent(ACTION_UPDATE), PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
