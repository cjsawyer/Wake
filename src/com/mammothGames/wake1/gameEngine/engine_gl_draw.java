package com.mammothGames.wake1.gameEngine;

import com.mammothGames.wake1.game.textures;

import android.util.Log;

public class engine_gl_draw {

        //////////////////////////////////////////////////////////////////
        //                To add a new texture:
        //
        //                        (textures need to be png)
        //                        Go to texture_name_array (right above this)
        //                        add 
        //                                "sprite_name_here",
        //                        then to to engine_gl_texture, and add a new array at the same index you added the name here
        /////////////////////////////////////////////////////////////////


        private final engine_reference ref;
        
        
        protected engine_gl_drawlist mainDrawList, lastDrawList, capturedDrawList;

        public engine_gl_draw(engine_reference r){
                ref = r;

                ref.texture = new engine_gl_texture(ref);
                ref.rectangle = new engine_gl_rectangle(ref);
                ref.circle = new engine_gl_circle(ref);
                // text = new engine_gl_text(c, renderer, texture, 1, 50, "test_font.otf");
                if (ref.textureLoader == null)
                        ref.textureLoader = new engine_gl_textureLoader(ref);
                ref.text = new engine_gl_text(ref);//, 1, 50, null);//Square.ttf
                
                mainDrawList = new engine_gl_drawlist();
                initList(mainDrawList);
                
                capturedDrawList = new engine_gl_drawlist();
                initList(capturedDrawList);
                
                lastDrawList = new engine_gl_drawlist();
                initList(lastDrawList);
                
                text = new StringBuilder(500);
        		text.setLength(0);
        }

        
        private char[] stringChars = new char[500];
    	public StringBuilder text = new StringBuilder(500);
        

        private void initList(engine_gl_drawlist list){
                int lengths = 10000;
                
                list.list_x = new float[lengths];
                list.list_y = new float[lengths];
                list.list_x2 = new float[lengths];
                list.list_y2 = new float[lengths];
                list.list_size_x = new float[lengths];
                list.list_size_y = new float[lengths];
                list.list_origin_x = new float[lengths];
                list.list_origin_y = new float[lengths];
                list.list_draw_angle = new float[lengths];

                list.list_depth = new int[lengths];
                list.list_textureID = new int[lengths];
                list.list_texture_sheet = new int[lengths];
                list.list_draw_type = new int[lengths];

                list.list_color_r = new float[lengths];
                list.list_color_g = new float[lengths];
                list.list_color_b = new float[lengths];
                list.list_color_a = new float[lengths];
                
                list.overlay = new boolean[lengths];
                
        }


        public float getSubTextureWidth(int subTextureID, int sheetID) {
                return (
//                                ref.g_textures.texture_locations_arrays[sheetID-1][  (  ( subTextureID ) * 8  ) - 6  + 2  ]
                                ref.loaded_textures.get_texCoords(sheetID-1)[  (  ( subTextureID ) * 8  ) - 6  + 2  ]
                                -
//                                ref.g_textures.texture_locations_arrays[sheetID-1]  [  (( subTextureID ) * 8  ) - 4  + 2  ]
                                ref.loaded_textures.get_texCoords(sheetID-1)[  (( subTextureID ) * 8  ) - 4  + 2  ]
                           );
        }
        
        public float getSubTextureHeight(int subTextureID, int sheetID) {
                return (
                                        ref.loaded_textures.get_texCoords(sheetID-1)[  (  ( subTextureID ) * 8  ) - 5  + 2  ]
                                        -
                                        ref.loaded_textures.get_texCoords(sheetID-1)[  (( subTextureID ) * 8  ) - 3  + 2  ]
                           );
        }

        /**
         * 
         * @param x
         * @param y
         * @param size_x
         * @param size_y
         * @param origin_x
         * @param origin_y
         * @param draw_angle
         * @param depth
         * @param textureID
         * @param texture_sheet
         */
        public void drawTexture(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float draw_angle, int depth, int textureID, int texture_sheet){
        	drawTexture(x, y, size_x, size_y, origin_x, origin_y, draw_angle, depth, textureID, texture_sheet, false);
        }
        
        /**
         * 
         * @param x
         * @param y
         * @param size_x
         * @param size_y
         * @param origin_x
         * @param origin_y
         * @param draw_angle
         * @param depth
         * @param textureID
         * @param texture_sheet
         * @param overlay Set to true to make this draw call not be captured in screenshots
         */
        public void drawTexture(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float draw_angle, int depth, int textureID, int texture_sheet, boolean overlay){
                if (ref.textureLoader.has_loaded[texture_sheet-1]) {
                        addToDrawList(x, y, 0, 0, size_x, size_y, origin_x, origin_y, draw_angle, depth, textureID, texture_sheet, DRAW_TYPE_TEXTURE, overlay);
                } else {
                        setDrawColor(1,1,1,1);
                        addToDrawList(x, y, 0, 0, size_x, size_y, origin_x, origin_y, draw_angle, depth, 1, textures.TEX_ERROR, DRAW_TYPE_TEXTURE, overlay);
                }
        }
        
        /**
         * 
         * @param x
         * @param y
         * @param size_x
         * @param size_y
         * @param origin_x
         * @param origin_y
         * @param draw_angle
         * @param depth
         */
        public void drawRectangle(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float draw_angle, int depth){
        	drawRectangle(x, y, size_x, size_y, origin_x, origin_y, draw_angle, depth, false);
        }
        /**
         * 
         * @param x
         * @param y
         * @param size_x
         * @param size_y
         * @param origin_x
         * @param origin_y
         * @param draw_angle
         * @param depth
         * @param overlay
         */
        public void drawRectangle(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float draw_angle, int depth, boolean overlay){
                addToDrawList(x, y, 0, 0, size_x, size_y, origin_x, origin_y, draw_angle, depth, 0, 0, DRAW_TYPE_RECTANGLE, overlay);
        }
        
        /**
         * 
         * @param x
         * @param y
         * @param radius
         * @param origin_x
         * @param origin_y
         * @param arc_angle
         * @param draw_angle
         * @param depth
         */
        public void drawCircle(float x, float y, float radius, float origin_x, float origin_y, float arc_angle, float draw_angle, int depth){
        	drawCircle(x, y, radius, origin_x, origin_y, arc_angle, draw_angle, depth, false);
        }
        /**
         * 
         * @param x
         * @param y
         * @param radius
         * @param origin_x
         * @param origin_y
         * @param arc_angle
         * @param draw_angle
         * @param depth
         * @param overlay
         */
        public void drawCircle(float x, float y, float radius, float origin_x, float origin_y, float arc_angle, float draw_angle, int depth, boolean overlay){
                addToDrawList(x, y, arc_angle, draw_angle, radius, radius, origin_x, origin_y, draw_angle, depth, 0, 0, DRAW_TYPE_CIRCLE, overlay);
        }
        
        public void drawLine(float x, float y, float x2, float y2, float width, int depth){
        	drawLine(x, y, x2, y2, width, depth, false);
        }
        /**
         * 
         * @param x
         * @param y
         * @param x2
         * @param y2
         * @param width
         * @param depth
         * @param overlay
         */
    	public void drawLine(float x, float y, float x2, float y2, float width, int depth, boolean overlay){
                addToDrawList(x, y, x2, y2, width, 0, 0, 0, 0, depth, 0, 0, DRAW_TYPE_LINE, overlay);
        }
        
        public void setDrawColor(float r, float g, float b, float a){
                ref.floatbuffers.changeDrawColor(r, g, b, a);
        }

        private char  temp_char;
        private int   temp_string_length;
        private float temp_total_width;
        private float temp_scale;
        private float temp_alignment_y;
        private float temp_alignment_x;

        final public static int X_ALIGN_LEFT = 0;
        final public static int X_ALIGN_CENTER = 1; 
        final public static int X_ALIGN_RIGHT = 2; 
        
        final public static int Y_ALIGN_TEXT_BASELINE = 0; 
        final public static int Y_ALIGN_BOTTOM = 1; 
        final public static int Y_ALIGN_CENTER = 2; 
        final public static int Y_ALIGN_TOP = 3;

        public void drawText(float x, float y, float size, int x_align, int y_align, int depth, int texture_sheet){
        	sys_drawText(x, y, size, x_align, y_align, depth, texture_sheet, true, false);
        }
        /**
         * 
         * @param x
         * @param y
         * @param size
         * @param x_align
         * @param y_align
         * @param draw_angle
         * @param depth
         * @param charArray
         * @param charLength
         * @param texture_sheet
         * @param overlay
         * @return
         */      
        public void drawText(float x, float y, float size, int x_align, int y_align, int depth, int texture_sheet, boolean overlay){
        	sys_drawText(x, y, size, x_align, y_align, depth, texture_sheet, true, overlay);
        }
        
        public float getTextWidth(float size, int texture_sheet){
        	return sys_drawText(0, 0, size, 0, 0, 0, texture_sheet, false, false);
        }
        
    	private float sys_drawText(float x, float y, float size, int x_align, int y_align, int depth, int texture_sheet, boolean draw_characters, boolean overlay){
            if (ref.textureLoader.has_loaded[texture_sheet-1]) {
            	
//            	char[] charArray = ;
            	text.getChars(0, text.length(), stringChars, 0);
            	
            	int charLength = text.length();
            	
            	
                    temp_string_length = charLength;
                    temp_scale = size/ref.text.size;
                    temp_total_width = 0;
                    
                    switch(y_align){
                            case Y_ALIGN_TEXT_BASELINE:
                                    temp_alignment_y = ((ref.text.padding_y[texture_sheet-1] - ref.text.font_ascent[texture_sheet-1])*temp_scale);
                                    break;
                            case Y_ALIGN_BOTTOM:
                                    temp_alignment_y = ((ref.text.padding_y[texture_sheet-1] - ref.text.font_descent[texture_sheet-1])*temp_scale);
                                    break;
                            case Y_ALIGN_CENTER:
                                    temp_alignment_y =  ((-ref.text.padding_y[texture_sheet-1] - (ref.text.font_height[texture_sheet-1]/2))*temp_scale);
                                    break;
                            case Y_ALIGN_TOP:
                                    temp_alignment_y = ((-ref.text.padding_y[texture_sheet-1] - ref.text.font_ascent[texture_sheet-1] - ref.text.font_descent[texture_sheet-1])*temp_scale);
                                    break;
                            default:
                                    Log.e("reywas","ERROR: In engine_gl_draw.drawText(), use ref.draw.X/Y_ALIGN_* for y_align and x_align arguments.");
                    }
                    
                    // Get the total scaled length in pixels of the string to draw for horizontal alignment.
                    for(int i=0; i < charLength; i++){
                            temp_char = stringChars[i];
                            for(int i_char_width=32; i_char_width<126;i_char_width++){
                                    if (((char) i_char_width)  == temp_char){
                                            temp_total_width += ((ref.text.char_widths[texture_sheet-1][i_char_width-31]) * temp_scale);
                                    }
                            }
                    }
                    
                    switch(x_align){
                            case X_ALIGN_RIGHT:
                                    temp_alignment_x = -(temp_total_width);
                                    break;
                            case X_ALIGN_CENTER:                                
                                    temp_alignment_x = -(temp_total_width/2);
                                    break;
                            case X_ALIGN_LEFT:                                
                                    temp_alignment_x = 0;
                                    break;
                    }
                    
                    
                    temp_total_width = 0;
                    for(int i=0; i < temp_string_length; i++){
    
                            temp_char = stringChars[i];
    
                            for(int i_char_width=32; i_char_width<126;i_char_width++){
                                    if (((char) i_char_width)  == temp_char){
                                        	if (draw_characters)
                                                addToDrawList(
                                                		x + temp_total_width - (ref.text.padding_x[texture_sheet-1]*temp_scale) + temp_alignment_x,
                                                		y + temp_alignment_y,
                                                		0,
                                                		0,
                                                		(ref.text.cell_width[texture_sheet-1])*temp_scale,
                                                		(ref.text.cell_height[texture_sheet-1])*temp_scale,
                                                		-(ref.text.cell_width[texture_sheet-1]/2)*temp_scale,
                                                		-(ref.text.cell_height[texture_sheet-1]/2)*temp_scale,
                                                		0, depth, i_char_width-31, texture_sheet, DRAW_TYPE_TEXTURE,
                                                		overlay);
                                            
    //                                        addToDrawList(x + temp_total_width - (ref.text.padding_x[texture_sheet-1]*temp_scale) + temp_alignment_x, y + temp_alignment_y, 0, 0, (ref.text.cell_width[texture_sheet-1])*temp_scale, (ref.text.cell_height[texture_sheet-1])*temp_scale, (ref.text.cell_width[texture_sheet-1]/2)*temp_scale, (ref.text.cell_height[texture_sheet-1]/2)*temp_scale, rotate_angle, depth, temp_i_char_width-31, texture_sheet, DRAW_TYPE_TEXTURE);
                                            temp_total_width += ((ref.text.char_widths[texture_sheet-1][i_char_width-31]) * temp_scale);
                                    }
                            }
    
                    }
            } else {
                    // The texture isn't loaded. Add this dummy call with the error texture so the draw color system isn't effected.
                    // I'm estimating the width/height ratio of an average character is 3/4. This times the number of letters is about how wide a string would be.
                    temp_total_width = size * 3/4 * text.length();
                    
                    switch(x_align){
                            case X_ALIGN_RIGHT:
                                    temp_alignment_x = -(temp_total_width);
                                    break;
                            case X_ALIGN_CENTER:                                
                                    temp_alignment_x = (temp_total_width/2);
                                    break;
                            case X_ALIGN_LEFT:                                
                                    temp_alignment_x = (temp_total_width);
                                    break;
                    }
                    
                    switch(y_align){
                            case Y_ALIGN_TEXT_BASELINE:
                            case Y_ALIGN_BOTTOM:
                                    temp_alignment_y = size/2;
                                    break;
                            case Y_ALIGN_CENTER:
                                    temp_alignment_y =  0;
                                    break;
                            case Y_ALIGN_TOP:
                                    temp_alignment_y = -size/2;
                                    break;
                    }
                    
                    temp_alignment_x=0;
                    if (draw_characters) {
                    	ref.draw.setDrawColor(1, 1, 1, 1);
                    	addToDrawList(x, y, 0, 0, temp_total_width , size, 0, 0, 0, depth, 1, textures.TEX_ERROR, DRAW_TYPE_TEXTURE, overlay);
                    }
            }
            
            text.setLength(0);
            
            return temp_total_width;
    }

        private final int DRAW_TYPE_TEXTURE= 0;
        private final int DRAW_TYPE_RECTANGLE = 1;
        private final int DRAW_TYPE_LINE = 2;
        private final int DRAW_TYPE_CIRCLE = 3;
        
        private void addToDrawList(float x, float y, float x2, float y2, float size_x, float size_y, float origin_x, float origin_y, float rotate_angle, int depth, int textureID, int texture_sheet, int draw_type, boolean overlay){
                
                int temp_draw_number = mainDrawList.temp_number_of_calls;
                
                mainDrawList.list_x[temp_draw_number] = x;
                mainDrawList.list_y[temp_draw_number] = y;
                mainDrawList.list_x2[temp_draw_number] = x2;
                mainDrawList.list_y2[temp_draw_number] = y2;
                mainDrawList.list_size_x[temp_draw_number] = size_x;
                mainDrawList.list_size_y[temp_draw_number] = size_y;
                mainDrawList.list_origin_x[temp_draw_number] = origin_x;
                mainDrawList.list_origin_y[temp_draw_number] = origin_y;
                mainDrawList.list_draw_angle[temp_draw_number] = rotate_angle;

                mainDrawList.list_depth[temp_draw_number] = depth;
                mainDrawList.list_textureID[temp_draw_number] = textureID;
                mainDrawList.list_texture_sheet[temp_draw_number] = texture_sheet;
                mainDrawList.list_draw_type[temp_draw_number] = draw_type;

                mainDrawList.list_color_r[temp_draw_number] = ref.floatbuffers.system_current_r;
                mainDrawList.list_color_g[temp_draw_number] = ref.floatbuffers.system_current_g;
                mainDrawList.list_color_b[temp_draw_number] = ref.floatbuffers.system_current_b;
                mainDrawList.list_color_a[temp_draw_number] = ref.floatbuffers.system_current_a;

                mainDrawList.overlay[temp_draw_number] = overlay;
                
                mainDrawList.temp_number_of_calls += 1;
                
                if (depth > mainDrawList.temp_max_depth){
                        mainDrawList.temp_max_depth = depth;
                }
        }
        
        final float RAD_TO_DEG = (float) (180/Math.PI);
        
        private void drawList(engine_gl_drawlist list, boolean clearList, boolean list_is_captured){
                int temp_draw_type;
                int temp_array_lengths = 0;
                
                int i_depth;
                int i_list_index;
                
                // All for line drawing.
                float x1;
                float x2;
                float y1;
                float y2;
                float length;
                float width;
                
                // for each draw call,
                        temp_array_lengths = list.temp_number_of_calls;
                        
                        for (i_depth=0; i_depth <= list.temp_max_depth; i_depth++){
                                // loop through all of the arrays,
                                for (i_list_index=0; i_list_index < temp_array_lengths; i_list_index++){
                                        // if this is the correct depth, draw
                                        if (list.list_depth[i_list_index] == i_depth) {
                                        	// do not draw the overlay draw calls if this is a captured draw list
                                        	if (!(list_is_captured && list.overlay[i_list_index])){
                                                
                                                temp_draw_type = list.list_draw_type[i_list_index];
                                                
                                                ref.floatbuffers.system_changeDrawColor(
                                                                list.list_color_r[i_list_index],
                                                                list.list_color_g[i_list_index],
                                                                list.list_color_b[i_list_index],
                                                                list.list_color_a[i_list_index]);
                                                
                                                switch(temp_draw_type){
                                                        case DRAW_TYPE_TEXTURE:{
                                                                
                                                                ref.texture.draw(
                                                                                list.list_x[i_list_index],
                                                                                list.list_y[i_list_index],
                                                                                list.list_size_x[i_list_index],
                                                                                list.list_size_y[i_list_index],
                                                                                list.list_origin_x[i_list_index],
                                                                                list.list_origin_y[i_list_index],
                                                                                list.list_draw_angle[i_list_index],
                                                                                list.list_depth[i_list_index],
                                                                                list.list_textureID[i_list_index],
                                                                                list.list_texture_sheet[i_list_index]);
                                                                
                                                                break;
                                                        }
                                                        case DRAW_TYPE_RECTANGLE:{
                                                                
                                                                ref.rectangle.draw(
                                                                                list.list_x[i_list_index],
                                                                                list.list_y[i_list_index],
                                                                                list.list_size_x[i_list_index],
                                                                                list.list_size_y[i_list_index],
                                                                                list.list_origin_x[i_list_index],
                                                                                list.list_origin_y[i_list_index],
                                                                                list.list_draw_angle[i_list_index],
                                                                                list.list_depth[i_list_index]);
                                                                
                                                                break;
                                                        }
                                                        case DRAW_TYPE_LINE:{
                                                                
                                                                x1 = list.list_x[i_list_index];
                                                                x2 = list.list_x2[i_list_index];
                                                                
                                                                y1 = list.list_y[i_list_index];
                                                                y2 = list.list_y2[i_list_index];
                                                                
                                                                length = (float) Math.hypot(x1 - x2, y1 - y2);
                                                                width = list.list_size_x[i_list_index];
                                                                //hack for non zero arguments
                                                                ref.rectangle.draw(x1, y1, length, width/2, -length/2, 0, (float) ((RAD_TO_DEG) * Math.atan2(y2 - y1 + 0.000001f, x2 - x1 + 0.000001f)), mainDrawList.list_depth[i_list_index]);
                                                                // public void draw(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float rotate_angle, float depth){...}
                                                                break;
                                                        }
                                                        case DRAW_TYPE_CIRCLE:{
                                                                
                                                                ref.circle.draw(
                                                                                list.list_x[i_list_index],
                                                                                list.list_y[i_list_index],
                                                                                list.list_x2[i_list_index],
                                                                                list.list_size_x[i_list_index],
                                                                                list.list_size_y[i_list_index],
                                                                                list.list_origin_x[i_list_index],
                                                                                list.list_origin_y[i_list_index],
                                                                                list.list_draw_angle[i_list_index],
                                                                                list.list_depth[i_list_index]);
                                                                
                                                                // public void draw(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float rotate_angle, float depth){...}
                                                                break;
                                                        }
                                                }//end switch
                                                temp_draw_type = -1;
                                        	}
                                        }
                                }
                        }
                        if (clearList) {
                                list.temp_max_depth = 0;
                                list.temp_number_of_calls = 0;
                        }
        }
        /**
         * Documentation, how does it work?
         * @param a This is a test, yo.
         */
        public void executeDraw(){
                
                
                if (captureDrawList) {
                        mainDrawList.copy(capturedDrawList);
//                        lastDrawList.copy(capturedDrawList);
                } else {
                        // We only want to draw the captured list if we aren't still in the same frame it was captured in, to avoid drawing it twice.
                        if (drawCapturedDrawList) {
                                drawList(capturedDrawList, false, true);
                        }
                }
                
                // Draw main drawList
                drawList(mainDrawList, true, false);
                
                // Reset
                captureDrawList = false;
                drawCapturedDrawList = false;
        }
        
        public void setBackgroundColor(float r, float g, float b){
                ref.renderer.sys_changeClearColor(r, g, b);
        }
        
        
        private boolean captureDrawList = false, drawCapturedDrawList = false;
        public void drawCapturedDraw() {
                drawCapturedDrawList = true;
        }
        public void captureDraw() {
                captureDrawList = true;
        }
        
        public float hsvToRgb(float hue, float saturation, float value, int r_g_or_b) {

                // From: http://stackoverflow.com/questions/7896280/converting-from-hsv-hsb-in-java-to-rgb-without-using-java-awt-color-disallowe
            int h = (int)(hue * 5);
            float f = hue * 6 - h;
            float p = value * (1 - saturation);
            float q = value * (1 - f * saturation);
            float t = value * (1 - (1 - f) * saturation);

            float r,g,b;
            switch (h) {
                        case 0: {r=value; g=t; b=p; break;} //return rgbToString(value, t, p);
                        case 1: {r=q; g=value; b=p; break;} //return rgbToString(q, value, p);
                        case 2: {r=p; g=value; b=t; break;} //return rgbToString(p, value, t);
                        case 3: {r=p; g=q; b=value; break;} //return rgbToString(p, q, value);
                        case 4: {r=t; g=p; b=value; break;} //return rgbToString(t, p, value);
                        case 5: {r=value; g=p; b=q; break;} //return rgbToString(value, p, q);
                        default: {
                                r=0;
                                g=0;
                                b=0;
                                Log.e("reywas","Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
                        }
            }
            
//            Log.e("reywas","r " + r + "  g " + g +"  b " + b);
            switch (r_g_or_b) {
                    case 0: return r; // This generates rgb values from 0 to 255. The engine uses 0 to 1
                    case 1: return g;
                    case 2: return b;
                    default: {
                            Log.e("reywas","Invalid argument. Use 0, 1, or 2 for r, g, or b to be returned");
                            return 0;
                    }
            }
        }
}

class engine_gl_drawlist {
        
        int temp_max_depth = 0;
        int temp_number_of_calls = 0;
        
        float[] list_x;
        float[] list_y;
        float[] list_x2;
        float[] list_y2;
        float[] list_size_x;//also used as width
        float[] list_size_y;
        float[] list_origin_x;
        float[] list_origin_y;
        float[] list_draw_angle;
        int[] list_depth;
        int[] list_textureID;
        int[] list_texture_sheet;
        int[] list_draw_type;
        float[] list_color_r;
        float[] list_color_g;
        float[] list_color_b;
        float[] list_color_a;
        boolean[] overlay;
        
        public void copy(engine_gl_drawlist outputList) {
                outputList.temp_max_depth = temp_max_depth;
                outputList.temp_number_of_calls = temp_number_of_calls;
                
                outputList.list_x = list_x.clone();
                outputList.list_y = list_y.clone();
                outputList.list_x2 = list_x2.clone();
                outputList.list_y2 = list_y2.clone();
                outputList.list_size_x = list_size_x.clone();
                outputList.list_size_y = list_size_y.clone();
                outputList.list_origin_x = list_origin_x.clone();
                outputList.list_origin_y = list_origin_y.clone();
                outputList.list_draw_angle = list_draw_angle.clone();
                outputList.list_depth = list_depth.clone();
                outputList.list_textureID = list_textureID.clone();
                outputList.list_texture_sheet = list_texture_sheet.clone();
                outputList.list_draw_type = list_draw_type.clone();
                outputList.list_color_r = list_color_r.clone();
                outputList.list_color_g = list_color_g.clone();
                outputList.list_color_b = list_color_b.clone();
                outputList.list_color_a = list_color_a.clone();
                outputList.overlay = overlay.clone();
        }
}