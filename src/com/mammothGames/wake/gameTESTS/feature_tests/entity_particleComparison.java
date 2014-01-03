package com.mammothGames.wake.gameTESTS.feature_tests;

import android.util.Log;

import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


//public class entity_particle Comparison extends engine_entity {
public class entity_particleComparison extends engine_entity {

	private engine_particleEmitter emitter1;
	private int[] x = new int[500];
	private int[] y = new int[500];
	
	@Override
	public void sys_firstStep(){
		emitter1 = new engine_particleEmitter(ref);
		ref.main.addEntity(emitter1);
		
		emitter1.setXY(0,0);
		emitter1.setLifeTime(100000);
		emitter1.setGravityAndDirection(0 ,270);
		emitter1.setVelocityAndDirection(0,0,  0,0);
		emitter1.setDrawAngleAndChange(0,0,0,0);
		emitter1.setDrawTypeToSprite(game_textures.TEX_ERROR, 1, 50, 50, 0);
//		emitter1.setSpriteSizeChange(.5f, .5f);
		emitter1.setColor(0,    0,0,1, 1);
		
		for(int i=0; i< 500; i++) {
			emitter1.setXY((int) (Math.random() * ref.screen_width),(int) (Math.random() *ref.screen_width));
			x[i] = (int) (Math.random() * ref.screen_width);
			y[i] = (int) (Math.random() *ref.screen_height);
			emitter1.addParticle(1);
		}
		
	}
	
	@Override
	public void sys_step(){
		/*
		ref.draw.changeDrawColor(0, 0, 1, 1);
		for(int i=0; i< 500; i++) {
			ref.draw.drawTexture(x[i], y[i], 50, 50, 0, 0, 0, 0, 1, game_textures.TEX_SPRITES2);
		}
		//*/
	}
}
