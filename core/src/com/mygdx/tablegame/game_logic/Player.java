package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.tablegame.tools.Animation;
import com.mygdx.tablegame.tools.CameraAnimation;
import com.mygdx.tablegame.tools.ElementUI;
import com.mygdx.tablegame.tools.MyCameraInputController;
import com.mygdx.tablegame.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimerTask;
// класс игрока, возможно придется создавать отдельный класс игрока под сетевую игру, в связи с возникающими противоречиями

public class Player implements EventReceiver {
    public final Integer player_number;// порядковый номер, используется для идентификации
    public PerspectiveCamera camera;// камера, у каждого игрока своя
    public MyCameraInputController inputController; // может быть разным, в зависимости от настроек(в разработке)
    public int id;
    private Integer health;// здоровье
    private Integer power_points;//очки мощи
    public ElementUI[] health_bar;// отображение здоровья на экране
    public Integer win_points;// победные очки
    public final Integer normal_hand_size; // стандартный размер руки
    private Integer hand_size; // реальный размер руки
    private Integer armor = 0; //броня
    public ElementUI[] armor_bar;// отображение брони на экране
    public ArrayList<Card> deck;//стопка добора карт, возможно лучше сделать private
    public Vector3 deck_pos;
    public ArrayList<Card> hand; //текущая рука
    public ArrayList<Card> trash; //сброс
    public ArrayList<Card> on_table_cards; // разыгранные карты(не используется? уточнить правила)
    public Vector3 trash_pos;
    public Vector3 played_card_pos;
    public Vector3 hand_pos;
    public Vector3 shop_pos;
    public Vector3 camera_on_played_card_pos;
    public Vector3 camera_on_hand_pos;
    public Vector3 camera_on_shop_pos;
    public Vector3 camera_on_player1_played_cards_pos;
    public Vector3 player1_played_cards_pos;
    public Vector3 camera_on_player2_played_cards_pos;
    public Vector3 player2_played_cards_pos;
    public Vector3 camera_on_player3_played_cards_pos;
    public Vector3 player3_played_cards_pos;
    public String name; // имя игрока
    public ArrayList<CameraAnimation> cameraAnimations = new ArrayList<>();
    public HashMap<Integer, Vector3[]> camera_positions_for_animation = new HashMap<>();
    public Integer current_camera_pos_id = 0;
    public Integer max_camera_animation_positions = 3;
    private Vector3 standart_card_world_rotation; // модификатор поворота карт, необходимо, т.к. при анимации поворот идет вокруг локальных осей карты
    public int rot_modifier;
    public String main_axis;
    public boolean inited=false;

    public Player(int num) {
        Gdx.app.setLogLevel(Application.LOG_ERROR);// для логирования ошибок
        player_number = num;
        camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // дальность обзора камеры
        camera.near = 0.05f;
        camera.far = 5000;
        health = 20;
        power_points = 0;
        win_points = 0;
        normal_hand_size = 5;
        hand_size = normal_hand_size;
        deck_pos = new Vector3();
        trash_pos = new Vector3();
        played_card_pos = new Vector3();
        shop_pos = Server.market_deck_pos;
        deck = new ArrayList<>();
        hand = new ArrayList<>();
        trash = new ArrayList<>();
        on_table_cards = new ArrayList<>();
        inputController = new MyCameraInputController(camera);
        inputController.target=new Vector3(0,0,0);
        health_bar = new ElementUI[health / 2];
        armor_bar = new ElementUI[10];
        //задание позиций элементам экранного интерфейса
        for (int i = 0; i < health_bar.length; i++) {
            health_bar[i] = new ElementUI(0, new BitmapFont());
            health_bar[i].sprite.setPosition(30 + health_bar[i].sprite.getWidth() * i, 50 + health_bar[i].sprite.getHeight() * player_number + 45 * player_number);
        }
        for (int i = 0; i < health_bar.length; i++) {
            armor_bar[i] = new ElementUI(1, new BitmapFont());
            armor_bar[i].change_texture(-1);
            armor_bar[i].sprite.setPosition(health_bar[0].sprite.getWidth() * health_bar.length + 30 + armor_bar[i].sprite.getHeight() * i, 50 + armor_bar[i].sprite.getHeight() * player_number + 45 * player_number);
        }

        // важно, при отсутсвии к камере не применятся изменения
    }

    public Integer getHealth() {
        return health;
    }

    public Integer getPower_points() {
        return power_points;
    }

    public Integer getHand_size() {
        return hand_size;
    }

    public Integer getArmor() {
        return armor;
    }

    public Vector3 getStandart_card_world_rotation() {
        return standart_card_world_rotation;
    }

    public Vector3[] getNext_camera_pos() {
        current_camera_pos_id += 1;
        return camera_positions_for_animation.get(current_camera_pos_id % max_camera_animation_positions);
    }

    public Vector3[] getPrev_camera_pos() {
        current_camera_pos_id -= 1;
        return camera_positions_for_animation.get(Math.abs(current_camera_pos_id) % max_camera_animation_positions);
    }

    public Vector3[] getCurrent_camera_pos() {
        return camera_positions_for_animation.get(Math.abs(current_camera_pos_id) % max_camera_animation_positions);
    }

    public void setPower_points(Integer power_points) {
        // не может быть отрицательным, иначе может вызвать софтлок
        if (power_points >= 0)
            this.power_points = power_points;
        else Gdx.app.log(GameController.log_tag, "Player.power_points must be >=0");
    }

    public void setHand_size(Integer hand_size) {
        if (hand_size >= 0) this.hand_size = hand_size;
        else Gdx.app.log(GameController.log_tag, "Player.hand_size must be >=0");
    }

    public void setStandart_card_world_rotation(Vector3 standart_card_world_rotation) {
        this.standart_card_world_rotation = standart_card_world_rotation;
    }

    public void updateCameraAnimations() {
        if (!cameraAnimations.isEmpty()) {

            CameraAnimation cameraAnimation = cameraAnimations.get(0);
            if (cameraAnimation.start_time == -1) {
                cameraAnimation.startAnimation();
            }
            if (cameraAnimation.duration < TimeUtils.timeSinceMillis(cameraAnimation.start_time)) {
                cameraAnimations.remove(0);
                return;
            } else {
                cameraAnimation.updateState();
            }
        }
    }

    public void player_init() {
        inited=true;
        camera_positions_for_animation.put(0, new Vector3[]{camera_on_hand_pos.cpy(), hand_pos.cpy()});
        camera_positions_for_animation.put(1, new Vector3[]{camera_on_shop_pos.cpy(), shop_pos.cpy()});
        camera_positions_for_animation.put(2, new Vector3[]{camera_on_played_card_pos.cpy(), played_card_pos.cpy()});
        if (camera_on_player1_played_cards_pos != null) {
            camera_positions_for_animation.put(4, new Vector3[]{camera_on_player1_played_cards_pos.cpy(), player1_played_cards_pos});
        }
        if (camera_on_player2_played_cards_pos != null) {
            camera_positions_for_animation.put(5, new Vector3[]{camera_on_player2_played_cards_pos.cpy(), player2_played_cards_pos});
        }
        if (camera_on_player3_played_cards_pos != null) {
            camera_positions_for_animation.put(6, new Vector3[]{camera_on_player3_played_cards_pos.cpy(), player3_played_cards_pos});
        }
        current_camera_pos_id = 0;
        camera.position.set(camera_on_hand_pos);
        camera.lookAt(hand_pos);
        camera.update();
        if (player_number == 0 || player_number == 2) {
            rot_modifier = 1;
        } else {
            rot_modifier = -1;
        }
        GlobalEvents.turnCompliteIntentEventSigners.add(this);
        GlobalEvents.turnComplitedEventSigners.add(this);
        GlobalEvents.turnStartedEventSigners.add(this);
        GlobalEvents.gameStartedEventSigners.add(this);
        GlobalEvents.gameEndedEventSigners.add(this);
        for (Card card : deck) {
            card.main_axis = main_axis;
        }
    }

    public void getHand() {
        // получение карт в руку
        hand.clear();// не должно возникать случаев использования, но пусть будет как мера безопасности
        deck.trimToSize();
        if (deck.size() < hand_size) {
            Collections.shuffle(trash);
            deck.addAll(trash);
            trash.clear();
        }
        for (int i = 0; i < hand_size; i++) {
            hand.add(deck.get(i));
        }
        for (int i = 0; i < hand_size; i++) {
            deck.remove(0);
        }
        for (int i = 0; i < hand_size; i++) {
            final int k = i;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Card card = hand.get(k);
                    card.moveToHandLocation();
                    Vector3 tmp = card.calculateInHandPos(k, hand_size, hand_pos);
                    card.animations3D.add(new Animation(card.getModelInstance(), tmp, 300, Interpolation.linear, "to hand pos"));
                }
            };
            GameController.timer.schedule(timerTask, 300L * (i + 1));
        }
    }
    public Card getCard(){
        if(deck.isEmpty()){
            Collections.shuffle(trash);
            deck.addAll(trash);
            trash.clear();
        }
        Card card=deck.get(0);
        deck.remove(0);
        return card;
    }
    public void getCardToHand(){
        if(deck.isEmpty()){
            Collections.shuffle(trash);
            deck.addAll(trash);
            trash.clear();
        }
        Card card=deck.get(0);
        deck.remove(0);
        hand.add(card);
        card.moveToHandLocation();
        refresh_hands_positions();
    }

    public void refresh_hands_positions() {
        //вычисление актуальных позиций карт в руке
        if (hand.isEmpty()) {
            return;
        }
        for (int i = 0; i < hand.size(); i++) {
            hand.trimToSize();
            Card card = hand.get(i);
            Vector3 tmp = card.calculateInHandPos(i, hand.size(), hand_pos);
            card.animations3D.add(new Animation(card.getModelInstance(), tmp, 1000, Interpolation.pow2InInverse, "refresh hand pos"));
        }
    }

    public void refresh_health(int delta) {
        //обновление экранного отображения здоровья после его обновления
        //#TODO найти нормальные ассеты сердец, для устранения костыльных и больших вычислений
        // вычисления победных очков в случае, если игрок умер от атаки
        if (health + delta <= 0) {
            for (int i = 0; i < Server.players_count; i++) {
                if (!Server.players[i].deck.isEmpty()) {
                    for (Card card : Server.players[i].deck) {
                        Server.players[i].win_points += card.win_points;
                    }
                }
                if (!Server.players[i].deck.isEmpty()) {
                    for (Card card : Server.players[i].trash) {
                        Server.players[i].win_points += card.win_points;
                    }
                }
                if (!Server.players[i].deck.isEmpty()) {
                    for (Card card : Server.players[i].hand) {
                        Server.players[i].win_points += card.win_points;
                    }
                }
            }
            GameController.state = GameState.END;//игрок умер, игра кончилась
            return;
        }
        int n = health / 2, k = (health + delta) / 2;
        if (health % 2 == 0) n--;
        if ((health + delta) % 2 == 0) k--;
        //вычисления при наличии брони
        if (delta < 0) {
            if (armor > 0 && delta <= -armor) {
                //вся
                for (int i = 0; i < armor; i++) {
                    armor_bar[i].change_texture(-1);
                }
                delta += armor;
                armor = 0;
            }
            if (armor > 0 && delta > -armor) {
                for (int i = armor; i > armor + delta; i--) {
                    armor_bar[i].change_texture(-1);//брони нет
                }
                delta = 0;
                armor += delta;
            }
            for (int i = k; i <= n; i++) {
                health_bar[i].change_texture(-1);//сердце отсутсвует
            }
            if (k % 2 == 1) health_bar[k].change_texture(2);
            health += delta;
        }
        if (delta > 0 && health <= 20) {
            if (k > 10) k = 10;
            for (int i = k; i <= n; i++) {
                health_bar[i].change_texture(1);//половина сердца
            }
            if (k % 2 == 1) health_bar[k].change_texture(2);// полное сердце
            health += delta;
        }
    }

    public void getArmor(int arm) {
        int n = armor, k = armor + arm;
        for (int i = n; i <= k; i++) {
            armor_bar[i].change_texture(1);//броня есть
        }
        armor += arm;
    }

    @Override
    public void turnCompliteIntent() {
        if (Server.player_now == this) {
            for (int i = 0; i < hand.size(); i++) {
                hand.get(i).moveToTrash();
                trash.add(hand.get(i));
            }
            for (int i = 0; i < on_table_cards.size(); i++) {
                on_table_cards.get(i).moveToTrash();
                trash.add(on_table_cards.get(i));
            }
            on_table_cards.clear();
            hand.clear();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Server.player_now.getHand();
                }
            };
            GameController.timer.schedule(timerTask, 2500);
        }
    }

    @Override
    public void turnComplited() {

    }

    @Override
    public void turnStarted() {
        if (Server.player_now == this) {
            for (Card c : hand) {
                c.setVisible(true);
            }
        }
    }

    @Override
    public void gameEnded() {

    }

    @Override
    public void gameStarted() {

    }
}


