package com.Project8;

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
    
    class MyView extends View {
    	int width, height;
    	int cx, cy;
    	int tw, th;
    	int sw, sh;
    	int ang, dir;
    	int an1, an2;
    	
    	Bitmap imgBack, imgToy, imgShadow;
    	
    	public MyView(Context context) {
    		super(context);
    		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    		Display display = window.getDefaultDisplay();
    		
    		width = display.getWidth();
    		height = display.getHeight();
    		
    		cx = width / 2;
    		cy = height / 2 + 100;
    		
    		imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.back);
    		imgBack = Bitmap.createScaledBitmap(imgBack, width, height, true);
    		
    		imgToy = BitmapFactory.decodeResource(context.getResources(), R.drawable.toy);
    		imgShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
    		
    		tw = imgToy.getWidth() / 2;
    		th = imgToy.getHeight();
    		sw = imgShadow.getWidth() / 2;
    		sh = imgShadow.getHeight() / 2;
    		
    		ang = 0;
    		dir = 0;
    		mHandler.sendEmptyMessageDelayed(0, 10);
    	}
    	
    	public void onDraw(Canvas canvas) {
    		RotateToy();
    		
    		canvas.drawBitmap(imgBack, 0, 0, null);
    		canvas.drawBitmap(imgShadow, cx - sw, cy - sh, null);
    		
    		canvas.rotate(ang, cx, cy);
    		canvas.drawBitmap(imgToy, cx - tw, cy - th, null);
    		canvas.rotate(-ang, cx, cy);
    	}
    	
    	private void RotateToy() {
    		ang += dir;
    		if (ang <= an1 || ang >= an2) {
    			an1 ++;
    			an2 --;
    			dir = -dir;
    			ang += dir;
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
    			an1 = -15;
    			an2 = 15;
    			if (dir == 0)
    				dir = -1;
    		}
    		return true;
    	}
    }
}