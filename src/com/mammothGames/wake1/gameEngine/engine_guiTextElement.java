package com.mammothGames.wake1.gameEngine;

import android.util.Log;

public class engine_guiTextElement extends engine_guiElement{
    
    protected String text = "";
    protected float size;
    protected int x_align = gui.ref.draw.X_ALIGN_CENTER; 
    protected int y_align = gui.ref.draw.Y_ALIGN_CENTER;
    protected int texture_sheet;
    protected float text_x = 0, text_y = 0;
    
    protected float r=1,g=1,b=1,a=1;
    
    protected boolean default_size = true;
    protected int DEFAULT_SIZE = -1;
    
    public engine_guiTextElement(engine_gui gui, int id) {
        super(gui, id);
    }
    
    public void setTextSize(float size) {
        this.size = size;
        default_size = false;
        
        if (size == DEFAULT_SIZE) {
            default_size = true;
        }
    }
    public void setTextColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
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
        
        super.computeSizesAndCenters();
        
        switch (x_align) {
            case engine_gl_draw.X_ALIGN_RIGHT:
                text_x = contentW/2;
                break;
            case engine_gl_draw.X_ALIGN_CENTER:
                text_x = 0;
                break;
            case engine_gl_draw.X_ALIGN_LEFT:
                text_x = -contentW/2;
                break;
        }
        switch (y_align) {
            case engine_gl_draw.Y_ALIGN_BOTTOM:
                text_y = -contentH/2;
                break;
            case engine_gl_draw.Y_ALIGN_CENTER:
                text_y = 0;
                break;
            case engine_gl_draw.Y_ALIGN_TEXT_BASELINE:
                text_y = 0;
                break;
            case engine_gl_draw.Y_ALIGN_TOP:
                text_y = contentH/2;
                break;
        }
        
        if (default_size) {
            size = contentH;//TODO make full content height, check if too wide, if so; set smaller until it fits. fancy resizing.
        }
    }
    
    @Override
    public void update() {
        drawDefaultBackground();
    }
    
}