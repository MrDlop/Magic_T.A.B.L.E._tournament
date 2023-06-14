package com.mygdx.tablegame.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.tablegame.tools.Animation;
import com.mygdx.tablegame.game_logic.CanTouch;
import com.mygdx.tablegame.game_logic.GameScreen;
import com.mygdx.tablegame.tools.Generator;
import com.mygdx.tablegame.tools.Pair;
import com.mygdx.tablegame.game_logic.Server;
import com.mygdx.tablegame.tools.TextureStorage;
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
    public boolean is3D;
    private BoundingBox box; //хитбокс карты, для рейкаста
    public Sprite sprite; // 2д представление карты
    private static int lay_down_cards = 0;// костыль, для отслеживания количества взятых карт при взятии руки, помле устранения зависимости анимаций от каадров убрать
    public ArrayList<Generator> animations2D;// анимации 2д представления
    public ArrayList<Animation> animations3D;// анимации 3д представления
    private float max_rotate_angel = 8;// поворот карт в руке(максимальный), знвчение подобранно в результате тестов
    private float inHandX;//позиция карты в руке
    private float inHandY;
    private float inHand_rotation;//поворот карты в руке
    private Vector3 temp_camera_pos;//для передачи данных между анимациями , после ввода анимаций зависящих отвремени убрать
    private Vector3 temp_on_table_pos;
    public boolean in_market = false;//находится ли магазине
    private int texture_id; //id текстуры, для быстрой их смены без подгрузок и не уссложняя наследования
    public Vector3 rot_angles;//эйлеровы углы 3д карты, пока не используется, необходимо доработать

    public Card(int texture_id) {
        this.texture_id = texture_id;
        sprite = new Sprite(TextureStorage.textures2d[texture_id][0], TextureStorage.textures2d[texture_id][0].getWidth(), TextureStorage.textures2d[texture_id][0].getHeight());
        sprite.setCenter(TextureStorage.textures2d[texture_id][0].getWidth() / 2, TextureStorage.textures2d[texture_id][0].getHeight() / 2);
        sprite.setOrigin(sprite.getWidth(), sprite.getHeight());//установка центра вращения
        change_texture(1);// изменение текстуры
        is3D = true;
        instance = new ModelInstance(card_model);
        instance.transform.setTranslation(card_pos);//перемещение карты
        box = new BoundingBox();
        instance.transform.setToScaling(8.5f, 8.5f, 8.5f);//увеличение карты, #TODO заменить фактическим увеличением и поворотом исходной модели
        instance.transform.rotate(1, 0, 0, 180);
        change_texture(3);
        animations2D = new ArrayList<>();
        animations3D = new ArrayList<>();
        instance.calculateBoundingBox(box).mul(instance.transform);//расчет хитбокса
        rot_angles = new Vector3(0, 0, 0);
    }

    public float getMax_rotate_angel() {
        return max_rotate_angel;
    }

    public float getInHandX() {
        return inHandX;
    }

    public float getInHandY() {
        return inHandY;
    }

    public float getInHand_rotation() {
        return inHand_rotation;
    }


    public void change_texture(int type) {
        //метод для смены текстур, работающий нормально с наследованием
        if (type == 1) {
            sprite.setTexture(TextureStorage.textures2d[texture_id][0]);
        }
        if (type == 2) {
            sprite.setTexture(TextureStorage.textures2d[texture_id][1]);
        }
        if (type == 3) {
            instance.materials.get(0).set(TextureStorage.textures3d[texture_id][0]);
        }
        if (type == 4) {
            instance.materials.get(0).set(TextureStorage.textures3d[texture_id][1]);
        }
    }

    public void played() {
    }


    public Generator<Pair<Vector2, Float>> doAnimation2D(final Vector2 startPos, final Vector2 endPos, final int frames, final float rotation, String id) {
        //объект для пермещения 2д представления карты
        Generator<Pair<Vector2, Float>> generator = new Generator<Pair<Vector2, Float>>() {
            @Override
            public void run() throws InterruptedException {
                //расчет перемещения карты за кадр
                Vector2 nowPos = new Vector2(startPos);
                float rotatePframe = rotation / frames;
                float distanceXpFrame = Math.abs(endPos.x - startPos.x) / frames;
                float distanceYpFrame = Math.abs(endPos.y - startPos.y) / frames;
                int directionX = 1, directionY = 1;
                if (endPos.x < startPos.x) directionX = -1;
                if (endPos.y < startPos.y) directionY = -1;
                Pair<Vector2, Float> answ = new Pair<>(nowPos, rotatePframe);
                for (int i = 1; i <= frames; i++) {
                    answ.first.set(startPos.x + distanceXpFrame * i * directionX, startPos.y + distanceYpFrame * i * directionY);
                    yield (answ);//каждый раз при повторном вызове функция вызывается с этого места
                }
            }
        };
        generator.id = id;
        return generator;
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

    public Rectangle getSpriteHitBox() {
        return sprite.getBoundingRectangle();
    }

    public void touched() {
        selected();
    }

    public void doubleTouched() {

        if (Server.market_deck.contains(this) && Server.player_now.getPower_points() >= cost) {
            //покупка карты
            Server.player_now.setPower_points(Server.player_now.getPower_points() - cost);
            GameScreen.getPlayer_UI_names()[Server.player_now.player_number] = Server.player_now.name + "`s power points  : " + Server.player_now.getPower_points();
            animations3D.add(new Animation(update_pos(), Server.player_now.trash_pos, 2000, "market_to_trash"));
        }
    }

    public void sprite_touched() {
        selected();
    }

    public void sprite_doubleTouched() {
        //разыгрывание карты(начало анимации
        convertTo3D(Server.player_now.camera.position, new Vector3(Server.player_now.played_card_pos.x + MathUtils.random(-1.2f, 1.2f), Server.player_now.played_card_pos.y, Server.player_now.played_card_pos.z + MathUtils.random(-1.2f, 1.2f)));
    }

    public void convertTo3D(Vector3 camera_pos, Vector3 target_pos) {
        //перевод карты в 3д
        temp_camera_pos = camera_pos;
        temp_on_table_pos = target_pos;
        animations2D.add(doAnimation2D(new Vector2(sprite.getX(), sprite.getY()), new Vector2(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2, Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2), 30, 0 - sprite.getRotation(), "convert3D"));
    }

    public void convertTo2D(Vector3 camera_pos) {
        //перевод карты в 2д
        if (!CanTouch.renderable_3d.contains(this)) CanTouch.renderable_3d.add(this);
        animations3D.add(new Animation(update_pos(), camera_pos, 2000, new Vector3(0, 0, 0), new Vector3(90, 0, 0), "convert2D"));
        //rot_angles.set(rot_angles.x+90,rot_angles.y,rot_angles.z);
    }


    public void calculate_inHand_pos(int hand_size, int index, boolean set) {
        //вычисление положения карты в руке, на основе размера руки номера карты в ней
        int n;
        float x = sprite.getX(), y = sprite.getY(), rotation = 0;
        if (hand_size % 2 == 1) {
            n = -hand_size / 2 + index;
            x = Gdx.graphics.getWidth() / 2 + sprite.getWidth() * n;
            if (index > 0) x -= sprite.getWidth() / 5 * index;
            y = -sprite.getHeight() / 4 - Math.abs(sprite.getHeight() / 8 * n);
            if (hand_size == 1) hand_size = 2;
            rotation = max_rotate_angel / (hand_size / 2) * -n;
            if (n < 0) sprite.setOrigin(0, 0);
            if (n > 0) sprite.setOrigin(sprite.getWidth(), 0);
        } else {
            if (index < hand_size / 2) n = -hand_size / 2 + index;
            else n = index - hand_size / 2 + 1;
            int n1_crutch_edition = -hand_size / 2 + index;
            x = Gdx.graphics.getWidth() / 2 + sprite.getWidth() * n1_crutch_edition;
            if (index > 0) x -= sprite.getWidth() / 5 * index;
            y = -sprite.getHeight() / 4 - Math.abs(sprite.getHeight() / 8 * n);
            if (hand_size == 1) hand_size = 2;
            rotation = max_rotate_angel / (hand_size / 2) * -n;
            if (n < 0) sprite.setOrigin(0, 0);
            if (n > 0) sprite.setOrigin(sprite.getWidth(), 0);
        }
        inHandX = x;
        inHandY = y;
        inHand_rotation = rotation;
        if (set) {
            sprite.setPosition(x, y);
            sprite.rotate(rotation);
        }
        //sprite.setOriginCenter();
    }

    public void animation3Dend(String animation_id) {
        //отслеживание окончания анимаций, после полного перехода на анимации зависящие от времени заменить таймером
        switch (animation_id) {
            case ("convert2D"): {
                CanTouch.renderable_3d.remove(this);
                CanTouch.collisions.remove(this);
                CanTouch.renderable_2d.add(this);
                if (!CanTouch.sprite_collisions.contains(this))
                    CanTouch.sprite_collisions.add(this);
                is3D = false;
                break;
            }
            case ("played_from_hand"): {
                if (Server.turn_end_button_pressed) {
                    CanTouch.renderable_3d.remove(this);
                    CanTouch.collisions.remove(this);
                    Server.player_now.trash.add(this);
                } else {
                    if (!CanTouch.collisions.contains(this)) CanTouch.collisions.add(this);
                    played();
                    Server.player_now.on_table_cards.add(this);
                }
                break;
            }
            case ("to_market_deck"): {
                if (!CanTouch.collisions.contains(this)) CanTouch.collisions.add(this);
                in_market = true;
                break;
            }
            case ("to_trash_end"): {
                CanTouch.renderable_3d.remove(this);
                CanTouch.collisions.remove(this);
                Server.player_now.on_table_cards.remove(this);
                Server.player_now.trash.add(this);
                if (Server.player_now.hand.isEmpty()) Server.player_now.getHand();
                break;
            }
            case ("market_to_trash"): {
                CanTouch.renderable_3d.remove(this);
                CanTouch.collisions.remove(this);
                Server.market_deck.remove(this);
                Server.player_now.trash.add(this);
            }
        }
    }

    public void animation2Dend(String animation_id) {
        //отслеживание окончания анимаций, после полного перехода на анимации зависящие от времени заменить таймером
        switch (animation_id) {
            case ("convert3D"): {
                CanTouch.renderable_2d.remove(this);
                CanTouch.sprite_collisions.remove(this);
                CanTouch.renderable_3d.add(this);
                animations3D.add(new Animation(temp_camera_pos, temp_on_table_pos, 2000, new Vector3(0, 0, 0), new Vector3(-90, 0, 0), "played_from_hand"));
                Server.player_now.hand.remove(this);
                Server.player_now.refresh_hands_positions();
                is3D = true;
                break;
            }
            case ("laying_out_card"): {
                if (Server.turn_end_button_pressed) {
                    for (Card card : Server.player_now.hand) {
                        CanTouch.renderable_2d.remove(card);
                        CanTouch.sprite_collisions.remove(card);
                    }
                    if (lay_down_cards >= 4) {
                        Server.turn_started();
                        lay_down_cards = 0;
                    } else lay_down_cards++;
                }
                Server.player_now.refresh_hands_positions();
                break;
            }
        }
    }

    public void non_selected() {
        //когда карта не выбрана
        if (is3D) {
            change_texture(3);
        } else change_texture(1);
        animations2D.add(doAnimation2D(new Vector2(sprite.getX(), sprite.getY()), new Vector2(inHandX, inHandY), 15, inHand_rotation, "non_selected"));
    }

    public void selected() {
        //когда карта выбрана
        if (is3D) change_texture(4);
        else change_texture(2);
        if (!(CanTouch.now_selected_card == this)) {
            CanTouch.setNow_selected_card(this);
            animations2D.add(doAnimation2D(new Vector2(sprite.getX(), sprite.getY()), new Vector2(sprite.getX(), 0), 15, 0 - sprite.getRotation(), "selected"));
        }
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
