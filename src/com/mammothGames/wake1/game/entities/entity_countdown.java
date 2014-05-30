package com.mammothGames.wake1.game.entities;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_countdown extends engine_entity {

    private float size,x,y,alpha;
    private boolean counting;
    private int number;
    
    
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
	    
	    if (counting) {
	        
	        // black veil covering everything under popup
	        // duplicated in popup
	        alpha = alarm[1]/3000f;
	        ref.draw.setDrawColor(0,0,0, 0.7f * alpha);
	        ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer7_overHUD);
	        
	        
	        alpha = alarm[0]/1000 * 1.2f;
            size = ref.screen_width * alarm[0]/1000;
            
            
            ref.draw.setDrawColor(1, 1, 1, alpha);
            ref.draw.text.append(number);
            ref.draw.drawText(x, y, size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer6_HUD, textures.TEX_FONT1);
            
	    }
	}
	
	@Override
	public void alarm0() {
	    alarm[0] = 1000;
	    number -= 1;
	    if (number == 0) {
	        mgr.menuPauseHUD.setPause(false);
	        counting = false;
	    }
	}
	
	
	public void startUnpauseCountdown() {
	    number = 3;
	    counting = true;
	    alarm[0] = 1000;
	    alarm[1] = number*alarm[0];
	}
	
	public boolean getCounting() {
	    return counting;
	}
	
}
