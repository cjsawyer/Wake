package com.mammothGames.wake1.gameEngine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.mammothGames.wake1free.R;
import com.mammothGames.wake1.game.game_constants;
import com.mammothGames.wake1.game.game_textures;
//import com.reywas.testGameName.*;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class engine_gl_renderer implements GLSurfaceView.Renderer {

	Context android_context;

	final int BYTES_PER_FLOATt = 4;

	final engine_reference ref;

	public engine_gl_renderer(engine_reference r){
		ref = r;
		
		ref.draw = new engine_gl_draw(ref);
//		bg_color_needs_reset = false;
	}

	boolean textures_initiated = false;

	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY-1);
		
		// Position the camera
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.5f;
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -1.0f;
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;

		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		GLES20.glClearColor(0, 0, 0, 1f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		ref.android.open_gl_surface_view.requestRender();

		GLES20.glEnable(GLES20.GL_BLEND);

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		String vertexShader = loadShader(R.raw.vertex_shader);
		String fragmentShader = loadShader(R.raw.fragment_shader);

		int vertex_shader_reference = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertex_shader_reference, vertexShader);
		GLES20.glCompileShader(vertex_shader_reference);

		int fragment_shader_reference = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragment_shader_reference, fragmentShader);
		GLES20.glCompileShader(fragment_shader_reference);


		program_reference = GLES20.glCreateProgram();
		GLES20.glAttachShader(program_reference, vertex_shader_reference);
		GLES20.glAttachShader(program_reference, fragment_shader_reference);

		GLES20.glLinkProgram(program_reference);

		final int[] link_status = new int[1];
		GLES20.glGetProgramiv(program_reference, GLES20.GL_LINK_STATUS, link_status, 0);

		// Check for errors.
		if (link_status[0] == 0)
		{
			GLES20.glDeleteProgram(program_reference);
			program_reference = 0;
			throw new RuntimeException("Error creating program.");
		}


		VS_u_MVP_Matrix = GLES20.glGetUniformLocation(program_reference, "u_MVPMatrix");
		VS_a_Position = GLES20.glGetAttribLocation(program_reference, "a_Position");
		VS_a_Color = GLES20.glGetAttribLocation(program_reference, "a_Color");
		VS_a_Texture = GLES20.glGetAttribLocation(program_reference, "a_TextureCoord");
		
		
//		GLES20.glEnable(GLES20.GL_CULL_FACE);// TODO: turn these off??
//		GLES20.glCullFace(GLES20.GL_FRONT);
		
		
		ref.textureLoader.initTextures();

	}
	
	public void sys_changeClearColor(float r, float g, float b){
		ref.system_clearColor_r = r;
		ref.system_clearColor_g = g;
		ref.system_clearColor_b = b;
		GLES20.glClearColor(r,g,b, 1.0f);
	}

	private String loadShader(int resID){
		
		String output = "";
		String temp_read = "";
		
		StringBuffer buffer = new StringBuffer();
		
		// read the files
		try {
			// Read the file from the resource
			InputStream inputStream = ref.android.getResources().openRawResource(resID);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			temp_read = in.readLine();
			while (temp_read != null) {
				buffer.append(temp_read + "\n");
				temp_read = in.readLine();
			}

			output = buffer.toString();
			buffer.deleteCharAt(output.length() - 1);
		} catch (Exception e) {
			Log.d("ERROR-readingShader", "Could not read shader: " + e.getLocalizedMessage());
		}
		return output;
	}
	
	boolean assets_loaded = false;


	public int program_reference;
	public int VS_u_MVP_Matrix;
	public int VS_a_Position;
	public int VS_a_Color;
	public int VS_a_Texture;
	public int VS_a_IsTextured;

	double start_time = System.currentTimeMillis();
	boolean sys_first_step_executed = false;

	

	float angle_in_degrees;

	
	
	
	
	float sys_delta_time = 0;
	private long old_time = 0, current_time = 0;
	private long delta_time_cap;
	final private float MAX_FPS = 1000f/60f; 

	private boolean startLogic = false;
	public void onDrawFrame(GL10 glUnused) {
		
		
		
		
		// Calculates delta_time
    	current_time = System.currentTimeMillis();
    	sys_delta_time = current_time - old_time;
    	old_time = current_time;
    	
//    	// Caps FPS at 60
//    	delta_time_cap = (int)(sys_delta_time - MAX_FPS);
//    	if(delta_time_cap < 0){
//    		try {Thread.sleep(-delta_time_cap);} catch (InterruptedException e) {e.printStackTrace();}
//    		// Log.e("reywas", "limited FPS " + delta_time_cap + " " + (1000f/sys_delta_time));
//    	}
    	
    	//Fixes edge cases in delta time 
    	if (sys_delta_time < 16) { sys_delta_time = 16; }
    	if (sys_delta_time > 66) { sys_delta_time = 66; }
    	
    	ref.main.sys_update_delta_time();

		GLES20.glUseProgram(program_reference);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		if (assets_loaded == false){
			
			//Load the error texture before anything else.
			ref.textureLoader.sys_loadTexture(game_textures.TEX_ERROR);
			// Reload previously loaded textures
			ref.textureLoader.reloadLoadedTextues();
			
			// Do the same for sounds and music
			ref.sound.initSounds(ref.android);
			ref.sound.reloadSounds();
			ref.sound.reloadMusic();
			
			assets_loaded = true;
			
		} else {
			startLogic = true;
		}
		if (startLogic) {
			sys_changeClearColor(ref.system_clearColor_r,ref.system_clearColor_g,ref.system_clearColor_b);
			ref.main.sys_gameLoop();
			ref.draw.executeDraw();
	
			
//			ref.floatbuffers.system_changeDrawColor(0, 1, 0, 1);
//			ref.circle.draw(ref.main.get_screen_width()/2, ref.main.get_screen_height()/2, 100, 100, 0, 0, 0, 1);
//			ref.draw.drawCircle(100, 100, 50, 0, 0, 500);
			
		}
		
		ref.android.open_gl_surface_view.requestRender();
		
		
		// For entitiy alarms
		sys_first_step_executed = true;
	}


	float[] mViewMatrix = new float[16];
	float[] mProjectionMatrix = new float[16];
	float[] mModelMatrix = new float[16];
	float[] mMVPMatrix = new float[16];
	
	float[] mProjectionTimesViewMatrix = new float[16];

	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		ref.screen_width = width;
		ref.screen_height = height;
		
		// Make the world size/coordinates the same as the pixel dimensions of the screen.
		final float ratio = (float) width / height;
		final float left = 0.0f;
		final float right = height*ratio;
		final float bottom = 0.0f;
		final float top = (float) height;
		final float near = 1.0f;
		final float far = 1000.0f;

		Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
		
		
	}
}
