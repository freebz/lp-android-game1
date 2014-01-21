package com.Project3;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bubble {

	public int x, y, rad;
	public Bitmap imgBall;
	public boolean dead = false;
	
	private int _rad;
	private int sx, sy;
	private int width, height;
	private Bitmap Bubbles[] = new Bitmap[6];
	private int imgNum = 0;
	private int loop = 0;

	
	public Bubble(Context context, int _x, int _y, int _width, int _height) {
		width = _width;
		height = _height;
		x = _x;
		y = _y;
		
		imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble_1);
		
		Random rnd = new Random();
		_rad = rnd.nextInt(11) + 20;
		rad = _rad;
		
		for (int i = 0; i <= 3; i++)
			Bubbles[i] = Bitmap.createScaledBitmap(imgBall, _rad * 2 + i * 2, _rad * 2 + i * 2, false);
		Bubbles[4] = Bubbles[2];
		Bubbles[5] = Bubbles[1];
		imgBall = Bubbles[0];
		
		sx = 2;
		sy = rnd.nextInt(2) == 0 ? -2 : 2;
		MoveBubble();
	}
	
	public void MoveBubble() {
		loop++;
		if (loop % 3 == 0) {
			imgNum++;
			if (imgNum > 5) imgNum = 0;
			imgBall = Bubbles[imgNum];
			
			rad = _rad + (imgNum <= 3 ? imgNum : 6 - imgNum ) * 2;
		}
		
		x += sx;
		y += sy;
		
		if (x >= width - rad) {
			x = - rad;
		}
		if (y <= rad || y >= 200) {
			sy = -sy;
			y += sy;
		}
	}
}



class WaterBall {
	public int x, y, rad;
	public boolean dead = false;
	public Bitmap imgBall;
	
	private int width, height;
	private int speed;
	
	public WaterBall(Context context, int _x, int _y, int _width, int _height) {
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		imgBall = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.w0);
		rad = imgBall.getWidth() / 2;
		speed = 8;
		MoveBall();
	}
	
	public void MoveBall() {
		y -= speed;
		if (y < 0) dead = true;
	}
}
