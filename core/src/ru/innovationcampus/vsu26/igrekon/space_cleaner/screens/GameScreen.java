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
import ru.innovationcampus.vsu26.igrekon.space_cleaner.components.TextView;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.managers.ContactManager;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.managers.MemoryManager;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.BulletObject;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.GarbageObject;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.ShipObject;
import ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.TrashObject;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    GameSession gameSession;

    ShipObject shipObject;
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







    ArrayList<TrashObject> trashArray;

    ArrayList<BulletObject> bulletArray;
    private ArrayList<GarbageObject> garbageList = new ArrayList<>();
    private float garbageSpawnTimer = 0;
//    private final float SPAWN_INTERVAL = 3.0f;

    private final short GARBAGE_BIT = 4;


    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        liveView = new LiveView(305,1215);
        pauseButton = new ButtonView(605, 1200, 46, 54, GameResources.PAUSE_IMG_PATH);
        commonWhiteFont = FontBuilder.generate(100, Color.WHITE, GameResources.FONT_PATH);
        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);
        fullBlackoutView =new ImageView(0,160,GameResources.BLACKOUT_FULL_IMG_PATH);
        continueButton = new ButtonView(300,900,26,24,GameResources.BUTTON_SHORT_BG_IMG_PATH);
        homeButton = new ButtonView(300,55,45,64,GameResources.BUTTON_SHORT_BG_IMG_PATH);
        pauseTextView = new TextView(myGdxGame.commonWhiteFont,300,900,"Pause");



        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();
        contactManager = new ContactManager(myGdxGame.world);
        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);

        topBlackoutView = new ImageView(0,1180,GameResources.BLACKOUT_TOP_IMG_PATH);

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
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
                TrashObject trashObject = new TrashObject(
                        GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                        GameResources.TRASH_IMG_PATH,
                        myGdxGame.world
                );
                trashArray.add(trashObject);
            }

            if (Math.random()<0.1){
//                trashArray.get(trashObject)
//                else



            }

            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());







            if (shipObject.needToShoot()) {
                BulletObject laserBullet = new BulletObject(
                        shipObject.getX(), shipObject.getY() + shipObject.height / 2,
                        GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                        GameResources.BULLET_IMG_PATH,
                        myGdxGame.world
                );
                bulletArray.add(laserBullet);
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.shootSound.play();


            }
            if (!shipObject.isAlive()) {
                gameSession.endGame();
                System.out.println("Game over!");
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }
            updateTrash();

            updateBullets();
            backgroundView.move();
//            scoreTextView.setText("Score:" );
            liveView.setLeftLives(shipObject.getLiveLeft());

            myGdxGame.stepWorld();



        }
        draw();
    }

    private void restartGame() {

        for (int i = 0; i < trashArray.size(); i++) {
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);
        }

        if (shipObject != null) {
            myGdxGame.world.destroyBody(shipObject.body);
        }

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        bulletArray.clear();
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
                        myGdxGame.setScreen(myGdxGame.menuScreen);
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
        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);
        shipObject.draw(myGdxGame.batch);
        for (BulletObject bulletObject: bulletArray) bulletObject.draw(myGdxGame.batch);
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
    private void updateBullets(){
        for (int i=0;i<bulletArray.size();i++){
            if (bulletArray.get(i).hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }

}
