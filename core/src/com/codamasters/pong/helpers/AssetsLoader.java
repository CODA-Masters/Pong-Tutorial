package com.codamasters.pong.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AssetsLoader {
	public static BitmapFont font;
	
	public static void load() {
		/*
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(
		font = new BitmapFont(Gdx.files.internal("ARCADECLASSIC.TTF"));*/
	}
	public static void dispose() {
		font.dispose();
	}
}
