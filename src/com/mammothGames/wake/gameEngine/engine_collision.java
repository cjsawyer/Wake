package com.mammothGames.wake.gameEngine;

public class engine_collision {
	
	private float box_left, box_right, box_top, box_bottom;
	/**
	 * Returns true if the point is inside of the Axis-Aligned-Bounding-Box.
	 * The box is defined by the width/height provided, with the x/y coordinate in the center.
	 * @param box_width
	 * @param box_height
	 * @param box_x
	 * @param box_y
	 * @param point_x
	 * @param point_y
	 * @return
	 */
	public boolean point_AABB(float box_width,float box_height, float box_x, float box_y, float point_x, float point_y) {
		
		box_left = box_x - box_width/2;
		box_right = box_x + box_width/2;
		box_top = box_y + box_height/2;
		box_bottom = box_y - box_height/2;
		
//		ref.draw.setDrawColor(0.5f, 0, 0, 0.5f);
//		ref.draw.drawRectangle(box_x, box_y, box_width, box_height, 0, 0, 0, 1000);
		
		if ((point_x>=box_left)&&(point_x<=box_right)&&(point_y<=box_top)&&(point_y>=box_bottom))
			return true;
		
		return false;
	}
	
	public boolean point_circle(float point_x,float point_y, float circle_x, float circle_y, float radius) {
		
		if (
				(
						((point_x - circle_x)*(point_x - circle_x))
						+
						((point_y - circle_y)*(point_y - circle_y))
				)
				<=
				(radius*radius)
			)
			return true;
		
		return false;
	}
	
	private engine_reference ref;
	public engine_collision(engine_reference r) {
		ref = r;
	}
}
