package ar.com.federicocristina.bolas.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public abstract class Sprite {

	public PointF position = new PointF(100, 100);
	public PointF vector = new PointF(5, 5);
	public Color color = new Color();
	public Paint paint = new Paint();
	public boolean moving = true;
	
	public abstract void update();
	public abstract void draw(Canvas canvas);
}
