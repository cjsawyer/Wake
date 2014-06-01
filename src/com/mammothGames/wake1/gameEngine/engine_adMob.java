package com.mammothGames.wake1.gameEngine;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.mammothGames.wake1.game.constants;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class engine_adMob {
	
	/*
	 * to implement:
	 * getting ad id from game constants
	 * constants for positioning horizontal/vertical
	 * ad size
	 * load/unload ad
	 */

	private engine_reference ref;
	
	public final int H_LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
	public final int H_RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
	public final int H_CENTER = RelativeLayout.CENTER_HORIZONTAL;
	
	public final int V_TOP = RelativeLayout.ALIGN_PARENT_TOP;
	public final int V_BOTTOM = RelativeLayout.ALIGN_PARENT_BOTTOM;
	public final int V_CENTER = RelativeLayout.CENTER_VERTICAL;
	
	public engine_adMob(engine_reference ref) {
		this.ref = ref;
	}
	
	public void loadAd(final int h_align, final int v_align) {
		
		if (!constants.devmode) {
			
			switch(h_align) {
				case H_LEFT: break;
				case H_RIGHT: break;
				case H_CENTER: break;
				default:
					if(constants.devmode)
						Log.e("reywas", "Invalid horizontal ad alignment argument! Use the constants in ref.ad");
			}
			
			switch(v_align) {
				case V_TOP: break;
				case V_BOTTOM: break;
				case V_CENTER: break;
				default:
					if(constants.devmode)
						Log.e("reywas", "Invalid vertical ad alignment argument! Use the constants in ref.ad");
			}
			
			
			ref.android.runOnUiThread(new Runnable() {
				  public void run() {
				    ref.android.loadAd(h_align,v_align);
				  }
			});
		}
		
	}
	public void unLoadAd() {
		ref.android.runOnUiThread(new Runnable() {
			  public void run() {
			    ref.android.unLoadAd();
			  }
		});
	}
}

