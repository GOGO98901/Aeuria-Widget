package me.roryclaasen.widget.aeuria.render;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import android.graphics.PorterDuff;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.roryclaasen.widget.aeuria.R;
import me.roryclaasen.widget.aeuria.util.AppUtil;
import me.roryclaasen.widget.aeuria.util.FancyTime;

public class FancyClockFace extends View {

	private Context context;
	private Calendar calendar;
	private FancyTime fancyTime;

	private int clockSize = 1080;

	private RectF mBounds;

	private Paint mPaint;
	private Paint mPaintTextHour, mPaintTextMin;

	private float mTextSizeNormal;

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
		setWillNotDraw(false);
		setLayerType(LAYER_TYPE_HARDWARE, null);

		this.context = context;
		calendar = Calendar.getInstance();
		fancyTime = new FancyTime(context, calendar.getTime());

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(ContextCompat.getColor(context, R.color.widget_background_dark));
		mPaint.setStyle(Paint.Style.FILL);

		createFonts();

		mBounds = new RectF(0, 0, clockSize, clockSize);
	}

	private void createFonts() {
		Typeface font_hour = Typeface.createFromAsset(context.getAssets(), "fonts/roboto.ttf");
		Typeface font_min = Typeface.createFromAsset(context.getAssets(), "fonts/MTCORSVA.TTF");

		mPaintTextHour = createPaintFont();
		mPaintTextHour.setTypeface(font_hour);

        mPaintTextMin = createPaintFont();
		mPaintTextMin.setTypeface(font_min);

		mTextSizeNormal = mPaintTextHour.getTextSize();
	}

    private Paint createPaintFont() {
        Paint paint = new Paint();
        paint.setTextAlign(Align.CENTER);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        return paint;
    }

	public void setTime(long time) {
		calendar.setTimeInMillis(time);
		fancyTime = new FancyTime(context, calendar.getTime());

		invalidate();
	}

	public void setTime(Calendar calendar) {
		this.calendar = calendar;
		fancyTime = new FancyTime(context, this.calendar.getTime());

		invalidate();
	}

	public void setTimezone(TimeZone timezone) {
        setTime(Calendar.getInstance(timezone));
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

		final int centerX = availW / 2;
		final int centerY = availH / 2;

		boolean scaled = false;

		if (availW < clockSize || availH < clockSize) {
			scaled = true;
			final float scale = Math.min((float) availW / (float) clockSize, (float) availH / (float) clockSize);
			canvas.save();
			canvas.scale(scale, scale, centerX, centerY);
		}

		if (sizeChanged) {
			mBounds.set(centerX - (availW / 2), centerY - (availH / 2), centerX + (availW / 2), centerY + (availH / 2));
		}

		float radius = Math.min(availW, availH) / 2;
		canvas.drawCircle(centerX, centerY, radius, mPaint);

		setTextSize(mPaintTextHour, getWidth(radius, Math.round(mPaintTextHour.getTextSize())));
		canvas.drawText(fancyTime.getHour().toUpperCase(), centerX, centerY, mPaintTextHour);

		setTextSize(mPaintTextMin, getWidth(radius, Math.round(mPaintTextMin.getTextSize())));
		canvas.drawText(fancyTime.getMinute().toLowerCase(), centerX, centerY + (int) (mPaintTextMin.getTextSize()), mPaintTextMin);

		if (scaled) {
			canvas.restore();
		}
	}

    private float getWidth(float radius, int offset) {
        return (float) Math.sqrt((radius * radius) - (offset * offset)) * 2;
    }

	private void setTextSize(Paint paint, float desiredWidth) {
        String text = AppUtil.getLongestMinute(context);
        if (paint == mPaintTextHour) text = AppUtil.getLongestHour(context).toUpperCase();

		final float testTextSize = 48f;
		paint.setTextSize(testTextSize);
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		paint.setTextSize(testTextSize * desiredWidth / bounds.width());
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

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < clockSize) {
			hScale = (float) widthSize / (float) clockSize;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < clockSize) {
			vScale = (float) heightSize / (float) clockSize;
		}

		final float scale = Math.min(hScale, vScale);

		setMeasuredDimension(getDefaultSize((int) (clockSize * scale), widthMeasureSpec), getDefaultSize((int) (clockSize * scale), heightMeasureSpec));
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return clockSize;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return clockSize;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		mRight = right;
		mLeft = left;
		mTop = top;
		mBottom = bottom;
	}
}
