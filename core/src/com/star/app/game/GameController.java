package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class GameController {

    private Background background;
    private AsteroidController ac;
    private BulletController bc;
    private ParticleController pc;
    private PowerUpsController puc;
    private Hero hero;
    private Vector2 tempVector;
    private boolean pause;

    public GameController() {
        this.background = new Background(this);
        this.ac = new AsteroidController(this);
        this.bc = new BulletController(this);
        this.pc = new ParticleController(this);
        this.puc = new PowerUpsController(this);
        this.hero = new Hero(this);
        this.tempVector = new Vector2();
        renderAsteroids(10);
    }

    public void renderAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            ac.setup(MathUtils.random(-200, SCREEN_WIDTH + 200), MathUtils.random(-200, SCREEN_HEIGHT + 200),
                    MathUtils.random(-500, 500), MathUtils.random(-500, 500), MathUtils.random(0.3f, 1.0f));
        }
    }

    public Background getBackground() {
        return background;
    }

    public AsteroidController getAsteroidController() {
        return ac;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletController getBulletController() {
        return bc;
    }

    public ParticleController getParticleController() {
        return pc;
    }

    public PowerUpsController getPowerUpsController() {
        return puc;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        background.update(dt);
        ac.update(dt);
        bc.update(dt);
        pc.update(dt);
        puc.update(dt);
        hero.update(dt);
        checkCollisions();
        if (hero.getHp() <= 0) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER, hero);
        }
    }

    public void checkCollisions() {
        // столкновения пуль и астероидов
        for (int i = 0; i < bc.getActiveList().size(); i++) {
            Bullet b = bc.getActiveList().get(i);
            for (int j = 0; j < ac.getActiveList().size(); j++) {
                Asteroid a = ac.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    pc.setup(b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-50, 50), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            10.5f, 1.2f,
                            1.0f, 0.5f, 0.0f, 1.0f,
                            1.0f, 1.0f, 1.0f, 0.0f);
                    b.deactivate();
                    if (a.takeDamage(hero.getCurrentWeapon().getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            puc.setup(a.getPosition().x, a.getPosition().y, a.getScale() * 0.25f);
                        }
                    }
                    break;
                }
            }
        }

        // столкновения астероидов и героя
        for (int i = 0; i < ac.getActiveList().size(); i++) {
            Asteroid a = ac.getActiveList().get(i);
            if (hero.getHitAria().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLength = (a.getHitArea().radius + hero.getHitAria().radius - dst) / 2.0f;
                tempVector.set(hero.getPosition()).sub(a.getPosition()).nor();

                hero.getPosition().mulAdd(tempVector, halfOverLength);
                a.getPosition().mulAdd(tempVector, -halfOverLength);

                float sumScl = hero.getHitAria().radius * 2 + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVector, -200.0f * hero.getHitAria().radius / sumScl);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 50);
                }
                hero.takeDamage(2);
            }
        }

        // столкновения призов и героя
        for (int i = 0; i < puc.getActiveList().size(); i++) {
            PowerUp p = puc.getActiveList().get(i);
            if (hero.getHitAria().contains(p.getPosition())) {
                hero.consume(p);
                pc.getEffectBuilder().takePowerUpsEffect(p);
                p.deactivate();
            }
        }
    }
}
