package com.opengles.book.galaxy;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;


import com.giants3.android.openglesframework.framework.Game;
import com.giants3.android.openglesframework.framework.Screen;
import com.giants3.android.openglesframework.framework.impl.GLGame;
import com.opengles.book.screen.MainScreen;

public class GalaxyGame extends GLGame {
	
	public static final String PARAMS_SCREEN_NAME="PARAMS_SCREEN_NAME";

	@Override
	public Screen getStartScreen() {
		
		String screenName=getIntent().getStringExtra(PARAMS_SCREEN_NAME);
		 
 
	//	return   new GalaxyScreen(this) ;
        //return new TuziScreen(this)  ;
			//return new TreeOnDesertScreen(this);
		//return new GrayMapScreen(this);
		//
			//  return new Reflect_BasketBall_Screen(this);
		  //  return new UnRealScreen(this);
		try {
			return 	(Screen) Class.forName(screenName).getConstructor(Game.class).newInstance(this);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return new MainScreen(this);
	}

	@Override
	public Context getContext() {

		return this;
	}




}
