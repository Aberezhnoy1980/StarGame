package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.utils.Assets;

public class Bot extends Ship implements Poolable {
    private final Vector2 tempVector;
    private final StringBuilder sb;
    private final Sound enemyAlert;
    private final Sound enemyDestroyed;
    private boolean active;
    private Texture texture;

    public Bot(GameController gc) {
        super(gc, 30, 500.0f, 100.0f);
        this.shipOwnerTexture = Assets.getInstance().getAtlas().findRegion("boss");
        this.enemyAlert = Assets.getInstance().getAssetManager().get("audio/enemyAlert.mp3");
        this.enemyDestroyed = Assets.getInstance().getAssetManager().get("audio/enemyDestroyed.mp3");
        this.position = new Vector2();
        this.hitArea = new Circle(position, 70);
        this.active = false;
        this.tempVector = new Vector2();
        this.shipOwner = ShipOwner.BOT;
        this.sb = new StringBuilder();
        this.weaponNum = MathUtils.random(gc.getLevel() + 1);
        this.currentWeapon = weapons[weaponNum];
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y) {
        position.set(x, y);
        maxHp = 50 + gc.getLevel() * 10;
//        if (maxHp > 150) maxHp = 150;
        hp = maxHp > 150 ? maxHp = 150 : maxHp;
        active = true;
        enemyAlert.play();
    }

    public void render(SpriteBatch batch) {
        batch.draw(shipOwnerTexture, position.x - 71, position.y - 80, 71, 80,
                142, 161, 1, 1, angle);
    }

    public void update(float dt) {
        super.update(dt);

        if (!isAlive()) {
            active = false;
        }

        tempVector.set(gc.getHero().getPosition()).sub(position).nor();
        angle = tempVector.angleDeg();

//        for (int i = 0; i < gc.getAsteroidController().getActiveList().size(); i++) {
//            Asteroid a = gc.getAsteroidController().getActiveList().get(i);
//            if (a.getPosition().dst(position) > 200) {
//                moveDirect(dt);
//            }
//        }

        if (gc.getHero().getPosition().dst(position) > 200) {
            moveDirect(dt);


            float bx = position.x + MathUtils.cosDeg(angle + 180) * 60;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 60;

            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20), velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.4f,
                        2.0f, 0.2f,
                        0.0f, 0.5f, 1.0f, 1.0f,
                        0.0f, 1.0f, 1.0f, 0.0f);
            }
        }

        if (gc.getHero().getPosition().dst(position) < 300) {
            tryToFire();
        }
    }

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            enemyDestroyed.play(0.5f);
            gc.getParticleController().getEffectBuilder().botDestroy(this);
        }
    }

    public void renderBotGUI(SpriteBatch batch, BitmapFont font) {

        Pixmap pixmap = new Pixmap(130, 5, Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        texture = new Texture(pixmap);
        pixmap.dispose();

        sb.setLength(0);
        sb.append("HP: ").append(this.hp).append(" / ").append(this.maxHp).append("\n");
        font.draw(batch, sb, position.x - 70, position.y + 120);

        batch.draw(texture, position.x - 70, position.y + 80, (float) 130 * hp / maxHp, 5);
    }
}
