package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
//класс главного меню игры, отсюда можно начать разные типы игры, посмотреть правила
public class MainMenuScreen implements Screen {
    private final GameController game;
    private SpriteBatch spriteBatch = new SpriteBatch();// для рисования 2д объектов
    private BitmapFont font = new BitmapFont(); // шрифт для различных надписей #TODO внедрить fretype font для использования языков поммимо английского
    private Stage stage;// основная сцена(место до кнопок, полей ввода текста и т.д.)
    public static ArrayList<String> names = new ArrayList<>();// имена игроков для начала игры
    private Stage players_create_stage; // сцена создания игры на одном устройстве
    private Stage session_create_stage; // сцена создания/присоединения онлайн игры
    private Skin skin; // объект, содержащий набор свойств элементов интерфейса(лучше создавать во внешнем редакторе)
    private TextureAtlas atlas; // текстурный атлас для элементов интерфейса
    private PerspectiveCamera camera; // камера
    private TextButton single_start_button;// различные кнопки
    private TextButton rules_button;
    private TextButton settings_button;
    private TextButton add_player_button;
    private TextButton begin_game_button;
    private TextButton online_start_button;
    private TextButton create_session_button;
    private TextButton join_session_button;
    private TextButton begin_online_game_button;
    private TextField enter_session_name; //поля для ввода текста
    private TextField enter_players_name;
    private VerticalGroup verticalGroup;// вертикальный контейнер для объектов интерфейса

    public MainMenuScreen(GameController gam) {
        //инициальзация переменных
        this.game = gam;
        verticalGroup = new VerticalGroup();
        font.getData().setScale(5);
        GameController.state = GameState.START; //изменение игрового состояния для отрисовки начального экрана
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
        players_create_stage = new Stage();
        //session_create_stage = new Stage(); игра по сети находится в разработке, нет смысла добавлять
        Gdx.input.setInputProcessor(stage);// позволяет обрабатывать нажтия на объекты, находящиеся на данной сцене
        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("test.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("test.json"));
        //
        single_start_button = new TextButton("Single Play", skin);
        single_start_button.setSize(600, 300);
        single_start_button.getLabel().setFontScale(5);
        single_start_button.addListener(new ClickListener() { //добаление обработчика нажатий кнопке
            public void clicked(InputEvent event, float x, float y) {
                GameController.state = GameState.CREATING;
            }
        });
        /*online_start_button = new TextButton("Online Game", skin);
        online_start_button.setSize(600, 300);
        online_start_button.getLabel().setFontScale(5);
        online_start_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameController.state = GameState.CREATING_ONLINE;
            }
        });*/
        rules_button = new TextButton("Game Rules", skin);
        rules_button.setSize(600, 300);
        rules_button.getLabel().setFontScale(5);
        rules_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://disk.yandex.ru/d/3HOYogUDruU7xQ");//переход на сайт для скачивания правил игры
            }
        });
        //в разработке #TODO разобраться с проблемой записи файлов на андроид для реализации запоминания настроек
        /*settings_button = new TextButton("Settings", skin);
        settings_button.setSize(600, 300);
        settings_button.getLabel().setFontScale(5);
        settings_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

            }
        });*/
        verticalGroup.setSize(600, 500);
        verticalGroup.fill(1.5f);
        verticalGroup.wrap(true);
        verticalGroup.setPosition(Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 - 370);
        verticalGroup.addActor(single_start_button);
        //verticalGroup.addActor(online_start_button);
        verticalGroup.addActor(rules_button);
        //verticalGroup.addActor(settings_button);
        add_player_button = new TextButton("add player", skin);
        begin_game_button = new TextButton("start game", skin);
        enter_players_name = new TextField("enter player`s name", skin);
        enter_players_name.setPosition(Gdx.graphics.getWidth() / 2f - enter_players_name.getWidth() * 3f, Gdx.graphics.getHeight() * 0.67f);
        enter_players_name.setSize(800, 150);
        enter_players_name.getStyle().font.getData().setScale(4);
        enter_players_name.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                enter_players_name.setText("");// нет способа добавить подсказку, поэтому следует при касании поля для ввода его очищать от надписи, выполняющей роль подсказки
            }
        });
        add_player_button.getLabel().setFontScale(5);
        add_player_button.setSize(500, 200);
        add_player_button.setPosition(Gdx.graphics.getWidth() * 0.67f, Gdx.graphics.getHeight() / 6);
        add_player_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (enter_players_name.getText() != "" && Server.players_count < 5 && enter_players_name.getText() != "enter player`s name") {
                    Server.players_count++;
                    names.add(enter_players_name.getText());// получение и запись имени игрока
                    enter_players_name.setText("");
                    if (Server.players_count > 1) begin_game_button.setVisible(true);//кнопка начала игры
                }
            }
        });
        begin_game_button.setSize(500, 200);
        begin_game_button.setPosition(Gdx.graphics.getWidth() * 0.67f, Gdx.graphics.getHeight() / 6 + 250);
        begin_game_button.setVisible(false);
        begin_game_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //запуск игры
                Server.server_init(names);
                game.setScreen(new GameScreen());
                GameController.state = GameState.CHANGE_PLAYER;
            }
        });
        stage.addActor(verticalGroup);
        players_create_stage.addActor(enter_players_name);
        players_create_stage.addActor(begin_game_button);
        players_create_stage.addActor(add_player_button);
        /*
        //находится в разработке(сетевая игра)
        enter_session_name = new TextField("enter session name", skin);
        enter_session_name.setSize(800, 150);
        enter_session_name.setPosition(Gdx.graphics.getWidth() / 2f - enter_players_name.getWidth() * 3f, Gdx.graphics.getHeight() * 0.8f);
        enter_session_name.getStyle().font.getData().setScale(4);
        enter_session_name.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //enter_players_name.setText("");
            }
        });
        create_session_button = new TextButton("create session", skin);
        create_session_button.setSize(600, 250);
        create_session_button.setPosition(Gdx.graphics.getWidth() / 2.2f - create_session_button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - create_session_button.getHeight() / 2);
        create_session_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        join_session_button = new TextButton("join session", skin);
        join_session_button.setSize(600, 250);
        join_session_button.setPosition(Gdx.graphics.getWidth() / 2.2f - join_session_button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - join_session_button.getHeight() * 1.5f);
        join_session_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        begin_online_game_button = new TextButton("start game", skin);
        begin_online_game_button.setSize(600, 250);
        begin_game_button.setPosition(Gdx.graphics.getWidth() * 0.67f, Gdx.graphics.getHeight() / 6);
        begin_game_button.setVisible(true);
        begin_game_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        session_create_stage.addActor(begin_game_button);
        session_create_stage.addActor(join_session_button);
        session_create_stage.addActor(create_session_button);
        session_create_stage.addActor(enter_session_name);*/
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        switch (GameController.state) {
            case START: {
                Gdx.input.setInputProcessor(stage);
                camera.update();//важно
                ScreenUtils.clear(0, 0, 0, 1);// покраска фона
                stage.act();
                stage.draw();
                break;
            }
            case CREATING: {
                Gdx.input.setInputProcessor(players_create_stage);
                camera.update();
                ScreenUtils.clear(0, 0, 0, 1);
                players_create_stage.act();
                players_create_stage.draw();
                spriteBatch.begin();
                font.draw(spriteBatch, "Players  " + Server.players_count + "/4", add_player_button.getX() + 50, add_player_button.getY() - 50);
                spriteBatch.end();
                break;
            }
            // в разработке(онлайн игра)
            /*case CREATING_ONLINE: {
                Gdx.input.setInputProcessor(session_create_stage);
                camera.update();
                ScreenUtils.clear(0, 0, 0, 1);
                session_create_stage.act();
                session_create_stage.draw();
                break;
            }*/
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}