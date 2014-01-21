package com.Project3;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SmallBall {
	public int x, y, rad;
	public boolean dead = false;
	public Bitmap imgBall;
	
	private int width, height;
	private int cx, cy;
	private int cr;
	private double r;
	private int speed;
	private int num;
	private int life;
	
	public SmallBall(Context context, int _x, int _y, int ang, int _width, int _height) {
		cx = _x;
		cy = _y;
		width = _width;
		height = _height;
		r = ang * Math.PI / 180;
		
		Random rnd = new Random();
		speed = rnd.nextInt(5) + 2;
		rad = rnd.nextInt(6) + 2;
		num = rnd.nextInt(6);
		life = rnd.nextInt(31) + 20;
		
		imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.b0 + num);
		imgBall = Bitmap.createScaledBitmap(imgBall, rad * 2, rad * 2, false);
		cr = 10;
		MoveBall();
	}
	
	public void MoveBall() {
		life--;
		cr += speed;
		x = (int) (cx + Math.cos(r) * cr);
		y = (int) (cy - Math.sin(r) * cr);
		if (x < -rad || x > width + rad ||
				y < -rad || y > height + rad || life <= 0)
			dead = true;
	}
}
