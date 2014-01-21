package com.mammothGames.wake.game.entities;

import android.os.SystemClock;

import com.mammothGames.wake.game.game_constants;
import com.mammothGames.wake.game.game_rooms;
import com.mammothGames.wake.game.game_textures;
import com.mammothGames.wake.gameEngine.*;


public class entity_menuMain extends engine_entity {

	masterGameReference mgr;
	public entity_menuMain() {
		this.persistent = true;
		this.pausable = false;

		mgr = new masterGameReference();
	}
	
	final float DEG_TO_RAD = (float)(Math.PI/180f);
	
	float screen_cover_alpha = 1;

	@Override
	public void sys_firstStep() {
		
		ref.ad.loadAd(ref.ad.H_CENTER, ref.ad.V_BOTTOM);

		ref.main.pauseEntities();
		
		mgr.menuMain = this;
		
		mgr.gameMain = new entity_gameMain(mgr);
		ref.main.addEntity(mgr.gameMain);

        mgr.orbSpawner = new entity_orbSpawner(mgr);
        ref.main.addEntity(mgr.orbSpawner);

		mgr.orbPatternMaker = new entity_orbPatternMaker(mgr);
		ref.main.addEntity(mgr.orbPatternMaker);
		
		mgr.orbPatternMaker = new entity_orbPatternMaker(mgr);
		ref.main.addEntity(mgr.orbPatternMaker);
		
		mgr.menuPause = new entity_menuPause(mgr);
		ref.main.addEntity(mgr.menuPause);
		
		mgr.orbSpawner = new entity_orbSpawner(mgr);
		ref.main.addEntity(mgr.orbSpawner);
		
		mgr.menuPostGame = new entity_menuPostGame(mgr);
		ref.main.addEntity(mgr.menuPostGame);
		
		ref.main.addEntity(new entity_stars());

		
	}

	@Override
	public void sys_step() {
		
		if (ref.room.get_current_room() == game_rooms.ROOM_MENU) {
			
			screen_cover_alpha -= ref.main.time_scale * 2;
			ref.draw.setDrawColor(0, 1, 0, screen_cover_alpha);
			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, game_constants.layer7_overHUD);
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			
			// Figures out the details of drawing the logo at 3/2 the width of the screen, properly scaling both width and height
			float tWidth = ref.draw.getSubTextureWidth(game_textures.SUB_LOGO, game_textures.TEX_SPRITES);
			float tHeight = ref.draw.getSubTextureHeight(game_textures.SUB_LOGO, game_textures.TEX_SPRITES);
			float tFinalWidth = ref.screen_width*2f/3f;
			float tWidthHeightRatio = tFinalWidth/tWidth;
			float tFinalHeight = tHeight * tWidthHeightRatio;
			float tLogoX = (ref.screen_width - tFinalWidth) / 2f;
			
			float tTextY = ( (ref.screen_height*3f/4f) - tFinalHeight/2f) / 2;
			
			//3/4th of the way up the screen, down 1/4th of the texture to match the middle of the word.
			float tLogoY = ref.screen_height*3f/4f - tFinalHeight/2;
			
			
			ref.draw.drawTexture(tLogoX, tLogoY, tFinalWidth, tFinalHeight, tFinalWidth/2, 0, 0, game_constants.layer6_HUD, game_textures.SUB_LOGO, game_textures.TEX_SPRITES);
			
			ref.draw.setDrawColor(1, 1, 1, ((float)Math.sin((float)(SystemClock.uptimeMillis() * DEG_TO_RAD / 1000f * 180f))) + 1);
			ref.draw.drawTextSingleString(ref.screen_width/2, tTextY, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, game_constants.layer6_HUD, "-tap to play-", game_textures.TEX_FONT1);
			
			// Draw high-score
			ref.draw.setDrawColor(1, 1, 1, 1);
//			
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  "BEST: "  );
			ref.strings.builder.append(  mgr.gameMain.high_score  );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(ref.screen_width/2, ref.screen_height - mgr.gameMain.text_size/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, 0, game_constants.layer7_overHUD, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
//			ref.draw.drawTextSingleString(ref.screen_width/2, ref.screen_height - mgr.gameMain.text_size/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, 0, _scoreToDraw, game_textures.TEX_FONT1);
			
			if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
				if (ref.input.get_touch_y(0) < (ref.screen_height - (ref.screen_height - tLogoY)) ) {
					// Start game
					ref.room.changeRoom(game_rooms.ROOM_GAME);
					mgr.gameMain.restartGame();
				}
			}
			
		}
	}
	

	
	@Override
	public void backButton() {
		if (ref.room.get_current_room() == game_rooms.ROOM_POSTGAME) {
			ref.room.changeRoom(game_rooms.ROOM_MENU);
		}
		if (ref.room.get_current_room() == game_rooms.ROOM_MENU) {
			ref.android.finish();
		}
	}
}