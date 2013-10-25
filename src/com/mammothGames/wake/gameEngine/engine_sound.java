package com.mammothGames.wake.gameEngine;

import java.io.IOException;
import java.util.HashMap;

import com.mammothGames.wake.game.game_sounds;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class engine_sound {
	
	// Based off of this: http://www.divided-games.com/giving-your-android-game-sounds/
	
	public enum SoundState {
		PLAYING, PAUSED,
	}

	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private Context mContext;
	
	private byte channels = 4;

	private engine_reference ref;
	
	private AssetManager assets;
	
	private boolean[] sound_was_loaded;
	private int music_loaded = -1;
	private boolean music_playing = false;
	private boolean music_playing_was_looped = false;
	
	public engine_sound(engine_reference ref) {
	
		sound_was_loaded = new boolean[game_sounds.sound_array.length];
		for(int i=0; i<sound_was_loaded.length; i++) {
			sound_was_loaded[i] = false;
		}
		
		this.ref = ref;
		initSounds(ref.android);
//		music_loaded = -1;
	}

	private boolean mute = false;
	
	protected void initSounds(Context theContext) {
		mContext = theContext;
		assets = mContext.getAssets();
		// The first number here is the number of audio channels
		mSoundPool = new SoundPool(channels, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	}

//	public void addSound(int index, int SoundID) {
//		mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
//	}
	public void loadSound(int index) {
		String name_and_extesion = game_sounds.sound_array[index];
		int mySoundId = 0;
		try {
		    mySoundId = mSoundPool.load(assets.openFd("sounds/" + name_and_extesion), 1);
		    sound_was_loaded[index] = true;
		    ref.loadHelper.informThatOneLoaded();
		} catch (IOException e) {
			Log.e("reywas", "Couldn't load sound " + name_and_extesion + " from assets!");
		}
		mSoundPoolMap.put(index, mySoundId);
	}

	public void playSound(int index) {
		if (!mute) {
//			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			mSoundPool.play(mSoundPoolMap.get(index), 1, 1, 1, 0, 1);
		}
	}
	public void playSoundSpeedChanged(int index, float percentChangeRange) {
		if (!mute) {
//			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			percentChangeRange /= 2f;
			
			mSoundPool.play(mSoundPoolMap.get(index), 1, 1, 1, 0, ref.main.randomRange(1-percentChangeRange, 1+percentChangeRange));
		}
	}


	
	protected void reloadSounds() {
		for(int i=0; i<sound_was_loaded.length; i++) {
			if (sound_was_loaded[i]) {
				loadSound(i);
			}
		}
	}
	public void releaseSounds() {
		if (mSoundPool != null) {
			mSoundPool.release();
			mSoundPool = null;
		}
	}
	
	
	
	
	
	
	public void setMusicState(boolean mute, boolean startPlayingIfNotAlready, boolean loop) {
		this.mute= mute;   
		if (mute) {
			// Mute everything
			for(int i=0; i<game_sounds.sound_array.length; i++) {
				mSoundPool.setVolume(i, 0, 0);
			}
			
			if (mPlayer != null) {
				mPlayer.setVolume(0, 0);
			}
		} else {
			//unmute everything
//			float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			
			for(int i=0; i<game_sounds.sound_array.length; i++) {
				mSoundPool.setVolume(i, 1, 1);
			}
			if (mPlayer != null) {
				mPlayer.setVolume(1, 1);
				
				if (startPlayingIfNotAlready) {
					if (!mPlayer.isPlaying()) {
						playMusic(loop);
					}
				} else {
						stopMusic();
				}
			}
		}
	}
	
	MediaPlayer mPlayer;
	public void loadMusic(int index) {
		
		releaseMusic();
		
		String name_and_extesion = game_sounds.music_array[index];
		
		try {
			AssetFileDescriptor afd = assets.openFd("music/" + name_and_extesion);
			
			mPlayer = new MediaPlayer();
			 try {
				mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
				mPlayer.prepare();
				music_loaded = index;
				ref.loadHelper.informThatOneLoaded();
			} catch (IllegalArgumentException e) {
			} catch (IllegalStateException e) {
			} catch (IOException e) {
			}
			
		} catch (IOException e) {
			Log.e("reywas", "Couldn't load sound " + name_and_extesion + " from assets!");
		}
		 
	}
	
	private void loadAndStartMusic(int index) {
		
		releaseMusic();
		
		String name_and_extesion = game_sounds.music_array[index];
		
		try {
			AssetFileDescriptor afd = assets.openFd("music/" + name_and_extesion);
			
			mPlayer = new MediaPlayer();
			 try {
				mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
				mPlayer.prepare();
				music_loaded = index;
				ref.loadHelper.informThatOneLoaded();
				playMusic(music_playing_was_looped);
			} catch (IllegalArgumentException e) {
			} catch (IllegalStateException e) {
			} catch (IOException e) {
			}
			
		} catch (IOException e) {
			Log.e("reywas", "Couldn't load sound " + name_and_extesion + " from assets!");
		}
		 
	}
	
	private void playMusic(boolean loop) {
		if (!mute) {
			music_playing = true;
			mPlayer.setLooping(loop);
			music_playing_was_looped = loop;
			mPlayer.start();
		}
	}
	public boolean getIsMusicPlaying() {
		if (mPlayer.isPlaying()) {
			return true;
		} else {
			return false;
		}
	}
	public void pauseMusic() {
		music_playing = false;
		mPlayer.pause();
	}
	public void stopMusic() {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
			mPlayer.seekTo(0);
			music_playing = false;
		}
	}

	public void reloadMusic() {
		if (!(music_loaded == -1)) {
			if (music_playing) {
				loadAndStartMusic(music_loaded);
			} else {
				loadMusic(music_loaded);
			}
		}
	}
	public void releaseMusic() {
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

	
}
