package com.mammothGames.wake1.gameEngine;

import android.util.Log;

public class engine_guiButton extends engine_guiTextElement {

    private boolean clicked = false, act_on_hover = true, draw_hover = false, default_hover_set = false, backround_captured = false, started_in_button = false;
    private float hr,hb,hg,ha, or=1, ob, og, oa; // hover color and original backgrgound color
    
    public engine_guiButton(engine_gui gui, int id) {
        super(gui, id);
    }
    
    public void setText(String text) {
        this.text = text;
    }

	public void setHoverColor(float r, float g, float b, float a) {
        hr = r;
        hg = g;
        hb = b;
        ha = a;
    }
	
	private void captureOriginalBackgroundColor() {
		if (backround_captured == false) {
			// Copy old background color values
    		or = pr;
    		og = pg;
    		ob = pb;
    		oa = pa;
    		backround_captured = true;
		}
	}
	private void setDefaultHoverColor() {
		if (default_hover_set == false) {
    		hr = pr + 0.15f;
    		hg = pg + 0.15f;
    		hb = pb + 0.15f;
    		ha = pa + 0.15f;
    		default_hover_set = true;
    	}
	}
	
    @Override
    public void update() {
    	
    	captureOriginalBackgroundColor();
    	setDefaultHoverColor();
    	
    	boolean touched = false;
    	int state = gui.ref.input.get_touch_state(0);
    	
    	if ( (state != gui.ref.input.TOUCH_NONE) && (gui.alpha > 0.5f) ) {
    		if (gui.ref.collision.point_AABB(w, h, x, y, gui.ref.input.get_touch_x(0), gui.ref.input.get_touch_y(0)))
    			touched = true;
    	}
    	
    	if (state == gui.ref.input.TOUCH_DOWN) {
    		if (touched)
    			started_in_button = true;
    		else
    			started_in_button = false;
    	}
    	
    	draw_hover = false;
    	
    	if ( (state == gui.ref.input.TOUCH_HELD) && started_in_button && touched && act_on_hover)
			draw_hover = true;
    	
    	if (draw_hover)
    		setBackgroundColor(hr, hg, hb, ha); // set padding color to hover color
    	else
    		setBackgroundColor(or, og, ob, oa); // set padding color to original color
    	
        drawDefaultBackground(); // this includes the modified padding color
        
        // Draw text
        gui.ref.draw.setDrawColor(r, g, b, a*gui.alpha);
        gui.ref.draw.text.append(text);
        gui.ref.draw.drawText(contentX+text_x, contentY+text_y, size, x_align, y_align, gui.depth, texture_sheet);
        
        if ( (state == gui.ref.input.TOUCH_UP) && started_in_button && touched )
            clicked = true;
    }
    
    public boolean getClicked() {
    	boolean rtn = clicked;
        clicked = false;
        return rtn;
    }
    
    public void setActOnHover(boolean act_on_hover) {
    	this.act_on_hover = act_on_hover;
    }
}
