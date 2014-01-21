package com.Project1;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
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
    
    class MyBubble {
    	public int x, y, rad;
    	public Bitmap imgBbl;
    	
    	public boolean dead = false;
    	private int count = 0;
    	private int sx, sy;
    	private int width, height;
    	
    	public MyBubble(int _x, int _y, int _width, int _height) {
    		x = _x;
    		y = _y;
    		width = _width;
    		height = _height;
    		
    		Random rnd = new Random();
    		rad = rnd.nextInt(31) + 10;
    		
    		int k = rnd.nextInt(2) == 0 ? -1 : 1;
    		sx = (rnd.nextInt(4) + 2) * k;
    		sy = (rnd.nextInt(4) + 2) * k;
    		
    		imgBbl = BitmapFactory.decodeResource(getResources(), R.drawable.bubble);
    		imgBbl = Bitmap.createScaledBitmap(imgBbl, rad * 2, rad * 2, false);
    		MoveBubble();
    	}
    	
    	private void MoveBubble() {
    		x += sx;
    		y += sy;
    		if (x <= rad || x >= width - rad) {
    			sx = -sx;
    			count++;
    		}
    		if (y <= rad || y >= height - rad) {
    			sy = -sy;
    			count++;
    		}
    		if (count >= 3) dead = true;
    	}
    }
    
    class MyView extends View {
    	int width, height;
    	Bitmap imgBack;
    	ArrayList<MyBubble> mBubble;
    	
    	public MyView(Context context) {
    		super(context);
    		WindowManager window = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    		Display display = window.getDefaultDisplay();
    		width = display.getWidth();
    		height = display.getHeight();
    		
    		imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
    		imgBack = Bitmap.createScaledBitmap(imgBack, width, height, false);
    		
    		mBubble = new ArrayList<MyBubble>();
    		mHandler.sendEmptyMessageDelayed(0, 10);
    	}
    	
    	private void MoveBubble() {
    		for (int i = mBubble.size() - 1; i >= 0; i--) {
    			mBubble.get(i).MoveBubble();
    			if (mBubble.get(i).dead == true)
    				mBubble.remove(i);
    		}
    	}
    	
    	private void CheckBubble(int x, int y) {
    		boolean flag = false;
    		for (MyBubble tmp : mBubble) {
    			if (Math.pow(tmp.x - x, 2) + Math.pow(tmp.y - y, 2)
    					<= Math.pow(tmp.rad, 2)) {
    				tmp.dead = true;
    				flag = true;
    			}
    		}
    		if (flag == false)
    			mBubble.add(new MyBubble(x, y, width, height));
    	}
    	
    	public void onDraw(Canvas canvas) {
    		MoveBubble();
    		canvas.drawBitmap(imgBack, 0, 0, null);
    		for (MyBubble tmp : mBubble) {
    			canvas.drawBitmap(tmp.imgBbl, tmp.x - tmp.rad, tmp.y - tmp.rad, null);
    		}
    	}
    	
    	Handler mHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			invalidate();
    			mHandler.sendEmptyMessageDelayed(0, 10);
    		}
    	};
    	
    	@Override
    	public boolean onTouchEvent(MotionEvent event) {
    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
    			int x = (int) event.getX();
    			int y = (int) event.getY();
    			CheckBubble(x, y);
    		}
    		return true;
    	}
    }
}