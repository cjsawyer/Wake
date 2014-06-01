package com.mammothGames.wake1.gameEngine;

import java.util.Random;

import android.util.Log;

import com.mammothGames.wake1.game.textures;

public class engine_gui {

    Random rand = new Random();
    
    float x, y, w, h;

	public float alpha;
    public int depth;
    boolean active;
    int[][] layout;
    
    protected final int NULL = -1;
    
    int elements_x;
    int elements_y;
    int num_elements = 0;
    engine_guiElement[] elements;
    engine_guiElement tmp_element;

    public engine_reference ref;
    public engine_gui(engine_reference ref) {
        this.ref = ref;
    }
    
    public void setLayout(int[][] layout) {
        this.layout = layout;
        
        elements_x = layout[0][0];
        elements_y = layout[0][1];
        num_elements = elements_x * elements_y;
        
        elements = new engine_guiElement[num_elements];
    }
    
    public void build() {

        float[] row_weight = new float[elements_x];
        float[] column_weight = new float[elements_x];
        boolean[] last_in_row = new boolean[num_elements];
        int[] row_ids = new int[elements_x];
        
        int row_i = 0;
        int element_i = 0;
        int ui_i = 0;
        
        float total_column_weight = 0;
        
        for (int iy=0; iy<elements_y; iy++) {
            
            row_i = 0;
            
            for (int ix=0; ix<elements_x; ix++) {
                
                int id = layout[1][ui_i];
                
                // if this isn't an empty cell
                if (id != -1) {
                    row_weight[row_i] = elements[element_i+row_i].weightH;
                    column_weight[row_i] = elements[element_i+row_i].weightV;
                    row_ids[row_i] = id;
                    row_i++;
                }
                
                ui_i++;
                
            }
            
            //sum weight in row
            float total_row_weight = 0;
            for (int i=0; i<row_i; i++) {
                total_row_weight += row_weight[i];
            }
            
            //find max column weight in this row
            float max_column_weight = 0;
            for (int i=0; i<row_i; i++) {
                if (column_weight[i] > max_column_weight) {
                    max_column_weight = column_weight[i];
                }
            }
            
            total_column_weight += max_column_weight;

            //fill element data
            float placed_width = 0;
            for (int i=0; i<row_i; i++) {
                
                float width = w * (row_weight[i]/total_row_weight);
                
                // the second arg is to avoid making another array
                // it stores the vertical weight of the current element  
                elements[element_i].setSize(width, max_column_weight);
                float X = -w/2 + placed_width + width/2f;
                elements[element_i].setPosition(X, 0);
                
                placed_width += width;
                
                if (i==row_i-1)
                    last_in_row[element_i] = true;
                else
                    last_in_row[element_i] = false;
                
                element_i++;
                
            }
            
        }
        
        float placed_height = 0;
        element_i =0;
        
        // Set component Ys and heights
        for (int i=0;i<num_elements;i++) {
            
            tmp_element = elements[i];
                
            if (tmp_element != null) {
                
                float height = h * (tmp_element.h/total_column_weight);
                float Y = h/2 - placed_height - height/2f;
                
                float width = tmp_element.w;
                float X = tmp_element.rx;
                
                tmp_element.setPosition(X, Y);
                tmp_element.setSize(width, height);
                
                if (last_in_row[i])
                    placed_height += height;
                
            }
    
        }
        
        
        // Compute all sizes/positions
        for (int i=0;i<num_elements;i++) {
            tmp_element = elements[i];
            if (tmp_element != null) {
                tmp_element.computeSizesAndCenters();
            }
        }
        
    }
    
    public void addElement(engine_guiElement element) {
        //TODO: change this if I ever implement dynamic GUI's. It wouldn't work if we're adding and removing
        elements[element.getID()] = element;
    }

    public void update() {
        if (active)
            for (int i=0;i<num_elements;i++) {
                tmp_element = elements[i];
                
                if (tmp_element != null) {
                    tmp_element.update();
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
    
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getH() {
        return h;
    }
    public float getW() {
        return w;
    }
}