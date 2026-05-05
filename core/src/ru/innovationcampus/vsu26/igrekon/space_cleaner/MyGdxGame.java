package ru.innovationcampus.vsu26.igrekon.space_cleaner;

import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.POSITION_ITERATIONS;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.STEP_TIME;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.GameScreen;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.ScreenAdapter;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera orthographicCamera;
	Texture img;

	public GameScreen gameScreen;

	public World world;

	float accumulator =0;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		world = new World(new Vector2(0,0),true);
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false,GameSettings.SCREEN_WIDTH,GameSettings.SCREEN_HEIGHT);
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
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

		if (Gdx.input.isTouched()){
			myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
