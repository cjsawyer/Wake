package com.mammothGames.wake1.gameEngine;

import android.util.Log;

public class engine_guiButton extends engine_guiTextElement {

    public engine_guiButton(engine_gui gui, int id) {
        super(gui, id);
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void action() {
        Log.e("MAGE", "@Override action() in your guiButton!");
    }
    
    @Override
    public void update() {
        
        drawDefaultBackground();
        
        gui.ref.draw.setDrawColor(r, g, b, a*gui.alpha);
        gui.ref.draw.text.append(text);
        gui.ref.draw.drawText(contentX+text_x, contentY+text_y, size, x_align, y_align, gui.depth, texture_sheet);
        
        if (gui.ref.input.get_touch_state(0) == gui.ref.input.TOUCH_DOWN) {
            if (gui.ref.collision.point_AABB(w, h, x, y, gui.ref.input.get_touch_x(0), gui.ref.input.get_touch_y(0))) {
                action();
            }
        }
        
    }
}