package com.cad.ycad.bean;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 15:09
 */
public class CadArc extends CadBean{

    public CadArc() {
    }

    public CadArc(String type, String handle, String parentHandle, String layerName, String color, Double sx, Double sy, Double radius, Double sangle, Double eangle) {
        super(type, handle, parentHandle, layerName, color);
        this.sx = sx;
        this.sy = sy;
        this.radius = radius;
        this.sangle = sangle;
        this.eangle = eangle;
    }

    //起点x 10
    private Double sx;
    //起点y 20
    private Double sy;
    //半径 40
    private Double radius;
    //起点角度 50
    private Double sangle;
    //端点角度 51
    private Double eangle;

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

    public Double getSangle() {
        return sangle;
    }

    public void setSangle(Double sangle) {
        this.sangle = sangle;
    }

    public Double getEangle() {
        if(eangle == 0){
            return 360.0;
        }
        return eangle;
    }

    public void setEangle(Double eangle) {
        this.eangle = eangle;
    }

    @Override
    public String toString() {
        return "CadArc{" +
                "sx=" + sx +
                ", sy=" + sy +
                ", radius=" + radius +
                ", sangle=" + sangle +
                ", eangle=" + eangle +
                ", type='" + type + '\'' +
                ", handle='" + handle + '\'' +
                ", parentHandle='" + parentHandle + '\'' +
                ", layerName='" + layerName + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
