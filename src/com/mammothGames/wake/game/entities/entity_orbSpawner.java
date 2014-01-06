package com.mammothGames.wake.game.entities;

import java.util.Random;

import android.os.SystemClock;
import android.util.Log;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_sounds;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_orbSpawner extends engine_entity {
	masterGameReference mgr;
	private boolean isFirstStart;
	public entity_orbSpawner(masterGameReference mgr) {
		
		this.persistent = true;
		this.pausable = true;
		
		this.mgr = mgr;
		isFirstStart = true;
	}
	Random rand = new Random();
	
	utility_pool<poolObj_orb> greenOrb_pool = new utility_pool<poolObj_orb>(ref, poolObj_orb.class, 20);
	poolObj_orb temp_orb;
//	greenOrb_pool = new utility_pool<poolObj_orb>(ref, poolObj_orb.class, 20);
	
	int screen_width, screen_height;
    protected int radius, border_size, radius_total;

    private final int TRAIL_SEGMENTS = 6;
    private float trail_total_height, TRAIL_PERIOD_MS = 333;

	private engine_particleEmitter emitterWhite, emitterColorCircle, emitterWhiteSparks, emitterColorSparks, emitterWhiteShards, emitterExplosion;
	
	public void restart() {
		if (!isFirstStart) {
			emitterWhite.returnAllParticles();
			greenOrb_pool.resetSystem();
		}
	}
	
	@Override
	public void sys_firstStep(){
//		greenOrb_pool = new utility_pool<poolObj_orb>(ref, poolObj_orb.class, 20);

        radius = (ref.screen_width/10);
        border_size = radius/10;
        radius_total = radius + border_size;

		screen_width = ref.screen_width;
		screen_height = ref.screen_height;

        trail_total_height =  radius*3;


		emitterWhite = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterWhite);
		
		emitterWhite.setXY(0,0);
		emitterWhite.setLifeTime(1000);
		emitterWhite.setGravityAndDirection(0 ,0);
		emitterWhite.setVelocityAndDirection(0,0,  0,360);
		emitterWhite.setDrawAngleAndChange(0,360,0,0);
		emitterWhite.setSizeChange(5f, 5f);
		emitterWhite.setColor(0,    1,1,1,1);
		
		emitterExplosion = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterExplosion);
		emitterExplosion.setLifeTime(500);
		emitterExplosion.setSizeChange(7f, 7f);
		
		emitterColorCircle = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterColorCircle);
		emitterColorCircle.setLifeTime(333);
		emitterColorCircle.setGravityAndDirection(0 ,0);
		emitterColorCircle.setVelocityAndDirection(0,0,  0,360);
		emitterColorCircle.setDrawAngleAndChange(0,0,0,0);
		emitterColorCircle.setSizeChange(-2f, -2f);

		
		emitterWhiteSparks = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterWhiteSparks);
		emitterWhiteSparks.setDrawAngleDirectionLock(true);
		emitterWhiteSparks.setLifeTime(1000);
		emitterWhiteSparks.setGravityAndDirection(screen_height/4 ,270);
		emitterWhiteSparks.setDrawAngleAndChange(0,0,0,0);
		emitterWhiteSparks.setSizeChange(-1.5f, -1.5f);
		emitterWhiteSparks.setColor(0,    1,1,1,1);
		
		
		
		emitterColorSparks = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterColorSparks);
		emitterColorSparks.setDrawAngleDirectionLock(true);
		emitterColorSparks.setLifeTime(1000);
		emitterColorSparks.setGravityAndDirection(screen_height/4 ,270);
		emitterColorSparks.setDrawAngleAndChange(0,0,0,0);
		emitterColorSparks.setSizeChange(-1.5f, -1.5f);
		emitterColorSparks.setColor(0,    1,1,1,1);
		
		
		
		emitterWhiteShards = new engine_particleEmitter(ref);
		ref.main.addEntity(emitterWhiteShards);
		emitterWhiteShards.setLifeTime(667);
//		emitterWhiteShards.setDrawAngleDirectionLock(true);
		emitterWhiteShards.setGravityAndDirection(screen_height ,270);
		emitterWhiteShards.setColor(0,    1,1,1,0.9f);
		
		isFirstStart = false;
		
	}
	
	private static float SATURATION=1f, VALUE=0.6f;
	public void spawnOrb(float x, float y, float speed, int radius, int border_size) {
		if (greenOrb_pool != null) {
			temp_orb = (poolObj_orb) greenOrb_pool.takeObject();
			temp_orb.x = x;
			temp_orb.y = y;
			temp_orb.speed = speed;
			temp_orb.radius = radius;
			temp_orb.border_size = border_size;
			
			float randomHue = (float) Math.random();
			temp_orb.r = ref.draw.hsvToRgb(randomHue, SATURATION, VALUE, 0);
			temp_orb.g = ref.draw.hsvToRgb(randomHue, SATURATION, VALUE, 1);
			temp_orb.b = ref.draw.hsvToRgb(randomHue, SATURATION, VALUE, 2);
		}
	}
	
	
	private float tRadius, tExtraV, tHalfTotalV, tBoxY, tShardAngle, tCurrentAngle;
	private float number_shards;
    private float resettingTimeCounter = 0;
	@Override
	public void sys_step(){
		
//		if ((Math.random() * 250) < 5) {
//			greenOrb_pool.takeObject();
//		}

        //TODO: test to see if this crashes!
        resettingTimeCounter += ref.main.time_delta;
        if (resettingTimeCounter > Integer.MAX_VALUE/2) {
            if (resettingTimeCounter%1000 == 0)
                resettingTimeCounter =0;
        }
		
		for(int i_objects = 0; i_objects < greenOrb_pool.MAX_OBJECTS; i_objects++) {
			temp_orb = greenOrb_pool.getInstance(i_objects);
//			temp_greenOrb = (poolObj_orb) greenOrb_pool.objects[i_objects];
			
			boolean delete_this_one = false;
			
			if ((temp_orb.sys_in_use)) {

                float dy = temp_orb.speed * ref.main.time_scale;
				temp_orb.y -= dy;
				
				tRadius = (temp_orb.radius + temp_orb.border_size);
				tExtraV = 7 * ref.main.time_scale * temp_orb.speed;
				tHalfTotalV = ( (2 * tRadius) + tExtraV )/2f;
				tBoxY = (temp_orb.y - tRadius) + tHalfTotalV;
				
				
				for(int i_finger = 0; i_finger<2; i_finger++) {
					if (ref.input.get_touch_state(i_finger) == ref.input.TOUCH_DOWN) {
						if (ref.collision.point_AABB(tRadius*2, tRadius*2 + tExtraV, temp_orb.x, tBoxY,  ref.input.get_touch_x(i_finger), ref.input.get_touch_y(i_finger))) {
							
							// Tap'd orb
							delete_this_one = true;
							
							mgr.gameMain.score += 1;
							mgr.gameMain.floor_height -= mgr.gameMain.floor_per_hit;
							
							number_shards = (int) (rand.nextFloat() * 3) + 6;
							tShardAngle = 360f / number_shards;
							emitterWhiteShards.setXY((int)(temp_orb.x),(int)(temp_orb.y));
							emitterWhiteShards.setDrawTypeToCircle(temp_orb.radius, tShardAngle, game_constants.layer2_underGame);
							for(int ii=0; ii<number_shards; ii++) {
								tCurrentAngle = (tShardAngle * ii);
								emitterWhiteShards.setVelocityAndDirection(temp_orb.speed/5,temp_orb.speed/5,  tCurrentAngle, tCurrentAngle);
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
				ref.draw.drawCircle(temp_orb.x, temp_orb.y, temp_orb.radius + temp_orb.border_size, 0, 0, 360, 0, game_constants.layer3_game);
				
				
				//Green interior
				ref.draw.setDrawColor(temp_orb.r, temp_orb.g, temp_orb.b, 1);
				ref.draw.drawCircle(temp_orb.x, temp_orb.y, temp_orb.radius, 0, 0, 360, 0, game_constants.layer3_game);

                // White trail
                float old_x = temp_orb.x + temp_orb.radius + temp_orb.border_size/2;
                float old_x_reverse = temp_orb.x - temp_orb.radius - temp_orb.border_size/2;
                float old_y = temp_orb.y;
                float degradation = 1;
                float degradationFactor = 1f/TRAIL_SEGMENTS;


                for(int i=0; i<TRAIL_SEGMENTS; i++) {

                	
                    trail_total_height = temp_orb.speed/5;

                    float new_x;
                    float new_x_reverse;
	            	new_x = degradation*temp_orb.radius;
	            	new_x_reverse = temp_orb.x -new_x;
	            	new_x += temp_orb.x;
                    
                    float new_y = old_y + degradation*trail_total_height/TRAIL_SEGMENTS;

//                    ref.draw.setDrawColor((float)Math.random(),(float)Math.random(),(float)Math.random(),1);
                    ref.draw.setDrawColor(1, 1, 1, degradation/2f);
                    ref.draw.drawLine(old_x, old_y, new_x, new_y ,temp_orb.border_size*2, 1);
                    ref.draw.drawLine(old_x_reverse, old_y, new_x_reverse, new_y ,temp_orb.border_size*2, game_constants.layer2_underGame);
                    
                    old_x = new_x;
                    old_x_reverse = new_x_reverse;
                    old_y = new_y;
                    degradation-=degradationFactor;
                }
				

				if (temp_orb.y < -temp_orb.radius - temp_orb.border_size + mgr.gameMain.floor_height) {
					
					// particle boxes shooting up
					emitterWhiteSparks.setVelocityAndDirection(temp_orb.speed/1.5f,temp_orb.speed,  120,60);
					emitterWhiteSparks.setXY((int)(temp_orb.x),(int)(temp_orb.y + tRadius));
					emitterWhiteSparks.setDrawTypeToRectangle(temp_orb.radius/2, temp_orb.radius/2, game_constants.layer2_underGame);
					emitterWhiteSparks.addParticle(10);
					
					emitterColorSparks.setColor(0,    temp_orb.r, temp_orb.g, temp_orb.b, 1);
					emitterColorSparks.setVelocityAndDirection(temp_orb.speed/1.5f,temp_orb.speed,  120,60);
					emitterColorSparks.setXY((int)(temp_orb.x),(int)(temp_orb.y + tRadius));
					emitterColorSparks.setDrawTypeToRectangle(temp_orb.radius/2, temp_orb.radius/2, game_constants.layer2_underGame);
					emitterColorSparks.addParticle(7);
					
					emitterExplosion.setColor(0,    temp_orb.r, temp_orb.g, temp_orb.b, 0.8f);
					emitterExplosion.setXY((int)(temp_orb.x),(int)(temp_orb.y));
					emitterExplosion.setDrawTypeToCircle(temp_orb.radius, 360, game_constants.layer4_overGame);
					emitterExplosion.addParticle(1);
					
					ref.sound.playSoundSpeedChanged(game_sounds.SND_SPLASH, 0.85f);
					
					mgr.gameMain.floor_height += mgr.gameMain.floor_per_miss;
					
					delete_this_one = true;
				}
				if (delete_this_one) {
//					ref.draw.setDrawColor(1, 1, 1, 1);
//					ref.draw.drawCircle(temp_greenOrb.x, temp_greenOrb.y, temp_greenOrb.radius*3/2, 0, 0, 360, 0, 0);
					
					emitterWhite.setXY((int)(temp_orb.x),(int)(temp_orb.y));
					emitterWhite.setDrawTypeToSprite(game_textures.TEX_SPRITES, game_textures.SUB_PARTICLE, temp_orb.radius*2, temp_orb.radius*2, game_constants.layer2_underGame);
					emitterWhite.addParticle(1);
					
					emitterColorCircle.setXY((int)(temp_orb.x),(int)(temp_orb.y));
					emitterColorCircle.setColor(0,    temp_orb.r, temp_orb.g, temp_orb.b, 1);
					emitterColorCircle.setDrawTypeToCircle(temp_orb.radius, 360, game_constants.layer3_game);
//					emitterGreen.setDrawTypeToSprite(game_textures.TEX_PARTICLE, 1, temp_greenOrb.radius*2, temp_greenOrb.radius*2, 2);
					emitterColorCircle.addParticle(1);
					
					
//					ref.sound.playSound(game_sounds.SND_OINK);
					
					greenOrb_pool.returnObject(temp_orb.sys_id);
					
				}
				
			}
		}
	}
}