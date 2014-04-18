package com.mammothGames.wake1.gameEngine;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.FloatMath;
import android.util.Log;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class engine_gl_circle {
	
	final engine_reference ref;
	public engine_gl_circle(engine_reference r){
		ref = r;
	}
	
	protected static float[] returnCircleVertexes() {
		
		int points = 30;
//		final float DEG_TO_RAD = (float) (180/Math.PI);
		float theta = (float) (2 * Math.PI/((float)points-2));
		int x=0,y=0;
		
		float[] verts = new float[points*3];
		
		verts[0] = 0;
		verts[1] = 0;
		verts[2] = 0;
		
		for(int i=1; i<points;i++) {
//			Log.e("sad", "percent " + (theta*i));
			verts[i*3] = (float) FloatMath.cos(theta*(i-1));
			verts[i*3+1] = (float) FloatMath.sin(theta*(i-1));
			verts[i*3+2] = 0;
//			Log.e("sad", "A:	" + verts[i*3] + "	" + verts[i*3+1] + "	" + verts[i*3+2]);
		}
		
        return verts;
	}
	
	 
	public void draw(float x, float y, float shape_angle, float size_x, float size_y, float origin_x, float origin_y, float rotate_angle, float depth){
		Matrix.setIdentityM(ref.renderer.mModelMatrix, 0);
		if (rotate_angle != 0){
			// move to x/y treating origin_x/y as the origin with rotation, if there is rotation
			Matrix.translateM(ref.renderer.mModelMatrix, 0,origin_x + x,origin_y + y, 0.0f);
			ref.matrix.rotateM(ref.renderer.mModelMatrix, 0, rotate_angle, 0.0f, 0.0f, 1.0f);
			Matrix.translateM(ref.renderer.mModelMatrix, 0, -origin_x, -origin_y, 0.0f);
			
			ref.matrix.rotateM(ref.renderer.mModelMatrix, 0, -rotate_angle, 0.0f, 0.0f, 1.0f);
			Matrix.translateM(ref.renderer.mModelMatrix, 0, -origin_x, -origin_y, 0.0f);
			ref.matrix.rotateM(ref.renderer.mModelMatrix, 0, rotate_angle, 0.0f, 0.0f, 1.0f);
		} else {
			//if no rotation, just move
			Matrix.translateM(ref.renderer.mModelMatrix, 0 ,origin_x + x, origin_y + y, 0.0f);
		}
		
		// then scale
		Matrix.scaleM(ref.renderer.mModelMatrix, 0, size_x, size_y, 1.0f);
		
		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Position, 3, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.circle_vertices_FloatBuffer);
//		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Position, ref.floatbuffers.circle_vertices_data.length, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.circle_vertices_FloatBuffer);
//		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Position, 3, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.circle_vertices_FloatBuffer);
		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Color, 4, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.colors_FloatBuffer);
		
//		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Color, 4, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.colors_FloatBuffer);
		
//    	GLES20.glVertexAttrib3fv(ref.renderer.VS_a_Position, ref.floatbuffers.color_data, 0);
//		GLES20.glVertexAttrib4fv(ref.renderer.VS_a_Color, ref.floatbuffers.vertices_data, 0);
		
		if (ref.current_texture_id != -1){
			ref.current_texture_id = -1;
			//ref.floatbuffers.texture_coords_FloatBuffer.put(ref.floatbuffers.blank_texture_coord_data).position(0);
//			( (FloatBuffer) ref.floatbuffers.texture_coords_FloatBuffer.clear() ).put(ref.floatbuffers.blank_texture_coord_data).flip();
			
//			GLES20.glVertexAttribPointer(ref.renderer.VS_a_Texture, 2, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.noTexture_coords_FloatBuffer);//blank_texture_coords_FloatBuffer
//:)			ref.floatbuffers.texture_coords_FloatBuffer.put(ref.floatbuffers.blank_texture_coord_data).position(0);

//			GLES20.glVertexAttrib2fv(ref.renderer.VS_a_Texture, ref.floatbuffers.blank_texture_coord_data, 0);
			
			GLES20.glVertexAttribPointer(ref.renderer.VS_a_Texture, 2, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.noTexture_coords_FloatBuffer);//blank_texture_coords_FloatBuffer
		}
//		GLES20.glVertexAttribPointer(ref.renderer.VS_a_Texture, 2, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.texture_coords_FloatBuffer);//blank_texture_coords_FloatBuffer
		
    	
		GLES20.glEnableVertexAttribArray(ref.renderer.VS_a_Position);
		GLES20.glEnableVertexAttribArray(ref.renderer.VS_a_Color);
//		GLES20.glEnableVertexAttribArray(ref.renderer.VS_a_Texture);
	
		
		Matrix.multiplyMM(ref.renderer.mMVPMatrix, 0, ref.renderer.mViewMatrix, 0, ref.renderer.mModelMatrix, 0);
		Matrix.multiplyMM(ref.renderer.mMVPMatrix, 0, ref.renderer.mProjectionMatrix, 0, ref.renderer.mMVPMatrix, 0);
		
		GLES20.glUniformMatrix4fv(ref.renderer.VS_u_MVP_Matrix, 1, false, ref.renderer.mMVPMatrix, 0);
//		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, ref.floatbuffers.circle_vertices_data.length/3);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, (int)((ref.floatbuffers.circle_vertices_data.length-1)/3 * Math.abs(shape_angle/360f) + 1));// verts in circle * percent of circle + 1 for the first vert in the middle
		
	}
}