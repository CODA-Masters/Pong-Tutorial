package com.codamasters.pong.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetsLoader {
	public static BitmapFont font;
	public static Sound pong;
	
	public static void load() {
		font = new BitmapFont(Gdx.files.internal("arcade.fnt"));
		pong = Gdx.audio.newSound(Gdx.files.internal("pong.wav"));
	}
	public static void dispose() {
		font.dispose();
		pong.dispose();
	}
}
