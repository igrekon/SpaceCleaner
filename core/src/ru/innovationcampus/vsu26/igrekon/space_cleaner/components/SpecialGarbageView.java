package ru.innovationcampus.vsu26.igrekon.space_cleaner.components;

import static ru.innovationcampus.vsu26.igrekon.space_cleaner.objects.ShipObject.hitBox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

import ru.innovationcampus.vsu26.igrekon.space_cleaner.GameResources;

public class SpecialGarbageView {
    private Texture texture;
    public float x, y;
    public float width, height;
    private float speed;
    private int scoreValue;


    public SpecialGarbageView(float screenWidth, float screenHeight) {
        // Указываем путь к вашей новой картинке в папке assets
        this.texture = new Texture(GameResources.GARBAGE_BAG_IMG_PATH);



        this.width = texture.getWidth();
        this.height = texture.getHeight();


        this.x = MathUtils.random(0, screenWidth - this.width);
        this.y = screenHeight + MathUtils.random(50, 200);

        this.speed = MathUtils.random(200, 350);


        this.scoreValue = 25;
    }

    public void update(float delta) {
        y -= speed * delta;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }



    public int getScoreValue() {
        return this.scoreValue;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }

    public Circle getHitBox() {
        return hitBox;
    }
}
