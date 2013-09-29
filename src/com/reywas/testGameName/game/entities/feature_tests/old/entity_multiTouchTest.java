package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.gameEngine.*;

import android.util.Log;


public class entity_multiTouchTest extends engine_entity {
	
	
	@Override
	public void sys_step(){
		
	}
	
	@Override
	public void sys_firstStep(){
		
		entity_multiTouchTestPoint p1 = new entity_multiTouchTestPoint();  
		entity_multiTouchTestPoint p2 = new entity_multiTouchTestPoint();  
		entity_multiTouchTestPoint p3 = new entity_multiTouchTestPoint();  
		entity_multiTouchTestPoint p4 = new entity_multiTouchTestPoint();  
		entity_multiTouchTestPoint p5 = new entity_multiTouchTestPoint();  
		
		ref.main.addEntity(p1);
		ref.main.addEntity(p2);
		ref.main.addEntity(p3);
		ref.main.addEntity(p4);
		ref.main.addEntity(p5);
		
		p1.touch_index = 0; p1.r = (float) Math.random(); p1.g = (float) Math.random(); p1.b = (float) Math.random();
		p2.touch_index = 1; p2.r = (float) Math.random(); p2.g = (float) Math.random(); p2.b = (float) Math.random();
		p3.touch_index = 2; p3.r = (float) Math.random(); p3.g = (float) Math.random(); p3.b = (float) Math.random();
		p4.touch_index = 3; p4.r = (float) Math.random(); p4.g = (float) Math.random(); p4.b = (float) Math.random();
		p5.touch_index = 4; p5.r = (float) Math.random(); p5.g = (float) Math.random(); p5.b = (float) Math.random();
	
		
	}
	
}