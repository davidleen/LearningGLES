package com.opengles.book.galaxy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.opengles.book.MatrixState;
import com.opengles.book.framework.Screen;
import com.opengles.book.framework.impl.GLGame;
import com.opengles.book.screen.GalaxyScreen;
import com.opengles.book.screen.GrayMapScreen;
import com.opengles.book.screen.TreeOnDesertScreen;

public class GalaxyGame extends GLGame {

	@Override
	public Screen getStartScreen() {
 
	//	return   new GalaxyScreen(this) ;
        //return new TuziScreen(this)  ;
	//	return new TreeOnDesertScreen(this);
		return new GrayMapScreen(this);
	}

	@Override
	public Context getContext() {

		return this;
	}




}
