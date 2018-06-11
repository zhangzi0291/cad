package com.cad.ycad.bean;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 15:08
 */
public class CadLine extends CadBean{

    public CadLine() {

    }

    public CadLine(String type, String handle, String parentHandle, String layerName, String color, Double sx, Double sy, Double ex, Double ey) {
        super(type, handle, parentHandle, layerName, color);
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
    }

    //起点x 10
    private Double sx;
    //起点y 20
    private Double sy;
    //终点x 11
    private Double ex;
    //终点y 21
    private Double ey;


    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

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

    public Double getEx() {
        return ex;
    }

    public void setEx(Double ex) {
        this.ex = ex;
    }

    public Double getEy() {
        return ey;
    }

    public void setEy(Double ey) {
        this.ey = ey;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "CadLine{" +
                "handle='" + handle + '\'' +
                ", layerName='" + layerName + '\'' +
                ", sx=" + sx +
                ", sy=" + sy +
                ", ex=" + ex +
                ", ey=" + ey +
                ", color='" + color + '\'' +
                '}';
    }
}
