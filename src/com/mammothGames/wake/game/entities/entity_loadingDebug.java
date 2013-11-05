package com.mammothGames.wake.game.entities;

import android.util.Log;

import com.mammothGames.wake.game.game_sounds;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_loadingDebug extends engine_entity {

	public entity_loadingDebug() {
		this.persistent = false;
		this.pausable = false;
	}
	
	@Override
	public void sys_firstStep(){
		loadAll();
	}
	
	float angle = 0, target_angle = 0;
	@Override
	public void sys_step() {
		
		target_angle = ref.loadHelper.checkFinishedScalar() * 360;
		angle += (target_angle - angle) * 10 * ref.main.time_scale;
		
		ref.draw.setDrawColor(0.5f, 0.5f, 0.5f, 1);
		ref.draw.drawCircle(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2, ref.main.get_screen_width()/4, 0, 0, 360, 0, 1);
		
		ref.draw.setDrawColor(1, 1, 1, 1);
		ref.draw.drawCircle(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2, ref.main.get_screen_width()/5, 0, 0, angle, 0, 2);
		
		if (ref.loadHelper.checkFinished()) {
			if (angle > 350)
				ref.room.changeRoom(1);
		}
		
	}
	
	public void loadAll() {
		
		ref.loadHelper.setNumberToLoad(4);//7
		ref.textureLoader.loadTexture(game_textures.TEX_SPRITES);
//		ref.textureLoader.loadTexture(game_textures.TEX_PARTICLE);
//		ref.textureLoader.loadTexture(game_textures.TEX_MENU_BUTTONS);
//		ref.textureLoader.loadTexture(game_textures.TEX_MAMMOTH_LOGO);
		ref.textureLoader.loadTexture(game_textures.TEX_FONT1);
		// error might be here vvvvvv
//		ref.sound.loadMusic(game_sounds.MSC_CEPHALOPOD);
		ref.sound.loadSound(game_sounds.SND_SPLASH);
		ref.sound.loadSound(game_sounds.SND_DING);
//		ref.sound.loadSound(game_sounds.SND_CRACK1);
//		ref.sound.loadSound(game_sounds.SND_CRACK2);
//		ref.sound.loadSound(game_sounds.SND_CRACK3);
	}
}
