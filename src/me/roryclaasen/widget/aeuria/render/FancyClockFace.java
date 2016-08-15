package me.roryclaasen.widget.aeuria.render;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class FancyClockFace extends View {

	private Calendar mCalendar;

	private int mDialWidth;
	private int mDialHeight;

	private int mBottom;
	private int mTop;
	private int mLeft;
	private int mRight;
	private boolean mSizeChanged;

	private RectF mBounds;

	private Paint mPaint;

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
		mCalendar = Calendar.getInstance();

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);

		mBounds = new RectF(mLeft, mTop, mRight, mBottom);
		mSizeChanged = true;
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

		final int w = mDialWidth;
		final int h = mDialHeight;

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

		canvas.drawRoundRect(mBounds, (w / 2), (h / 2), mPaint);

		System.out.println(canvas.getWidth() + ", " + canvas.getHeight());

		// TODO Draw text

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

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
			hScale = (float) widthSize / (float) mDialWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
			vScale = (float) heightSize / (float) mDialHeight;
		}

		final float scale = Math.min(hScale, vScale);

		setMeasuredDimension(getDefaultSize((int) (mDialWidth * scale), widthMeasureSpec), getDefaultSize((int) (mDialHeight * scale), heightMeasureSpec));
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return mDialHeight;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return mDialWidth;
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
}
