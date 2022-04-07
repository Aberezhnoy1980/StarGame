package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {

    private ScreenManager sm;
    private final Background background;
    private final AsteroidController ac;
    private final BulletController bc;
    private final ParticleController pc;
    private final PowerUpsController puc;
    private final InfoController ic;
    private final BotController btc;
    private final Hero hero;
    private final Vector2 tempVector;
    private boolean pause;
    private final Stage stage;
    private int level;
    private float timer;
    private final Music music;

    public GameController(SpriteBatch batch) {
        this.sm = ScreenManager.getInstance();
        this.background = new Background(this);
        this.ac = new AsteroidController(this);
        this.bc = new BulletController(this);
        this.pc = new ParticleController();
        this.puc = new PowerUpsController(this);
        this.ic = new InfoController();
        this.btc = new BotController(this);
        this.hero = new Hero(this);
        this.tempVector = new Vector2();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.level = 1;
        renderAsteroids(1);
        renderLevelBoss();

        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.setVolume(ScreenManager.getInstance().getSettingsScreen().getPreferences().getMusicVolume());
        this.music.play();
    }

    public BotController getBotController() {
        return btc;
    }

    public InfoController getInfoController() {
        return ic;
    }

    public Music getMusic() {
        return music;
    }

    public int getLevel() {
        return level;
    }

    public float getTimer() {
        return timer;
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

    public Stage getStage() {
        return stage;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void renderAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            ac.setup(MathUtils.random(0, ScreenManager.SPACE_WIDTH), MathUtils.random(0, ScreenManager.SPACE_HEIGHT),
                    MathUtils.random(-300, 300), MathUtils.random(-300, 300), MathUtils.random(0.3f, 1.0f));
        }
    }

    public void renderLevelBoss() {
//        if (level % 3 == 0) {
            btc.setup(MathUtils.random(ScreenManager.SPACE_WIDTH), MathUtils.random(ScreenManager.SPACE_HEIGHT));
//        }
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        timer += dt;
        background.update(dt);
        ac.update(dt);
        bc.update(dt);
        pc.update(dt);
        puc.update(dt);
        ic.update(dt);
        btc.update(dt);
        hero.update(dt);
        stage.act(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER, hero);
        }
        if (ac.getActiveList().size() == 0) {
            level++;
            renderAsteroids(level * 2);
            timer = 0;
        }
//        if (level % 3 == 0) {
//            renderAsteroids(0);
//            renderLevelBoss();
//        }
    }


    public void checkCollisions() {
        // столкновения пуль и астероидов
        for (int i = 0; i < bc.getActiveList().size(); i++) {
            Bullet b = bc.getActiveList().get(i);
            for (int j = 0; j < ac.getActiveList().size(); j++) {
                Asteroid a = ac.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    pc.getEffectBuilder().bulletCollideWithAsteroid(b);
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
            if (hero.getHitArea().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLength = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVector.set(hero.getPosition()).sub(a.getPosition()).nor();

                hero.getPosition().mulAdd(tempVector, halfOverLength);
                a.getPosition().mulAdd(tempVector, -halfOverLength);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVector, -200.0f * hero.getHitArea().radius / sumScl);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 50);
                }
                hero.takeDamage(2 + level);
            }
        }

        // Столкновения астероидов и бота
        for (int i = 0; i < ac.getActiveList().size(); i++) {
            Asteroid a = ac.getActiveList().get(i);
            for (int j = 0; j < btc.getActiveList().size(); j++) {
                Bot b = btc.getActiveList().get(j);

                if (b.getHitArea().overlaps(a.getHitArea())) {
                    float dst = a.getPosition().dst(b.getPosition());
                    float halfOverLength = (a.getHitArea().radius + b.getHitArea().radius - dst) / 2.0f;
                    tempVector.set(b.getPosition()).sub(a.getPosition()).nor();

                    b.getPosition().mulAdd(tempVector, halfOverLength);
                    a.getPosition().mulAdd(tempVector, -halfOverLength);

                    float sumScl = b.getHitArea().radius * 2 + a.getHitArea().radius;
                    b.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                    a.getVelocity().mulAdd(tempVector, -200.0f * b.getHitArea().radius / sumScl);

                    a.takeDamage(1);
                    b.takeDamage(level);
                }
            }
        }

        // столкновения призов и героя
        for (int i = 0; i < puc.getActiveList().size(); i++) {
            PowerUp p = puc.getActiveList().get(i);
            if (hero.getMagneticField().contains(p.getPosition())) {
                tempVector.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tempVector, 100);
            }

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                pc.getEffectBuilder().takePowerUpsEffect(p);
                p.deactivate();
            }
        }

        // Столкновения пуль и кораблей
        for (int i = 0; i < bc.getActiveList().size(); i++) {
            Bullet b = bc.getActiveList().get(i);

            if (b.getOwner().getShipOwner() == ShipOwner.BOT) {
                if (hero.getHitArea().contains(b.getPosition())) {
                    hero.takeDamage((b.getOwner().getCurrentWeapon().getDamage()));
                    b.deactivate();
                }
            }

            if (b.getOwner().getShipOwner() == ShipOwner.HERO) {
                for (int j = 0; j < btc.getActiveList().size(); j++) {
                    Bot bot = btc.getActiveList().get(j);
                    if (bot.getHitArea().contains(b.getPosition())) {
                        bot.takeDamage(b.getOwner().getCurrentWeapon().getDamage());
                        b.deactivate();
                    }
                }
            }
        }

        // Столкновения кораблей
        for (int i = 0; i < btc.getActiveList().size(); i++) {
            Bot b = btc.getActiveList().get(i);
            if (hero.getHitArea().overlaps(b.getHitArea())) {
                float dst = hero.getPosition().dst(b.getPosition());
                float halfOverLen = (b.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVector.set(hero.getPosition()).sub(b.getPosition()).nor();
                hero.getPosition().mulAdd(tempVector, halfOverLen);
                b.getPosition().mulAdd(tempVector, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + b.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVector, 200.0f * b.getHitArea().radius / sumScl);
                b.getVelocity().mulAdd(tempVector, 200.0f * hero.getHitArea().radius / sumScl);

                b.takeDamage(level);
                hero.takeDamage(level);
            }
        }
    }

    public void dispose() {
        background.dispose();
    }
}
