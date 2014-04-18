package com.mammothGames.wake1.gameEngine;

import java.lang.reflect.Array;

import android.util.Log;

import com.mammothGames.wake1.game.game_constants;

public class utility_pool <T extends utility_poolObject> {

	//private T type;
	
	public utility_poolObject objects[];
	public final int MAX_OBJECTS;
	private int ids = 0;
	
	protected utility_poolObject temp_object;
	private int temp_i;
	
	public int objects_current = 0;
	
	
	private Class<T> clazzOfT; 
	
	private engine_reference ref;
	
	public utility_pool (engine_reference r, Class<T> clazz, int max_objects){
	
	clazzOfT = clazz;
	
	ref = r;
	MAX_OBJECTS = max_objects;
	
	objects = new utility_poolObject[MAX_OBJECTS];
	
	for(temp_i=0;temp_i<MAX_OBJECTS;temp_i++) {
		
//			try {
//				addObject((utility_poolObject)object_class.newInstance());
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
		try {
			temp_object = clazzOfT.newInstance();
			temp_object.ref = ref;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addObject(temp_object);
	}
}
	
	private void addObject(utility_poolObject new_object) {
		
//		try {
//			temp_object = (utility_poolObject) getInstance();
//		} catch (Exception e) { e.printStackTrace(); }
//		
//		if (temp_object == null)
//    		Log.e("asd", "aaaaaaaaaaaa");
		
		temp_object = new_object;
//		temp_object.set_ref(ref);
		
		temp_object.sys_id = ids++;
		temp_object.sys_in_use = false;
		objects[ids-1] = temp_object;
		
		//objects_current++;
	}
//	public T[] createArray(Class<T> cls, int size) throws Exception  
//	{  
//	    @SuppressWarnings("unchecked") T[] array = (T[])Array.newInstance(cls, size);  
//	    return array;  
//	}  
	
//	private Class<T> classOfT;  
//	private T getInstance(int id) throws Exception {  
    public T getInstance(int id) {  
//        //return new T(); // won't compile  
//        //return T.newInstance(); // won't compile  
//        //return T.class.newInstance(); // won't compile  
////        return classOfT.newInstance();  
//    	
//        return (T) rtrn; 
        return (T) objects[id]; 
    }
	public int getNumberObjectsFree() {
//		Log.e("free", "free: " + (MAX_OBJECTS - objects_current));
		return MAX_OBJECTS - objects_current;
	}
	
	public T takeObject() {
		for(temp_i=0;temp_i<MAX_OBJECTS;temp_i++) {
			
			temp_object = objects[temp_i];
			
			if (!temp_object.sys_in_use) {
				
//				Log.e("reywas", "took " + temp_i);
				
				temp_object.sys_in_use = true;
				
				objects_current++;
				
				return (T) temp_object;
			}
		}
		
		//We got through the whole array without finding an open space, so we can't return anything logical. So we just return the last one in the list.
		//It's probably so old that it won't be noticed.
		temp_object.sys_in_use = true;
		return (T) temp_object;
	}
	
	public void returnObject(int id) {
		for(int temp_i=0;temp_i<MAX_OBJECTS;temp_i++) {
			temp_object = objects[temp_i];
			if(temp_object.sys_id == id) {
//				Log.e("reywas", "returned " + id + "  t_i: " + temp_i);
				temp_object.sys_in_use = false;
				objects_current--;
			}
		}
	}
	
	/**
	 * Use when moving rooms to reset system
	 */
	public void resetSystem() {
		
		for(temp_i=0;temp_i<MAX_OBJECTS;temp_i++) {
			objects[temp_i].sys_in_use=false;
		}
		objects_current = 0;
	}
}
