package com.Project6;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        setContentView(new MyView(this));
    }
    
    class MyView extends View {
    	
    	public MyView(Context context) {
    		super(context);
    	}
    	
    	public void onDraw(Canvas canvas) {
    		int cx = getWidth() / 2;
    		int cy = getHeight() / 2;
    		int w = 0;
    		int h = 0;
    		
    		int DIRECTION = 1;
    		
    		
    		Bitmap rose[] = new Bitmap[4];
    		Resources res = getResources();
    		
    		rose[0] = BitmapFactory.decodeResource(res, R.drawable.rose_1);
    		rose[1] = BitmapFactory.decodeResource(res, R.drawable.rose_2);
    		rose[2] = BitmapFactory.decodeResource(res, R.drawable.rose_3);
    		rose[3] = BitmapFactory.decodeResource(res, R.drawable.rose_4);
    		
    		Paint paint = new Paint();
    		paint.setColor(Color.RED);
    		paint.setStyle(Paint.Style.STROKE);
    		
    		canvas.drawColor(Color.WHITE);
    		switch (DIRECTION) {
    		case 1:
    			w = rose[0].getWidth() / 2;
    			h = rose[0].getHeight();
    			break;
    		case 2:
    			w = 0;
    			h = rose[1].getHeight() / 2;
    			break;
    		case 3:
    			w = rose[2].getWidth() / 2;
    			h = 0;
    		case 4:
    			w = rose[3].getWidth();
    			h = rose[3].getHeight() / 2;
    			break;
    		}
    		
    		getWindow().setTitle("Àå¹Ì²É È¸Àü " + DIRECTION);
    		
    		canvas.drawBitmap(rose[DIRECTION - 1], 10, 10, null);
    		
    		
    		canvas.drawCircle(w + 10, h + 10, 10, paint);
    		
    		for (int i = 1; i <= 18; i++) {
    			canvas.rotate(20, cx, cy);
    			canvas.drawBitmap(rose[DIRECTION - 1], cx - w, cy - h, null);
    		}
    	}
    }
}