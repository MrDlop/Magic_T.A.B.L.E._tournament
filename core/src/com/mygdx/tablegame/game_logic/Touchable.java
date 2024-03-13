package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
// класс для реализации RayCast, содержит минимально необходимый для это набор полей и методов, от него наследуются карты
public class Touchable extends RenderableObject {
    public BoundingBox hitBox;//"коробка", которая считается по минимальным максимальным точкам арены
    public long prevTouchTime=TimeUtils.millis();//время предыдущего касания
    public BoundingBox getHitBox() {return  hitBox;} //переопределить в наследнике
    public void touched(){};
    public void doubleTouched(){};
    public void updateTime(){
        prevTouchTime= TimeUtils.millis();
    }
}