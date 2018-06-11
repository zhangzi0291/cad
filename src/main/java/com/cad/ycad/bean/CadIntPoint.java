package com.cad.ycad.bean;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/7 9:49
 */
public class CadIntPoint {

    public CadIntPoint() {
    }

    public CadIntPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    public CadIntPoint(Double x, Double y,Integer sx, Integer sy) {
        this.x = (int)(x*sx);
        this.y = sy-(int)(y*sy);
    }

    private Integer x;
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
