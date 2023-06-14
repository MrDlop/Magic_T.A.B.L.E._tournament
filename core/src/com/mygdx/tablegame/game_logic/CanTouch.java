package com.mygdx.tablegame.game_logic;

import com.mygdx.tablegame.tools.ElementUI;
import com.mygdx.tablegame.cards.Card;

import java.util.ArrayList;
// класс используемый для контроля рендера объектов
public class CanTouch {
    public static ArrayList<Touchable> collisions=new ArrayList<>();//3д объекты, доступные для взаимодействия
    public static ArrayList<Touchable> sprite_collisions=new ArrayList<>();//2д объекты, доступные для взаимодействия
    public static ArrayList<Card> renderable_3d =new ArrayList<>();//3д объекты, которые нужно отрисовать
    public static ArrayList<Card> renderable_2d=new ArrayList<>();//2д объекты, которые нужно отрисовать
    public static ArrayList<Card> need_to_delete3D=new ArrayList<>();//используется для удаления анимаций, возможно стоит переделать
    public static ArrayList<Card> need_to_delete2D=new ArrayList<>();//используется для удаления анимаций, возможно стоит переделать
    public static ArrayList<ElementUI> UI_elements=new ArrayList<>();//элементы экранного интерфейса, которые нужно отрисовать
    public static Card now_selected_card=null;// карта, которая выбрана сейчас
    public static Card now_looking_card=null;//карта, просматриваемая игроком(функция приближения)

    public static void setNow_selected_card(Card card) {
        if(now_selected_card!=null)now_selected_card.non_selected();
        now_selected_card=card;
    }

    public CanTouch() {
    }
}
