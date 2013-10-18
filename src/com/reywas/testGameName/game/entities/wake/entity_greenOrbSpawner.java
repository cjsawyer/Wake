package com.reywas.testGameName.game.entities.wake;

import java.util.Random;

import android.util.Log;

import com.reywas.testGameName.game.game_sounds;
import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.game.entities.feature_tests.test_pool_object;
import com.reywas.testGameName.gameEngine.*;


public class entity_greenOrbSpawner extends engine_entity {
	masterGameReference mgr;
	private boolean isFirstStart;
	public entity_greenOrbSpawner(masterGameReference mgr) {
		
		this.persistent = true;
		this.pausable = true;
		
		this.mgr = mgr;
		isFirstStart = true;
	}
	Random rand = new Random();
	
	utility_pool<poolObj_greenOrb> greenOrb_pool = new utility_pool<poolObj_greenOrb>(ref, poolObj_greenOrb.class, 20);
	poolObj_greenOrb temp_greenOrb;
//	greenOrb_pool = new utility_pool<poolObj_greenOrb>(ref, poolObj_greenOrb.class, 20);
	
	int screen_width, screen_height;
	
	private engine_particleEmitter emitterWhite, emitterGreen, emitterWhiteSparks, emitterGreenSparks, emitterWhiteShards;
	
	public void restart() {
		if (!isFirstStart) {
			emitterWhite.returnAllParticles();
			greenOrb_pool.resetSystem();
		}
	}
	
	@Override
	public void sys_firstStep(){
//		greenOrb_pool = new utility_pool<poolObj_greenOrb>(ref, poolObj_greenOrb.class, 20);
		
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
		
		
		
		emitterWhite = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterWhite);
		
		emitterWhite.setXY(0,0);
		emitterWhite.setLifeTime(1000);
		emitterWhite.setGravityAndDirection(0 ,0);
		emitterWhite.setVelocityAndDirection(0,0,  0,360);
		emitterWhite.setDrawAngleAndChange(0,360,0,0);
		emitterWhite.setSizeChange(5f, 5f);
		emitterWhite.setColor(0,    1,1,1,1);
		
		
		
		emitterGreen = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterGreen);
		
		emitterGreen.setLifeTime(333);
		emitterGreen.setGravityAndDirection(0 ,0);
		emitterGreen.setVelocityAndDirection(0,0,  0,360);
		emitterGreen.setDrawAngleAndChange(0,0,0,0);
		emitterGreen.setSizeChange(-2f, -2f);
		emitterGreen.setColor(0,    0,1,0,1);
		
		
		
		emitterWhiteSparks = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterWhiteSparks);
		
		emitterWhiteSparks.setDrawAngleDirectionLock(true);
		emitterWhiteSparks.setLifeTime(1000);
		emitterWhiteSparks.setGravityAndDirection(screen_height/4 ,270);
		emitterWhiteSparks.setDrawAngleAndChange(0,0,0,0);
		emitterWhiteSparks.setSizeChange(-1.5f, -1.5f);
		emitterWhiteSparks.setColor(0,    1,1,1,1);
		
		
		
		emitterGreenSparks = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterGreenSparks);
		
		emitterGreenSparks.setDrawAngleDirectionLock(true);
		emitterGreenSparks.setLifeTime(1000);
		emitterGreenSparks.setGravityAndDirection(screen_height/4 ,270);
		emitterGreenSparks.setDrawAngleAndChange(0,0,0,0);
		emitterGreenSparks.setSizeChange(-1.5f, -1.5f);
		emitterGreenSparks.setColor(0,    0,1,0,1);
		
		
		
		emitterWhiteShards = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterWhiteShards);
		
		emitterWhiteShards.setLifeTime(667);
//		emitterWhiteShards.setDrawAngleDirectionLock(true);
		emitterWhiteShards.setGravityAndDirection(screen_height ,270);
		emitterWhiteShards.setColor(0,    1,1,1,0.9f);
		
		isFirstStart = false;
		
	}
	
	
//	public void spawnGreenOrb(float x, float y) {
	public void spawnGreenOrb(float x, float y, float speed, int radius, int border_size) {
		if (greenOrb_pool != null) {
			temp_greenOrb = (poolObj_greenOrb) greenOrb_pool.takeObject();
			temp_greenOrb.x = x;
			temp_greenOrb.y = y;
			temp_greenOrb.speed = speed;
			temp_greenOrb.radius = radius;
			temp_greenOrb.border_size = border_size;
		}
	}
	
	
	private float tRadius, tExtraV, tHalfTotalV, tBoxY, tShardAngle, tCurrentAngle;
	private int number_shards;
	@Override
	public void sys_step(){
		
//		if ((Math.random() * 250) < 5) {
//			greenOrb_pool.takeObject();
//		}
		
		for(int i_objects = 0; i_objects < greenOrb_pool.MAX_OBJECTS; i_objects++) {
			temp_greenOrb = greenOrb_pool.getInstance(i_objects);
//			temp_greenOrb = (poolObj_greenOrb) greenOrb_pool.objects[i_objects];
			
			boolean delete_this_one = false;
			
			if ((temp_greenOrb.sys_in_use)) {
				
				temp_greenOrb.y -= temp_greenOrb.speed * ref.main.time_scale;
				
				tRadius = (temp_greenOrb.radius + temp_greenOrb.border_size);
				tExtraV = 7 * ref.main.time_scale * temp_greenOrb.speed;
				tHalfTotalV = ( (2 * tRadius) + tExtraV )/2f;
				tBoxY = (temp_greenOrb.y - tRadius) + tHalfTotalV;
				
				
				for(int i_finger = 0; i_finger<2; i_finger++) {
					if (ref.input.get_touch_state(i_finger) == ref.input.TOUCH_DOWN) {
						if (ref.collision.point_AABB(tRadius*2, tRadius*2 + tExtraV, temp_greenOrb.x, tBoxY,  ref.input.get_touch_x(i_finger), ref.input.get_touch_y(i_finger))) {
							
							// Tap'd orb
							delete_this_one = true;
							
							mgr.gameMain.score += 1;
							mgr.gameMain.floor_height -= mgr.gameMain.floor_per_hit;
							
							number_shards = (int) (rand.nextFloat() * 3) + 6;
							tShardAngle = 360f / number_shards;
							emitterWhiteShards.setXY((int)(temp_greenOrb.x),(int)(temp_greenOrb.y));
							emitterWhiteShards.setDrawTypeToCircle(temp_greenOrb.radius, tShardAngle, 1);
							for(int ii=0; ii<number_shards; ii++) {
								tCurrentAngle = (tShardAngle * ii);
								emitterWhiteShards.setVelocityAndDirection(temp_greenOrb.speed/5,temp_greenOrb.speed/5,  tCurrentAngle, tCurrentAngle);
								emitterWhiteShards.setDrawAngleAndChange(tCurrentAngle - (tShardAngle)/2f, tCurrentAngle - (tShardAngle)/2f, -180, 180);
								emitterWhiteShards.addParticle(1);
							}
							
							ref.sound.playSoundSpeedChanged(game_sounds.SND_DING, 0.3f);
							
							mgr.gameMain.speed_multiplier += mgr.gameMain.speed_gain_per_orb;
							mgr.gameMain.time_between_orbs -= mgr.gameMain.time_change_per_orb;
						}
					}
				}
				//ref.collision.point_AABB(tRadius*2, tRadius*2 + tExtraV, temp_greenOrb.x, tBoxY, -1,-1);
				
				
				
				// White outline
				ref.draw.setDrawColor(1, 1, 1, 1);
				ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius + temp_greenOrb.border_size, 0, 0, 360, 0, 0);
				
				//Green interior
				ref.draw.setDrawColor(0, 1, 0, 1);
				ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius, 0, 0, 360, 0, 2);
				
				
				
				/*
				// white spazzyness
				ref.draw.setDrawColor(1, 1, 1, 0.8f);
				ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius + temp_greenOrb.border_size, 0, 0, rand.nextFloat() * 270, rand.nextFloat() * 360, 6);
				
				// black spazzyness
				ref.draw.setDrawColor(0, 0, 0, 1);
				ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius + temp_greenOrb.border_size, 0, 0, rand.nextFloat() * 45, rand.nextFloat() * 360, 6);
				ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius + temp_greenOrb.border_size, 0, 0, rand.nextFloat() * 45, rand.nextFloat() * 360, 6);
				*/
				
				if (temp_greenOrb.y < -temp_greenOrb.radius - temp_greenOrb.border_size + mgr.gameMain.floor_height) {
					
					// particle boxes shooting up
					emitterWhiteSparks.setVelocityAndDirection(temp_greenOrb.speed/1.5f,temp_greenOrb.speed,  120,60);
					emitterWhiteSparks.setXY((int)(temp_greenOrb.x),(int)(temp_greenOrb.y + tRadius));
					emitterWhiteSparks.setDrawTypeToRectangle(temp_greenOrb.radius/2, temp_greenOrb.radius/2, 1);
					emitterWhiteSparks.addParticle(10);
					
					emitterGreenSparks.setVelocityAndDirection(temp_greenOrb.speed/1.5f,temp_greenOrb.speed,  120,60);
					emitterGreenSparks.setXY((int)(temp_greenOrb.x),(int)(temp_greenOrb.y + tRadius));
					emitterGreenSparks.setDrawTypeToRectangle(temp_greenOrb.radius/2, temp_greenOrb.radius/2, 1);
					emitterGreenSparks.addParticle(7);
					
					
					ref.sound.playSoundSpeedChanged(game_sounds.SND_SPLASH, 0.85f);
//					Log.e("reywas", "HIT THE BOTTOM " + temp_greenOrb.sys_id);
					
					mgr.gameMain.floor_height += mgr.gameMain.floor_per_miss;
					
					delete_this_one = true;
				}
				if (delete_this_one) {
//					ref.draw.setDrawColor(1, 1, 1, 1);
//					ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius*3/2, 0, 0, 360, 0, 0);
					
					emitterWhite.setXY((int)(temp_greenOrb.x),(int)(temp_greenOrb.y));
					emitterWhite.setDrawTypeToSprite(game_textures.TEX_SPRITES, game_textures.SUB_PARTICLE, temp_greenOrb.radius*2, temp_greenOrb.radius*2, 0);
					emitterWhite.addParticle(1);
					
					emitterGreen.setXY((int)(temp_greenOrb.x),(int)(temp_greenOrb.y));
					emitterGreen.setDrawTypeToCircle(temp_greenOrb.radius, 360, 2);
//					emitterGreen.setDrawTypeToSprite(game_textures.TEX_PARTICLE, 1, temp_greenOrb.radius*2, temp_greenOrb.radius*2, 2);
					emitterGreen.addParticle(1);
					
					
//					ref.sound.playSound(game_sounds.SND_OINK);
					
					greenOrb_pool.returnObject(temp_greenOrb.sys_id);
					
				}
				
			}
		}
	}
}
