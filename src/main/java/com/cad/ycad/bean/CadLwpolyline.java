package com.cad.ycad.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 16:09
 */
public class CadLwpolyline extends CadBean{

    public CadLwpolyline() {
    }

    public CadLwpolyline(String type, String handle, String parentHandle, String layerName, String color, List<Double[]> coordinate) {
        super(type, handle, parentHandle, layerName, color);
        this.coordinate = coordinate;
    }

    //定点组 10，20
    List<Double[]> coordinate = new ArrayList<>();

    public List<Double[]> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(List<Double[]> coordinate) {
        this.coordinate = coordinate;
    }

    public void addCoordinate(Double[] coordinate){
        this.coordinate.add(coordinate);
    }
}
