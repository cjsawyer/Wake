package com.mammothGames.wake.gameEngine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.util.SparseArray;

public class engine_gl_textureLoader {
	
	engine_reference ref;
	
	String resource_name;
	
	volatile int number_of_textures;
	volatile int[] textures;
	volatile int[] texture_ids;
	
	final Object loaded_lock = new Object();
	volatile boolean[] has_loaded;
	volatile int[] widths;
	volatile int[] heights;
	
	volatile byte[] buffer;
	
	volatile SparseArray<byte[]> byte_map;
	
	int temp_i;
	
	public engine_gl_textureLoader(engine_reference r){
		ref = r;
		number_of_textures = ref.g_textures.get_numTextures();
		
		byte_map = new SparseArray<byte[]>();
		
		textures = new int[number_of_textures+1];
		texture_ids = new int[number_of_textures+1];
		
		heights = new int[number_of_textures+1];
		widths = new int[number_of_textures+1];
		
		has_loaded = new boolean[number_of_textures+1];
		
		for(temp_i=0;temp_i<(number_of_textures+1); temp_i++){
			has_loaded[temp_i] = false;
		}
		
	}
	
	
	public void initTextures(){
		if (textures != null){
			GLES20.glDeleteTextures(number_of_textures, textures, 0);		
			textures = null;
		}
		textures = new int[number_of_textures+1];
		GLES20.glGenTextures(number_of_textures, textures, 0);		

		texture_ids = textures.clone();

		
	}

	
	private volatile boolean isLoading = false;
	
	volatile ByteBuffer byteBuffer;
	
	private final Object condition = new Object();
	
	/**
	 * Does not report to texture loadHelper.
	 * Used when not using loadHelper, like the error texture
	 */
	protected void sys_loadTexture(int texture_id) {
		loadTexture(texture_id, false);
	}
	public void loadTexture(int texture_id) {
		loadTexture(texture_id, true);
	}
	private void loadTexture(final int texture_id, final boolean report_loaded){
		
		
		boolean isFontLocal;
		if ( ref.g_textures.get_fontNameAndExtension(texture_id-1) == null ) {
			isFontLocal = false;
		} else {
			isFontLocal = true;
		}
		final boolean isFont = isFontLocal;
		
		Thread loadingThread = new Thread(new Runnable() {

            public void run() {
            	
            	Bitmap temp_bitmap = null;
            	
            	if (isLoading) {
            		synchronized(condition) {
                		try {
    						condition.wait();
    					} catch (InterruptedException e) {}
                	}
            	}
            		
	            	
	            	isLoading = true;
	            	final int id = texture_id-1;
	    			
	    			resource_name = ref.g_textures.get_texNameAndExtension(id); 
	    					
	    			// Bitmap bitmap = null;
	    	
	    			if (has_loaded[id] == false){
	    				
	    				if (isFont) {
	    					temp_bitmap = ref.text.returnGeneratedFontAtlas(id+1, ref.g_textures.getFontSize(id), ref.g_textures.get_fontNameAndExtension(id));
	    					if (temp_bitmap == null) {
	    						Log.e("reywas", "Font " + resource_name + " failed to generate!");
	    					}
	    				}
	    				
	    				try {
	    					if (temp_bitmap == null){
	    						Log.d("reywas", "About to load bitmap from " + "textures/" + resource_name + ".png");
	    						
	    						temp_bitmap = BitmapFactory.decodeStream(ref.android.getAssets().open("textures/" + resource_name + ".png"));
	    						
	    						Log.d("reywas", "Loaded " + "textures/" + resource_name + ".png successfully");
	    					}else{
	    						Log.d("reywas", "Loaded " + resource_name + " from memory successfully");
	    					}
	    				} catch (IOException e) {
	    					Log.e("reywas", "Could not load bitmap from assets folder.");
	    					e.printStackTrace();
	    				}
	    				
	    				heights[id] = temp_bitmap.getHeight();
	    				widths[id] =  temp_bitmap.getWidth();
	    			}
	    	
	    			final int temp_height = heights[id];						
	    			final int temp_width = widths[id];
	    			
	    			buffer = new byte[temp_width * temp_height * 4];
	    			
	    			if (has_loaded[id] == false){
	    	
	    				for (int temp_loop_y = 0; temp_loop_y < temp_height; temp_loop_y++ ){
	    					for (int  temp_loop_x = 0; temp_loop_x < temp_width; temp_loop_x++ )
	    					{
	    							int pixel = temp_bitmap.getPixel(temp_loop_x, temp_loop_y);
	    							if (buffer == null)
	    								Log.e("reywas", "null buffer!");
	    							buffer[(temp_loop_y * temp_width + temp_loop_x) * 4 + 0] = (byte)((pixel >> 16) & 0xFF);
	    							buffer[(temp_loop_y * temp_width + temp_loop_x) * 4 + 1] = (byte)((pixel >> 8) & 0xFF);
	    							buffer[(temp_loop_y * temp_width + temp_loop_x) * 4 + 2] = (byte)((pixel >> 0) & 0xFF);
	    							buffer[(temp_loop_y * temp_width + temp_loop_x) * 4 + 3] = (byte)((pixel >> 24) & 0xFF);
	    					}
	    				}
	    				
	    				byte_map.put(id, buffer);
	    				//has_loaded[texture_id] = true;
	    			} else {
	    				buffer = byte_map.get(id);
	    			}
	    		
	    			if (temp_bitmap != null){
	    				temp_bitmap.recycle();
	    				temp_bitmap = null;
	    			}
	    			//System.gc();
	    			
	    			Log.e("texture", "width: " + temp_width);
	    			Log.e("texture", "height: " + temp_height);
	    			
	    			byteBuffer = ByteBuffer.allocateDirect(temp_width * temp_height * 4);
	    			byteBuffer.put(buffer).position(0);
	    			
	    			buffer = null;
	    			//System.gc();
	    	
	    			//texture memory leak fix
	    			//i'm not sure if this actually did anything, so if it's broken, try uncommenting this block.
	    			/*int[] tempIntArray = new int[2];
	    			tempIntArray[1] = texture_ids[texture_id];
	    			
	    			GLES20.glDeleteTextures(1, tempIntArray, 0);
	    			GLES20.glGenTextures(1, tempIntArray, 0);

	    			texture_ids[texture_id] = tempIntArray[1];
	    			tempIntArray = null;
	    			*/ //end of fix
	    			
	    			// GLSurfaceView.queueEvent
	                ref.android.open_gl_surface_view.queueEvent(new Runnable() {
	                    @Override
	                    public void run() {
//	                    	ref.android.open_gl_surface_view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	                    	// GLES20.glActiveTexture(texture_ids[id]);
	                    	GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, texture_ids[id] );
	    	    			ref.current_texture_sheet = 0;
	    	    			ref.current_texture_id = 0;
	    	    	
	    	    	
	    	    			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );//GL_LINEAR ie bilinear filtering
	    	    			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );//GL_NEAREST ie nearest-neighbor
	    	    			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
	    	    			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
	    	    			
	    	    			GLES20.glTexImage2D ( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, temp_width, temp_height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer );
	    	    			
//	    	    			ref.android.open_gl_surface_view.requestRender();//CHanged this post release 1
//	    	    			ref.android.open_gl_surface_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	    	    			
	    	    			byteBuffer.clear();
	    	    			byteBuffer = null;
	    	    			//System.gc();
	    	    			
	    	    			has_loaded[id] = true;
	    	    			if (report_loaded) {
	    	    				ref.loadHelper.informThatOneLoaded();
	    	    			}
	    	    			
	    	    			isLoading = false;
	    	    			synchronized(condition){
	    	    				condition.notify();
	    	    			}
	                    }
	                });
	    				    	
	    			
	    								
	    			
            	
            }

        });
		loadingThread.start();
			
			
		}
	public void reloadLoadedTextues() {
		for(temp_i=2; temp_i<number_of_textures+1; temp_i++) {
			
			if (has_loaded[temp_i-1]==true) {
				loadTexture(temp_i);
			}		
		}
	}
}
