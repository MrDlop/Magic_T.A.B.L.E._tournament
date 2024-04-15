package com.mygdx.tablegame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.tablegame.game_logic.EventReceiver;
import com.mygdx.tablegame.game_logic.GameController;
import com.mygdx.tablegame.game_logic.GlobalEvents;
import com.mygdx.tablegame.tools.Animation;
import com.mygdx.tablegame.game_logic.RenderController;
import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Server;
import com.mygdx.tablegame.tools.AssetStorage;
import com.mygdx.tablegame.game_logic.Touchable;
import com.mygdx.tablegame.tools.MyModelInstance;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
//общий класс карты, со всеми необходимыми картами, от него не=аследуются все остальные карты

public class Card extends Touchable implements EventReceiver {
    public static String ID;
    final static Model card_model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("Card_model1.g3dj"));// модель карты
    public Vector3 card_pos = new Vector3(0, 0, 0);//позиция карты
    public MyModelInstance instance; //экземпляр модели
    public Integer cost = 0; // цена карты
    public Integer power_points;// очки мощи
    public Integer win_points;//победные очки
    private BoundingBox box; //хитбокс карты, для рейкаста
    private static int lay_down_cards = 0;// костыль, для отслеживания количества взятых карт при взятии руки, помле устранения зависимости анимаций от каадров убрать
    public ArrayList<Animation> animations3D;// анимации 3д представления
    public boolean in_market = false;//находится ли магазине
    private int texture_id; //id текстуры, для быстрой их смены без подгрузок и не уссложняя наследования
    public String main_axis = "X";
    public float width;
    public float height;


    public Card(int texture_id) {
        this.texture_id = texture_id;
        instance = new MyModelInstance(card_model);
        instance.transform.setTranslation(card_pos);//перемещение карты
        box = new BoundingBox();
        change_texture(3);
        animations3D = new ArrayList<>();
        instance.transform.scale(8.5f, 8.5f, 8.5f);
        instance.calculateBoundingBox(box).mul(instance.transform);//расчет хитбокса
        width = box.getWidth();
        height = box.getHeight();
    }

    @Override
    public MyModelInstance getModelInstance() {
        return instance;
    }

    public void change_texture(int type) {
        //метод для смены текстур, работающий нормально с наследованием
        if (type == 3) {
            instance.materials.get(0).set(AssetStorage.textures3d[texture_id][0]);
        }
        if (type == 4) {
            instance.materials.get(0).set(AssetStorage.textures3d[texture_id][1]);
        }
    }

    public void played() {

    }

    public void discard() {
        //действие при сбросе, переопределяется в наследниках
    }

    public void setCardPos(Vector3 pos) {
        //перемещение карты с обновлением позиции
        card_pos = pos;
        instance.transform.setTranslation(card_pos);
    }

    public void setCardPos(float x, float y, float z) {
        card_pos.set(x, y, z);
        instance.transform.setTranslation(card_pos);
    }

    public BoundingBox getHitBox() {
        //переопределение метода из родительского класса(вычисление актуального хитбокса
        instance.calculateBoundingBox(box).mul(instance.transform);
        return instance.calculateBoundingBox(box).mul(instance.transform);
    }

    public void touched() {
        selected();
    }

    public void doubleTouched() {
        if (Server.market_deck.contains(this) && Server.player_now.getPower_points() >= cost) {
            //покупка карты
            Server.player_now.setPower_points(Server.player_now.getPower_points() - cost);
            GameScreen.getPlayer_UI_names()[Server.player_now.player_number] = Server.player_now.name + "`s power points  : " + Server.player_now.getPower_points();
            //animations3D.add(new Animation(instance, Server.player_now.trash_pos, 2000, Interpolation.linear, Interpolation.linear, "market_to_trash"));
        }
        if (Server.player_now.hand.contains(this)) {
            playedFromHand();
        }
    }

    public void moveToHandLocation() {
        if (!RenderController.collisions.contains(this)) {
            RenderController.collisions.add(this);
        }
        if (!RenderController.renderable_3d.contains(this)) {
            RenderController.renderable_3d.add(this);
        }
        Animation animation = new Animation(instance, Server.player_now.deck_pos, Server.player_now.hand_pos, 2000, instance.getCurrentRotation().cpy(),instance.getCurrentRotation().cpy().add(new Vector3(60, 0, 0)), Interpolation.linear, Interpolation.linear, "to hand");
        animations3D.add(animation);
    }

    public void playedFromHand() {
        Animation animation = new Animation(instance, Server.player_now.played_card_pos, 2000, Server.player_now.getStandart_card_world_rotation(), Interpolation.linear, Interpolation.linear, "played from hand");
        animations3D.add(animation);
        Server.player_now.hand.remove(this);
        if (!Server.player_now.hand.isEmpty()) {
            Server.player_now.refresh_hands_positions();
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //played();
            }
        };
        GameController.timer.schedule(timerTask, 1999);
    }

    public void moveToTrash() {
        Animation animation = new Animation(instance, Server.player_now.trash_pos, 1500, Server.player_now.getStandart_card_world_rotation(), Interpolation.linear, Interpolation.linear, "to trash");
        animations3D.add(animation);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                setVisible(false);
            }
        };
        GameController.timer.schedule(timerTask,1500);
    }

    @Override
    public void update_animation() {

        if (!animations3D.isEmpty()) {
            Animation animation = animations3D.get(0);
            if (animation.start_time == -1) {
                animation.startAnimation();
            }
            if (animation.duration < TimeUtils.timeSinceMillis(animation.start_time)) {
                animation.lastUpdate();
                animations3D.remove(0);
            } else {
                animation.update();
            }
        }
    }

    public Vector3 calculateInHandPos(int num, int max_num, Vector3 hand_pos) {
        float delta = 0;
        if (max_num % 2 == 0) {
            if (num < max_num / 2) {
                delta = -1 * ((width * ((max_num / 2) - num)) - (0.5f * width));
            } else {
                delta = width * (num - max_num / 2 + 1) - 0.5f * width;
            }
        } else {
            if (num < (max_num / 2 + 1)) {
                delta = (((max_num / 2) - num) * -1) * width;
            }
            if (num >= (max_num / 2 + 1)) {
                delta = (num - (max_num / 2)) * width;
            }
        }
        if (main_axis.equals((String) "X")) {
            return new Vector3(hand_pos.x + delta, hand_pos.y, hand_pos.z + 0.001f * num);
        }
        if (main_axis.equals((String) "Z")) {
            return new Vector3(hand_pos.x + 0.001f * num, hand_pos.y, hand_pos.z + delta);
        }
        return hand_pos;
    }


//    public void animationEnd(String animation_id) {
//        //отслеживание окончания анимаций, после полного перехода на анимации зависящие от времени заменить таймером
//        switch (animation_id) {
//            case ("played_from_hand"): {
//                if (Server.turn_end_button_pressed) {
//                    RenderController.renderable_3d.remove(this);
//                    RenderController.collisions.remove(this);
//                    Server.player_now.trash.add(this);
//                } else {
//                    if (!RenderController.collisions.contains(this))
//                        RenderController.collisions.add(this);
//                    played();
//                    Server.player_now.on_table_cards.add(this);
//                }
//                break;
//            }
//            case ("to_market_deck"): {
//                if (!RenderController.collisions.contains(this))
//                    RenderController.collisions.add(this);
//                in_market = true;
//                break;
//            }
//            case ("to_trash_end"): {
//                RenderController.renderable_3d.remove(this);
//                RenderController.collisions.remove(this);
//                Server.player_now.on_table_cards.remove(this);
//                Server.player_now.trash.add(this);
//                //if (Server.player_now.hand.isEmpty()) Server.player_now.getHand();
//                break;
//            }
//            case ("market_to_trash"): {
//                RenderController.renderable_3d.remove(this);
//                RenderController.collisions.remove(this);
//                Server.market_deck.remove(this);
//                Server.player_now.trash.add(this);
//            }
//        }
//    }

//    public void animation2Dend(String animation_id) {
//        //отслеживание окончания анимаций, после полного перехода на анимации зависящие от времени заменить таймером
//        switch (animation_id) {
//            case ("convert3D"): {
//                CanTouch.renderable_2d.remove(this);
//                CanTouch.sprite_collisions.remove(this);
//                CanTouch.renderable_3d.add(this);
//                animations3D.add(new Animation(temp_camera_pos, temp_on_table_pos, 2000, new Vector3(0, 0, 0), new Vector3(-90, 0, 0), "played_from_hand"));
//                Server.player_now.hand.remove(this);
//                Server.player_now.refresh_hands_positions();
//                is3D = true;
//                break;
//            }
//            case ("laying_out_card"): {
//                if (Server.turn_end_button_pressed) {
//                    for (Card card : Server.player_now.hand) {
//                        CanTouch.renderable_2d.remove(card);
//                        CanTouch.sprite_collisions.remove(card);
//                    }
//                    if (lay_down_cards >= 4) {
//                        Server.turn_started();
//                        lay_down_cards = 0;
//                    } else lay_down_cards++;
//                }
//                Server.player_now.refresh_hands_positions();
//                break;
//            }
//        }
//    }

    public void non_selected() {
        //когда карта не выбрана
        change_texture(3);

    }

    public void selected() {
        //когда карта выбрана
        change_texture(4);
    }

    public void setVisible(boolean visible) {
        if (visible) {
            if (!RenderController.renderable_3d.contains(this)) {
                RenderController.renderable_3d.add(this);
            }
        } else {
            if (RenderController.renderable_3d.contains(this)) {
                RenderController.renderable_3d.remove(this);
            }
        }
    }

    public Vector3 update_pos() {
        //получение актуальной позиции карты
        card_pos = instance.transform.getTranslation(card_pos);
        return card_pos;
    }


    @Override
    public void turnComplited() {
        moveToTrash();

    }

    @Override
    public void turnStarted() {

    }

    @Override
    public void gameEnded() {

    }

    @Override
    public void gameStarted() {

    }

}
