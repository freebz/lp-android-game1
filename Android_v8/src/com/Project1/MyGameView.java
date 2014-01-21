package com.Project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MyGameView extends SurfaceView implements Callback {

	public GameThread mThread;
	
	public MyGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		mThread = new GameThread(holder, context);
		
		setFocusable(true);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		mThread.start();
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		
		boolean done = true;
		while (done) {
			try {
				mThread.join();
				done = false;
			} catch (InterruptedException e) {
				
			}
		}
	}
	
	class GameThread extends Thread {
		
		SurfaceHolder mHolder;
		
		int width, height;
		int x, y, dw, dh;
		int sx, sy;
		int num;
		int x1, y1;
		Bitmap imgBack;
		Bitmap Dragon[] = new Bitmap[2];
		
		public GameThread(SurfaceHolder holder, Context context) {
			mHolder = holder;
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = windowManager.getDefaultDisplay();
			width = display.getWidth();
			height = display.getHeight() - 50;
			
			imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back_1);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);
			
			Dragon[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dragon);
			dw = Dragon[0].getWidth() / 2;
			dh = Dragon[0].getHeight() / 2;
			
			Matrix matrix = new Matrix();
			matrix.postScale(-1, 1);
			Dragon[1] = Bitmap.createBitmap(Dragon[0], 0, 0, dw * 2, dh * 2, matrix, false);
			
			num = 0;
			sx = -2;
			sy = 3;
			x = 100;
			y = 100;
		}
		
		public void MoveDragon() {
			x += sx;
			y += sy;
			if (x <= dw || x >= width - dw) {
				sx = -sx;
				num = 1 - num;
			}
			if (y <= dh || y >= height - dh) {
				sy = - sy;
			}
		}
		
		public void run() {
			Canvas canvas = null;
			while(true) {
				canvas = mHolder.lockCanvas();
				try {
					synchronized (mHolder) {
						MoveDragon();
						canvas.drawBitmap(imgBack, 0, 0, null);
						canvas.drawBitmap(Dragon[num], x - dw, y - dh, null);
					}
				} finally {
					if (canvas != null)
						mHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mThread.x1 = (int) event.getX();
			mThread.y1 = (int) event.getY();
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			int x2 = (int) event.getX();
			int y2 = (int) event.getY();
			
			mThread.sx = (x2 - mThread.x1) / 10;
			mThread.sy = (y2 - mThread.y1) / 10;
			if (mThread.x1 < x2)
				mThread.num = 1;
			else
				mThread.num = 0;
		}
		return true;
	}

}
