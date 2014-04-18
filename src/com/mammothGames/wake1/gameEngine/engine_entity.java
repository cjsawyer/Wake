package com.mammothGames.wake1.gameEngine;

import android.util.Log;

public class engine_entity {
	
	public engine_reference ref;
	
	protected int id = -1;
	
	protected boolean persistent = false;
	protected boolean paused = false, pausable = true;
	
	
//	public void sys_set_id(int id){ this.id = id; }
//	public int entity_get_id(){ return this.id; }
	
	protected void sys_initiate_entity(engine_reference r){
		ref = r;
				
		sys_initAlarms();
	}
	
	public void backButton() { }
	public void menuButton() { }
	
	public void onScreenSleep() { }
	public void onRoomLoad() { }
	
	
	Boolean sys_delete_me = false;
	public void instance_destroy(){
		sys_delete_me = true;
	}
	
	/////////////////ALARMS/////////////////
	public double[] alarm = new double[13];
	protected void sys_initAlarms(){
		alarm[0] = -1;
		alarm[1] = -1;
		alarm[2] = -1;
		alarm[3] = -1;
		alarm[4] = -1;
		alarm[5] = -1;
		alarm[6] = -1;
		alarm[7] = -1;
		alarm[8] = -1;
		alarm[9] = -1;
		alarm[10] = -1;
		alarm[11] = -1;
		alarm[12] = -1;
	}
	protected void alarmStep(){
		// Bug-fix. The first dt is very large, this fixes that.
		if (ref.renderer.sys_first_step_executed == false){
			ref.main.time_delta = 0;
		}
		
		for (int i = 0; i < 12; i++){
			// For each alarm, tick if it has time.
			if (alarm[i] > 0){
				alarm[i] -= ref.main.time_delta;
				
				if (alarm[i] <= 0){
					alarm[i] = 0;
				}
			}
			// If the alarm runs out, then do the action.
			if (alarm[i] == 0){
				alarm[i] = -1;
				alarmAction(i);
			}
		}
	}
	
	
	protected void alarmAction(int alarmNumber){
		switch (alarmNumber){
			case 0: alarm0(); break;	
			case 1: alarm1(); break;
			case 2: alarm2(); break;
			case 3: alarm3(); break;
			case 4: alarm4(); break;
			case 5: alarm5(); break;
			case 6: alarm6(); break;
			case 7: alarm7(); break;
			case 8: alarm8(); break;
			case 9: alarm9(); break;
			case 10: alarm10(); break;
			case 11: alarm11(); break;
			case 12: alarm12(); break;
			default: Log.e("reywas", "You called an alarm outside of the range 0-12");
		}
	}
	
	private void sys_showAlarmError(int alarm_index){
		Log.e("reywas", "You called alarm" + alarm_index + " but didn't @Override the default!");
	}
	
	public void alarm0(){sys_showAlarmError(0);}
	public void alarm1(){sys_showAlarmError(1);}
	public void alarm2(){sys_showAlarmError(2);}
	public void alarm3(){sys_showAlarmError(3);}
	public void alarm4(){sys_showAlarmError(4);}
	public void alarm5(){sys_showAlarmError(5);}
	public void alarm6(){sys_showAlarmError(6);}
	public void alarm7(){sys_showAlarmError(7);}
	public void alarm8(){sys_showAlarmError(8);}
	public void alarm9(){sys_showAlarmError(9);}
	public void alarm10(){sys_showAlarmError(10);}
	public void alarm11(){sys_showAlarmError(11);}
	public void alarm12(){sys_showAlarmError(12);}
	
	////////////////////////////////////////
	
	
	public boolean sys_first_step_executed = false;
	
	public void sys_firstStep(){ }
	
	public void sys_step(){ }
	public void sys_afterStep(){ }
	public void sys_beforeStep(){ }
}