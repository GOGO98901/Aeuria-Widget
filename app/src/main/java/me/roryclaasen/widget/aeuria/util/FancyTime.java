package me.roryclaasen.widget.aeuria.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import me.roryclaasen.widget.aeuria.R;

public class FancyTime {
	private final Context _context;
	private final Date date;
	private final DateFormat format = new SimpleDateFormat("hh:mm", Locale.getDefault());

	private String time;

	public FancyTime(Context context, Date date) {
		this._context = context;
		this.date = date;
		getCurrentTime();
	}

	private void getCurrentTime() {
		time = format.format(date);
	}

	public String getHour() {
		getCurrentTime();
		String hour = time.split(":")[0];
		try {
			int number = Integer.parseInt(hour);
			if (number == 0) return getWord(12);
			return getWord(number);
		} catch (Exception e) {
			return _context.getString(R.string.error);
		}
	}

	public String getMinute() {
		getCurrentTime();
		String minute = time.split(":")[1];
		try {
			int number = Integer.parseInt(minute);
			if (number == 0 || number == 10 || number == 20 || number == 30 || number == 40 || number == 50) return getWord(number);
			if (number < 10) return _context.getString(R.string.ohh) + "-" + getWord(number);
			if (number < 20) return getWord(number);
			if (number < 30) return getWord(20) + "-" + getWord(number - 20);
			if (number < 40) return getWord(30) + "-" + getWord(number - 30);
			if (number < 50) return getWord(40) + "-" + getWord(number - 40);
			return getWord(50) + "-" + getWord(number - 50);
		} catch (Exception e) {
			return _context.getString(R.string.error);
		}
	}

	private String getWord(int number) {
		switch (number) {
			case (0): {
				return _context.getString(R.string.oclock);
			}
			case (1): {
				return _context.getString(R.string.one);
			}
			case (2): {
				return _context.getString(R.string.two);
			}
			case (3): {
				return _context.getString(R.string.three);
			}
			case (4): {
				return _context.getString(R.string.four);
			}
			case (5): {
				return _context.getString(R.string.five);
			}
			case (6): {
				return _context.getString(R.string.six);
			}
			case (7): {
				return _context.getString(R.string.seven);
			}
			case (8): {
				return _context.getString(R.string.eight);
			}
			case (9): {
				return _context.getString(R.string.nine);
			}
			case (10): {
				return _context.getString(R.string.ten);
			}
			case (11): {
				return _context.getString(R.string.eleven);
			}
			case (12): {
				return _context.getString(R.string.twelve);
			}
			case (13): {
				return _context.getString(R.string.thirteen);
			}
			case (14): {
				return _context.getString(R.string.fourteen);
			}
			case (15): {
				return _context.getString(R.string.fifteen);
			}
			case (16): {
				return _context.getString(R.string.sixteen);
			}
			case (17): {
				return _context.getString(R.string.seventeen);
			}
			case (18): {
				return _context.getString(R.string.eighteen);
			}
			case (19): {
				return _context.getString(R.string.nineteen);
			}
			case (20): {
				return _context.getString(R.string.twenty);
			}
			case (30): {
				return _context.getString(R.string.thirty);
			}
			case (40): {
				return _context.getString(R.string.forty);
			}
			case (50): {
				return _context.getString(R.string.fifty);
			}
			default: {
				return _context.getString(R.string.error);
			}
		}
	}
}
