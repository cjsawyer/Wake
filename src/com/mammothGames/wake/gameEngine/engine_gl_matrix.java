package com.mammothGames.wake.gameEngine;

import android.opengl.Matrix;
import android.util.FloatMath;

public class engine_gl_matrix {
	
	engine_reference ref;
	public engine_gl_matrix(engine_reference ref) {
		this.ref = ref;
	}
	private float[] r = new float[32];
	
	private void setRotateM(float[] rm, int rmOffset, float a, float x, float y, float z) {

		rm[rmOffset + 3] = 0;
		rm[rmOffset + 7] = 0;
		rm[rmOffset + 11] = 0;
		rm[rmOffset + 12] = 0;
		rm[rmOffset + 13] = 0;
		rm[rmOffset + 14] = 0;
		rm[rmOffset + 15] = 1;

		a *= (float) (Math.PI / 180.0f);
		float s = FloatMath.sin(a);
		float c = FloatMath.cos(a);
		if (1.0f == x && 0.0f == y && 0.0f == z) {
			rm[rmOffset + 5] = c;
			rm[rmOffset + 10] = c;
			rm[rmOffset + 6] = s;
			rm[rmOffset + 9] = -s;
			rm[rmOffset + 1] = 0;
			rm[rmOffset + 2] = 0;
			rm[rmOffset + 4] = 0;
			rm[rmOffset + 8] = 0;
			rm[rmOffset + 0] = 1;
		} else if (0.0f == x && 1.0f == y && 0.0f == z) {
			rm[rmOffset + 0] = c;
			rm[rmOffset + 10] = c;
			rm[rmOffset + 8] = s;
			rm[rmOffset + 2] = -s;
			rm[rmOffset + 1] = 0;
			rm[rmOffset + 4] = 0;
			rm[rmOffset + 6] = 0;
			rm[rmOffset + 9] = 0;
			rm[rmOffset + 5] = 1;
		} else if (0.0f == x && 0.0f == y && 1.0f == z) {
			rm[rmOffset + 0] = c;
			rm[rmOffset + 5] = c;
			rm[rmOffset + 1] = s;
			rm[rmOffset + 4] = -s;
			rm[rmOffset + 2] = 0;
			rm[rmOffset + 6] = 0;
			rm[rmOffset + 8] = 0;
			rm[rmOffset + 9] = 0;
			rm[rmOffset + 10] = 1;
		} else {
			float len = length(x, y, z);
			if (1.0f != len) {
				float recipLen = 1.0f / len;
				x *= recipLen;
				y *= recipLen;
				z *= recipLen;
			}
			float nc = 1.0f - c;
			float xy = x * y;
			float yz = y * z;
			float zx = z * x;
			float xs = x * s;
			float ys = y * s;
			float zs = z * s;
			rm[rmOffset + 0] = x * x * nc + c;
			rm[rmOffset + 4] = xy * nc - zs;
			rm[rmOffset + 8] = zx * nc + ys;
			rm[rmOffset + 1] = xy * nc + zs;
			rm[rmOffset + 5] = y * y * nc + c;
			rm[rmOffset + 9] = yz * nc - xs;
			rm[rmOffset + 2] = zx * nc - ys;
			rm[rmOffset + 6] = yz * nc + xs;
			rm[rmOffset + 10] = z * z * nc + c;
		}
	}
	
	
	private float length(float x, float y, float z) {
		return FloatMath.sqrt((x * x) + (y * y) + (z * z));
	}
	
	public void rotateM(float[] m, int mOffset, float a, float x, float y, float z) {
        setRotateM(r, 0, a, x, y, z);
        Matrix.multiplyMM(r, 16, m, mOffset, r, 0);
        System.arraycopy(r, 16, m, mOffset, 16);
    }
}
