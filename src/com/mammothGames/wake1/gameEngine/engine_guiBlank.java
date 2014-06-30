package com.mammothGames.wake1.gameEngine;

public class engine_guiBlank extends engine_guiElement{
    
    /**
     * For alignment of other elements horizontally
     * @param gui
     * @param id
     */
    public engine_guiBlank(engine_gui gui, int id) {
        super(gui,id);
    }
    
    
    @Override
    protected void computeSizesAndCenters() {
        super.computeSizesAndCenters();
    }
    
    @Override
    public void update() {
    }
    
}