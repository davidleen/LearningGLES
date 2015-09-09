package com.giants3.android.openglesframework.framework;

import com.giants3.android.openglesframework.framework.Graphics.PixmapFormat;

 

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
