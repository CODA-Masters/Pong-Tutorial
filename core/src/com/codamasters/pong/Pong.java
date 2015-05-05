package com.codamasters.pong;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.codamasters.pong.screens.MainMenu;

public class Pong extends Game implements ApplicationListener {
	
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
}
