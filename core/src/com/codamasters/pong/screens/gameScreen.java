package com.codamasters.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.codamasters.pong.Pong;
import com.codamasters.pong.gameobjects.Ball;
import com.codamasters.pong.gameobjects.Bounds;
import com.codamasters.pong.gameobjects.Player;
import com.codamasters.pong.helpers.AssetsLoader;
import com.codamasters.pong.helpers.InputHandler;

public class gameScreen implements Screen{
	
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private World world;
	private Box2DDebugRenderer debugRenderer; // Renderizador de depuración
	private SpriteBatch batch; // Renderizador de sprites
	private ShapeRenderer shapeRenderer; // Renderizador de figuras
	private OrthographicCamera camera, camera2;
	private Player player, player2;
	private Bounds bounds;
	private Ball ball;
	private boolean multiplayer;
	private double angle;
	private double module;
	private float posIA;
	private int scoreP1;
	private int scoreP2;
	private int scored;
	private GlyphLayout layout;
	private boolean end;
	private float force;
	private Preferences preferences;
	private static boolean hand;
	private boolean paused;

	private Pong game;

	
	public gameScreen(final Pong g, boolean multiplayer){
		float screenWidth = 800;
		float screenHeight = 400;
		float gameWidth = 203;
		float gameHeight = screenHeight / (screenWidth / gameWidth);

		game = g;
		camera = new OrthographicCamera(gameWidth/10, gameHeight/10);
		camera2 = new OrthographicCamera(gameWidth*1.5f, gameHeight*1.5f);
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		layout = new GlyphLayout();
		preferences = Gdx.app.getPreferences("pong");
		this.multiplayer = multiplayer;
		angle = 0;
		force = 800;
		scoreP1 = 0;
		scoreP2 = 0;
		scored = 0;
		end = false;
		paused = false;
		
		initObjects();
		initAssets();
		
		Gdx.input.setInputProcessor(new InputHandler(g, this, gameWidth/10, gameHeight/10));
		createCollisionListener();
	}
	
	// Cargar objetos del juego
	void initObjects(){
		hand = preferences.getBoolean("hand", true);
		if(multiplayer){
			hand = false;
		}
		if(hand == false) {
			player = new Player(world, -9, 0, 0.2f, 1.5f);
			player2 = new Player(world, 9, 0, 0.2f, 1.5f);
		}
		else{
			player = new Player(world, 9, 0, 0.2f, 1.5f);
			player2 = new Player(world, -9, 0, 0.2f, 1.5f);
		}
		posIA = player2.getBody().getPosition().y;
		bounds = new Bounds(world);
		ball = new Ball(world,0,0);
		ball.getBody().applyForce(-800f,100f,0,0,true);
		module = Math.sqrt(force*force + 100f*100f);
	}
	
	// Cargar contenido audiovisual
	void initAssets(){
		AssetsLoader.load();
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
	
	public int isScored(){
		return scored;
	}
	
	public boolean isEnded(){
		return end;
	}

	public boolean isPaused(){
		return paused;
	}
	public void pauseGame(){
		paused = true;
	}
	public void resumeGame(){
		paused = false;
	}
	
	// Reinicio del juego al marcar puntos
	public void restartGame(){
		force = 800;
		switch(scored){
		case 1:
			ball.getBody().applyForce(-force,100f,0,0,true);
			break;
		case 2:
			ball.getBody().applyForce(force,100f,0,0,true);
			break;
		}
		scored = 0;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(!paused) {
			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		}
		else{
			world.step(0, VELOCITYITERATIONS, POSITIONITERATIONS);
		}
		
		camera.update();
		camera2.update();
		batch.setProjectionMatrix(camera2.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		
		float maxTop = 3.5f;
		float maxBot = -3.5f;
		
		// Si no estamos en modo multijugador, el jugador 2 se moverá de forma automática siguiendo la pelota
		if(!multiplayer){
			
			// Movimiento automático (Inteligencia artificial básica)
			
			if(ball.getBody().getPosition().y-0.085f > player2.getBody().getPosition().y || ball.getBody().getPosition().y+0.085f < player2.getBody().getPosition().y){
			
				if(ball.getBody().getPosition().y > player2.getBody().getPosition().y)
					posIA += 0.13f;
				else
					posIA -= 0.13f;
			}
			
			player2.getBody().setTransform(player2.getBody().getPosition().x, posIA,0);
		}
			
		// Limitar los bordes
		if (player2.getBody().getPosition().y > maxTop) {
			player2.getBody().setTransform(player2.getBody().getPosition().x, maxTop, 0);
			posIA = maxTop;
		} else if (player2.getBody().getPosition().y < maxBot) {
			player2.getBody().setTransform(player2.getBody().getPosition().x, maxBot, 0);
			posIA = maxBot;
		}
		
		// Si la pelota se pasa de la posición del jugador, marca punto el otro.

		if(!hand) {
			if (ball.getBody().getPosition().x + 0.5f < player.getBody().getPosition().x && scored == 0) {
				scoreP2 += 1;
				scored = 2;
			} else if (ball.getBody().getPosition().x - 0.5f > player2.getBody().getPosition().x && scored == 0) {
				scoreP1 += 1;
				scored = 1;
			}
		}
		else{
			if (ball.getBody().getPosition().x + 0.5f < player2.getBody().getPosition().x && scored == 0) {
				scoreP2 += 1;
				scored = 2;
			} else if (ball.getBody().getPosition().x - 0.5f > player.getBody().getPosition().x && scored == 0) {
				scoreP1 += 1;
				scored = 1;
			}
		}
		
		// Reseteamos la escena a sus valores originales cuando se marca un punto
		if (scored != 0){
			ball.getBody().setTransform(0, 0, 0);
			if(!hand) {
				player.getBody().setTransform(-9, 0, 0);
				player2.getBody().setTransform(9, 0, 0);
			}
			else{
				player.getBody().setTransform(9, 0, 0);
				player2.getBody().setTransform(-9, 0, 0);
			}
			ball.getBody().setLinearVelocity(0, 0);
		}
		
		// Aplicar fuerza a la pelota dependiendo de en qué posición se encuentre respecto al palote
		if(angle != 0){
			ball.getBody().setLinearVelocity(0,0);
			force += 50f;
			module = Math.sqrt(force*force + 100f*100f);
			ball.getBody().applyForceToCenter((float)(module*Math.cos(angle)),(float)(module*Math.sin(angle)), true);
			angle = 0;
			if(MainMenu.getSound())
				AssetsLoader.pong.play();
		}
	
		// Cuando algún jugador marca 10 puntos (a diferencia de 2) termina la partida
		if((scoreP1 >= 10 || scoreP2 >=10) && Math.abs(scoreP1 - scoreP2) > 1){
			end = true;
		}
		
		// DIBUJAR LA ESCENA
		shapeRenderer.begin(ShapeType.Filled);
		
		// Dibujar jugador 1
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(player.getBody().getPosition().x-player.width, player.getBody().getPosition().y-player.height, player.width*2, player.height*2);
		
		// Dibujar jugador 2
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(player2.getBody().getPosition().x-player2.width, player2.getBody().getPosition().y-player2.height, player2.width*2, player2.height*2);
		
		
		// Dibujar pelota
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.circle(ball.getBody().getPosition().x,ball.getBody().getPosition().y, ball.RADIUS,32);
		
		shapeRenderer.rect(-0.05f,-50,0.1f,100);
		
		shapeRenderer.end();
		
		// Dibujar las puntuaciones
		batch.begin();
		
		layout.setText(AssetsLoader.font, scoreP1+"");
		float width = layout.width;// contains the width of the current set text
		
		AssetsLoader.font.draw(batch, scoreP1+"",-25-width/2, 60);
		AssetsLoader.font.draw(batch, scoreP2+"", 25-width/2, 60);
		
		if(end){
			if(!hand) {
				if (scoreP1 > scoreP2) {
					AssetsLoader.font.draw(batch, "PLAYER 1 \n       WINS", -65, 0);
				} else {
					AssetsLoader.font.draw(batch, "PLAYER 2 \n       WINS", -65, 0);
				}
			}
			else{
				if (scoreP1 > scoreP2) {
					AssetsLoader.font.draw(batch, "PLAYER 2 \n       WINS", -65, 0);
				} else {
					AssetsLoader.font.draw(batch, "PLAYER 1 \n       WINS", -65, 0);
				}
			}

			game.actionResolver.showInterstital();
		}

		// Dibujar PAUSE
		if(paused){
			AssetsLoader.font.draw(batch, "PAUSE",-45, 0);
		}

		batch.end();
		
		// Renderizador de depuración
		//debugRenderer.render(world, camera.combined);
		
	}
	
	private void createCollisionListener() {
        world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				if(!hand) {
					if (fixtureA == player.getFixture() && fixtureB == ball.getFixture()) {
						float diff = ball.getBody().getPosition().y - player.getBody().getPosition().y;

						angle = (diff / player.height * 45) / 360 * 2 * Math.PI;

					}

					if (fixtureA == player2.getFixture() && fixtureB == ball.getFixture()) {
						float diff = ball.getBody().getPosition().y - player2.getBody().getPosition().y;

						angle = (180 - diff / player.height * 45) / 360 * 2 * Math.PI;

					}
				}
				else{
					if (fixtureA == player2.getFixture() && fixtureB == ball.getFixture()) {
						float diff = ball.getBody().getPosition().y - player2.getBody().getPosition().y;

						angle = (diff / player.height * 45) / 360 * 2 * Math.PI;

					}

					if (fixtureA == player.getFixture() && fixtureB == ball.getFixture()) {
						float diff = ball.getBody().getPosition().y - player.getBody().getPosition().y;

						angle = (180 - diff / player.height * 45) / 360 * 2 * Math.PI;

					}
				}
				
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
        	
        });
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
		AssetsLoader.dispose();
		world.dispose();
		debugRenderer.dispose();
		shapeRenderer.dispose();
		batch.dispose();
		
	}

}
