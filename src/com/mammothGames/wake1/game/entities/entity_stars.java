package com.mammothGames.wake1.game.entities;

import com.mammothGames.wake1.game.*;
import com.mammothGames.wake1.gameEngine.*;

public class entity_stars extends engine_entity {

	float starTextureSmallHeight;
	float starTextureTallHeight;
	
	float small_angle = 0, tall_angle = 0;
	float stars_alpha = 0;
	
	float red_alpha=0, red_alpha_target=0;
	private float transition_alpha=0, transition_alpha_target=0;
	
	float x, y;
	
	masterGameReference mgr;
	public entity_stars(masterGameReference mgr) {
		this.mgr = mgr;
		this.persistent = true;
		this.pausable = false;
	}
	
	@Override
	public void sys_firstStep() {
		
		persistent = true;
		pausable = false;		
		
		// Calculates the minimum size for the starfield textures.
		// It's the length between the bottom middle of the screen and one of the top corners that is the minimum for half of the smaller of the two textures.
		
		float tmpWidth = (ref.screen_width/2 + ref.screen_width/6);
		float tmpHeight = (ref.screen_height*7/6f);
		
		tmpWidth = tmpWidth*tmpWidth;
		tmpHeight = tmpHeight*tmpHeight;
		
		float hyp = (float) Math.sqrt(tmpWidth+tmpHeight);
		
		starTextureSmallHeight = hyp*2;
		starTextureTallHeight = hyp*4;
	}
	
	@Override
	public void sys_step() {
		
		// So it never stops outside of being paused in the game, and still updates during the count-down to avoid being gone if the screen locks and is there to be captured
		if ( (ref.room.get_current_room() == rooms.ROOM_GAME) || (mgr.countdown.counting) )
			pausable = true;
		else
			pausable = false;
		
		
		if (mgr.popup.show_stars) { // save is loaded here
			
			// Tick linear fade functions
			red_alpha += (red_alpha_target - red_alpha) * mgr.gameMain.ANIMATION_SCALE * ref.main.time_scale;
			transition_alpha += (transition_alpha_target - transition_alpha) * mgr.gameMain.ANIMATION_SCALE * ref.main.time_scale / 2f;
			
			// Compute base position
			x = mgr.menuMain.x/3 - ref.screen_width/2;
			y = mgr.menuMain.y/3 - ref.screen_height/2;
			
			// Fade in at start
			stars_alpha += 2 * ref.main.time_scale;
			if (stars_alpha > 1)
				stars_alpha = 1;
			
			// Rotate over time
			small_angle += ref.main.time_scale/3.5f; // Rotate 1/2 degree per second
			tall_angle -= ref.main.time_scale/4;
			
			// Draw both textures
			ref.draw.setDrawColor(1, 1-red_alpha, 1-red_alpha, 1 * stars_alpha);
			ref.draw.drawTexture(ref.screen_width/2 + x/6, y/6, starTextureSmallHeight, starTextureSmallHeight, 0, 0, small_angle, constants.layer0_backgroundSquares, 1, textures.TEX_STARS);
			ref.draw.setDrawColor(1, 1-red_alpha, 1-red_alpha, 0.9f * stars_alpha);
			ref.draw.drawTexture(ref.screen_width/2 + x/4, y/4, starTextureTallHeight, starTextureTallHeight, 0, 0, tall_angle, constants.layer0_backgroundSquares, 1, textures.TEX_STARS);
		}
		
		// Draw white overlay for transition into records screen
		ref.draw.setDrawColor(1, 1, 1, 1 * transition_alpha);
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer0_backgroundSquares);
		
	}
	
	@Override
	public void onRoomLoad() {
		//
	}
	
	public void transition() {
		transition_alpha = 1;
		transition_alpha_target = 0;
	}
	
}
