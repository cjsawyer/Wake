package com.mammothGames.wake.testbed.feature_tests;

import com.mammothGames.wake.gameEngine.*;


public class entity_poolTest extends engine_entity {

	utility_pool<test_pool_object> test_pool;
	test_pool_object temp_object;
	
	@Override
	public void sys_firstStep(){
		int a = ref.screen_height;
		test_pool = new utility_pool<test_pool_object>(ref, test_pool_object.class, 200);
		for(int temp_i=0;temp_i<test_pool.MAX_OBJECTS;temp_i++) {
			test_pool.takeObject();
		}
	}
	
	@Override
	public void sys_step(){
		
		
		for(int temp_i = 0; temp_i < test_pool.MAX_OBJECTS; temp_i++) {
			temp_object = (test_pool_object) test_pool.objects[temp_i];
			if ((temp_object.sys_in_use)) {
				
				// start area to put object logic
				
				ref.draw.setDrawColor(temp_object.r, temp_object.g, temp_object.b, 1);
				ref.draw.drawCircle(temp_object.x, temp_object.y, temp_object.radius, 0, 0, 360, 0, 0);
				temp_object.y += temp_object.speed * ref.main.time_scale;
				
				if (temp_object.y >= ref.screen_height + temp_object.radius) {
					test_pool.returnObject(temp_object.sys_id);
				}
				
				// end
			}
		}	
				
		if (test_pool.getNumberObjectsFree() > 0) {
			test_pool.takeObject();
		}
	}
}