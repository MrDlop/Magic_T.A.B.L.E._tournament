package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.tablegame.tools.ElementUI;
import com.mygdx.tablegame.tools.MyCameraInputController;
import com.mygdx.tablegame.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
// класс игрока, возможно придется создавать отдельный класс игрока под сетевую игру, в связи с возникающими противоречиями

public class Player {
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
    public String name; // имя игрока
    private int card_rot_modifier; // модификатор поворота карт, необходимо, т.к. при анимации поворот идет вокруг локальных осей карты



    public Player(Vector3 pos, int num) {
        Gdx.app.setLogLevel(Application.LOG_ERROR);// для логирования ошибок
        player_number = num;
        camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // дальность обзора камеры
        camera.near = 1;
        camera.far = 2000;
        camera.position.set(pos);
        camera.lookAt(0, 0, 0);
        inputController = new MyCameraInputController(camera);
        camera.update();// важно, при отсутсвии к камере не применятся изменения
        System.out.println(camera.direction);
        health = 20;
        power_points = 0;
        win_points = 0;
        normal_hand_size = 5;
        hand_size = normal_hand_size;
        deck_pos = new Vector3();
        trash_pos = new Vector3();
        played_card_pos = new Vector3();
        deck = new ArrayList<>();
        hand = new ArrayList<>();
        trash = new ArrayList<>();
        on_table_cards = new ArrayList<>();
        health_bar = new ElementUI[health / 2];
        armor_bar = new ElementUI[10];
        //задание позиций элементам экранного интерфейса
        for (int i = 0; i < health_bar.length; i++) {
            health_bar[i] = new ElementUI(0);
            health_bar[i].sprite.setPosition(30 + health_bar[i].sprite.getWidth() * i, 50 + health_bar[i].sprite.getHeight() * player_number + 45 * player_number);
        }
        for (int i = 0; i < health_bar.length; i++) {
            armor_bar[i] = new ElementUI(1);
            armor_bar[i].change_texture(-1);
            armor_bar[i].sprite.setPosition(health_bar[0].sprite.getWidth() * health_bar.length + 30 + armor_bar[i].sprite.getHeight() * i, 50 + armor_bar[i].sprite.getHeight() * player_number + 45 * player_number);
        }
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

    public int getCard_rot_modifier() {
        return card_rot_modifier;
    }

    public void setPower_points(Integer power_points) {
        // не может быть отрицательным, иначе может вызвать софлок
        if(power_points>=0)
            this.power_points = power_points;
        else Gdx.app.log(GameController.log_tag,"Player.power_points must be >=0");
    }

    public void setHand_size(Integer hand_size) {
        if(hand_size>=0) this.hand_size = hand_size;
        else Gdx.app.log(GameController.log_tag,"Player.hand_size must be >=0");
    }

    public void setCard_rot_modifier(int card_rot_modifier) {
        this.card_rot_modifier = card_rot_modifier%360;
    }


    public void player_init() {
        Gdx.input.setInputProcessor(inputController);
    }

//    public void getHand() {
//        // получение карт в руку
//        hand.clear();// не должно возникать случаев использования, но пусть будет как мера безопасности
//        deck.trimToSize();
//        if (deck.size() < hand_size) {
//            Collections.shuffle(trash);
//            deck.addAll(trash);
//            trash.clear();
//        }
//        for (int i = 0; i < hand_size; i++) {
//            hand.add(deck.get(i));
//            if (!CanTouch.renderable_3d.contains(deck.get(i)))
//                CanTouch.renderable_3d.add(deck.get(i));// добавление карт в список отрисовываемых 3д объектов
//        }
//        for (int i = 0; i < hand_size; i++) {
//            deck.remove(0);
//        }
//        for (int i = 0; i < hand_size; i++) {
//            hand.get(i).calculate_inHand_pos(hand_size, i, false);
//            hand.get(i).animations2D.clear();//не должно возникать случаев использования,но пусть будет как мера безопасности
//            hand.get(i).convertTo2D(camera.position);// перевод карт в состояние 2д,после создания нормальных анимаций камеры удалить
//            hand.get(i).animations2D.add(hand.get(i).doAnimation2D(new Vector2(Gdx.graphics.getWidth() / 2f, 600), new Vector2(hand.get(i).getInHandX(), hand.get(i).getInHandY()), 20, hand.get(i).getInHand_rotation() - hand.get(i).sprite.getRotation(), "laying_out_card"));// перовод карт в позиции руки
//        }
//    }

//    public void getCard() {
//        // получение одной карты
//        if (deck.isEmpty()) {
//            deck.addAll(trash);
//            Collections.shuffle(deck);
//            trash.clear();
//        }
//        Card card = deck.get(0);
//        deck.remove(0);
//        card.setCardPos(deck_pos);
//        CanTouch.renderable_3d.add(card);
//        card.convertTo2D(camera.position);
//        hand.add(card);
//        for (int i = 0; i < hand_size; i++) {
//            hand.get(i).calculate_inHand_pos(hand_size, i, false);
//
//        }
//        card.animations2D.add(card.doAnimation2D(new Vector2(Gdx.graphics.getWidth() / 2f, 600), new Vector2(card.getInHandX(), card.getInHandY()), 20, card.getInHand_rotation() - card.sprite.getRotation(), "laying_out_card"));
//    }

//    public void refresh_hands_positions() {
//        //вычисление актуальных позиций карт в руке
//        for (int i = 0; i < hand.size(); i++) {
//            hand.trimToSize();
//            Card card = hand.get(i);
//            card.calculate_inHand_pos(hand.size(), i, false);
//            card.animations2D.add(card.doAnimation2D(new Vector2(card.sprite.getX(), card.sprite.getY()), new Vector2(card.getInHandX(), card.getInHandY()), 10, card.getInHand_rotation() - card.sprite.getRotation(), "relocate_after_discard"));
//        }
//    }

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
}


