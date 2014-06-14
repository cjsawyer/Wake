package com.mammothGames.wake1.gameEngine;

import android.util.Log;

public class engine_guiButton extends engine_guiTextElement {

    private boolean clicked = false, act_on_hover = true, draw_hover = false, started_in_button = false, clickable=true;
    private float hr,hb,hg,ha, or=1, ob, og, oa; // hover color and original backgrgound color
    
    public engine_guiButton(engine_gui gui, int id) {
        super(gui, id);
    }

    /**
     * Requires rebuild with gui.build() for because of hover color 
     */
    @Override
    public void setBackgroundColor(float r, float g, float b, float a) {
        super.setBackgroundColor(r, g, b, a);
    }
    
    @Override
	protected void computeSizesAndCenters() {
    	super.computeSizesAndCenters();
    	captureOriginalBackgroundColor();
    	setDefaultHoverColor();
    }
    
    public void setClickable(boolean clickable) {
    	this.clickable = clickable;
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
		// Copy old background color values
		or = pr;
		og = pg;
		ob = pb;
		oa = pa;
	}
	private void setDefaultHoverColor() {
		hr = pr + 0.15f;
		hg = pg + 0.15f;
		hb = pb + 0.15f;
		ha = pa;
	}
	
    @Override
    public void update() {
    	
    	draw_hover = false;
    	
    	if ( (gui.clickable) && (clickable) ) {
	    	boolean touched = false;
	    	int state = gui.ref.input.get_touch_state(0);
	    	
	    	if ( (state != gui.ref.input.TOUCH_NONE) && (gui.alpha > 0.5f) ) {
	    		if (gui.ref.collision.point_AABB(w, h, gui.x + x, gui.y + y, gui.ref.input.get_touch_x(0), gui.ref.input.get_touch_y(0)))
	    			touched = true;
	    	}
	    	
	    	if (state == gui.ref.input.TOUCH_DOWN) {
	    		if (touched)
	    			started_in_button = true;
	    		else
	    			started_in_button = false;
	    	}
	    	
	    	if ( (state >= gui.ref.input.TOUCH_DOWN) && started_in_button && touched && act_on_hover)
	    		draw_hover = true;
	    	
	    	if ( (state == gui.ref.input.TOUCH_UP) && started_in_button && touched )
	    		clicked = true;
    	}
    	
    	if (draw_hover)
    		setBackgroundColor(hr, hg, hb, ha); // set padding color to hover color
    	else
    		setBackgroundColor(or, og, ob, oa); // set padding color to original color
    	
        drawDefaultBackground(); // this includes the modified padding color ie the background color
        
        // Draw text
        gui.ref.draw.setDrawColor(r, g, b, a*gui.alpha);
        gui.ref.draw.text.append(text);
        gui.ref.draw.drawText(gui.x + contentX+text_x, gui.y + contentY+text_y, size, x_align, y_align, gui.depth, texture_sheet);
        
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
