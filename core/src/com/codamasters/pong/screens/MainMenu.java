package com.codamasters.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.codamasters.pong.Pong;
import com.codamasters.pong.helpers.AssetsLoader;

public class MainMenu implements Screen{
	private Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	private OrthographicCamera camera;
    private Preferences preferences;
	public static boolean sound;

    public MainMenu(final Pong g){

		AssetsLoader.load();
		stage = new Stage(new FitViewport(800,400));
		camera = new OrthographicCamera(stage.getWidth()/3,stage.getHeight()/3);
		batch = new SpriteBatch();
		
		// A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
		skin = new Skin();
        preferences = Gdx.app.getPreferences("pong");


        Gdx.input.setInputProcessor(stage);
		
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		
		pixmap.setColor(Color.RED);
		pixmap.fill();
		skin.add("red", new Texture(pixmap));
 
		// Store the default libgdx font under the name "arcade".
		BitmapFont bfont= AssetsLoader.font;
		skin.add("arcade",bfont);
 
		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("arcade");
 
		skin.add("default", textButtonStyle);
		
		
		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle exitButtonStyle = new TextButtonStyle();
		exitButtonStyle.up = skin.newDrawable("red", Color.DARK_GRAY);
		exitButtonStyle.down = skin.newDrawable("red", Color.DARK_GRAY);
		exitButtonStyle.checked = skin.newDrawable("red", Color.BLUE);
		exitButtonStyle.over = skin.newDrawable("red", Color.LIGHT_GRAY);
		exitButtonStyle.font = skin.getFont("arcade");
		 
		skin.add("exit", exitButtonStyle);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("arcade");
 
		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		final TextButton singleButton=new TextButton("1  PLAYER",textButtonStyle);
		singleButton.setBounds(450, 310, 250, 80);
		
		final TextButton multiButton=new TextButton("2  PLAYERS",textButtonStyle);
		multiButton.setBounds(450, 210, 250, 80);

		final TextButton wallButton=new TextButton("WALL  MODE",textButtonStyle);
		wallButton.setBounds(450, 110, 250, 80);
		
		final TextButton exitButton=	new TextButton("EXIT",exitButtonStyle);
		exitButton.setBounds(450, 10, 250, 80);

		final ImageButton rankingButtonWallMode =new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("trophy.png")))));
        rankingButtonWallMode.setBounds(720, 110, 50, 80);

		final ImageButton soundButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sound_on.png")))),null,  new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sound_off.png")))));

        sound = preferences.getBoolean("sound", true);
        if(sound){
            soundButton.setChecked(false);
        }else{
            soundButton.setChecked(true);
        }

		soundButton.setBounds(50, 330, 50, 50);


		stage.addActor(singleButton);
		stage.addActor(multiButton);
		stage.addActor(wallButton);
		stage.addActor(exitButton);
		stage.addActor(rankingButtonWallMode);
		stage.addActor(soundButton);



		singleButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				g.setScreen( new gameScreen(g,false));
			}
		});
		
		multiButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				g.setScreen( new gameScreen(g,true));

			}
		});

		wallButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				g.setScreen( new gameWallScreen(g,false));
			}
		});
		
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

		rankingButtonWallMode.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

                g.actionResolver.submitScoreWallMode(preferences.getInteger("highscore", 0));
                g.actionResolver.displayLeaderboardWallMode();
            }
		});

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(sound){
                    sound = false;
                }else{
                    sound=true;
                }
                preferences.putBoolean("sound", sound);
                preferences.flush();
            }
        });

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		AssetsLoader.font.draw(batch, "PONG",-90, 10);
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getCamera().update();
        stage.getViewport().update(width, height, false);
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
		stage.dispose();
		skin.dispose();
		
	}

	public static boolean getSound(){
		return sound;
	}

}
