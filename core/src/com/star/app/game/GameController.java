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
                    MathUtils.random(-500, 500), MathUtils.random(-500, 500));
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
                // Если разнокалиберные камни, то можно вот так неравенство оформить,
                // предварительно добавив константу радиуса (ну, можно и нативно прописать),
                // геттер на нее и зарандомить масштаб, просто ради эксперемента сделал.
                if (a.getPosition().dst(b.getPosition()) < a.getRadius() * a.getScale()) {
                    b.deactivate();
                    a.deactivate();
                }
            }
        }
    }
}
