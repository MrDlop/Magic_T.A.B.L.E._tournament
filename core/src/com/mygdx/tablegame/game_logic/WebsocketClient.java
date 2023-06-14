package com.mygdx.tablegame.game_logic;

import com.mygdx.tablegame.cards.Card;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

import tech.gusavila92.websocketclient.WebSocketClient;

public class WebsocketClient extends WebSocketClient {
    /**
     * Initialize all the variables
     *
     * @param uri URI of the WebSocket server
     */
    public WebsocketClient(URI uri) {
        super(uri);
    }

    @Override
    public void onOpen() {
    }

    public void onConnect(String SessionID) {
        this.send("{" +
                "\"session\":\"" + SessionID + "\", " +
                "\"request\":\"CONNECT\"," +
                "\"id\": -1" +
                "}"
        );
    }

    public void onCreate(String SessionID) {
        this.send("{" +
                "\"session\":\"" + SessionID + "\", " +
                "\"request\":\"CREATE\"," +
                "\"id\": -1" +
                "}"
        );
    }

    @Override
    public void onTextReceived(String message) {
        try {
            JSONResponse jsonResponse = new JSONResponse(new JSONObject(message));
            switch (jsonResponse.type_request) {
                case "CREATE":
                    if (jsonResponse.mess == 0) {
                        // not connected message (this session already create)
                                /*
--------------------------------------PASTE CODE----------------------------------------------------
                                 */
                        // close connection
                        this.close();
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
                        this.close();
                        this.close();
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
        this.send("{" +
                "\"session\":\"" + ServerOnline.SessionID + "\", " +
                "\"request\":\"DISCONNECT\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "}"
        );
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
        this.send("{" +
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
        this.send("{" +
                "\"session\":\"" + ServerOnline.SessionID + "\", " +
                "\"request\":\"START\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "}"
        );
    }

    public void sendAttackRequest(int card_id, int player_number) {
        this.send("{" +
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
        this.send("{" +
                "\"session\":" + ServerOnline.SessionID + "\", " +
                "\"request\":\"ARMOR\"," +
                "\"id\": " + ServerOnline.this_player_id +
                "\"card\":\"" + card_id + "\"," +
                "\"id_target\":" + player_number + "," +
                "\"id_player\":" + ServerOnline.this_player_id +
                "}"
        );
    }

}
