package com.reywas.testGameName.gameEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

public class engine_file {
	
	engine_reference ref;
	public engine_file(engine_reference ref) {
		this.ref = ref;
	}

	private int i;
	private byte[] encrypt(byte[] bytes) {
		for(i=0; i<bytes.length; i++) {
			bytes[i] ^= (byte)10101101;
		}
		return bytes;
	}
	public void save(String file_name, String string_to_save) {
//		file_name = "test";
		
		try {
			FileOutputStream fos = ref.android.openFileOutput(file_name, ref.android.MODE_PRIVATE);
			
			byte[] to_write, bytes = new byte[1024];
			to_write = string_to_save.getBytes();
			for(i=0; i<to_write.length; i++) {
				bytes[i] = to_write[i];
			}
			
			fos.write(encrypt(bytes));
//			fos.write(encrypt(string_to_save.getBytes()));
//			fos.write(string_to_save.getBytes());
			
			
			fos.close();
		} catch (IOException e) {
			Log.e("reywas", "error saving!");
		}
	}
	
	private String loaded_string;
	
	public String load(String file_name) {
		loaded_string = "";
		try {
			FileInputStream fis;
			try {
				fis = ref.android.openFileInput(file_name);
			
				StringBuffer fileContent = new StringBuffer("");
		
				byte[] buffer = new byte[1024];
		
				while ((fis.read(buffer)) != -1) {
					buffer = encrypt(buffer);
					fileContent.append(new String(buffer).trim());
				}
				
				loaded_string = fileContent.toString();
				
				fis.close();
			} catch(FileNotFoundException e) {
				Log.e("reywas", "Failed to find '" + file_name + "' to load");
			}
		} catch ( IOException ioe ) {
        	Log.e("reywas", "error loading!");
            ioe.printStackTrace() ;
        }
		return loaded_string;
	}
//	http://stackoverflow.com/questions/9095610/android-fileinputstream-read-txt-file-to-string
//	http://www.ehow.com/how_2266672_use-bit-shifting-cryptography.html
}
