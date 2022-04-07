package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;


public class Shop extends Group {
    private GameController gc;
    private Hero hero;
    private BitmapFont font20;

    public Shop(final Hero hero, final GameController gc) {
        this.gc = gc;
        this.hero = hero;
        font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");
        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888);
        pixmap.setColor(0.0f, 0.0f, 0.5f, 1.0f);
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font20;
        skin.add("simpleSkin", textButtonStyle);

        final TextButton btnClose = new TextButton("CONTINUE", textButtonStyle);
        final Shop thisShop = this;

        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
                hero.setPause(false);
                gc.getMusic().play();
            }
        });
        btnClose.setTransform(true);
        btnClose.setSize(170f, 60f);
        btnClose.setColor(Color.RED);
        btnClose.setPosition(210f, 32f);
        this.addActor(btnClose);


        final TextButton btnHpMax = new TextButton("HpMax", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isGoldEnough(Hero.Skill.HP_MAX.cost)) {
                    if (hero.upgrade(Hero.Skill.HP_MAX)) {
                        hero.decreaseGold(Hero.Skill.HP_MAX.cost);
                        hero.getHealthUp().play();
                    }
                }
            }
        });

        btnHpMax.setTransform(true);
        btnHpMax.setSize(170f, 60f);
        btnHpMax.setColor(Color.GREEN);
        btnHpMax.setPosition(20f, 308f);
        this.addActor(btnHpMax);


        final TextButton btnHp = new TextButton("Hp", textButtonStyle);
        btnHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isGoldEnough(Hero.Skill.HP.cost)) {
                    if (hero.upgrade(Hero.Skill.HP)) {
                        hero.decreaseGold(Hero.Skill.HP.cost);
                        hero.getHealthUp().play();
                    }
                }
            }
        });

        btnHp.setTransform(true);
        btnHp.setSize(170f, 60f);
        btnHp.setColor(Color.GREEN);
        btnHp.setPosition(20f, 216f);
        this.addActor(btnHp);


        final TextButton btnWeapon = new TextButton("Weapon", textButtonStyle);
        btnWeapon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isGoldEnough(Hero.Skill.WEAPON.cost)) {
                    if (hero.upgrade(Hero.Skill.WEAPON)) {
                        hero.decreaseGold(Hero.Skill.WEAPON.cost);
                        hero.getAmmoUp().play();
                    }
                }
            }
        });

        btnWeapon.setTransform(true);
        btnWeapon.setSize(170f, 60f);
        btnWeapon.setColor(Color.GREEN);
        btnWeapon.setPosition(20f, 124f);
        this.addActor(btnWeapon);

        final TextButton btnMagnet = new TextButton("Magnet", textButtonStyle);
        btnMagnet.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isGoldEnough(Hero.Skill.MAGNET.cost)) {
                    if (hero.upgrade(Hero.Skill.MAGNET)) {
                        hero.decreaseGold(Hero.Skill.MAGNET.cost);
                        hero.getMagnetUp().play();
                    }
                }
            }
        });

        btnMagnet.setTransform(true);
        btnMagnet.setSize(170f, 60f);
        btnMagnet.setColor(Color.GREEN);
        btnMagnet.setPosition(20f, 32f);
        this.addActor(btnMagnet);

        final TextButton btnNewGame = new TextButton("New game", textButtonStyle);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnNewGame.setTransform(true);
        btnNewGame.setSize(170f, 60f);
        btnNewGame.setPosition(210f, 308f);
        this.addActor(btnNewGame);

        final TextButton btnMenuGame = new TextButton("Game menu", textButtonStyle);
        btnMenuGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        btnMenuGame.setTransform(true);
        btnMenuGame.setSize(170f, 60f);
        btnMenuGame.setPosition(210f, 216f);
        this.addActor(btnMenuGame);

        final TextButton btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        btnExitGame.setTransform(true);
        btnExitGame.setSize(170f, 60f);
        btnExitGame.setPosition(210f, 124f);
        this.addActor(btnExitGame);

        setVisible(false);
        skin.dispose();
    }
}
