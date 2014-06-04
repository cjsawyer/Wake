package com.mammothGames.wake1.game.entities;

import android.hardware.Camera.Area;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_menuMain extends engine_entity {

	masterGameReference mgr;
	public entity_menuMain(masterGameReference mgr) {
		this.mgr = mgr;
		persistent = true;
		pausable = false;
	}
	
	MainMenuGUI menu;
	
	@Override
	public void sys_firstStep() {
		menu = new MainMenuGUI(ref,mgr);
		menu.setPosition(ref.screen_width/2, ref.screen_height/2);
		menu.setSize(
				ref.screen_width - 2*mgr.gameMain.padding_x,
				ref.screen_height - mgr.gameMain.padding_y);
		menu.setDepth(constants.layer6_HUD);
		menu.populate();
		menu.setActive(true);
		menu.setAlpha(1);
	}
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == rooms.ROOM_MENUMAIN) {
			
			
			if (mgr.popup.getPopupOpenness()) {
			}
			
			menu.update();
			
			if (menu.settings.getClicked()) {
				mgr.popup.setPopupState(mgr.popup.STATE_SETTINGS);
				mgr.popup.setPopupOpenness(true);
			}
		}
	}
	
	
	public void start() {
		mgr.gameMain.floor_height_target = 0; // Make the water go back down
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
		ref.room.changeRoom(rooms.ROOM_MENUMAIN);
	}
	
}

class MainMenuGUI extends engine_gui {
	    
    private final int idLogo = 0;
    private final int iPlayButton = 1;
    private final int idRecordsButton = 2;
    private final int idSettingsButton = 3;
    private final int idAboutButton = 4;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            1,5
        }, {
            // GUI element ID's
            idLogo,
            iPlayButton,
            idRecordsButton,
            idSettingsButton,
            idAboutButton,
        }
    };
    
    masterGameReference mgr;
    public MainMenuGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    engine_guiButton play, settings, records, about;
    
    
    public void populate() {
        setLayout(layout);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        engine_guiImage logo = new engine_guiImage(this, idLogo);
        logo.setTexture(textures.TEX_SPRITES, textures.SUB_LOGO);
        logo.setTextureSheet(textures.TEX_FONT1);
        logo.setScale(0.9f);
        logo.setWeight(1, 2);
        
        play = new engine_guiButton(this, iPlayButton);
        play.setText("Play");
        play.setTextureSheet(textures.TEX_FONT1);
        play.setTextSize(mgr.gameMain.text_size);
        play.setBorder(border);
        play.setBorderColor(0,1,1,0.3f);
        play.setBackgroundColor(0,0,0,.9f);
        play.setMargin(border,border,0,0);
        
        records = new engine_guiButton(this, idRecordsButton);
        records.setText("Records");
        records.setTextureSheet(textures.TEX_FONT1);
        records.setTextSize(mgr.gameMain.text_size);
        records.setBorder(border);
        records.setBorderColor(0,1,1,0.3f);
        records.setBackgroundColor(0,0,0,.9f);
        records.setMargin(border,border,0,0);
        
        settings = new engine_guiButton(this, idSettingsButton);
        settings.setText("settings");
        settings.setTextureSheet(textures.TEX_FONT1);
        settings.setTextSize(mgr.gameMain.text_size);
        settings.setBorder(border);
        settings.setBorderColor(0,1,1,0.3f);
        settings.setBackgroundColor(0,0,0,.9f);
        settings.setMargin(border,border,0,0);
        
        about = new engine_guiButton(this, idAboutButton);
        about.setText("about");
        about.setTextureSheet(textures.TEX_FONT1);
        about.setTextSize(mgr.gameMain.text_size);
        about.setBorder(border);
        about.setBorderColor(0,1,1,0.3f);
        about.setBackgroundColor(0,0,0,.9f);
        about.setMargin(border,border,0,0);
        
        
        addElement(logo);
        addElement(play);
        addElement(records);
        addElement(settings);
        addElement(about);
        
        build();
    }

}