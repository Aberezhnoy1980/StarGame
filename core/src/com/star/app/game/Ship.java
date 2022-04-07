package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;

public class Ship {
    protected GameController gc;
    protected TextureRegion shipOwnerTexture;
    protected TextureRegion starTexture;
    protected Vector2 tempVector;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float angle;
    protected int maxHp;
    protected int hp;
    protected Circle hitArea;
    protected float directEnginePower;
    protected float reverseEnginePower;
    protected float fireTimer;
    protected Weapon[] weapons;
    protected int weaponNum;
    protected Weapon currentWeapon;
    protected ShipOwner shipOwner;

    public Ship(GameController gc, int maxHp, float directEnginePower, float reverseEnginePower) {
        this.gc = gc;
        this.velocity = new Vector2(0.0f, 0.0f);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.directEnginePower = directEnginePower;
        this.reverseEnginePower = reverseEnginePower;
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
    }

    public ShipOwner getShipOwner() {
        return shipOwner;
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

    public Circle getHitArea() {
        return hitArea;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void moveDirect(float dt) {
        velocity.x += MathUtils.cosDeg(angle) * directEnginePower * dt;
        velocity.y += MathUtils.sinDeg(angle) * directEnginePower * dt;
    }

    public void moveReverse(float dt) {
        velocity.x -= MathUtils.cosDeg(angle) * reverseEnginePower * dt;
        velocity.y -= MathUtils.sinDeg(angle) * reverseEnginePower * dt;
    }

    public void update(float dt) {
        fireTimer += dt;
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        float stopFactor = 1.0f - dt;
        if (stopFactor < 0.0f) {
            stopFactor = 0.0f;
        }
        velocity.scl(stopFactor);
        checkBorders();
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }


    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(gc, this, 0.2f, 1, 400, 100,
                        new Vector3[]{
                                new Vector3(28, -90, 0),
                                new Vector3(28, 90, 0),}),
                new Weapon(gc, this, 0.2f, 1, 400, 100,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -5),
                                new Vector3(28, 90, 5),}),
                new Weapon(gc, this, 0.2f, 1, 500, 200,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),}),
                new Weapon(gc, this, 0.1f, 1, 700, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),}),
                new Weapon(gc, this, 0.1f, 1, 700, 800,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),}),
                new Weapon(gc, this, 0.1f, 2, 700, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),}),
                new Weapon(gc, this, 0.2f, 10, 700, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, -90, -30),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, 90, 30),})};
    }

    public void checkBorders() {
        if (position.x < 650.0f) {
            position.x = 650.0f;
            velocity.x *= -0.3f;
        }
        if (position.x > ScreenManager.SPACE_WIDTH - 650.0f) {
            position.x = ScreenManager.SPACE_WIDTH - 650.0f;
            velocity.x *= -0.3f;
        }
        if (position.y < 370.0f) {
            position.y = 370.0f;
            velocity.y *= -0.3f;
        }
        if (position.y > ScreenManager.SPACE_HEIGHT - 370.0f) {
            position.y = ScreenManager.SPACE_HEIGHT - 370.0f;
            velocity.y *= -0.3f;
        }
    }

    public void tryToFire() {
        if (fireTimer > 0.2f) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }
}
