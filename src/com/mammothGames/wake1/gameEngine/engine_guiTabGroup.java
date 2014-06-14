package com.mammothGames.wake1.gameEngine;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.game.entities.masterGameReference;

public class engine_guiTabGroup extends engine_guiTextElement {
	
	private TabGroupGUI container;
	private int num_tabs;
	private String[] strings;
	private float[] colors;
	private int active_tab = 0, previous_active_tab = -1;
	
	private float ipr = 1, ipg = 1, ipb = 1, ipa = 1; // inactive padding color
	
    public engine_guiTabGroup(engine_gui gui, int id, int num_tabs) {
        super(gui, id);
        
        container = new TabGroupGUI(gui.ref, null, num_tabs);
        
        strings = new String[num_tabs];
    	colors = new float[num_tabs * 4];
        
        this.num_tabs = num_tabs;
        
        // Set defaults for tabs
        for(int i=0; i<num_tabs;i++) {
        	strings[i] = "";
        }
        for(int i=0; i<colors.length; i++) {
        	colors[i] = 1;
        }
    }
    
    public void setTabString(int tab, String s) {
    	strings[tab] = s;
    }
    
    public void setTabStringColor(int tab, float r, float g, float b, float a) {
    	colors[tab*4] = r;
    	colors[tab*4+1] = g;
    	colors[tab*4+2] = b;
    	colors[tab*4+3] = a;
    }
    
    public void setInactiveBackgroundColor(float r, float g, float b, float a) {
        ipr = r;
        ipg = g;
        ipb = b;
        ipa = a;
    }
    
    
    @Override
    protected void computeSizesAndCenters() {
    	
    	super.computeSizesAndCenters();
    	
    	container.setSize(w,h);
    	container.setPosition(contentX, contentY);
    	container.setDepth(constants.layer6_HUD);
    	setup();
    	container.build();
    	container.setActive(true);
    	
    }
    
    private void setup() {
    	
    	engine_guiButton tmp = null;
    	for(int i=0; i<num_tabs;i++) {

    		tmp = container.tabs[i];
    		
    		tmp.setText(strings[i]);
            tmp.setTextureSheet(textures.TEX_FONT1);
            tmp.setTextSize(size);
            tmp.setTextColor(
            		colors[i*4],
            		colors[i*4 + 1],
            		colors[i*4 + 2],
            		colors[i*4 + 3]
            		);

            // This is so the interior tabs don't have double L/R borders
            float bL, bR;
            if (i==0)
            	bL = borderL;
            else
            	bL = borderL/2;
            
            if (i==num_tabs-1)
            	bR = borderR;
            else
            	bR = borderR/2; 
            	
            tmp.setBorder(borderT,borderB,bL,bR);
            	
            
            tmp.setBorderColor(br,bg,bb,ba);
            tmp.setBackgroundColor(pr,pg,pb,pa);
            tmp.setMargin(marginT,marginB,marginL,marginR);
            tmp.setPadding(paddingT, paddingB, paddingL, paddingR);
    		
    	}
    	
    	
    }
    
    @Override
    public void update() {
    	
    	for (int i=0; i<num_tabs; i++) {
    		if(container.tabs[i].getClicked()) {
    			previous_active_tab = active_tab;
    			active_tab = i;
    		}
    	}
    	
    	if (previous_active_tab != active_tab) {
    		for (int i=0; i<num_tabs; i++) {
    			container.tabs[i].borderB = borderB;
    			container.tabs[i].setBackgroundColor(ipr, ipb, ipg, ipa);
    			container.tabs[i].setClickable(true);
    		}
    		container.tabs[active_tab].setBackgroundColor(pr, pb, pg, pa);
    		container.tabs[active_tab].borderB = 0;
    		container.tabs[active_tab].setClickable(false);
    		container.build();
    	}
    	
    	container.setActive(gui.active);
    	container.setAlpha(gui.alpha);
    	container.setPosition(gui.x+contentX, gui.y+contentY);
    	container.update();
    	
    }
}

class TabGroupGUI extends engine_gui {
    
    private int[][] layout;
    engine_guiButton[] tabs;
    
    masterGameReference mgr;
    public TabGroupGUI(engine_reference ref, masterGameReference mgr, int num_tabs) {
        super(ref);
        this.mgr = mgr;
        
        int[] sub_layout = new int[num_tabs];
    	
    	for(int i=0; i<num_tabs; i++)
    		sub_layout[i] = i;
    
    	layout = new int[2][num_tabs];
    	layout[0][0] = num_tabs;
    	layout[0][1] = 1;
    	layout[1] = sub_layout;
    	
    	setLayout(layout);
    	
    	tabs = new engine_guiButton[num_tabs];
    	
    	for(int i=0; i<num_tabs; i++) {
    		tabs[i] = new engine_guiButton(this, i);
        	addElement(tabs[i]);
        }
        
    }
    
}