package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private TextureRegion starTexture;
    private Vector2 tempVector;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private int maxHp;
    private int hp;
    private Circle hitAria;
    private float directEnginePower;
    private float reverseEnginePower;
    private float fireTimer;
    private float bulletSpeed;
    private int score;
    private int scoreView;
    private StringBuilder sb;
    private Weapon currentWeapon;
    private int gold;
    private Shop shop;
    private Weapon[] weapons;
    private int weaponNum;

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.starTexture = Assets.getInstance().getAtlas().findRegion("star16");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.tempVector = new Vector2(0, 0);
        this.angle = 0.0f;
        this.maxHp = 10;
        this.hp = maxHp;
        this.hitAria = new Circle(position, 28);
        this.directEnginePower = 700.0f;
        this.reverseEnginePower = 200.0f;
        this.bulletSpeed = 2000f;
        this.sb = new StringBuilder();
        this.gold = 1000;
        this.shop = new Shop(this);
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
    }

    public Shop getShop() {
        return shop;
    }

    public int getScore() {
        return score;
    }

    public int getGold() {
        return gold;
    }

    public boolean isGoldEnough(int amount) {
        return gold >= amount;
    }

    public void decreaseGold(int amount) {
        gold -= amount;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Circle getHitAria() {
        return hitAria;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public int getHp() {
        return hp;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public void setPause(boolean pause) {
        gc.setPause(pause);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("SCORE:").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(maxHp).append("\n");
        sb.append("AMMO: ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("GOLD: ").append(gold).append("\n");
//        font.draw(batch, sb, 20, 700);
        font.draw(batch, sb, position.x-ScreenManager.HALF_SCREEN_WIDTH+20, position.y+ScreenManager.HALF_SCREEN_HEIGHT-20);

        float mapX =  position.x + 480;
        float mapY =  position.y + 200;
        batch.setColor(Color.GREEN);
        batch.draw(starTexture, mapX - 24, mapY - 24, 48, 48);
        batch.setColor(Color.RED);
        for (int i = 0; i < gc.getAsteroidController().getActiveList().size(); i++) {
            Asteroid a = gc.getAsteroidController().getActiveList().get(i);
            float dst = position.dst(a.getPosition());
            if (dst < 2000.0f) {
                tempVector.set(a.getPosition()).sub(this.position);
                tempVector.scl(160.0f / 2000.0f);
                batch.draw(starTexture, mapX + tempVector.x - 16, mapY + tempVector.y - 16, 32, 32);
            }
        }

        batch.setColor(Color.WHITE);
        for (int i = 0; i < 120; i++) {
            batch.draw(starTexture, mapX + 160.0f * MathUtils.cosDeg(360.0f / 120.0f * i) - 8, mapY + 160.0f * MathUtils.sinDeg(360.0f / 120.0f * i) - 8);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            setPause(true);
            shop.setVisible(true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFire();
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

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 25;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 25;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 0.5f, 0.0f, 1.0f);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * reverseEnginePower * dt;
            velocity.y -= MathUtils.sinDeg(angle) * reverseEnginePower * dt;

            float bx = position.x + MathUtils.cosDeg(angle - 90) * 25;
            float by = position.y + MathUtils.sinDeg(angle - 90) * 25;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.2f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f);
            }

            bx = position.x + MathUtils.cosDeg(angle + 90) * 25;
            by = position.y + MathUtils.sinDeg(angle + 90) * 25;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20),
                        0.2f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f);
            }
        }

        position.mulAdd(velocity, dt);
        float stopFactor = 1.0f - dt;

        if (stopFactor < 0.0f) {
            stopFactor = 0.0f;
        }
        velocity.scl(stopFactor);
        hitAria.setPosition(position);

        checkBorders();
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void consume(PowerUp p) {
        switch (p.getType()) {
            case HEALTH:
                hp += p.getPower();
                if (hp > maxHp) {
                    hp = maxHp;
                }
                break;
            case AMMO:
                currentWeapon.addAmmos(p.getPower());
                break;
            case GOLD:
                gold += p.getPower();
                break;
        }
    }

    public boolean upgrade(Skill skill) {
        switch (skill) {
            case HP_MAX:
                maxHp += 10;
                return true;
            case HP:
                if (hp < maxHp) {
                    hp += 10;
                    if (hp > maxHp) {
                        hp = maxHp;
                    }
                    return true;
                }
                return false;
            case WEAPON:
                if (weaponNum < weapons.length - 1) {
                    weaponNum++;
                    currentWeapon = weapons[weaponNum];
                    return true;
                }
                return false;
        }
        return false;
    }

    private void tryToFire() {
        if (fireTimer > 0.2f) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
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

    public enum Skill {
        HP_MAX(20), HP(20), WEAPON(100);

        int cost;

        Skill(int cost) {
            this.cost = cost;
        }
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                  new Weapon(gc, this, 0.2f, 1, 2000, 100,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -5),
                                new Vector3(28, 90, 5),
                        }),
                new Weapon(gc, this, 0.2f, 1, 400, 100,
                        new Vector3[]{
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.2f, 1, 500, 200,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.1f, 1, 700, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, 90, 10),
                        }),
                new Weapon(gc, this, 0.1f, 1, 700, 800,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                        }),
                new Weapon(gc, this, 0.1f, 2, 700, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                        }),
                new Weapon(gc, this, 0.2f, 10, 700, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20),
                                new Vector3(28, -90, -30),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, 90, 30),
                        })
        };
    }

}
