package com.Project1;

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
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new MyView(this));
    }
    
    //--------------------------------------
    //	MyView
    //--------------------------------------
    class MyView extends View {
    	int width, height;
    	int x, y;
    	int sx, sy;
    	int rw, rh;
    	
    	int counter = 0;
    	int n;
    	Bitmap rabbits[] = new Bitmap[2];
    	
    	public MyView(Context context) {
    		super(context);
    		Display display = ((WindowManager)context.getSystemService(
    				Context.WINDOW_SERVICE)).getDefaultDisplay();
    		width = display.getWidth();
    		height = display.getHeight();

    		int count = 0;
    		rabbits[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit_1);
    		rabbits[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit_2);
    		rw = rabbits[0].getWidth() / 2;
    		rh = rabbits[0].getHeight() / 2;
    		
    		x = 100;
    		y = 100;
    		sx = 3;
    		sy = 3;
    		
    		mHandler.sendEmptyMessageDelayed(0, 10);
    	}
    	
    	public void onDraw(Canvas canvas) {
    		
    		x += sx;
    		y += sy;
    		
    		counter++;
    		n = counter % 20 / 10;
    		
    		if (x < rw) {
    			x = rw;
    			sx = -sx;
    		}
    		if (x > width - rw) {
    			x = width - rw;
    			sx = -sx;
    		}
    		if (y < rh) {
    			y = rh;
    			sy = -sy;
    		}
    		if (y > height - rh) {
    			y = height - rh;
    			sy = -sy;
    		}
    		
    		canvas.drawBitmap(rabbits[n], x - rw, y - rh, null);
    		
    	}
    	
    	float x1, y1, x2, y2;
    	@Override
    	public boolean onTouchEvent(MotionEvent event) {
    		
    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
    			x1 = event.getX();
    			y1 = event.getY();
    		}
    		
    		if (event.getAction() == MotionEvent.ACTION_UP) {
    			x2 = event.getX();
    			y2 = event.getY();
    			sx = (int) ((x2 - x1) / 10);
    			sy = (int) ((y2 - y1) / 10);
    		}
    		return true;
    	}
    	
    	Handler mHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			invalidate();
    			mHandler.sendEmptyMessageDelayed(0, 10);
    		}
    	};
    }
}