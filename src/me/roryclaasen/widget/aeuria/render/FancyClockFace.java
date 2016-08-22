package me.roryclaasen.widget.aeuria.render;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import me.roryclaasen.widget.aeuria.util.AppUtil;
import me.roryclaasen.widget.aeuria.util.FancyTime;

public class FancyClockFace extends View {

	private Context mContext;
	private Calendar mCalendar;
	private FancyTime mTime;

	private int mClockAimFor = 800;
	private int mClockSize = mClockAimFor;
	private int mMargin;
	private RectF mBounds;

	private Paint mPaint;
	private Paint mPaintText;
	
	private int mTextSizeLarge = 100;
	private int mTextSizeSmall = 50;

	private int mBottom;
	private int mTop;
	private int mLeft;
	private int mRight;
	private boolean mSizeChanged;

	public FancyClockFace(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	public FancyClockFace(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public FancyClockFace(Context context) {
		super(context);
		init(context, null, 0);
	}

	private void init(Context context, AttributeSet attributeSet, int defStyle) {
		mContext = context;
		mCalendar = Calendar.getInstance();
		mTime = new FancyTime(context, mCalendar.getTime());

		// Typeface font_hour = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/Roboto.ttf");
		// Typeface font_min = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/MTCORSVA.TTF");

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);

		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintText.setColor(Color.BLACK);
		mPaintText.setStyle(Paint.Style.FILL);
		mPaintText.setTextAlign(Align.CENTER);
		// mPaintText.setTypeface(font_hour);
		mPaintText.setTextSize(AppUtil.convertDpToPixel(100, context));

		mMargin = 8;
		mBounds = new RectF(mMargin / 2, mMargin / 2, mClockAimFor - mMargin, mClockAimFor - mMargin);
	}

	public void setTime(long time) {
		mCalendar.setTimeInMillis(time);

		invalidate();
	}

	public void setTime(Calendar calendar) {
		mCalendar = calendar;

		invalidate();
	}

	public void setTimezone(TimeZone timezone) {
		mCalendar = Calendar.getInstance(timezone);
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mSizeChanged = true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final boolean sizeChanged = mSizeChanged;
		mSizeChanged = false;

		final int availW = mRight - mLeft;
		final int availH = mBottom - mTop;

		final int cX = availW / 2;
		final int cY = availH / 2;

		final int w = mClockSize;
		final int h = mClockSize;

		boolean scaled = false;

		if (availW < w || availH < h) {
			scaled = true;
			final float scale = Math.min((float) availW / (float) w, (float) availH / (float) h);
			canvas.save();
			canvas.scale(scale, scale, cX, cY);
		}

		if (sizeChanged) {
			mBounds.set(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY + (h / 2));
		}
		canvas.drawRoundRect(mBounds, w / 2, h / 2, mPaint);

		// TODO Draw text

		mPaintText.setTextSize(AppUtil.convertDpToPixel(mTextSizeLarge, mContext));
		//canvas.drawText(mTime.getHour().toUpperCase(Locale.getDefault()), cX, cY, mPaintText);
		mPaintText.setTextSize(AppUtil.convertDpToPixel(mTextSizeSmall, mContext));
		canvas.drawText(mTime.getMinute().toLowerCase(Locale.getDefault()), cX, cY + (int) (mPaintText.getTextSize() / 1.5), mPaintText);
		
		Path hourPath = new Path();
		mPaintText.getTextPath(mTime.getHour().toUpperCase(Locale.getDefault()), 0, mTime.getHour().length(), cX, cY, hourPath);
		
		canvas.clipPath(hourPath);
		if (scaled) {
			canvas.restore();
		}
	}

	// from AnalogClock.java
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float hScale = 1.0f;
		float vScale = 1.0f;

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mClockSize) {
			hScale = (float) widthSize / (float) mClockSize;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mClockSize) {
			vScale = (float) heightSize / (float) mClockSize;
		}

		final float scale = Math.min(hScale, vScale);

		setMeasuredDimension(getDefaultSize((int) (mClockSize * scale), widthMeasureSpec), getDefaultSize((int) (mClockSize * scale), heightMeasureSpec));
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return mClockSize;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return mClockSize;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// because we don't have access to the actual protected fields
		mRight = right;
		mLeft = left;
		mTop = top;
		mBottom = bottom;
	}

	@Override
	public boolean isInEditMode() {
		return true;
	}
}
