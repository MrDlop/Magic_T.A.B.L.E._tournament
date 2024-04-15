package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;

import java.net.URI;
import java.net.URISyntaxException;
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
    private Stage waiting_room_stage;
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
    TextButton go_back_button1;
    TextButton go_back_button2;
    TextButton go_back_button3;
    public static TextField enter_session_name; //поля для ввода текста
    private TextField enter_players_name;
    private VerticalGroup verticalGroup;// вертикальный контейнер для объектов интерфейса
    public static ArrayList<String> session_names=new ArrayList<>();
    Texture start_screen_img=new Texture( Gdx.files.internal("start_screen.png"));
    Texture single_play_screen_img=new Texture( Gdx.files.internal("single start.png"));
    Texture online_play_screen_img=new Texture( Gdx.files.internal("online_play.png"));
    Sprite start_screen_background=new Sprite(start_screen_img,start_screen_img.getWidth(),start_screen_img.getHeight());
    Sprite single_play_screen_background=new Sprite(single_play_screen_img,single_play_screen_img.getWidth(),single_play_screen_img.getHeight());
    Sprite online_play_screen_background=new Sprite(online_play_screen_img,online_play_screen_img.getWidth(),online_play_screen_img.getHeight());
    AssetManager assetManager=new AssetManager();
    final String FONT_CHARS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

    public MainMenuScreen(GameController gam) {
        //инициализация переменных
        this.game = gam;
        verticalGroup = new VerticalGroup();
//        font.getData().setScale(5);
        GameController.state = GameState.START; //изменение игрового состояния для отрисовки начального экрана
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
        players_create_stage = new Stage();
        session_create_stage = new Stage(); // игра по сети находится в разработке, нет смысла добавлять
        waiting_room_stage=new Stage();
        Gdx.input.setInputProcessor(stage);// позволяет обрабатывать нажатия на объекты, находящиеся на данной сцене

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Rus_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = Gdx.graphics.getHeight() / 13;
        param.characters = FONT_CHARS;
        font = generator.generateFont(param);
        param.size=Gdx.graphics.getHeight() / 20;
        BitmapFont font1=generator.generateFont(param);
        font.setColor(Color.WHITE);
        font1.setColor(Color.BLACK);
        generator.dispose();

//        ObjectMap<String, Object> fontMap = new ObjectMap<String, Object>();
//        fontMap.put("font1", font);
//        fontMap.put("font2", font1);
//        SkinLoader.SkinParameter skinParameter=new SkinLoader.SkinParameter("test.atlas",fontMap);
//        assetManager.load("test.json",Skin.class,skinParameter);
//        assetManager.finishLoading();
//        skin=assetManager.get("test.json",Skin.class);
        skin = new Skin();
        atlas = new TextureAtlas(Gdx.files.internal("test.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("test.json"));
        skin.get(TextButton.TextButtonStyle.class).font=font;

        start_screen_background.setScale(Gdx.graphics.getWidth()/start_screen_background.getWidth(),Gdx.graphics.getHeight()/start_screen_background.getHeight());
        start_screen_background.setCenter(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        single_play_screen_background.setScale(Gdx.graphics.getWidth()/single_play_screen_background.getWidth(),Gdx.graphics.getHeight()/single_play_screen_background.getHeight());
        single_play_screen_background.setCenter(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        online_play_screen_background.setScale(Gdx.graphics.getWidth()/online_play_screen_background.getWidth(),Gdx.graphics.getHeight()/online_play_screen_background.getHeight());
        online_play_screen_background.setCenter(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        single_start_button = new TextButton("Одиночная игра", skin);
        single_start_button.setSize(600, 300);
        single_start_button.addListener(new ClickListener() { //добаление обработчика нажатий кнопке
            public void clicked(InputEvent event, float x, float y) {
                GameController.state = GameState.CREATING;
            }
        });
        online_start_button = new TextButton("Игра по сети", skin);
        online_start_button.setSize(600, 300);
        online_start_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                GameController.state = GameState.CREATING_ONLINE;
            }
        });
        rules_button = new TextButton("Правила", skin);
        rules_button.setSize(600, 300);
        rules_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI("https://disk.yandex.ru/d/3HOYogUDruU7xQ");//переход на сайт для скачивания правил игры
            }
        });
        //в разработке #TODO разобраться с проблемой записи файлов на андроид для реализации запоминания настроек
        settings_button = new TextButton("Настройки", skin);
        settings_button.setSize(600, 300);
        settings_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Server.players_count+=3;
                names.add("biba");
                names.add("boba");
                names.add("aboba");
                GameController.constants.current_preset=GameController.constants.preset_4_players;
                Server.server_init(names);
                game.setScreen(new GameScreen());
                GameController.state = GameState.CHANGE_PLAYER;
            }
        });
        verticalGroup.setSize(600, 500);
        verticalGroup.fill(1.5f);
        verticalGroup.wrap(true);
        verticalGroup.setPosition(Gdx.graphics.getWidth() / 2 - 250, Gdx.graphics.getHeight() / 2 - 370);
        verticalGroup.addActor(single_start_button);
        verticalGroup.addActor(online_start_button);
        verticalGroup.addActor(rules_button);
        verticalGroup.addActor(settings_button);
        add_player_button = new TextButton("add player", skin);
        begin_game_button = new TextButton("start game", skin);
        go_back_button1=new TextButton("back",skin);
        enter_players_name = new TextField("enter player`s name", skin);
        enter_players_name.setSize(800, 150);
        enter_players_name.setPosition(Gdx.graphics.getWidth() / 2f-enter_players_name.getWidth()/2f, Gdx.graphics.getHeight() * 0.67f);
        enter_players_name.getStyle().font.getData().setScale(4);
        enter_players_name.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                enter_players_name.setText("");// нет способа добавить подсказку, поэтому следует при касании поля для ввода его очищать от надписи, выполняющей роль подсказки
            }
        });
//        enter_players_name.clearListeners();
        add_player_button.setSize(500, 200);
        add_player_button.setPosition(Gdx.graphics.getWidth() * 0.67f, Gdx.graphics.getHeight() / 6);
        add_player_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
               if (!enter_players_name.getText().equals("") && Server.players_count < 5 && !enter_players_name.getText().equals("enter player`s name")) {
                    Server.players_count++;
                    names.add(enter_players_name.getText());// получение и запись имени игрока
                    enter_players_name.setText("");
                    if (Server.players_count > 1) begin_game_button.setVisible(true);//кнопка начала игры
                }
            }
        });
        begin_game_button.setSize(500, 200);
        begin_game_button.setVisible(false);
        begin_game_button.setPosition(Gdx.graphics.getWidth() * 0.67f, Gdx.graphics.getHeight() / 6f +begin_game_button.getHeight());
        begin_game_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //запуск игры
                names.trimToSize();
                if(names.size()==2){
                    GameController.constants.current_preset=GameController.constants.preset_2_players;
                }
                if(names.size()==3){
                    GameController.constants.current_preset=GameController.constants.preset_3_players;
                }
                if(names.size()==4){
                    GameController.constants.current_preset=GameController.constants.preset_4_players;
                }
                Server.server_init(names);
                game.setScreen(new GameScreen());
                GameController.state = GameState.CHANGE_PLAYER;
            }
        });
        go_back_button1.setSize(250,100);
        go_back_button1.setPosition(0,Gdx.graphics.getHeight()-go_back_button1.getHeight());
        go_back_button1.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                GameController.state=GameState.START;
            }
        });
        stage.addActor(verticalGroup);
        players_create_stage.addActor(enter_players_name);
        players_create_stage.addActor(add_player_button);
        players_create_stage.addActor(begin_game_button);
        players_create_stage.addActor(go_back_button1);
        //находится в разработке(сетевая игра)
        go_back_button2=new TextButton("back",skin);
        go_back_button2.setSize(250,100);
        go_back_button2.getLabel().setFontScale(4);
        go_back_button2.setPosition(0,Gdx.graphics.getHeight()-go_back_button1.getHeight());
        go_back_button2.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                GameController.state=GameState.START;
            }
        });
        enter_session_name = new TextField("enter session name", skin);
        enter_session_name.setSize(800, 150);
        enter_session_name.setPosition(Gdx.graphics.getWidth() / 2f - enter_session_name.getWidth()*0.56f, Gdx.graphics.getHeight() * 0.8f);
        enter_session_name.getStyle().font.getData().setScale(4);
        enter_session_name.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                enter_session_name.setText("");
            }
        });
        create_session_button = new TextButton("create session", skin);
        create_session_button.setSize(600, 250);
        create_session_button.setPosition(Gdx.graphics.getWidth() / 2.2f - create_session_button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - create_session_button.getHeight() / 2);
        create_session_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
//                URI uri = null;
//                if(!enter_session_name.getText().equals("") && !enter_players_name.getText().equals("enter session name")){
//                    try {
//                        uri=new URI("wss://magic-t-a-b-l-e-tournament-python-server-t8rd.onrender.com");
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    ServerOnline.websocketClient=new WebsocketClient(uri);
//                    ServerOnline.this_player_name=enter_players_name.getText();
//                    ServerOnline.websocketClient.onCreate(enter_session_name.getText(),ServerOnline.this_player_name);
//                }
            }
        });
        join_session_button = new TextButton("join session", skin);
        join_session_button.setSize(600, 250);
        join_session_button.setPosition(Gdx.graphics.getWidth() / 2.2f - join_session_button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - join_session_button.getHeight() * 1.5f);
        join_session_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
//                URI uri = null;
//                if(!enter_session_name.getText().equals("") && !enter_players_name.getText().equals("enter session name")){
//                    try {
//                        uri=new URI("wss://magic-t-a-b-l-e-tournament-python-server-t8rd.onrender.com");
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }
//                    ServerOnline.websocketClient=new WebsocketClient(uri);
//                    ServerOnline.this_player_name=enter_players_name.getText();
//                    ServerOnline.websocketClient.onConnect(enter_session_name.getText(),ServerOnline.this_player_name);
//                }
            }
        });
        begin_online_game_button = new TextButton("start game", skin);
        begin_online_game_button.setSize(600, 250);
//        begin_game_button.setPosition(Gdx.graphics.getWidth() * 0.67f, Gdx.graphics.getHeight() / 6f);
//        begin_game_button.setVisible(true);
//        begin_game_button.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                if(!enter_session_name.getText().equals("") && !enter_players_name.getText().equals("")){
//                    ServerOnline.websocketClient.sendStartRequest();
//                    ServerOnline.server_init(session_names);
//                }
//            }
//        });
        session_create_stage.addActor(join_session_button);
        session_create_stage.addActor(create_session_button);
        session_create_stage.addActor(enter_session_name);
        session_create_stage.addActor(go_back_button2);
//        session_create_stage.addActor(enter_players_name);
        //waiting_room_stage.addActor(begin_game_button);

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
                //ScreenUtils.clear(0, 0, 0, 1);// покраска фона
                spriteBatch.begin();
                start_screen_background.draw(spriteBatch);
                spriteBatch.end();
                stage.draw();
                stage.act();
                break;
            }
            case CREATING: {
                Gdx.input.setInputProcessor(players_create_stage);
                camera.update();
                //ScreenUtils.clear(0, 0, 0, 1)
                spriteBatch.begin();
                single_play_screen_background.draw(spriteBatch);
                spriteBatch.end();
                players_create_stage.draw();
                players_create_stage.act();
                spriteBatch.begin();
                font.draw(spriteBatch, "Игроков  " + Server.players_count + "/4", add_player_button.getX() + 50, add_player_button.getY() - 50);
                spriteBatch.end();
                break;
            }
            // в разработке(онлайн игра)
            case CREATING_ONLINE: {
                Gdx.input.setInputProcessor(session_create_stage);
                camera.update();
                //ScreenUtils.clear(0, 0, 0, 1);
                spriteBatch.begin();
                online_play_screen_background.draw(spriteBatch);
                spriteBatch.end();
                session_create_stage.draw();
                session_create_stage.act();
                break;
            }
            case STARS_SESSION_WAITING:{
                Gdx.input.setInputProcessor(waiting_room_stage);
                camera.update();
                ScreenUtils.clear(0, 0, 0, 1);
                waiting_room_stage.draw();
                waiting_room_stage.act();
                spriteBatch.begin();
                for(int i=0;i<session_names.size();i++){
                    font.getData().setScale(4);
                    font.draw(spriteBatch,session_names.get(i),Gdx.graphics.getWidth()/2.5f,Gdx.graphics.getHeight()/2f+75*i);
                }
                spriteBatch.end();
                break;
            }
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