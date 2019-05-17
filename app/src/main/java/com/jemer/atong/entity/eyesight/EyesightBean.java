/**
 * 
 */
package com.jemer.atong.entity.eyesight;

import java.io.Serializable;

public class EyesightBean implements Serializable {

    private int direction;  //方向  1234  上下左右
    private float eyesight; //视力等级
    private float multiple;   //倍数

    public EyesightBean(int direction, float eyesight, float multiple) {
        this.direction = direction;
        this.eyesight = eyesight;
        this.multiple = multiple;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getMultiple() {
        return multiple;
    }

    public void setMultiple(float multiple) {
        this.multiple = multiple;
    }

    public float getEyesight() {
        return eyesight;
    }

    public void setEyesight(float eyesight) {
        this.eyesight = eyesight;
    }
}
