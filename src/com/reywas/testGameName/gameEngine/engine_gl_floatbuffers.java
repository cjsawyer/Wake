package com.reywas.testGameName.gameEngine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class engine_gl_floatbuffers {
	
	final FloatBuffer rectangle_vertices_FloatBuffer;
	final FloatBuffer circle_vertices_FloatBuffer;
	final FloatBuffer colors_FloatBuffer;
	//final FloatBuffer blank_texture_coords_FloatBuffer;
	final FloatBuffer texture_coords_FloatBuffer;
	final FloatBuffer noTexture_coords_FloatBuffer;
	final int BYTES_PER_FLOAT = 4;
	
	final float[] rectangle_vertices_data = {
			// X, Y, Z,
			
			//0
			-0.5f, -0.5f, 0.0f,
			//1
			0.5f, -0.5f, 0.0f,
			//2
			-0.5f, 0.5f, 0.0f,
			//3
			0.5f, 0.5f, 0.0f
			
	};
	
	/*
	final float[] circle_vertices_data = {
			// X, Y, Z,
			
			//0
			-0.5f, -0.5f, 0.0f,
			//1
			0.5f, -0.5f, 0.0f,
			//2
			0f, 0f, 0.0f,
			//3
			1f, 1f, 0.0f
	};
	*/
	final float[] circle_vertices_data;
//	final float[] circle_vertices_data = {0.0f, 0.0f, 0.0f, 0.9781476f, 0.2079117f, 0.0f, 0.9135454f, 0.40673664f, 0.0f, 0.809017f, 0.58778524f, 0.0f, 0.66913056f, 0.74314487f, 0.0f, 0.49999997f, 0.86602545f, 0.0f, 0.30901697f, 0.95105654f, 0.0f, 0.10452842f, 0.9945219f, 0.0f, -0.10452851f, 0.9945219f, 0.0f, -0.30901703f, 0.9510565f, 0.0f, -0.50000006f, 0.8660254f, 0.0f, -0.6691307f, 0.7431448f, 0.0f, -0.80901706f, 0.5877852f, 0.0f, -0.9135455f, 0.40673658f, 0.0f, -0.9781476f, 0.20791161f, 0.0f, -1.0f, -8.742278E-8f, 0.0f, -0.97814757f, -0.20791179f, 0.0f, -0.9135454f, -0.40673673f, 0.0f, -0.80901694f, -0.58778536f, 0.0f, -0.6691305f, -0.74314487f, 0.0f, -0.4999999f, -0.86602545f, 0.0f, -0.3090171f, -0.9510565f, 0.0f, -0.10452834f, -0.9945219f, 0.0f, 0.10452884f, -0.99452186f, 0.0f, 0.30901712f, -0.9510565f, 0.0f, 0.4999999f, -0.86602545f, 0.0f, 0.66913074f, -0.74314475f, 0.0f, 0.80901724f, -0.58778495f, 0.0f, 0.91354555f, -0.4067365f, 0.0f, 0.97814757f, -0.20791176f, 0.0f};

	
	final float[] blank_texture_coord_data = {
			// U, V
			-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,
			-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,
			-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,
			-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,
			-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,
			-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,-1f, -1f,
			
			
			
	};
	
//	final float[] texture_coord_data = {
//			// U, V
//			0f, 1f,
//			1f, 1f,
//			0f, 0f,
//			1f, 0f,
//			
//	};
	
//	float[] color_data = new float[120];
	float[] color_data = {
			// R, G, B, A
			
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
			0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,  
	};
	float[] texture_coord_data = {
			// U, V
			0f, 1f,
			1f, 1f,
			0f, 0f,
			1f, 0f,
			
	};

	final engine_reference ref;
	
	public engine_gl_floatbuffers(engine_reference r){
		
		ref = r;
		
		circle_vertices_data = engine_gl_circle.returnCircleVertexes();

		rectangle_vertices_FloatBuffer = ByteBuffer.allocateDirect(rectangle_vertices_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
		circle_vertices_FloatBuffer = ByteBuffer.allocateDirect(circle_vertices_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
//		circle_vertices_FloatBuffer = ByteBuffer.allocateDirect(29* BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		colors_FloatBuffer = ByteBuffer.allocateDirect(color_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		texture_coords_FloatBuffer = ByteBuffer.allocateDirect(texture_coord_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
		noTexture_coords_FloatBuffer = ByteBuffer.allocateDirect(blank_texture_coord_data.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		
		
		rectangle_vertices_FloatBuffer.put(rectangle_vertices_data).position(0);
		circle_vertices_FloatBuffer.put(circle_vertices_data).position(0);
		
//		colors_FloatBuffer.put(color_data).position(0);
		system_changeDrawColor(0,0,0,1);
		
		texture_coords_FloatBuffer.put(texture_coord_data).position(0);
		noTexture_coords_FloatBuffer.put(blank_texture_coord_data).position(0);
		
	}
	
	float system_current_r = 1f;
	float system_current_g = 1f;
	float system_current_b = 1f;
	float system_current_a = 1f;
	
	public void changeDrawColor(float r, float g, float b, float a) {
			system_current_r = r;
			system_current_g = g;
			system_current_b = b;
			system_current_a = a;
	}
	
	private boolean color_has_changed = false;
	protected boolean system_changeDrawColor(float r, float g, float b, float a){
		
			
		/*
			if (system_current_r != r){
				color_data[0]=r;
				color_data[4]=r;
				color_data[8]=r;
				color_data[12]=r;
				system_current_r = r;
				color_has_changed = true;
			}
			if (system_current_g != g){
				color_data[1]=g;
				color_data[5]=g;
				color_data[9]=g;
				color_data[13]=g;
				system_current_g = g;
				color_has_changed = true;
			}
			if (system_current_b != b){
				color_data[2]=b;
				color_data[6]=b;
				color_data[10]=b;
				color_data[14]=b;
				system_current_b = b;
				color_has_changed = true;
			}
			if (system_current_a != a){
				color_data[3]=a;
				color_data[7]=a;
				color_data[11]=a;
				color_data[15]=a;
				system_current_a = a;
				color_has_changed = true;
			}
			
			
			
			if (color_has_changed) {
				colors_FloatBuffer.put(color_data).position(0);
				GLES20.glVertexAttribPointer(ref.renderer.VS_a_Color, 4, GLES20.GL_FLOAT, false, 0, ref.floatbuffers.colors_FloatBuffer);
				color_has_changed = false;
			}
			//*/
		

//		/*
//		if ((system_current_r != r) || (system_current_g != g) || (system_current_b != b) || (system_current_a != a)) {
			
		for (temp_i=0;temp_i<color_data.length/4;temp_i++) {
			color_data[temp_i*4]=r;
			color_data[temp_i*4+1]=g;
			color_data[temp_i*4+2]=b;
			color_data[temp_i*4+3]=a;
		}
			
			
			system_current_r = r;
			system_current_g = g;
			system_current_b = b;
			system_current_a = a;
			
			
			//colors_FloatBuffer.clear();
			
			colors_FloatBuffer.put(color_data).position(0);
//		}

//		 */
		return true;
		
	}
	private int temp_i;

}
