package com.mammothGames.wake1.game.entities;

import android.os.SystemClock;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_menuFirst extends engine_entity {

	masterGameReference mgr;
	public entity_menuFirst() {
		this.persistent = true;
		this.pausable = false;

		mgr = new masterGameReference();
	}
	
	float logo_w, logo_h;
	float wait_time = 1000; 
	
	final float DEG_TO_RAD = (float)(Math.PI/180f);
	
	float screen_cover_alpha = 1;
	
	boolean fade_out;

	@Override
	public void sys_firstStep() {
		
		fade_out = false;
		
		// ref.ad.loadBannerAd(ref.ad.H_CENTER, ref.ad.V_BOTTOM);
		// moved to interstitial ads

		ref.main.pauseEntities();
		
		mgr.menuFirst = this;
		
		mgr.gameMain = new entity_gameMain(mgr);
		ref.main.addEntity(mgr.gameMain);

        mgr.orbSpawner = new entity_orbSpawner(mgr);
        ref.main.addEntity(mgr.orbSpawner);

		mgr.orbPatternMaker = new entity_orbPatternMaker(mgr);
		ref.main.addEntity(mgr.orbPatternMaker);
		
		mgr.orbPatternMaker = new entity_orbPatternMaker(mgr);
		ref.main.addEntity(mgr.orbPatternMaker);
		
		mgr.menuPauseHUD = new entity_menuPauseHUD(mgr);
		ref.main.addEntity(mgr.menuPauseHUD);
		
		mgr.orbSpawner = new entity_orbSpawner(mgr);
		ref.main.addEntity(mgr.orbSpawner);
		
		mgr.menuDifficulty = new entity_menuDifficulty(mgr);
		ref.main.addEntity(mgr.menuDifficulty);
		
		mgr.menuPostGame = new entity_menuPostGame(mgr);
		ref.main.addEntity(mgr.menuPostGame);
		
		mgr.stars = new entity_stars(mgr);
		ref.main.addEntity(mgr.stars);
		
		mgr.backButton = new entity_backButton(mgr);
		ref.main.addEntity(mgr.backButton);
		

		mgr.menuMain = new entity_menuMain(mgr);
		ref.main.addEntity(mgr.menuMain);
		
//		mgr.menuRecords = new entity_menuRecords(mgr);
//		ref.main.addEntity(mgr.menuRecords);
		
		mgr.popup = new entity_popup(mgr);
		ref.main.addEntity(mgr.popup);
		
		mgr.countdown = new entity_countdown(mgr);
        ref.main.addEntity(mgr.countdown);
		
		
	}

	@Override
	public void sys_step() {
		
		//logo_alpha += (logo_alpha_target - logo_alpha) * 2 * mgr.gameMain.ANIMATION_SCALE * ref.main.time_scale;
		
		if (ref.room.get_current_room() == rooms.ROOM_MENUFIRST) {
			
			wait_time -= ref.main.time_delta;
			
			
//			if (first_fade_in_finished)
//				ref.draw.setDrawColor(1, 1, 1, logo_alpha);
//			else
//				ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			
			// Figures out the details of drawing the logo at 3/2 the width of the screen, properly scaling both width and height
			float tWidth = ref.draw.getSubTextureWidth(textures.SUB_LOGO, textures.TEX_SPRITES);
			float tHeight = ref.draw.getSubTextureHeight(textures.SUB_LOGO, textures.TEX_SPRITES);
			logo_w = ref.screen_width*2f/3f;
			float tWidthHeightRatio = logo_w/tWidth;
			logo_h = tHeight * tWidthHeightRatio;
			
			
//			ref.draw.drawTexture(ref.screen_width/2, ref.screen_height/2, logo_w, logo_h, 0, 0, 0, game_constants.layer6_HUD, game_textures.SUB_LOGO, game_textures.TEX_SPRITES);

			
			// If fully faded in
//			if (mgr.gameMain.shade_alpha > 0.98f) {
				
//				first_fade_in_finished = true;
				
				// If you tap the screen
				if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) || (wait_time<0)) {
					// Start fading out
					fade_out = true;
					mgr.gameMain.shade_alpha_target = 0;
				}
//			}
			
			// When fully faded back out, start the game
//			if ( (fade_out == true) && (mgr.gameMain.shade_alpha < 0.02f) ) {
//				fade_out = false;
				mgr.menuMain.start();
//			}
		}
	}
	
	public void start() {
		mgr.gameMain.shade_alpha = -1; // prepare fade in effect
		mgr.gameMain.shade_alpha_target = 1;
//		logo_alpha = -1;
//		logo_alpha_target = 1;
		
		ref.room.changeRoom(rooms.ROOM_MENUFIRST);
	}

}