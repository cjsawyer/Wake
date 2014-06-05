package com.mammothGames.wake1.game.entities;

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
	
	private MainMenuGUI menu;
	private DifficultyMenuGUI difficulty;
	private RecordsMenuGUI records;
	private AboutMenuGUI about;
	
	private final int MAIN = 0, DIFFICULTY = 1, RECORDS = 2, ABOUT = 3;
	private int active_screen;
	private float x, y, xtarget = 0, ytarget = 0;
	private float X_MAIN, Y_MAIN, X_DIFF, Y_DIFF, X_REC, Y_REC, X_ABOUT, Y_ABOUT;
	
	@Override
	public void sys_firstStep() {
		
		x = ref.screen_width/2;
		y = ref.screen_height/2;
		
		xtarget = x;
		ytarget = y;
		
		X_MAIN = 0;
		Y_MAIN = 0;
		
		X_DIFF = 0;
		Y_DIFF = -ref.screen_height;
		
		X_REC = -ref.screen_width;
		Y_REC = 0;
		
		X_ABOUT = ref.screen_width;
		Y_ABOUT = 0;
		
		
		active_screen = MAIN;
		
		
		menu = new MainMenuGUI(ref,mgr);
		menu.setSize(
				ref.screen_width - 2*mgr.gameMain.padding_x,
				ref.screen_height - mgr.gameMain.padding_y);
		menu.setDepth(constants.layer6_HUD);
		menu.populate();
		menu.setActive(true);
		
		difficulty = new DifficultyMenuGUI(ref,mgr);
		difficulty.setSize(
				ref.screen_width - 2*mgr.gameMain.padding_x,
				ref.screen_height - mgr.gameMain.padding_y);
		difficulty.setDepth(constants.layer6_HUD);
		difficulty.populate();
		difficulty.setActive(true);
		
		records = new RecordsMenuGUI(ref,mgr);
		records.setSize(
				ref.screen_width - 2*mgr.gameMain.padding_x,
				ref.screen_height - mgr.gameMain.padding_y);
		records.setDepth(constants.layer6_HUD);
		records.populate();
		records.setActive(true);
		
		about = new AboutMenuGUI(ref,mgr);
		about.setSize(
				ref.screen_width - 2*mgr.gameMain.padding_x,
				ref.screen_height - mgr.gameMain.padding_y);
		about.setDepth(constants.layer6_HUD);
		about.populate();
		about.setActive(true);
	}
	
	@Override
	public void sys_step() {
		if (ref.room.get_current_room() == rooms.ROOM_MENUMAIN) {
			
			x += (xtarget - x) * mgr.gameMain.ANIMATION_SCALE * ref.main.time_scale;
			y += (ytarget - y) * mgr.gameMain.ANIMATION_SCALE * ref.main.time_scale;
			
			menu.setClickable(false);//TODO fix the fxn
			difficulty.setClickable(false);
			records.setClickable(false);
			about.setClickable(false);
			
			switch(active_screen) {
				case MAIN:
					setRelativePositionTarget(-X_MAIN, -Y_MAIN);
					
					menu.setClickable(!mgr.popup.getPopupOpenness());
					
					if (menu.play.getClicked())
						active_screen = DIFFICULTY;
					
					if (menu.records.getClicked()) 
						active_screen = RECORDS;
					
					if (menu.settings.getClicked()) {
						mgr.popup.setPopupState(mgr.popup.STATE_SETTINGS);
						mgr.popup.setPopupOpenness(true);
					}
					
					if (menu.about.getClicked())
						active_screen = ABOUT;
					
					
					break;
				case DIFFICULTY:
					
					difficulty.setClickable(true);
					
					if (difficulty.back.getClicked())
						active_screen = MAIN; // TODO: build rest of this
					
					setRelativePositionTarget(-X_DIFF, -Y_DIFF);
					
					break;
				case RECORDS:
					
					records.setClickable(!mgr.popup.getPopupOpenness());
					
					if (records.back.getClicked())
						active_screen = MAIN; // TODO: build rest of this
					
					setRelativePositionTarget(-X_REC, -Y_REC);
					
					break;
				case ABOUT:
					
					about.setClickable(true);
					
					if (about.back.getClicked())
						active_screen = MAIN; // TODO: build rest of this
					
					setRelativePositionTarget(-X_ABOUT, -Y_ABOUT);
					
					break;
			}
			
			menu.setAlpha(mgr.gameMain.shade_alpha);
			menu.setPosition(x + X_MAIN, y + Y_MAIN);
			menu.update();
			
			difficulty.setAlpha(mgr.gameMain.shade_alpha);
			difficulty.setPosition(x + X_DIFF, y + Y_DIFF);
			difficulty.update();
			
			records.setAlpha(mgr.gameMain.shade_alpha);
			records.setPosition(x + X_REC, y + Y_REC);
			records.update();
			
			about.setAlpha(mgr.gameMain.shade_alpha);
			about.setPosition(x + X_ABOUT, y + Y_ABOUT);
			about.update();
			
		}
	}
	
	
	public void start() {
		mgr.gameMain.floor_height_target = 0; // Make the water go back down
		mgr.gameMain.shade_alpha = 0;
		mgr.gameMain.shade_alpha_target = 1;
		ref.room.changeRoom(rooms.ROOM_MENUMAIN);
	}

	private void setRelativePositionTarget(float xt, float yt) {
		xtarget = xt + ref.screen_width/2;
		ytarget = yt + ref.screen_height/2;
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

class RecordsMenuGUI extends engine_gui {
    
    private final int idBack = 0;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            1,1
        }, {
            // GUI element ID's
            idBack,
        }
    };
    
    masterGameReference mgr;
    public RecordsMenuGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
//    engine_guiButton play, settings, records, about;
    engine_guiButton back;
    
    
    public void populate() {
        setLayout(layout);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        back = new engine_guiButton(this, idBack);
        back.setText("BACKrecords");
        back.setTextureSheet(textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(border);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,.9f);
        back.setMargin(border,border,0,0);
        
        addElement(back);
        
        build();
    }

}

class DifficultyMenuGUI extends engine_gui {
    
    private final int idBack = 0;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            1,1
        }, {
            // GUI element ID's
            idBack,
        }
    };
    
    masterGameReference mgr;
    public DifficultyMenuGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
//    engine_guiButton play, settings, records, about;
    engine_guiButton back;
    
    
    public void populate() {
        setLayout(layout);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        back = new engine_guiButton(this, idBack);
        back.setText("BACKdifff");
        back.setTextureSheet(textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(border);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,.9f);
        back.setMargin(border,border,0,0);
        
        addElement(back);
        
        build();
    }

}

class AboutMenuGUI extends engine_gui {
    
    private final int idBack = 0;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            1,1
        }, {
            // GUI element ID's
            idBack,
        }
    };
    
    masterGameReference mgr;
    public AboutMenuGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
//    engine_guiButton play, settings, records, about;
    engine_guiButton back;
    
    
    public void populate() {
        setLayout(layout);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        back = new engine_guiButton(this, idBack);
        back.setText("BACKabout");
        back.setTextureSheet(textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(border);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,.9f);
        back.setMargin(border,border,0,0);
        
        addElement(back);
        
        build();
    }

}