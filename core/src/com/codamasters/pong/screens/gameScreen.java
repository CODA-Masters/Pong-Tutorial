package com.codamasters.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.codamasters.pong.gameobjects.Ball;
import com.codamasters.pong.gameobjects.Bounds;
import com.codamasters.pong.gameobjects.Player;
import com.codamasters.pong.helpers.InputHandler;

public class gameScreen implements Screen{
	
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Player player, player2;
	private Bounds bounds;
	private Ball ball;
	private boolean multiplayer;
	
	public gameScreen(){
		float screenWidth = 1280;
		float screenHeight = 720;
		float gameWidth = 203;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		
		camera = new OrthographicCamera(gameWidth/10, gameHeight/10);
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		multiplayer = false;
		
		initObjects();
		initAssets();
		
		Gdx.input.setInputProcessor(new InputHandler(this, gameWidth/10, gameHeight/10));
	}
	
	void initObjects(){
		player = new Player(world,ball,-9,0,0.2f,1.5f);
		player2 = new Player(world,ball,9,0,0.2f,1.5f);
		bounds = new Bounds(world);
		ball = new Ball(world,0,0);
		ball.getBody().applyForce(-800f,100f,0,0,true);
	}
	
	void initAssets(){
		
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	public void setCamera(OrthographicCamera cam){
		this.camera = cam;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Player getPlayer2(){
		return player2;
	}
	
	public boolean isMultiplayer(){
		return multiplayer;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(90 / 255f, 89 / 255f, 94 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		// Si no estamos en modo multijugador, el jugador 2 se moverá solo siguiendo la pelota
		if(!multiplayer){
			
			player2.getBody().setTransform(player2.getBody().getPosition().x, ball.getBody().getPosition().y,0);
			if(player2.getBody().getPosition().y > 4.05f)
				player2.getBody().setTransform(player2.getBody().getPosition().x, 4.05f,0);
			else if(player2.getBody().getPosition().y < -4.1f)
				player2.getBody().setTransform(player2.getBody().getPosition().x, -4.1f,0);
		}
		
		debugRenderer.render(world, camera.combined);
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
