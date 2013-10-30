package com.mammothGames.wake.game.entities;

import android.os.SystemClock;
import android.util.Log;

import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_sounds;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_mammothLoadingScreen extends engine_entity {

	public entity_mammothLoadingScreen() {
		this.persistent = false;
		this.pausable = false;
	}
	
	boolean logoLoaded = false;
	float screen_width, screen_height;
	float start_time, current_time;
	float logo_alpha = 0;
	
	@Override
	public void sys_firstStep(){
		
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
		
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
			float tFinalWidth = screen_width*2f/3f;
			float tWidthHeightRatio = tFinalWidth/tWidth;
			float tFinalHeight = tHeight * tWidthHeightRatio;
			
			
			float tLogoX = (screen_width - tFinalWidth) / 2f;
			float tLogoY = screen_height/2;
			
			logo_alpha += ref.main.time_scale * 2f;
			
			ref.draw.setDrawColor(1, 1, 1, logo_alpha);
			ref.draw.drawTexture(tLogoX, tLogoY, tFinalWidth, tFinalHeight, tFinalWidth/2, 0, 0, 0, game_textures.SUB_MAMMOTH, game_textures.TEX_SPRITES);

			
			
			if (ref.loadHelper.checkFinished()) {
				// if it's been longer than 5 seconds.
//				Log.e("asd", "diff " + (current_time-start_time));
				if (current_time-start_time > 1000) {
					// Go to the room that initializes the menu.
					ref.room.changeRoom(1);
				}
			}
		}
		
		
		
		
	}
	
	public void loadRest() {
		ref.loadHelper.reset();
		ref.loadHelper.setNumberToLoad(3);
//		ref.textureLoader.loadTexture(game_textures.TEX_SPRITES);
		ref.textureLoader.loadTexture(game_textures.TEX_FONT1);
//		ref.sound.loadMusic(game_sounds.MSC_CEPHALOPOD);
		ref.sound.loadSound(game_sounds.SND_SPLASH);
		ref.sound.loadSound(game_sounds.SND_DING);
	}
}
