package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class Asteroid implements Poolable {
    private final float RADIUS = 256.0f / 2.0f;
    private GameController gc;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float rotationSpeed;
    private float scale;
    private boolean active;

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Float getAngle() {
        return angle;
    }

    public Float getRadius() {
        return RADIUS;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - RADIUS, position.y - RADIUS, RADIUS, RADIUS,
                RADIUS * 2.0f, RADIUS * 2.0f, scale, scale, angle, 0, 0, (int) RADIUS * 2, (int) RADIUS * 2, false, false);
    }


    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;

        // при выходе за пределы параметров ScreenManager обновляются параметры угла атаки (направления), скорости, частоты вращения и размера астероида. Рефакторим переопределение параметров через отдельный метод
        if (position.x < -200) {
            position.x = SCREEN_WIDTH + 200;
            position.y = MathUtils.random(0, SCREEN_HEIGHT);
            updateAttackAngleAndVelocityAndRotationSpeed();
            // добавлять новый астероид, если текущий ушел за границы скринмэнеджера.
            // Жесть конечно получается, надо придумать ограничения какие-нибудь.
//            gc.renderAsteroids();
        }
        if (position.x > SCREEN_WIDTH + 200) {
            position.x = -200;
            position.y = MathUtils.random(0, SCREEN_HEIGHT);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            gc.renderAsteroids();
        }

        if (position.y < -200) {
            position.y = SCREEN_HEIGHT + 200;
            position.x = MathUtils.random(0, SCREEN_WIDTH);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            gc.renderAsteroids();
        }
        if (position.y > SCREEN_HEIGHT + 200) {
            position.y = -200;
            position.x = MathUtils.random(0, SCREEN_WIDTH);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            gc.renderAsteroids();
        }
    }

    public void updateAttackAngleAndVelocityAndRotationSpeed() {
        velocity.x = MathUtils.random(-500, 500);
        velocity.y = MathUtils.random(-500, 500);
        angle = MathUtils.random(0.0f, 360.0f);
        rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        scale = MathUtils.random(0.3f, 1.0f);
    }

    public void activate(float x, float y, float vx, float vy) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        this.active = true;
    }
}
