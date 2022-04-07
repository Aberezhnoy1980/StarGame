package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.utils.Assets;

public class Weapon {
    private GameController gc;
    private final Ship ship;
    private final float firePeriod;
    private final int damage;
    private final float bulletSpeed;
    private final int maxBullets;
    private int curBullets;
    private final Sound shootSound;
    private final Sound emptyMagazine;

    private final Vector3[] slots;

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, Ship ship, float firePeriod, int damage,
                  float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.ship = ship;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = maxBullets;
        this.slots = slots;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/shoot.mp3");
        this.emptyMagazine = Assets.getInstance().getAssetManager().get("audio/emptyMagazine.mp3");
    }

    public void fire() {
        if (curBullets > 0) {
            curBullets--;
            shootSound.play();

//            for (int i = 0; i < slots.length; i++) {
            for (Vector3 slot : slots) {
                float x, y, vx, vy;
                x = ship.getPosition().x + slot.x * MathUtils.cosDeg(ship.getAngle() + slot.y);
                y = ship.getPosition().y + slot.x * MathUtils.sinDeg(ship.getAngle() + slot.y);
                vx = ship.getVelocity().x + bulletSpeed * MathUtils.cosDeg(ship.getAngle() + slot.z);
                vy = ship.getVelocity().y + bulletSpeed * MathUtils.sinDeg(ship.getAngle() + slot.z);

                gc.getBulletController().setup(ship, x, y, vx, vy);
            }
        } else emptyMagazine.play();
    }

    public int addAmmo(int amount) {
        int oldCurBullets = curBullets;
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
        return curBullets - oldCurBullets;
    }
}
