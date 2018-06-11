package com.cad.ycad;

import com.cad.ycad.bean.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/1 10:16
 */
public class CadDemo {

    //图片大小
    static int width=2500;
    static int height=2000;

    private static Double maxx = Double.MIN_VALUE;
    private static Double minx = Double.MAX_VALUE;
    private static Double maxy = Double.MIN_VALUE;
    private static Double miny = Double.MAX_VALUE;
    //坐标范围
    private static Double maxx2 = 229.2581184316173;
    private static Double minx2 = -0.0128164049386896;
    private static Double maxy2 = 156.4760747466656;
    private static Double miny2 = -0.0090630292066862;

    public static void main(String[] args) throws Exception {
//        String filePaht = "C:\\Users\\north\\Desktop\\东乡人民医院\\3楼.dxf";
//        String outPath = "d:/123/test.jpg";
//        String antName = "ant";

        Scanner scan = new Scanner(System.in);
        String filePaht = "";
        String outPath = "";
        String antName = "";

        System.out.println("请输入dxf文件路径：");
        if (scan.hasNext()) {
            filePaht = scan.next();
        }
        System.out.println("请输入jpg文件输出路径：");
        if (scan.hasNext()) {
            outPath = scan.next();
        }
        System.out.println("请输入天线名称：");
        if (scan.hasNext()) {
            antName = scan.next();
        }

        readDxf(filePaht,antName,outPath);
    }



    public static void readDxf(String filePath,String antName,String outPath) throws Exception {
        Path path = Paths.get(filePath);
        //读取dxf所有行
        List<String> lineList = Files.readAllLines(path);

        //获取几个头部SECTION
        List<List<String[]>> sectionList = new ArrayList<>();
        List<String[]> section = new ArrayList<>();
        for (Integer i = 0; i < lineList.size(); i++) {
            String[] value = readCodes(lineList, i);
            i++;
            if ("0".equals(value[0]) && "SECTION".equals(value[1]) && i > 1) {
                sectionList.add(section);
                section = new ArrayList<>();
            }
            section.add(value);
        }
        sectionList.add(section);

        //获取dxf中坐标范围
        List<List<String[]>> header = getNode(sectionList.get(SECTION_TYPE.HEADER.getIndex()), "9");
        for(List<String[]> node : header){
            List<List<String[]>> l = getNode(node, "0");
            //获取最大最小坐标
            getMaxMinXY(l);
        }

        //获取entites节点
        List<List<String[]>> list = getNode(sectionList.get(SECTION_TYPE.ENTITIES.getIndex()), "0");
        List<CadBean> resultList = new ArrayList<>();
        List<CadBean> antList = new ArrayList<>();
        //节点下
        for(List<String[]> node : list){
            List<List<String[]>> l = getNode(node, "0");
            //获取各种entites中各种节点 存放在resulteList
            addNode(resultList,l);
            //获取天线名称为antName开头的文字作为天线的位置
            getAnt(antList,l,antName);
        }
        //按照图层分组
        Map<String,List<CadBean>> layerMap = resultList.stream().collect(Collectors.groupingBy(CadBean::getLayerName));
        Path p = Paths.get(outPath);
        //删除文件重新生成
        Files.deleteIfExists(p);
        for(Map.Entry<String,List<CadBean>> e : layerMap.entrySet()){
            drawPic(e.getValue(),p);
        }
        //画天线位置
        drawAnt(antList,p);

//        drawPic(layerMap.get("GLASS"),p);
    }

    /**
     *  绘图
     * @param list
     * @param filePath
     * @throws IOException
     */
    public static void drawPic(List<CadBean> list,Path filePath) throws IOException {
        BufferedImage image = null;
        //图片存在继续画，否则新建
        if(Files.exists(filePath)){
            image =  ImageIO.read(filePath.toFile());
        }else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D graphics = image.createGraphics();
        //设置字体
        Font font = new Font("宋体", Font.TYPE1_FONT,20);
        graphics.setFont(font);
        //消除锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(CadBean bean : list){
            drawBean(graphics,bean, java.awt.Color.WHITE);
        }

        graphics.dispose();

        ImageIO.write(image,"JPEG",filePath.toFile());
    }

    /**
     * 绘天线
     * @param list
     * @param filePath
     * @throws IOException
     */
    public static void drawAnt(List<CadBean> list,Path filePath) throws IOException {
        //图片存在继续画，否则新建
        BufferedImage image = null;
        if(Files.exists(filePath)){
            image =  ImageIO.read(filePath.toFile());
        }else {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        Graphics2D graphics = image.createGraphics();
        //设置字体
        Font font = new Font("宋体", Font.TYPE1_FONT,20);
        graphics.setFont(font);
        //消除锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(CadBean bean : list){
            CadText text = (CadText)bean;
            graphics.setColor(java.awt.Color.PINK);
            if(text.getSy()<miny) {
                text.setSy(maxy - text.getSy());
            }
            CadIntPoint bip = new CadIntPoint(text.getSx().intValue(),text.getSy().intValue());

            double x = (bip.getX()-minx2)/(maxx2-minx2);
            double y = (bip.getY()-miny2)/(maxy2-miny2);
            bip = new CadIntPoint(x,y,width,height);
//            Integer radius = (int)((circle.getRadius()-minx2)/(maxx2-minx2)*width);
            int ovalSize = 25;
            graphics.fillOval(bip.getX()-ovalSize,bip.getY()-ovalSize,ovalSize,ovalSize);
            graphics.setColor(java.awt.Color.WHITE);
//            graphics.drawString(bip.getX()+"==="+bip.getY(),bip.getX(),bip.getY());
            graphics.drawString(text.getText(),bip.getX(),bip.getY());
//            graphics.fillOval(bip.getX()-ovalSize,bip.getY()-ovalSize,ovalSize,ovalSize);
        }

        graphics.dispose();

        ImageIO.write(image,"JPEG",filePath.toFile());
    }

    public static void drawBean(Graphics2D graphics, CadBean bean, java.awt.Color color){
        int px = 0;
        int py = 0;

//        if(bean.getColor()!=null){
//            graphics.setColor(new java.awt.Color(Integer.valueOf(bean.getColor())));
//        }else {
            graphics.setColor(color);
//        }
        CadIntPoint bip = null;
        CadIntPoint eip = null;
        Double x=null;
        Double y=null;
        switch (bean.getType()){
            case "LINE":
                CadLine line = (CadLine)bean;
                graphics.setColor(color);

                bip = new CadIntPoint(line.getSx().intValue(),line.getSy().intValue());
                eip = new CadIntPoint(line.getEx().intValue(),line.getEy().intValue());

                x = (bip.getX()-minx2)/(maxx2-minx2);
                y = (bip.getY()-miny2)/(maxy2-miny2);
                bip = new CadIntPoint(x,y,width,height);

                x = (eip.getX()-minx2)/(maxx2-minx2);
                y = (eip.getY()-miny2)/(maxy2-miny2);
                eip =  new CadIntPoint(x,y,width,height);
                graphics.drawLine(bip.getX(),bip.getY(),eip.getX(),eip.getY());
                break;
            case "TEXT": {
                CadText text = (CadText) bean;
                graphics.setColor(color);
                if(text.getSy()<miny){
                    text.setSy(maxy - text.getSy());
                }

                bip = new CadIntPoint(text.getSx().intValue(), text.getSy().intValue());

                x = (bip.getX() - minx2) / (maxx - minx2);
                y = (bip.getY() - miny2) / (maxy - miny2);
                bip = new CadIntPoint(x, y, width, height);

//                graphics.drawString(text.getText(), bip.getX(), bip.getY());
                break;
            }
            case "MTEXT": {
                CadText text = (CadText) bean;
                graphics.setColor(color);

                bip = new CadIntPoint(text.getSx().intValue(), text.getSy().intValue());

                x = (bip.getX() - minx2) / (maxx2 - minx2);
                y = (bip.getY() - miny2) / (maxy2 - miny2);
                bip = new CadIntPoint(x, y, width, height);

//                graphics.drawString(text.getText(), bip.getX(), bip.getY());
                break;
            }
            case "ATTRIB": {
                CadText text = (CadText) bean;
                graphics.setColor(color);

                bip = new CadIntPoint(text.getSx().intValue(), text.getSy().intValue());

                x = (bip.getX() - minx2) / (maxx2 - minx2);
                y = (bip.getY() - miny2) / (maxy2 - miny2);
                bip = new CadIntPoint(x, y, width, height);

//                graphics.drawString(text.getText(), bip.getX(), bip.getY());
                break;
            }
            case "CIRCLE":
                CadCircle circle = (CadCircle) bean;
                graphics.setColor(color);

                bip = new CadIntPoint(circle.getSx().intValue(),circle.getSy().intValue());

                x = (bip.getX()-minx2)/(maxx-minx2);
                y = (bip.getY()-miny2)/(maxy-miny2);
                bip = new CadIntPoint(x,y,width,height);

                Integer radius = (int)(circle.getRadius()/(maxx2-minx2)*width);

                graphics.drawArc(bip.getX(),bip.getY(),radius*2,radius*2,
                        0,360);
                break;
            case "ARC":
                CadArc arc = (CadArc) bean;
                graphics.setColor(color);

                bip = new CadIntPoint(arc.getSx().intValue(),arc.getSy().intValue());
                x = (bip.getX()-minx2)/(maxx-minx2);
                y = (bip.getY()-miny2)/(maxy-miny2);
                bip = new CadIntPoint(x,y,width,height);

                Integer radius2 = (int)(arc.getRadius()/(maxx2-minx2)*width);
                graphics.drawArc(bip.getX()-radius2,bip.getY()-radius2,radius2*2,radius2*2,
                        arc.getSangle().intValue(),arc.getEangle().intValue()-arc.getSangle().intValue());
                break;
//            case "LWPOLYLINE":
//                CadLwpolyline lwpolyline = (CadLwpolyline) bean;
//                graphics.setColor(color);
//
//                for(int i=0;i<lwpolyline.getCoordinate().size()-1;i++) {
//                    Double[] coordinate = lwpolyline.getCoordinate().get(i);
//                    Double[] coordinate2 = lwpolyline.getCoordinate().get(i+1);
//                    if (coordinate[1] < miny) {
//                        coordinate[1]=(maxy - coordinate[1]);
//                    }
//                    if (coordinate2[1] < miny) {
//                        coordinate2[1]=(maxy - coordinate2[1]);
//                    }
//
//                    bip = new CadIntPoint(coordinate[0].intValue(), coordinate[1].intValue());
//                    eip = new CadIntPoint(coordinate2[0].intValue(), coordinate2[0].intValue());
//
//                    x = (bip.getX() - minx2) / (maxx - minx2);
//                    y = (bip.getY() - miny2) / (maxy - miny2);
//                    bip = new CadIntPoint(x, y, width, height);
//
//                    x = (eip.getX()-minx2)/(maxx-minx2);
//                    y = (eip.getY()-miny2)/(maxy-miny2);
//                    eip =  new CadIntPoint(x,y,width,height);
//
//                    graphics.drawLine(bip.getX(), bip.getY(), eip.getX(), eip.getY());
//                }
//                break;
            default:
                break;
        }
    }

    /**
     * 获取天线对象，从文字里获取天线位置，antName全部小写
     * @param returnlist
     * @param list
     * @param antName
     * @return
     */
    public static List<CadBean> getAnt(List<CadBean> returnlist , List<List<String[]>> list,String antName){
        CadText bean = null;
        String type = "";
        for(List<String[]> l : list){
            for(String[] as : l){
                if("0".equals(as[0])){
                    if(bean != null) {
                        returnlist.add(bean);
                    }
                    //从下面3种类型里获取
                    switch (as[1]){
                        case "TEXT":
                            bean = new CadText();
                            type = "TEXT";
                            break;
                        case "MTEXT":
                            bean = new CadText();
                            type = "TEXT";
                            break;
                        case "ATTRIB":
                            bean = new CadText();
                            type = "TEXT";
                            break;
                        default:
                            break;
                    }
                }
                if(bean==null){
                    continue;
                }

                if("0".equals(as[0])){
                    bean.setType(as[1]);
                }
                if("5".equals(as[0])){
                    bean.setHandle(as[1]);
                }
                if("330".equals(as[0])){
                    bean.setParentHandle(as[1]);
                }
                if("8".equals(as[0])){
                    bean.setLayerName(as[1]);
                }
                if("62".equals(as[0])){
                    bean.setColor(as[1]);
                }
                switch (type){
                    case "TEXT":
                        if("10".equals(as[0])){
                            bean.setSx(Double.valueOf(as[1]));
                        }
                        if("20".equals(as[0])){
                            bean.setSy(Double.valueOf(as[1]));
                        }
                        if("11".equals(as[0])){
                            bean.setEx(Double.valueOf(as[1]));
                        }
                        if("21".equals(as[0])){
                            bean.setEy(Double.valueOf(as[1]));
                        }
                        if("1".equals(as[0])){
                            bean.setText(as[1]);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if(bean != null&&bean.getText().toLowerCase().startsWith(antName)) {
            returnlist.add(bean);
        }
        return returnlist;
    }

    /**
     * 获取节点下的所有节点
     * @param returnlist
     * @param list
     * @return
     */
    public static List<CadBean> addNode(List<CadBean> returnlist , List<List<String[]>> list){
        CadBean bean = null;
        String type = "";
        Double[] coordinate = new Double[2];
        for(List<String[]> l : list){
            for(String[] as : l){
                if("0".equals(as[0])){
                    if(bean != null) {
                        returnlist.add(bean);
                    }
                    switch (as[1]){
                        case "LINE":
                            bean = new CadLine();
                            type = "LINE";
                            break;
                        case "TEXT":
                            bean = new CadText();
                            type = "TEXT";
                            break;
                        case "MTEXT":
                            bean = new CadText();
                            type = "TEXT";
                            break;
                        case "ATTRIB":
                            bean = new CadText();
                            type = "TEXT";
                            break;
                        case "CIRCLE":
                            bean = new CadCircle();
                            type = "CIRCLE";
                            break;
                        case "ARC":
                            bean = new CadArc();
                            type = "ARC";
                            break;
                        case "LWPOLYLINE":
                            bean = new CadLwpolyline();
                            type = "LWPOLYLINE";
                            break;
                        default:
                            break;
                    }
                }
                if(bean==null){
                    continue;
                }

                if("10".equals(as[0])){
                    if(maxx<Double.valueOf(as[1])){
                        maxx = Double.valueOf(as[1]);
                    }
                    if(minx>Double.valueOf(as[1])){
                        minx = Double.valueOf(as[1]);
                    }
                }
                if("20".equals(as[0])){
                    if(maxy<Double.valueOf(as[1])){
                        maxy = Double.valueOf(as[1]);
                    }
                    if(miny>Double.valueOf(as[1])){
                        miny = Double.valueOf(as[1]);
                    }
                }
                if("11".equals(as[0])){
                    if(maxx<Double.valueOf(as[1])){
                        maxx = Double.valueOf(as[1]);
                    }
                    if(minx>Double.valueOf(as[1])){
                        minx = Double.valueOf(as[1]);
                    }
                }
                if("21".equals(as[0])){
                    if(maxy<Double.valueOf(as[1])){
                        maxy = Double.valueOf(as[1]);
                    }
                    if(miny>Double.valueOf(as[1])){
                        miny = Double.valueOf(as[1]);
                    }
                }

                if("0".equals(as[0])){
                    bean.setType(as[1]);
                }
                if("5".equals(as[0])){
                    bean.setHandle(as[1]);
                }
                if("330".equals(as[0])){
                    bean.setParentHandle(as[1]);
                }
                if("8".equals(as[0])){
                    bean.setLayerName(as[1]);
                }
                if("62".equals(as[0])){
                    bean.setColor(as[1]);
                }
                switch (type){
                    case "LINE":
                        if("10".equals(as[0])){
                            ((CadLine)bean).setSx(Double.valueOf(as[1]));
                        }
                        if("20".equals(as[0])){
                            ((CadLine)bean).setSy(Double.valueOf(as[1]));
                        }
                        if("11".equals(as[0])){
                            ((CadLine)bean).setEx(Double.valueOf(as[1]));
                        }
                        if("21".equals(as[0])){
                            ((CadLine)bean).setEy(Double.valueOf(as[1]));
                        }
                        break;
                    case "TEXT":
                        if("10".equals(as[0])){
                            ((CadText)bean).setSx(Double.valueOf(as[1]));
                        }
                        if("20".equals(as[0])){
                            ((CadText)bean).setSy(Double.valueOf(as[1]));
                        }
                        if("11".equals(as[0])){
                            ((CadText)bean).setEx(Double.valueOf(as[1]));
                        }
                        if("21".equals(as[0])){
                            ((CadText)bean).setEy(Double.valueOf(as[1]));
                        }
                        if("1".equals(as[0])){
                            ((CadText)bean).setText(as[1]);
                        }
                        break;
                    case "CIRCLE":
                        if("10".equals(as[0])){
                            ((CadCircle)bean).setSx(Double.valueOf(as[1]));
                        }
                        if("20".equals(as[0])){
                            ((CadCircle)bean).setSy(Double.valueOf(as[1]));
                        }
                        if("40".equals(as[0])){
                           ((CadCircle)bean).setRadius(Double.valueOf(as[1]));
                        }
                        break;
                    case "ARC":
                        if("10".equals(as[0])){
                            ((CadArc)bean).setSx(Double.valueOf(as[1]));
                        }
                        if("20".equals(as[0])){
                            ((CadArc)bean).setSy(Double.valueOf(as[1]));
                        }
                        if("40".equals(as[0])){
                            ((CadArc)bean).setRadius(Double.valueOf(as[1]));
                        }
                        if("50".equals(as[0])){
                            ((CadArc)bean).setSangle(Double.valueOf(as[1]));
                        }
                        if("51".equals(as[0])){
                            ((CadArc)bean).setEangle(Double.valueOf(as[1]));
                        }
                        break;
                    case "LWPOLYLINE":

                        if("10".equals(as[0])){
                            coordinate[0] = Double.valueOf(as[1]);
                        }
                        if("20".equals(as[0])){
                            coordinate[1] = Double.valueOf(as[1]);
                            ((CadLwpolyline)bean).addCoordinate(coordinate);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if(bean != null) {
            returnlist.add(bean);
        }
        return returnlist;
    }
    public static void printNode(List<List<String[]>> list){
        for(List<String[]> l : list){
            for(String[] as : l){
                if("0".equals(as[0])){
//                    break;
                }
                System.out.println(Arrays.toString(as));
            }
        }
    }

    public static void getMaxMinXY(List<List<String[]>> list) {
        for (int i = 0; i < list.size(); i++) {
            List<String[]> l = list.get(i);
            for (int j = 0; j < l.size(); j++) {
                String[] as = l.get(j);
                if ("9".equals(as[0]) && "$EXTMAX".equals(as[1])) {
                    j++;
                    as = l.get(j);
                    if (j >= l.size()) {
                        break;
                    }
                    maxx2 = Double.valueOf(as[1]);
                    j++;
                    as = l.get(j);
                    if (j >= l.size()) {
                        break;
                    }
                    maxy2 = Double.valueOf(as[1]);
                }
                if ("9".equals(as[0]) && "$EXTMIN".equals(as[1])) {
                    j++;
                    as = l.get(j);
                    if (j >= l.size()) {
                        break;
                    }
                    minx2 = Double.valueOf(as[1]);
                    j++;
                    as = l.get(j);
                    if (j >= l.size()) {
                        break;
                    }
                    miny2 = Double.valueOf(as[1]);
                }
            }
        }
    }

    public static void write(Path p,String s) throws Exception{

        Files.write(p,s.getBytes(), StandardOpenOption.APPEND);
        Files.write(p,"\r\n".getBytes(), StandardOpenOption.APPEND);

    }

    public static List<List<String[]>> getNode(List<String[]> section, String nodeNum) {
        List<List<String[]>> returnList = new ArrayList<>();
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < section.size(); i++) {
            String[] value = section.get(i);
            if (nodeNum.equals(value[0]) ) {
                if(list.size()!=0) {
                    returnList.add(list);
                }
                list = new ArrayList<>();
            }
            list.add(value);
        }
        returnList.add(list);
        return returnList;
    }

    public static String[] readCodes(List<String> lines, Integer i) {
        String[] value = new String[2];
        value[0] = lines.get(i).trim();
        i++;
        if (lines.size() == i) {
            return value;
        }
        value[1] = lines.get(i).trim();
        return value;
    }

    enum SECTION_TYPE {
        HEADER(0), CLASSES(1), TABLES(2), BLOCKS(3), ENTITIES(4);

        private Integer index;

        SECTION_TYPE(int index) {
            this.index = index;
        }

        public Integer getIndex() {
            return index;
        }
    }
}
