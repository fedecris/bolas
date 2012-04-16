package ar.com.federicocristina.bolas.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ar.com.federicocristina.bolas.model.Sprite;

/**
 * Engine de rendering
 */
public class FastRenderView extends SurfaceView implements Runnable {
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public FastRenderView(Context context) {
        super(context);           
        holder = getHolder();
    }

    public void resume() {          
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }
    
    public void pause() {        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
        renderThread = null;        
    }
    
    public void run() {
        while(running) {
        	
        	// Que cada objeto actualice su modelo
        	if (BolasActivity.canvasSize != null)
        	{	
        		int size = BolasActivity.sprites.size();
        		for (int i=0; i<size; i++)
        			BolasActivity.sprites.get(i).update();
        	}
        	
            if(!holder.getSurface().isValid())
                continue;

            Canvas canvas = holder.lockCanvas();    
            if (BolasActivity.canvasSize == null)
            	BolasActivity.canvasSize = new Point(canvas.getWidth(), canvas.getHeight());
            
            drawSurface(canvas);                                           
            holder.unlockCanvasAndPost(canvas);            
        }
    }        
    
    private void drawSurface(Canvas canvas) {
    	// Limpiar la pantalla
    	canvas.drawRGB(0, 0, 0);

    	// Que cada objeto se auto-dibuje en el canvas
    	if (BolasActivity.canvasSize != null)
    	{	
    		int size = BolasActivity.sprites.size();
    		for (int i=0; i<size; i++)
    			BolasActivity.sprites.get(i).draw(canvas);
    	}

    	    	
    }
    
}
