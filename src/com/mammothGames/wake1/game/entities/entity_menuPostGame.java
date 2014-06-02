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
	
	boolean fade_out;
	PostGUI post_gui;
	
	@Override
	public void sys_firstStep() {
		post_gui = new PostGUI(ref, mgr);
        post_gui.setPosition(ref.screen_width/2, ref.screen_height/2);
        post_gui.setSize(ref.screen_width - mgr.gameMain.padding_x*2, ref.screen_height - mgr.gameMain.padding_y);
        post_gui.populate();
        post_gui.setDepth(constants.layer7_overHUD);
        post_gui.setActive(true);
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
			blink_alpha *= mgr.gameMain.shade_alpha;
			
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
			
			/*
			float box_width = ref.screen_width - mgr.gameMain.padding_x;
			float box_height = ref.screen_height - mgr.gameMain.padding_y;
			
			
			float box_inner_padding = mgr.gameMain.padding_y/9;
			float box_top_y = ref.screen_height -mgr.gameMain.padding_y/2;
			float box_partition = mgr.gameMain.text_size*4;//(box_height - 3*box_inner_padding)/4;
			
			// The text at the top gets it's own alpha so it doesn't clash with the sliding HUD
			float diff_text_alpha = mgr.gameMain.shade_alpha * (1-(mgr.menuPauseHUD.HUD_y_target-mgr.menuPauseHUD.HUD_y)/mgr.menuPauseHUD.HUD_y_target);
			float blink_alpha = ((float)Math.sin((float)(SystemClock.uptimeMillis() * mgr.menuFirst.DEG_TO_RAD / 500f * 180f))) + 1;
			blink_alpha = blink_alpha > 1 ? 1:blink_alpha;
			blink_alpha *= mgr.gameMain.shade_alpha;
			
			float dx=0;
			float dy=0;
			if(mgr.gameMain.current_diff==mgr.gameMain.DIF_HELL) {
				// Do the shaky text for hell mode
				float shake_range = mgr.gameMain.text_size/20;
				dx = ref.main.randomRange(-shake_range, shake_range);
				dy = ref.main.randomRange(-shake_range, shake_range);
				ref.draw.setDrawColor(1, 0, 0, diff_text_alpha);
				ref.draw.text.append("HELL");
			} else {
				// Normal text for the other modes
				ref.draw.setDrawColor(1, 1, 1, diff_text_alpha);
				ref.draw.text.append(mgr.gameMain.current_diff_string);	
			}
			
			ref.draw.drawText(ref.screen_width/2 + dx,  (ref.screen_height+box_top_y)/2 + dy, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			
//			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
//			ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, box_width, box_height, 0, 0, 0, game_constants.layer6_HUD);
			
			float draw_y, text_y, text_h;
			draw_y = box_top_y;
			text_h = mgr.menuDifficulty.draw_width/2-mgr.menuDifficulty.button_border_size;
			
			//Gray rectangle behind text.
			ref.draw.setDrawColor(1, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
//			ref.draw.drawRectangle(ref.screen_width/2, draw_y - box_partition - box_inner_padding/2, mgr.menuDifficulty.draw_width, box_partition*2 + box_inner_padding , 0, 0, 0, game_constants.layer6_HUD);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y - box_inner_padding/2 - (box_partition + mgr.menuDifficulty.draw_height + box_inner_padding -mgr.menuDifficulty.button_border_size)/2, mgr.menuDifficulty.draw_width, box_partition + mgr.menuDifficulty.draw_height + box_inner_padding , 0, 0, 0, constants.layer6_HUD);
			// darker gray inner box
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(ref.screen_width/2, draw_y - box_inner_padding/2 - (box_partition + mgr.menuDifficulty.draw_height + box_inner_padding -mgr.menuDifficulty.button_border_size)/2, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, box_partition + mgr.menuDifficulty.draw_height + box_inner_padding -mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);
			
//																																							draw_y -= box_partition+box_inner_padding;
//																																							draw_y -= mgr.menuDifficulty.draw_height + box_inner_padding;
			//vvvvvv
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			text_y = draw_y - box_partition/2 + (box_inner_padding+mgr.gameMain.text_size)/2;
			
			ref.draw.text.append("Score");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.score);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			////////
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			text_y = draw_y - box_partition/2 - (box_inner_padding+mgr.gameMain.text_size)/2;
			
			ref.draw.text.append("Streak");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.best_points_streak_this_game);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			//^^^^^^
			
//			draw_y -= 2*mgr.gameMain.text_size+box_inner_padding;
//			draw_y -= box_partition+box_inner_padding;
			draw_y -= mgr.menuDifficulty.draw_height + box_inner_padding;
			
			//vvvvvv
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			if (mgr.gameMain.new_high_score)
				ref.draw.setDrawColor(0, 1, 1, blink_alpha);
			
			text_y = draw_y - box_partition/2 + (box_inner_padding+mgr.gameMain.text_size)/2;
			
			
			ref.draw.text.append("High Score");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.high_score);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			////////
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			if (mgr.gameMain.new_best_streak)
				ref.draw.setDrawColor(0, 1, 1, blink_alpha);
			
			text_y = draw_y - box_partition/2 - (box_inner_padding+mgr.gameMain.text_size)/2;
			
			ref.draw.text.append("Best Streak");
			ref.draw.drawText(ref.screen_width/2 - text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_LEFT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			ref.draw.text.append(mgr.gameMain.best_points_streak);
			ref.draw.drawText(ref.screen_width/2 + text_h,  text_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_RIGHT, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			//^^^^^^
			
			draw_y -=  mgr.menuDifficulty.draw_height/2+box_partition/2+box_inner_padding;
			
			//vvvvvv
			//draw back, blue rectangle
			ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(ref.screen_width/2, draw_y-box_partition/2, mgr.menuDifficulty.draw_width, mgr.menuDifficulty.draw_height, 0, 0, 0, constants.layer6_HUD);
			
			// draw inner black rectangle
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(ref.screen_width/2, draw_y-box_partition/2, mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.text.append("RECORDS");
			ref.draw.drawText(ref.screen_width/2, draw_y-box_partition/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) )
				if (ref.collision.point_AABB( mgr.menuDifficulty.draw_width-mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, ref.screen_width/2, draw_y-box_partition/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
					prepLeave(room_records);
				}
			//^^^^^^
			
			draw_y -= mgr.menuDifficulty.draw_height + box_inner_padding;//box_partition+box_inner_padding;
			
			//vvvvvv
			//Left, "AGAIN!" button
			float lr_width = ((mgr.menuDifficulty.draw_width-box_inner_padding)/2);
			float l_button_x = ref.screen_width/2 - (mgr.menuDifficulty.draw_width+box_inner_padding)/4;
			float r_button_x = ref.screen_width/2 + (mgr.menuDifficulty.draw_width+box_inner_padding)/4;
			//draw back, blue rectangle
			ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(l_button_x, draw_y-box_partition/2, lr_width, mgr.menuDifficulty.draw_height, 0, 0, 0, constants.layer6_HUD);
			
			// draw inner black rectangle
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(l_button_x, draw_y-box_partition/2, lr_width - mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.text.append("AGAIN");
			ref.draw.drawText(l_button_x, draw_y-box_partition/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) )
				if (ref.collision.point_AABB(lr_width, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, l_button_x, draw_y-box_partition/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
					prepLeave(room_game);
				}
			////////
			//Left, "QUIT" button
			//draw back, blue rectangle
			ref.draw.setDrawColor(0, 1, 1, 0.3f * mgr.gameMain.shade_alpha );
			ref.draw.drawRectangle(r_button_x, draw_y-box_partition/2, lr_width, mgr.menuDifficulty.draw_height, 0, 0, 0, constants.layer6_HUD);
			
			// draw inner black rectangle
			ref.draw.setDrawColor(0, 0, 0, 0.9f * mgr.gameMain.shade_alpha);
			ref.draw.drawRectangle(r_button_x, draw_y-box_partition/2, lr_width - mgr.menuDifficulty.button_border_size, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, 0, 0, 0, constants.layer6_HUD);			
			
			ref.draw.setDrawColor(1, 1, 1, mgr.gameMain.shade_alpha);
			ref.draw.text.append("QUIT");
			ref.draw.drawText(r_button_x, draw_y-box_partition/2, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
			
			if ( (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) && (mgr.gameMain.shade_alpha > 0.9f) )
				if (ref.collision.point_AABB( lr_width, mgr.menuDifficulty.draw_height-mgr.menuDifficulty.button_border_size, r_button_x, draw_y-box_partition/2, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
					prepLeave(room_menu);
				}
			//^^^^^^
			
			*/
		}
	}
	
	private final int room_game=0, room_records=1, room_diff=2;
	private int room_to_leave_to;
	
	public void start() {
		mgr.gameMain.floor_height_target = 0;
		mgr.gameMain.shade_alpha_target = 1;
		fade_out = false;
		ref.room.changeRoom(rooms.ROOM_POSTGAME);
		ref.ad.showInterstitialAd(); //TODO: make this popup less often
	}
	
	
	public void prepLeave(int room_to_leave_to) {
		mgr.gameMain.shade_alpha_target = 0;
		fade_out = true;
		
		this.room_to_leave_to = room_to_leave_to;  
	}
	
	private void leave() {
		
		switch (room_to_leave_to){
		case room_game:
			mgr.gameMain.startGame();
			break;
		case room_records:
			mgr.menuRecords.start(mgr.menuDifficulty.tab);
			break;
		case room_diff:
			mgr.menuDifficulty.start();
			break;
		}
	
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

class guiDifficultyText extends engine_guiText {

	masterGameReference mgr;
	
	public guiDifficultyText(engine_gui gui, int id, masterGameReference mgr) {
		super(gui, id);
		this.mgr = mgr;
	}
	
	@Override
	public void update() {
	    
	    drawDefaultBackground();
	    
	    float dx=0;
		float dy=0;
		if(mgr.gameMain.current_diff==entity_gameMain.DIF_HELL) {
			// Do the shaky text for hell mode
			float shake_range = mgr.gameMain.text_size/20;
			dx = gui.ref.main.randomRange(-shake_range, shake_range);
			dy = gui.ref.main.randomRange(-shake_range, shake_range);
			gui.ref.draw.setDrawColor(1, 0, 0, a*gui.alpha);
			gui.ref.draw.text.append("HELL");
		} else {
			// Normal text for the other modes
			gui.ref.draw.setDrawColor(1, 1, 1, a*gui.alpha);
			gui.ref.draw.text.append(mgr.gameMain.current_diff_string);	
		}
	    
	    
	    gui.ref.draw.drawText(contentX+text_x+dx, contentY+text_y+dy, size, x_align, y_align, gui.depth, texture_sheet);
	    
	}
	
}