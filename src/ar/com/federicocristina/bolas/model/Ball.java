package ar.com.federicocristina.bolas.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import ar.com.federicocristina.bolas.view.BolasActivity;

public class Ball extends Sprite {

	/* Proporcion de las bolas con respecto al tamaño del dispositivo */
	public static final int PROPORTION = 15;
	/* Multiplicador para el acelerometro */
	public static final int SPEED_MPY = 1;
	/* Factor aleatorio para cada bola */
	public final float SPEED_RND = 1 + (float) (Math.random() * 5);
	/* Maximo corrimiento entre frame y frame */
	public static final int VECTOR_MAX = 20;
	/* Inercia de las bolas */
	public static final float VECTOR_FLOW = (float) .25;
	/* Tamaño y tamaño al cuadrado relativos de las bolas */
	public int relativeSize = -1;
	public int relativeSize2 = -1;
	/* Nueva posicion de la bola para el siguiente render */
	protected PointF newPos = new PointF();
	
	public Ball()
	{
		paint.setColor(Color.YELLOW);
	}
	
	public Ball(float xPos, float yPos, float xVec, float yVec, int aColor, boolean isAlive)
	{
		this();
		position.x = xPos;
		position.y = yPos;
		vector.x = xVec;
		vector.y = yVec;
		paint.setColor(aColor);
		moving = isAlive;
	}
	
	
	@Override
	public void update() {
		
		// Cargar valores iniciales
		if (relativeSize == -1)
		{
			relativeSize = BolasActivity.canvasSize.y / PROPORTION;
			relativeSize2 = relativeSize * relativeSize * 4;
		}
		
		// Si no se mueve, no calcular nada
		if (!moving)
			return;
		
		// Leer acelerometro y calcular el vector (limitando un valor maximo razonable)
		vector.x = (vector.x / VECTOR_FLOW + BolasActivity.acel[1] * SPEED_MPY * SPEED_RND * VECTOR_FLOW) / 4;
		vector.y = (vector.y / VECTOR_FLOW + BolasActivity.acel[0] * SPEED_MPY * SPEED_RND * VECTOR_FLOW) / 4;
		if (vector.x > VECTOR_MAX) 
			vector.x = VECTOR_MAX;
		if (vector.y > VECTOR_MAX)
			vector.y = VECTOR_MAX;

    	// Calcular la nueva posicion en funcion de la actual mas el vector
		newPos.x = position.x + vector.x;
		newPos.y = position.y + vector.y;

    	// Que no se pase del canvas
    	if (newPos.x + relativeSize < BolasActivity.canvasSize.x && newPos.x - relativeSize > 0 )
        	position.x = newPos.x;
    	else
    		vector.x = 0;
        if (newPos.y + relativeSize < BolasActivity.canvasSize.y && newPos.y - relativeSize > 0 )
        	position.y = newPos.y;
    	else
    		vector.y = 0;
    	
	}

	@Override
	public void draw(Canvas canvas) {

    	// Graficar
    	canvas.drawCircle(position.x, position.y, relativeSize, paint);
    	
	}

}
