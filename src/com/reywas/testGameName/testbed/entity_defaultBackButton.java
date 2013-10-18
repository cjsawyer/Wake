package com.reywas.testGameName.testbed;

import com.reywas.testGameName.gameEngine.*;


public class entity_defaultBackButton extends engine_entity {

	@Override
	public void backButton() {
		ref.android.finish();
	}
}
