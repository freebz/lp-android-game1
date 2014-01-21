package com.Project12;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
    
    class Arrow {
    	public int x, y;
    	public Bitmap dart;
    	public int dh;
    	
    	public Arrow(int _x, int _y) {
    		dart = BitmapFactory.decodeResource(getResources(), R.drawable.dart);
    		dh = dart.getHeight();
    		x = _x;
    		y = _y;
    	}
    }
    
    public class MyView extends View {
    	int width, height;
    	int cx, cy;
    	int tw, th;
    	ArrayList<Arrow> mArrow;
    	Bitmap imgBack, imgTarget;
    	
    	int arScore[] = {10, 6, 12, 4, 15, 8, 10, 6, 12, 4, 15, 8, 10};
    	int score = 0;
    	int tot = 0;
    	
    	public MyView(Context context) {
    		super(context);
    		
    		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    		Display display = window.getDefaultDisplay();
    		
    		width = display.getWidth();
    		height = display.getHeight();
    		
    		cx = width / 2;
    		cy = height / 2 - 30;
    		
    		imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
    		imgBack = Bitmap.createScaledBitmap(imgBack, width, height, true);
    		
    		imgTarget = BitmapFactory.decodeResource(getResources(), R.drawable.target_2);
    		imgTarget = Bitmap.createScaledBitmap(imgTarget, 280, 280, true);
    		
    		tw = imgTarget.getWidth() / 2;
    		th = imgTarget.getHeight() / 2;
    		
    		mArrow = new ArrayList<Arrow>();
    	}
    	
    	public void onDraw(Canvas canvas) {
    		Paint paint = new Paint();
    		paint.setColor(Color.WHITE);
    		paint.setTextSize(18);
    		canvas.drawBitmap(imgBack, 0, 0, null);
    		
    		canvas.drawText("점수 = " + score, 10, 30, paint);
    		//canvas.drawText("각도 = " + deg, 200, 30, paint);
    		canvas.drawText("합계 = " + tot, 200, 30, paint);
    		canvas.drawBitmap(imgTarget, cx - tw, cy - th, null);
    		for (Arrow tDart : mArrow) {
    			canvas.drawBitmap(tDart.dart, tDart.x, tDart.y - tDart.dh, null);
    		}
    	}
    	
    	@Override
    	public boolean onTouchEvent(MotionEvent event) {
    		if (event.getAction() == MotionEvent.ACTION_DOWN) {
    			int x = (int) event.getX();
    			int y = (int) event.getY();
    			CalcScore(x, y);
    			invalidate();
    		}
    		return true;
    	}
    	
    	private void CalcScore(int x, int y) {
    		int r[] = {40, 90, 140};
    		double deg = Math.atan2(x - cx, y - cy) * 180 / Math.PI - 90;
    		if (deg < 0) deg += 360;
    		
    		int n = 3;
    		score = 0;
    		
    		for (int i = 0; i < 3; i++) {
    			if (Math.pow(cx - x, 2) + Math.pow(cy - y, 2) <= Math.pow(r[i], 2)) {
    				mArrow.add(new Arrow(x, y));
    				for (int j = 0; j < 13; j++) {
    					int k = j * 30 + 15;
    					if (deg < k) {
    						score = arScore[j] * n;
    						tot += score;
    						break;
    					}
    				}
    			}
    			n--;
    			if (score > 0) break;
    		}
    	}
    }
}