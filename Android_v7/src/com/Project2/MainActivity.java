package com.Project2;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
    
    class Bullet {
    	public int x, y;
    	public Bitmap hole;
    	public int bw, bh;
    	public long lastTime;
    	private int alpha = 255;
    	
    	public Bullet(int _x, int _y) {
    		x = _x;
    		y = _y;
    		
    		hole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
    		bw = hole.getWidth() / 2;
    		bh = hole.getHeight() / 2;
    		
    		lastTime = System.currentTimeMillis();
    	}
    	
    	public boolean MeltHole() {
    		alpha -= 15;
    		if (alpha <= 0)
    			return true;
    		else
    			return false;
    	}
    }
    
    class MyView extends View {
    	int width, height;
    	int cx, cy;
    	
    	int tw, th;
    	ArrayList<Bullet> mBullet;
    	Bitmap imgBack, imgTarget;
    	
    	int Score[] = {10, 8, 6, 0};
    	int n = 3;
    	int tot = 0;
    	
    	public MyView(Context context) {
    		super(context);
    		
    		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    		Display display = window.getDefaultDisplay();
    		width = display.getWidth();
    		height = display.getHeight();
    		
    		cx = width / 2;
    		cy = height / 2 - 30;
    		
    		imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
    		imgBack = Bitmap.createScaledBitmap(imgBack, width, height, true);
    		
    		imgTarget = BitmapFactory.decodeResource(context.getResources(), R.drawable.target_1);
    		imgTarget = Bitmap.createScaledBitmap(imgTarget, 280, 280, true);
    		
    		tw = imgTarget.getWidth() / 2;
    		th = imgTarget.getHeight() / 2;
    		
    		mBullet = new ArrayList<Bullet>();
    		mHandler.sendEmptyMessageDelayed(0, 10);
    	}
    	
    	private void MeltHoles() {
    		long thisTime = System.currentTimeMillis();
    		for (int i = mBullet.size() - 1; i >= 0; i--) {
    			if (thisTime - mBullet.get(i).lastTime >= 2000) {
    				if (mBullet.get(i).MeltHole() == true)
    					mBullet.remove(i);
    			}
    		}
    	}
    	
    	public void onDraw(Canvas canvas) {
    		Paint paint = new Paint();
    		paint.setColor(Color.WHITE);
    		paint.setTextSize(18);
    		
    		canvas.drawBitmap(imgBack, 0, 0, null);
    		canvas.drawText("점수 = " + Score[n], 10, 30, paint);
    		canvas.drawText("합계 = " + tot, 200, 30, paint);
    		canvas.drawBitmap(imgTarget, cx - tw, cy - th, null);
    		
    		MeltHoles();
    		for (Bullet tBullet : mBullet) {
    			paint.setAlpha(tBullet.alpha);
    			canvas.drawBitmap(tBullet.hole, tBullet.x - tBullet.bw, tBullet.y - tBullet.bh, paint);
    		}
    		
    		paint.setAlpha(255);
    	}
    	
    	Handler mHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			invalidate();
    			mHandler.sendEmptyMessageDelayed(0,  10);
    		}
    	};
    	
    	@Override
    	public boolean onTouchEvent(MotionEvent event) {
    		int r[] = {40, 90, 140};
    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
    			int x = (int) event.getX();
    			int y = (int) event.getY();
    			
    			n = 3;
    			
    			for (int i = 0; i < 3; i++) {
    				if (Math.pow(cx - x, 2) + Math.pow(cy - y, 2)
    						<= Math.pow(r[i], 2)) {
    					mBullet.add(new Bullet(x, y));
    					n = i;
    					tot += Score[n];
    					break;
    				}
    			}
    		}
    		return true;
    	}
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	System.exit(0);
    	return true;
    }
}