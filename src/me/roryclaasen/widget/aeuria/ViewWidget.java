package me.roryclaasen.widget.aeuria;

import java.util.Date;
import java.util.TimerTask;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.RemoteViews;
import me.roryclaasen.widget.aeuria.util.ClearTextView;
import me.roryclaasen.widget.aeuria.util.FancyTime;

public class ViewWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		System.out.println("yay");
		Paint p = new Paint(); 
		p.setAntiAlias(true);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(8);
		p.setColor(0xFFFF0000);

		Bitmap bitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawArc(new RectF(10, 10, 90, 90), 0, 270, false, p);

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		views.setImageViewBitmap(R.id.widget_Image, bitmap);

		appWidgetManager.updateAppWidget( new ComponentName(context, ViewWidget.class), views);
		

		System.out.println("updated");
		// Timer timer = new Timer();
		// timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);

	}

	private class MyTime extends TimerTask {
		RemoteViews remoteViews;
		Context context;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		FancyTime time;

		public MyTime(Context context, AppWidgetManager appWidgetManager) {
			this.context = context;
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
			thisWidget = new ComponentName(context, ViewWidget.class);
			time = new FancyTime(context, new Date());
		}

		@Override
		public void run() {
			draw();
			// remoteViews.setTextViewText(R.id.widget_hour, time.getHour());
			// remoteViews.setTextViewText(R.id.widget_minute, time.getMinute());
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		}

		private void draw() {
			System.out.println("drawing");
			ClearTextView myView = new ClearTextView(context);
			myView.measure(500, 500);
			myView.layout(0, 0, 500, 500);
			myView.setText(time.getHour());
			myView.draw(500, 500);
			Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
			myView.draw(new Canvas(bitmap));
			remoteViews.setImageViewBitmap(R.id.widget_Image, drawTextToBitmap(context, R.id.widget_Image, "this is a test"));
		}

		public Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
			Resources resources = gContext.getResources();
			float scale = resources.getDisplayMetrics().density;
			Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

			android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
			// set default bitmap config if none
			if (bitmapConfig == null) {
				bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);

			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// text color - #3D3D3D
			paint.setColor(Color.rgb(61, 61, 61));
			// text size in pixels
			paint.setTextSize((int) (14 * scale));
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

			// draw text to the Canvas center
			Rect bounds = new Rect();
			paint.getTextBounds(gText, 0, gText.length(), bounds);
			int x = (bitmap.getWidth() - bounds.width()) / 2;
			int y = (bitmap.getHeight() + bounds.height()) / 2;

			canvas.drawText(gText, x, y, paint);

			return bitmap;
		}
	}
}
