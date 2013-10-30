package com.mammothGames.wake.game;

import android.util.Log;

import com.mammothGames.wake.game.entities.entity_mammothLoadingScreen;
import com.mammothGames.wake.game.entities.entity_menuMain;
import com.mammothGames.wake.gameEngine.engine_reference;

public class game_rooms {
	
/*
	Copy this to add a room:
	
		case #: {
			current_room_name = "Menu loader";
			
			break;
		}

	Then add a constant for it.

*/
	
	public static int ROOM_MENU = 2;
	public static int ROOM_GAME = 3;
	public static int ROOM_POSTGAME = 4;

	public boolean sys_startRoom(int index){
		tHasMovedRoom = true;
		switch(index){
		
			case 0: {
				// Default Room, for initializing persistent objects and loading.
				
				ref.draw.setBackgroundColor(0,0,0);
//				ref.main.addEntity(new entity_particleTest());
//				ref.main.addEntity(new entity_loadingDebug());
//				ref.main.addEntity(new entity_debugLoadingScreen());
				ref.main.addEntity(new entity_mammothLoadingScreen());
				break;
			}
			
			case 1: {
				current_room_name = "Menu loader";

				ref.main.addEntity(new entity_menuMain());
				
				ref.room.changeRoom(2);
				break;
			}
			
			case 2: {
				current_room_name = "Menu";
				break;
			}
			
			case 3: {
				current_room_name = "Flux";
				break;
			}
			
			case 4: {
				current_room_name = "Post";
				break;
			}
			
			
			
			default: {
				tHasMovedRoom = false;
				Log.e("reywas","Called room.sys_startRoom() with and invalid room index (" + index + ").");
			}
		}
		
		if(tHasMovedRoom) {
			if (game_constants.devmode) {
				Log.e("reywas", "Started room (" + index + ") " + current_room_name);
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	
	
	
	// Do not modify the following.
	private boolean tHasMovedRoom;
	public String current_room_name = "";
	private engine_reference ref;
	public game_rooms(engine_reference r) {
		ref = r;
	}
	
}
