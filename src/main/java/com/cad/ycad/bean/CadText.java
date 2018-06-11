package com.cad.ycad.bean;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 15:09
 */
public class CadText extends CadBean{

    public CadText() {
    }

    public CadText(String type, String handle, String parentHandle, String layerName, String color, String text, Double sx, Double sy, Double ex, Double ey) {
        super(type, handle, parentHandle, layerName, color);
        this.text = text;
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
    }

    //文本内容 1
    private String text;
    //起点x 10
    private Double sx;
    //起点y 20
    private Double sy;
    //终点x 11
    private Double ex;
    //终点y 21
    private Double ey;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Double getCx() {
        return (this.sx+this.ey)/2;
    }
    public Double getCy() {
        return (this.sy+this.ey)/2;
    }
    @Override
    public String toString() {
        return "CadText{" +
                "text='" + text + '\'' +
                ", sx=" + sx +
                ", sy=" + sy +
                ", ex=" + ex +
                ", ey=" + ey +
                ", type='" + type + '\'' +
                ", handle='" + handle + '\'' +
                ", parentHandle='" + parentHandle + '\'' +
                ", layerName='" + layerName + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
