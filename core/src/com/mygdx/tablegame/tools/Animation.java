package com.mygdx.tablegame.tools;

import com.badlogic.gdx.math.Vector3;
// класс, содержащий всю информацию об анимации 3д представления карты
public class Animation {
    public long start_time;//время начала анимации
    public Vector3 startPos;//начальное положение
    public Vector3 endPos;//конечное положение
    public float duration;//продолжительность
    //смещение по каждой из осей
    public float distanceX;
    public float distanceY;
    public float distanceZ;
    public Vector3 start_rotation_angles;//начальные углы поворота(углы эйлера по каждой из осей)
    public Vector3 end_rotation_angles;//конечные углы поворота(углы эйлера по каждой из осей)
    //угловое смещение по каждой из осей
    public float delta_angleX;
    public float delta_angleY;
    public float delta_angleZ;
    //текущие углы поворота
    public float prevRotX = 0;
    public float prevRotY = 0;
    public float prevRotZ = 0;
    public boolean is3D;
    public String id;
    

    public Animation(Vector3 start, Vector3 end, float millis_time, String id) {
        is3D = true;
        start_time = -1;
        startPos = start;
        endPos = end;
        duration = millis_time;
        distanceX = endPos.x - startPos.x;
        distanceY = endPos.y - startPos.y;
        distanceZ = endPos.z - startPos.z;
        this.id = id;
    }

    public Animation(Vector3 start, Vector3 end, float millis_time, Vector3 startR_angles, Vector3 endR_angles, String id) {
        is3D = true;
        start_time = -1;
        startPos = start;
        endPos = end;
        duration = millis_time;
        start_rotation_angles = startR_angles;
        end_rotation_angles = endR_angles;
        delta_angleX = end_rotation_angles.x - start_rotation_angles.x;
        delta_angleY = end_rotation_angles.y - start_rotation_angles.y;
        delta_angleZ = end_rotation_angles.z - start_rotation_angles.z;
        distanceX = endPos.x - startPos.x;
        distanceY = endPos.y - startPos.y;
        distanceZ = endPos.z - startPos.z;
        this.id = id;
    }

    public Animation() {
    }
}
