package com.cad.ycad.bean;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 15:20
 */
public class CadBean {

    public CadBean() {
    }

    public CadBean(String type, String handle, String parentHandle, String layerName, String color) {
        this.type = type;
        this.handle = handle;
        this.parentHandle = parentHandle;
        this.layerName = layerName;
        this.color = color;
    }

    //类型 0
    protected String type;
    //句柄 5
    protected String handle;
    //父句柄 330
    protected String parentHandle;
    //图层名称 8
    protected String layerName;
    //颜色 62
    protected String color;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getParentHandle() {
        return parentHandle;
    }

    public void setParentHandle(String parentHandle) {
        this.parentHandle = parentHandle;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        switch (type){
            case "Text":
                return ((CadText)this).toString();
            case "ARC":
                return ((CadArc)this).toString();
            case "LINE":
                return ((CadLine)this).toString();
            case "CIRCLE":
                return ((CadCircle)this).toString();
        }
        return "CadBean{" +
                "type='" + type + '\'' +
                ", handle='" + handle + '\'' +
                ", parentHandle='" + parentHandle + '\'' +
                ", layerName='" + layerName + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
