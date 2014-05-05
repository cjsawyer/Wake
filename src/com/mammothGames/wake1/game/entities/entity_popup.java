package com.mammothGames.wake1.game.entities;

import android.util.Log;

import com.mammothGames.wake1.game.constants;
import com.mammothGames.wake1.game.textures;
import com.mammothGames.wake1.gameEngine.*;


public class entity_popup extends engine_entity {

	private boolean popup_open = false;
	private float popup_alpha = 0, popup_alpha_target = 0;
	
	masterGameReference mgr;
	public entity_popup(masterGameReference mgr) {
		this.persistent = true;
		this.pausable = false;

		this.mgr = mgr;
	}

	
	
	public final int STATE_ABANDON = 0;
	public final int STATE_QUIT = 1;
	public final int STATE_ERASE = 2;
	public final int STATE_TEST = 3;
	
	private final String[][] state_text = {
			{ "Abandon game?" },
			{ "Quit game?" },
			{ "Erase records?" },
			{ "text 1", "text 2", "text the third" },
	};
	
	public final int NULL = 0;
	public final int UI_TITLE = 1;
	public final int UI_TEXT = 2;
	public final int UI_BUTTON = 3;
	public final int UI_CHECKBOX = 4;
	
	public final int ACT_NO = 1;
	public final int ACT_YES = 2;
	public final int ACT_SETTINGS = 2;
	
	private final int[][][] ui = {
			// UI type, action
			{{1,1,UI_TEXT}, {NULL}},
			{{1,1,UI_TEXT}, {NULL}},
			{{1,1,UI_TEXT}, {NULL}},

			{
				{ 
					// horizontal, vertical number of GUI elements
					3,6
				}, {
					// Type of GUI element
					NULL,		UI_TITLE,	NULL,
					UI_TEXT,	NULL,		UI_TEXT,
					UI_CHECKBOX,NULL,		UI_TEXT,
					UI_TEXT,	NULL,		UI_TEXT,
					NULL,		UI_BUTTON,	NULL,
					UI_BUTTON,	NULL,		UI_BUTTON
				}, {
					// Actions when that element is clicked
					NULL,		NULL,			NULL,
					NULL,		NULL,			NULL,
					NULL,		NULL,			NULL,
					NULL,		NULL,			NULL,
					NULL,		ACT_SETTINGS,	NULL,
					ACT_YES,	NULL,			ACT_NO
				}

			},

	};
	
	private void drawUI(float ui_x, float ui_y, float ui_w, float ui_h) {
		
		// Number of elements
		int elements_x = ui[action][0][0];
		int elements_y = ui[action][0][1];
		
		float[] row_weight = new float[elements_x];
		int[] row_type = new int[elements_x];
		int[] row_action = new int[elements_x];
		int row_i = 0;
		
		float[] element_x = new float[elements_x*elements_y];
		float[] element_y = new float[elements_x*elements_y];
		float[] element_w = new float[elements_x*elements_y];
		float[] element_h = new float[elements_x*elements_y];
		int[] element_type = new int[elements_x*elements_y];
		int[] element_action = new int[elements_x*elements_y];
		int element_i = 0;
		
		int ui_i = 0;
		
		for (int iy=0; iy<elements_y; iy++) {
			
			float y = ui_y+ui_h/2 - (iy+0.5f)*(ui_h/((float)elements_y) );
			
			row_i = 0;
			for (int ix=0; ix<elements_x; ix++) {
				
				int type = ui[action][1][ui_i]; 
				
				if (type != NULL) {
					row_weight[row_i] = 1;
					row_type[row_i] = type;
					row_action[row_i] = ui[action][2][ui_i];
					row_i++;
				}
				
				ui_i++;
				
			}
			
			//sum row weights
			float total_row_weight = 0;
			for (int i=0; i<row_i; i++) {
				total_row_weight += row_weight[i];
			}

			//fill element data
			float placed_width = 0;
			for (int i=0; i<row_i; i++) {
				
				element_w[element_i] = ui_w * (row_weight[i]/total_row_weight);
				
				element_h[element_i] = ui_h/(elements_y);
				element_y[element_i] = ui_y+ui_h/2 - iy*element_h[element_i] - element_h[element_i]/2f;
				
				
				element_x[element_i] = ui_x-ui_w/2 + placed_width + element_w[element_i]/2f;
				placed_width += element_w[element_i];
				
				element_type[element_i] = row_type[i];
				element_action[element_i] = row_action[i];
				element_i++;
				
			}
			
		}
		
		
		//Draw elements
		for (int i=0;i<element_i;i++) {
			ref.draw.setDrawColor(ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), popup_alpha);
			ref.draw.drawRectangle(element_x[i], element_y[i], element_w[i], element_h[i], 0, 0, 0, 100);
			
			switch(element_type[i]) {
				case UI_TITLE:
					ref.draw.text.append("TITLE");
					break;
				case UI_TEXT:
					ref.draw.text.append("TEXT");
					break;
				case UI_BUTTON:
					ref.draw.text.append("BUTTON");
					break;
				case UI_CHECKBOX:
					ref.draw.text.append("CHECK");
					break;
			}
			ref.draw.setDrawColor(1, 1, 1, popup_alpha);
			ref.draw.drawText(element_x[i], element_y[i], mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 200, textures.TEX_FONT1);
			
		}
	}
	
//	float x = x-ui_w/2 + (ix+0.5f)*(ui_w/((float)elements_x) );
//	int action = ui[action][1][ui_i];
	
//	float width = w/(elements_x);
//	float height = h/(elements_y);
//	int type = ui[action][1][size];
//	int weight = 1;
	
	//ref.draw.setDrawColor(ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), ref.main.randomRange(0, 1), popup_alpha);
	//ref.draw.drawRectangle(draw_x, draw_y, width, height/2, 0, 0, 0, 101);
	
//	if (element_type[size-b] == NULL) {
//		ref.draw.setDrawColor(1, 0, 0, popup_alpha);
//		ref.draw.text.append(b);
//		ref.draw.drawText(draw_x-width/2, draw_y, 30, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, 200, textures.TEX_FONT1);
//		//ref.draw.drawCircle(draw_x-width/2, draw_y, 10, 0, 0, 360, 0, 200);
//	}
	
//	for(int f=iw; f<elements_x; f++) {
//		if (element_type[size-b] == NULL) {
//			ref.draw.setDrawColor(1, 0, 0, popup_alpha);
//			ref.draw.drawCircle(draw_x-width/2, draw_y, 10, 0, 0, 360, 0, 100);
//		}
//	}

	
	public void buttonAction(boolean yes_or_no_action) {
		switch (action) {
		
			case STATE_ABANDON:
				if (yes_or_no_action) {
					setPopupOpenness(false);
					mgr.menuPauseHUD.setPause(false);
					mgr.gameMain.floor_height_target = 0;
					mgr.gameMain.endGame();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_QUIT:
				if (yes_or_no_action) {
					ref.main.exitApp();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_ERASE:
				if (yes_or_no_action) {
					ref.file.save("SCO_E", String.valueOf(0) );
					ref.file.save("STR_E", String.valueOf(0) );
					ref.file.save("PLY_E", String.valueOf(0) );
					
					ref.file.save("SCO_M", String.valueOf(0) );
					ref.file.save("STR_M", String.valueOf(0) );
					ref.file.save("PLY_M", String.valueOf(0) );
					
					ref.file.save("SCO_H", String.valueOf(0) );
					ref.file.save("STR_H", String.valueOf(0) );
					ref.file.save("PLY_H", String.valueOf(0) );
					
					ref.file.save("SCO_HE", String.valueOf(0) );
					ref.file.save("STR_HE", String.valueOf(0) );
					ref.file.save("PLY_HE", String.valueOf(0) );
					
					mgr.menuRecords.loadScores();
					mgr.areYouSure.setPopupOpenness(false);
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
			case STATE_TEST:
				if (yes_or_no_action) {
					ref.main.exitApp();
				} else {
					mgr.areYouSure.setPopupOpenness(false);
				}
				break;
				
		}
	}
	
	
	
	@Override
	public void sys_firstStep() {}
	
	@Override
	public void sys_step() {
		
		popup_alpha += (popup_alpha_target - popup_alpha) * 8f * ref.main.time_scale;
		
//		float border_draw_width = ref.screen_width*3.5f/5;
		ref.draw.text.append(state_text[action][0]);
		float border_draw_width = ref.draw.getTextWidth(mgr.gameMain.text_size, textures.TEX_FONT1) + 4 * mgr.menuDifficulty.button_border_size;
		
		
		float title_h, text_h, button_h;
		text_h = 4*mgr.gameMain.text_size;
		title_h = 2*mgr.gameMain.text_size;
		button_h = mgr.menuDifficulty.button_height - mgr.menuDifficulty.button_border_size;
		
		
		float draw_width = border_draw_width - mgr.menuDifficulty.button_border_size;
		float draw_x = ref.screen_width/2;
		float draw_y = ref.screen_height/2;
		
//		float black_height = ;//draw_width - mgr.menuDifficulty.button_border_size/2 - button_height;
//		float black_y = ;//draw_y + draw_width/2 - black_height/2;
		
		// relative to the above vars
		float small_button_width = draw_width/2 - mgr.menuDifficulty.button_border_size/4;
		float small_button_x = draw_width/4 + mgr.menuDifficulty.button_border_size/8; //offset from center, so it can be used for both buttons in the 2 button wide row 
		float button_y = 0;//-draw_width/2 + button_height/2;
		
		// black veil covering everything under popup
		ref.draw.setDrawColor(0,0,0, 0.7f * popup_alpha);
		ref.draw.drawRectangle(ref.screen_width/2, ref.screen_height/2, ref.screen_width, ref.screen_height, 0, 0, 0, constants.layer7_overHUD);
		
		/*
		// Blue border rectangle
		ref.draw.setDrawColor(0, 1, 1, 0.3f * popup_alpha);
		ref.draw.drawRectangle(draw_x, draw_y, border_draw_width, border_draw_width, 0, 0, 0, constants.layer7_overHUD);
		
		// Black inner rectangle
		ref.draw.setDrawColor(0, 0, 0, popup_alpha);
		ref.draw.drawRectangle(draw_x, black_y, draw_width, black_height, 0, 0, 0, constants.layer7_overHUD);
		
		// Yes Button rectangle
		ref.draw.setDrawColor(0, 0.4f, 0, popup_alpha);
		ref.draw.drawRectangle(draw_x-button_x, draw_y+button_y, button_width, button_height, 0, 0, 0, constants.layer7_overHUD);
		
		// No button rectangle
		ref.draw.setDrawColor(0.4f, 0, 0, popup_alpha);
		ref.draw.drawRectangle(draw_x+button_x, draw_y+button_y, button_width, button_height, 0, 0, 0, constants.layer7_overHUD);
		*/
		
//		// Popup text
//		ref.draw.setDrawColor(1, 1, 1, 0.8f * popup_alpha);
//		ref.draw.text.append(state_text[action][0]);
//		ref.draw.drawText(draw_x, black_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer7_overHUD, textures.TEX_FONT1);
		
		
		ref.draw.setDrawColor(1, 1, 1, 0.8f * popup_alpha);
		ref.draw.text.append("YES");
		ref.draw.drawText(draw_x-small_button_x, draw_y+button_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer7_overHUD, textures.TEX_FONT1);
		
		ref.draw.text.append("NO");
		ref.draw.drawText(draw_x+small_button_x, draw_y+button_y, mgr.gameMain.text_size, ref.draw.X_ALIGN_CENTER, ref.draw.Y_ALIGN_CENTER, constants.layer7_overHUD, textures.TEX_FONT1);
		
		drawUI(ref.screen_width/2, ref.screen_height/2, ref.screen_width*2/3, ref.screen_width*2/3);
		
//		if(popup_open) {
//			
//			if (popup_alpha > 0.8f) {
//				if (ref.input.get_touch_state(0) == ref.input.TOUCH_DOWN) {
//					if (ref.collision.point_AABB(button_width, button_height, draw_x-button_x, draw_y+button_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
//						buttonAction(true);
//					} else if (ref.collision.point_AABB(button_width, button_height, draw_x+button_x, draw_y+button_y, ref.input.get_touch_x(0), ref.input.get_touch_y(0))) {
//						buttonAction(false);
//					}
//				}
//			}
//		}
		
	}
	
	@Override
	public void onScreenSleep() {
		setPopupOpennessHard(false);
	}
	
	private int action = 0;
	private String text = "";

	public void setPopupAction(int action) {
		this.action = action;
	}
	
	public void setPopupOpenness(boolean open) {
		if (Math.abs(popup_alpha-popup_alpha_target) < 0.2f) {
			popup_open = open;
			popup_alpha = popup_alpha_target;
			popup_alpha_target = open ? 1 : 0;
		}
	}
	public void setPopupOpennessHard(boolean open) {
		popup_open = open;
		popup_alpha_target = open ? 1 : 0;
		popup_alpha = popup_alpha_target;
		
	}
	public boolean getPopupOpenness() {
		return popup_open;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}