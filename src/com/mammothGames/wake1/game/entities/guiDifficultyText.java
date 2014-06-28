package com.mammothGames.wake1.game.entities;

import com.mammothGames.wake1.gameEngine.*;

public class guiDifficultyText extends engine_guiText {

	masterGameReference mgr;
	private int diff = 0;

	public guiDifficultyText(engine_gui gui, int id, masterGameReference mgr) {
		super(gui, id);
		this.mgr = mgr;
	}
	
	public void setDifficulty(int diff) {
		this.diff = diff;
	}
	
	@Override
	public void update() {
	    
	    drawDefaultBackground();
	    
	    float dx=0;
		float dy=0;
		if(diff==entity_gameMain.DIF_HELL) {
			// Do the shaky text for hell mode
			float shake_range = mgr.gameMain.text_size/20;
			dx = gui.ref.main.randomRange(-shake_range, shake_range);
			dy = gui.ref.main.randomRange(-shake_range, shake_range);
			gui.ref.draw.setDrawColor(1, 0, 0, a*gui.alpha);
			gui.ref.draw.text.append("HELL");
		} else {
			// Normal text for the other modes
			gui.ref.draw.setDrawColor(1, 1, 1, a*gui.alpha);
			switch(diff) {
				case entity_gameMain.DIF_EASY:
					gui.ref.draw.text.append("Easy");	
					break;
				case entity_gameMain.DIF_MEDIUM:
					gui.ref.draw.text.append("Medium");	
					break;
				case entity_gameMain.DIF_HARD:
					gui.ref.draw.text.append("Hard");	
					break;
			}
		}
	    
	    gui.ref.draw.drawText(gui.x+contentX+text_x+dx, gui.y+contentY+text_y+dy, size, x_align, y_align, gui.depth, texture_sheet);
	    
	}
	
}