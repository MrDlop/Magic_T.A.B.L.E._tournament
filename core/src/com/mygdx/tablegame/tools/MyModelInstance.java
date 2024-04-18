package com.mygdx.tablegame.tools;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class MyModelInstance extends ModelInstance {
    Vector3 global_rotations;

    public MyModelInstance(Model model) {
        super(model);
        global_rotations=new Vector3(0,0,0);
    }
    public Vector3 getCurrentRotation(){
        return global_rotations;
    }
    public void setToWorldRotation(float x,float y,float z){
        setToWorldRotation(new Vector3(x,y,z));
    }

    public void setToWorldRotation(Vector3 euler_angles){
        float XrotAng = euler_angles.x;
        float YrotAng = euler_angles.y;
        float ZrotAng = euler_angles.z;
        Quaternion quaternionX = new Quaternion(MathUtils.sinDeg(XrotAng / 2), 0, 0, MathUtils.cosDeg(XrotAng / 2));
        Quaternion quaternionY = new Quaternion(0, MathUtils.sinDeg(YrotAng / 2), 0, MathUtils.cosDeg(YrotAng / 2));
        Quaternion quaternionZ = new Quaternion(0, 0, MathUtils.sinDeg(ZrotAng / 2), MathUtils.cosDeg(ZrotAng / 2));
        Vector3 scale = new Vector3();
        Vector3 translation = new Vector3();
        transform.getScale(scale);
        transform.getTranslation(translation);
        float[] tmp = new float[16];
        quaternionX.mul(quaternionY.mul(quaternionZ)).toMatrix(tmp);
        transform=transform.idt().mul(new Matrix4(tmp));
        transform.scale(scale.x, scale.y, scale.z);
        transform.setTranslation(translation);
        global_rotations=euler_angles.cpy();
//        Vector3 translation=new Vector3();
//        Vector3 scale=new Vector3();
//        transform.getTranslation(translation);
//        transform.getScale(scale);
//        Matrix4 target=setMatrixFromAxis(
//                forward.nor(),right.nor(),up.nor());
//        transform=target.cpy();
//        transform.scale(scale.x,scale.y,scale.z);
//        transform.setTranslation(translation);
//    }
//    public static Vector3 getRightVector(Vector3 forwardVector)
//    {
//        return new Vector3(-forwardVector.z, 0, forwardVector.x);
//    }
//    static Matrix4 setMatrixFromAxis(Vector3 x,Vector3 y,Vector3 z){
//        return new Matrix4(new float[]{
//                x.x,y.x,z.x,0,
//                x.y,y.y,z.y,0,
//                x.z,y.z,z.z,0,
//                0,0,0,1});
   }
}
