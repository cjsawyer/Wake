package com.mammothGames.wake1.gameEngine;

public class engine_guiNumber extends engine_guiTextElement {

    private int number = 0;
    private double number_double = 0;
    private boolean use_double = false;
    
    public engine_guiNumber(engine_gui gui, int id) {
        super( gui, id);
    }
    
    public void setNumber(int number) {
        this.number = number;
        use_double = false;
    }
    public void setNumber(double number) {
        this.number_double = number;
        use_double = true;
    }
    
    
    @Override
    public void update() {
        
        drawDefaultBackground();
        
        
        gui.ref.draw.setDrawColor(r, g, b, a*gui.alpha);
        
        if (use_double)
            gui.ref.draw.text.append(number_double);
        else
            gui.ref.draw.text.append(number);
        
        gui.ref.draw.drawText(gui.x + contentX+text_x, gui.y + contentY+text_y, size, x_align, y_align, gui.depth, texture_sheet);
        
    }
    
}
