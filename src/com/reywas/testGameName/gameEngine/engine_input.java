package com.reywas.testGameName.gameEngine;

import java.sql.Ref;

public class engine_input {
	
	engine_reference ref;
	public engine_input(engine_reference r){
		ref = r;
	}
	
	protected void setTouchConstants() {
		TOUCH_NONE = ref.android.TOUCH_NONE;
		TOUCH_DOWN = ref.android.TOUCH_DOWN;
		TOUCH_HELD = ref.android.TOUCH_HELD;
		TOUCH_UP = ref.android.TOUCH_UP;
	}
	public int TOUCH_NONE;
	public int TOUCH_DOWN;
	public int TOUCH_HELD;
	public int TOUCH_UP;

	
	// These 3 methods are to keep things clean and coherent.
	public int get_touch_x(int index){ return ref.android.sys_get_touch_x(index); }
	public int get_touch_y(int index){ return ref.android.sys_get_touch_y(index); }
	public int get_touch_state(int index){ return ref.android.sys_get_touch_state(index); }

	



	// TODO: implement these?
	//public int get_touch_x_previous(int index){ return ref.android.sys_get_touch_x_previous(index); }
	//public int get_touch_y_previous(int index){ return ref.android.sys_get_touch_y_previous(index); }
}
