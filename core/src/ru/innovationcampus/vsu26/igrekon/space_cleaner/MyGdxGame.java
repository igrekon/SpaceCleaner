package ru.innovationcampus.vsu26.igrekon.space_cleaner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.GameScreen;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.ScreenAdapter;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera orthographicCamera;
	Texture img;

	public GameScreen gameScreen;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false,GameSettings.SCREEN_WIDTH,GameSettings.SCREEN_HEIGHT);
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
