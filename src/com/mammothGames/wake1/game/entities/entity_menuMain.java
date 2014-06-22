package com.mammothGames.wake1.game.entities;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

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
	public float x, y;
	private float xtarget = 0, ytarget = 0;
	private float X_MAIN, Y_MAIN, X_DIFF, Y_DIFF, X_REC, Y_REC, X_ABOUT, Y_ABOUT;
	
	private int tab = 0;
	private int last_tab = -1;
	
	// c/p from gameMain
	final String SCO_E = "SCO_E";
	final String SCO_M = "SCO_M";
	final String SCO_H = "SCO_H";
	final String SCO_HE = "SCO_HE";
	
	final String STR_E = "STR_E";
	final String STR_M = "STR_M";
	final String STR_H = "STR_H";
	final String STR_HE = "STR_HE";
	
	final String PLY_E = "PLY_E";
	final String PLY_M = "PLY_M";
	final String PLY_H = "PLY_H";
	final String PLY_HE = "PLY_HE";
	
	private String STR, SCO, PLY;
	
	int high_score, best_streak, games_played;
	
	@Override
	public void sys_firstStep() {
		
		x = ref.screen_width/2;
		y = ref.screen_height/2;
		
		xtarget = x;
		ytarget = y;
		
		float hyp = ref.screen_height; //(float) Math.sqrt(ref.screen_width*ref.screen_width+ref.screen_height*ref.screen_height); 
		
		X_MAIN = 0;
		Y_MAIN = 0;
		
		X_DIFF = 0;
		Y_DIFF = hyp;
		
		X_REC = hyp * (float)Math.cos(210*Math.PI/180f);
		Y_REC = hyp * (float)Math.sin(210*Math.PI/180f);
		
		X_ABOUT = hyp * (float)Math.cos(330*Math.PI/180f);
		Y_ABOUT = hyp * (float)Math.sin(330*Math.PI/180f);
		
		
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
		
		loadScores();
		
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
			
			menu.setClickable(false);
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
					
					if (difficulty.back.getClicked()) {
						mgr.gameMain.setDifficulty(mgr.gameMain.DIF_HARD);
						mgr.gameMain.startGame();
					}
					
					setRelativePositionTarget(-X_DIFF, -Y_DIFF);
					
					break;
				case RECORDS:
					
					records.setClickable(!mgr.popup.getPopupOpenness());
					
					int previous_tab = tab;
					tab = records.tabs.getActiveTab();
					
					if (previous_tab != tab) {
						loadScores();
					}
					
					
					if (records.back.getClicked())
						active_screen = MAIN;
					
					if (records.rate.getClicked())
						ratePlayStore();
						
					if (records.pro.getClicked())
						getProPlayStore();
						
					if (records.erase.getClicked())
						showErasePopup();
					
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
	
	public void loadScores() {
		// Load high scores
		
		switch(tab) {
			case 0:
				SCO  = SCO_E;
				STR  = STR_E;
				PLY = PLY_E;
				records.diff.setText("EASY");
				break;
			case 1:
				SCO  = SCO_M;
				STR  = STR_M;
				PLY = PLY_M;
				records.diff.setText("MEDIUM");
				break;
			case 2:
				SCO  = SCO_H;
				STR  = STR_H;
				PLY = PLY_H;
				records.diff.setText("HARD");
				break;
			case 3:
				SCO  = SCO_HE;
				STR  = STR_HE;
				PLY = PLY_HE;
				records.diff.setText("HELL");
				break;
		}
		
		String high_score_string = ref.file.load(SCO);
		if (high_score_string.equals("")) {
			high_score = 0;
		} else {
			high_score = Integer.parseInt(ref.file.load(SCO)); 
		}
		String best_streak_string = ref.file.load(STR);
		if (best_streak_string.equals("")) {
			best_streak = 0;
		} else {
			best_streak = Integer.parseInt(ref.file.load(STR)); 
		}
		String games_played_string = ref.file.load(PLY);
		if (games_played_string.equals("")) {
			games_played = 0;
		} else {
			games_played = Integer.parseInt(ref.file.load(PLY)); 
		}
		
		records.hsn.setNumber(high_score);
		records.bsn.setNumber(best_streak);
		records.gpn.setNumber(games_played);
		records.diff.setDifficulty(tab);
		
		
	}
	
	private void showErasePopup() {
		mgr.popup.setPopupState(mgr.popup.STATE_ERASE);
		mgr.popup.setPopupOpenness(true);
	}
	
	private void ratePlayStore() {
		final String appPackageName = ref.android.getPackageName();
		try {
			ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}
	
	private void getProPlayStore() {
		final String appPackageName = "com.mammothGames.wake1"; // getPackageName() from Context or Activity object
		try {
			ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
			ref.android.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}

}


class MainMenuGUI extends engine_gui {
	    
    private final int idLogo = 0;
    private final int iPlayButton = 1;
    private final int idRecordsButton = 2;
    private final int idAboutButton = 3;
    private final int idSettingsButton = 4;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            1,5
        }, {
            // GUI element ID's
            idLogo,
            iPlayButton,
            idRecordsButton,
            idAboutButton,
            idSettingsButton,
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
        
        about = new engine_guiButton(this, idAboutButton);
        about.setText("about");
        about.setTextureSheet(textures.TEX_FONT1);
        about.setTextSize(mgr.gameMain.text_size);
        about.setBorder(border);
        about.setBorderColor(0,1,1,0.3f);
        about.setBackgroundColor(0,0,0,.9f);
        about.setMargin(border,border,0,0);
        
        settings = new engine_guiButton(this, idSettingsButton);
        settings.setText("settings");
        settings.setTextureSheet(textures.TEX_FONT1);
        settings.setTextSize(mgr.gameMain.text_size);
        settings.setBorder(border);
        settings.setBorderColor(0,1,1,0.3f);
        settings.setBackgroundColor(0,0,0,.9f);
        settings.setMargin(border,border,0,0);
        

        
        addElement(logo);
        addElement(play);
        addElement(records);
        addElement(about);
        addElement(settings);
        
        build();
    }

}

class RecordsMenuGUI extends engine_gui {
    
	private final int idTabs = 0;
    private final int idDiff = 1;
    private final int idHS = 2;
    private final int idHSN = 3;
    private final int idBS = 4;
    private final int idBSN = 5;
    private final int idGP = 6;
    private final int idGPN = 7;
    private final int idErase = 8;
    private final int idRate = 9;
    private final int idPro = 10;
    private final int idBack =11;
    
    final int[][] layout = {
        { 
            // horizontal, vertical number of GUI elements
            2,9
        }, {
            // GUI element ID's
        	idTabs, NULL,
        	idDiff, NULL,
        	idHS, idHSN,
        	idBS, idBSN,
        	idGP, idGPN,
        	idErase, NULL,
        	idRate, NULL,
        	idPro, NULL,
        	idBack, NULL
        }
    };
    
    masterGameReference mgr;
    public RecordsMenuGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    engine_guiButton back, erase, rate, pro;
    engine_guiText hs, bs, gp;
    guiDifficultyText diff; 
    engine_guiNumber hsn, bsn, gpn;
    engine_guiTabGroup tabs;
    
    
    public void populate() {
        setLayout(layout);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        
        tabs = new engine_guiTabGroup(this, idTabs, 4);
        tabs.setTabString(0, "E"); tabs.setTabStringColor(0, 1, 1, 1, 1);
        tabs.setTabString(1, "M"); tabs.setTabStringColor(1, 1, 1, 1, 1);
        tabs.setTabString(2, "H"); tabs.setTabStringColor(2, 1, 1, 1, 1);
        tabs.setTabString(3, "H"); tabs.setTabStringColor(3, 1, 0, 0, 1);
        tabs.setTextureSheet(textures.TEX_FONT1);
        tabs.setTextSize(mgr.gameMain.text_size);
        tabs.setBorder(border);
        tabs.setBorderColor(0,1,1,0.3f);
        tabs.setBackgroundColor(0,0,0,.9f);
        tabs.setInactiveBackgroundColor(0.12f, 0.12f, 0.12f, 0.9f);
        addElement(tabs);
        
        diff = new guiDifficultyText(this, idDiff, mgr);
        diff.setBorderColor(0,1,1,0.3f);
        diff.setBackgroundColor(0,0,0,.9f);
        diff.setTextureSheet(textures.TEX_FONT1);
        diff.setTextSize(mgr.gameMain.text_size);
        diff.setText("Diff");
        diff.setBorder(0, 0, border, border);
        diff.setWeight(1, 0.75f);
        addElement(diff);
        
        hs = new engine_guiText(this, idHS);
        hs.setBorderColor(0,1,1,0.3f);
        hs.setBackgroundColor(0,0,0,.9f);
        hs.setTextureSheet(textures.TEX_FONT1);
        hs.setTextSize(mgr.gameMain.text_size);
        hs.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        hs.setText("High Score");
        hs.setWeight(4, 0.75f);
        hs.setBorder(0,0,border,0);
        hs.setPadding(0,border,border,0);
        addElement(hs);
        
        bs = new engine_guiText(this, idBS);
        bs.setBorderColor(0,1,1,0.3f);
        bs.setBackgroundColor(0,0,0,.9f);
        bs.setTextureSheet(textures.TEX_FONT1);
        bs.setTextSize(mgr.gameMain.text_size);
        bs.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        bs.setText("Best Streak");
        bs.setWeight(4, 0.75f);
        bs.setBorder(0,0,border,0);
        bs.setPadding(0,border,border,0);
        addElement(bs);
        
        gp = new engine_guiText(this, idGP);
        gp.setBorderColor(0,1,1,0.3f);
        gp.setBackgroundColor(0,0,0,.9f);
        gp.setTextureSheet(textures.TEX_FONT1);
        gp.setTextSize(mgr.gameMain.text_size);
        gp.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        gp.setText("Games Played");
        gp.setWeight(4, 0.75f);
        gp.setBorder(0,border,border,0);
        gp.setPadding(0,border,border,0);
        addElement(gp);
        
        hsn = new engine_guiNumber(this, idHSN);
        hsn.setBorderColor(0,1,1,0.3f);
        hsn.setBackgroundColor(0,0,0,.9f);
        hsn.setTextureSheet(textures.TEX_FONT1);
        hsn.setTextSize(mgr.gameMain.text_size);
        hsn.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        hsn.setBorder(0,0,0,border);
        hsn.setPadding(0,border,0,border);
        hsn.setWeight(1, 0.75f);
        addElement(hsn);
        
        bsn = new engine_guiNumber(this, idBSN);
        bsn.setBorderColor(0,1,1,0.3f);
        bsn.setBackgroundColor(0,0,0,.9f);
        bsn.setTextureSheet(textures.TEX_FONT1);
        bsn.setTextSize(mgr.gameMain.text_size);
        bsn.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        bsn.setBorder(0,0,0,border);
        bsn.setPadding(0,border,0,border);
        bsn.setWeight(1, 0.75f);
        addElement(bsn);
        
        gpn = new engine_guiNumber(this, idGPN);
        gpn.setBorderColor(0,1,1,0.3f);
        gpn.setBackgroundColor(0,0,0,.9f);
        gpn.setTextureSheet(textures.TEX_FONT1);
        gpn.setTextSize(mgr.gameMain.text_size);
        gpn.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        gpn.setBorder(0,border,0,border);
        gpn.setPadding(0,border,0,border);
        gpn.setWeight(1, 0.75f);
        addElement(gpn);
        
        
        erase = new engine_guiButton(this, idErase);
        erase.setText("erase");
        erase.setTextureSheet(textures.TEX_FONT1);
        erase.setTextSize(mgr.gameMain.text_size);
        erase.setBorder(border);
        erase.setBorderColor(0,1,1,0.3f);
        erase.setBackgroundColor(0,0,0,.9f);
        erase.setMargin(border,border/2,0,0);
        addElement(erase);
        
        rate = new engine_guiButton(this, idRate);
        rate.setText("rate");
        rate.setTextureSheet(textures.TEX_FONT1);
        rate.setTextSize(mgr.gameMain.text_size);
        rate.setBorder(border);
        rate.setBorderColor(0,1,1,0.3f);
        rate.setBackgroundColor(0,0,0,.9f);
        rate.setMargin(border/2,border/2,0,0);
        addElement(rate);
        
        pro = new engine_guiButton(this, idPro);
        pro.setText("pro");
        pro.setTextureSheet(textures.TEX_FONT1);
        pro.setTextSize(mgr.gameMain.text_size);
        pro.setBorder(border);
        pro.setBorderColor(0,1,1,0.3f);
        pro.setBackgroundColor(0,0,0,.9f);
        pro.setMargin(border/2,border/2,0,0);
        addElement(pro);
        
        back = new engine_guiButton(this, idBack);
        back.setText("BACKrecords");
        back.setTextureSheet(textures.TEX_FONT1);
        back.setTextSize(mgr.gameMain.text_size);
        back.setBorder(border);
        back.setBorderColor(0,1,1,0.3f);
        back.setBackgroundColor(0,0,0,.9f);
        back.setMargin(border/2,0,0,0);
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