package com.mammothGames.wake1.gameTESTS;

import com.mammothGames.wake1.gameEngine.*;


public class entity_defaultBackButton extends engine_entity {

	@Override
	public void backButton() {
		ref.android.finish();
	}
}
