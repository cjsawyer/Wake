package com.mammothGames.wake.gameEngine;

public class engine_particle {

	float life_remaining, gravity, draw_angle, image_angle_change, size_x, size_y, change_x, change_y, x, y, x_p, y_p, dxv, dyv, arc_angle;
	float[] r,g,b,a;
	boolean in_use;
	int id, my_emitter_index;
	
	public engine_particle(){
		reset();
		r = new float[9];
		g = new float[9];
		b = new float[9];
		a = new float[9];
	}
	
	public void reset() {
		life_remaining = 0;
		gravity = 0;
		draw_angle = 0;
		image_angle_change = 0;
		size_x = 0;
		size_y = 0;
		change_x = 0;
		change_y = 0;
		x = 0;
		y = 0;
		x_p = 0;
		y_p = 0;
		dxv = 0;
		dyv = 0;
		arc_angle = 0;
		
		in_use = false;
	}
}
