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

	private int mClockAimFor = 400;
	private int mClockSize = mClockAimFor;
	private int mMargin;
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
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		fixSizes(canvas);

		canvas.drawText(mBounds.toString(), 0, 20, mPaint);
		canvas.drawRoundRect(mBounds, mClockSize / 2, mClockSize / 2, mPaint);
		
		//canvas.drawText(mBounds.toString(), 0, 20, mPaint);
		//canvas.drawText(getMeasuredWidth() + "x" + getMeasuredHeight(), 0, 40, mPaint);

		// canvas.drawColor(mPaint.getColor());

		// TODO Draw text

		//canvas.restore();
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

	private void fixSizes(final Canvas canvas) {
		fixSizes(canvas.getWidth(), canvas.getHeight());
		fixSizes(getWidth(), getHeight());
		// fixSizes(getMeasuredWidth(), getMeasuredHeight());
	}

	private void fixSizes(int width, int height) {
		width -= mMargin;
		height -= mMargin;
		
		if (width > height) mClockSize = height;
		else mClockSize = width;
		if (mClockSize > mClockAimFor) mClockSize = mClockAimFor;
		
		mBounds.set((width / 2) - (mClockSize / 2) + (mMargin / 2), (height / 2) - (mClockSize / 2) + (mMargin / 2), mClockSize, mClockSize);
	}
}
