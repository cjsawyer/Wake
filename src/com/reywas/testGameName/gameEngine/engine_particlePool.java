package com.reywas.testGameName.gameEngine;

import java.util.Random;

import com.reywas.testGameName.game.game_constants;

import android.util.Log;

public class engine_particlePool {
	
	private engine_reference ref;
	
	protected engine_particle particles[];
	protected final int MAX_PARTICLES = game_constants.max_particles;//200;
	private int ids = 0;
	
	protected engine_particle temp_particle;
	private int temp_i;
	
	public int particles_current = 0;
	
	public int emitter_indices = 0;
	
	private Random random;
	
	public engine_particlePool(engine_reference r){
		ref = r;
		
		random = new Random();
		
		particles = new engine_particle[MAX_PARTICLES];
		//maybe -1 from 2nd bit
		for(temp_i=0;temp_i<MAX_PARTICLES;temp_i++) {
			addParticle();
		}
	}
	
	private void addParticle() {
		temp_particle = new engine_particle();
		
		temp_particle.id = ids++;
		particles[ids-1] = temp_particle;
		
//		particles_current++;
	}
	
	
	public engine_particle takeParticle() {
		for(temp_i=0;temp_i<MAX_PARTICLES;temp_i++) {
			
			temp_particle = particles[temp_i];
			
			if (!temp_particle.in_use) {
				temp_particle.reset();
				temp_particle.in_use = true;
				
				particles_current++;

				return temp_particle;
			}
		}
		
		//We got through the whole array without finding an open space, so we can't return anything logical. So we just return the last one in the list.
		//It's probably so old that it won't be noticed.
		temp_particle = particles[MAX_PARTICLES-1];
		temp_particle.reset();
		temp_particle.in_use = true;
		return temp_particle;
	}
	
	public void returnParticle(int id) {
		for(temp_i=0;temp_i<MAX_PARTICLES;temp_i++) {
			temp_particle = particles[temp_i];
			if(temp_particle.id == id) {
				temp_particle.in_use = false;
				particles_current--;
			}
		}
	}
	
	/**
	 * Use when moving rooms to reset system
	 */
	protected void sys_clearParticleSystem() {
		
//		emitter_indices = 0;
		
		for(temp_i=0;temp_i<MAX_PARTICLES;temp_i++) {
			particles[temp_i].in_use=false;
		}
		particles_current = 0;
	}

//	

	

	private int temp_total_width = 200;
	private int temp_total_height = 50;
	private int temp_half_total_width = (int)( temp_total_width/2f );
	private int temp_point_width =  (int) ( (float)temp_total_width/MAX_PARTICLES );
	
	protected void sys_drawDebugGraph() {
//		Log.e("reywas", "" + temp_total_width + " " + temp_half_total_width + " " + temp_point_width);

		ref.draw.setDrawColor(.5f, .5f, .5f, 1);
		ref.draw.drawRectangle(0, 0, temp_total_width, 50, temp_half_total_width, 25, 0, 0);
		
		for(temp_i=0; temp_i<MAX_PARTICLES; temp_i++) {
				if(particles[temp_i].in_use){
					ref.draw.setDrawColor(1, 0, 0, 1);
					ref.draw.drawRectangle(temp_point_width*temp_i, 0, temp_point_width, temp_total_height, temp_point_width/2f, temp_total_height/2, 0, 0);
				}
				
		}
	}


		
		
		
}
