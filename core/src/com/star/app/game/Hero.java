package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class Hero {
    private GameController gc;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float directEnginePower;
    private float fireTimer;

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("ship.png");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.directEnginePower = 700.0f;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle, 0, 0, 64, 64, false, false);
    }

    public void update(float dt) {

        fireTimer += dt;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireTimer > 0.2f) {
                fireTimer = 0.0f;
                gc.getBulletController().setup(position.x, position.y,
                        MathUtils.cosDeg(angle) * 500f + velocity.x,
                        MathUtils.sinDeg(angle) * 500f + velocity.y);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * directEnginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * directEnginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * 100f * dt;
            velocity.y -= MathUtils.sinDeg(angle) * 100f * dt;
        }

        position.mulAdd(velocity, dt);
        float stopFactor = 1.0f - dt;

        if (stopFactor < 0.0f) {
            stopFactor = 0.0f;
        }
        velocity.scl(stopFactor);

        checkBorders();
    }

    public void checkBorders() {
        if (position.x < 32.0f) {
            position.x = 32.0f;
            velocity.x *= -0.3f;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -0.3f;
        }
        if (position.y < 32.0f) {
            position.y = 32.0f;
            velocity.y *= -0.3f;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32.0f) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32.0f;
            velocity.y *= -0.3f;
        }
    }
}
