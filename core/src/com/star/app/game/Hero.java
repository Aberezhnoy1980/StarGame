package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.maxHp = 10;
        this.hp = maxHp;
        this.hitAria = new Circle(position, 28);
        this.directEnginePower = 700.0f;
        this.reverseEnginePower = 200.0f;
        this.bulletSpeed = 2000f;
        this.sb = new StringBuilder();
        this.gold = 0;

        this.currentWeapon = new Weapon(gc, this,0.2f,1, 2000, 100,
                new Vector3[]{
                        new Vector3(28, 0,0),
                        new Vector3(28, -90,-5),
                        new Vector3(28, 90,5),
                });
    }

    public int getScore() {
        return score;
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

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("SCORE:").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(maxHp).append("\n");
        sb.append("AMMO: ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("GOLD: ").append(gold).append("\n");
        font.draw(batch, sb, 20, 700);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);

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

    private void tryToFire() {
        if (fireTimer > 0.2f) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
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
