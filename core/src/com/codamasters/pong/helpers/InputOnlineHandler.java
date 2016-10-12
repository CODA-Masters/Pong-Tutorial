package com.codamasters.pong.helpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.codamasters.pong.Pong;
import com.codamasters.pong.gameobjects.Player;
import com.codamasters.pong.screens.MainMenu;
import com.codamasters.pong.screens.onlineScreen;

public class InputOnlineHandler implements InputProcessor{
	 private Player player, player2;
	 private onlineScreen screen;
	 private Vector3 target;
	 private Pong game;
	 private float maxTop = 3.5f;
	 private float maxBot = -3.5f;

	 public InputOnlineHandler(final Pong g, onlineScreen screen, float scaleFactorX, float scaleFactorY){
	     this.screen = screen;
	     this.target = new Vector3();
	     target.set(-10,0,0);
	     player = screen.getPlayer();
	     player2 = screen.getPlayer2();
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

		if(screen.isScored() != 0 && !screen.isEnded()){
			screen.restartGame();
			game.actionResolver.sendPos(0);
		}
		
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
		OrthographicCamera cam = screen.getCamera();
		cam.unproject(target.set(screenX,screenY,0));
		screen.setCamera(cam);

		// Enviamos nuestra posicion

		float value = target.y;
		if(player.getBody().getPosition().y > maxTop) {
			value = maxTop;
		}else if(player.getBody().getPosition().y < maxBot) {
			value = maxBot;
		}

		game.actionResolver.sendPos(value);
		player.getBody().setTransform(player.getBody().getPosition().x, value,0);

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
