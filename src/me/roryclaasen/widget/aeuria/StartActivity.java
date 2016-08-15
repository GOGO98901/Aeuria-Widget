package me.roryclaasen.widget.aeuria;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import me.roryclaasen.widget.aeuria.util.AppUtil;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
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
		if (id == R.id.action_about) {
			AppUtil.openUrl(this, "http://roryclaasen.me/project/aeuria-widget");
			return true;
		}
		if (id == R.id.action_help) {
			AppUtil.openUrl(this, "http://github.com/GOGO98901/Aeuria-Widget/issues");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
