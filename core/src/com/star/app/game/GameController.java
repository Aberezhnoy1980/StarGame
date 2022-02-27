package com.star.app.game;

public class GameController {

    private Background background;
    private BulletController bc;
    private Hero hero;

    public GameController() {
        this.background = new Background(this);
        this.bc = new BulletController();
        this.hero = new Hero(this);
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletController getBulletController() {
        return bc;
    }

    public void update(float dt) {
        background.update(dt);
        bc.update(dt);
        hero.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < bc.getActiveList().size(); i++) {
            Bullet b = bc.getActiveList().get(i);

            if (hero.getPosition().dst(b.getPosition()) < 32.0f) {
                //b.deactivate();
            }
        }
    }
}
