package com.codamasters.pong;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.codamasters.pong.helpers.ActionResolver;
import com.codamasters.pong.screens.MainMenu;
import com.codamasters.pong.screens.onlineScreen;

public class Pong extends Game implements ApplicationListener {

	public static ActionResolver actionResolver;
	private onlineScreen onlineScreen;

	public Pong(ActionResolver actionResolver){
		this.actionResolver = actionResolver;
	}
	
	@Override
	public void create () {
		setScreen(new MainMenu(this));
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	public void startOnlineGame(){
		final Pong pong = this;
		Gdx.app.postRunnable(new Runnable() {
							 @Override
							 public void run() {
								 onlineScreen = new onlineScreen(pong, 0);
								 setScreen(onlineScreen);
							 }
					 }
		);
	}

	public onlineScreen getOnlineGame(){
		return onlineScreen;
	}
}
