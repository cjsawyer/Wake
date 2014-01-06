package com.mammothGames.wake.game;

import android.util.Log;

public final class game_textures {

//	public static final TEX_*
	public static final int TEX_ERROR = 1;
	public static final int TEX_FONT1 = 2;
	
	public static final int TEX_SPRITES = 3;
	
	public static final int SUB_MUTED = 1;
	public static final int SUB_MUTE = 2;
	public static final int SUB_PAUSE = 3;
	public static final int SUB_LOGO = 4;
	public static final int SUB_MAMMOTH = 5;
	public static final int SUB_PARTICLE = 6;
	
	public static final int TEX_STARS = 4;
	
//	Use the command line tool in the tools directory
	private float[][] texture_locations_arrays = {
			{ 32, 32,   0,32, 32,32, 0,0, 32,0, }, // TEX_ERROR --- DO NOT EDIT
			
			{}, // font_sheet_1
			
			{ 1024, 1024,  0,950+64, 0+64,950+64, 0,950, 0+64,950,  0,886+64, 0+64,886+64, 0,886, 0+64,886,  0,822+64, 0+64,822+64, 0,822, 0+64,822,  0,0+370, 0+600,0+370, 0,0, 0+600,0,  0,370+452, 0+454,370+452, 0,370, 0+454,370,  64,822+58, 64+58,822+58, 64,822, 64+58,822,  },
			
			{ 512, 512, 0,512, 512,512, 0,0, 512,0}, //stars
//			{ 64,64,   0,64, 64,64, 0,0, 64,0,}, // particle texture
//;
//			{ 600,370,   0,370, 600,370, 0,0, 600,0, }, // Neon mist logo
//			
//			{ 198,66,    0,66, 66,66, 0,0, 66,0,  66,66,132,66,66,0,132,0,  132,66,198,66,132,0,198,0,   }, // menu buttons
//			
//			{ 512, 512,       0, 512,       512,        512,       0, 0,     512,   0,   }, // mammoth logo
//			{ 516, 516   ,    2, 516-2+512, 2+512,      516-2+512, 2, 516-2, 2+512, 516-2,   },
//			{ 516, 516,       2, 516-2+512, 2+512,      516-2+512, 2,516-2, 2+512,516-2,   },
			
			/*
			TODO TODO TODO TODO TODO 
			{ }, // texture_name_here
			TODO TODO TODO TODO TODO 
			*/
			
	};
	
	// These are the names of the PNG's in the assets folder.
	private String[] texture_name_array = {
			"error",
			"font_sheet_1", // generated at runtime, so there is no file in assets
			"wakesheet",
			"stars",
	};
	
	// These are used for font loading
	private String[] font_name_and_extension = {
		// Put null if not a font.
		null,
		"Square.ttf",
		null,
		null,
	};
	
	private int[] font_size = {
		// Put -1 if not a font.
		-1,
		50,
		-1,
		-1,
	};
	private float[][] font_stroke_color = {
		// If not a font (or a font with no stroke):
		//-1,   -1,   -1,      -1,   -1,   -1,
		//
		// Inner color          Outer color
		  {-1,   -1,   -1,      -1,   -1,   -1},
		  { 0,    0,    0,       1,    1,    1}, // font 1
		  {-1,   -1,   -1,      -1,   -1,   -1},
		  {-1,   -1,   -1,      -1,   -1,   -1},
	};	

	public int get_numTextures() {
		return texture_name_array.length;
	}
	public float[] get_texCoords(int id) {
		return texture_locations_arrays[id];
	}
	public void set_texCoords(int id, float[] array) {
		texture_locations_arrays[id] = array;
	}
	public void set_texCoords(int id, int array_index, float array_value) {
		texture_locations_arrays[id][array_index] = array_value;
	}
	public String get_texNameAndExtension(int id) {
		return texture_name_array[id];
	}
	public String get_fontNameAndExtension(int id) {
		return font_name_and_extension[id];
	}
	public boolean getIsFont(int id) {
		return (font_name_and_extension[id].equals(null)) ? false:true;
	}
	public int getFontSize(int id) {
		return font_size[id];
	}
	public float[] getStrokeColor(int id) {
		return font_stroke_color[id];
	}
	
}