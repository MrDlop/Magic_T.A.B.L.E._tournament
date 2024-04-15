package com.mygdx.tablegame.tools;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.tablegame.game_logic.Server;

public class CameraAnimation {
    public long start_time;
    public Vector3 startPos;
    public Vector3 endPos;
    public float duration;
    public float distanceX;
    public float distanceY;
    public float distanceZ;
    public Vector3 look_at_pos;
    public Vector3 start_direction;
    public float angle;
    public Vector3 rotationAxis;
    public Vector3 startUp;
    public Vector3 targetDirection;
    public Camera camera;
    public String id;
    public boolean started = false;
    Interpolation moveInterpolationType;
    Interpolation rotationInterpolationType;

    public CameraAnimation(final Vector3 start, final Vector3 end, float millis_time, final Vector3 look_at_pos, Interpolation moveInterpolationType, Interpolation rotationInterpolationType, String id, Camera cam) {
        start_time = -1;
        startPos = start;
        endPos = end;
        duration = millis_time;
        this.look_at_pos = look_at_pos;
        this.id = id;
        distanceX = endPos.x - startPos.x;
        distanceY = endPos.y - startPos.y;
        distanceZ = endPos.z - startPos.z;
        targetDirection = new Vector3(look_at_pos.x - endPos.x, look_at_pos.y - endPos.y, look_at_pos.z - endPos.z).nor();
        camera = cam;
        start_direction = new Vector3();
        startUp = new Vector3();
        rotationAxis = new Vector3();
        this.moveInterpolationType=moveInterpolationType;
        this.rotationInterpolationType=rotationInterpolationType;
    }

    public void startAnimation() {
        start_direction = camera.direction.cpy().nor();
        startUp = camera.up.cpy().nor();
        rotationAxis.set(start_direction.cpy().crs(targetDirection).nor());
        angle = (float) (Math.acos(start_direction.x * targetDirection.x + start_direction.y * targetDirection.y + start_direction.z * targetDirection.z)) / 2f;
        start_time = TimeUtils.millis();
        camera.position.set(startPos);
        camera.update();
        started = true;
    }

    public void updateState() {
        if (started && TimeUtils.timeSinceMillis(start_time) <= duration) {
            camera.position.set(startPos.x + moveInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration) * distanceX, startPos.y + moveInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration) * distanceY, startPos.z + moveInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration) * distanceZ);
            float current_angle = angle * rotationInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration);
            Vector3 tmp3 = rotationAxis.cpy().scl((float) Math.sin(current_angle));
            Quaternion quaternion = new Quaternion(tmp3.x, tmp3.y, tmp3.z, (float) (Math.cos(current_angle)));
            Vector3 tmp = start_direction.cpy();
            camera.direction.set(tmp.mul(quaternion));
            camera.up.set(MakeCameraUpVector());
            camera.update();
//            System.out.println(new Vector3(startPos.x + moveInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration) * distanceX, startPos.y + moveInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration) * distanceY, startPos.z + moveInterpolationType.apply(TimeUtils.timeSinceMillis(start_time) / duration) * distanceZ));
//            System.out.println(startPos.toString()+endPos.toString());
        }

    }

    private Vector3 MakeCameraUpVector() {
        if (camera == null) return Vector3.Y.cpy();
        Vector3 CameraLeft = Vector3.Y.cpy().crs(camera.direction.cpy()).nor();
        return camera.direction.cpy().crs(CameraLeft).nor();
    }
}
