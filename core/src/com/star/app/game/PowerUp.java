package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;

public class PowerUp implements Poolable {
    private GameController gc;
    private Vector2 position;
    private Vector2 velocity;
    private float time;
    private boolean active;
    private PowerUpsType type;
    private int power;

    public PowerUp(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public PowerUpsType getType() {
        return type;
    }

    public float getTime() {
        return time;
    }

    public int getPower() {
        return power;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void activate(PowerUpsType type, float x, float y, int power) {
        this.type = type;
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(1.0f, 1.0f), MathUtils.random(1.0f, 1.0f));
        this.velocity.nor().scl(50.0f);
        this.active = true;
        this.power = power;
        this.time = 0.0f;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        time += dt;
        if (time >= 7.0f) {
            deactivate();
        }
    }

    public enum PowerUpsType {
        HEALTH(4), GOLD(1), AMMO(6), MAGNET(3), NUCLEAR(5);

        final int index;

        PowerUpsType(int index) {
            this.index = index;
        }
    }
}
