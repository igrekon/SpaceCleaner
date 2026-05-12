package ru.innovationcampus.vsu26.igrekon.space_cleaner;

import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.POSITION_ITERATIONS;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.STEP_TIME;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.GameScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;
	Texture img;
	public Vector3 touch;

	public GameScreen gameScreen;

	public World world;

	float accumulator =0;



	@Override
	public void create () {
		Box2D.init();
		batch = new SpriteBatch();
		world = new World(new Vector2(0,0),true);
		camera = new OrthographicCamera();
		camera.setToOrtho(false,GameSettings.SCREEN_WIDTH,GameSettings.SCREEN_HEIGHT);
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);

	}

	public void stepWorld(){
		float delta = Gdx.graphics.getDeltaTime();
		accumulator+=delta;

		if (accumulator>=STEP_TIME){
			accumulator-=STEP_TIME;
			world.step(STEP_TIME,VELOCITY_ITERATIONS,POSITION_ITERATIONS);
		}


	}


	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
