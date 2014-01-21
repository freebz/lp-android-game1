package com.Project7;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        setContentView(new MyView(this));
    }
    
    class MyView extends View {
    	int cx, cy, cw;
    	int pw, ph;
    	private Bitmap clock;
    	private Bitmap pins[] = new Bitmap[3];
    	
    	int hour, min, sec;
    	float rHour, rMin, rSec;
    	
    	public MyView(Context context) {
    		super(context);
    		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    		Display display = manager.getDefaultDisplay();
    		
    		cx = display.getWidth() / 2;
    		cy = (display.getHeight() - 100) / 2;
    		
    		Resources res = context.getResources();
    		clock = BitmapFactory.decodeResource(res, R.drawable.dial);
    		
    		pins[0] = BitmapFactory.decodeResource(res, R.drawable.pin_1);
    		pins[1] = BitmapFactory.decodeResource(res, R.drawable.pin_2);
    		pins[2] = BitmapFactory.decodeResource(res, R.drawable.pin_3);
    		
    		cw = clock.getWidth() / 2;
    		pw = pins[0].getWidth() / 2;
    		ph = pins[0].getHeight() - 10;
    		
    		mHandler.sendEmptyMessageDelayed(0, 15);
    	}
    	
    	public void onDraw(Canvas canvas) {
    		CalcTime();
    		canvas.drawColor(Color.WHITE);
    		
    		canvas.drawBitmap(clock, cx - cw, cy - cw, null);
    		canvas.rotate(rHour, cx, cy);
    		canvas.drawBitmap(pins[2], cx - pw, cy - ph, null);
    		
    		canvas.rotate(rMin - rHour, cx, cy);
    		canvas.drawBitmap(pins[1], cx - pw, cy - ph, null);
    		
    		canvas.rotate(rSec - rMin, cx, cy);
    		canvas.drawBitmap(pins[0], cx - pw, cy - ph, null);
    		canvas.rotate(-rSec, cx, cy);
    		
    		Paint paint = new Paint();
    		paint.setColor(Color.BLACK);
    		paint.setTextSize(24);
    		canvas.drawText(String.format("%2d : %d", hour, min, sec), cx - 40, cy + cw + 50, paint);
    	}
    	
    	public void CalcTime() {
    		GregorianCalendar calendar = new GregorianCalendar();
    		hour = calendar.get(Calendar.HOUR);
    		min = calendar.get(Calendar.MINUTE);
    		sec = calendar.get(Calendar.SECOND);
    		
    		rSec = sec * 6;
    		rMin = min * 6 + rSec / 60;
    		rHour = hour * 30 + rMin / 12;
    	}
    	
    	Handler mHandler = new Handler() {
    		public void handleMessage(Message msg) {
    			invalidate();
    			mHandler.sendEmptyMessageDelayed(0, 500);
    		}
    	};
    }
}