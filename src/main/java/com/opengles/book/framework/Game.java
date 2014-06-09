package com.opengles.book.framework;

import android.content.Context;

public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    
    public Context getContext();
    

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
}