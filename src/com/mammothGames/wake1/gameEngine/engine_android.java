package com.mammothGames.wake1.gameEngine;

import java.util.Arrays;
import java.util.HashMap;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1free.R;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
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

import com.google.android.gms.ads.*;
import com.google.android.gms.common.*;
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
	    
	    
	    ref.renderer = new engine_gl_renderer(ref);
	    
	    // TODO: uncomment this if older phones are crashign again. The getHolder line should make 8888 work.
	    // open_gl_surface_view.setEGLConfigChooser(5, 6, 5, 0, 0, 0);
	    open_gl_surface_view.setEGLConfigChooser(8, 8, 8, 8, 0, 0); 
	    open_gl_surface_view.getHolder().setFormat(PixelFormat.RGBA_8888);
	    
	    // Turn on error-checking and logging
	    // open_gl_surface_view.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);
	    
		open_gl_surface_view.setRenderer(ref.renderer);
		open_gl_surface_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		ref.floatbuffers = new engine_gl_floatbuffers(ref);
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			
		
		//Touch stuff
		initate_touch_points();
		
		loadInterstitialAd();
	}
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void onWindowFocusChanged(boolean hasFocus) {
	        super.onWindowFocusChanged(hasFocus);
	    if (Integer.valueOf(android.os.Build.VERSION.SDK) >= Build.VERSION_CODES.KITKAT) {
		    if (hasFocus) {
		    	getWindow().getDecorView().setSystemUiVisibility(
		                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		                | View.SYSTEM_UI_FLAG_FULLSCREEN
		                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
		}
	}
	
//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//		
//		
//		
//	    //if (Integer.valueOf(android.os.Build.VERSION.SDK) >= Build.VERSION_CODES.JELLY_BEAN) {
////	        getWindow().getDecorView().setSystemUiVisibility(
////	                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////	                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////	                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////	                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////	                | View.SYSTEM_UI_FLAG_FULLSCREEN
////	                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//	    //}
//    }
	
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
		super.onResume();
		
		// Set the orientation.
		if(constants.is_landscape) {
	    	ref.android.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			ref.android.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		
		// checkGooglePlayServices();
		if(adViewBanner != null)
			adViewBanner.resume();	
		
		open_gl_surface_view.onResume();
		
		
	}

	@Override
	protected void onPause()
	{
		Log.e("reywas","onPause");
		open_gl_surface_view.onPause();
		ref.renderer.assets_loaded = false;
		ref.sound.pauseMusicHARD();
		ref.main.onScreenSleep();
		
		if(adViewBanner != null)
			adViewBanner.pause();
		
//		System.gc();
		super.onPause();
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    if (!constants.devmode) {
	    	EasyTracker.getInstance(this).set("&tid", constants.google_analytics_id);
	    	EasyTracker.getInstance(this).activityStart(this);
	    }
	  }
	
	@Override
	protected void onStop(){
		Log.e("reywas","onStop");
		
		ref.sound.pauseMusicHARD();
//		ref.sound.pauseMusic();
		
		EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}
	
	@Override
	protected void onDestroy()
	{
		
		if(adViewBanner != null)
			adViewBanner.destroy();
		
		ref.renderer.assets_loaded = false;
		ref.sound.releaseSounds();
		ref.sound.releaseMusic();
		
		super.onDestroy();
	}
	
	/// ad stuff //
	private AdView adViewBanner; 
	protected void loadBannerAd(int h_align, int v_align) {
		if (adViewBanner == null) {
			if (!constants.pro) {
				RelativeLayout rl = (RelativeLayout)findViewById(R.id.adHolder);
				
				adViewBanner = new AdView(this);
				adViewBanner.setAdUnitId(constants.adMob_banner_id);
				adViewBanner.setAdSize(AdSize.BANNER);
				
				RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				relativeParams.addRule(h_align);
				relativeParams.addRule(v_align);
				
				
				rl.addView(adViewBanner, relativeParams);

				AdRequest request;
				if (constants.devmode)
					request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("8CDF2127BAF995A9AE9BA2B6098E7C34").build();
				else
					request = new AdRequest.Builder().build();
		
				adViewBanner.setEnabled(true);
		        adViewBanner.setVisibility(View.VISIBLE);
				adViewBanner.loadAd(request);
				
			}
		}
		
		/*
		// This setup is for having the ad separate from the game, not on top
		LinearLayout ll = (LinearLayout)findViewById(R.id.topView);
		ll.setBackgroundColor(Color.GRAY);
		av = new AdView(this, AdSize.BANNER, constants.adMob_publisher_id);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity=align_mode;
		av.setLayoutParams(params);
		ll.addView(av);
		*/
	}
	
	/*
	// Not needed because AdMob ads work with without the rest of G.P.S. installed.
	private boolean google_play_services_avaliable = true;
	private void checkGooglePlayServices() {
		google_play_services_avaliable = true;
		// Check for current Google Play Services
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode == ConnectionResult.SUCCESS) {
			google_play_services_avaliable = true;
		} else if (resultCode == ConnectionResult.SERVICE_MISSING ||
		           resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED ||
		           resultCode == ConnectionResult.SERVICE_DISABLED) {
		    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1);
		    
		    // Make it quit the app without installing
		    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    engine_android.this.finish();
                }
            });
		    
		    dialog.show();
		}
	}
	 */
	
	protected void unLoadBannerAd() {
		
		if (adViewBanner != null) {
			adViewBanner.setEnabled(false);
			adViewBanner.setVisibility(View.GONE);
			adViewBanner = null;
		}
		
	}
	
	
	private InterstitialAd interstitial;
		
	protected void loadInterstitialAd() {
			
		if ( (!constants.pro) ) {
		// if ( (!constants.pro) && (google_play_services_avaliable) ) {
			
			// Create the Interstitial.
			interstitial = new InterstitialAd(this);
			interstitial.setAdUnitId(constants.adMob_interstitial_id);
		
		    // Create ad request.
		    AdRequest adRequest;
			
			if (constants.devmode)
				adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("8CDF2127BAF995A9AE9BA2B6098E7C34").build();
			else
				adRequest = new AdRequest.Builder().build();
				

			interstitial.loadAd(adRequest);
			// Begin loading your Interstitial.
		}
		
	}
	
	protected void showInterstitialAd() {
		if ( (!constants.pro) && (interstitial.isLoaded()) ) {
		// if ( (google_play_services_avaliable) && (interstitial.isLoaded()) ) {
			interstitial.show();
			loadInterstitialAd(); //so we have the next one to show as soon as possible.
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
