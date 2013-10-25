package com.mammothGames.wake.game.entities.feature_tests.old;

import android.util.Log;

import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_particleTest extends engine_entity {

	
	private engine_particleEmitter emitter1, emitter2;
	float x, y;
	
	@Override
	public void sys_firstStep(){
		
		//Rainbow down
		emitter1 = new engine_particleEmitter(ref);
		ref.main.addEntity(emitter1);
		
		int width = ref.main.get_screen_width();
		int lifetime = 2; //secs
		int speed = width/lifetime*2/3;
		
		emitter1.setXY(0,ref.main.get_screen_height()/2);
		emitter1.setLifeTime(lifetime * 1000);
		emitter1.setGravityAndDirection(0 ,270);
		emitter1.setVelocityAndDirection(speed,speed,  0,0);
		emitter1.setDrawAngleAndChange(0,0,144,144);
		emitter1.setDrawTypeToSprite(game_textures.TEX_ERROR, 1, 10, 150, 0);
		emitter1.setSizeChange(.5f, .5f);
		emitter1.setColor(0,    1,0,0, 1);//red
		emitter1.setColor(1,    0,1,0, 1);//green
		emitter1.setColor(2,    0,0,1, 1);//blue
		emitter1.setColor(3,    1,1,0, 1);//yellow
		emitter1.setColor(4,    1,0,1, 1);//pink
		emitter1.setColor(4,    0,0,0, 1);//black
		
		
		
		//Obama Faces
		emitter2 = new engine_particleEmitter(ref);
		ref.main.addEntity(emitter2);
		
		emitter2.setGravityAndDirection(ref.main.get_screen_height()/3 ,90);
		emitter2.setXY(375,ref.main.get_screen_height()/2);
		emitter2.setLifeTime(2000);
		emitter2.setVelocityAndDirection(100,150,  0,360);
		emitter2.setDrawAngleAndChange(0,0,0,0);
		emitter2.setDrawTypeToSprite(3, 1, 40, 40, 0);
//		emitter2.setSpriteSizeChange(.5f, .5f);
		emitter2.setColor(0,    0,1,0, 1);
//		emitter2.setColor(1,    0,0,1, 1);
		
		
		alarm[0] = 0;
		alarm[1] = 0;
		
		
		x = 375;//ref.main.get_screen_width()/2;
		y = ref.main.get_screen_height()/2;
	}

	@Override
	public void sys_step(){
		// Draw the black lines for testing color tweening
		/*
		ref.draw.changeDrawColor(0, 0, 0, 1);
		int life_time = 9000/1000*50;
		int number_of_colors = 5;
		for(int i=0; i<number_of_colors+1; i++){
			ref.draw.drawLine(x+(life_time/number_of_colors*i), y-100, x+(life_time/number_of_colors*i), y+100, 8, 100);
		}
		*/
	}
	
	@Override
	public void alarm0(){
		alarm[1] = 5;
		
		if((ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) || ref.input.get_touch_state(0) == ref.input.TOUCH_HELD){
			emitter2.setXY(ref.input.get_touch_x(0),ref.input.get_touch_y(0));
			emitter2.addParticle(5);
		}
	}
	
	@Override
	public void alarm1(){
		alarm[0] = 3;
		emitter1.addParticle(1);
//		Log.e("reywas", "mnbvfc");
	}
	
}