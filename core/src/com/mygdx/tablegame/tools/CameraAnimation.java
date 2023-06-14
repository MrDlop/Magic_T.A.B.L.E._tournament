package com.mygdx.tablegame.tools;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.tablegame.game_logic.Server;

// класс анимаций камеры, временно не используется, находится в процессе доработки
public class CameraAnimation {
    public long start_time;
    public final Vector3 startPos;
    public final Vector3 endPos;
    public float duration;
    public float distanceX;
    public float distanceY;
    public float distanceZ;
    public final Vector3 look_at_pos;
    public Vector3 tmp_look_at;
    public final Vector3 camera_pos;
    public float XZrotAngle;
    public float YrotAngle;
    public float prevRotAngleXZ = 0;
    public float prevRotAngleY = 0;
    public final boolean is3D;
    public final String id;

    public CameraAnimation(final Vector3 start, final Vector3 end, float millis_time, final Vector3 look_at_pos, String id) {
        is3D = true;
        start_time = -1;
        startPos = start;
        endPos = end;
        camera_pos = Server.player_now.camera.position;
        this.look_at_pos = look_at_pos;
        duration = millis_time;
        distanceX = endPos.x - startPos.x;
        distanceY = endPos.y - startPos.y;
        distanceZ = endPos.z - startPos.z;
        Vector3 tmp = new Vector3(Server.player_now.camera.direction);
        tmp.setLength((float) Math.sqrt(Math.pow(look_at_pos.x - endPos.x, 2) + Math.pow(look_at_pos.y - endPos.y, 2) + Math.pow(look_at_pos.z - endPos.z, 2)));
        Vector3 dotC = new Vector3(endPos.x + tmp.x, endPos.y + tmp.y, endPos.z + tmp.z);
        float b = (float) Math.sqrt(Math.pow(dotC.x-endPos.x,2)+Math.pow(dotC.z-endPos.z,2));
        float c = b;
        float a = (float) Math.sqrt((Math.pow(look_at_pos.x - dotC.x, 2) + Math.pow(look_at_pos.z - dotC.z, 2)));
        float d= (float) Math.sqrt(Math.pow(look_at_pos.y-dotC.y,2));
        XZrotAngle = MathUtils.radiansToDegrees * MathUtils.acos((-b * b - c * c + a * a) / (-2 * b * c));
        tmp.set(look_at_pos.x, tmp.y, look_at_pos.z);
        tmp.setLength(c);
        YrotAngle = MathUtils.radiansToDegrees * MathUtils.acos((b * b + c * c - d * d) / (2 * b * c));
        float k = (look_at_pos.x - camera_pos.x) / (-1 * (look_at_pos.z - camera_pos.z));
        tmp_look_at = new Vector3(1, 0, 1 * k);
        this.id = id;
    }
}
