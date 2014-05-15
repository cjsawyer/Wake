package com.mammothGames.wake1.gameEngine;

public class engine_guiElement {

    final engine_gui gui;
    private final int id;
    
    float x, y, rx, ry, w,h; // for drawing. width/height (relative x/y to the center of the gui)
    float marginT=0,marginB=0,marginL=0,marginR=0;
    float borderT=0,borderB=0,borderL=0,borderR=0;
    float paddingT=0, paddingB=0, paddingL=0, paddingR=0; // set in constructor
    
    float borderTX, borderTY, borderTW, borderTH;
    float borderBX, borderBY, borderBW, borderBH;
    float borderLX, borderLY, borderLW, borderLH;
    float borderRX, borderRY, borderRW, borderRH;
    
    //Sizes and centers of different regions
    float borderW, borderH, paddingW, paddingH, contentW, contentH;
    float borderX, borderY, paddingX, paddingY, contentX, contentY;
    
    // RGBA (margin is not drawn, then border [around padding, with lines], then padding [which is the background area], then the content, from outside to inside)
    float br = 1, bg = 1, bb = 1, ba = 1; // border color
    float pr = 1, pg = 1, pb = 1, pa = 1; // padding color
    
    //margin between B and A  =  padding(A) + padding(B) + max(margin(A), margin(B))
    
    //TODO move these back down
    void computeSizesAndCenters() {
        //TODO contentH = h;
        x = rx + gui.x;
        y = ry + gui.y;
        
        borderW = w - marginL - marginR;
        borderH = h - marginT - marginB;
        borderX = x - w/2 + marginL + borderW/2f;
        borderY = y - h/2 + marginB + borderH/2f;
        
        // Left border segment rectangle 
        borderLW = borderL;
        borderLH = borderH - borderT - borderB;
        
        borderLX = borderX - borderW/2 + borderL/2;
        borderLY = borderY + (borderB - borderT)/2;
        
        // Right border segment rectangle 
        borderRX = borderX + borderW/2 - borderR/2;
        borderRY = borderY + (borderB - borderT)/2;
        borderRW = borderR;
        borderRH = borderH - borderT - borderB;
        
        // Top border segment rectangle 
        borderTX = borderX;
        borderTY = borderY + borderH/2 - borderT/2;
        borderTW = borderW;
        borderTH = borderT;
        
        // Bottom border segment rectangle 
        borderBX = borderX;
        borderBY = borderY - borderH/2 + borderB/2;
        borderBW = borderW;
        borderBH = borderB;
        
        
        
        paddingW = borderW - borderL - borderR;
        paddingH = borderH - borderT - borderB;
        paddingX = borderX - borderW/2 + borderL + paddingW/2f;
        paddingY = borderY - borderH/2 + borderB + paddingH/2f;
        
        
        contentW = paddingW - paddingL - paddingR;
        contentH = paddingH - paddingT - paddingB;
        contentX = paddingX - paddingW/2 + paddingL + contentW/2f;
        contentY = paddingY - paddingH/2 + paddingB + contentH/2f;
        
        
        
        
    }
    
    public engine_guiElement(engine_gui gui, int id) {
        this.gui = gui;
        this.id = id;
    }
    
    public void setMargin(float margin) {
        marginT=margin;
        marginB=margin;
        marginL=margin;
        marginR=margin;
    }
    public void setMargin(float top, float bottom, float left, float right) {
        marginT=top;
        marginB=bottom;
        marginL=left;
        marginR=right;
    }
    
    public void setBorder(float border) {
        borderT=border;
        borderB=border;
        borderL=border;
        borderR=border;
    }
    public void setBorder(float top, float bottom, float left, float right) {
        borderT=top;
        borderB=bottom;
        borderL=left;
        borderR=right;
    }
    
    public void setBorderColor(float r, float g, float b, float a) {
        br = r;
        bg = g;
        bb = b;
        ba = a;
    }
    
    public void setPadding(float padding) {
        paddingT=padding;
        paddingB=padding;
        paddingL=padding;
        paddingR=padding;
    }
    public void setPadding(float top, float bottom, float left, float right) {
        paddingT=top;
        paddingB=bottom;
        paddingL=left;
        paddingR=right;
    }
    
    public void setBackgroundColor(float r, float g, float b, float a) {
        pr = r;
        pg = g;
        pb = b;
        pa = a;
    }
    
    public void setPosition(float x, float y) {
        this.rx = x;
        this.ry = y;
    }
    
    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }
    
    public int getID() {
        return id;
    }
    
    protected void drawDefaultBackground() {
        gui.ref.draw.setDrawColor(br, bg, bb, ba*gui.alpha);
        gui.ref.draw.drawRectangle(borderTX, borderTY, borderTW, borderTH, 0, 0, 0, gui.depth);
        gui.ref.draw.drawRectangle(borderBX, borderBY, borderBW, borderBH, 0, 0, 0, gui.depth);
        gui.ref.draw.drawRectangle(borderLX, borderLY, borderLW, borderLH, 0, 0, 0, gui.depth);
        gui.ref.draw.drawRectangle(borderRX, borderRY, borderRW, borderRH, 0, 0, 0, gui.depth);
        
        gui.ref.draw.setDrawColor(pr, pg, pb, pa*gui.alpha);
        gui.ref.draw.drawRectangle(paddingX, paddingY, paddingW, paddingH, 0, 0, 0, gui.depth);

    }
    
    public void update() {
        drawDefaultBackground();
    }
    

}
