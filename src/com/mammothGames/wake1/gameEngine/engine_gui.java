package com.mammothGames.wake1.gameEngine;

import java.util.Random;

import android.util.Log;

import com.mammothGames.wake1.game.textures;

public class engine_gui {

    Random rand = new Random();
    
    float x, y, w, h, alpha;
    int depth;
    boolean active;
    int[][] layout;
    
    protected final int NULL = -1;
    
    int elements_x;
    int elements_y;
    int num_elements = 0;
    engine_guiElement[] elemments;
    engine_guiElement tmp_element;

    public engine_reference ref;
    public engine_gui(engine_reference r) {
        ref = r;
    }
    
    public void setLayout(int[][] layout) {
        this.layout = layout;
        
        elements_x = layout[0][0];
        elements_y = layout[0][1];
        num_elements = elements_x * elements_y;
        
        elemments = new engine_guiElement[num_elements];
    }
    
    public void build() {

        float[] row_weight = new float[elements_x];
        int[] row_ids = new int[elements_x];
        int[] row_action = new int[elements_x];
        int row_i = 0;
        
        float[] element_x = new float[elements_x*elements_y];
        float[] element_y = new float[elements_x*elements_y];
        float[] element_w = new float[elements_x*elements_y];
        float[] element_h = new float[elements_x*elements_y];
        int[] element_type = new int[elements_x*elements_y];
        int[] element_action = new int[elements_x*elements_y];
        int element_i = 0;
        
        int ui_i = 0;
        
        for (int iy=0; iy<elements_y; iy++) {
            
//            float y = this.y+h/2 - (iy+0.5f)*(h/((float)elements_y) );
            
            row_i = 0;
            for (int ix=0; ix<elements_x; ix++) {
                
                int id = layout[1][ui_i];//TODO TYPE
                
                if (id != -1) {
                    row_weight[row_i] = 1;//TODO weights
                    row_ids[row_i] = id;
                    row_i++;
                }
                
                ui_i++;
                
            }
            
            //sum row weight
            float total_row_weight = 0;
            for (int i=0; i<row_i; i++) {
                total_row_weight += row_weight[i];
            }

            //fill element data
            float placed_width = 0;
            for (int i=0; i<row_i; i++) {
                
                float width = w * (row_weight[i]/total_row_weight);
                float height = h/(elements_y);
                
//                Log.e("popup", "i" + element_i);
//                Log.e("popup", "type" + row_ids[i]);
//                Log.e("popup", "w" + width);
//                Log.e("popup", "h" + height);
                
                elemments[element_i].setSize(width, height);
                
                float X = -w/2 + placed_width + width/2f;
                float Y = h/2 - iy*height - height/2f;
                
                elemments[element_i].setPosition(X, Y);
                
                placed_width += width;
                
                
                //element_type[element_i] = row_type[i];
                //element_action[element_i] = row_action[i];
                element_i++;
                
            }
            
        }
        
        computePositions();
        
    }
    
    private void computePositions() {
        for (int i=0;i<num_elements;i++) {
            
            tmp_element = elemments[i];
            
            if (tmp_element != null) {
                tmp_element.computeSizesAndCenters();
            }
        }
    }
            
    
    public void addElement(engine_guiElement element) {
        elemments[element.getID()] = element;
        element.computeSizesAndCenters();
    }

    public void draw() {
        
        if (active) {
            //Draw elements
            for (int i=0;i<num_elements;i++) {
                
                tmp_element = elemments[i];
                
                if (tmp_element != null) {
                    tmp_element.update();
                }
            }
        }
            
            /*
            ref.draw.setDrawColor(ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), popup_alpha);
            ref.draw.drawRectangle(element_x[i], element_y[i], element_w[i], element_h[i], 0, 0, 0, 100);
            
            switch(element_type[i]) {
                case UI_TITLE:
                    ref.draw.text.append("TITLE");
                    break;
                case UI_TEXT:
                    ref.draw.text.append("TEXT");
                    break;
                case UI_BUTTON:
                    ref.draw.text.append("BUTTON");
                    break;
                case UI_CHECKBOX:
                    ref.draw.text.append("CHECK");
                    break;
            }
            ref.draw.setDrawColor(1, 1, 1, popup_alpha);
            ref.draw.drawText(element_x[i], element_y[i], mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 200, textures.TEX_FONT1);
            */
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }
    
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
}