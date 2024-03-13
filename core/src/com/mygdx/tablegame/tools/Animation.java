package com.mygdx.tablegame.tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import jdk.internal.event.Event;

// класс, содержащий всю информацию об анимации 3д представления карты
public class Animation {
    public ModelInstance modelInstance;
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
    public Interpolation move_interpolation_type=Interpolation.linear;
    public Interpolation rotation_interpolation_type=Interpolation.linear;
    public float prevUpdateTime = -1;
    public boolean is3D;
    public String id;
    private  float progress=0;
    private Matrix3 anim_matrix = new Matrix3();
    private Quaternion quaternion = new Quaternion();


    public Animation(Vector3 start, Vector3 end, float millis_time,Interpolation move_interpolation,Interpolation rotation_interpolation, String id) {
        is3D = true;
        start_time = -1;
        startPos = start;
        endPos = end;
        duration = millis_time;
        distanceX = endPos.x - startPos.x;
        distanceY = endPos.y - startPos.y;
        distanceZ = endPos.z - startPos.z;
        this.id = id;
        move_interpolation_type=move_interpolation;
        rotation_interpolation_type=rotation_interpolation;
    }

    public Animation(Vector3 start, Vector3 end, float millis_time, Vector3 startR_angles, Vector3 endR_angles,Interpolation move_interpolation,Interpolation rotation_interpolation, String id) {
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
        move_interpolation_type=move_interpolation;
        rotation_interpolation_type=rotation_interpolation;
    }

    public Animation() {
    }

    public void update() {
        float delta_time = TimeUtils.timeSinceMillis(start_time) / duration;
        float XrotAng = start_rotation_angles.x + delta_angleX * rotation_interpolation_type.apply(delta_time)- prevRotX;
        prevRotX += XrotAng;
        float YrotAng = start_rotation_angles.y + delta_angleY * rotation_interpolation_type.apply(delta_time) - prevRotY;
        prevRotY += YrotAng;
        float ZrotAng = start_rotation_angles.z + delta_angleZ * rotation_interpolation_type.apply(delta_time) - prevRotZ;
        prevRotZ += ZrotAng;
        anim_matrix.set(new float[]{
                MathUtils.cosDeg(YrotAng) * MathUtils.cosDeg(ZrotAng),
                MathUtils.sinDeg(XrotAng) * MathUtils.sinDeg(YrotAng) * MathUtils.cosDeg(ZrotAng) + MathUtils.sinDeg(ZrotAng) * MathUtils.cosDeg(XrotAng),
                MathUtils.sinDeg(XrotAng) * MathUtils.sinDeg(ZrotAng) - MathUtils.sinDeg(YrotAng) * MathUtils.cosDeg(XrotAng) * MathUtils.cosDeg(ZrotAng),
                -1 * MathUtils.sinDeg(ZrotAng) * MathUtils.cosDeg(YrotAng),
                -1 * MathUtils.sinDeg(XrotAng) * MathUtils.sinDeg(YrotAng) * MathUtils.sinDeg(ZrotAng) + MathUtils.cosDeg(XrotAng) * MathUtils.cosDeg(ZrotAng),
                MathUtils.sinDeg(XrotAng) * MathUtils.cosDeg(ZrotAng) + MathUtils.sinDeg(YrotAng) * MathUtils.sinDeg(ZrotAng) * MathUtils.cosDeg(XrotAng),
                MathUtils.sinDeg(YrotAng),
                -1 * MathUtils.sinDeg(XrotAng) * MathUtils.cosDeg(YrotAng),
                MathUtils.cosDeg(XrotAng) * MathUtils.cosDeg(YrotAng)
        });
        quaternion.setFromMatrix(anim_matrix);
        modelInstance.transform.rotate(quaternion);
        modelInstance.transform.setTranslation(new Vector3(startPos.x+distanceX*move_interpolation_type.apply(delta_time),startPos.y+distanceY*move_interpolation_type.apply(delta_time),startPos.z+distanceZ*move_interpolation_type.apply(delta_time)));
    }
    public  void freezeAnimation(){
        progress=TimeUtils.timeSinceMillis(start_time)/duration;
    }
    public void unfreezeAnimation(){
        start_time= (long) (TimeUtils.millis()-(duration*progress));
    }
}
