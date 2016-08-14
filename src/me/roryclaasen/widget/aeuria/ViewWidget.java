package me.roryclaasen.widget.aeuria;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;
import me.roryclaasen.widget.aeuria.util.FancyTime;

public class ViewWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);
	}

	private class MyTime extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		FancyTime time;

		public MyTime(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			thisWidget = new ComponentName(context, ViewWidget.class);
			time = new FancyTime(context, new Date());
		}

		@Override
		public void run() {
			remoteViews.setTextViewText(R.id.widget_hour, time.getHour());
			remoteViews.setTextViewText(R.id.widget_minute, time.getMinute());
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}
	}
}
