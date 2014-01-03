package com.mammothGames.wake.gameEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mammothGames.wake.game.game_textures;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.FloatMath;
import android.util.Log;

public class engine_gl_text {

	Canvas c;
	Paint p, stkPaint;
	Bitmap b;
	Bitmap output;
	FontMetrics fm;
	int size;
	int font_texture_sheet = 0;
	String font_name_and_extension;

	
	final engine_reference ref;

	public engine_gl_text(engine_reference r){
//		public engine_gl_text(engine_reference r, int texture_sheet, int size, String font_name_and_extension){
		
		ref = r;

		p = new Paint();
		stkPaint = new Paint();
		
	}

	//*
	private void save_generated_font_atlas(String filename){
			FileOutputStream out;
			try {
				out = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + filename + ".png");
				b.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				//Log.e("reywas","files: " + context.getAssets().list("textures/font1.png"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	//*/

	
	public void recycleBitmap() {
		b.recycle();
	}
	public Bitmap returnGeneratedFontAtlas(int texture_sheet, int size, String font_name_and_extension){
		if (ref.textureLoader.has_loaded[texture_sheet-1] == false) {
			
			Log.d("reywas", "About to generate font atlas.");
			
			boolean stroke = false;
//			if (game_textures.font_stroke_color[((texture_sheet-1)*6)] >= 0) // 6 is stride
			if (game_textures.font_stroke_color[texture_sheet-1][0] >= 0) // 6 is stride
				stroke = true;
			
			generateFontAtlas(texture_sheet, size, stroke, font_name_and_extension);
			Log.d("reywas","Generated the font atlas successfully.");
			return b;
			
		} else {
			return null;
		}
//		b = null;
	}
	
//	private boolean generateFontAtlas(int texture_sheet, int size, String font_name_and_extension){
//		if (ref.textureLoader.has_loaded[texture_sheet] == false) {
//			
//			Log.d("reywas", "About to generate font atlas.");
//			generate_font_atlas(texture_sheet, size, font_name_and_extension);
//			Log.d("reywas","Generated the font atlas successfully.");
//			
//			return true;
//		} else {
//			return false;
//		}
//	}
	

	
	private float tSingleCharWidth[] = new float[1];
	private float max_char_width = 0;
	
	protected int[] cell_height = new int[game_textures.texture_name_array.length];;
	protected int[] cell_width = new int[game_textures.texture_name_array.length];;
	//10x10 cells

	protected float[][] char_widths = new float[game_textures.texture_name_array.length][];
	protected int[] padding_x = new int[game_textures.texture_name_array.length];
	protected int[] padding_y = new int[game_textures.texture_name_array.length];
	protected float[] font_height  = new float[game_textures.texture_name_array.length];
	protected float[] font_ascent  = new float[game_textures.texture_name_array.length];
	protected float[] font_descent = new float[game_textures.texture_name_array.length];
	protected float[] font_leading = new float[game_textures.texture_name_array.length];
	   
	private void generateFontAtlas(int texture_sheet, int size, boolean stroke, String font_name_and_extension) {
		
		tSingleCharWidth = new float[1];
		max_char_width = 0;
		
		this.size = size;
		this.font_texture_sheet = texture_sheet;
		this.font_name_and_extension = font_name_and_extension;
		
		int texture_id = font_texture_sheet -1;
		
		p.reset();
		stkPaint.reset();
		if (font_name_and_extension == null) {
			p.setTypeface(Typeface.DEFAULT);
			stkPaint.setTypeface(Typeface.DEFAULT);
		} else {
			Typeface testFont = Typeface.createFromAsset(ref.android.getAssets(), "fonts/" + font_name_and_extension); 
			p.setTypeface(testFont);
			stkPaint.setTypeface(testFont);
		}

		
		p.setAntiAlias(true);
		p.setTextAlign(Paint.Align.LEFT);
		p.setTextSize(size);
		
		if (stroke) {
			p.setColor(Color.rgb(
					(int) (255*game_textures.font_stroke_color[texture_id][0]),
					(int) (255*game_textures.font_stroke_color[texture_id][1]),
					(int) (255*game_textures.font_stroke_color[texture_id][2]))
			);
			stkPaint.setColor(Color.rgb(
					(int) (255*game_textures.font_stroke_color[texture_id][3]),
					(int) (255*game_textures.font_stroke_color[texture_id][4]),
					(int) (255*game_textures.font_stroke_color[texture_id][5]))
			);
		} else {
			p.setColor(Color.WHITE);
			stkPaint.setColor(Color.TRANSPARENT);
		}
		
		stkPaint.setAntiAlias(true);
		stkPaint.setStyle(Style.STROKE);
		stkPaint.setTextAlign(Paint.Align.LEFT);
		stkPaint.setTextSize(size);


//		fm = p.getFontMetrics();
		fm = stkPaint.getFontMetrics();
//		font_top
		font_height[texture_id] = FloatMath.ceil( Math.abs( fm.bottom ) + Math.abs( fm.top ) );
		font_ascent[texture_id] = FloatMath.ceil( Math.abs( fm.ascent ) );
		font_descent[texture_id] = FloatMath.ceil( Math.abs( fm.descent ) );
		font_leading[texture_id] = FloatMath.ceil( Math.abs( fm.leading ) );
		
		float stroke_width = font_height[texture_id]/10;
		font_height[texture_id] += 2*stroke_width;
		stkPaint.setStrokeWidth(stroke_width);
		
		char_widths[texture_id] = new float[95];
		for(int i = 31; i < 126; i++){
			p.getTextWidths(Character.toString((char)(i)), tSingleCharWidth);
			
			
			
			char_widths[texture_id][i-31] = tSingleCharWidth[0]; 
			
			if (tSingleCharWidth[0]>max_char_width)
				max_char_width=tSingleCharWidth[0];
		}
//		char_widths[1] = max_char_width/3;
		// hack-y fix for space character..
		
		padding_y[texture_id] = (int) Math.round(font_height[texture_id]/8);
		padding_x[texture_id] = (int) Math.round(max_char_width/3 + stroke_width);
		
		// Log.e("reywas","" + padding_x + " " + padding_y);
				
		cell_height[texture_id] = Math.round(font_height[texture_id] + (2*padding_y[texture_id]));
		cell_width[texture_id] = (int) FloatMath.ceil(max_char_width + (2*padding_x[texture_id]));//+ (2*padding_x)); 

		if (b!=null){
			b.recycle();
			b = null;
//			System.gc();
		}
		
		int bitWidth = cell_width[texture_id]*10;
		int bitHeight = cell_height[texture_id]*10;
		int largerOfBitWH = (bitWidth>=bitHeight) ? bitWidth : bitHeight;
		int leastLargestPow2Size = getLeastPow2GtOrEqTo(largerOfBitWH);
		
		Log.e("gl_text", "bitWidth: " + bitWidth);
		Log.e("gl_text", "bitHeight: " + bitHeight);
		Log.e("gl_text", "largerOfBitHW: " + largerOfBitWH);
		Log.e("gl_text", "leastLargestPow2Size: " + leastLargestPow2Size);
		
//		TODO:b = Bitmap.createBitmap(leastLargestPow2Size, leastLargestPow2Size, Bitmap.Config.ARGB_8888);
		b = Bitmap.createBitmap((cell_width[texture_id])*10, (cell_height[texture_id])*10, Bitmap.Config.ARGB_8888);
		// Bitmap.Config.ARGB_4444);
		c = new Canvas(b);
		
		
		c.setDensity(Bitmap.DENSITY_NONE);
		
		b.eraseColor(android.graphics.Color.TRANSPARENT);
		
		
		
		
		int current_char = 32;
		float red_x;
		for (int cell_y=0; cell_y < 10; cell_y++){
			int cell_y_pos = cell_y * (cell_height[texture_id]);
			
			for (int cell_x=0; cell_x < 10; cell_x++){
				int cell_x_pos = cell_x * (cell_width[texture_id]);
				
				/*
				// debug colors
				p.setColor(Color.GREEN);
				c.drawLine(cell_x_pos+padding_x, cell_y_pos+padding_y, cell_x_pos+padding_x, cell_y_pos + cell_height - padding_y, p);
				c.drawLine(cell_x_pos+cell_width-padding_x, cell_y_pos+padding_y, cell_x_pos+cell_width-padding_x, cell_y_pos + cell_height - padding_y, p);
				c.drawLine(cell_x_pos+padding_x, cell_y_pos+padding_y, cell_x_pos+max_char_width+padding_x, cell_y_pos + padding_y, p);
				c.drawLine(cell_x_pos+padding_x, cell_y_pos+padding_y+font_height, cell_x_pos+max_char_width+padding_x, cell_y_pos+padding_y+font_height, p);
				
				p.setColor(Color.BLUE);
				c.drawLine(cell_x_pos, cell_y_pos, cell_x_pos, cell_y_pos + cell_height, p);
				c.drawLine(cell_x_pos, cell_y_pos, cell_x_pos+cell_width, cell_y_pos, p);
				
				red_x = (current_char < 126) ? cell_x_pos+(padding_x)+char_widths[current_char-31] : 1;
				p.setColor(Color.RED);
				c.drawLine(red_x, cell_y_pos + padding_y, red_x, cell_y_pos + cell_height - padding_y, p);
				*/
				
				c.drawText(Character.toString((char)current_char),(int) (cell_x_pos + padding_x[texture_id]),(int)(cell_y_pos+padding_y[texture_id]+font_height[texture_id]-font_descent[texture_id]), stkPaint);
				c.drawText(Character.toString((char)current_char),(int) (cell_x_pos + padding_x[texture_id]),(int)(cell_y_pos+padding_y[texture_id]+font_height[texture_id]-font_descent[texture_id]), p);
				current_char+=1;
			}
		}
		
		
		/*if (b.isRecycled()){
			Log.e("reywas","ERROR: call generate_font_atlas() before returnGeneratedFontAtlas()");			
		}*/
		
		generateTextureChoords((cell_width[texture_id]), (cell_height[texture_id]), 10, 10);
		
		
		
		//save_generated_font_atlas("TEST_SAVE");
	}

	private void generateTextureChoords(int cell_width, int cell_height, int cells_horizontal, int cells_vertical){
		int cells_x = cells_horizontal;
		int cells_y = cells_vertical;
		
		int cell_x = cell_width;//32
		int cell_y = cell_height;//33
		
		String texture_coords = "";
		texture_coords += ((int)(cell_x * cells_x) + ", " + (int)(cell_y * cells_y) + ",   ");
		for(int y=0;  y<cells_y; y++){
			for(int x=0;  x<cells_x; x++){
				texture_coords += ((int)(cell_x*x) + "," + (int)((cell_y*y)+cell_y) + ", " + (int)((cell_x*x) + cell_x) + "," + (int)((cell_y*y)+cell_y) + ", " + (int)(cell_x*x) + "," + (int)(cell_y*y) + ", " + (int)((cell_x*x) + cell_x) + "," + (int)(cell_y*y)+ ",   ");					
			}
		}
		
		
		String texture_coords_clean = texture_coords.replaceAll(" ", "");
		// System.out.println(texture_coords_clean);
		
		// System.out.println(texture_coords_clean);
		
		String[] texture_coords_split = texture_coords_clean.split(",");
		int split_length = texture_coords_split.length;
		
		int[] temp_texture_locations_arrays = new int[((split_length) + 1)];
//		texture.texture_locations_arrays[3] = new float[(split_length/2) + 1];
		for(int i=0; i<split_length; i++){
				temp_texture_locations_arrays[i] = Integer.parseInt(String.valueOf(texture_coords_split[i]));
		}
		
		/*for(int i=0; i<temp_texture_locations_arrays.length; i++){
			System.out.println(temp_texture_locations_arrays[i]);
		}*/
		
		int length = temp_texture_locations_arrays.length;
		ref.texture.texture_locations_arrays[font_texture_sheet-1] = new float[length];
//		ref.texture.texture_locations_arrays[texture_sheet+2] = new float[length];
		
		for(int i = 0; i < length; i++){
//			ref.texture.texture_locations_arrays[texture_sheet+2][i] = temp_texture_locations_arrays[i];
			ref.texture.texture_locations_arrays[font_texture_sheet-1][i] = temp_texture_locations_arrays[i];
		}
		temp_texture_locations_arrays = null;
		System.gc();
//		texture.texture_locations_arrays[3] = temp_texture_locations_arrays;
	}
	
	private int getLeastPow2GtOrEqTo(int thisNumber) {
		int i = 0;
		while(true) {
			int pow2 = 1;
			for(int p=0; p<i; p++)
				pow2 *= 2;
			if (pow2 >= thisNumber)
				return pow2;
			i++;
		}
	}
}
