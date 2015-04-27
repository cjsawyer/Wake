package com.mammothGames.wake1.game.entities;

import android.os.SystemClock;
import android.util.Log;

import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.sounds;
import com.mammothGames.wake1.game.textures;
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
		ref.textureLoader.loadTexture(textures.TEX_SPRITES);
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
			
			float tWidth = ref.draw.getSubTextureWidth(textures.SUB_MAMMOTH, textures.TEX_SPRITES);
			float tHeight = ref.draw.getSubTextureHeight(textures.SUB_MAMMOTH, textures.TEX_SPRITES);
			float tFinalWidth = ref.screen_width*2f/3f;
			float tWidthHeightRatio = tFinalWidth/tWidth;
			float tFinalHeight = tHeight * tWidthHeightRatio;
			
			
			float tLogoX = (ref.screen_width - tFinalWidth) / 2f;
			float tLogoY = ref.screen_height/2;
			
			if (!fade_out)
				logo_alpha += ref.main.time_scale * 8f;
			else if (current_time-start_time > 2500)
				logo_alpha -= ref.main.time_scale * 8f;
			
			if (logo_alpha > 1)
				logo_alpha = 1;
			
			ref.draw.setDrawColor(1, 1, 1, logo_alpha);
			ref.draw.drawTexture(tLogoX, tLogoY, tFinalWidth, tFinalHeight, -tFinalWidth/2, 0, 0, 0, textures.SUB_MAMMOTH, textures.TEX_SPRITES);

			
			
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
		ref.textureLoader.loadTexture(textures.TEX_FONT1);
		ref.textureLoader.loadTexture(textures.TEX_STARS);
//		ref.sound.loadMusic(game_sounds.MSC_CEPHALOPOD);
//		ref.sound.loadSound(game_sounds.SND_SPLASH);
//		ref.sound.loadSound(game_sounds.SND_DING);
//		ref.sound.loadSound(sounds.SND_THUD);
//		ref.sound.loadSound(sounds.SND_CRACK1);
//		ref.sound.loadSound(sounds.SND_CRACK2);
//		ref.sound.loadSound(sounds.SND_CRACK3);
		ref.sound.loadMusic(sounds.MSC_SINE);
	}
}
