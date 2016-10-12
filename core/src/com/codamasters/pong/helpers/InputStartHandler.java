package com.codamasters.pong.helpers;

import com.badlogic.gdx.InputProcessor;
import com.codamasters.pong.Pong;
import com.codamasters.pong.screens.MainMenu;
import com.codamasters.pong.screens.onlineStartScreen;

public class InputStartHandler implements InputProcessor{
	 private onlineStartScreen screen;
	 private Pong game;


	 public InputStartHandler(final Pong g, onlineStartScreen screen, float scaleFactorX, float scaleFactorY){
	     this.screen = screen;
	     game = g;
	 }
	 
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		if(screen.isEnded()){
			game.setScreen( new MainMenu(game));
		}
		
		return true;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return true;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
