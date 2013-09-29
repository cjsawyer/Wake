package com.reywas.testGameName.game.entities.feature_tests.old;

import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.gameEngine.*;

import android.util.FloatMath;



public class entity_OBAMA extends engine_entity {

	float angle = 0;
	float timer_time = 222;//222;
	float timer = timer_time;
	float x,y;
	entity_lilOBAMA a,b,c,d;
	
	@Override
	public void sys_firstStep(){}
	
	@Override
	public void sys_step(){
		angle += 60f * (ref.main.time_delta/1000f);
		
		timer -= ref.main.time_delta;
		if (timer <= 0) {
			timer = timer_time;
				
			a = new entity_lilOBAMA();
			//b = new entity_lilOBAMA();
			c = new entity_lilOBAMA();
			//d = new entity_lilOBAMA();
			
			ref.main.addEntity(a);
			a.x = x;
			a.y = y;
			//ref.main.addEntity(b, x,y);
			ref.main.addEntity(c);
			c.x = x;
			c.x = x;
			//ref.main.addEntity(d, x,y);
			
			a.direction = angle+90;
			//b.direction = angle+90;
			c.direction = angle+270;
			//d.direction = angle+270;
		}
		
		ref.draw.setDrawColor(0, 0, 0, 1);
		ref.draw.drawTexture(x, y, 300, 300, 0, 0, 0, 499, 1, 3); // big obama
		
		ref.draw.setDrawColor(0, 0, 0, 1);
		// FIX ME ref.draw.drawText(200, 300, 100, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, 100, "" + Math.round(1000/ref.main.time_delta), 4);
		ref.draw.drawText(x, y-ref.main.get_screen_height()/3, ref.main.get_screen_width()/36, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 100, "Wait - does this make lag spikes?", game_textures.TEX_FONT1);
	}
}

class entity_lilOBAMA extends engine_entity {
	
	float direction = 0;
	float speed = 50;
	double life_time = 6000;
	double life = life_time;
	
	float dx, dy, dt, x, y;
	
	boolean alarmTest = false;
	
	@Override
	public void sys_step(){
		
		dt = (float) (ref.main.time_delta / 1000f);
		
		life -= ref.main.time_delta;//=life_time/2;
		if (life <= 0) {
			instance_destroy();
		}
		if (alarmTest){
			ref.draw.setDrawColor(1, 0, 0, 1);
		} else {
			ref.draw.setDrawColor((float) (life/life_time), (float) (life/life_time), .5f + (float) (life/life_time), 1);
		}
		
		
		x+= (speed * dx) * dt;
		y+= (speed * dy) * dt;
		
//		ref.draw.drawTexture(x, y, 50, 50, 0, 0, image_angle, 0, 1, 3); // lil obama
		ref.draw.drawRectangle(x, y, 50, 50, 0, 0, 0, 3);
		
		
	}
	
	@Override
	public void sys_firstStep(){
		alarm[0] = 3000;//Math.round(10000 * Math.random());
		dx = FloatMath.cos(3.14159265359f/180 * direction);
		dy = FloatMath.sin(3.14159265359f/180 * direction);
	}
	
	@Override
	public void alarm0(){
		alarmTest = true;
	}
	
}