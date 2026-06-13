package ru.innovationcampus.vsu26.igrekon.space_cleaner.components;

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

    // Конструктор теперь принимает размеры экрана для случайного спавна
    public SpecialGarbageView(float screenWidth, float screenHeight) {
        // Указываем путь к вашей новой картинке в папке assets
        this.texture = new Texture(GameResources.GARBAGE_BAG_IMG_PATH);


        // Размеры автоматически подстроятся под размер картинки
        this.width = texture.getWidth();
        this.height = texture.getHeight();

        // Появление по всей ширине экрана, чуть выше верхней границы
        this.x = MathUtils.random(0, screenWidth - this.width);
        this.y = screenHeight + MathUtils.random(50, 200);

        // Скорость падения мешка (можно сделать чуть медленнее обычного, так как он тяжелый)
        this.speed = MathUtils.random(200, 350);

        // За такой большой мешок даем много очков, например 25!
        this.scoreValue = 25;
    }

    public void update(float delta) {
        y -= speed * delta;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

//    public Circle getHitBox() {
//        return new Rectangle(x, y, width, height);
//    }

    public int getScoreValue() {
        return this.scoreValue;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
