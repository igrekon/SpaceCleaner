package ru.innovationcampus.vsu26.igrekon.space_cleaner;

import static java.awt.Color.WHITE;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameResources.FONT_PATH;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.POSITION_ITERATIONS;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.STEP_TIME;
import static ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.MovingBackgroundView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.managers.AudioManager;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.GameScreen;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.MenuScreen;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.screens.SettingsScreen;

public class MyGdxGame extends Game {
	public World world;

	public BitmapFont largeWhiteFont;
	public BitmapFont commonWhiteFont;
	public BitmapFont commonBlackFont;

	public Vector3 touch;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public AudioManager audioManager;
	public MovingBackgroundView movingBackgroundView;





	public GameScreen gameScreen;
	public MenuScreen menuScreen;
	public SettingsScreen settingsScreen;
	public int score;

	float accumulator = 0;


	@Override
	public void create() {

		Box2D.init();
		world = new World(new Vector2(0, 0), true);

		largeWhiteFont = FontBuilder.generate(48, Color.WHITE, FONT_PATH);
		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, FONT_PATH);
		commonBlackFont = FontBuilder.generate(24, Color.BLACK, FONT_PATH);

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
		audioManager = new AudioManager();

		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);
		settingsScreen = new SettingsScreen(this);
		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);


		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}
}
