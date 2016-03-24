package com.example.mardiak.marek.hrapp.myOpenGl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.mardiak.marek.hrapp.activities.GLActivity;

/**
 * Created by mm on 3/23/2016.
 */
public class MyGlSurfaceView extends GLSurfaceView {
    /*TextView tv;*/
    GLActivity maContext; //mm context of Activity that instantiated this GLSurfaceview

    public MyGlSurfaceView(Context context)
    {
        super(context);
        maContext=(GLActivity) context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        final float x=e.getX();
        final float y=e.getY();
        Log.i("GL Surface View", "X=" + x + " Y=" + y);
        maContext.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                maContext.setTitle("X="+x+" Y="+y);
            }
        });
        return false;
    }
}
