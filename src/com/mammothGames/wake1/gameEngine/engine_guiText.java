package com.mammothGames.wake1.gameEngine;

public class engine_guiText extends engine_guiTextElement{
    
    
    public engine_guiText(engine_gui gui, int id) {
        super(gui, id);
    }
    public void setText(String text) {
        this.text = text;
    }
    
    
    
    @Override
    public void update() {
        
        drawDefaultBackground();
        
        gui.ref.draw.setDrawColor(r, g, b, a*gui.alpha);
        gui.ref.draw.text.append(text);
        gui.ref.draw.drawText(gui.x + contentX+text_x, gui.y + contentY+text_y, size, x_align, y_align, gui.depth, texture_sheet);
        
    }
    
}