package me.roryclaasen.widget.aeuria;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.widget.RemoteViews;

public class ViewWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(8);
		p.setColor(0xFFFF0000);

		Bitmap bitmap = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawArc(new RectF(0, 0, 500, 500), 0, 270, false, p);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		views.setImageViewBitmap(R.id.widget_Image, bitmap);
		appWidgetManager.updateAppWidget(new ComponentName(context, ViewWidget.class), views);

		// Timer timer = new Timer();
		// timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		setWidgetActive(true);
		context.startService(new Intent(appContext, WidgetUpdateService.class));
	}

	@Override
	public void onDisabled(Context context) {
		Context appContext = context.getApplicationContext();
		setWidgetActive(false);
		context.stopService(new Intent(appContext, WidgetUpdateService.class));
		super.onDisabled(context);
	}

	private void setWidgetActive(boolean active) {
		Context appContext = context.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(appContext);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean(Constants.WIDGET_ACTIVE, active);
		edit.commit();
	}
	/*
	 * private class MyTime extends TimerTask {
	 * RemoteViews remoteViews;
	 * Context context;
	 * AppWidgetManager appWidgetManager;
	 * ComponentName thisWidget;
	 * FancyTime time;
	 * 
	 * public MyTime(Context context, AppWidgetManager appWidgetManager) {
	 * this.context = context;
	 * this.appWidgetManager = appWidgetManager;
	 * remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
	 * thisWidget = new ComponentName(context, ViewWidget.class);
	 * time = new FancyTime(context, new Date());
	 * }
	 * 
	 * @Override
	 * public void run() {
	 * remoteViews.setTextViewText(R.id.widget_hour, time.getHour());
	 * remoteViews.setTextViewText(R.id.widget_minute, time.getMinute());
	 * appWidgetManager.updateAppWidget(thisWidget, remoteViews);
	 * }
	 * }
	 */
}
