package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.tablegame.cards.Card;
import com.mygdx.tablegame.cards.Crown;
import com.mygdx.tablegame.cards.Fire_ball;
import com.mygdx.tablegame.cards.Magic_dog;
import com.mygdx.tablegame.cards.Protection_amulet;
import com.mygdx.tablegame.cards.Pshik;
import com.mygdx.tablegame.cards.SmallKnight;
import com.mygdx.tablegame.cards.SpellBook;
import com.mygdx.tablegame.cards.Summon;
import com.mygdx.tablegame.cards.SunFaced;
import com.mygdx.tablegame.cards.Znak;
import com.mygdx.tablegame.tools.Animation;

import java.util.ArrayList;
import java.util.Collections;

// класс для построения взаимодействий между другими классами и логики игровых ходов, несмотря на название, пока не имеет отношения к сетевой игре
public class Server {
    private static ArrayList<Card> main_deck = new ArrayList<>(); // основная колода, откуда берутся карты для пополнения магазина
    public static Vector3 main_deck_pos = new Vector3(-2.5f, 30.5f, 0);
    private static ArrayList<Card> legend_deck = new ArrayList<>();// пока не реализованно, не используется
    public static Vector3 legend_deck_pos = new Vector3(2.5f, 30.5f, 0);
    private static ArrayList<Card> destroyed_deck = new ArrayList<>();// пока не используется
    public static ArrayList<Card> market_deck = new ArrayList<>();// магазин
    public static Vector3 market_deck_pos = new Vector3(-9.7f, 30, 8);
    public static Boolean turn_end_button_pressed = false;// завершен ли ход(лучше придумать более красивое решение)
    public static Player[] players;// игроки
    public static Player player_now;// игрок, который сейчас ходит
    public static Player prev_player;
    private static int turns_lasts = 0;// число прошедших со старта ходов
    public static int players_count = 0;// количество игроков
    public static Vector3 actual_card_rotation = new Vector3(0, 0, 0);// для доворота новых карт, дополняющих магазин

    public static void server_init(ArrayList<String> names) {// лучше сделать конструктором или переделать класс в static
        // пока положения задаются вручную #TODO добавить автоматический расчет положения камер и игроков
        players = new Player[players_count];
        if (players_count == 2) {
            Player player = new Player(new Vector3(0, 50, 40), 0);
            player.deck_pos = new Vector3(13, 29.9f, 28);//  положение колоды на столе
            player.trash_pos = new Vector3(18, 29.7f, 28);
            player.played_card_pos = new Vector3(0, 30, 25);
            players[0] = player;
            Player player1 = new Player(new Vector3(0, 50, -40), 1);
            player1.deck_pos = new Vector3(-13, 29.9f, -28);
            player1.trash_pos = new Vector3(-18, 29.7f, -28);
            player1.played_card_pos = new Vector3(0, 30, -25);
            player1.setCard_rot_modifier(180);
            players[1] = player1;
        }
        if (players_count == 3) {
            Player player = new Player(new Vector3(0, 50, 40), 0);
            player.deck_pos = new Vector3(13, 29.9f, 28);
            player.trash_pos = new Vector3(18, 29.7f, 28);
            player.played_card_pos = new Vector3(0, 30, 25);
            player.setCard_rot_modifier(0);
            players[0] = player;
            Player player1 = new Player(new Vector3(30, 50, 0), 1);
            player1.deck_pos = new Vector3(18, 29.9f, -10);
            player1.trash_pos = new Vector3(18, 29.7f, -15);
            player1.played_card_pos = new Vector3(19, 30, -3);
            player1.setCard_rot_modifier(-90);
            players[1] = player1;
            Player player2 = new Player(new Vector3(0, 50, -40), 2);
            player2.deck_pos = new Vector3(-13, 29.9f, -28);
            player2.trash_pos = new Vector3(-18, 29.7f, -28);
            player2.played_card_pos = new Vector3(0, 30, -25);
            player2.setCard_rot_modifier(180);
            players[2] = player2;

        }
        if (players_count == 4) {
            Player player = new Player(new Vector3(0, 50, 40), 0);
            player.deck_pos = new Vector3(13, 29.9f, 28);
            player.trash_pos = new Vector3(18, 29.7f, 28);
            player.played_card_pos = new Vector3(0, 30, 25);
            players[0] = player;
            Player player1 = new Player(new Vector3(30, 50, 0), 1);
            player1.deck_pos = new Vector3(18, 29.9f, -10);
            player1.trash_pos = new Vector3(18, 29.7f, -15);
            player1.played_card_pos = new Vector3(19, 30, -8);
            player1.setCard_rot_modifier(-90);
            players[1] = player1;
            Player player2 = new Player(new Vector3(0, 50, -40), 2);
            player2.deck_pos = new Vector3(-13, 29.9f, -28);
            player2.trash_pos = new Vector3(-18, 29.7f, -28);
            player2.played_card_pos = new Vector3(0, 30, -25);
            player2.setCard_rot_modifier(-180);
            players[2] = player2;
            Player player3 = new Player(new Vector3(-30, 50, 0), 3);
            player3.deck_pos = new Vector3(-18, 29.9f, 10);
            player3.trash_pos = new Vector3(-18, 29.7f, 15);
            player3.played_card_pos = new Vector3(-19, 30, 8);
            player3.setCard_rot_modifier(-270);
            players[3] = player3;
        }
        // раздача карт игрокам и в колоды #TODO сделать файл для хранения количества и видов карт
        for (int i = 0; i < players_count; i++) {
            players[i].name = names.get(i);
            for (int j = 0; j < 7; j++) {
                Znak card = new Znak();
                card.setCardPos(players[i].deck_pos);
                card.instance.transform.rotate(0, card.getHitBox().getCenterY(), 0, players[i].getCard_rot_modifier());
                players[i].deck.add(card);
            }
            for (int j = 0; j < 2; j++) {
                Pshik card = new Pshik();
                card.setCardPos(players[i].deck_pos);
                card.instance.transform.rotate(0, card.getHitBox().getCenterY(), 0, players[i].getCard_rot_modifier());
                players[i].deck.add(card);
            }
            for (int j = 0; j < 2; j++) {
                Fire_ball card = new Fire_ball();
                card.setCardPos(players[i].deck_pos);
                card.instance.transform.rotate(0, card.getHitBox().getCenterY(), 0, players[i].getCard_rot_modifier());
                players[i].deck.add(card);
            }
            Collections.shuffle(players[i].deck);
        }
        player_now = players[0];
        turns_lasts = 0;
        for (int i = 0; i < 6; i++) {
            Fire_ball card = new Fire_ball();
            main_deck.add(card);
        }
        for (int i = 0; i < 8; i++) {
            Magic_dog card = new Magic_dog();
            main_deck.add(card);
        }
        for (int i = 0; i < 4; i++) {
            Protection_amulet card = new Protection_amulet();
            main_deck.add(card);
        }
        for (int i = 0; i < 4; i++) {
            SpellBook card = new SpellBook();
            main_deck.add(card);
        }
        for (int i = 0; i < 6; i++) {
            Crown card = new Crown();
            main_deck.add(card);
        }
        for (int i = 0; i < 8; i++) {
            SmallKnight card = new SmallKnight();
            main_deck.add(card);
        }
        for (int i = 0; i < 5; i++) {
            Summon card = new Summon();
            main_deck.add(card);
        }
        for (int i = 0; i < 3; i++) {
            SunFaced card = new SunFaced();
            main_deck.add(card);
        }
        Collections.shuffle(main_deck);
    }

    public static void attack(Player target_player, int damage) {//атака определенного игрока, в будущем следует добавить обработку доп.эффектов
        target_player.refresh_health(damage);
    }

    public static Card get_card(int player_num, String container_id) {
        //получение карты из любой колоды,метод нуже для гарантии отсутствия дублирования карт
        Card card = null;
        if (player_num == -1) {
            switch (container_id) {
                case ("main_deck"): {
                    if (!main_deck.isEmpty()) {
                        card = main_deck.get(0);
                        main_deck.remove(0);
                    }
                    break;
                }
                case ("legend_deck"): {
                    if (!legend_deck.isEmpty()) {
                        card = legend_deck.get(0);
                        legend_deck.remove(0);
                    }
                    break;
                }
            }
        } else {
            switch (container_id) {
                case ("hand"): {
                    if (!players[player_num].hand.isEmpty()) {
                        int i = MathUtils.random(0, players[player_num].getHand_size() - 1);
                        card = players[player_num].hand.get(i);
                        players[player_num].hand.remove(i);
                    }
                    break;
                }
                case ("deck"): {
                    if (!players[player_num].deck.isEmpty()) {
                        card = players[player_num].deck.get(0);
                        players[player_num].deck.remove(0);
                    }
                    break;
                }
                case ("trash"): {
                    if (!players[player_num].trash.isEmpty()) {
                        card = players[player_num].trash.get(0);
                        players[player_num].trash.remove(0);
                    }
                    break;
                }
            }
        }
        return card;
    }

    public static void refresh_market() {
        //обновление магазина #TODO сделать эту функцию менее ресурсозатратной
        int y = market_deck.size();
        for (int i = 0; i < 5 - y; i++) {
            // получение отсутствующих в магазине карт
            Card card = Server.get_card(-1, "main_deck");
            card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,actual_card_rotation.y);
            CanTouch.renderable_3d.add(card);
            market_deck.add(card);
        }
        for (int i = 0; i < 5; i++) {
            //поворот карт магазина
            Card card = market_deck.get(i);
            if (turns_lasts != 0) {
                if (players_count == 2 && player_now.player_number == 0) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 0, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,-180);
                } else {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 180, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,-180);
                }
                if (players_count == 3 && player_now.player_number == 0) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 0, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,0);
                }
                if (players_count == 3 && player_now.player_number == 1) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, -90, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,90);
                }
                if (players_count == 3 && player_now.player_number == 2) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, -180, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,90);
                }
                if (players_count == 4 && player_now.player_number == 0) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 90, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,90);
                }
                if (players_count == 4 && player_now.player_number == 1) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 180, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,90);
                }
                if (players_count == 4 && player_now.player_number == 2) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 270, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,90);
                }
                if (players_count == 4 && player_now.player_number == 3) {
                    if (i == 0)
                        actual_card_rotation.set(actual_card_rotation.x, 0, actual_card_rotation.z);
                    card.instance.transform.rotate(0,card.getHitBox().getCenterY(),0,90);
                }
            }
            // обновление позиций карт
            if (market_deck.get(i).in_market) {
                if (players_count == 2) {
                    if (player_now.player_number % 2 == 0) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, market_deck_pos.z), 300, "to_market_deck"));
                    } else {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(-market_deck_pos.x - card.getHitBox().getWidth() * i, market_deck_pos.y, -market_deck_pos.z), 300, "to_market_deck"));
                    }
                }
                if (players_count == 3) {
                    if (player_now.player_number == 0) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, market_deck_pos.z), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 1) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(-market_deck_pos.x, market_deck_pos.y, market_deck_pos.z - card.getHitBox().getDepth() * i), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 2) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(-market_deck_pos.x - card.getHitBox().getWidth() * i, market_deck_pos.y, -market_deck_pos.z), 300, "to_market_deck"));
                    }
                }
                if (players_count == 4) {
                    if (player_now.player_number == 0) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, market_deck_pos.z), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 1) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(-market_deck_pos.x, market_deck_pos.y, market_deck_pos.z - card.getHitBox().getDepth() * i), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 2) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, -market_deck_pos.z), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 3) {
                        card.animations3D.add(new Animation(market_deck.get(i).update_pos(), new Vector3(market_deck_pos.x, market_deck_pos.y, -market_deck_pos.z + card.getHitBox().getDepth() * i), 300, "to_market_deck"));
                    }
                }

            } else {
                if (players_count == 2) {
                    if (player_now.player_number % 2 == 0) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, market_deck_pos.z), 300, "to_market_deck"));
                    } else {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(-market_deck_pos.x - card.getHitBox().getWidth() * i, market_deck_pos.y, -market_deck_pos.z), 300, "to_market_deck"));
                    }
                }
                if (players_count == 3) {
                    if (player_now.player_number == 0) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, market_deck_pos.z), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 1) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(-market_deck_pos.x, market_deck_pos.y, market_deck_pos.z - card.getHitBox().getDepth() * i), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 2) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(-market_deck_pos.x - card.getHitBox().getWidth() * i, market_deck_pos.y, -market_deck_pos.z), 300, "to_market_deck"));
                    }
                }
                if (players_count == 4) {
                    if (player_now.player_number == 0) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, market_deck_pos.z), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 1) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(-market_deck_pos.x, market_deck_pos.y, market_deck_pos.z - card.getHitBox().getDepth() * i), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 2) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(market_deck_pos.x + card.getHitBox().getWidth() * i, market_deck_pos.y, -market_deck_pos.z), 300, "to_market_deck"));
                    }
                    if (player_now.player_number == 3) {
                        card.animations3D.add(new Animation(main_deck_pos, new Vector3(market_deck_pos.x, market_deck_pos.y, -market_deck_pos.z + card.getHitBox().getDepth() * i), 300, "to_market_deck"));
                    }
                }
            }
        }
    }

    public static void turn_ended() {
        //завершение хода #TODO при отказе от 2д карт и анимаций, связанный с отрисовкой кадров, переделать блок смены ходов с добавлением таймеров, во избежание софлоков, связанных с преждевременным нажатием кнопки до конца анимации
        turn_end_button_pressed = true;
        //перемещение карт в сброс
        for (Card card : player_now.hand) {
            card.convertTo3D(player_now.camera.position, player_now.trash_pos);
        }
        for (Card card : player_now.on_table_cards) {
            card.animations3D.add(new Animation(card.update_pos(), player_now.trash_pos, 2000, "to_trash_end"));
        }
        turns_lasts++;
        if (main_deck.isEmpty() && market_deck.size() < 5) {
            GameController.state = GameState.END;//конец игры при опустошении основной колоды
        }
    }

    public static void turn_started() {
        turn_end_button_pressed = false;
        if (players_count != 0) {
            prev_player = player_now;
            player_now = players[turns_lasts % players_count];
        }
        player_now.player_init();
        GameController.state = GameState.CHANGE_PLAYER;//состояние начала хода игрока
        if (player_now.hand.isEmpty()) player_now.getHand();
        else {
            player_now.refresh_hands_positions();
            for (Card card : player_now.hand) {
                if (!CanTouch.renderable_2d.contains(card)) CanTouch.renderable_2d.add(card);
                if (!CanTouch.sprite_collisions.contains(card))
                    CanTouch.sprite_collisions.add(card);
            }
        }
    }
}
