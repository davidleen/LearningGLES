package com.opengles.book.framework.impl;

import com.opengles.book.framework.Game;
import com.opengles.book.framework.Screen;

 

public abstract class GLScreen extends Screen {
    protected   GLGraphics glGraphics;
    protected   GLGame glGame;
    protected String TAG=GLScreen.this.getClass().getName();
    public GLScreen(Game game) {
        super(game);
        iniGame(game);
    }

	/**
	 * @param game
	 */
	private void iniGame(Game game) {
		glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
	}

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
