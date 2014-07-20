package com.mammothGames.wake1.game.entities;

import android.os.SystemClock;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;
import com.mammothGames.wake1.game.textures;


public class entity_menuPostGame extends engine_entity {

	masterGameReference mgr;
	public entity_menuPostGame(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;
		
		this.mgr = mgr;
	}
	
	private boolean fade_out;
	public PostGUI post_gui;
	
	private float games_before_next_ad = 0.00001f; // low number to show ad after the first game
	
	@Override
	public void sys_firstStep() {
		post_gui = new PostGUI(ref, mgr);
        post_gui.setPosition(ref.screen_width/2, ref.screen_height/2);
        post_gui.setSize(ref.screen_width - mgr.gameMain.padding_x*2, ref.screen_height - mgr.gameMain.padding_y);
        post_gui.populate();
        post_gui.setDepth(constants.layer7_overHUD);
        post_gui.setActive(true);
        post_gui.setAlpha(0);
        ref.ad.loadInterstitialAd();
	}
	
	
	@Override
	public void sys_step() {
		
		if (ref.room.get_current_room() == rooms.ROOM_POSTGAME) {
			
			// Done fading out, so go to the main menu
			if ( (mgr.gameMain.shade_alpha < 0.02f) && fade_out ) {
				leave();
			}

			post_gui.scoreNumber.setNumber(mgr.gameMain.score);
			post_gui.streakNumber.setNumber(mgr.gameMain.best_points_streak_this_game);
			post_gui.highScoreNumber.setNumber(mgr.gameMain.high_score);
			post_gui.bestStreakNumber.setNumber(mgr.gameMain.best_points_streak);
			
			
			if (mgr.gameMain.shade_alpha > 0.98f) {
				
				if (post_gui.diff.getClicked()) {
					prepLeave(room_diff);
				}
				if (post_gui.play.getClicked()) {
					prepLeave(room_game);
				}
				if (post_gui.records.getClicked()) {
					prepLeave(room_records);
				}
				
			}
			
			// For blinking high score text
			float blink_alpha = ((float)Math.sin((float)(SystemClock.uptimeMillis() * mgr.menuFirst.DEG_TO_RAD / 500f * 180f))) + 1;
			blink_alpha = blink_alpha > 1 ? 1:blink_alpha;
			
			if (mgr.gameMain.new_high_score) {
				post_gui.scoreNumber.setTextColor(0, 1, 1, blink_alpha);
				post_gui.highScoreNumber.setTextColor(0, 1, 1, blink_alpha);
			} else {
				post_gui.scoreNumber.setTextColor(1, 1, 1, 1);
				post_gui.highScoreNumber.setTextColor(1, 1, 1, 1);
			}
			
			if (mgr.gameMain.new_best_streak) {
				post_gui.streakNumber.setTextColor(0, 1, 1, blink_alpha);
				post_gui.bestStreakNumber.setTextColor(0, 1, 1, blink_alpha);
			} else {
				post_gui.streakNumber.setTextColor(1, 1, 1, 1);
				post_gui.bestStreakNumber.setTextColor(1, 1, 1, 1);
			}
			
			
			post_gui.setAlpha(mgr.gameMain.shade_alpha);
			post_gui.update();
			
		}
	}
	
	private final int room_game=0, room_records=1, room_diff=2;
	private int room_to_leave_to;
	
	public void start() {
		
		switch(mgr.gameMain.current_diff) {
			case (entity_gameMain.DIF_EASY):
				games_before_next_ad -= 1/2f;
				break;
			case (entity_gameMain.DIF_MEDIUM):
				games_before_next_ad -= 1/2f;
				break;
			case (entity_gameMain.DIF_HARD):
				games_before_next_ad -= 1/3f;
				break;
			case (entity_gameMain.DIF_HELL):
				games_before_next_ad -= 1/5f;
				break;
		}
		
		
		mgr.gameMain.floor_height_target = 0;
		mgr.gameMain.shade_alpha_target = 1;
		fade_out = false;
		ref.room.changeRoom(rooms.ROOM_POSTGAME);
		
		mgr.gameMain.game_running = false;
		
	}
	
	
	public void prepLeave(int room_to_leave_to) {
		mgr.gameMain.shade_alpha_target = 0;
		fade_out = true;
		mgr.menuPauseHUD.leaving_post = true;
		
		this.room_to_leave_to = room_to_leave_to;  
	}
	
	private void leave() {
		
		if (games_before_next_ad <= 0) {
			games_before_next_ad = 1;
			ref.ad.showInterstitialAd();
		}
		
		switch (room_to_leave_to){
			case room_game:
				mgr.countdown.startCountdown();
				break;
			case room_records:
				mgr.menuMain.setRecordsPositionHard();
				mgr.menuMain.start();
				break;
			case room_diff:
				mgr.menuMain.setDifficultyPositionHard();
				mgr.menuMain.start();
				break;
		}
	
		mgr.menuPauseHUD.leaving_post = false;
		
	}
	
}

class PostGUI extends engine_gui {
    
    private final int idDiff = 0;
    private final int idScore = 1;
    private final int idScoreNumber = 2;
    private final int idStreak = 3;
    private final int idStreakNumber = 4;
    
    private final int idHighScore = 5;
    private final int idHighScoreNumber = 6;
    private final int idBestStreak = 7;
    private final int idBestStreakNumber = 8;
    
    private final int idRecordsButton = 9;
    private final int idChangeDiffButton = 10;
    private final int idPlayAgainButton = 11;
    
    final int[][] layout1 = {
        { 
            // horizontal, vertical number of GUI elements
            2,8
        }, {
            // GUI element ID's
            NULL, idDiff,
            idScore, idScoreNumber,
            idStreak, idStreakNumber,
            idHighScore, idHighScoreNumber,
            idBestStreak, idBestStreakNumber,
            NULL, idRecordsButton,
            NULL, idChangeDiffButton,
            NULL, idPlayAgainButton,
        }
    };
    
    masterGameReference mgr;
    public PostGUI(engine_reference ref, masterGameReference mgr) {
        super(ref);
        this.mgr = mgr;
    }
    
    engine_guiNumber scoreNumber, streakNumber, highScoreNumber, bestStreakNumber;
    engine_guiButton records, diff, play;
    guiDifficultyText title;
    
    
    public void populate() {
        setLayout(layout1);
        
        float border = mgr.menuDifficulty.button_border_size/2;
        
        title = new guiDifficultyText(this, idDiff, mgr);
        title.setTextureSheet(textures.TEX_FONT1);
        title.setTextSize(mgr.gameMain.text_size);
        title.setBorder(border,border,border,border);
        title.setBorderColor(1,1,1,0.3f);
        title.setBackgroundColor(0,0,0,.9f);
        title.setPadding(border);
        
        engine_guiText score = new engine_guiText(this, idScore);
        score.setText("Score");
        score.setTextureSheet(textures.TEX_FONT1);
        score.setTextSize(mgr.gameMain.text_size);
        score.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        score.setBorder(0,0,border,0);
        score.setBorderColor(1,1,1,0.3f);
        score.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        score.setPadding(border);
        
        scoreNumber = new engine_guiNumber(this, idScoreNumber);
        scoreNumber.setNumber(000);
        scoreNumber.setTextureSheet(textures.TEX_FONT1);
        scoreNumber.setTextSize(mgr.gameMain.text_size);
        scoreNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        scoreNumber.setBorder(0,0,0,border);
        scoreNumber.setBorderColor(1,1,1,0.3f);
        scoreNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        scoreNumber.setPadding(border);
        
        
        engine_guiText streak = new engine_guiText(this, idStreak);
        streak.setText("Streak");
        streak.setTextureSheet(textures.TEX_FONT1);
        streak.setTextSize(mgr.gameMain.text_size);
        streak.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        streak.setBorder(0,border/2,border,0);
        streak.setBorderColor(1,1,1,0.3f);
        streak.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        streak.setPadding(border);
        
        streakNumber = new engine_guiNumber(this, idStreakNumber);
        streakNumber.setNumber(3210);
        streakNumber.setTextureSheet(textures.TEX_FONT1);
        streakNumber.setTextSize(mgr.gameMain.text_size);
        streakNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        streakNumber.setBorder(0,border/2,0,border);
        streakNumber.setBorderColor(1,1,1,0.3f);
        streakNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        streakNumber.setPadding(border);
        
        engine_guiText highScore = new engine_guiText(this, idHighScore);
        highScore.setText("High Score");
        highScore.setTextureSheet(textures.TEX_FONT1);
        highScore.setTextSize(mgr.gameMain.text_size);
        highScore.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        highScore.setBorder(border/2,0,border,0);
        highScore.setBorderColor(1,1,1,0.3f);
        highScore.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        highScore.setPadding(border);
        highScore.setWeight(5, 1); // ugly hack so the long string displays correctly. blame on lack of wrap mode.
        
        highScoreNumber = new engine_guiNumber(this, idHighScoreNumber);
        highScoreNumber.setNumber(000);
        highScoreNumber.setTextureSheet(textures.TEX_FONT1);
        highScoreNumber.setTextSize(mgr.gameMain.text_size);
        highScoreNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        highScoreNumber.setBorder(border/2,0,0,border);
        highScoreNumber.setBorderColor(1,1,1,0.3f);
        highScoreNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        highScoreNumber.setPadding(border);
        
        
        engine_guiText bestStreak = new engine_guiText(this, idBestStreak);
        bestStreak.setText("Best Streak");
        bestStreak.setTextureSheet(textures.TEX_FONT1);
        bestStreak.setTextSize(mgr.gameMain.text_size);
        bestStreak.setAlignment(ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER);
        bestStreak.setBorder(0,border,border,0);
        bestStreak.setBorderColor(1,1,1,0.3f);
        bestStreak.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        bestStreak.setPadding(border);
        bestStreak.setWeight(5, 1); // ugly hack so the long string displays correctly. blame on lack of wrap mode.
//        bestStreak.setMargin(0, border, 0, 0);
        
        bestStreakNumber = new engine_guiNumber(this, idBestStreakNumber);
        bestStreakNumber.setNumber(3210);
        bestStreakNumber.setTextureSheet(textures.TEX_FONT1);
        bestStreakNumber.setTextSize(mgr.gameMain.text_size);
        bestStreakNumber.setAlignment(ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER);
        bestStreakNumber.setBorder(0,border,0,border);
        bestStreakNumber.setBorderColor(1,1,1,0.3f);
        bestStreakNumber.setBackgroundColor(0.12f,0.12f,0.12f,.9f);
        bestStreakNumber.setPadding(border);
//        bestStreakNumber.setMargin(0, border, 0, 0);
        
        records = new engine_guiButton(this, idRecordsButton);
        records.setText("records");
        records.setTextureSheet(textures.TEX_FONT1);
        records.setTextSize(mgr.gameMain.text_size);
        records.setBorder(border,border,border,border);
        records.setBorderColor(0,1,1,0.3f);
        records.setBackgroundColor(0,0,0,.9f);
        records.setPadding(border);
        records.setWeight(1, 1.25f);
        records.setMargin(2*border,0,0,0);
        
        diff = new engine_guiButton(this, idChangeDiffButton);
        diff.setText("set difficulty");
        diff.setTextureSheet(textures.TEX_FONT1);
        diff.setTextSize(mgr.gameMain.text_size);
        diff.setBorder(border,border,border,border);
        diff.setBorderColor(0,1,1,0.3f);
        diff.setBackgroundColor(0,0,0,.9f);
        diff.setPadding(border);
        diff.setWeight(1, 1.25f);
        diff.setMargin(2*border,0,0,0);
        
        play = new engine_guiButton(this, idPlayAgainButton);
        play.setText("play again");
        play.setTextureSheet(textures.TEX_FONT1);
        play.setTextSize(mgr.gameMain.text_size);
        play.setTextColor(0, 1, 0, 1);
        play.setBorder(border,border,border,border);
        play.setBorderColor(0,1,1,0.3f);
        play.setBackgroundColor(0,0,0,.9f);
        play.setPadding(border);
        play.setWeight(1, 1.25f);
        play.setMargin(2*border,0,0,0);
        
        
        
        addElement(title);
        addElement(score);
        addElement(scoreNumber);
        addElement(streak);
        addElement(streakNumber);
        addElement(highScore);
        addElement(highScoreNumber);
        addElement(bestStreak);
        addElement(bestStreakNumber);
        addElement(records);
        addElement(diff);
        addElement(play);
        
        build();
    }

}
