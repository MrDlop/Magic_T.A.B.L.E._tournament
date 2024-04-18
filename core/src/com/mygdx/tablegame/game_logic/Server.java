package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.tablegame.cards.Card;
import com.mygdx.tablegame.cards.Crown;
import com.mygdx.tablegame.cards.Fire_ball;
import com.mygdx.tablegame.cards.Magic_dog;
import com.mygdx.tablegame.cards.Princess;
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
import java.util.TimerTask;

// класс для построения взаимодействий ме жду другими классами и логики игровых ходов, несмотря на название, пока не имеет отношения к сетевой игре
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
    public static boolean end_turn_button_pressed;

    public static void server_init(ArrayList<String> names) {// лучше сделать конструктором или переделать класс в static
        // пока положения задаются вручную #TODO добавить автоматический расчет положения камер и игроков
        players = new Player[players_count];
        if (players_count == 2) {
            Player player = new Player(0);
            Constants.PlayerCoordinateInfo player1CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[0];
            player.deck_pos = player1CoordinateInfo.deck_pos;
            player.hand_pos = player1CoordinateInfo.hand_pos;
            player.trash_pos = player1CoordinateInfo.trash_pos;
            player.played_card_pos = player1CoordinateInfo.played_card_pos;
            player.shop_pos = player1CoordinateInfo.shop_pos;
            player.camera_on_played_card_pos = player1CoordinateInfo.camera_on_played_card_pos;
            player.camera_on_hand_pos = player1CoordinateInfo.camera_on_hand_pos;
            player.camera_on_shop_pos = player1CoordinateInfo.camera_on_shop_pos;
            player.setStandart_card_world_rotation(player1CoordinateInfo.laying_card_global_rotation);
            player.main_axis = player1CoordinateInfo.main_axis;
            players[0] = player;

            Player player1 = new Player(1);
            Constants.PlayerCoordinateInfo player2CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[1];
            player1.deck_pos = player2CoordinateInfo.deck_pos;
            player1.hand_pos = player2CoordinateInfo.hand_pos;
            player1.trash_pos = player2CoordinateInfo.trash_pos;
            player1.played_card_pos = player2CoordinateInfo.played_card_pos;
            player1.shop_pos = player2CoordinateInfo.shop_pos;
            player1.camera_on_played_card_pos = player2CoordinateInfo.camera_on_played_card_pos;
            player1.camera_on_hand_pos = player2CoordinateInfo.camera_on_hand_pos;
            player1.camera_on_shop_pos = player2CoordinateInfo.camera_on_shop_pos;
            player1.setStandart_card_world_rotation(player2CoordinateInfo.laying_card_global_rotation);
            player1.main_axis = player2CoordinateInfo.main_axis;
            players[1] = player1;
        }
        if (players_count == 3) {
            Player player = new Player(0);
            Constants.PlayerCoordinateInfo player1CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[0];
            player.deck_pos = player1CoordinateInfo.deck_pos;
            player.hand_pos = player1CoordinateInfo.hand_pos;
            player.trash_pos = player1CoordinateInfo.trash_pos;
            player.played_card_pos = player1CoordinateInfo.played_card_pos;
            player.shop_pos = player1CoordinateInfo.shop_pos;
            player.camera_on_played_card_pos = player1CoordinateInfo.camera_on_played_card_pos;
            player.camera_on_hand_pos = player1CoordinateInfo.camera_on_hand_pos;
            player.camera_on_shop_pos = player1CoordinateInfo.camera_on_shop_pos;
            player.setStandart_card_world_rotation(player1CoordinateInfo.laying_card_global_rotation);
            player.main_axis = player1CoordinateInfo.main_axis;
            players[0] = player;

            Player player1 = new Player(1);
            Constants.PlayerCoordinateInfo player2CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[1];
            player1.deck_pos = player2CoordinateInfo.deck_pos;
            player1.hand_pos = player2CoordinateInfo.hand_pos;
            player1.trash_pos = player2CoordinateInfo.trash_pos;
            player1.played_card_pos = player2CoordinateInfo.played_card_pos;
            player1.shop_pos = player2CoordinateInfo.shop_pos;
            player1.camera_on_played_card_pos = player2CoordinateInfo.camera_on_played_card_pos;
            player1.camera_on_hand_pos = player2CoordinateInfo.camera_on_hand_pos;
            player1.camera_on_shop_pos = player2CoordinateInfo.camera_on_shop_pos;
            player1.setStandart_card_world_rotation(player2CoordinateInfo.laying_card_global_rotation);
            player1.main_axis = player2CoordinateInfo.main_axis;
            players[1] = player1;

            Player player2 = new Player(2);
            Constants.PlayerCoordinateInfo player3CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[2];
            player2.deck_pos = player3CoordinateInfo.deck_pos;
            player2.hand_pos = player3CoordinateInfo.hand_pos;
            player2.trash_pos = player3CoordinateInfo.trash_pos;
            player2.played_card_pos = player3CoordinateInfo.played_card_pos;
            player2.shop_pos = player3CoordinateInfo.shop_pos;
            player2.camera_on_played_card_pos = player3CoordinateInfo.camera_on_played_card_pos;
            player2.camera_on_hand_pos = player3CoordinateInfo.camera_on_hand_pos;
            player2.camera_on_shop_pos = player3CoordinateInfo.camera_on_shop_pos;
            player2.setStandart_card_world_rotation(player3CoordinateInfo.laying_card_global_rotation);
            player2.main_axis = player3CoordinateInfo.main_axis;
            players[2] = player2;

        }
        if (players_count == 4) {
            Player player = new Player(0);
            Constants.PlayerCoordinateInfo player1CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[0];
            player.deck_pos = player1CoordinateInfo.deck_pos;
            player.hand_pos = player1CoordinateInfo.hand_pos;
            player.trash_pos = player1CoordinateInfo.trash_pos;
            player.played_card_pos = player1CoordinateInfo.played_card_pos;
            player.shop_pos = player1CoordinateInfo.shop_pos;
            player.camera_on_played_card_pos = player1CoordinateInfo.camera_on_played_card_pos;
            player.camera_on_hand_pos = player1CoordinateInfo.camera_on_hand_pos;
            player.camera_on_shop_pos = player1CoordinateInfo.camera_on_shop_pos;
            player.setStandart_card_world_rotation(player1CoordinateInfo.laying_card_global_rotation);
            player.main_axis = player1CoordinateInfo.main_axis;
            players[3] = player;

            Player player1 = new Player(1);
            Constants.PlayerCoordinateInfo player2CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[1];
            player1.deck_pos = player2CoordinateInfo.deck_pos;
            player1.hand_pos = player2CoordinateInfo.hand_pos;
            player1.trash_pos = player2CoordinateInfo.trash_pos;
            player1.played_card_pos = player2CoordinateInfo.played_card_pos;
            player1.shop_pos = player2CoordinateInfo.shop_pos;
            player1.camera_on_played_card_pos = player2CoordinateInfo.camera_on_played_card_pos;
            player1.camera_on_hand_pos = player2CoordinateInfo.camera_on_hand_pos;
            player1.camera_on_shop_pos = player2CoordinateInfo.camera_on_shop_pos;
            player1.setStandart_card_world_rotation(player2CoordinateInfo.laying_card_global_rotation);
            player1.main_axis = player2CoordinateInfo.main_axis;
            players[1] = player1;

            Player player2 = new Player(2);
            Constants.PlayerCoordinateInfo player3CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[2];
            player2.deck_pos = player3CoordinateInfo.deck_pos;
            player2.hand_pos = player3CoordinateInfo.hand_pos;
            player2.trash_pos = player3CoordinateInfo.trash_pos;
            player2.played_card_pos = player3CoordinateInfo.played_card_pos;
            player2.shop_pos = player3CoordinateInfo.shop_pos;
            player2.camera_on_played_card_pos = player3CoordinateInfo.camera_on_played_card_pos;
            player2.camera_on_hand_pos = player3CoordinateInfo.camera_on_hand_pos;
            player2.camera_on_shop_pos = player3CoordinateInfo.camera_on_shop_pos;
            player2.setStandart_card_world_rotation(player3CoordinateInfo.laying_card_global_rotation);
            player2.main_axis = player3CoordinateInfo.main_axis;
            players[2] = player2;

            Player player3 = new Player(3);
            Constants.PlayerCoordinateInfo player4CoordinateInfo = GameController.constants.current_preset.playerCoordinateInfos[3];
            player3.deck_pos = player4CoordinateInfo.deck_pos;
            player3.hand_pos = player4CoordinateInfo.hand_pos;
            player3.trash_pos = player4CoordinateInfo.trash_pos;
            player3.played_card_pos = player4CoordinateInfo.played_card_pos;
            player3.shop_pos = player4CoordinateInfo.shop_pos;
            player3.camera_on_played_card_pos = player4CoordinateInfo.camera_on_played_card_pos;
            player3.camera_on_hand_pos = player4CoordinateInfo.camera_on_hand_pos;
            player3.camera_on_shop_pos = player4CoordinateInfo.camera_on_shop_pos;
            player3.setStandart_card_world_rotation(player4CoordinateInfo.laying_card_global_rotation);
            player3.main_axis = player4CoordinateInfo.main_axis;
            players[0] = player3;
        }
        // раздача карт игрокам и в колоды #TODO сделать файл для хранения количества и видов карт
        for (int i = 0; i < players_count; i++) {
            players[i].name = names.get(i);
            for (int j = 0; j < 7; j++) {
                Znak card = new Znak();
                card.setCardPos(players[i].deck_pos);
                card.instance.setToWorldRotation(players[i].getStandart_card_world_rotation());
                players[i].deck.add(card);
            }
            for (int j = 0; j < 2; j++) {
                Pshik card = new Pshik();
                card.setCardPos(players[i].deck_pos);
                card.instance.setToWorldRotation(players[i].getStandart_card_world_rotation());
                players[i].deck.add(card);
            }
            for (int j = 0; j < 2; j++) {
                Fire_ball card = new Fire_ball();
                card.setCardPos(players[i].deck_pos);
                card.instance.setToWorldRotation(players[i].getStandart_card_world_rotation());
                players[i].deck.add(card);
            }
            Collections.shuffle(players[i].deck);
        }
        player_now = players[0];
        turns_lasts = 0;
        for (int i = 0; i < 4; i++) {
            Fire_ball card = new Fire_ball();
            main_deck.add(card);
        }
        for (int i = 0; i < 6; i++) {
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
        for (int i = 0; i < 4; i++) {
            SmallKnight card = new SmallKnight();
            main_deck.add(card);
        }
        for (int i = 0; i < 5; i++) {
            Summon card = new Summon();
            main_deck.add(card);
        }
        for (int i = 0; i < 4; i++) {
            SunFaced card = new SunFaced();
            main_deck.add(card);
        }
        for (int i = 0; i < 2; i++) {
            Princess card = new Princess();
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
        int y = market_deck.size();
        for (int i = 0; i < 5 - y; i++) {
            // получение отсутствующих в магазине карт
            Card card = Server.get_card(-1, "main_deck");
            market_deck.add(card);
        }
        for (int i = 0; i < 5; i++) {
            Card card=market_deck.get(i);
            card.setVisible(true);
            card.setTouchable(true);
            card.getModelInstance().setToWorldRotation(player_now.getStandart_card_world_rotation());
            if(player_now.main_axis.equals("1")){
                card.setCardPos(new Vector3(market_deck_pos.x+card.width*i,market_deck_pos.y,market_deck_pos.z));
            }
            if(player_now.main_axis.equals("3")){
                card.setCardPos(new Vector3(-market_deck_pos.x-card.width*i,market_deck_pos.y,-market_deck_pos.z));
            }
            if(player_now.main_axis.equals("2")){
                card.setCardPos(new Vector3(-market_deck_pos.x,market_deck_pos.y,market_deck_pos.z-card.width*i));
            }
            if(player_now.main_axis.equals("4")){
                card.setCardPos(new Vector3(market_deck_pos.x,market_deck_pos.y,market_deck_pos.z-card.width*i));
            }
        }

    }

    public static void turn_ended() {
        //завершение хода
        GlobalEvents.activate_turnCompliteIntentEvent();
        turns_lasts++;
        if (main_deck.isEmpty() && market_deck.size() < 5) {
            GameController.state = GameState.END;//конец игры при опустошении основной колоды
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                GlobalEvents.activate_turnComplitedEvent();
                turn_started();
            }
        };
        GameController.timer.schedule(timerTask, 5500);
    }

    public static void turn_started() {
        if (players_count != 0) {
            prev_player = player_now;
            player_now = players[turns_lasts % players_count];
        }
        if (!player_now.inited) {
            player_now.player_init();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    player_now.getHand();
                }
            };
            GameController.timer.schedule(timerTask, 50);
        }
        GameController.state = GameState.CHANGE_PLAYER;//состояние начала хода игрока

    }
}
