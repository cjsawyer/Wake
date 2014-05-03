package com.mammothGames.wake1.gameEngine;

import java.util.Arrays;
import java.util.HashMap;

import com.mammothGames.wake1free.R;
import com.mammothGames.wake1.game.constants;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.AudioManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GAServiceManager;

public class engine_android extends Activity {

	GLSurfaceView open_gl_surface_view;
	engine_reference ref;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("reywas","onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
//		if(game_constants.is_landscape) {
//	    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		} else {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
		
		
		
		setContentView(R.layout.main);
		
		open_gl_surface_view = (GLSurfaceView) findViewById(R.id.gameView);
		
//		open_gl_surface_view = new GLSurfaceView(this);
		
		// Request an OpenGL ES 2.0 compatible context.
		open_gl_surface_view.setEGLContextClientVersion(2);
		
		ref = (engine_reference) getLastNonConfigurationInstance();
		
		
	    if (ref == null) {
	    		ref = new engine_reference();
	    		ref.android = this;
	    		
	    		ref.main = new engine_main(ref);
	    }
	    ref.android = this;
	    
	    
	    audio = (AudioManager) getSystemService(this.AUDIO_SERVICE);
	    
	    
//	    open_gl_surface_view.setZOrderOnTop(true);
	    
	    
	    ref.renderer = new engine_gl_renderer(ref);
	    //TODO TODO TODO TODO
	    open_gl_surface_view.setEGLConfigChooser(5, 6, 5, 0, 0, 0);
	    //TODO TODO TODO TODO
		open_gl_surface_view.setRenderer(ref.renderer);
		open_gl_surface_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		ref.floatbuffers = new engine_gl_floatbuffers(ref);
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			
		
		//Touch stuff
		initate_touch_points();
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT) // Minimum API level for SYSTEM_UI_FLAG_IMMERSIVE
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
		getWindow().getDecorView().setSystemUiVisibility(
		          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		        | View.SYSTEM_UI_FLAG_FULLSCREEN
		        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		
		// Log.e("reywas","onRetainNonConfigurationInstance");
		ref.renderer = null;
		return ref;
	}

	
	@Override
	protected void onResume()
	{
		Log.e("reywas","onResume");
		
		// Set the orientation.
		if(constants.is_landscape) {
	    	ref.android.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			ref.android.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}	
		
		super.onResume();
		open_gl_surface_view.onResume();
//		ref.main.onLoad();

		//do other unpause stuff here
	}

	@Override
	protected void onPause()
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		Log.e("reywas","onPause");
		open_gl_surface_view.onPause();
		ref.renderer.assets_loaded = false;
		ref.sound.pauseMusicHARD();
		ref.main.onScreenSleep();
//		System.gc();
		super.onPause();
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
//	    EasyTracker.getInstance(this).set("ga_trackingId", "UA-50122948-1");
	    EasyTracker.getInstance(this).set("&tid", constants.google_analytics_id);
	    
//	    EasyTracker tracker = EasyTracker.getInstance(this);
//	    tracker.set("&tid", "UA-XXXX-2");
//	    tracker.set("&tid", game_constants.google_analytics_id);
	    EasyTracker.getInstance(this).activityStart(this);
	  }
	
	@Override
	protected void onStop(){
		Log.e("reywas","onStop");
		
		ref.renderer.assets_loaded = false;
		ref.sound.releaseSounds();
//		ref.sound.pauseMusic();
		ref.sound.releaseMusic();
		
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	/// ad stuff //
//	private int adId = 100000;
	private AdView av; 
	protected void loadAd(int h_align, int v_align) {
		
		if (av == null) {
			if (!constants.pro) {
				RelativeLayout rl = (RelativeLayout)findViewById(R.id.adHolder);
		
				
				av = new AdView(this, AdSize.BANNER, constants.adMob_publisher_id);
				
				RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				relativeParams.addRule(h_align);
				relativeParams.addRule(v_align);
				
				
				rl.addView(av, relativeParams);
		
				AdRequest adRequest = new AdRequest();
		
				av.setEnabled(true);
		        av.setVisibility(View.VISIBLE);
				av.loadAd(adRequest);
				
			}
		}
	}
	
	protected void unLoadAd() {
		
		if (av != null) {
			av.setEnabled(false);
			av.setVisibility(View.GONE);
			av = null;
		}
		
	}
	
	/// Sound management ///
	
	private AudioManager audio;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_VOLUME_UP:
	        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
	        return true;
	    case KeyEvent.KEYCODE_BACK:
	    	ref.main.handleBackButton();
	    	return true;
	    case KeyEvent.KEYCODE_MENU:
	    	ref.main.handleMenuButton();
	    	return true;
	    default:
	        return false;
	    }
	}
	
	
	
	
	
	/// Touch input ///
    
	private int NUMBER_OF_TOUCH_POINTS = 10;
    private PointF[] touch_points_array = new PointF[NUMBER_OF_TOUCH_POINTS];
    private int temp_motion_event_action;
    private int temp_index; // the index returned from android of the touch event. 
    private int temp_id; // the finger id of the touch
    private int temp_length;
    private int temp_i;
//    private int NULL_XY_POSITION = -100;

    private void initate_touch_points() {
    	
    	ref.input.setTouchConstants();

    	for(temp_i=0; temp_i<NUMBER_OF_TOUCH_POINTS; temp_i++) {
    		touch_points_array[temp_i] = new PointF(0, 0);
    	}
    	
    	steps_held = new int[NUMBER_OF_TOUCH_POINTS];
    	steps_held_stepSync = new int[NUMBER_OF_TOUCH_POINTS];
	    for(temp_i=0; temp_i<NUMBER_OF_TOUCH_POINTS;temp_i++){
	    	steps_held[temp_i] = 0;
	    }
	    
	    for(temp_i=0; temp_i<NUMBER_OF_TOUCH_POINTS;temp_i++){
	    	steps_held_stepSync[temp_i] = steps_held[temp_i];
	    }
    }
    
    public int sys_get_touch_x(int index){
    	return (int) touch_points_array[index].x;
    }
    public int sys_get_touch_y(int index){
    	return (int) (ref.screen_height - touch_points_array[index].y);
    }
    
    private int[] steps_held;
    private int[] steps_held_stepSync;
	private int sys_steps_held = 0;
	
	protected static final int TOUCH_NONE = 0;
	protected static final int TOUCH_DOWN = 1;
	protected static final int TOUCH_HELD = 2;
	protected static final int TOUCH_UP = -1;
	
    public int sys_get_touch_state(int index){
    	
    	if (index < NUMBER_OF_TOUCH_POINTS-1){
    		sys_steps_held = steps_held_stepSync[index];
    	} else {
    		sys_steps_held = TOUCH_NONE;
    	}
    	
    	return sys_steps_held;
    }
    
    protected void sys_updateTouchDurations(){
    	for(temp_i=0; temp_i<NUMBER_OF_TOUCH_POINTS;temp_i++){
    		steps_held_stepSync[temp_i] = steps_held[temp_i];
    	}
    	
    	for(temp_i=0; temp_i<NUMBER_OF_TOUCH_POINTS;temp_i++){
    		
	    	if (steps_held[temp_i] == TOUCH_UP){
	    		steps_held[temp_i] = TOUCH_NONE;
	    	}
	    	if (steps_held[temp_i] == TOUCH_DOWN){
	    		steps_held[temp_i] = TOUCH_HELD;
	    	}
	    }
    }
    
//    public boolean onTouchEvent(MotionEvent event) {
    @Override
	public boolean dispatchTouchEvent(MotionEvent event) {
    	
    	
		temp_motion_event_action = event.getActionMasked();//getActionMasked
		
		switch (temp_motion_event_action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN: {
				sys_captureTouchDown(event);
				break;
			}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP: {
				sys_captureTouchUp(event);
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				sys_capturePointerMoves(event);
				break;
			}
		}
		
		//TODO TODO TODO
		super.dispatchTouchEvent(event);
		
		
//		try {
//	        Thread.sleep(16);
//	    } catch (InterruptedException e) {} //ignore
		
		return true;
	}

	private void sys_captureTouchDown(MotionEvent event) {
		
		temp_index = event.getActionIndex();
		temp_id = event.getPointerId(temp_index);
		
		
//		Log.e("DOWN", "ID: " + temp_id);
//		Log.e("DOWN", "INDEX: " + temp_index);
//		Log.e("DOWN", ".");
		
		if (temp_id < NUMBER_OF_TOUCH_POINTS-1){
			touch_points_array[temp_id].set(event.getX(temp_index), event.getY(temp_index));
			steps_held[temp_id] = TOUCH_DOWN;
		}
//		Log.e("DOWN", Arrays.toString(steps_held));
		
		//Log.e("reywas",temp_id + " down.");
		
	}
	
	private void sys_captureTouchUp(MotionEvent event) {
		
		temp_index = event.getActionIndex();
		temp_id = event.getPointerId(temp_index);
		
//		touch_points_array[temp_id].set(0, 0);
		//Log.e("reywas",temp_id + " up.");
		if (temp_id < NUMBER_OF_TOUCH_POINTS-1){
			steps_held[temp_id] = TOUCH_UP;
		}
	}

	
	private void sys_capturePointerMoves(MotionEvent event) {
		
		temp_length = event.getPointerCount();
		
//		Log.e("esat","" + temp_length);
			for (temp_i = 0; temp_i < temp_length; temp_i++) {
				
					try {
						temp_id = event.getPointerId(temp_i);
					} catch (Exception e) {
						Log.e("EAndroid TOUCHMOVES", "that touch glitch");
						e.printStackTrace();
					}
					
				if (temp_id < NUMBER_OF_TOUCH_POINTS-1){
					try {
						touch_points_array[temp_id].set(event.getX(temp_i), event.getY(temp_i));
					} catch (Exception e) {
						Log.e("TOUCH", "was unable to set x and y coords for moving pointer.");
					}
				}
				
				
//				steps_held[temp_id] = TOUCH_HELD;
			}
	}
}
