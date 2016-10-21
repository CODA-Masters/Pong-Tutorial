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
import com.codamasters.pong.helpers.InputHandlerWall;

public class gameWallScreen implements Screen{

	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private World world;
	private Box2DDebugRenderer debugRenderer; // Renderizador de depuración
	private SpriteBatch batch, batch2; // Renderizador de sprites
	private ShapeRenderer shapeRenderer; // Renderizador de figuras
	private OrthographicCamera camera, camera2, camera3;
	private Player player, player2;
	private Bounds bounds;
	private Ball ball;
	private double angle;
	private double module;
	private int score;
	private int scored;
	private GlyphLayout layout;
	private boolean end;
	private float force;
    private Preferences preferences;
    private int highscore;
    private boolean new_highscore = false;
	private boolean hand;
	private boolean paused;


	public gameWallScreen(final Pong g, boolean multiplayer){
		float screenWidth = 800;
		float screenHeight = 400;
		float gameWidth = 203;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		
		camera = new OrthographicCamera(gameWidth/10, gameHeight/10);
		camera2 = new OrthographicCamera(gameWidth*1.5f, gameHeight*1.5f);
        camera3 = new OrthographicCamera(gameWidth*3f, gameHeight*3f);

        world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
        batch2 = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		layout = new GlyphLayout();

		angle = 0;
		force = 800;
		score = 0;
		scored = 0;
		end = false;
		paused = false;

		preferences = Gdx.app.getPreferences("pong");
		highscore = preferences.getInteger("highscore", 0);

		initObjects();
		initAssets();
		
		Gdx.input.setInputProcessor(new InputHandlerWall(g, this, gameWidth/10, gameHeight/10));
		createCollisionListener();
    }
	
	// Cargar objetos del juego
	void initObjects(){
		hand = preferences.getBoolean("hand", true);

		if(!hand){
			player = new Player(world,-9,0,0.2f,1.5f);
			player2 = new Player(world,10,0,0.2f,20f);
		}else{
			player = new Player(world,9,0,0.2f,1.5f);
			player2 = new Player(world,-10,0,0.2f,20f);
		}

		bounds = new Bounds(world);
		ball = new Ball(world,0,0);

		if(!hand)
			ball.getBody().applyForce(-800f,100f,0,0,true);
		else
			ball.getBody().applyForce(800f,100f,0,0,true);

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

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!paused) {
			world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		} else {
			world.step(0, VELOCITYITERATIONS, POSITIONITERATIONS);
		}

		camera.update();
		camera2.update();
		camera3.update();
		batch.setProjectionMatrix(camera2.combined);
		batch2.setProjectionMatrix(camera3.combined);

		shapeRenderer.setProjectionMatrix(camera.combined);


		// Si la pelota se pasa de la posición del jugador 1 acaba la partida

		if (!hand) {
			if (ball.getBody().getPosition().x + 2 < player.getBody().getPosition().x && scored == 0) {
				end = true;

				if (score > highscore) {
					preferences.putInteger("highscore", score);
					preferences.flush();

					highscore = score;
					new_highscore = true;
				}
			}
		} else {
			if (ball.getBody().getPosition().x - 2 > player.getBody().getPosition().x && scored == 0) {
				end = true;

				if (score > highscore) {
					preferences.putInteger("highscore", score);
					preferences.flush();

					highscore = score;
					new_highscore = true;
				}
			}
		}


		// Aplicar fuerza a la pelota dependiendo de en qué posición se encuentre respecto al palote
		if (angle == 9000) {
			if (MainMenu.getSound())
				AssetsLoader.pong.play();

			angle = 0;
		}

		if(angle != 0){
			ball.getBody().setLinearVelocity(0,0);
			force += 50f;
			module = Math.sqrt(force*force + 100f*100f);
			ball.getBody().applyForceToCenter((float)(module*Math.cos(angle)),(float)(module*Math.sin(angle)), true);
			angle = 0;

			if(MainMenu.getSound())
				AssetsLoader.pong.play();
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
		
		layout.setText(AssetsLoader.font, score+"");
		float width = layout.width;// contains the width of the current set text

        AssetsLoader.font.draw(batch, score+"", -width/2, 60);

		// Dibujar PAUSE
		if(paused){
			AssetsLoader.font.draw(batch, "PAUSE",-45, 0);
		}

        batch.end();
        batch2.begin();

        if(end){
            if(new_highscore)
                AssetsLoader.font.draw(batch2, "Congratulations! \n New   Highscore   "+highscore, -150 , 5);
            else
			    AssetsLoader.font.draw(batch2, "GAME  FINISHED \n Highscore   " +highscore, -100 , 5);
		}
		
		batch2.end();
		
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
						angle = 9000;
					}


				}else{
					if (fixtureA == player2.getFixture() && fixtureB == ball.getFixture()) {
						float diff = ball.getBody().getPosition().y - player2.getBody().getPosition().y;

						angle = (diff / player2.height * 45) / 360 * 2 * Math.PI;
					}

					if (fixtureA == player.getFixture() && fixtureB == ball.getFixture()) {
						angle = 9000;
					}
				}

				if (fixtureA == player2.getFixture() && fixtureB == ball.getFixture()) {
					score += 1;
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
