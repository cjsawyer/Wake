package com.mammothGames.wake1.gameEngine;

import android.util.Log;

public class engine_guiText extends engine_guiElement{
    
    String text;
    int number=0;
    boolean use_string = true;
    float size;
    int x_align = gui.ref.draw.X_ALIGN_CENTER; 
    int y_align = gui.ref.draw.Y_ALIGN_CENTER;
    int texture_sheet;
    float text_x = 0, text_y = 0;
    
    private boolean default_size = true;
    public int DEFAULT_SIZE = -1;
    
    public engine_guiText(engine_gui gui, int id) {
        super(gui, id);
    }
    
    public void setText(String text) {
        this.text = text;
        use_string = true;
    }
    public void setText(int number) {
        this.number = number;
        use_string = false;
    }
    
    public void setSize(float size) {
        this.size = size;
        default_size = false;
        
        if (size == DEFAULT_SIZE) {
            default_size = true;
        }
    }
    public void setAlignment(int x_align, int y_align) {
        this.x_align = x_align;
        this.y_align = y_align;
    }
    public void setTextureSheet(int texture_sheet) {
        this.texture_sheet= texture_sheet; 
    }
    
    @Override
    void computeSizesAndCenters() {
        //TODO
        contentH = h;
        x = rx + gui.x;
        y = ry + gui.y;
        
        switch (x_align) {
            case engine_gl_draw.X_ALIGN_RIGHT:
                text_x = w/2;
                break;
            case engine_gl_draw.X_ALIGN_CENTER:
                text_x = 0;
                break;
            case engine_gl_draw.X_ALIGN_LEFT:
                text_x = -w/2;
                break;
        }
        switch (y_align) {
            case engine_gl_draw.Y_ALIGN_BOTTOM:
                text_y = -h/2;
                break;
            case engine_gl_draw.Y_ALIGN_CENTER:
                text_y = 0;
                break;
            case engine_gl_draw.Y_ALIGN_TEXT_BASELINE:
                text_y = 0;
                break;
            case engine_gl_draw.Y_ALIGN_TOP:
                text_y = h/2;
                break;
        }
    }
    
    @Override
    public void update() {
        
        if (default_size) {
            size = contentH/2;//TODO, tmp make full content height, check if too wide, if so; set smaller until it fits
        }
        
        gui.ref.draw.setDrawColor(bgr, bgg, bgb, bga*gui.alpha);
        gui.ref.draw.drawRectangle(x, y, w, h, 0, 0, 0, gui.depth);
        
        gui.ref.draw.setDrawColor(1, 1, 1, 1*gui.alpha);
        
        if (use_string)
            gui.ref.draw.text.append(text);
        else
            gui.ref.draw.text.append(number);
        
        gui.ref.draw.drawText(x+text_x, y+text_y, size, x_align, y_align, gui.depth, texture_sheet);
        
        if (gui.ref.input.get_touch_state(0) == gui.ref.input.TOUCH_DOWN) {
          if (gui.ref.collision.point_AABB(w, h, x, y, gui.ref.input.get_touch_x(0), gui.ref.input.get_touch_y(0))) {
              Log.e("button","pressed");
          }
        }
    }
    
}