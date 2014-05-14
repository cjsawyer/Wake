package com.mammothGames.wake1.gameEngine;

public class engine_guiElement {

    final engine_gui gui;
    private final int id;
    
    float x, y, rx, ry, w,h; // for drawing. width/height (relative x/y to the center of the gui)
    float marginT=0,marginB=0,marginL=0,marginR=0;
    float borderT=0,borderB=0,borderL=0,borderR=0;
    float paddingT=0, paddingB=0, paddingL=0, paddingR=0; // set in constructor
    
    //Sizes and centers of different regions
    float borderW, borderH, paddingW, paddingH, contentW, contentH;
    float borderX, borderY, paddingX, paddingY, contentX, contentY;
    
    // RGBA (margin is not drawn, then border, then background, the content color from outside to inside)
    float r = 1, g = 1, b = 1, a = 1; // content color
    float bgr = 1, bgg = 1, bgb = 1, bga = 1; // background color
    float br = 1, bg = 1, bb = 1, ba = 1; // border color
    
    //margin between B and A  =  padding(A) + padding(B) + max(margin(A), margin(B))
    
    
    public engine_guiElement(engine_gui gui, int id) {
        
        this.gui = gui;
        this.id = id;
        
        //TODO temp for test
        bgr = gui.rand.nextFloat();
        bgg = gui.rand.nextFloat();
        bgb = gui.rand.nextFloat();
        bga = 1;
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
    
    public void setBorder(float passing) {
        paddingT=passing;
        paddingB=passing;
        paddingL=passing;
        paddingR=passing;
    }
    public void setBorder(float top, float bottom, float left, float right) {
        paddingT=top;
        paddingB=bottom;
        paddingL=left;
        paddingR=right;
    }
    
    public void setPadding(float passing) {
        paddingT=passing;
        paddingB=passing;
        paddingL=passing;
        paddingR=passing;
    }
    public void setPadding(float top, float bottom, float left, float right) {
        paddingT=top;
        paddingB=bottom;
        paddingL=left;
        paddingR=right;
    }
    
    void computeSizesAndCenters() {
        //TODO
        contentH = h;
        x = rx + gui.x;
        y = ry + gui.y;
    }
    
    public void setPosition(float x, float y) {
        this.rx = x;
        this.ry = y;
    }
    
    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }
    
    public void update() {
        gui.ref.draw.setDrawColor(bgr, bgg, bgb, bga*gui.alpha);
        gui.ref.draw.drawRectangle(x, y, w, h, 0, 0, 0, gui.depth);
    }
    
    public int getID() {
        return id;
    }

}
