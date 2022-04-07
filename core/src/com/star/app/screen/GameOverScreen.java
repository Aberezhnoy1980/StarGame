package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.Background;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private Stage stage;
    private TextureRegion futurama_crash;
    private Hero defeatedHero;
    private Background background;
    private StringBuilder sb;
    private BitmapFont font72;
    private BitmapFont font36;
    private BitmapFont font24;
    private Sound heroDead;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
    }

    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.futurama_crash = Assets.getInstance().getAtlas().findRegion("futurama_crash");
        this.background = new Background(null);
        this.sb = new StringBuilder();
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font36 = Assets.getInstance().getAssetManager().get("fonts/font36.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.heroDead = Assets.getInstance().getAssetManager().get("audio/gameOver.mp3");

        heroDead.play();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("Try again", textButtonStyle);
        Button btnMenuGame = new TextButton("Game menu", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnNewGame.setPosition(110, 50);
        btnMenuGame.setPosition(480, 50);
        btnExitGame.setPosition(850, 50);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnMenuGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnNewGame);
        stage.addActor(btnMenuGame);
        stage.addActor(btnExitGame);
        skin.dispose();
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1.0f);
        batch.begin();
        background.render(batch);
        batch.draw(futurama_crash, 640 - 478.0f / 2.0f, 360 - 366.0f / 2.0f - 20.0f, 478, 366);
        font72.draw(batch, "GAME OVER", 0, 600, 1280, 1, false);
        font36.draw(batch, "YOUR RESULT:", 110, 500, ScreenManager.SCREEN_WIDTH, Align.left, false);
        font36.draw(batch, "BEST RESULT:", -110, 500, ScreenManager.SCREEN_WIDTH, Align.right, false);
        sb.setLength(0);
        sb.append("SCORE: ").append(defeatedHero.getScore()).append("\n");
        sb.append("GOLD: ").append(defeatedHero.getGold()).append("\n");
        font24.draw(batch, sb, 110, 450, ScreenManager.SCREEN_WIDTH, Align.left, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
