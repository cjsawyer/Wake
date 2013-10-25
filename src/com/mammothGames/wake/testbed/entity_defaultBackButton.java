package com.mammothGames.wake.testbed;

import com.mammothGames.wake.gameEngine.*;


public class entity_defaultBackButton extends engine_entity {

	@Override
	public void backButton() {
		ref.android.finish();
	}
}
