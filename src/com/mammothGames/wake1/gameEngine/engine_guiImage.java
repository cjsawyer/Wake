package com.mammothGames.wake1.gameEngine;

public class engine_guiImage extends engine_guiTextElement {

    private int texture, sub_image;
    private float size_x = 1, size_y = 1;
    private float scale = 1;
    private boolean draw_background = false;
	
    public engine_guiImage(engine_gui gui, int id) {
        super(gui, id);
    }
    
    public void setTexture(int texture, int sub_image) {
        this.texture = texture;
        this.sub_image = sub_image;
    }
    public void setDrawBackground(boolean b) {
    	draw_background = b;
    }
    public void setScale(float scale) {
    	this.scale = scale;
    }
    
    @Override
    protected void computeSizesAndCenters() {
    	super.computeSizesAndCenters();
    	
    	// Set image scale so the maximum size is attained
    	//   inside of the content area of the element
    	//   without changing the width/height proportion
    	
    	float sub_w = gui.ref.draw.getSubTextureWidth(sub_image, texture);
    	float sub_h = gui.ref.draw.getSubTextureHeight(sub_image, texture);
    	
    	boolean wide = false;
    	
    	if (sub_w >= sub_h)
    		wide = true;
    	
    	float scale;
    	
    	if (wide)
    		scale = contentW/sub_w;
    	else
    		scale = contentH/sub_h;
    	
    	size_x = sub_w * scale;
    	size_y = sub_h * scale;
    	
    	
    }
    	
    @Override
    public void update() {
    	if (draw_background)
    		drawDefaultBackground();
    	gui.ref.draw.setDrawColor(r, g, b, a);
//    	gui.ref.draw.drawRectangle(x, y, scale_x, scale_y, 0, 0, 0, gui.depth);
    	gui.ref.draw.drawTexture(gui.x + x, gui.y + y, size_x*scale, size_y*scale, 0, 0, 0, gui.depth, sub_image, texture);
    }
}
