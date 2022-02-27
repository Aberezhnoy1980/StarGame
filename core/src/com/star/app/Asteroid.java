package com.star.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.star.app.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.ScreenManager.SCREEN_WIDTH;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float rotationSpeed;

    public Asteroid() {
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(MathUtils.random(-200, SCREEN_WIDTH + 200), MathUtils.random(-200, SCREEN_HEIGHT + 200));
        this.velocity = new Vector2(MathUtils.random(-500, 500), MathUtils.random(-500, 500));
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, 1, 1, angle, 0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        angle += rotationSpeed * dt;

        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        // при выходе за пределы параметров ScreenManager обновляются параметры угла атаки (направления), скорости и частоты вращения астероида. Рефакторим переопределение параметров через отдельный метод
        if (position.x < -200) {
            position.x = SCREEN_WIDTH + 200;
            position.y = MathUtils.random(0, SCREEN_HEIGHT);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            velocity.x = MathUtils.random(-500, 500);
//            velocity.y = MathUtils.random(-500, 500);
//            angle = MathUtils.random(0.0f, 360.0f);
//            rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        }
        if (position.x > SCREEN_WIDTH + 200) {
            position.x = - 200;
            position.y = MathUtils.random(0, SCREEN_HEIGHT);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            velocity.x = MathUtils.random(-500, 500);
//            velocity.y = MathUtils.random(-500, 500);
//            angle = MathUtils.random(0.0f, 360.0f);
//            rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        }

        if (position.y < - 200) {
            position.y = SCREEN_HEIGHT + 200;
            position.x = MathUtils.random(0, SCREEN_WIDTH);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            velocity.x = MathUtils.random(-500, 500);
//            velocity.y = MathUtils.random(-500, 500);
//            angle = MathUtils.random(0.0f, 360.0f);
//            rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        }
        if (position.y > SCREEN_HEIGHT + 200) {
            position.y = -200;
            position.x = MathUtils.random(0, SCREEN_WIDTH);
            updateAttackAngleAndVelocityAndRotationSpeed();
//            velocity.x = MathUtils.random(-500, 500);
//            velocity.y = MathUtils.random(-500, 500);
//            angle = MathUtils.random(0.0f, 360.0f);
//            rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        }
    }

    public void updateAttackAngleAndVelocityAndRotationSpeed() {
        velocity.x = MathUtils.random(-500, 500);
        velocity.y = MathUtils.random(-500, 500);
        angle = MathUtils.random(0.0f, 360.0f);
        rotationSpeed = MathUtils.random(-180.0f, 180.0f);
    }
}
