package com.mammothGames.wake.game.entities.feature_tests;

import com.mammothGames.wake.gameEngine.*;


public class entity_loadingDebug extends engine_entity {

	@Override
	public void sys_firstStep() {}
	
	@Override
	public void sys_step() {
		
//		ref.draw.setDrawColor(1, 1, 1, 1);
//		ref.draw.drawCircle(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2, ref.main.get_screen_width()/5, 0, 0, 360, 0, 2);
		
//		ref.draw.setDrawColor(0.5f, 0.5f, 0.5f, 0.5f);
//		ref.draw.drawCircle(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2 - 50, ref.main.get_screen_width()/5, 0, 0, 360, 0, 2);
		
		ref.draw.setDrawColor(1, 1, 1, 1);
//		ref.draw.drawCircle(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2 - 100, ref.main.get_screen_width()/5, 0, 0, 360, 0, 2);
		
		
		for(int i=0;i<50;i++){
			ref.draw.setDrawColor((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
//			ref.draw.drawTexture(100+10*i, 100+10*i, 100, 100, -50, -50, 0, 100,   1,1);
			ref.draw.drawRectangle(100+10*i, 100+10*i, 100, 100, -50, -50, 0, 100);
		}
		
		
	}
}
