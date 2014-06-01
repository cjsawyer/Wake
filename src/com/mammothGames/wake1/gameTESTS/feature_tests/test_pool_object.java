package com.mammothGames.wake1.gameTESTS.feature_tests;

import android.util.Log;

import com.mammothGames.wake1.gameEngine.engine_main;
import com.mammothGames.wake1.gameEngine.engine_reference;
import com.mammothGames.wake1.gameEngine.utility_poolObject;

public class test_pool_object extends utility_poolObject {
	
	public float x = 0, y = 0, speed, r, g, b;
	int radius = 25;
	
	//changed way pool objets work.
//	@Override
//	public void reset() {
//		super.reset();
//		
//		radius = (int)(Math.random() * 40);
//		
//		speed = (float) (Math.random() * 100 + 50);
//		
//		x = (float) (ref.main.get_screen_width() * Math.random());
//		y = -radius;
//		
//		r = (float) Math.random();
//		g = (float) Math.random();
//		b = (float) Math.random();
//		//figure out why i need the catch block..
//		
//		
//		
//		
//		
//	}
}