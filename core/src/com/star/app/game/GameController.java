package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;

import static com.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static com.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class GameController {

    private Background background;
    private AsteroidController ac;
    private BulletController bc;
    private Hero hero;

    public GameController() {
        this.background = new Background(this);
        this.ac = new AsteroidController(this);
        this.bc = new BulletController();
        this.hero = new Hero(this);
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

    public void update(float dt) {
        background.update(dt);
        ac.update(dt);
        bc.update(dt);
        hero.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < bc.getActiveList().size(); i++) {
            Bullet b = bc.getActiveList().get(i);
            for (int j = 0; j < ac.getActiveList().size(); j++) {
                Asteroid a = ac.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if(a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
    }
}
