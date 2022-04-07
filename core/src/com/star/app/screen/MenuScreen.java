package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.Background;
import com.star.app.screen.utils.Assets;

public class MenuScreen extends AbstractScreen {
    private BitmapFont font72;
    private BitmapFont font24;
    private BitmapFont font18;
    private Stage stage;
    private Background background;
    private Music music;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font18 = Assets.getInstance().getAssetManager().get("fonts/font18.ttf");
        this.background = new Background(null);

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("New Game", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        Button btnGameSettings = new TextButton("Settings", textButtonStyle);
        btnNewGame.setPosition(110, 110);
        btnExitGame.setPosition(480, 110);
        btnGameSettings.setPosition(850, 110);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        btnGameSettings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.SETTINGS);
            }
        });

        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        stage.addActor(btnGameSettings);
        skin.dispose();

        this.music = Assets.getInstance().getAssetManager().get("audio/music.mp3");
        this.music.setLooping(true);
        this.music.play();
        this.music.setVolume(ScreenManager.getInstance().getSettingsScreen().getPreferences().getMusicVolume());
    }

    public void update(float dt) {
        stage.act(dt);
        background.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1.0f);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "Star Game", 0, 600, 1280, 1, false);
        font24.draw(batch, "Advanced edition", 0, 500, 1280, 1, false);
        font18.draw(batch, "GeekBrains. All right reserved. 2022", 0, 50, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
