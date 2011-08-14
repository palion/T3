package net.tidal.games;

import java.util.ArrayList;

import net.tidal.R;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int turn = 1;
	private int winningCombo[] = new int[3];
	private ArrayList<Integer> playerSelections;
	private int occupiedBitMask = 0;

	private Drawable surfboard;
	private Drawable wave;
	private Drawable icon;

	private static final int[] BUTTONS = {
		R.id.Button1, R.id.Button2, R.id.Button3, R.id.Button4, R.id.Button5,
		R.id.Button6, R.id.Button7,	R.id.Button8, R.id.Button9
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Resources res = getBaseContext().getResources();
		surfboard = res.getDrawable(R.drawable.surfboard);
		wave = res.getDrawable(R.drawable.wave);
		icon = res.getDrawable(R.drawable.icon);

		playerSelections = new ArrayList<Integer>(9);
		for(int i = 0; i < 9; i++){
			playerSelections.add(i, 0);
		}

		TextView textView = (TextView)findViewById(R.id.status);
		textView.setText(this.getString(R.string.player) + "["+ turn + "]" + this.getString(R.string.turn));

		for(int i = 0; i < 9; i++){
			ButtonClickListener listener = new ButtonClickListener(i);
			findViewById(BUTTONS[i]).setOnClickListener(listener);
		}

		findViewById(R.id.Reset).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reset(v);
			}
		});
	}

	private void takeTurn(View view, int index) {

		ImageButton button = (ImageButton)view;

		if((occupiedBitMask & (1 << index)) == 0 && getWinner() == 0){

			playerSelections.set(index, turn);

			if(turn == 1){
				button.setImageDrawable(surfboard);//setText(view.getContext().getString(R.string.x));
			}else{
				button.setImageDrawable(wave);//setText(view.getContext().getString(R.string.o));
			}

			occupiedBitMask = occupiedBitMask + (1 << index);

			turn = (turn == 1) ? 2 : 1;

			if(getWinner() != 0){
				updateStatusText(view, view.getContext().getString(R.string.player) + getWinner() + view.getContext().getString(R.string.win));
				updateWinningButtonColor(Color.RED);
			}else if(occupiedBitMask == 511){
				updateStatusText(view, view.getContext().getString(R.string.tie));
			}
			else{
				updateStatusText(view, getStatus(view));
			}

		}else{
			updateStatusText(view, view.getContext().getString(R.string.invalid_move));
		}
	}

	private void reset(View view) {
		updateWinningButtonColor(0);
		turn = 1;
		winningCombo = new int[3];
		playerSelections = new ArrayList<Integer>(9);
		for(int i = 0; i < 9; i++){
			playerSelections.add(i, 0);
		}

		occupiedBitMask = 0;

		for(int rid : BUTTONS){
			resetButtonTextByResourceId(rid);
		}

		if(view != null){
			updateStatusText(view, getStatus(view));
		}
	}

	private void updateStatusText(View v, String text){
		TextView textView = (TextView)findViewById(R.id.status);
		textView.setText(text);
	}

	private void resetButtonTextByResourceId(int id){
		ImageButton b = (ImageButton)findViewById(id);
		b.setImageDrawable(icon);
	}

	private String getStatus(View v){
		return v.getContext().getString(R.string.player) +"[" + turn + "]" + v.getContext().getString(R.string.turn);
	}

	private int getWinner(){
		if(isWin(0, 1, 2) != 0){winningCombo[0] = 0; winningCombo[1] = 1; winningCombo[2] = 2; return isWin(0, 1, 2);}
		if(isWin(3, 4, 5) != 0){winningCombo[0] = 3; winningCombo[1] = 4; winningCombo[2] = 5; return isWin(3, 4, 5);}
		if(isWin(6, 7, 8) != 0){winningCombo[0] = 6; winningCombo[1] = 7; winningCombo[2] = 8; return isWin(6, 7, 8);}
		if(isWin(0, 4, 8) != 0){winningCombo[0] = 0; winningCombo[1] = 4; winningCombo[2] = 8; return isWin(0, 4, 8);}
		if(isWin(0, 3, 6) != 0){winningCombo[0] = 0; winningCombo[1] = 3; winningCombo[2] = 6; return isWin(0, 3, 6);}
		if(isWin(1, 4, 7) != 0){winningCombo[0] = 1; winningCombo[1] = 4; winningCombo[2] = 7; return isWin(1, 4, 7);}
		if(isWin(2, 5, 8) != 0){winningCombo[0] = 2; winningCombo[1] = 5; winningCombo[2] = 8; return isWin(2, 5, 8);}
		if(isWin(2, 4, 6) != 0){winningCombo[0] = 2; winningCombo[1] = 4; winningCombo[2] = 6; return isWin(2, 4, 6);}
		return 0;
	}

	private int isWin(int x, int y, int z){
		if((playerSelections.get(x) != null
				&& playerSelections.get(y) != null
				&& playerSelections.get(z) != null)
				&& playerSelections.get(x) == playerSelections.get(y)
				&& playerSelections.get(y) == playerSelections.get(z)){
			return playerSelections.get(x);
		}
		return 0;
	}

	private void updateWinningButtonColor(int color){
		ImageButton button0 = (ImageButton)findViewById(BUTTONS[winningCombo[0]]);
		button0.setBackgroundColor(color);
		ImageButton button1 = (ImageButton)findViewById(BUTTONS[winningCombo[1]]);
		button1.setBackgroundColor(color);
		ImageButton button2 = (ImageButton)findViewById(BUTTONS[winningCombo[2]]);
		button2.setBackgroundColor(color);
	}

	// 1  2   4
	// 8  16  32
	// 64 128 256

	public class ButtonClickListener implements OnClickListener {

		private int index;

		public ButtonClickListener() {
			// TODO Auto-generated constructor stub
		}

		public ButtonClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View view) {
			takeTurn(view, index);
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
