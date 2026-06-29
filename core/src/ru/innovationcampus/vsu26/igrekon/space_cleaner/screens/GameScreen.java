package ru.innovationcampus.vsu26.igrekon.space_cleaner.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;



import ru.innovationcampus.vsu26.igrekon.space_cleaner.FontBuilder;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSession;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameSettings;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameState;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.MyGdxGame;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameResources;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.ButtonView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.ImageView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.LiveView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.MovingBackgroundView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.RecordListView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.SpecialGarbageView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.TextView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.managers.ContactManager;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.managers.MemoryManager;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.Ball;

public class GameScreen extends ScreenAdapter {
    ArrayList<SpecialGarbageView> specialGarbageList = new ArrayList<>();
    MyGdxGame myGdxGame;
    GameSession gameSession;

    Ball shipObject;
    ContactManager contactManager;
    ImageView topBlackoutView;
    TextView scoreTextView;

    TextView recordsTextView;

    TextView pauseTextView;

    ButtonView pauseButton;
    ButtonView homeButton;
    ButtonView homeButton2;
    ImageView fullBlackoutView;
    ButtonView continueButton;
    RecordListView recordsListView;

    LiveView liveView;

    MovingBackgroundView backgroundView;
    BitmapFont commonWhiteFont;







    ArrayList<Ball> trashArray;

    private ArrayList<GarbageObject> garbageList = new ArrayList<>();
    private float garbageSpawnTimer = 0;
//    private final float SPAWN_INTERVAL = 3.0f;

    private final short GARBAGE_BIT = 4;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        liveView = new LiveView(305,1215);
        pauseButton = new ButtonView(605, 1200, 46, 54, GameResources.PAUSE_IMG_PATH);
        commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);
        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);
        fullBlackoutView =new ImageView(0,160,GameResources.BLACKOUT_FULL_IMG_PATH);
        continueButton = new ButtonView(605,1200,46,24,GameResources.BUTTON_SHORT_BG_IMG_PATH);
        homeButton = new ButtonView(300,55,45,64,GameResources.BUTTON_SHORT_BG_IMG_PATH);
        pauseTextView = new TextView(myGdxGame.commonWhiteFont,40,1000);



        trashArray = new ArrayList<>();
        contactManager = new ContactManager(myGdxGame.world);
        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);

        topBlackoutView = new ImageView(0,1180,GameResources.BLACKOUT_TOP_IMG_PATH);

        shipObject = new Ball(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.BALL_WIDTH, GameSettings.BALL_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );
        recordsListView = new RecordListView(myGdxGame.commonWhiteFont, 690);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );

    }

    @Override
    public void show() {
          gameSession.startGame();
          restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();
        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnTrash()) {
                Ball ball = new Ball(
                        GameSettings.BALL_WIDTH, GameSettings.BALL_HEIGHT;
                )
                trashArray.add(ball);
            }

            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());







//            if (shipObject.needToShoot()) {
//                BulletObject laserBullet = new BulletObject(
//                        shipObject.getX(), shipObject.getY() + shipObject.height / 2,
//                        GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
////                        GameResources.BULLET_IMG_PATH,
////                        myGdxGame.world
//                );
//                bulletArray.add(laserBullet);
//                if (myGdxGame.audioManager.isSoundOn)
//                    myGdxGame.audioManager.shootSound.play();
//
//
//            }
            if (!shipObject.isAlive()) {
                gameSession.endGame();
                System.out.println("Game over!");
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }
            updateTrash();

            updateBullets();
            backgroundView.move();
            scoreTextView.setText("Score:" + 100);
            liveView.setLeftLives(shipObject.getLiveLeft());

            myGdxGame.stepWorld();

            draw();

        }
    }

    private void restartGame() {

        for (int i = 0; i < trashArray.size(); i++) {
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);
        }

        if (shipObject != null) {
            myGdxGame.world.destroyBody(shipObject.body);
        }

        shipObject = new Ball(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.BALL_WIDTH, GameSettings.BALL_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        gameSession.startGame();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            shipObject.move(myGdxGame.touch);
            switch (gameSession.state){
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    shipObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        System.out.println("end of game");
                    }
                    break;
                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;

            }
        }
    }


    private void draw() {

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);


        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
//        for (TrashObjecta trash : trashArray) trash.draw(myGdxGame.batch);
//        shipObject.draw(myGdxGame.batch);
//        for (BulletObject bulletObject: bulletArray) bulletObject.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);
        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);

        }
        myGdxGame.batch.end();


    }

    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++) {

            boolean hasToBeDestroyed = !trashArray.get(i).isAlive() || !trashArray.get(i).isInFrame();

            if (!trashArray.get(i).isAlive()) {
                myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (!trashArray.get(i).isAlive()) {
                gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }
            if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(trashArray.get(i).body);
                trashArray.remove(i--);
            }
        }
    }


}
