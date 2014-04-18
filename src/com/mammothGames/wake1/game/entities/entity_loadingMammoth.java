package com.mammothGames.wake1.game.entities;

import android.os.SystemClock;
import android.util.Log;

import com.mammothGames.wake1.game.game_rooms;
import com.mammothGames.wake1.game.game_sounds;
import com.mammothGames.wake1.game.game_textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_loadingMammoth extends engine_entity {

	public entity_loadingMammoth() {
		this.persistent = false;
		this.pausable = false;
	}
	
	boolean logoLoaded = false;
//	float screen_width, screen_height;
	float start_time, current_time;
	float logo_alpha = 0;
	
	boolean fade_out = false;
	
	@Override
	public void sys_firstStep(){
		
		ref.loadHelper.setNumberToLoad(1);
		ref.textureLoader.loadTexture(game_textures.TEX_SPRITES);
	}
	
	@Override
	public void sys_step() {
		
		current_time = (float) (SystemClock.elapsedRealtime());
		
		if (logoLoaded == false) {
			if (ref.loadHelper.checkFinished()) {
				logoLoaded = true;
				loadRest();
				start_time = (float) (SystemClock.elapsedRealtime());
			}
		} else {
			
			float tWidth = ref.draw.getSubTextureWidth(game_textures.SUB_MAMMOTH, game_textures.TEX_SPRITES);
			float tHeight = ref.draw.getSubTextureHeight(game_textures.SUB_MAMMOTH, game_textures.TEX_SPRITES);
			float tFinalWidth = ref.screen_width*2f/3f;
			float tWidthHeightRatio = tFinalWidth/tWidth;
			float tFinalHeight = tHeight * tWidthHeightRatio;
			
			
			float tLogoX = (ref.screen_width - tFinalWidth) / 2f;
			float tLogoY = ref.screen_height/2;
			
			if (!fade_out)
				logo_alpha += ref.main.time_scale * 8f;
			else if (current_time-start_time > 1750)
				logo_alpha -= ref.main.time_scale * 8f;
			
			ref.draw.setDrawColor(1, 1, 1, logo_alpha);
			ref.draw.drawTexture(tLogoX, tLogoY, tFinalWidth, tFinalHeight, -tFinalWidth/2, 0, 0, 0, game_textures.SUB_MAMMOTH, game_textures.TEX_SPRITES);

			
			
			if (ref.loadHelper.checkFinished()) {
				fade_out = true;
			}
			// if it's been longer than a second.
//			if (current_time-start_time > 1500) {
				if ( (fade_out) && (logo_alpha < 0.02f) ) {
					// Go to the room that initializes the menu.
					ref.room.changeRoom(1);
				}
//			}
		}
		
		
		
		
	}
	
	public void loadRest() {
		ref.loadHelper.reset();
		ref.loadHelper.setNumberToLoad(3);
//		ref.textureLoader.loadTexture(game_textures.TEX_SPRITES);
		ref.textureLoader.loadTexture(game_textures.TEX_FONT1);
		ref.textureLoader.loadTexture(game_textures.TEX_STARS);
//		ref.sound.loadMusic(game_sounds.MSC_CEPHALOPOD);
//		ref.sound.loadSound(game_sounds.SND_SPLASH);
//		ref.sound.loadSound(game_sounds.SND_DING);
		ref.sound.loadMusic(game_sounds.MSC_SINE);
	}
}
