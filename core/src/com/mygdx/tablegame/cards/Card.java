package com.mygdx.tablegame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.tablegame.game_logic.EventReceiver;
import com.mygdx.tablegame.game_logic.GameController;
import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.GlobalEvents;
import com.mygdx.tablegame.game_logic.RenderController;
import com.mygdx.tablegame.game_logic.Server;
import com.mygdx.tablegame.game_logic.Touchable;
import com.mygdx.tablegame.tools.Animation;
import com.mygdx.tablegame.tools.AssetStorage;
import com.mygdx.tablegame.tools.MyModelInstance;

import java.util.ArrayList;
import java.util.TimerTask;
//общий класс карты, со всеми необходимыми картами, от него не=аследуются все остальные карты

public class Card extends Touchable implements EventReceiver {
    public String ID;
    final static Model card_model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("Card_model1.g3dj"));// модель карты
    public Vector3 card_pos = new Vector3(0, 0, 0);//позиция карты
    public MyModelInstance instance; //экземпляр модели
    public Integer cost = 0; // цена карты
    public Integer power_points;// очки мощи
    public Integer win_points;//победные очки
    private BoundingBox box; //хитбокс карты, для рейкаста
    public ArrayList<Animation> animations3D;// анимации 3д представления
    public boolean in_market = false;//находится ли магазине
    private int texture_id; //id текстуры, для быстрой их смены без подгрузок и не уссложняя наследования
    public String main_axis;
    public float width;
    public float height;


    public Card(int texture_id) {
        GlobalEvents.gameEndedEventSigners.add(this);
        GlobalEvents.gameStartedEventSigners.add(this);
        GlobalEvents.turnComplitedEventSigners.add(this);
        GlobalEvents.turnStartedEventSigners.add(this);
        this.texture_id = texture_id;
        instance = new MyModelInstance(card_model);
        instance.transform.setTranslation(card_pos);//перемещение карты
        box = new BoundingBox();
        change_texture(3);
        animations3D = new ArrayList<>();
        instance.transform.scale(8.5f, 8.5f, 8.5f);
        instance.calculateBoundingBox(box).mul(instance.transform);//расчет хитбокса
        width = box.getDepth();
        height = box.getHeight();
        GlobalEvents.turnCompliteIntentEventSigners.add(this);
        GlobalEvents.turnComplitedEventSigners.add(this);
        GlobalEvents.turnStartedEventSigners.add(this);
        GlobalEvents.gameStartedEventSigners.add(this);
        GlobalEvents.gameEndedEventSigners.add(this);
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
        RenderController.setNow_selected_card(this);
    }

    public void doubleTouched() {
        if (Server.market_deck.contains(this) && Server.player_now.getPower_points() >= cost) {
            //покупка карты
            Server.player_now.setPower_points(Server.player_now.getPower_points() - cost);
            GameScreen.refreshPowerPoints();
            Server.player_now.trash.add(this);
            Server.market_deck.remove(this);
            animations3D.add(new Animation(instance,Server.player_now.trash_pos,1500,Server.player_now.getStandart_card_world_rotation(),Interpolation.circle,Interpolation.linear,"market to trash"));
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
        if (main_axis.equals("1")) {
            Animation animation = new Animation(instance, Server.player_now.deck_pos, Server.player_now.hand_pos, 1300, instance.getCurrentRotation().cpy(), new Vector3(0, -90, 45), Interpolation.exp5, Interpolation.circle, "to hand");
            animations3D.add(animation);
        }
        if (main_axis.equals("2")) {
            Animation animation = new Animation(instance, Server.player_now.deck_pos, Server.player_now.hand_pos, 1300, instance.getCurrentRotation().cpy(), new Vector3(0, 0, 45), Interpolation.exp5, Interpolation.circle, "to hand");
            animations3D.add(animation);
        }
        if (main_axis.equals("3")) {
            Animation animation = new Animation(instance, Server.player_now.deck_pos, Server.player_now.hand_pos, 1300, instance.getCurrentRotation().cpy(), new Vector3(180, 90, 225), Interpolation.exp5, Interpolation.circle, "to hand");
            animations3D.add(animation);
        }
        if (main_axis.equals("4")) {
            Animation animation = new Animation(instance, Server.player_now.deck_pos, Server.player_now.hand_pos, 1300, instance.getCurrentRotation().cpy(), new Vector3(360, 180, 45), Interpolation.exp5, Interpolation.circle, "to hand");
            animations3D.add(animation);
        }
    }

    public void playedFromHand() {
        Animation animation = new Animation(instance, new Vector3(Server.player_now.played_card_pos.x+ MathUtils.random(-1,1),Server.player_now.played_card_pos.y+0.1f*(Server.player_now.getHand_size()-Server.player_now.hand.size()),Server.player_now.played_card_pos.z+ MathUtils.random(-1,1)), 1500, Server.player_now.getStandart_card_world_rotation(), Interpolation.exp5In, Interpolation.pow4Out, "played from hand");
        animations3D.add(animation);
        Server.player_now.hand.remove(this);
        if (!Server.player_now.hand.isEmpty()) {
            Server.player_now.refresh_hands_positions();
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                played();
            }
        };
        GameController.timer.schedule(timerTask, 2000);
    }

    public void moveToTrash() {
        Animation animation = new Animation(instance, Server.player_now.trash_pos, 1500, Server.player_now.getStandart_card_world_rotation(), Interpolation.linear, Interpolation.smooth2, "to trash");
        animations3D.add(animation);

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
        if (main_axis.equals((String) "1") || main_axis.equals((String) "3")) {
            return new Vector3(hand_pos.x + delta, hand_pos.y, hand_pos.z + 0.001f * num);
        }
        if (main_axis.equals((String) "2") || main_axis.equals((String) "4")) {
            return new Vector3(hand_pos.x + 0.001f * num, hand_pos.y, hand_pos.z + delta);
        }
        return hand_pos;
    }


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

    public void setTouchable(boolean visible) {
        if (visible) {
            if (!RenderController.collisions.contains(this)) {
                RenderController.collisions.add(this);
            }
        } else {
            if (RenderController.collisions.contains(this)) {
                RenderController.collisions.remove(this);
            }
        }
    }

    public Vector3 update_pos() {
        //получение актуальной позиции карты
        card_pos = instance.transform.getTranslation(card_pos);
        return card_pos;
    }


    @Override
    public void turnCompliteIntent() {

    }

    @Override
    public void turnComplited() {
        setVisible(false);
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
