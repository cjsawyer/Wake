package com.mammothGames.wake.game.entities;

import android.util.FloatMath;
import android.util.Log;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_stars extends engine_entity {

	float starTextureSmallHeight;
	float starTextureTallHeight;
	
	float small_angle = 0, tall_angle = 0;
	float stars_alpha = 0;
	
	@Override
	public void sys_firstStep() {
		
		persistent = true;
		pausable = false;
		
		// Calculates the minimum size of the textures for the starfield textures.
		// It's the length between the bottom middle of the screen and one of the top corners that is the minimum for half of the smaller of the two textures.
		
		float tmpWidth = (ref.screen_width/2);
		float tmpHeight = (ref.screen_height);
		
		tmpWidth = tmpWidth*tmpWidth;
		tmpHeight = tmpHeight*tmpHeight;
		
		float hyp = (float) Math.sqrt(tmpWidth+tmpHeight);
		
		starTextureSmallHeight = hyp*2;
		starTextureTallHeight = hyp*4;
	}
	
	@Override
	public void sys_step() {
		
		stars_alpha += 2 * ref.main.time_scale;
		if (stars_alpha > 1)
			stars_alpha = 1;
		
		small_angle += ref.main.time_scale/3; // Rotate 1/2 degree per second
		tall_angle -= ref.main.time_scale/4;
		
//		if ((ref.room.get_current_room() == game_rooms.ROOM_GAME) || (ref.room.get_current_room() == game_rooms.ROOM_MENU)) {
		ref.draw.setDrawColor(1, 1, 1, 1 * stars_alpha);
		ref.draw.drawTexture(ref.screen_width/2, 0, starTextureSmallHeight, starTextureSmallHeight, 0, 0, small_angle, game_constants.layer0_backgroundSquares, 1, game_textures.TEX_STARS);
		ref.draw.setDrawColor(1, 1, 1, 0.9f * stars_alpha);
		ref.draw.drawTexture(ref.screen_width/2, 0, starTextureTallHeight, starTextureTallHeight, 0, 0, tall_angle, game_constants.layer0_backgroundSquares, 1, game_textures.TEX_STARS);
//		}

	}
	
	@Override
	public void onRoomLoad() {
		if (ref.room.get_current_room() == game_rooms.ROOM_GAME)
			pausable = true;
		else
			pausable = false;
	}
	
	
}
