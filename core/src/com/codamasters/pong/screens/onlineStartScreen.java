package com.codamasters.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.codamasters.pong.Pong;
import com.codamasters.pong.gameobjects.Ball;
import com.codamasters.pong.gameobjects.Bounds;
import com.codamasters.pong.gameobjects.Player;
import com.codamasters.pong.helpers.AssetsLoader;

import java.util.Random;

public class onlineStartScreen implements Screen {

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
	private double angle;
	private double module;
	private int scoreP1;
	private int scoreP2;
	private int scored;
	private GlyphLayout layout;
	private boolean end;
	private float force;

	private final String CONNECTING = "Conectando";
	private final String SEARCHING = "Buscando";
	private final String ERROR = "Error conexión";

	private final String WIN = "Victoria";
	private final String LOOSE = "Derrota";
	private final String ENEMY_LEFT = "Rival abandonado";


	private String msg = CONNECTING;
	private Pong game;
    private boolean side;

	public onlineStartScreen(final Pong game){

		this.game = game;

		float screenWidth = 800;
		float screenHeight = 400;
		float gameWidth = 203;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		
		camera = new OrthographicCamera(gameWidth/10, gameHeight/10);
		camera2 = new OrthographicCamera(gameWidth*1.5f, gameHeight*1.5f);
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		layout = new GlyphLayout();
		
		angle = 0;
		force = 800;
		scoreP1 = 0;
		scoreP2 = 0;
		scored = 0;
		end = false;
		
		initObjects();
		initAssets();

        Random rnd = new Random();
        side = rnd.nextBoolean();

	}

	void initObjects(){
		player = new Player(world,-9,0,0.2f,1.5f);
		player2 = new Player(world,9,0,0.2f,1.5f);
		bounds = new Bounds(world);
		ball = new Ball(world,0,0);
		ball.getBody().applyForce(-800f,0,0,0,true);
		module = Math.sqrt(force*force + 100f*100f);
	}
	
	// Cargar contenido audiovisual
	void initAssets(){
		AssetsLoader.load();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		update();
	}

	public void update(){
		Gdx.gl.glClearColor(0 / 255f, 0 / 255f, 0 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

		camera.update();
		camera2.update();
		batch.setProjectionMatrix(camera2.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(player.getBody().getPosition().x-player.width, player.getBody().getPosition().y-player.height, player.width*2, player.height*2);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(player2.getBody().getPosition().x-player2.width, player2.getBody().getPosition().y-player2.height, player2.width*2, player2.height*2);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.circle(ball.getBody().getPosition().x,ball.getBody().getPosition().y, ball.RADIUS,32);
		shapeRenderer.rect(-0.05f,-50,0.1f,100);

		shapeRenderer.end();

		batch.begin();
		layout.setText(AssetsLoader.font, scoreP1+"");
		AssetsLoader.font.draw(batch, msg,-65, 0);
		batch.end();
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

    public void onWaitingStarted(String message) {
        this.msg = SEARCHING;
        update();
    }

    public void onError(String message) {
        this.msg = ERROR;
		update();
    }

    public void onGameStarted(String message) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                sendSide(0, side ? 1 : 0);
                game.setScreen(new onlineScreen(game, onlineStartScreen.this,  side ? 1 : 0));
            }
        });
    }

    public void onGameFinished(int code, boolean isRemote) {

		/*
		if(code==WarpController.GAME_WIN){
			this.msg = LOOSE;
		}else if(code==WarpController.GAME_LOOSE) {
			this.msg = WIN;
		}
		if(code == WarpController.ENEMY_LEFT){
			this.msg = ENEMY_LEFT;
		}
		update();
        end=true;
		game.setScreen(this);

        float screenWidth = 800;
        float screenHeight = 400;
        float gameWidth = 203;
        float gameHeight = screenHeight / (screenWidth / gameWidth);
        Gdx.input.setInputProcessor(new InputStartHandler(game, this, gameWidth / 10, gameHeight / 10));
        */
    }


    private void sendSide(float y, int restart){

    }

    public boolean isEnded(){
        return end;
    }
	
}
