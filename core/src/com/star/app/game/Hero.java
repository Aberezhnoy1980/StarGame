package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero extends Ship {

    private final TextureRegion bossTexture;
    private int score;
    private int scoreView;
    private final StringBuilder sb;
    private int gold;
    private final Shop shop;
    private final Circle magneticField;
    private final Sound pause;
    private final Sound healthUp;
    private final Sound ammoUp;
    private final Sound goldUp;
    private final Sound moveReverse;
    private final Sound magnetUp;
    private final Sound nuclear1;
    private final Sound nuclear;

    public Hero(GameController gc) {
        super(gc, 10, 1000.0f, 200.0f);
        this.shipOwnerTexture = Assets.getInstance().getAtlas().findRegion("new ship");
        this.starTexture = Assets.getInstance().getAtlas().findRegion("star16");
        this.bossTexture = Assets.getInstance().getAtlas().findRegion("boss");
        this.position = new Vector2(640, 360);
        this.tempVector = new Vector2(0, 0);
        this.hitArea = new Circle(position, 28);
        this.magneticField = new Circle(position, 50);
        this.sb = new StringBuilder();
        this.shipOwner = ShipOwner.HERO;
        this.gold = 1000;
        this.shop = new Shop(this, gc);
        this.pause = Assets.getInstance().getAssetManager().get("audio/pause.mp3");
        this.healthUp = Assets.getInstance().getAssetManager().get("audio/healthUp.mp3");
        this.ammoUp = Assets.getInstance().getAssetManager().get("audio/ammoUp.mp3");
        this.goldUp = Assets.getInstance().getAssetManager().get("audio/goldUp.mp3");
        this.magnetUp = Assets.getInstance().getAssetManager().get("audio/magnetUp.mp3");
        this.nuclear1 = Assets.getInstance().getAssetManager().get("audio/nuclear.mp3");
        this.nuclear = Assets.getInstance().getAssetManager().get("audio/explose2.mp3");
        this.moveReverse = Assets.getInstance().getAssetManager().get("audio/moveReverse.mp3");
    }

    public Sound getMagnetUp() {
        return magnetUp;
    }

    public int getScore() {
        return score;
    }

    public int getGold() {
        return gold;
    }

    public Shop getShop() {
        return shop;
    }

    public Circle getMagneticField() {
        return magneticField;
    }

    public void setPause(boolean pause) {
        gc.setPause(pause);
    }

    public Sound getHealthUp() {
        return healthUp;
    }

    public Sound getAmmoUp() {
        return ammoUp;
    }

    public Sound getGoldUp() {
        return goldUp;
    }

    public boolean isGoldEnough(int amount) {
        return gold >= amount;
    }

    public void decreaseGold(int amount) {
        gold -= amount;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void consume(PowerUp p) {
        sb.setLength(0);
        switch (p.getType()) {
            case HEALTH:
                int oldHp = hp;
                hp += p.getPower();
                if (hp > maxHp) {
                    hp = maxHp;
                }
                sb.append("HP + ").append(hp - oldHp);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb, Color.GREEN);
                healthUp.play();
                break;
            case AMMO:
                sb.append("AMMO + ").append(currentWeapon.addAmmo(p.getPower()));
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb, Color.RED);
                ammoUp.play();
                break;
            case GOLD:
                gold += p.getPower();
                sb.append("GOLD + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb, Color.YELLOW);
                goldUp.play();
                break;
            case MAGNET:
                float oldMagneticRadius = magneticField.radius;
                magneticField.radius += p.getPower();
                if (magneticField.radius > 500.0f) {
                    magneticField.radius = 500.0f;
                }
                sb.append("MAGNET + ").append(magneticField.radius - oldMagneticRadius);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb, Color.BLUE);
                magnetUp.play();
                break;
            case NUCLEAR:
                int oldScore = score;
                for (int i = 0; i < gc.getAsteroidController().getActiveList().size(); i++) {
                    Asteroid a = gc.getAsteroidController().getActiveList().get(i);
                    a.deactivate();
                }
                addScore(gc.getAsteroidController().getActiveList().size() * 1000);
                sb.append("EXTRA KILL: ").append(gc.getAsteroidController().getActiveList().size()).append(" ASTEROIDS AND ").append(score - oldScore).append(" POINTS");
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb, Color.FIREBRICK);
                nuclear.play();
//                gc.getParticleController().getEffectBuilder().nuclearSky();
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
            case MAGNET:
                if (magneticField.radius < 500) {
                    magneticField.radius += 10;
                    return true;
                }
                return false;
        }
        return false;
    }

    public void renderHeroGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("SCORE:").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append(" / ").append(maxHp).append("\n");
        sb.append("AMMO: ").append(currentWeapon.getCurBullets()).append(" / ").append(currentWeapon.getMaxBullets()).append("\n");
        sb.append("GOLD: ").append(gold).append("\n");
        sb.append("MAGNET: ").append((int) magneticField.radius).append("\n");
        sb.append("LEVEL: ").append(gc.getLevel()).append("\n");
        font.draw(batch, sb, position.x - ScreenManager.HALF_SCREEN_WIDTH + 20, position.y + ScreenManager.HALF_SCREEN_HEIGHT - 20);

        float mapX = position.x + 480;
        float mapY = position.y + 200;
        batch.setColor(Color.GREEN);
        batch.draw(starTexture, mapX - 24, mapY - 24, 48, 48);
        batch.setColor(Color.ORANGE);
        for (int i = 0; i < gc.getAsteroidController().getActiveList().size(); i++) {
            Asteroid a = gc.getAsteroidController().getActiveList().get(i);
            float dst = position.dst(a.getPosition());
            if (dst < 2000.0f) {
                tempVector.set(a.getPosition()).sub(this.position);
                tempVector.scl(160.0f / 2000.0f);
                batch.draw(starTexture, mapX + tempVector.x - 16, mapY + tempVector.y - 16, 32, 32);
            }
        }
        batch.setColor(Color.RED);
        for (int i = 0; i < gc.getBotController().getActiveList().size(); i++) {
            Bot b = gc.getBotController().getActiveList().get(i);
            float dst = position.dst(b.getPosition());
            if (dst < 2000.0f) {
                tempVector.set(b.getPosition()).sub(this.position);
                tempVector.scl(160.0f / 2000.0f);
                batch.draw(bossTexture, mapX + tempVector.x - 16, mapY + tempVector.y - 16, 16, 16, 32, 32, 1, 1, b.getAngle());
            }
        }
        //TODO BlackHole
        batch.setColor(Color.RED);
        for (int i = 0; i < gc.getBotController().getActiveList().size(); i++) {
            Bot b = gc.getBotController().getActiveList().get(i);
            float dst = position.dst(b.getPosition());
            if (dst < 2000.0f) {
                tempVector.set(b.getPosition()).sub(this.position);
                tempVector.scl(160.0f / 2000.0f);
                batch.draw(bossTexture, mapX + tempVector.x - 16, mapY + tempVector.y - 16, 16, 16, 32, 32, 1, 1, b.getAngle());
            }
        }
        batch.setColor(Color.WHITE);
        for (int i = 0; i < 120; i++) {
            batch.draw(starTexture, mapX + 160.0f * MathUtils.cosDeg(360.0f / 120.0f * i) - 8, mapY + 160.0f * MathUtils.sinDeg(360.0f / 120.0f * i) - 8);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(shipOwnerTexture, position.x - 43, position.y - 43, 43, 43, 86, 87, 1, 1, angle);
    }

    public void update(float dt) {
        super.update(dt);
        updateScore(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            setPause(true);
            shop.setPosition(position.x - 620, position.y - 340);
            shop.setVisible(true);
            gc.getMusic().pause();
            pause.play();
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
            moveDirect(dt);

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 50;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 50;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4), velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20), 0.3f, 2.5f, 0.2f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
            }
        }


        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveReverse(dt);
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                moveReverse.play();
            }

            float bx = position.x + MathUtils.cosDeg(angle - 90) * 25;
            float by = position.y + MathUtils.sinDeg(angle - 90) * 25;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4), velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20), 0.2f, 1.2f, 0.2f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
            }

            bx = position.x + MathUtils.cosDeg(angle + 90) * 25;
            by = position.y + MathUtils.sinDeg(angle + 90) * 25;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4), velocity.x * 0.1f + MathUtils.random(-20, 20), velocity.y * 0.1f + MathUtils.random(-20, 20), 0.2f, 1.2f, 0.2f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f);
            }
        }
        magneticField.setPosition(position);
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }


    public enum Skill {
        HP_MAX(20), HP(20), WEAPON(100), MAGNET(50);

        final int cost;

        Skill(int cost) {
            this.cost = cost;
        }
    }

}
