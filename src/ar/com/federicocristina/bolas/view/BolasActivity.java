package ar.com.federicocristina.bolas.view;

import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import ar.com.federicocristina.bolas.model.Ball;
import ar.com.federicocristina.bolas.model.Sprite;

public class BolasActivity extends Activity implements OnTouchListener, SensorEventListener {      
	protected FastRenderView renderView;            
    public volatile static Vector<Sprite> sprites = new Vector<Sprite>(); 
    public static Point canvasSize = null; 
    public static float[] acel = {1, 2, 0};
    private Random rand = new Random();
    private Ball lastBall = null;
    private PointF previousPoint = new PointF();
    private int targetBall = 0;
    public static final int MAX_SPRITES = 20;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("starting...");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        renderView = new FastRenderView(this);
        renderView.setOnTouchListener(this);
        setContentView(renderView);
        
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0) {
        	System.exit(1);
        } else {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            if (!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)) {
            	System.exit(1);
            }
        }
        
        // Setear bolas iniciales
        Ball ball1 = new Ball(100, 100, 5, 5, Color.RED, true);
        Ball ball2 = new Ball(150, 150, 6, 4, Color.GREEN, true);
        Ball ball3 = new Ball(200, 200, 4, 6, Color.BLUE, true);
        sprites.add(ball1);
        sprites.add(ball2);
        sprites.add(ball3);
    }      
    
    protected void onResume() {
        super.onResume();
        renderView.resume();
    }
    
    protected void onPause() {
        super.onPause();         
        renderView.pause();
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if (BolasActivity.sprites.size() < MAX_SPRITES)
			{
				lastBall = new Ball(event.getX(), event.getY(), 1, 1, Color.rgb(rand.nextInt(), rand.nextInt(), rand.nextInt()), false);
				sprites.add(lastBall);
			}
			else
			{
				lastBall = (Ball)sprites.get(targetBall++);
				lastBall.moving = false;
				lastBall.position.x = event.getX();
				lastBall.position.y = event.getY();
				if (targetBall >= MAX_SPRITES)
					targetBall = 0;
			}
			previousPoint.x = lastBall.position.x;
			previousPoint.y = lastBall.position.y;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			previousPoint.x = lastBall.position.x;
			previousPoint.y = lastBall.position.y;
			lastBall.position.x = event.getX();
			lastBall.position.y = event.getY();
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			lastBall.vector.x = (event.getX() - previousPoint.x);
			lastBall.vector.y = (event.getY() - previousPoint.y);
			lastBall.moving = true;
		}
        try {
        	// 60 eventos por segundo como maximo
        	Thread.sleep(16);
        } catch (Exception e) {}
		return true;
	}   
 
    @Override
    public void onSensorChanged(SensorEvent event) {
    	acel = event.values;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
}