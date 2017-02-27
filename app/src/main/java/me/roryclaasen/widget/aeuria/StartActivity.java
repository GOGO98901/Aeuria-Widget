package me.roryclaasen.widget.aeuria;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import me.roryclaasen.widget.aeuria.render.FancyClockFace;
import me.roryclaasen.widget.aeuria.util.AppUtil;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                setContentView(R.layout.activity_start);
                View clock = findViewById(R.id.clock);
                if (clock instanceof FancyClockFace) {
                    ((FancyClockFace) clock).setTime(Calendar.getInstance());
                }
                handler.postDelayed(this, 60 * 1000);
            }
        }, (60 - Calendar.getInstance().get(Calendar.SECOND)) * 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_github) {
			AppUtil.openUrl(this, "http://github.com/GOGO98901/Aeuria-Widget");
			return true;
		}
		if (id == R.id.action_help) {
			AppUtil.openUrl(this, "http://github.com/GOGO98901/Aeuria-Widget/issues");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
