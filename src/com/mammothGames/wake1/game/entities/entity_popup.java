package com.mammothGames.wake1.game.entities;

import android.util.Log;

import com.mammothGames.wake1.gameEngine.*;
import com.mammothGames.wake1.game.*;


public class entity_popup extends engine_entity {

	private boolean popup_open = false;
	private float popup_alpha = 0, popup_alpha_target = 0;
	private boolean game_muted;

	PauseGUI pause_gui;
	BooleanGUI bool_gui;
	SettingsGUI settings_gui;

	masterGameReference mgr;
	public entity_popup(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		this.mgr = mgr;
	}



	public final int STATE_ABANDON = 0;
	public final int STATE_QUIT = 1;
	public final int STATE_ERASE = 2;
	public final int STATE_PAUSED = 3;
	public final int STATE_SETTINGS = 4;





   @Override
   public void sys_firstStep() {
        
        float draw_width = ref.screen_width*3.5f/5;
        float draw_height = draw_width*1.25f;
        float draw_x = ref.screen_width/2;
        float draw_y = ref.screen_height/2;
        
        pause_gui = new PauseGUI(ref, mgr);
        pause_gui.setPosition(draw_x, draw_y);
        pause_gui.setSize(draw_width, draw_height);
        pause_gui.populate();
        pause_gui.setDepth(constants.layer7_overHUD);
        
        bool_gui = new BooleanGUI(ref, mgr);
        bool_gui.setPosition(draw_x, draw_y);
        bool_gui.setSize(draw_width, draw_width);
        bool_gui.populate();
        bool_gui.setDepth(constants.layer7_overHUD);
        
        settings_gui = new SettingsGUI(ref, mgr);
        settings_gui.setPosition(draw_x, draw_y);
        settings_gui.setSize(draw_width, draw_width);
        settings_gui.populate();
        settings_gui.setDepth(constants.layer7_overHUD);
        
        
        // Load weather or not to play music
        String muted = ref.file.load("muted");
        // If first boot, save "not muted"
        if (muted.equals("")) {
            game_muted = false;
            ref.file.save("muted", "" + game_muted);
        } else {
            game_muted = Boolean.parseBoolean(muted);
        }
        ref.sound.setMusicState(game_muted, true, true);
        
        // Set button color in settings menu
        if(game_muted){
            settings_gui.check.setTextColor(1,0,0,1);
            settings_gui.check.setText("OFF");
        } else {
            settings_gui.check.setTextColor(0,1,0,1);
            settings_gui.check.setText("ON");
        }
        
        
    }



	@Override
	public void sys_step() {

	    //Close popup if a touch is outside of the GUI
//	    if (ref.input.get_touch_state(0) == ref.input.TOUCH_HELD) {
//	        if (!ref.collision.point_AABB(pause_gui.getW(), pause_gui.getH(), pause_gui.getX(), pause_gui.getY(), ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
//	            setPopupOpenness(false);
//            }
//	    }

		popup_alpha += (popup_alpha_target - popup_alpha) * 8f * ref.main.time_scale;

		pause_gui.setActive(false);
		bool_gui.setActive(false);
		settings_gui.setActive(false);

        switch (action) {
        
                
            case STATE_QUIT:
                
                bool_gui.setActive(popup_open);
                bool_gui.setAlpha(popup_alpha);
                
                bool_gui.text.setText("Leave game?");
                bool_gui.yes.setTextColor(1, 1, 1, 1);
                bool_gui.yes.setText("Yes");
                bool_gui.no.setText("No");
                
                if (bool_gui.yes.getClicked())
                    quitGame();
                else if (bool_gui.no.getClicked())
                    mgr.popup.setPopupOpenness(false);
                
                break;
                
            case STATE_ERASE:
                bool_gui.setActive(popup_open);
                bool_gui.setAlpha(popup_alpha);
                
                bool_gui.text.setText("Are you sure?");
                bool_gui.yes.setTextColor(1, 0, 0, 1);
                bool_gui.yes.setText("Erase");
                bool_gui.no.setText("Cancel");
                
                if (bool_gui.yes.getClicked()) {
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
                    mgr.popup.setPopupOpenness(false);
                }
                else if (bool_gui.no.getClicked())
                    mgr.popup.setPopupOpenness(false);
                
                break;
                
            case STATE_ABANDON:
                bool_gui.setActive(popup_open);
                bool_gui.setAlpha(popup_alpha);
                
                bool_gui.text.setText("Abandon game?");
                bool_gui.yes.setTextColor(1, 1, 1, 1);
                bool_gui.yes.setText("Yes");
                bool_gui.no.setText("No");
                
                if (bool_gui.yes.getClicked()) {
                    abandonCurrentGame();
                } else if (bool_gui.no.getClicked()) {
                    setPopupState(STATE_PAUSED);
                }
                
                break;
                
            case STATE_PAUSED:
                pause_gui.setActive(popup_open);
                pause_gui.setAlpha(popup_alpha);
                
                pause_gui.title.setText(mgr.gameMain.current_diff_string);
                pause_gui.scoreNumber.setNumber(mgr.gameMain.score);
                pause_gui.streakNumber.setNumber(mgr.gameMain.streak);
                
                if (pause_gui.play.getClicked()) {
                    setPopupOpennessHard(false);
                    mgr.countdown.startUnpauseCountdown();
                    //mgr.menuPauseHUD.setPause(false);
                }
                if (pause_gui.leave.getClicked()) {
                    setPopupState(STATE_ABANDON);
                }
                if (pause_gui.settings.getClicked()) {
                    setPopupState(STATE_SETTINGS);
                }
                
                break;
                
            case STATE_SETTINGS:
                
                settings_gui.setActive(popup_open);
                settings_gui.setAlpha(popup_alpha);
                
                if (settings_gui.back.getClicked()) {
                	if (ref.room.get_current_room() == rooms.ROOM_GAME)
                		setPopupState(STATE_PAUSED);
                	if (ref.room.get_current_room() == rooms.ROOM_MENUMAIN) {
                		setPopupOpenness(false);
                	}
                }
                if (settings_gui.check.getClicked()) {
                    // Flip the muted var, then start the music
                    game_muted = !game_muted;
                    
                    if(game_muted){
                        settings_gui.check.setTextColor(1,0,0,popup_alpha);
                        settings_gui.check.setText("OFF");
                    } else {
                        settings_gui.check.setTextColor(0,1,0,popup_alpha);
                        settings_gui.check.setText("ON");
                    }
                    
                    ref.file.save("muted", "" + game_muted);
                    ref.sound.setMusicState(game_muted, true, true);
                }
                
//                if 
                
                
                break;
                
        }



//		if (ref.input.get_touch_state(0) == ref.input.TOUCH_HELD) {
//		    pause_gui.setPosition(ref.input.get_touch_x(0), ref.input.get_touch_y(0));
//		}

        
		// black veil covering everything under popup
        // duplicated in countdown
		ref.draw.setDrawColor(0,0,0, 0.7f * popup_alpha);
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer7_overHUD);

		pause_gui.update();
		bool_gui.update();
		settings_gui.update();

	}

	public void quitGame() {
	    ref.main.exitApp();
	}
	public void abandonCurrentGame() {
	    setPopupOpenness(false);
        mgr.menuPauseHUD.setPause(false);
        mgr.gameMain.floor_height_target = 0;
        mgr.gameMain.endGame();
	}

	@Override
	public void onScreenSleep() {

	    if(ref.room.get_current_room() == rooms.ROOM_GAME) {
	        setPopupState(STATE_PAUSED);
		    setPopupOpennessHard(true);
	    }
	}

	private int action = 0;

	public void setPopupState(int action) {
		this.action = action;
	}
	public int getPopupState() {
	    return action;
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

class PauseGUI extends engine_gui {
    
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
    public PauseGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    engine_guiNumber scoreNumber, streakNumber;
    engine_guiButton settings, play, leave;
    engine_guiText title;
    
    
    public void populate() {
        setLayout(layout1);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        title = new engine_guiText(this, idTitle);
        title.setText("PAUSED");
        title.setTextureSheet(textures.TEX_FONT1);
        title.setTextSize(mgr.gameMain.text_size);
        title.setTextColor(0,1,0,1);
        title.setBorder(border,border,border,border);
        title.setBorderColor(0,1,1,0.3f);
        title.setBackgroundColor(0,0,0,.9f);
        title.setPadding(border);
        
        engine_guiText score = new engine_guiText(this, idScore);
        score.setText("Score");
        score.setTextureSheet(textures.TEX_FONT1);
        score.setTextSize(mgr.gameMain.text_size);
        score.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        score.setBorder(0,0,border,0);
        score.setBorderColor(0,1,1,0.3f);
        score.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        score.setPadding(border);
        
        scoreNumber = new engine_guiNumber(this, idScoreNumber);
        scoreNumber.setNumber(12345);
        scoreNumber.setTextureSheet(textures.TEX_FONT1);
        scoreNumber.setTextSize(mgr.gameMain.text_size);
        scoreNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        scoreNumber.setBorder(0,0,0,border);
        scoreNumber.setBorderColor(0,1,1,0.3f);
        scoreNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        scoreNumber.setPadding(border);
        
        
        engine_guiText streak = new engine_guiText(this, idStreak);
        streak.setText("Streak");
        streak.setTextureSheet(textures.TEX_FONT1);
        streak.setTextSize(mgr.gameMain.text_size);
        streak.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        streak.setBorder(0,0,border,0);
        streak.setBorderColor(0,1,1,0.3f);
        streak.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        streak.setPadding(border);
        
        streakNumber = new engine_guiNumber(this, idStreakNumber);
        streakNumber.setNumber(3210);
        streakNumber.setTextureSheet(textures.TEX_FONT1);
        streakNumber.setTextSize(mgr.gameMain.text_size);
        streakNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        streakNumber.setBorder(0,0,0,border);
        streakNumber.setBorderColor(0,1,1,0.3f);
        streakNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        streakNumber.setPadding(border);
        
        settings = new engine_guiButton(this, idSettingsButton);
        settings.setText("Settings");
        settings.setTextureSheet(textures.TEX_FONT1);
        settings.setTextSize(mgr.gameMain.text_size);
        settings.setBorder(border,border,border,border);
        settings.setBorderColor(0,1,1,0.3f);
        settings.setBackgroundColor(0,0,0,.9f);
        settings.setPadding(border);
        
        play = new engine_guiButton(this, idAgainButton);
        play.setText("play");
        play.setTextureSheet(textures.TEX_FONT1);
        play.setTextSize(mgr.gameMain.text_size);
        play.setBorder(0,border,border,border/2);
        play.setBorderColor(0,1,1,0.3f);
        play.setBackgroundColor(0,0,0,.9f);
        play.setPadding(border);
        
        leave = new engine_guiButton(this, idLeaveButton);
        leave.setText("leave");
        leave.setTextureSheet(textures.TEX_FONT1);
        leave.setTextSize(mgr.gameMain.text_size);
        leave.setBorder(0,border,border/2,border);
        leave.setBorderColor(0,1,1,0.3f);
        leave.setBackgroundColor(0,0,0,.9f);
        leave.setPadding(border);
        
        
        
        addElement(title);
        addElement(score);
        addElement(scoreNumber);
        addElement(streak);
        addElement(streakNumber);
        addElement(settings);
        addElement(play);
        addElement(leave);
        
        build();
    }

}

class BooleanGUI extends engine_gui {
    
    private final int idText = 0;
    private final int idYesButton = 1;
    private final int idNoButton = 2;
    
    engine_guiText text;
    engine_guiButton yes, no;
    
    final int[][] layout1 = {
        { 
            // horizontal, vertical number of GUI elements
            2,2
        }, {
            // GUI element ID's
            NULL,        idText,
            idYesButton, idNoButton
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
        
        text = new engine_guiText(this, idText);
        text.setText("PAUSED");
        text.setTextureSheet(textures.TEX_FONT1);
        text.setTextSize(mgr.gameMain.text_size);
        text.setTextColor(1,1,1,1);
        text.setBorder(border,border,border,border);
        text.setBorderColor(0,1,1,0.3f);
        text.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        text.setPadding(border);
        text.setWeight(1, 3);

        yes = new engine_guiButton(this, idYesButton);
        yes.setText("again");
        yes.setTextureSheet(textures.TEX_FONT1);
        yes.setTextSize(mgr.gameMain.text_size);
        yes.setBorder(0,border,border,border/2);
        yes.setBorderColor(0,1,1,0.3f);
        yes.setBackgroundColor(0,0,0,.9f);
        yes.setPadding(border);
        
        no = new engine_guiButton(this, idNoButton);
        no.setText("leave");
        no.setTextureSheet(textures.TEX_FONT1);
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

class SettingsGUI extends engine_gui {
    
    private final int idTitle = 0;
    private final int idMusic = 1;
    private final int idCheckbox = 2;
    private final int idBackButton = 3;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            2,3
        }, {
            // GUI element ID's
            NULL,    idTitle,
            idMusic, idCheckbox,
            NULL,    idBackButton,
        }
    };
    
    masterGameReference mgr;
    public SettingsGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    engine_guiButton back, check;
    
    
    public void populate() {
        setLayout(layout);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        engine_guiText title = new engine_guiText(this, idTitle);
        title.setText("SETTINGS");
        title.setTextureSheet(textures.TEX_FONT1);
        title.setTextSize(mgr.gameMain.text_size);
        title.setTextColor(0,1,0,1);
        title.setBorder(border,border,border,border);
        title.setBorderColor(0,1,1,0.3f);
        title.setBackgroundColor(0,0,0,.9f);
        title.setPadding(border);
        
        engine_guiText music = new engine_guiText(this, idMusic);
        music.setText("Music");
        music.setTextureSheet(textures.TEX_FONT1);
        music.setTextSize(mgr.gameMain.text_size);
        music.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        music.setBorder(0,0,border,0);
        music.setBorderColor(0,1,1,0.3f);
        music.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        music.setPadding(border);
        music.setWeight(1, 2);
        
        check = new engine_guiButton(this, idCheckbox);
        check.setTextureSheet(textures.TEX_FONT1);
        check.setTextSize(mgr.gameMain.text_size);
        check.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        check.setBorder(0,0,0,border);
        check.setBorderColor(0,1,1,0.3f);
        check.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        check.setPadding(border);
        check.setActOnHover(false);
        
        back = new engine_guiButton(this, idBackButton);
        back.setText("back");
        back.setTextureSheet(textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(border,border,border,border);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,.9f);
        back.setPadding(border);
        
        
        addElement(title);
        addElement(check);
        addElement(music);
        addElement(back);
        
        build();
    }

}