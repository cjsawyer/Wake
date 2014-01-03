package com.mammothGames.wake.gameTESTS;

import com.mammothGames.wake.gameEngine.*;


public class entity_defaultBackButton extends engine_entity {

	@Override
	public void backButton() {
		ref.android.finish();
	}
}
