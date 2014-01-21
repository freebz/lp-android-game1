package com.Project3;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MyGameView extends SurfaceView implements Callback {
	
	final int LEFT = 1;
	final int RIGHT = 2;
	
	GameThread mThread;
	SurfaceHolder mHolder;
	Context mContext;
	
	public MyGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		mHolder = holder;
		mContext = context;
		mThread = new GameThread(holder, context);
		setFocusable(true);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		try {
			mThread.start();
		} catch (Exception e) {
			RestartGame();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
//		boolean done = true;
//		while (done) {
//			try {
//				mThread.join();
//				done = false;
//			} catch (InterruptedException e) {
//				
//			}
//		}
		StopGame();
	}
	
	public void StopGame() {
		mThread.StopThread();
	}
	
	public void PauseGame() {
		mThread.PauseNResume(true);
	}
	
	public void ResumeGame() {
		mThread.PauseNResume(false);
	}
	
	public void RestartGame() {
		mThread.StopThread();
		
		mThread = null;
		mThread = new GameThread(mHolder, mContext);
		mThread.start();
	}
	
	class GameThread extends Thread {
		SurfaceHolder mHolder;
		Context mContext;
		
		int width, height;
		Bitmap imgBack;
		Bitmap imgSpider;
		int sw, sh;
		int mx, my;
		long lastTime;
		
		int Tot = 0;
		
		ArrayList<Bubble> mBall = new ArrayList<Bubble>();
		ArrayList<SmallBall> sBall = new ArrayList<SmallBall>();
		ArrayList<WaterBall> wBall = new ArrayList<WaterBall>();
		
		boolean canRun = true;
		boolean isWait = false;
		
		public GameThread(SurfaceHolder holder, Context context) {
			mHolder = holder;
			mContext = context;
			
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			width = display.getWidth();
			height = display.getHeight() - 50;
			
			imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
			imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);
			
			
			// 거미 이미지 읽기
			imgSpider = BitmapFactory.decodeResource(getResources(), R.drawable.spider1);
			sw = imgSpider.getWidth() / 2;
			sh = imgSpider.getHeight() / 2;
			
			mx = width / 2;
			my = height - 45;
		}
		
		public void StopThread() {
			canRun = false;
			synchronized (this) {
				this.notify();
			}
		}
		
		public void PauseNResume(boolean wait) {
			isWait = wait;
			synchronized (this) {
				this.notify();
			}
		}
		
		public void MakeBubble() {
			Random rnd = new Random();
			if (mBall.size() > 9 || rnd.nextInt(40) < 38) return;
			int x = -50;
			int y = rnd.nextInt(201) + 50;
			mBall.add(new Bubble(mContext, x, y, width, height));
		}
		
		private void MakeSmallBall(int x, int y) {
			Random rnd = new Random();
			int count = rnd.nextInt(9) + 7;
			for (int i = 1; i <= count; i++) {
				int ang = rnd.nextInt(360);
				sBall.add(new SmallBall(mContext, x, y, ang, width, height));
			}
		}
		
		//public void MoveBubble() {
		public void MoveCharacters() {
			
			// 큰 풍선
			for (int i = mBall.size() - 1; i >= 0; i--) {
				mBall.get(i).MoveBubble();
				if (mBall.get(i).dead) {
					MakeSmallBall(mBall.get(i).x, mBall.get(i).y);
					mBall.remove(i);
				}
			}
			
			// 작은품선
			for (int i = sBall.size() - 1; i >= 0; i--) {
				sBall.get(i).MoveBall();
				if (sBall.get(i).dead == true)
					sBall.remove(i);
			}
			
			// 거미 총알
			for (int i = wBall.size() - 1; i >= 0; i--) {
				wBall.get(i).MoveBall();
				if (wBall.get(i).dead == true)
					wBall.remove(i);
			}
		}
		
		private void MoveSpider(int n) {
			int sx = 4;
			if (n == LEFT) sx = -4;
			mx += sx;
			if (mx < sw) mx = sw;
			if (mx > width - sw) mx = width - sw;
		}
		
		public void MakeWaterBall() {
			long thisTime = System.currentTimeMillis();
			if (thisTime - lastTime >= 300) {
				wBall.add(new WaterBall(mContext, mx, my - 20, width, height));
				lastTime = thisTime;
			}
		}
		
		public void CheckCollision() {
			int x1, y1, x2, y2;
			
			for (WaterBall water : wBall) {
				x1 = water.x;
				y1 = water.y;
				for (Bubble tmp : mBall) {
					x2 = tmp.x;
					y2 = tmp.y;
					if (Math.abs(x1 - x2) < tmp.rad
							&& Math.abs(y1 - y2) < tmp.rad) {
						MakeSmallBall(tmp.x, tmp.y);
						tmp.dead = true;
						water.dead = true;
						break;
					}
				}
			}
		}
		
		public void DrawCharacters(Canvas canvas) {
			canvas.drawBitmap(imgBack, 0, 0, null);
			for (Bubble tmp : mBall)
				canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad, tmp.y - tmp.rad, null);
			for (SmallBall tmp : sBall)
				canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad, tmp.y - tmp.rad, null);
			for (WaterBall tmp : wBall)
				canvas.drawBitmap(tmp.imgBall, tmp.x - tmp.rad, tmp.y - tmp.rad, null);
			canvas.drawBitmap(imgSpider, mx - sw, my - sh, null);
		}
		
		public void run() {
			Canvas canvas = null;
			while (canRun) {
				canvas = mHolder.lockCanvas();
				try {
					synchronized (mHolder) {
						MakeBubble();
						MoveCharacters();
						CheckCollision();
						DrawCharacters(canvas);
					}
				} finally {
					if (canvas != null)
						mHolder.unlockCanvasAndPost(canvas);
				}
				
				synchronized (this) {
					if (isWait)
						try {
							wait();
						} catch (Exception e) {
							
						}
				}
			}
		}
		
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		synchronized (mHolder) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_LEFT:
					mThread.MoveSpider(LEFT);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					mThread.MoveSpider(RIGHT);
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					mThread.MakeWaterBall();
					break;
				}
			}
			return super.onKeyDown(keyCode, event);
		}
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			synchronized (mHolder) {
//				int x = (int) event.getX();
//				int y = (int) event.getY();
//				mThread.MakeBubble(x, y);
//			}
//		}
//		return true;
//	}

}
