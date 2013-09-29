package com.reywas.testGameName.game.entities.neonFlux;

import android.os.SystemClock;
import android.util.Log;

import com.reywas.testGameName.game.game_rooms;
import com.reywas.testGameName.game.game_textures;
import com.reywas.testGameName.gameEngine.*;


public class entity_menuMain extends engine_entity {

	masterGameReference mgr;
	public entity_menuMain() {
		this.persistent = true;
		this.pausable = false;
		
		mgr = new masterGameReference();
	}
	
	final float DEG_TO_RAD = (float)(Math.PI/180f);
	
	private float screen_width, screen_height;
	int random_number = (int) (Math.random() * 100);
	float screen_cover_alpha = 1;
	
	@Override
	public void sys_firstStep() {
		
		ref.ad.loadAd(ref.ad.H_CENTER, ref.ad.V_BOTTOM);
		
		ref.main.pauseEntities();
		
		mgr.gameMenu = this;
		
		mgr.gameMain = new entity_gameMain(mgr);
		ref.main.addEntity(mgr.gameMain);

		mgr.masterOrbSpawner = new entity_masterOrbSpawner(mgr);
		ref.main.addEntity(mgr.masterOrbSpawner);
		
		mgr.orbSpawnerState = new entity_orbSpawnerState(mgr);
		ref.main.addEntity(mgr.orbSpawnerState);
		
		mgr.gameStateManager = new entity_gameStateManager(mgr);
		ref.main.addEntity(mgr.gameStateManager);
		
		mgr.pauseMenu = new entity_menuPause(mgr);
		ref.main.addEntity(mgr.pauseMenu);
		
		mgr.greenOrbSpawner = new entity_greenOrbSpawner(mgr);
		ref.main.addEntity(mgr.greenOrbSpawner);
		
		mgr.menuPostGame = new entity_menuPostGame(mgr);
		ref.main.addEntity(mgr.menuPostGame);
		
		mgr.hud = new entity_hud(mgr);
		ref.main.addEntity(mgr.hud);
		
		
		
		screen_width = ref.main.get_screen_width();
		screen_height = ref.main.get_screen_height();
		
	}
	
	@Override
	public void sys_step() {
		
		if (ref.room.get_current_room() == game_rooms.ROOM_MENU) {
			
			screen_cover_alpha -= ref.main.time_scale * 2;
			ref.draw.setDrawColor(0, 1, 0, screen_cover_alpha);
			ref.draw.drawRectangle(screen_width/2, screen_height/2, screen_width, screen_height, 0, 0, 0, 200);
			
			ref.draw.setDrawColor(1, 1, 1, 1);
			
			// Figures out the details of drawing the logo at 3/2 the width of the screen, properly scaling both width and height
			float tWidth = ref.draw.getSubTextureWidth(game_textures.SUB_LOGO, game_textures.TEX_SPRITES);
			float tHeight = ref.draw.getSubTextureHeight(game_textures.SUB_LOGO, game_textures.TEX_SPRITES);
			float tFinalWidth = screen_width*2f/3f;
			float tWidthHeightRatio = tFinalWidth/tWidth;
			float tFinalHeight = tHeight * tWidthHeightRatio;
			float tLogoX = (screen_width - tFinalWidth) / 2f;
			
			float tTextY = ( (screen_height*3f/4f) - tFinalHeight/2f) / 2;
			
			//3/4th of the way up the screen, down 1/4th of the texture to match the middle of the word.
			float tLogoY = screen_height*3f/4f - tFinalHeight/2;
			
			
			ref.draw.drawTexture(tLogoX, tLogoY, tFinalWidth, tFinalHeight, tFinalWidth/2, 0, 0, 0, game_textures.SUB_LOGO, game_textures.TEX_SPRITES);
			
			ref.draw.setDrawColor(1, 1, 1, ((float)Math.sin((float)(SystemClock.uptimeMillis() * DEG_TO_RAD / 1000f * 180f))) + 1);
			ref.draw.drawText(ref.main.get_screen_width()/2, tTextY, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 0, "-tap to play-", game_textures.TEX_FONT1);
			
			// Draw high-score
			ref.draw.setDrawColor(1, 1, 1, 1);
			
			ref.strings.builder.setLength(0);
			ref.strings.builder.append(  "BEST: "  );
			ref.strings.builder.append(  mgr.gameMain.high_score  );
			ref.strings.builder.getChars(0, ref.strings.builder.length(), ref.strings.stringChars, 0);
			ref.draw.drawText(screen_width/2, screen_height - mgr.gameMain.text_size/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, 0, 0, ref.strings.stringChars, ref.strings.builder.length(), game_textures.TEX_FONT1);
					//x, y, size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, draw_angle, depth, ref.strings.stringChars, ref.strings.builder.length(), game_textures.FONT_TEXTURE_NAME);
//			ref.draw.drawText(, screen_height - mgr.gameMain.text_size/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_TOP, 0, "BEST: " + , game_textures.TEX_FONT1);
			
			if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
				if (ref.input.get_touch_y(0) < (ref.main.get_screen_height() - (ref.main.get_screen_height() - tLogoY)) ) {
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
