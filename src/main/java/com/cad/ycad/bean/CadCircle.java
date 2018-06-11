package com.cad.ycad.bean;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 16:01
 */
public class CadCircle extends CadBean{

    public CadCircle() {
    }

    public CadCircle(String type, String handle, String parentHandle, String layerName, String color, Double sx, Double sy, Double radius) {
        super(type, handle, parentHandle, layerName, color);
        this.sx = sx;
        this.sy = sy;
        this.radius = radius;
    }

    //起点x 10
    private Double sx;
    //起点y 20
    private Double sy;
    //半径 40
    private Double radius;

    public Double getSx() {
        return sx;
    }

    public void setSx(Double sx) {
        this.sx = sx;
    }

    public Double getSy() {
        return sy;
    }

    public void setSy(Double sy) {
        this.sy = sy;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "CadCircle{" +
                "sx=" + sx +
                ", sy=" + sy +
                ", radius=" + radius +
                ", type='" + type + '\'' +
                ", handle='" + handle + '\'' +
                ", parentHandle='" + parentHandle + '\'' +
                ", layerName='" + layerName + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
