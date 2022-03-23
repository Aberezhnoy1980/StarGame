package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float directEnginePower;
    private float reverseEnginePower;
    private float fireTimer;
    private float bulletSpeed;
    private int score;
    private int scoreView;

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.directEnginePower = 700.0f;
        this.reverseEnginePower = 200.0f;
        this.bulletSpeed = 500f;
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

    public int getScore() {
        return score;
    }

    public int getScoreView() {
        return scoreView;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;

        if (scoreView < score) {
            scoreView += 1000 * dt;
            if(scoreView > score) {
                scoreView = score;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireTimer > 0.2f) {
                fireTimer = 0.0f;

                float wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
                float wy = position.y + MathUtils.sinDeg(angle + 90) * 20;

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * bulletSpeed + velocity.x,
                        MathUtils.sinDeg(angle) * bulletSpeed + velocity.y);

                wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 20;

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * bulletSpeed + velocity.x,
                        MathUtils.sinDeg(angle) * bulletSpeed + velocity.y);
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
            velocity.x -= MathUtils.cosDeg(angle) * reverseEnginePower * dt;
            velocity.y -= MathUtils.sinDeg(angle) * reverseEnginePower * dt;
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
