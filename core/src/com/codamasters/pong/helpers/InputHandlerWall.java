package com.codamasters.pong.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.codamasters.pong.Pong;
import com.codamasters.pong.gameobjects.Player;
import com.codamasters.pong.screens.MainMenu;
import com.codamasters.pong.screens.gameWallScreen;

public class InputHandlerWall implements InputProcessor{
	 private Player player, player2;
	 private gameWallScreen screen;
	 private Vector3 target;
	 private Pong game;


	 public InputHandlerWall(final Pong g, gameWallScreen screen, float scaleFactorX, float scaleFactorY){
	     this.screen = screen;
	     this.target = new Vector3();
	     target.set(-10,0,0);
	     player = screen.getPlayer();
	     player2 = screen.getPlayer2();
	     game = g;

		 Gdx.input.setCatchBackKey(true);

	 }
	 
	@Override
	public boolean keyDown(int keycode) {

		if(keycode == Input.Keys.BACK) {
			if (screen.isPaused()) {
				game.setScreen(new MainMenu(game));
			} else {
				screen.pauseGame();
			}
		}

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

		if(screen.isPaused()){
			screen.resumeGame();
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
		
		float maxTop = 3.5f;
		float maxBot = -3.5f;

		player.getBody().setTransform(player.getBody().getPosition().x, target.y,0);
		if(player.getBody().getPosition().y > maxTop)
			player.getBody().setTransform(player.getBody().getPosition().x, maxTop,0);
		else if(player.getBody().getPosition().y < maxBot)
			player.getBody().setTransform(player.getBody().getPosition().x, maxBot,0);

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
