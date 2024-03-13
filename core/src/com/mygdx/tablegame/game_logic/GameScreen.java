package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.tablegame.tools.Animation;
import com.mygdx.tablegame.tools.CameraAnimation;
import com.mygdx.tablegame.tools.ElementUI;
import com.mygdx.tablegame.tools.Pair;
import com.mygdx.tablegame.cards.Card;
import com.mygdx.tablegame.cards.Deck;

import java.util.ArrayList;
//класс игрового экрана, используется для отображения игрового процесса
public class GameScreen implements Screen {
    private Model table_model;//модель стола
    private Model terrain_model;//модель окружения(лес вокруг стола)
    private ModelInstance table_instance;
    private ModelInstance terrain_instance;
    private ModelBatch modelBatch;//для рендера 3д моделей
    private Environment environment;//освещение
    private static SpriteBatch spriteBatch;//для рендера 2д моделей
    Pair<Vector2, Float> animation_data;//для анимаций 2д карт
    private Sprite black_fon;
    private Texture black = new Texture(Gdx.files.internal("black.png"));// текстура для затемнения экрана
    Texture bg_image=new Texture(Gdx.files.internal("change_player_scene.png"));
    Sprite change_player_background=new Sprite(bg_image,bg_image.getWidth(),bg_image.getHeight());
    //сцены для разных ситуаций
    private Stage selection_stage;
    private Stage changing_stage;
    private TextButton change_button;
    private static TextButton[] selection_buttons;//кнопки для выбора цели атаки
    private static BitmapFont font;
    private ArrayList<Deck> decks = new ArrayList<>();// отрисовка колод на столе
    private TextButton end_turn_button;//кнопка для завершения хода
    private Stage run_stage;
    private InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private static String[] player_UI_names = new String[4];//имена игроков
    private Animation anim_data = new Animation();//для 3д анимаций
    private Matrix3 anim_matrix = new Matrix3();//матрица для поворота при 3д анимации
    private Quaternion quaternion = new Quaternion();//для поворота моделей при анимации
    //углы для поворота при 3д анимации
    private Float XrotAng;
    private Float YrotAng;
    private Float ZrotAng;
    private Sprite now_looking_card_sprite;//просматриваемая сейчас карта
    public static boolean card_looking=false;
    Vector3 tmp=new Vector3(Server.player_now.camera.position);
    CameraAnimation cameraAnimation;
    public static String[] getPlayer_UI_names() {
        return player_UI_names;
    }


    public GameScreen() {
        modelBatch = new ModelBatch();
        terrain_model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("terrain.g3dj"));
        table_model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("Table.g3dj"));
        terrain_instance = new ModelInstance(terrain_model);
        terrain_instance.transform.setToScaling(8, 8, 8);//подобрано экспериментально(т.к. нет редактора карт), а делать стол и террейн экземплярами одной модели не представлялось возможным из-за текстурирования
        terrain_instance.transform.setTranslation(0, 227.5f, -175);
        table_instance = new ModelInstance(table_model);
        table_instance.transform.setToScaling(50, 50, 50);
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));//подобрано экспериментально
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        Server.player_now.camera.update();
        spriteBatch = new SpriteBatch();
        black_fon = new Sprite(black, black.getWidth(), black.getHeight());
        black_fon.setCenter(black.getWidth() / 2, black.getHeight() / 2);
        black_fon.setOrigin(black_fon.getWidth() / 2, black_fon.getHeight() / 2);
        black_fon.setTexture(black);
        black_fon.setScale(20);//для закрытия всего экрана
        black_fon.setAlpha(0.75f);// установка прозрачности
        change_player_background.setScale(Gdx.graphics.getWidth()/change_player_background.getWidth(),Gdx.graphics.getHeight()/change_player_background.getHeight());
        change_player_background.setCenter(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f);
        font = new BitmapFont();
        selection_stage = new Stage();
        Skin skin = new Skin();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("text_button.pack"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("skin.json"));
        selection_buttons = new TextButton[5];
        for (int i = 0; i < 5; i++) {
            //по идее более 3 кнопок не понадобится, но на будующее пусть будет 4
            selection_buttons[i] = new TextButton(" ", skin);
            selection_buttons[i].setSize(Gdx.graphics.getHeight() / 5, Gdx.graphics.getHeight() / 5);
            selection_buttons[i].setPosition(0, 0);
            selection_buttons[i].setColor(Color.RED);
            selection_buttons[i].setVisible(false);
            selection_stage.addActor(selection_buttons[i]);
        }
        // размещения моделей колод игроков
        for (int i = 0; i < Server.players_count; i++) {
            Deck deck = new Deck();
            if (Server.players_count != 2 && i % 2 == 1) {
                deck.instance.transform.rotate(0, deck.getHitBox().getCenterY(), 0, 90);
            }
            deck.setCardPos(Server.players[i].deck_pos);
            decks.add(deck);
            Deck deck1 = new Deck();
            if (Server.players_count != 2 && i % 2 == 1) {
                deck1.instance.transform.rotate(0, deck1.getHitBox().getCenterY(), 0, 90);
            }
            deck1.setCardPos(Server.players[i].trash_pos);
            decks.add(deck1);
        }
        Deck deck = new Deck();
        deck.setCardPos(Server.main_deck_pos);
        decks.add(deck);
        Deck deck1 = new Deck();
        deck1.setCardPos(Server.legend_deck_pos);
        decks.add(deck1);
        //сцена для смены игроков
        changing_stage = new Stage();
        change_button = new TextButton(" ", skin);
        change_button.setSize(500, 250);
        change_button.setRound(true);
        change_button.setColor(Color.BLUE);
        change_button.setPosition(Gdx.graphics.getWidth() / 2f - change_button.getWidth() / 2, Gdx.graphics.getHeight() / 2f-change_button.getHeight()*1.5f);
        change_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                inputMultiplexer.clear();
                inputMultiplexer.addProcessor(run_stage);
                //inputMultiplexer.addProcessor(Server.player_now.inputController);
                Server.refresh_market();
                GameController.state = GameState.RUN;
            }
        });
        changing_stage.addActor(change_button);
        //кнопка конца хода
        end_turn_button = new TextButton("End Turn", skin);
        end_turn_button.getLabel().setFontScale(4);
        end_turn_button.setSize(550, 200);
        end_turn_button.setColor(0,0,1,0.8f);
        end_turn_button.setPosition(Gdx.graphics.getWidth() - end_turn_button.getWidth(), Gdx.graphics.getHeight() - end_turn_button.getHeight());
        end_turn_button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Server.player_now.setPower_points(0);
                GameScreen.player_UI_names[Server.player_now.player_number] = Server.player_now.name + "`s power points  : " + Server.player_now.getPower_points();
                Server.turn_ended();
            }
        });
        run_stage = new Stage();
        run_stage.addActor(end_turn_button);
        for (int i = 0; i < Server.players_count; i++) {
            player_UI_names[i] = Server.players[i].name + "`s power points" + Server.players[i].getPower_points();
        }
    }

    @Override
    public void show() {
        //вызывается при установке данного экрана
        Server.player_now.player_init();
        //Server.player_now.getHand();
        cameraAnimation=new CameraAnimation(tmp,new Vector3(-50,50,50),3000,new Vector3(100,100,100),"test",Server.player_now.camera);
    }
    public  void render3dObjects(float delta){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        ScreenUtils.clear(Color.SKY);
        modelBatch.begin(Server.player_now.camera);
        for (RenderableObject model:RenderController.renderable_3d) {
            modelBatch.render(model.modelInstance,environment);
        }
        modelBatch.end();
    }
    public void drawAnimations(){
        for (RenderableObject animatedObj:RenderController.animations) {
            animatedObj.update_animation();
        }
    }
    public  void drawUI(){
        spriteBatch.begin();
        for (ElementUI element : RenderController.UI_elements) {
            element.sprite.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void render(float delta) {
        switch (GameController.state) {
            case RUN: {
                Gdx.input.setInputProcessor(inputMultiplexer);
                Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                ScreenUtils.clear(Color.SKY);
                modelBatch.begin(Server.player_now.camera);
                modelBatch.render(table_instance, environment);
                modelBatch.render(terrain_instance, environment);
                for (Card card : decks) {
                    modelBatch.render(card.instance, environment);
                }
                modelBatch.end();
                drawAnimations();
                render3dObjects(delta);
                drawUI();
                run_stage.act();
                run_stage.draw();
                break;
            }
            case CARD_LOOKING: {
                Gdx.input.setInputProcessor(Server.player_now.inputController);
                Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                ScreenUtils.clear(Color.SKY);
                card_looking=true;
                modelBatch.begin(Server.player_now.camera);
                modelBatch.render(table_instance, environment);
                modelBatch.render(terrain_instance, environment);
                for (Card card : decks) {
                    modelBatch.render(card.instance, environment);
                }
                if (!RenderController.renderable_3d.isEmpty()) {
                    for (Card card : RenderController.renderable_3d) {
                        if (!card.animations3D.isEmpty()) {
                            anim_data = card.animations3D.get(0);
                            if (anim_data.start_time == -1) {
                                anim_data.start_time = TimeUtils.millis();
                            }
                            if (anim_data.duration > TimeUtils.timeSinceMillis(anim_data.start_time)) {
                                if (anim_data.start_rotation_angles != null) {
                                    XrotAng = anim_data.start_rotation_angles.x + anim_data.delta_angleX * (TimeUtils.timeSinceMillis(anim_data.start_time) / anim_data.duration) - anim_data.prevRotX;
                                    anim_data.prevRotX += XrotAng;
                                    YrotAng = anim_data.start_rotation_angles.y + anim_data.delta_angleY * (TimeUtils.timeSinceMillis(anim_data.start_time) / anim_data.duration) - anim_data.prevRotY;
                                    anim_data.prevRotY += YrotAng;
                                    ZrotAng = anim_data.start_rotation_angles.z + anim_data.delta_angleZ * (TimeUtils.timeSinceMillis(anim_data.start_time) / anim_data.duration) - anim_data.prevRotZ;
                                    anim_data.prevRotZ += ZrotAng;
                                    anim_matrix.set(new float[]{
                                            MathUtils.cosDeg(YrotAng) * MathUtils.cosDeg(ZrotAng),
                                            MathUtils.sinDeg(XrotAng) * MathUtils.sinDeg(YrotAng) * MathUtils.cosDeg(ZrotAng) + MathUtils.sinDeg(ZrotAng) * MathUtils.cosDeg(XrotAng),
                                            MathUtils.sinDeg(XrotAng) * MathUtils.sinDeg(ZrotAng) - MathUtils.sinDeg(YrotAng) * MathUtils.cosDeg(XrotAng) * MathUtils.cosDeg(ZrotAng),
                                            -1 * MathUtils.sinDeg(ZrotAng) * MathUtils.cosDeg(YrotAng),
                                            -1 * MathUtils.sinDeg(XrotAng) * MathUtils.sinDeg(YrotAng) * MathUtils.sinDeg(ZrotAng) + MathUtils.cosDeg(XrotAng) * MathUtils.cosDeg(ZrotAng),
                                            MathUtils.sinDeg(XrotAng) * MathUtils.cosDeg(ZrotAng) + MathUtils.sinDeg(YrotAng) * MathUtils.sinDeg(ZrotAng) * MathUtils.cosDeg(XrotAng),
                                            MathUtils.sinDeg(YrotAng),
                                            -1 * MathUtils.sinDeg(XrotAng) * MathUtils.cosDeg(YrotAng),
                                            MathUtils.cosDeg(XrotAng) * MathUtils.cosDeg(YrotAng)
                                    });
                                    quaternion.setFromMatrix(anim_matrix);
                                    card.instance.transform.rotate(quaternion);
                                }
                                card.setCardPos(new Vector3(anim_data.startPos.x + anim_data.distanceX * (TimeUtils.timeSinceMillis(anim_data.start_time) / anim_data.duration), anim_data.startPos.y + anim_data.distanceY * (TimeUtils.timeSinceMillis(anim_data.start_time) / anim_data.duration), anim_data.startPos.z + anim_data.distanceZ * (TimeUtils.timeSinceMillis(anim_data.start_time) / anim_data.duration)));
                            }

                            if (anim_data.duration <= TimeUtils.timeSinceMillis(anim_data.start_time)) {
                                card.setCardPos(anim_data.endPos);
                                RenderController.need_to_delete3D.add(card);
                            }
                        }
                        modelBatch.render(card.instance);
                    }
                }
                for (int i = 0; i < RenderController.need_to_delete3D.size(); ) {
                    RenderController.need_to_delete3D.get(0).animationEnd(RenderController.need_to_delete3D.get(0).animations3D.get(0).id);
                    RenderController.need_to_delete3D.get(0).animations3D.remove(0);
                    RenderController.need_to_delete3D.remove(0);
                }
                modelBatch.end();
                spriteBatch.begin();
                for (ElementUI element : RenderController.UI_elements) {
                    element.sprite.draw(spriteBatch);
                }
                font.getData().setScale(2.5f);
                for (int i = 0; i < Server.players_count; i++) {
                    font.draw(spriteBatch, player_UI_names[i], 10, RenderController.UI_elements.get(0).sprite.getHeight() * i + 50 * i + 40);
                }
                black_fon.setAlpha(0.75f);
                black_fon.draw(spriteBatch);
                //затенение экрана
//                now_looking_card_sprite = new Sprite(CanTouch.now_looking_card.sprite.getTexture(),CanTouch.now_looking_card.sprite.getTexture().getWidth(),CanTouch.now_looking_card.sprite.getTexture().getHeight());
//                now_looking_card_sprite.setScale(2);
//                now_looking_card_sprite.setCenter(now_looking_card_sprite.getWidth()/2f,now_looking_card_sprite.getHeight()/2f);
//                now_looking_card_sprite.setPosition(Gdx.graphics.getWidth() / 2 - now_looking_card_sprite.getWidth()/2 , Gdx.graphics.getHeight() / 2 - now_looking_card_sprite.getHeight()/2);
//                now_looking_card_sprite.draw(spriteBatch);
                spriteBatch.end();
                break;
            }
            case SELECT: {
                //выбор цели для атаки
                Gdx.input.setInputProcessor(selection_stage);
                Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                modelBatch.begin(Server.player_now.camera);
                modelBatch.render(table_instance, environment);
                modelBatch.render(terrain_instance, environment);
                for (Card card : decks) {
                    modelBatch.render(card.instance, environment);
                }
                if (!RenderController.renderable_3d.isEmpty()) {
                    for (Card card : RenderController.renderable_3d) {
                        modelBatch.render(card.instance);
                    }
                }
                modelBatch.end();
//                spriteBatch.begin();
//                if (!CanTouch.renderable_2d.isEmpty()) {
//                    for (Card card : CanTouch.renderable_2d) {
//                        card.sprite.draw(spriteBatch);
//                    }
//                }
                black_fon.setAlpha(0.75f);
                black_fon.draw(spriteBatch);
                font.getData().setScale(5);
                font.draw(spriteBatch, "choose who will be you attack target", Gdx.graphics.getWidth() / 2 - 650, Gdx.graphics.getHeight() / 2 + 100);
                spriteBatch.end();
                selection_stage.act();
                selection_stage.draw();
                break;
            }
            case CHANGE_PLAYER: {
                //смена игрока
                Gdx.input.setInputProcessor(changing_stage);
                spriteBatch.begin();
                change_player_background.draw(spriteBatch);
                font.getData().setScale(5);
                font.draw(spriteBatch, "press button to start " + Server.player_now.name + "`s turn", Gdx.graphics.getWidth() / 2 -("press button to start " + Server.player_now.name + "`s turn").length() * (font.getData().xHeight/3), Gdx.graphics.getHeight() / 2);
                spriteBatch.end();
                changing_stage.act();
                changing_stage.draw();
                break;
            }
            case END: {
                ScreenUtils.clear(Color.GOLDENROD);
                spriteBatch.begin();
                font.getData().setScale(5);
                font.draw(spriteBatch, "Game Over ", Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 2);
                font.getData().setScale(3);
                for (int i = 0; i < Server.players_count; i++) {
                    font.draw(spriteBatch, Server.players[i].name + "  get " + Server.players[i].win_points + " win points", Gdx.graphics.getWidth() / -Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 2.5f - 70 * i);
                }
                spriteBatch.end();
            }
        }
    }


    public static void attack_target_selection(final ArrayList<Player> targets, final int damage) {
        //выбор цели атаки
        //#TODO переделать функцию с использованием элементов интерфеса ElementUI,а не сцены и кнопок
        GameController.state = GameState.SELECT;
        for (int i = 0; i < targets.size(); i++) {
            if (targets.size() == 2) {
                selection_buttons[i].setPosition((float) (Gdx.graphics.getWidth() / 2 - 1.5 * selection_buttons[i].getWidth() + 2 * selection_buttons[i].getWidth() * i), Gdx.graphics.getHeight() / 3.5f);
            }
            if (targets.size() == 3) {
                selection_buttons[i].setPosition((float) (Gdx.graphics.getWidth() / 2 - 1.75f * selection_buttons[i].getWidth() + 1.25 * selection_buttons[i].getWidth() * i), Gdx.graphics.getHeight() / 3.5f);
            }
            if (targets.size() == 4) {
                selection_buttons[i].setPosition((float) (Gdx.graphics.getWidth() / 2 - 3 * selection_buttons[i].getWidth() + 2 * selection_buttons[i].getWidth() * i), Gdx.graphics.getHeight() / 3.5f);
            }
            selection_buttons[i].setVisible(true);
            selection_buttons[i].clearListeners();
            if (i == 0) {
                selection_buttons[i].setText(targets.get(0).name);
                selection_buttons[i].getLabel().setFontScale(3);
                selection_buttons[i].addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Server.attack(targets.get(0), damage);
                        GameController.state = GameState.RUN;
                    }
                });
            }
            if (i == 1) {
                selection_buttons[i].setText(targets.get(1).name);
                selection_buttons[i].getLabel().setFontScale(3);
                selection_buttons[i].addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Server.attack(targets.get(1), damage);
                        GameController.state = GameState.RUN;
                    }
                });
            }
            if (i == 2) {
                selection_buttons[i].setText(targets.get(2).name);
                selection_buttons[i].getLabel().setFontScale(3);
                selection_buttons[i].addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Server.attack(targets.get(2), damage);
                        GameController.state = GameState.RUN;
                    }
                });
            }
            if (i == 3) {
                selection_buttons[i].setText(targets.get(3).name);
                selection_buttons[i].getLabel().setFontScale(3);
                selection_buttons[i].addListener(new ClickListener() {
                    public void clicked(InputEvent event, float x, float y) {
                        Server.attack(targets.get(3), damage);
                        GameController.state = GameState.RUN;
                    }
                });
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
