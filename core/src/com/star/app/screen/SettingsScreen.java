package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.Background;
import com.star.app.AppSettings;
import com.star.app.screen.utils.Assets;

public class SettingsScreen extends AbstractScreen {

    private AppSettings preferences;
    private Stage stage;
    private Background background;
    private StringBuilder sb;
    private BitmapFont font72;
    private BitmapFont font36;
    private BitmapFont font24;
    private Music gameSettingMusic;

    public SettingsScreen(SpriteBatch batch) {
        super(batch);
        this.preferences = new AppSettings();
    }

    public AppSettings getPreferences() {
        return preferences;
    }

    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
//        this.crack = new Texture("images/pngwing.com-3.png");
        this.background = new Background(null);
        this.sb = new StringBuilder();
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font36 = Assets.getInstance().getAssetManager().get("fonts/font36.ttf");
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.gameSettingMusic = Assets.getInstance().getAssetManager().get("audio/gta.mp3");

        gameSettingMusic.play();
        gameSettingMusic.setVolume(ScreenManager.getInstance().getSettingsScreen().getPreferences().getMusicVolume());

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        table.setPosition(-300, 150);
        table.layout();

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        Pixmap pixmap = new Pixmap(130, 10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        Texture sliderLine = new Texture(pixmap);
        pixmap.dispose();

        batch.begin();
        batch.draw(sliderLine, 310, 400, 130, 10);
        batch.end();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        skin.add("default", labelStyle);

        Label languageLabel = new Label("Language", skin);

        Label startTitle = new Label("", skin);
        Label volumeTitle = new Label("Volume", skin);
        Label disableTitle = new Label("Enable", skin);
        Label examineTitle = new Label("Examine", skin);

        Label volumeMusicLabel1 = new Label("Game sound track", skin);
        Label volumeMusicLabel2 = new Label("Game setting", skin);
        Label volumeMusicLabel3 = new Label("Main menu", skin);

        Label volumeSoundLabel = new Label("Sound Volume", skin);
        Label musicOnOffLabel = new Label("Music", skin);
        Label soundOnOffLabel = new Label("Sound Effect", skin);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.getDrawable("sliderBG");
        sliderStyle.knob = skin.getDrawable("sliderGreenKnobSph");
        skin.add("default-horizontal", sliderStyle);

//        final Slider volumeMusicSlider2 = new Slider(0f, 1f, 0.05f, false, skin);
//        volumeMusicSlider2.setValue(preferences.getMusicVolume());
//        volumeMusicSlider2.addListener(new EventListener() {
//            @Override
//            public boolean handle(Event event) {
//                preferences.setMusicVolume(volumeMusicSlider2.getValue());
//                return false;
//            }
//        });

        final Slider volumeMusicSlider1 = new Slider(0f, 1f, 0.05f, false, sliderStyle);
        volumeMusicSlider1.setValue(preferences.getMusicVolume());
        volumeMusicSlider1.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                preferences.setMusicVolume(volumeMusicSlider1.getValue());
                return false;
            }
        });


        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOn = skin.getDrawable("confirmRGB");
        checkBoxStyle.checkboxOff = skin.getDrawable("cancelRGB");
        checkBoxStyle.font = font24;
        skin.add("default", checkBoxStyle);

        final CheckBox musicCheckbox1 = new CheckBox(null, skin);
        musicCheckbox1.setChecked(preferences.isMusicEnabled());
        musicCheckbox1.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox1.isChecked();
                preferences.setMusicEnabled(enabled);
                return false;
            }
        });

        final CheckBox musicCheckbox2 = new CheckBox(null, skin);
        musicCheckbox2.setChecked(preferences.isMusicEnabled());
        musicCheckbox2.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox1.isChecked();
                preferences.setMusicEnabled(enabled);
                return false;
            }
        });

        CheckBox.CheckBoxStyle checkBoxStyle1 = new CheckBox.CheckBoxStyle();
        checkBoxStyle1.checkboxOn = skin.getDrawable("playRGB");
        checkBoxStyle1.checkboxOff = skin.getDrawable("stopPlayRGB");
        checkBoxStyle1.font = font24;
        skin.add("default", checkBoxStyle1);

        final CheckBox soundExamineCheckBox1 = new CheckBox(null, skin);
        soundExamineCheckBox1.setChecked(preferences.isMusicEnabled());
        soundExamineCheckBox1.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundExamineCheckBox1.isChecked();
                preferences.setMusicEnabled(enabled);
                return false;
            }
        });

        final CheckBox soundExamineCheckBox2 = new CheckBox(null, skin);
        soundExamineCheckBox2.setChecked(preferences.isMusicEnabled());
        soundExamineCheckBox2.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundExamineCheckBox2.isChecked();
                preferences.setMusicEnabled(enabled);
                return false;
            }
        });

        table.row().pad(10, 0, 0, 10);
        table.add(startTitle);
        table.add(volumeTitle);
        table.add(disableTitle);
        table.add(examineTitle);
        table.row().pad(10, 0, 0, 10);
        table.add(volumeMusicLabel1).right();
        table.add(volumeMusicSlider1).center();
        table.add(musicCheckbox1).center();
        table.add(soundExamineCheckBox1).center();
        table.row().pad(10, 0, 0, 10);
        table.add(volumeMusicLabel2).right();
        table.add(volumeMusicSlider1).center();
        table.add(musicCheckbox2).center();
        table.add(soundExamineCheckBox2).center();



//        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
//        selectBoxStyle.background = skin.getDrawable("simpleButton");
//        selectBoxStyle.font = font24;
//        skin.add("simpleSkin", selectBoxStyle);

//        SelectBox<String> chooseLanguage = new SelectBox<>(skin);
//        chooseLanguage.setItems("rus", "eng");
//        chooseLanguage.setPosition(110, 600);


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("Start game", textButtonStyle);
        Button btnMenuGame = new TextButton("Game menu", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnNewGame.setPosition(110, 50);
        btnNewGame.setTransform(true);
        btnNewGame.setSize(320.0f, 60.0f);
        btnMenuGame.setPosition(480, 50);
        btnMenuGame.setTransform(true);
        btnMenuGame.setSize(320.0f, 60.0f);
        btnExitGame.setPosition(850, 50);
        btnExitGame.setTransform(true);
        btnExitGame.setSize(320.0f, 60.0f);

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
        stage.addActor(table);

//        stage.addActor(volumeMusicSlider1);
//        stage.addActor(volumeMusicSlider3);
//        stage.addActor(musicCheckbox1);
//        stage.addActor(volumeMusicLabel1);
//        stage.addActor(volumeMusicLabel2);
//        stage.addActor(volumeMusicLabel3);
//        stage.addActor(volumeTitle);
//        stage.addActor(disableTitle);



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
//        batch.draw(nuclear_bomb, 1000, 360 - 400.0f / 2.0f - 20.0f, 302.0f, 400.0f);
//        batch.draw(crack, 640 - 1035.0f / 2.0f, 360 - 1352.0f / 2.0f, 1035.0f, 1352.0f);
        font36.draw(batch, "GAME SETTINGS", 0.0f, 650.0f, 1280.0f, 1, false);
        sb.setLength(0);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }

    public enum Language {
        ENGLISH, RUSSIAN
    }
}

