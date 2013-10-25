package com.mammothGames.wake.gameEngine;

import android.util.Log;

public class engine_loadHelper {
	
	private int number_left_to_load = -1;
	private int number_to_load = -1;
	
	/**
	 * Call this before any loading calls.
	 * It's very important that the argument be correct!
	 * @param number
	 * This is the number of texture/font/sound/music files to be loaded.
	 */
	public void setNumberToLoad(int number) {
		number_left_to_load = number;
		number_to_load = number;
	}
	
	protected void informThatOneLoaded() {
		if (number_left_to_load > 0) {
			number_left_to_load--;
		}
	}
	
	public boolean checkFinished() {
//		Log.e("asd", "number left: " + number_to_load);
		if (number_left_to_load < 1) {
			
			//reset for next time
//			number_left_to_load = -1;
//			number_to_load = -1;
			
			return true;
		}
		return false;
	}
	
	public float checkFinishedScalar() {
		return ((float) number_to_load - number_left_to_load)/(number_to_load);
//		return ((float) number_left_to_load - number_to_load)/(number_left_to_load);
	}
	
	public void reset() {
		number_left_to_load = -1;
		number_to_load = -1;
	}
}