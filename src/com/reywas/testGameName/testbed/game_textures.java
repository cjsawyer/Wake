package com.reywas.testGameName.testbed;

import com.reywas.testGameName.gameEngine.engine_reference;

public final class game_textures {

	public static final int TEX_ERROR = 1;
	public static final int TEX_FONT1 = 2;
	
//	public static final int TEX_SPRITES = 3;
//	public static final int SUB_subsprite1 = 1;
	
	public static float[][] texture_locations_arrays = {
			{ 32, 32,   0,32, 32,32, 0,0, 32,0, }, // TEX_ERROR --- DO NOT EDIT
			{}, // font_sheet_1
//			{ 1024, 1024,  0,950+64, 0+64,950+64, 0,950, 0+64,950,  0,886+64, 0+64,886+64, 0,886, 0+64,886,  0,822+64, 0+64,822+64, 0,822, 0+64,822,  0,0+370, 0+600,0+370, 0,0, 0+600,0,  0,370+452, 0+454,370+452, 0,370, 0+454,370,  64,822+58, 64+58,822+58, 64,822, 64+58,822,  },
	};
	
	// These are the names of the PNG's in the assets folder.
	public static String[] texture_name_array = {
			"error",
			"font_sheet_1", // generated at runtime, so there is no file in assets
//			"wakesheet",
	};
	
	// These are used for font loading
	public static String[] font_name_and_extension = {
		// Put null if not a font.
		null,
		"Square.ttf",
//		null,
	};
	
	public static int[] font_size = {
		// Put -1 if not a font.
		-1,
		50,
//		-1,
	};
	public static float[] font_stroke_color = {
		// If not a font (or a font with no stroke):
		//-1,   -1,   -1,      -1,   -1,   -1,
		// Inner color          Outer color
		  -1,   -1,   -1,      -1,   -1,   -1,
		   0,    0,    0,       1,    1,    1, // font 1
//		  -1,   -1,   -1,      -1,   -1,   -1,
	};	
}
