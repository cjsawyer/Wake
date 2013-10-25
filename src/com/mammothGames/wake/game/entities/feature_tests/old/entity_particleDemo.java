package com.mammothGames.wake.game.entities.feature_tests.old;

import java.util.Random;

import com.mammothGames.wake.gameEngine.*;


public class entity_particleDemo extends engine_entity {

	
	private engine_particleEmitter emitter1, emitter2, emitter3;
	private float x, y;
	
	@Override
	public void sys_firstStep(){
		
		
		//fire!
		emitter1 = new engine_particleEmitter(ref);
		ref.main.addEntity(emitter1);
		
		emitter1.setXY(ref.main.get_screen_width()/2,ref.main.get_screen_height()/3);
		emitter1.setLifeTime(4000);
		emitter1.setGravityAndDirection(0 ,270);///////////
		emitter1.setVelocityAndDirection(30,50,  80,100);//////direction
		emitter1.setDrawAngleAndChange(0,360,-400, 400);//(float)(Math.random()*220f)
		emitter1.setDrawTypeToSprite(3, 1, 75, 75, 0);
		emitter1.setSizeChange(-.5f, -.5f);
		emitter1.setColor(0,    1,.4f,0, 1);
		emitter1.setColor(1,    1,0,0, .5f);
		emitter1.setColor(1,    .5f,.5f,.5f, 0);
		
		
		//white fire!
		emitter2 = new engine_particleEmitter(ref);
		ref.main.addEntity(emitter2);
		
		emitter2.setXY(ref.main.get_screen_width()/2,ref.main.get_screen_height()/3-20);
		emitter2.setLifeTime(2000);
		emitter2.setGravityAndDirection(0 ,270);///////////
		emitter2.setVelocityAndDirection(30,50,  80,100);//////direction
		emitter2.setDrawAngleAndChange(0, 360, -300, 300);//(float)(Math.random()*220f)
		emitter2.setDrawTypeToSprite(3, 1, 30, 30, 1);
		emitter2.setSizeChange(-.45f, -.45f);
		
		emitter2.setColor(0,    1,1,1, 1);
		
		//red fire!
		emitter3= new engine_particleEmitter(ref);
		ref.main.addEntity(emitter3);
		
		emitter3.setXY(ref.main.get_screen_width()/2,ref.main.get_screen_height()/3-20);
		emitter3.setLifeTime(2000);
		emitter3.setGravityAndDirection(0 ,270);///////////
		emitter3.setVelocityAndDirection(30,50,  80,100);//////direction
		emitter3.setDrawAngleAndChange(0, 360, -300, 300);//(float)(Math.random()*220f)
		emitter3.setDrawTypeToSprite(3, 1, 50, 50, 1);
		emitter3.setSizeChange(-.45f, -.45f);
		
		emitter3.setColor(1,    1,0,0, 1);
		

		
		
		alarm[0] = 0;
		alarm[1] = 0;
		
		
		x = 375;//ref.main.get_screen_width()/2;
		y = ref.main.get_screen_height()/2;
	}

	@Override
	public void sys_step(){
	}
	
	@Override
	public void alarm0(){
		alarm[0] = Math.random() * 60;
		emitter1.addParticle(1);
	}
	@Override
	public void alarm1(){
		alarm[1] = Math.random() * 90;
		emitter2.addParticle(1);
		emitter3.addParticle(1);
	}
	
}