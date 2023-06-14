package com.mygdx.tablegame.game_logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;
import java.util.Vector;

public class JSONResponse {
    String session;
    String type_request;

    int mess;

    JSONPost jsonPost;

    JSONAttack jsonAttack;

    JSONResponse(JSONObject jsonObject) {
        try {
            this.session = jsonObject.getString("session");
            this.type_request = jsonObject.getString("request");
            switch (this.type_request) {
                case "POST":
                    jsonPost = new JSONPost(jsonObject.getJSONObject("data"));
                    break;
                case "ATTACK":
                case "ARMOR":
                    jsonAttack = new JSONAttack(jsonObject.getJSONObject("data"));
                    break;
                case "CONNECT":
                case "CREATE":
                    mess = jsonObject.getInt("data");
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static class JSONAttack {
        int card, id_target, id_player_attacker;

        JSONAttack(JSONObject jsonObject) {
            try {
                this.card = jsonObject.getInt("card");
                this.id_target = jsonObject.getInt("id_target");
                this.id_player_attacker = jsonObject.getInt("id_player_attacker");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class JSONDeck {
        public Vector<Integer> deck;


        JSONDeck(JSONArray jsonArray) {
            deck = new Vector<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    deck.set(i, jsonArray.getInt(i));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class JSONPost {
        int move;
        TreeMap<String, JSONDeck> decks = new TreeMap<>();
        TreeMap<String, JSONState> states = new TreeMap<>();


        JSONPost(JSONObject jsonObject) {
            try {
                this.move = jsonObject.getInt("move");
                JSONObject decks = jsonObject.getJSONObject("decks");
                // CHECK COUNT PLAYERS OR SEND ALWAYS 4 deck players
                this.decks.put("deck_player_1", new JSONDeck(decks.getJSONArray("deck_player_1")));
                this.decks.put("deck_player_2", new JSONDeck(decks.getJSONArray("deck_player_2")));
                this.decks.put("deck_player_3", new JSONDeck(decks.getJSONArray("deck_player_3")));
                this.decks.put("deck_player_4", new JSONDeck(decks.getJSONArray("deck_player_4")));
                this.decks.put("deck_all", new JSONDeck(decks.getJSONArray("deck_all")));
                this.decks.put("deck_wand", new JSONDeck(decks.getJSONArray("deck_wand")));
                this.decks.put("deck_dead", new JSONDeck(decks.getJSONArray("deck_dead")));
                JSONObject states = jsonObject.getJSONObject("statistic");
                this.states.put("0", new JSONState(states.getJSONObject("0")));
                this.states.put("1", new JSONState(states.getJSONObject("1")));
                this.states.put("2", new JSONState(states.getJSONObject("2")));
                this.states.put("3", new JSONState(states.getJSONObject("3")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class JSONState {
        public int health, power;


        JSONState(JSONObject jsonObject) {
            try {
                health = jsonObject.getInt("health");
                power = jsonObject.getInt("power");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
