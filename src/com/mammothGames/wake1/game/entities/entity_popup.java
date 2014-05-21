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
	BooleanGUI bool_gui;
	
	masterGameReference mgr;
	public entity_popup(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;

		this.mgr = mgr;
		
	}

	
	
	public final int STATE_ABANDON = 0;
	public final int STATE_QUIT = 1;
	public final int STATE_ERASE = 2;
	
	private final String[][] state_text = {
			{ "Abandon game?" },
			{ "Quit game?" },
			{ "Erase records?" },
	};
	
	public final int NULL = 0;
	public final int UI_TITLE = 1;
	public final int UI_TEXT = 2;
	public final int UI_BUTTON = 3;
	public final int UI_CHECKBOX = 4;
	
	public final int ACT_NO = 1;
	public final int ACT_YES = 2;
	public final int ACT_SETTINGS = 2;
	
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
//        pause_gui.setActive(true);
        
        bool_gui = new BooleanGUI(ref, mgr);
        bool_gui.setPosition(draw_x, draw_y);
        bool_gui.setSize(draw_width, draw_width);
        bool_gui.populate();
        bool_gui.setDepth(constants.layer7_overHUD);
        bool_gui.setActive(true);
        
        
        
    }
	
	
	
	@Override
	public void sys_step() {
	    
	    //Close popup if a touch is outside of the GUI
	    if (ref.input.get_touch_state(0) == ref.input.TOUCH_HELD) {
	        if (!ref.collision.point_AABB(pause_gui.getW(), pause_gui.getH(), pause_gui.getX(), pause_gui.getY(), ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
	            setPopupOpenness(false);
            }
	    }
		
		popup_alpha += (popup_alpha_target - popup_alpha) * 8f * ref.main.time_scale;
		
		pause_gui.setAlpha(0);
		pause_gui.setActive(false);
		bool_gui.setAlpha(0);
		bool_gui.setActive(false);
		
        switch (action) {
        
            case STATE_ABANDON:
                pause_gui.setActive(popup_open);
                pause_gui.setAlpha(popup_alpha);
                break;
            case STATE_QUIT:
                bool_gui.setActive(popup_open);
                bool_gui.setAlpha(popup_alpha);
                break;
            case STATE_ERASE:
                bool_gui.setActive(popup_open);
                bool_gui.setAlpha(popup_alpha);
                break;
        }
		
		
	    
//		if (ref.input.get_touch_state(0) == ref.input.TOUCH_HELD) {
//		    pause_gui.setPosition(ref.input.get_touch_x(0), ref.input.get_touch_y(0));
//		}
		
		// black veil covering everything under popup
		ref.draw.setDrawColor(0,0,0, 0.7f * popup_alpha);
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer7_overHUD);
		
		
		pause_gui.update();
		bool_gui.update();
		
	}
	
	@Override
	public void onScreenSleep() {
		setPopupOpennessHard(false);
	}
	
	private int action = 0;

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
        title.setBackgroundColor(0,0,0,.9f);
        title.setPadding(border);
        
        engine_guiText score = new engine_guiText(this, idScore);
        score.setText("Score");
        score.setTextureSheet(game_textures.TEX_FONT1);
        score.setTextSize(mgr.gameMain.text_size);
        score.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        score.setBorder(0,0,border,0);
        score.setBorderColor(0,1,1,0.3f);
        score.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        score.setPadding(border);
        
        engine_guiNumber scoreNumber = new engine_guiNumber(this, idScoreNumber);
        scoreNumber.setNumber(12345);
        scoreNumber.setTextureSheet(game_textures.TEX_FONT1);
        scoreNumber.setTextSize(mgr.gameMain.text_size);
        scoreNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        scoreNumber.setBorder(0,0,0,border);
        scoreNumber.setBorderColor(0,1,1,0.3f);
        scoreNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        scoreNumber.setPadding(border);
        
        
        engine_guiText streak = new engine_guiText(this, idStreak);
        streak.setText("Streak");
        streak.setTextureSheet(game_textures.TEX_FONT1);
        streak.setTextSize(mgr.gameMain.text_size);
        streak.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        streak.setBorder(0,0,border,0);
        streak.setBorderColor(0,1,1,0.3f);
        streak.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        streak.setPadding(border);
        
        engine_guiNumber streakNumber = new engine_guiNumber(this, idStreakNumber);
        streakNumber.setNumber(3210);
        streakNumber.setTextureSheet(game_textures.TEX_FONT1);
        streakNumber.setTextSize(mgr.gameMain.text_size);
        streakNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        streakNumber.setBorder(0,0,0,border);
        streakNumber.setBorderColor(0,1,1,0.3f);
        streakNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        streakNumber.setPadding(border);
        
        
        engine_guiButton settings = new engine_guiButton(this, idSettingsButton);
        settings.setText("Settings");
        settings.setTextureSheet(game_textures.TEX_FONT1);
        settings.setTextSize(mgr.gameMain.text_size);
        settings.setBorder(border,border,border,border);
        settings.setBorderColor(0,1,1,0.3f);
        settings.setBackgroundColor(0,0,0,.9f);
        settings.setPadding(border);
        
        engine_guiButton back = new engine_guiButton(this, idAgainButton);
        back.setText("again");
        back.setTextureSheet(game_textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(0,border,border,border/2);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,.9f);
        back.setPadding(border);
        back.setWeight(1.5f, 1);                                                                      //TODO
        
        engine_guiButton quit = new engine_guiButton(this, idLeaveButton);
        quit.setText("leave");
        quit.setTextureSheet(game_textures.TEX_FONT1);
        quit.setTextSize(mgr.gameMain.text_size);
        quit.setBorder(0,border,border/2,border);
        quit.setBorderColor(0,1,1,0.3f);
        quit.setBackgroundColor(0,0,0,.9f);
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

class BooleanGUI extends engine_gui {
    
    private final int idText = 0;
    private final int idYesButton = 1;
    private final int idNoButton = 2;
    
    final int[][] layout1 = {
        { 
            // horizontal, vertical number of GUI elements
            3,2
        }, {
            // GUI element ID's
            NULL,         idText,          NULL,
            idYesButton, NULL,             idNoButton
        }
    };
    
    masterGameReference mgr;
    public BooleanGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    public void populate() {
        setLayout(layout1);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        engine_guiText text = new engine_guiText(this, idText);
        text.setText("PAUSED");
        text.setTextureSheet(game_textures.TEX_FONT1);
        text.setTextSize(mgr.gameMain.text_size);
        text.setTextColor(0,1,0,1);
        text.setBorder(border,border,border,border);
        text.setBorderColor(0,1,1,0.3f);
        text.setBackgroundColor(0,0,0,.9f);
        text.setPadding(border);
        text.setWeight(1, 3);

        engine_guiButton yes = new engine_guiButton(this, idYesButton);
        yes.setText("again");
        yes.setTextureSheet(game_textures.TEX_FONT1);
        yes.setTextSize(mgr.gameMain.text_size);
        yes.setBorder(0,border,border,border/2);
        yes.setBorderColor(0,1,1,0.3f);
        yes.setBackgroundColor(0,0,0,.9f);
        yes.setPadding(border);
        yes.setWeight(1.5f, 1);
        
        engine_guiButton no = new engine_guiButton(this, idNoButton);
        no.setText("leave");
        no.setTextureSheet(game_textures.TEX_FONT1);
        no.setTextSize(mgr.gameMain.text_size);
        no.setBorder(0,border,border/2,border);
        no.setBorderColor(0,1,1,0.3f);
        no.setBackgroundColor(0,0,0,.9f);
        no.setPadding(border);
        
        addElement(text);
        addElement(yes);
        addElement(no);
        
        build();
    }

}