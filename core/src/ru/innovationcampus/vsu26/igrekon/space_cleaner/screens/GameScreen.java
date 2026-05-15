package ru.innovationcampus.vsu26.igrekon.space_cleaner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSession;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.MyGdxGame;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameResources;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.BulletObject;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.ShipObject;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.TrashObject;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    GameSession gameSession;
    ShipObject shipObject;

    ArrayList<TrashObject> trashArray;

    ArrayList<BulletObject> bulletArray;

    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();

        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

    }

    @Override
    public void show() {
        gameSession.startGame();
    }

    @Override
    public void render(float delta) {

        myGdxGame.stepWorld();
        handleInput();

        if (gameSession.shouldSpawnTrash()) {
            TrashObject trashObject = new TrashObject(
                    GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                    GameResources.TRASH_IMG_PATH,
                    myGdxGame.world
            );
            trashArray.add(trashObject);
        }

        updateTrash();

        updateBullets();

        draw();

        if (shipObject.needToShoot()){
            BulletObject laserBullet = new BulletObject(
                    shipObject.getX(), shipObject.getY() + shipObject.height / 2,
                    GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                    GameResources.BULLET_IMG_PATH,
                    myGdxGame.world
            );
            bulletArray.add(laserBullet);
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            shipObject.move(myGdxGame.touch);
        }
    }

    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (BulletObject bulletObject: bulletArray) bulletObject.draw(myGdxGame.batch);
        myGdxGame.batch.end();

    }

    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++) {
            if (!trashArray.get(i).isInFrame()) {
                myGdxGame.world.destroyBody(trashArray.get(i).body);
                trashArray.remove(i--);
            }
        }
    }
    private void updateBullets(){
        for (int i=0;i<bulletArray.size();i++){
            if (bulletArray.get(i).hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }
}
