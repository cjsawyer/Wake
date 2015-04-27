package com.mammothGames.wake1.gameEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.util.FloatMath;
import android.util.Log;

public class engine_particleEmitter extends engine_entity {
	
	private engine_reference ref;
	public engine_particleEmitter(engine_reference ref) {
		this.ref = ref;
		this.persistent = true;
		emitter_index= ref.partPool.emitter_indices++;
	
		random = new Random();
		
		r = new float[9];
		g = new float[9];
		b = new float[9];
		a = new float[9];
	}

	
	int emitter_index;
	
	// When these are set, all partials are made with the same characteristics
	private float   x,y,   life_time,   velocity_low, velocity_high, direction_low, direction_high,   gravity, acceleration, gravity_direction,   image_angle_low, image_angle_high, image_angle_change_low, image_angle_change_high, size_x, size_y, change_x, change_y,   dxv, dyv, dxg, dyg, arc_angle;
	private int draw_type, sprite_sheet, sprite_index, draw_depth, number_of_colors;
	private float[] r,g,b,a; 
	private boolean draw_angle_is_direction = false;
	private final int DRAW_SPRITE = 0, DRAW_CIRCLE = 1, DRAW_RECTANGLE = 2;
	
	private Random random;
	
	private int temp_i = 0;
	private float temp_r, temp_g, temp_b, temp_a, temp_life_remaining, temp_life_delta, temp_gravity, temp_scale, time_scale, temp_velocity, temp_direction, temp_x, temp_y;
	private engine_particle temp_particle; 
	
	public void returnAllParticles() {
		for (temp_i=0; temp_i< ref.partPool.MAX_PARTICLES; temp_i++) {
//			temp_particle = ;
			ref.partPool.returnParticle(ref.partPool.particles[temp_i].id);
		}
	}
	
	private void copyColorArrays(float[] from, float[] to) {
		for (int i=0; i<number_of_colors;i++)
			to[i]=from[i];
	}
	public void addParticle(int number){

		for (temp_i=0; temp_i<number; temp_i++) {
			
//			temp_particle = new engine_particle();
//			temp_particle = ref.partPool.takeParticle();
			ref.partPool.takeParticle();
			temp_particle = ref.partPool.temp_particle;
			
			temp_particle.my_emitter_index = emitter_index;
			
			temp_particle.x = x;
			temp_particle.y = y;
			temp_particle.life_remaining = life_time;
			temp_particle.gravity = 0;
			temp_particle.draw_angle = randomRange(image_angle_low, image_angle_high);
			temp_particle.image_angle_change = randomRange(image_angle_change_low,image_angle_change_high);
			temp_particle.size_x = size_x;
			temp_particle.size_y = size_y;
			temp_particle.change_x = change_x;
			temp_particle.change_y = change_y;
			
			copyColorArrays(r,temp_particle.r);
			copyColorArrays(b,temp_particle.b);
			copyColorArrays(g,temp_particle.g);
			copyColorArrays(a,temp_particle.a);
			
			temp_velocity = randomRange(velocity_low, velocity_high);
			temp_direction = DEG_TO_RAD * randomRange(direction_low, direction_high);
			
			// Computes the delta for the x/y components for velocity, in pixels.
			temp_particle.dxv = temp_velocity * FloatMath.cos( temp_direction );
			temp_particle.dyv = temp_velocity * FloatMath.sin( temp_direction );
			
			temp_particle.arc_angle = arc_angle;
			
		}
	}
	public float randomRange(float min, float max){
		return ( random.nextFloat() * (max-min) ) + min;
	}
	
	private int temp_color_index = 0;
	private final float DEG_TO_RAD = 3.1415926536f/180f;
	private final float RAD_TO_DEG = 180f/3.1415926536f;
	
	private void updateAllParticles(){
		
//		if (!ref.main.e) {
		
			for(temp_i = 0; temp_i < ref.partPool.MAX_PARTICLES; temp_i++) {
				
				temp_particle = ref.partPool.particles[temp_i];
				if ((temp_particle.in_use) && (temp_particle.my_emitter_index==emitter_index) ) {
					
					// update life			
					temp_life_remaining = temp_particle.life_remaining;
					temp_life_remaining -= ref.main.time_delta;
					temp_particle.life_remaining = temp_life_remaining;
					
					
					// update position with velocity
					temp_x = (temp_particle.x + ((temp_particle.dxv * time_scale)) );
					temp_y = (temp_particle.y + ((temp_particle.dyv * time_scale)) );
		
					
					//recalculate/update gravity, then add to position
						//acceleration = ((Math.sqrt(2*speed))*dt);
						//y -= Math.round((acceleration*(Math.sin(degToRad*direction))));
					//temp_gravity = ( temp_particle.gravity + ( gravity * time_scale ));
	//				if (temp_particle.gravity != 0) {
						temp_gravity = ( temp_particle.gravity + ( gravity * time_scale ) );
						temp_particle.gravity = temp_gravity;
					
						temp_x += temp_gravity * FloatMath.cos(DEG_TO_RAD * gravity_direction) * time_scale;
						temp_y += temp_gravity * FloatMath.sin(DEG_TO_RAD * gravity_direction) * time_scale;
	//				}
					
					temp_particle.x_p = temp_particle.x;
					temp_particle.y_p = temp_particle.y;
						
					temp_particle.x = temp_x;
					temp_particle.y = temp_y;
					
					
					temp_life_delta = life_time - temp_life_remaining;
					temp_color_index = (int)  (temp_life_delta / life_time * number_of_colors);
					if (temp_color_index == number_of_colors+1){
						temp_color_index = number_of_colors;
					}
					
					
					//update image angle
					temp_particle.draw_angle += temp_particle.image_angle_change * time_scale;
	
					
					//update size
					temp_particle.size_x += temp_particle.change_x * size_x * time_scale;
					temp_particle.size_y += temp_particle.change_y * size_y * time_scale;
					
					
					if (draw_angle_is_direction) {
						temp_particle.draw_angle = (float) ( RAD_TO_DEG * Math.atan2(temp_particle.y - temp_particle.y_p, temp_particle.x - temp_particle.x_p) );
					}
					
					// update color
					temp_r = interpolate(temp_particle.r);
					temp_g = interpolate(temp_particle.g);
					temp_b = interpolate(temp_particle.b);
					temp_a = interpolate(temp_particle.a);
					
					// draw particle
					ref.draw.setDrawColor(temp_r, temp_g, temp_b, temp_a);
					switch(draw_type) {
						case DRAW_SPRITE: {
							ref.draw.drawTexture(temp_x, temp_y, temp_particle.size_x, temp_particle.size_y, 0, 0, temp_particle.draw_angle, draw_depth, sprite_index, sprite_sheet);
							break;
						}
						case DRAW_CIRCLE: {
							ref.draw.drawCircle(temp_x, temp_y, temp_particle.size_x/2, 0, 0, temp_particle.arc_angle, temp_particle.draw_angle, draw_depth);
							break;
						}
						case DRAW_RECTANGLE: {
							ref.draw.drawRectangle(temp_x, temp_y, temp_particle.size_x, temp_particle.size_y, 0, 0, temp_particle.draw_angle, draw_depth);
							break;
						}
					}
				}
				
//			}
			
		}
		
		
		// remove dead particles
////		particle_iterator = particles.iterator(); 
//		while (particle_iterator.hasNext()){
//	        if (particle_iterator.next().life_remaining <= 0){
//	        	particle_iterator.remove();
//	        }
//		}
		
		/*
		for (engine_particle temp_particle : particles) {
			particle_iterator = particles.iterator();
			//temp_life_remaining = temp_particle.life_remaining;
			
			if ((temp_particle.life_remaining) <= 0){
				ref.partPool.returnParticle(temp_particle.id);
				particle_iterator.remove();
				temp_numRemoved += 1;
				ref.main.particles_current--;
			}
		}
		*/
		
		//*
		for(temp_i = 0; temp_i < ref.partPool.MAX_PARTICLES; temp_i++) {
			// To avoid continuously re-checking the last element if particle/s were removed, so as not to loop to the last remaining particle over and over.  
				
				temp_particle = ref.partPool.particles[temp_i];
				
				if ( (temp_particle.in_use) && (temp_particle.life_remaining <= 0) ){
					ref.partPool.returnParticle(temp_particle.id);
				}
		}
		//*/
		
		
	}
	
	private float interpolate(float[] array) {
		if (number_of_colors > 1) {
			//if more than one color, interpolate.
			temp_scale = (life_time)/(number_of_colors);
			return (float) (
					(
							array[temp_color_index+1]-array[temp_color_index]
					)
					*
					(
							( 
								temp_life_delta  - temp_scale*(temp_color_index)
							)
							/
							(
								temp_scale
							)	
					)
					+
					array[temp_color_index]
				);
		} else {
			//if only one color, just return that.
			//return array[temp_color_index];
			
			temp_scale = (life_time)/(number_of_colors);
			return (float) (
					(
							-array[temp_color_index]
					)
					*
					(
							( 
								temp_life_delta  - temp_scale*(temp_color_index)
							)
							/
							(
								temp_scale
							)	
					)
					+
					array[temp_color_index]
				);
			
			
		}
	}
	
	
	//TODO: be more consistent in language, 'changes' is for things that can be changed per-particle, 'sets' is for things that effect all particles.
	/**
	 * Changes the color of the particles, and transitions between them if more than one color index added. (0 first, 1 second, 2 third, etc.)
	 * Fades to transparent after the last added index.
	 * @param color_index
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setColor(int color_index, float r, float g, float b, float a){
		
		
		this.r[color_index] = r;
		this.g[color_index] = g;
		this.b[color_index] = b;
		this.a[color_index] = a;
		
		this.r[color_index+1] = r;
		this.g[color_index+1] = g;
		this.b[color_index+1] = b;
		this.a[color_index+1] = 0; //-1
		
		number_of_colors = color_index+1;
	}
	
	/**
	 * Changes the life span of the particles, in ms.
	 * @param life_time
	 */
	public void setLifeTime(float life_time){
		this.life_time = life_time;
	}
	
	/**
	 * Changes the start position of the particles.
	 * @param x
	 * @param y
	 */
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the amount of gravity acting on the particles and the direction in which it acts.
	 * @param gravity
	 * @param direction
	 */
	public void setGravityAndDirection(float gravity, float direction) {
		this.gravity = gravity;
		this.gravity_direction = direction;
	}
	
	/**
	 * Sets the velocity with speed (in screen px) somewhere in the given range, and a direction in the given range.
	 * @param velocity_low
	 * @param velocity_high
	 * @param direction_low
	 * @param direction_high
	 */
	public void setVelocityAndDirection(float velocity_low, float velocity_high, float direction_low, float direction_high) {
		this.velocity_low = velocity_low;
		this.velocity_high = velocity_high;
		this.direction_low = direction_low;
		this.direction_high = direction_high;
	}
	
	/**
	 * Sets the initial angle to draw the sprite, and the change per second. (in degrees)
	 * @param angle
	 * @param change_per_second
	 */
	public void setDrawAngleAndChange(float angle_low, float angle_high, float change_per_second_low, float change_per_second_high){
		image_angle_low = angle_low;
		image_angle_high = angle_high;
		image_angle_change_low = change_per_second_low;
		image_angle_change_high = change_per_second_high;
	}
	
	/**
	 * Sets all particles to draw as this sprite.
	 * @param sprite_sheet
	 * @param sprite_index
	 * @param size_x
	 * @param size_y
	 * @param draw_depth
	 */
	public void setDrawTypeToSprite(int sprite_sheet, int sprite_index, float size_x, float size_y, int draw_depth){
		
		this.draw_type = DRAW_SPRITE;
		
		this.sprite_sheet = sprite_sheet;
		this.sprite_index = sprite_index;
		this.size_x = size_x;
		this.size_y = size_y;
		this.draw_depth = draw_depth;
		
	}
	
	/**
	 * Sets all particles to draw as circles.
	 * @param sprite_sheet
	 * @param sprite_index
	 * @param size_x
	 * @param size_y
	 * @param draw_depth
	 */
	public void setDrawTypeToCircle(float radius, float arc_angle, int draw_depth){
		
		this.draw_type = DRAW_CIRCLE;
		
		this.size_x = radius*2;
		this.arc_angle = arc_angle;
		this.draw_depth = draw_depth;
		
	}
	
	/**
	 * Sets all particles to draw as circles.
	 * @param sprite_sheet
	 * @param sprite_index
	 * @param size_x
	 * @param size_y
	 * @param draw_depth
	 */
	public void setDrawTypeToRectangle(float size_x, float size_y, int draw_depth){
		
		this.draw_type = DRAW_RECTANGLE;
		
		this.size_x = size_x;
		this.size_y = size_y;
		this.draw_depth = draw_depth;
		
	}
	
	/**
	 * Multiplies the size of all particles by change_x and change_y per second.
	 * @param change_x
	 * @param change_y
	 */
	public void setSizeChange(float change_x, float change_y){
		this.change_x = change_x; 
		this.change_y = change_y;
	}
	
	/**
	 * If set to true, locks the draw angle to the current direction.
	 * @param trueFalse
	 */
	public void setDrawAngleDirectionLock(boolean trueFalse) {
		draw_angle_is_direction = true;
	}
	
	
	/**
	 * Internal method to update the particles.
	 */
	@Override
	public void sys_step(){
		time_scale = ref.main.time_scale;
		updateAllParticles();
	}
	
	
	
	
	
}
