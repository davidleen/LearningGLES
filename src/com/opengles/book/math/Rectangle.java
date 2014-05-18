package com.opengles.book.math;

public class Rectangle {
    public final Vector2 lowerLeft;
    public float width, height;
    
    public Rectangle(float x, float y, float width, float height) {
        this.lowerLeft = new Vector2(x,y);
        this.width = width;
        this.height = height;
    }
    
    public boolean  include(int x, int y)
    {
    	
    	return x>lowerLeft.x&&x<lowerLeft.x+width&&y>lowerLeft.y&&y<lowerLeft.y+height;
    	
    }
}
