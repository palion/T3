package net.tidal.games;

import net.tidal.R;
import android.app.Activity;
import android.os.Bundle;

public class TicTacToeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		System.out.println("test");
	}
}