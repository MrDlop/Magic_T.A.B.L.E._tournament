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
    public MyModelInstance modelInstance;
    public long start_time;//время начала анимации
    public Vector3 startPos = new Vector3();//начальное положение
    public Vector3 endPos = new Vector3();//конечное положение
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
    public Interpolation move_interpolation_type = Interpolation.linear;
    public Interpolation rotation_interpolation_type = Interpolation.linear;
    public float prevUpdateTime = -1;
    public boolean is3D;
    public boolean delayed_call;
    public String id;
    private float progress = 0;
    private Quaternion quaternion = new Quaternion();
    private Quaternion quaternionX = new Quaternion();
    private Quaternion quaternionY = new Quaternion();
    private Quaternion quaternionZ = new Quaternion();


    public Animation(MyModelInstance instance, Vector3 start, Vector3 end, float millis_time, Interpolation move_interpolation, String id) {
        is3D = true;
        delayed_call = false;
        modelInstance = instance;
        start_time = -1;
        startPos = start;
        endPos = end;
        duration = millis_time;
        distanceX = endPos.x - startPos.x;
        distanceY = endPos.y - startPos.y;
        distanceZ = endPos.z - startPos.z;
        this.id = id;
        move_interpolation_type = move_interpolation;
    }

    public Animation(MyModelInstance instance, Vector3 start, Vector3 end, float millis_time, final Vector3 startR_angles, final Vector3 endR_angles, Interpolation move_interpolation, Interpolation rotation_interpolation, String id) {
        is3D = true;
        delayed_call = false;
        modelInstance = instance;
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
        move_interpolation_type = move_interpolation;
        rotation_interpolation_type = rotation_interpolation;
    }

    public Animation(MyModelInstance instance, Vector3 end, float millis_time, final Vector3 endR_angles, Interpolation move_interpolation, Interpolation rotation_interpolation, String id) {
        is3D = true;
        delayed_call = true;
        modelInstance = instance;
        start_time = -1;
        endPos = end;
        duration = millis_time;
        end_rotation_angles = endR_angles;
        this.id = id;
        move_interpolation_type = move_interpolation;
        rotation_interpolation_type = rotation_interpolation;
    }

    public Animation(MyModelInstance instance, Vector3 end, float millis_time, Interpolation move_interpolation, String id) {
        is3D = true;
        delayed_call = true;
        modelInstance = instance;
        start_time = -1;
        endPos = end;
        duration = millis_time;
        this.id = id;
        move_interpolation_type = move_interpolation;
    }

    public void startAnimation() {
        if (delayed_call) {
            if (end_rotation_angles == null) {
                modelInstance.transform.getTranslation(startPos);
                distanceX = endPos.x - startPos.x;
                distanceY = endPos.y - startPos.y;
                distanceZ = endPos.z - startPos.z;
            } else {
                modelInstance.transform.getTranslation(startPos);
                start_rotation_angles = modelInstance.global_rotations.cpy();
                delta_angleX = end_rotation_angles.x - start_rotation_angles.x;
                delta_angleY = end_rotation_angles.y - start_rotation_angles.y;
                delta_angleZ = end_rotation_angles.z - start_rotation_angles.z;
                distanceX = endPos.x - startPos.x;
                distanceY = endPos.y - startPos.y;
                distanceZ = endPos.z - startPos.z;
            }
        }
        start_time = TimeUtils.millis();
    }

    public void update() {
        float delta_time = TimeUtils.timeSinceMillis(start_time) / duration;
        if (start_rotation_angles != null && end_rotation_angles != null) {
            float XrotAng = start_rotation_angles.x + delta_angleX * rotation_interpolation_type.apply(delta_time);
            float YrotAng = start_rotation_angles.y + delta_angleY * rotation_interpolation_type.apply(delta_time);
            float ZrotAng = start_rotation_angles.z + delta_angleZ * rotation_interpolation_type.apply(delta_time);
            modelInstance.setToWorldRotation(new Vector3(XrotAng,YrotAng,ZrotAng));
        }

        modelInstance.transform.setTranslation(new Vector3(startPos.x + distanceX * move_interpolation_type.apply(delta_time), startPos.y + distanceY * move_interpolation_type.apply(delta_time), startPos.z + distanceZ * move_interpolation_type.apply(delta_time)));
    }

    public void lastUpdate() {
        float delta_time = 1;
        if (start_rotation_angles != null && end_rotation_angles != null) {
            float XrotAng = start_rotation_angles.x + delta_angleX * rotation_interpolation_type.apply(delta_time);
            float YrotAng = start_rotation_angles.y + delta_angleY * rotation_interpolation_type.apply(delta_time);
            float ZrotAng = start_rotation_angles.z + delta_angleZ * rotation_interpolation_type.apply(delta_time);
            modelInstance.setToWorldRotation(new Vector3(XrotAng, YrotAng, ZrotAng));
        }
        modelInstance.transform.setTranslation(new Vector3(startPos.x + distanceX * move_interpolation_type.apply(delta_time), startPos.y + distanceY * move_interpolation_type.apply(delta_time), startPos.z + distanceZ * move_interpolation_type.apply(delta_time)));
    }


    public void freezeAnimation() {
        progress = TimeUtils.timeSinceMillis(start_time) / duration;
    }

    public void unfreezeAnimation() {
        start_time = (long) (TimeUtils.millis() - (duration * progress));
    }
}
