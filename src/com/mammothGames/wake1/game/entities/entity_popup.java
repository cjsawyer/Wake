package com.mammothGames.wake1.game.entities;

import android.util.Log;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;
import com.mammothGames.wake1.gameTESTS.game_constants;
import com.mammothGames.wake1.gameTESTS.game_textures;


public class entity_popup extends engine_entity {

	private boolean popup_open = false;
	private float popup_alpha = 0, popup_alpha_target = 0;
	
	PopupGUI pause_gui;
	
	masterGameReference mgr;
	public entity_popup(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;

		this.mgr = mgr;
		
	}

	
	
	public final int STATE_ABANDON = 0;
	public final int STATE_QUIT = 1;
	public final int STATE_ERASE = 2;
	public final int STATE_TEST = 3;
	
	private final String[][] state_text = {
			{ "Abandon game?" },
			{ "Quit game?" },
			{ "Erase records?" },
			{ "text 1", "text 2", "text the third" },
	};
	
	public final int NULL = 0;
	public final int UI_TITLE = 1;
	public final int UI_TEXT = 2;
	public final int UI_BUTTON = 3;
	public final int UI_CHECKBOX = 4;
	
	public final int ACT_NO = 1;
	public final int ACT_YES = 2;
	public final int ACT_SETTINGS = 2;
	
/*	
	private final int[][][] ui = {
	        // UI type, action
	        {{1,1,UI_TEXT}, {NULL}},
	        {{1,1,UI_TEXT}, {NULL}},
	        {{1,1,UI_TEXT}, {NULL}},

	        {
	           , , {
	                // TODO: move this into element object method
	                // Actions when that element is clicked
	                NULL,		NULL,			NULL,
	                NULL,		NULL,			NULL,
	                NULL,		NULL,			NULL,
	                NULL,		NULL,			NULL,
	                NULL,		ACT_SETTINGS,	NULL,
	                ACT_YES,	NULL,			ACT_NO
	            }

	        },

	};
*/
	
	/*

	}, {
        // Type of GUI element
        NULL,       UI_TITLE,   NULL,
        UI_TEXT,    UI_TEXT,    UI_TEXT,
        UI_CHECKBOX,NULL,       UI_TEXT,
        UI_TEXT,    NULL,       UI_TEXT,
        NULL,       UI_BUTTON,  NULL,
        UI_BUTTON,  NULL,       UI_BUTTON
    }, {
	
	*/
	
	
//	float x = x-ui_w/2 + (ix+0.5f)*(ui_w/((float)elements_x) );
//	int action = ui[action][1][ui_i];
	
//	float width = w/(elements_x);
//	float height = h/(elements_y);
//	int type = ui[action][1][size];
//	int weight = 1;
	
	//ref.draw.setDrawColor(ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), popup_alpha);
	//ref.draw.drawRectangle(draw_x, draw_y, width, height/2, 0, 0, 0, 101);
	
//	if (element_type[size-b] == NULL) {
//		ref.draw.setDrawColor(1, 0, 0, popup_alpha);
//		ref.draw.text.append(b);
//		ref.draw.drawText(draw_x-width/2, draw_y, 30, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 200, textures.TEX_FONT1);
//		//ref.draw.drawCircle(draw_x-width/2, draw_y, 10, 0, 0, 360, 0, 200);
//	}
	
//	for(int f=iw; f<elements_x; f++) {
//		if (element_type[size-b] == NULL) {
//			ref.draw.setDrawColor(1, 0, 0, popup_alpha);
//			ref.draw.drawCircle(draw_x-width/2, draw_y, 10, 0, 0, 360, 0, 100);
//		}
//	}

	
	public void buttonAction(boolean yes_or_no_action) {
		switch (action) {
		
			case STATE_ABANDON:
				if (yes_or_no_action) {
					setPopupOpenness(false);
					mgr.menuPauseHUD.setPause(false);
					mgr.gameMain.floor_height_target = 0;
					mgr.gameMain.endGame();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_QUIT:
				if (yes_or_no_action) {
					ref.main.exitApp();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_ERASE:
				if (yes_or_no_action) {
					ref.file.save("SCO_E", String.valueOf(0) );
					ref.file.save("STR_E", String.valueOf(0) );
					ref.file.save("PLY_E", String.valueOf(0) );
					
					ref.file.save("SCO_M", String.valueOf(0) );
					ref.file.save("STR_M", String.valueOf(0) );
					ref.file.save("PLY_M", String.valueOf(0) );
					
					ref.file.save("SCO_H", String.valueOf(0) );
					ref.file.save("STR_H", String.valueOf(0) );
					ref.file.save("PLY_H", String.valueOf(0) );
					
					ref.file.save("SCO_HE", String.valueOf(0) );
					ref.file.save("STR_HE", String.valueOf(0) );
					ref.file.save("PLY_HE", String.valueOf(0) );
					
					mgr.menuRecords.loadScores();
					mgr.areYouSure.setPopupOpenness(false);
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_TEST:
				if (yes_or_no_action) {
					ref.main.exitApp();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
		}
	}
	
	
	
   @Override
   public void sys_firstStep() {
        
        float draw_width = ref.screen_width*3.5f/5;
        float draw_height = draw_width*1.25f;
        float draw_x = ref.screen_width/2;
        float draw_y = ref.screen_height/2;
        
        pause_gui = new PopupGUI(ref, mgr);
        pause_gui.setPosition(draw_x, draw_y);
        pause_gui.setSize(draw_width, draw_height);
        pause_gui.populate();
        pause_gui.setDepth(constants.layer7_overHUD);
        pause_gui.setActive(true);
        
        
    }
	
	
	
	@Override
	public void sys_step() {
		
		popup_alpha += (popup_alpha_target - popup_alpha) * 8f * ref.main.time_scale;
		pause_gui.setAlpha(popup_alpha);
	    pause_gui.setActive(popup_open);
		
//		if (ref.input.get_touch_state(0) == ref.input.TOUCH_HELD) {
//		    pause_gui.setPosition(ref.input.get_touch_x(0), ref.input.get_touch_y(0));
//		}
		
//		r
		// black veil covering everything under popup
		ref.draw.setDrawColor(0,0,0, 0.7f * popup_alpha);
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer7_overHUD);
		
		
		pause_gui.draw();
		
	}
	
	@Override
	public void onScreenSleep() {
		setPopupOpennessHard(false);
	}
	
	private int action = 0;
	private String text = "";

	public void setPopupAction(int action) {
		this.action = action;
	}
	
	public void setPopupOpenness(boolean open) {
		if (Math.abs(popup_alpha-popup_alpha_target) < 0.2f) {
			popup_open = open;
			popup_alpha = popup_alpha_target;
			popup_alpha_target = open ? 1 : 0;
		}
	}
	public void setPopupOpennessHard(boolean open) {
		popup_open = open;
		popup_alpha_target = open ? 1 : 0;
		popup_alpha = popup_alpha_target;
		
	}
	public boolean getPopupOpenness() {
		return popup_open;
	}
	
}

class PopupGUI extends engine_gui {
    
    private final int idTitle = 0;
    private final int idScore = 1;
    private final int idScoreNumber = 2;
    private final int idStreak = 3;
    private final int idStreakNumber = 4;
    private final int idSettingsButton = 5;
    private final int idAgainButton = 6;
    private final int idLeaveButton = 7;
    
    final int[][] layout1 = {
        { 
            // horizontal, vertical number of GUI elements
            3,5
        }, {
            // GUI element ID's
            NULL,         idTitle,          NULL,
            idScore,      NULL,             idScoreNumber,
            idStreak,     NULL,             idStreakNumber,
            NULL,         idSettingsButton, NULL,
            idAgainButton, NULL,             idLeaveButton
        }
    };
    
    masterGameReference mgr;
    public PopupGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    public void populate() {
        setLayout(layout1);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        engine_guiText title = new engine_guiText(this, idTitle);
        title.setText("PAUSED");
        title.setTextureSheet(game_textures.TEX_FONT1);
        title.setTextSize(mgr.gameMain.text_size);
        title.setTextColor(0,1,0,1);
        title.setBorder(border,border,border,border);
        title.setBorderColor(0,1,1,0.3f);
        title.setBackgroundColor(0,0,0,1);
        title.setPadding(border);
        
        engine_guiText score = new engine_guiText(this, idScore);
        score.setText("Score");
        score.setTextureSheet(game_textures.TEX_FONT1);
        score.setTextSize(mgr.gameMain.text_size);
        score.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        score.setBorder(0,0,border,0);
        score.setBorderColor(0,1,1,0.3f);
        score.setBackgroundColor(0.12f,0.12f,0.12f,1);
        score.setPadding(border);
        
        engine_guiNumber scoreNumber = new engine_guiNumber(this, idScoreNumber);
        scoreNumber.setNumber(12345);
        scoreNumber.setTextureSheet(game_textures.TEX_FONT1);
        scoreNumber.setTextSize(mgr.gameMain.text_size);
        scoreNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        scoreNumber.setBorder(0,0,0,border);
        scoreNumber.setBorderColor(0,1,1,0.3f);
        scoreNumber.setBackgroundColor(0.12f,0.12f,0.12f,1);
        scoreNumber.setPadding(border);
        
        
        engine_guiText streak = new engine_guiText(this, idStreak);
        streak.setText("Streak");
        streak.setTextureSheet(game_textures.TEX_FONT1);
        streak.setTextSize(mgr.gameMain.text_size);
        streak.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        streak.setBorder(0,0,border,0);
        streak.setBorderColor(0,1,1,0.3f);
        streak.setBackgroundColor(0.12f,0.12f,0.12f,1);
        streak.setPadding(border);
        
        engine_guiNumber streakNumber = new engine_guiNumber(this, idStreakNumber);
        streakNumber.setNumber(3210);
        streakNumber.setTextureSheet(game_textures.TEX_FONT1);
        streakNumber.setTextSize(mgr.gameMain.text_size);
        streakNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        streakNumber.setBorder(0,0,0,border);
        streakNumber.setBorderColor(0,1,1,0.3f);
        streakNumber.setBackgroundColor(0.12f,0.12f,0.12f,1);
        streakNumber.setPadding(border);
        
        
        engine_guiButton settings = new engine_guiButton(this, idSettingsButton);
        settings.setText("Settings");
        settings.setTextureSheet(game_textures.TEX_FONT1);
        settings.setTextSize(mgr.gameMain.text_size);
        settings.setBorder(border,border,border,border);
        settings.setBorderColor(0,1,1,0.3f);
        settings.setBackgroundColor(0,0,0,1);
        settings.setPadding(border);
        
        engine_guiButton back = new engine_guiButton(this, idAgainButton);
        back.setText("again");
        back.setTextureSheet(game_textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(0,border,border,border/2);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,1);
        back.setPadding(border);
        
        engine_guiButton quit = new engine_guiButton(this, idLeaveButton);
        quit.setText("leave");
        quit.setTextureSheet(game_textures.TEX_FONT1);
        quit.setTextSize(mgr.gameMain.text_size);
        quit.setBorder(0,border,border/2,border);
        quit.setBorderColor(0,1,1,0.3f);
        quit.setBackgroundColor(0,0,0,1);
        quit.setPadding(border);
        
        
        
        addElement(title);
        addElement(score);
        addElement(scoreNumber);
        addElement(streak);
        addElement(streakNumber);
        addElement(settings);
        addElement(back);
        addElement(quit);
        
        build();
    }

}