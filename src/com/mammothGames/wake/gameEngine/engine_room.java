package com.mammothGames.wake.gameEngine;

import com.mammothGames.wake.game.game_rooms;
//import com.mammothGames.wake.testbed.game_rooms;

public class engine_room {
	
	engine_reference ref;
	game_rooms rooms;
	public engine_room(engine_reference r){
		ref = r;
		// By setting the new room to 0, something other than current_room's -1, room 0 will be started automatically. 
		current_room = -1;
		new_room = 0;
		
		rooms = new game_rooms(ref);
	}

	private int current_room;
	
	private int new_room, old_room;
	public int get_current_room(){
		//So we can get the room, but not be able to mess with it.
		return current_room;
	}
	
	public String get_current_room_name(){
		return rooms.current_room_name;
	}
	
	public void changeRoom(int index){
		new_room = index;
	}	
	
	
	public void sys_moveRooms(){
		if (current_room != new_room){
			
			
			ref.main.deleteAllNonPersistentEntities();
			ref.main.cleanDeletedEntitiesStepIndependent();
			ref.partPool.sys_clearParticleSystem();
			old_room = current_room;
			current_room = new_room;
			
			// If the room move failed, then we assume it's because we tried to move to a non-existent room. So we roll back the changes.
			if ( ! rooms.sys_startRoom(new_room) ) {
				current_room = old_room;
				new_room = old_room;
			}
			ref.main.sys_condenseEntityList();
			
		}
	}
	
}
