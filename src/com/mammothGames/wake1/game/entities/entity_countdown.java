package com.mammothGames.wake1.game.entities;

import android.util.Log;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.rooms;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_countdown extends engine_entity {

    private float size,x,y,alpha;
    boolean counting;
    private int number=-1;
    
    
    masterGameReference mgr;
    public entity_countdown(masterGameReference mgr) {
        this.persistent = true;
        this.pausable = false;
        this.mgr = mgr;
    }
    
    
	@Override
	public void sys_firstStep() {
	    
	    x = ref.screen_width/2;
	    y = ref.screen_height/2;
	    
	    counting = false;
	}
	
	@Override
	public void sys_step() {
	    
	    if ( counting ) {
	        
	        // black veil covering everything under popup
	    	// duplicated in popup
	    	if (mgr.gameMain.game_running) { //game is paused, we need to fade the black vail out
	    		alpha = alarm[2]/3000f;
	    		ref.draw.setDrawColor(0,0,0, 0.7f * alpha);
	    		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer7_overHUD);
	    	}
	        
	        
	        alpha = alarm[1]/1000 * 1.2f;
            size = ref.screen_width * alarm[1]/1000;
            
            
            ref.draw.setDrawColor(1, 1, 1, alpha);
            ref.draw.text.append(number);
            ref.draw.drawText(x, y, size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
            
	    }
	}
	
	public void stopCountdown() {
		
		Log.e("countdown", "countdown stopped!");
		
		counting = false;
		alarm[0] = -1;
		alarm[1] = -1;
		alarm[2] = -1;
		number = -1;
	}
	
	@Override
	public void alarm1() {
		alarm[1] = 1000;
		number -= 1;
	    if (number == 0) {
	        mgr.menuPauseHUD.doPauseHard(false);
	        counting = false;
	        if ( !mgr.gameMain.game_running )
	        	mgr.gameMain.startGame();
	    }
	}
	
	public void startCountdown() {
	    number = 3;
	    counting = true;
	    alarm[1] = 1000;
		alarm[2] = number*alarm[1]; // used for shade alpha
		if ( !mgr.gameMain.game_running )
			mgr.gameMain.prepGame();
	}
	
}
