package ru.innovationcampus.vsu26.igrekon.space_cleaner.objects;

import com.badlogic.gdx.physics.box2d.World;
import java.util.Random;

public class GarbageObject extends GameObject {


    private final int points;
    private final int radius;
    private boolean isCollected;

    // 1. Первый конструктор (вызывает родительский super)
    public GarbageObject(String texturePath, int x, int y, int width, int height, short cBits, World world, int points, int radius) {
        super(texturePath, x, y, width, height, cBits, world);
        this.points = points;
        this.radius = radius;
        this.isCollected = false;
    }


    public GarbageObject(int screenWidth, World world, short cBits, String texturePath) {

        super(
                texturePath,
                new Random().nextInt(screenWidth - 40) + 20,
                -30,
                30,
                30,
                cBits,
                world
        );


        this.points = 50;
        this.radius = 15;
        this.isCollected = false;


        if (this.body != null) {
            Random random = new Random();
            float speedX = (random.nextFloat() * 2f) - 1f;
            float speedY = -(random.nextFloat() * 1.5f + 1f);
            this.body.setLinearVelocity(speedX, speedY);
        }
    }

    // Метод сбора
    public int collect() {
        if (!isCollected) {
            isCollected = true;
            return points;
        }
        return 0;
    }


    public boolean isOutOfBounds(int screenHeight) {
        // Если у вас в GameObject координаты хранятся в body или в полях x, y:
        if (this.body != null) {
            return this.body.getPosition().y < -50; // Для Box2D, если экран идет вверх от 0
        }
        return false;
    }


    public int getPoints() { return points; }
    public int getRadius() { return radius; }
    public boolean isCollected() { return isCollected; }
}
