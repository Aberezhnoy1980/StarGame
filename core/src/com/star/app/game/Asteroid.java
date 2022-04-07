package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Asteroid implements Poolable {
    private final float RADIUS = 256.0f / 2.0f;
    private final GameController gc;
    private final TextureRegion texture;
    private final Vector2 position;
    private final Vector2 velocity;
    private int hpMax;
    private int hp;
    private final Circle hitArea;
    private float angle;
    private float rotationSpeed;
    private float scale;
    private boolean active;
    private final Sound asteroidExplosion;


    public Asteroid(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.asteroidExplosion = Assets.getInstance().getAssetManager().get("audio/explose.mp3");
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Circle getHitArea() {
        return hitArea;
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
        batch.draw(texture, position.x - RADIUS, position.y - RADIUS, RADIUS, RADIUS, RADIUS * 2.0f, RADIUS * 2.0f, scale, scale, angle);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;

        if (position.x < 0) {
            position.x = ScreenManager.SPACE_WIDTH;
            position.y = MathUtils.random(0, ScreenManager.SPACE_HEIGHT);
            updateAttackAngleAndVelocityAndRotationSpeed();

        }
        if (position.x > ScreenManager.SPACE_WIDTH) {
            position.x = 0;
            position.y = MathUtils.random(0, ScreenManager.SPACE_HEIGHT);
            updateAttackAngleAndVelocityAndRotationSpeed();
        }

        if (position.y < 0) {
            position.y = ScreenManager.SPACE_HEIGHT;
            position.x = MathUtils.random(0, ScreenManager.SPACE_WIDTH);
            updateAttackAngleAndVelocityAndRotationSpeed();
        }
        if (position.y > ScreenManager.SPACE_HEIGHT) {
            position.y = 0;
            position.x = MathUtils.random(0, ScreenManager.SPACE_WIDTH);
            updateAttackAngleAndVelocityAndRotationSpeed();
        }
        hitArea.setPosition(position);
    }

    public void updateAttackAngleAndVelocityAndRotationSpeed() {
        velocity.x = MathUtils.random(-300, 300);
        velocity.y = MathUtils.random(-300, 300);
        angle = MathUtils.random(0.0f, 360.0f);
        rotationSpeed = MathUtils.random(-180.0f, 180.0f);
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        hpMax = (int) ((10 + gc.getLevel()) * scale);
        hp = hpMax;
        angle = MathUtils.random(0.0f, 360.0f);
        this.scale = scale;
        rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        hitArea.setPosition(x, y);
        hitArea.setRadius(RADIUS * scale * 0.9f);
        active = true;
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            asteroidExplosion.play(0.5f);
            if (scale > 0.41f) {
                for (int i = 0; i < 5; i++) {
                    if (MathUtils.random() < 0.5) {
                        gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
                    }
                }
//                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
//                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
//                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150, 150), MathUtils.random(-150, 150), scale - 0.3f);
            }
            return true;
        } else return false;
    }
}
