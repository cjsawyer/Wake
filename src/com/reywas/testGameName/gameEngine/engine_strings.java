package com.reywas.testGameName.gameEngine;

import android.util.Log;

import com.reywas.testGameName.gameEngine.*;

public class engine_strings {

	engine_reference ref;
	
	public engine_strings(engine_reference r){
		ref = r;
		builder = new StringBuilder(500);
	}
	
	public char[] stringChars = new char[500];
	public StringBuilder builder = new StringBuilder(500);
	
}
