package com.opengles.book.framework;

import com.opengles.book.framework.Graphics.PixmapFormat;

 

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
