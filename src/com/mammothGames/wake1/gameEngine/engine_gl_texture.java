package com.mammothGames.wake1.gameEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.mammothGames.wake1.game.game_constants;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class engine_gl_texture {


//	float[][] texture_locations_arrays = ref.g_textures.texture_locations_arrays;
//	String[] texture_name_array = ref.g_textures.texture_name_array;
	
	final engine_reference ref;
	
	public engine_gl_texture(engine_reference r){
		ref = r;
	}

	
	final int bytes_per_float = 4;
	
	final float[] default_square_texture_coord_data = {
			// U, V
			0f, 1f,
			1f, 1f,
			0f, 0f,
			1f, 0f,
	};
	float[] calc_square_texture_coord_data = default_square_texture_coord_data;
	
	
	int binded_texture;
	int stride; //for setting the texture cords
	float sheet_width;
	float sheet_height;
	
	protected int number_times_binded_this_frame = 0;
	void setTextureCoordsAndSheet(int texture_id, int texture_sheet_to_bind){
		
		if ((ref.current_texture_id != texture_id) | (ref.current_texture_sheet != texture_sheet_to_bind)){
		
			// Bind new sheet if we need to.
			if (ref.current_texture_sheet != texture_sheet_to_bind){
				
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_sheet_to_bind);
				number_times_binded_this_frame++;
				
				sheet_width = ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[0]; 
				sheet_height = ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[1];
				
				
				ref.current_texture_sheet = texture_sheet_to_bind;
			}
			
			
			if(game_constants.devmode) {
				// Check if the sheet has the requested sprite on it
				if ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1).length - 2)/8 < texture_id){
					Log.e("reywas","ERROR: tried to bind sprite " + texture_id + " on sheet #" + texture_sheet_to_bind + ", but texture sheet #" + texture_sheet_to_bind + " only has " + ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1).length - 2)/8) + " sprite/s.");
				}
			}
			
			stride = (8 * (texture_id-1)) + 2;
			
			                                                                                                       
			calc_square_texture_coord_data[0] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[stride]      )/sheet_width);//+
			calc_square_texture_coord_data[1] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 1)])/sheet_height);//-
			calc_square_texture_coord_data[2] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 2)])/sheet_width);//-
			calc_square_texture_coord_data[3] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 3)])/sheet_height);//-
			calc_square_texture_coord_data[4] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 4)])/sheet_width);//+
			calc_square_texture_coord_data[5] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 5)])/sheet_height);//+
			calc_square_texture_coord_data[6] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 6)])/sheet_width);//-
			calc_square_texture_coord_data[7] = ((ref.loaded_textures.get_texCoords(texture_sheet_to_bind - 1)[(stride + 7)])/sheet_height);//+
			
			ref.floatbuffers.texture_coords_FloatBuffer.put(calc_square_texture_coord_data);
			GLES20.glVertexAttribPointer(ref.renderer.VS_a_Texture, 2, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.texture_coords_FloatBuffer);
			
			ref.current_texture_id = texture_id;
		}
	}
	
	

	
	
	
	
	int glError = 0;
	
	
	
	// the origin should be set in relation to the unscaled sprite
	public void draw(float x, float y, float size_x, float size_y, float origin_x, float origin_y, float rotate_angle, float depth, int textureID, int texture_sheet){
		
		Matrix.setIdentityM(ref.renderer.mModelMatrix, 0);
		
		if(game_constants.devmode) {
			if ((depth > 500) | (depth < 0)){
				Log.e("reywas", "When passing depth to a draw call, use an int in the range[0, 500]. 0 is the back, 500 the front." );
			}
		}
		
		
		
		
		if (rotate_angle != 0){
			// move to x/y treating origin_x/y as the origin with rotation, if there is rotation
			
			
			Matrix.translateM(ref.renderer.mModelMatrix, 0,origin_x + x + 0.5f,origin_y + y + 0.5f, ((depth)/1000.0f));//0.0f);
			ref.matrix.rotateM(ref.renderer.mModelMatrix, 0, rotate_angle, 0.0f, 0.0f, 1.0f);
			Matrix.translateM(ref.renderer.mModelMatrix, 0, -origin_x, -origin_y, 0.0f);
			
			ref.matrix.rotateM(ref.renderer.mModelMatrix, 0, -rotate_angle, 0.0f, 0.0f, 1.0f);
			Matrix.translateM(ref.renderer.mModelMatrix, 0, -origin_x, -origin_y, 0.0f);
			ref.matrix.rotateM(ref.renderer.mModelMatrix, 0, rotate_angle, 0.0f, 0.0f, 1.0f);
		} else {
			//if no rotation, just move
			Matrix.translateM(ref.renderer.mModelMatrix, 0 ,x - origin_x, y - origin_y, 0.0f);
		}
    	
		// then scale
    	Matrix.scaleM(ref.renderer.mModelMatrix, 0, size_x, size_y, 1.0f);
		
//    	GLES20.glVertexAttrib3fv(ref.renderer.VS_a_Color, ref.floatbuffers.vertices_data, 0);
//    	GLES20.glVertexAttrib3fv(ref.renderer.VS_a_Position, ref.floatbuffers.color_data, 0);
    	
		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Position, 3, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.rectangle_vertices_FloatBuffer);
		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Color, 4, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.colors_FloatBuffer);
		

		// modify texture cords to fit intended texture part
		setTextureCoordsAndSheet(textureID, texture_sheet);
		
		//GLES20.glVertexAttrib2fv(ref.renderer.VS_a_Texture, ref.floatbuffers.texture_coord_data, 0);
//		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Texture, 2, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.texture_coords_FloatBuffer);
		
		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Texture, 2, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.texture_coords_FloatBuffer.position(0));
		
		GLES20.glEnableVertexAttribArray(ref.renderer.VS_a_Position);
		GLES20.glEnableVertexAttribArray(ref.renderer.VS_a_Color);
		GLES20.glEnableVertexAttribArray(ref.renderer.VS_a_Texture);
	
		
		Matrix.multiplyMM(ref.renderer.mMVPMatrix, 0, ref.renderer.mViewMatrix, 0, ref.renderer.mModelMatrix, 0);
		Matrix.multiplyMM(ref.renderer.mMVPMatrix, 0, ref.renderer.mProjectionMatrix, 0, ref.renderer.mMVPMatrix, 0);
		
		
		GLES20.glUniformMatrix4fv(ref.renderer.VS_u_MVP_Matrix, 1, false, ref.renderer.mMVPMatrix, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
//		glError = GLES20.glGetError();
//		if(glError!=GLES20.GL_NO_ERROR){
//			Log.e("GL Error", ""+glError);
//		}
		
	}
}