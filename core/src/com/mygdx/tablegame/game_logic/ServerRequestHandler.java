package com.mygdx.tablegame.game_logic;

import com.mygdx.tablegame.cards.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ServerRequestHandler {
    private WebSocketClient webSocketClient;

    private void createWebSocketClient() {
        URI uri;
        try {
            // Create connection
            uri = new URI("ws://192.0.0.1:8001/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                // Create connection to SessionID (connect to session)
                webSocketClient.send("{" +
                        "\"session\":\"" + ServerOnline.SessionID + "\", " +
                        "\"request\":\"CONNECT\"," +
                        "\"id\": -1" +
                        "}"
                );
            }

            public void onCreate() {
                // Create connection to SessionID (create session)
                webSocketClient.send("{" +
                        "\"session\":\"" + ServerOnline.SessionID + "\", " +
                        "\"request\":\"CREATE\"," +
                        "\"id\": -1" +
                        "}"
                );
            }

            @Override
            public void onTextReceived(String s) {
                try {
                    JSONResponse jsonResponse = new JSONResponse(new JSONObject(s));
                    switch (jsonResponse.type_request) {
                        case "CREATE":
                            if (jsonResponse.mess == 0) {
                                // not connected message (this session already create)
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                                // close connection
                                webSocketClient.close();
                            } else {
                                // set global id player - mess value
                                ServerOnline.this_player_id = jsonResponse.mess;
                            }
                            break;
                        case "CONNECT":
                            if (jsonResponse.mess == 0) {
                                // not connected message (this session already start or not found)
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                                // close connection
                                webSocketClient.close();
                                webSocketClient.close();
                            } else {
                                // set global id player - mess value
                                ServerOnline.this_player_id = jsonResponse.mess;
                            }
                            break;
                        case "ATTACK":
                            // check armor card and send ARMOR request
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                            break;
                        case "POST":
                            // update decks and stats
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                            break;
                        case "ARMOR":
                            // logic turn
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                            break;
                        case "START":
                            // start session
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                            break;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                // you actions when receive message
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onCloseReceived() {
                // session close handling
                webSocketClient.send("{" +
                        "\"session\":\"" + ServerOnline.SessionID + "\", " +
                        "\"request\":\"DISCONNECT\"," +
                        "\"id\": " + ServerOnline.this_player_id +
                        "}"
                );
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private String deckToString(ArrayList<Card> deck, String name) {
        // format deck to JSON string
        String str_deck = "";
        str_deck += "\"" + name + "\":[";
        StringBuilder str_deckBuilder = new StringBuilder(str_deck);
        for (Card card : deck) {
            str_deckBuilder.append(Card.ID).append(",");
        }
        str_deck = str_deckBuilder.toString();
        str_deck += "],";
        return str_deck;
    }

    private String stateToString(Player player, String name) {
        // format player state to JSON string
        String str_state = "";
        str_state += "\"" + name + "\":{";
        str_state += "\"health\":" + player.getHealth() + ",";
        str_state += "\"power_points\":" + player.getPower_points() + ",";
        str_state += "},";
        return str_state;
    }

    public void sendPostRequest(ArrayList<Card> main_deck,
                                ArrayList<Card> legend_deck,
                                ArrayList<Card> destroyed_deck,
                                ArrayList<Card> market_deck,
                                Player player_1,
                                Player player_2,
                                Player player_3,
                                Player player_4) {
        String decks = "";
        String state = "";
        decks += deckToString(player_1.deck, "deck_player_1");
        decks += deckToString(player_1.hand, "hand_player_1");
        decks += deckToString(player_1.trash, "trash_player_1");
        decks += deckToString(player_1.on_table_cards, "on_table_player_1");
        state += stateToString(player_1, "0");
        decks += deckToString(player_2.deck, "deck_player_2");
        decks += deckToString(player_2.hand, "hand_player_2");
        decks += deckToString(player_2.trash, "trash_player_2");
        decks += deckToString(player_2.on_table_cards, "on_table_player_2");
        state += stateToString(player_2, "1");
        decks += deckToString(player_3.deck, "deck_player_3");
        decks += deckToString(player_3.hand, "hand_player_3");
        decks += deckToString(player_3.trash, "trash_player_3");
        decks += deckToString(player_3.on_table_cards, "on_table_player_3");
        state += stateToString(player_3, "2");
        decks += deckToString(player_4.deck, "deck_player_4");
        decks += deckToString(player_4.hand, "hand_player_4");
        decks += deckToString(player_4.trash, "trash_player_4");
        decks += deckToString(player_4.on_table_cards, "on_table_player_4");
        state += stateToString(player_4, "3");
        decks += deckToString(main_deck, "main_deck");
        decks += deckToString(legend_deck, "legend_deck");
        decks += deckToString(destroyed_deck, "destroyed_deck");
        decks += deckToString(market_deck, "market_deck");
        webSocketClient.send("{" +
                "\"session\":\"" + ServerOnline.SessionID + "\", " +
                "\"request\":\"POST\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "\"data\":" +
                "\"move\":" + Server.player_now.id + "," +
                "\"decks\":{" + decks + "}," +
                "\"statistic\":{" + state + "}," +
                "}");
    }

    public void sendStartRequest() {
        webSocketClient.send("{" +
                "\"session\":\"" + ServerOnline.SessionID + "\", " +
                "\"request\":\"START\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "}"
        );
    }

    public void sendAttackRequest(int card_id, int player_number) {
        webSocketClient.send("{" +
                "\"session\":" + ServerOnline.SessionID + "\", " +
                "\"request\":\"ATTACK\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "\"card\":\"" + card_id + "\"," +
                "\"id_target\":" + player_number + "," +
                "\"id_player\":" + ServerOnline.this_player_id +
                "}"
        );
    }

    public void sendArmorRequest(int card_id, int player_number) {
        webSocketClient.send("{" +
                "\"session\":" + ServerOnline.SessionID + "\", " +
                "\"request\":\"ARMOR\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "\"card\":\"" + card_id + "\"," +
                "\"id_target\":" + player_number + "," +
                "\"id_player\":" + ServerOnline.this_player_id +
                "}"
        );
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

    private static class JSONResponse {
        long session;
        String type_request;

        int mess;

        JSONPost jsonPost;

        JSONAttack jsonAttack;

        JSONResponse(JSONObject jsonObject) {
            try {
                this.session = jsonObject.getLong("session");
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
