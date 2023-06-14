package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
// класс для реализации RayCast, содержит минимально необходимый для это набор полей и методов, от него наследуются карты
public class Touchable {
    public BoundingBox hitBox;//"коробка", которая считается по минимальным максимальным точкам арены
    public long prevTouchTime=TimeUtils.millis();//время предыдущего касания
    public Sprite sprite;
    public BoundingBox getHitBox() {return  hitBox;} //переопределить в наследнике
    public Rectangle getSpriteHitBox(){return sprite.getBoundingRectangle();}
    public void touched(){};
    public void doubleTouched(){};
    public void sprite_touched(){};
    public void sprite_doubleTouched(){};
    public void updateTime(){
        prevTouchTime= TimeUtils.millis();
    }
}