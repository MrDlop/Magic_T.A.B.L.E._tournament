package com.mygdx.tablegame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.tablegame.tools.Animation;
import com.mygdx.tablegame.game_logic.RenderController;
import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.game_logic.Server;
import com.mygdx.tablegame.tools.AssetStorage;
import com.mygdx.tablegame.game_logic.Touchable;


import java.util.ArrayList;
//общий класс карты, со всеми необходимыми картами, от него не=аследуются все остальные карты

public class Card extends Touchable {
    public static String ID;
    final static Model card_model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("Card_model1.g3dj"));// модель карты
    public Vector3 card_pos = new Vector3(0, 0, 0);//позиция карты
    public ModelInstance instance; //экземпляр модели
    public Integer cost = 0; // цена карты
    public Integer power_points;// очки мощи
    public Integer win_points;//победные очки
    private BoundingBox box; //хитбокс карты, для рейкаста
    private static int lay_down_cards = 0;// костыль, для отслеживания количества взятых карт при взятии руки, помле устранения зависимости анимаций от каадров убрать
    public ArrayList<Animation> animations3D;// анимации 3д представления
    private Vector3 temp_camera_pos;//для передачи данных между анимациями , после ввода анимаций зависящих от времени убрать
    private Vector3 temp_on_table_pos;
    public boolean in_market = false;//находится ли магазине
    private int texture_id; //id текстуры, для быстрой их смены без подгрузок и не уссложняя наследования
    public Vector3 rot_angles;//эйлеровы углы 3д карты, пока не используется, необходимо доработать

    public Card(int texture_id) {
        this.texture_id = texture_id;
        change_texture(1);// изменение текстуры
        instance = new ModelInstance(card_model);
        instance.transform.setTranslation(card_pos);//перемещение карты
        box = new BoundingBox();
        instance.transform.setToScaling(8.5f, 8.5f, 8.5f);//увеличение карты, #TODO заменить фактическим увеличением и поворотом исходной модели
        instance.transform.rotate(1, 0, 0, 180);
        change_texture(3);
        animations3D = new ArrayList<>();
        instance.calculateBoundingBox(box).mul(instance.transform);//расчет хитбокса
        rot_angles = new Vector3(0, 0, 0);
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
        //переопределение метода из радительского класса(вычисление актуального хитбокса
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
            animations3D.add(new Animation(update_pos(), Server.player_now.trash_pos,2000, Interpolation.linear,Interpolation.linear,  "market_to_trash"));
        }
    }


    public void animationEnd(String animation_id) {
        //отслеживание окончания анимаций, после полного перехода на анимации зависящие от времени заменить таймером
        switch (animation_id) {
            case ("played_from_hand"): {
                if (Server.turn_end_button_pressed) {
                    RenderController.renderable_3d.remove(this);
                    RenderController.collisions.remove(this);
                    Server.player_now.trash.add(this);
                } else {
                    if (!RenderController.collisions.contains(this)) RenderController.collisions.add(this);
                    played();
                    Server.player_now.on_table_cards.add(this);
                }
                break;
            }
            case ("to_market_deck"): {
                if (!RenderController.collisions.contains(this)) RenderController.collisions.add(this);
                in_market = true;
                break;
            }
            case ("to_trash_end"): {
                RenderController.renderable_3d.remove(this);
                RenderController.collisions.remove(this);
                Server.player_now.on_table_cards.remove(this);
                Server.player_now.trash.add(this);
                //if (Server.player_now.hand.isEmpty()) Server.player_now.getHand();
                break;
            }
            case ("market_to_trash"): {
                RenderController.renderable_3d.remove(this);
                RenderController.collisions.remove(this);
                Server.market_deck.remove(this);
                Server.player_now.trash.add(this);
            }
        }
    }

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

    public Vector3 update_pos() {
        //получение актуальной позиции карты
        instance.calculateBoundingBox(box).mul(instance.transform);
        box.getCenter(card_pos);
        return card_pos;
    }
    /*public  void rotate_card(Vector3 axis,float angle){
        instance.transform.rotate(axis,angle);
        if(axis.x!=0) rot_angles.set(rot_angles.x+angle,rot_angles.y,rot_angles.z);
        if(axis.y!=0) rot_angles.set(rot_angles.x,rot_angles.y+angle,rot_angles.z);
        if(axis.z!=0) rot_angles.set(rot_angles.x,rot_angles.y,rot_angles.z+angle);
    }*/
}
