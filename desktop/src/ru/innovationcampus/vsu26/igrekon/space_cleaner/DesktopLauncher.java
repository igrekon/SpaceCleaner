package ru.innovationcampus.vsu26.igrekon.space_cleaner;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.MyGdxGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("SpaceCleaner");
		config.setWindowedMode(GameSettings.SCREEN_WIDTH/2, GameSettings.SCREEN_HEIGHT / 2);
		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
