package com.opengles.book.framework.gl;

import android.util.Log;

public class FPSCounter {
    long startTime = System.nanoTime();
    int frames = 0;
    int fps=Integer.MAX_VALUE;
    public void logFrame() {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
             Log.d("FPSCounter", "fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
    public long getFps()
    {
        return fps;
    }
    public boolean countFrame()
    {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
           fps=frames;
            frames = 0;
            startTime = System.nanoTime();
            return true;
        }
        return false;

    }
}
