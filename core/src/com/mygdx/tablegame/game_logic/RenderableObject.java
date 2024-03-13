package com.mygdx.tablegame.game_logic;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.tablegame.tools.Animation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Queue;

public class RenderableObject {
    ModelInstance modelInstance;
    boolean enable=false;
    ArrayList<Animation> animations_list;
    public void setVisible(){

    }
    public  void update_animation(){
        if (!animations_list.isEmpty()){
            Animation animation=animations_list.get(0);
            if(animation.duration< TimeUtils.timeSinceMillis(animation.start_time)){
                animations_list.remove(0);
                return;
            }
            if(animation.start_time==-1){
                animation.start_time=TimeUtils.millis();
            }
            else {
                animation.update();
            }
        }
    }
}
