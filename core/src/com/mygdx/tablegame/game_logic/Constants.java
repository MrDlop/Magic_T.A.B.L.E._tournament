package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.math.Vector3;

public class Constants {
    class PlayerCoordinateInfo {
        public Vector3 deck_pos;
        public Vector3 hand_pos;
        public Vector3 trash_pos;
        public Vector3 played_card_pos;
        public Vector3 shop_pos;
        public Vector3 camera_on_played_card_pos;
        public Vector3 camera_on_hand_pos;
        public Vector3 camera_on_shop_pos;
        public Vector3 laying_card_global_rotation;
        public String main_axis;

        public PlayerCoordinateInfo(Vector3 deck_pos,Vector3 hand_pos, Vector3 trash_pos, Vector3 played_card_pos, Vector3 shop_pos, Vector3 camera_on_played_card_pos, Vector3 camera_on_hand_pos, Vector3 camera_on_shop_pos,Vector3 laying_card_global_rotation,String main_axis) {
            this.deck_pos=deck_pos;
            this.hand_pos = hand_pos;
            this.trash_pos = trash_pos;
            this.played_card_pos = played_card_pos;
            this.shop_pos = shop_pos;
            this.camera_on_played_card_pos = camera_on_played_card_pos;
            this.camera_on_hand_pos = camera_on_hand_pos;
            this.camera_on_shop_pos = camera_on_shop_pos;
            this.laying_card_global_rotation=laying_card_global_rotation;
            this.main_axis=main_axis;
        }
    }
    public class Preset{
        Integer player_number;
        PlayerCoordinateInfo[] playerCoordinateInfos;

        public Preset(Integer player_number, PlayerCoordinateInfo[] playerCoordinateInfos) {
            this.player_number = player_number;
            this.playerCoordinateInfos = playerCoordinateInfos;
        }
    }

    final PlayerCoordinateInfo place_1 = new PlayerCoordinateInfo(
            new Vector3(13, 29.9f, 28),
            new Vector3(0, 40, 40),
            new Vector3(18, 29.7f, 28),
            new Vector3(0, 30, 25),
            new Vector3(0, 30, 8),
            new Vector3(0, 40, 28),
            new Vector3(0, 45, 47),
            new Vector3(0, 38, 9.5f),
            new Vector3(0,-90,90),
            "1");
    final PlayerCoordinateInfo place_2 = new PlayerCoordinateInfo(
            new Vector3(18, 29.9f, -10),//deck_pos
            new Vector3(30, 40, 0), //hand_pos
            new Vector3(18, 29.7f, -15),//trash_pos
            new Vector3(19, 30, 0),//played_card_pos
            new Vector3(9.7f, 30, 0),//shop_pos
            new Vector3(23f, 36f, 0),//camera_on_played_card_pos
            new Vector3(36.5f, 44.5f, 0),//camera_on_hand_pos
            new Vector3(13, 38, 0),// camera_on_shop_pos
            new Vector3(0,0,90),
            "2");
    final PlayerCoordinateInfo place_3 = new PlayerCoordinateInfo(
            new Vector3(-13, 29.9f, -28),
            new Vector3(0, 40, -40),
            new Vector3(-18, 29.7f, -28),
            new Vector3(0, 30, -25),
            new Vector3(0, 30, -8),
            new Vector3(0, 40, -28),
            new Vector3(0, 45, -47),
            new Vector3(0, 38, -9.5f),
            new Vector3(0,90,90),
            "3");
    final PlayerCoordinateInfo place_4 = new PlayerCoordinateInfo(
            new Vector3(-18, 29.9f, -10),//deck_pos
            new Vector3(-30, 40, 0), //hand_pos
            new Vector3(-18, 29.7f, 15),//trash_pos
            new Vector3(-19, 30, 0),//played_card_pos
            new Vector3(-9.8f, 30, 0),//shop_pos
            new Vector3(-23f, 36f, 0),//camera_on_played_card_pos
            new Vector3(-36.5f, 44.5f, 0),//camera_on_hand_pos
            new Vector3(-13, 38, 0),//camera_on_shop_pos
            new Vector3(0,180,90),
            "4");
    public final Preset preset_2_players=new Preset(2,new PlayerCoordinateInfo[]{place_1,place_3});
    public final Preset preset_3_players=new Preset(3,new PlayerCoordinateInfo[]{place_1,place_2,place_3});
    public final Preset preset_4_players=new Preset(4,new PlayerCoordinateInfo[]{place_1,place_2,place_3,place_4});
    public Preset current_preset;
}
