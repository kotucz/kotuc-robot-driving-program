package cz.kotuc.robot.driver.bt;

import android.*;
import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by IntelliJ IDEA.
 * User: Kotuc
 * Date: 19.3.12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class JoyStickView extends View {

    float startX;
    float startY;

    float setX;
    float setY;

    int pointerId;

    float val = 0;

    int sizeHalf = 20;

    private float py;
    private float px;
    private final Drawable drawable;

    public JoyStickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawable = context.getResources().getDrawable(R.drawable.btn_default);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("joystick", "event");
        // p.setColor(0xffAC7423);
        int[] xy0 = new int[2];
        getLocationOnScreen(xy0);
        px = event.getRawX() - xy0[0];
        py = event.getRawY() - xy0[1];

//        Log.e("JS", "px " + px + " py " + py + " idx " + event.getActionIndex());


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                this.pointerId = event.getPointerId(event.getActionIndex());
                startX = px;
                startY = py;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                this.pointerId = event.getPointerId(event.getActionIndex());
                setX = 0;
                setY = 0;
                startX = 0;
                startY = 0;
                val = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                setX = px - startX;
                setY = py - startY;
                val = -(py - (getHeight()/2))/(getHeight()/2 - sizeHalf);
                break;
            default:

        }

//        Log.e("JS", "id" + pointerId + "sx " + startX + " sy " + startY + "x " + setX + " y " + setY);

        invalidate();
        return true; // true if consumed event
    }

    float getValue() {
        return val;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(0xFFFF0000);
        {
            RectF rect = new RectF(startX, startY, startX + setX, startY + setY);
            canvas.drawRect(rect, paint);
        }
        int width = getWidth();
        int height = getHeight();

        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xFF00FF00);
            RectF rect = new RectF(0, 0, width - 1, height - 1);
            canvas.drawRect(rect, paint);
        }
        {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xFFFFFF00);
            float pos = getHeight()/2 - (val*(getHeight()/2-sizeHalf));
            RectF rect = new RectF(0, pos-sizeHalf, width - 1, pos+sizeHalf);

            drawable.setBounds((int)rect.left,(int)rect.top,(int)rect.right,(int)rect.bottom);
            drawable.draw(canvas);

            canvas.drawRect(rect, paint);
        }

    }
}
