package com.cad.ycad;

import com.cad.ycad.bean.CadIntPoint;
import com.cad.ycad.bean.CadPoint;

import java.io.File;

/**
 * 类的描述
 *
 * @Author zhengxiangnan
 * @Date 2018/6/4 9:47
 */
public class Utils {

    /**
     * Transformation matrix element.
     */
    private static double              m00             = 1;
    /**
     * Transformation matrix element.
     */
    private static double              m01             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m02             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m03             = 0;

    /**
     * Transformation matrix element.
     */
    private static double              m10             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m11             = 1;
    /**
     * Transformation matrix element.
     */
    private static double              m12             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m13             = 0;

    /**
     * Transformation matrix element.
     */
    private static double              m20             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m21             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m22             = 1;
    /**
     * Transformation matrix element.
     */
    private static double              m23             = 0;

    /**
     * Transformation matrix element.
     */
    private static double              m30             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m31             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m32             = 0;
    /**
     * Transformation matrix element.
     */
    private static double              m33             = 1;

    private static CadPoint calc_extmax = new CadPoint(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);

    private static CadPoint calc_extmin = new CadPoint(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY);
    
    public static String getDataDir(Class c) {
        File dir = new File(System.getProperty("user.dir"));
        dir = new File(dir, "src");
        dir = new File(dir, "main");
        dir = new File(dir, "resources");

        return dir.toString() + File.separator;
    }



    public static CadIntPoint MCS_to_SCS_calc_extents(Double x, Double y, Double z)
    {
        CadPoint point = mtxTransformPoint(x,y,z);

        if (calc_extmin.getX() > point.getX()) calc_extmin.setX(point.getX());
        if (calc_extmin.getY() > point.getY()) calc_extmin.setY(point.getY());
        if (calc_extmin.getZ() > point.getZ()) calc_extmin.setZ(point.getZ());
        if (calc_extmax.getX() < point.getX()) calc_extmax.setX(point.getX());
        if (calc_extmax.getY() < point.getY()) calc_extmax.setY(point.getY());
        if (calc_extmax.getZ() < point.getZ()) calc_extmax.setZ(point.getZ());

        return new CadIntPoint((int)Math.round(x),(int)Math.round(y));
    }

    public static CadIntPoint MCS_to_SCS_calc_extents(Double x, Double y)
    {
        CadPoint point = mtxTransformPoint(x,y,0.0);

        if (calc_extmin.getX() > point.getX()) calc_extmin.setX(point.getX());
        if (calc_extmin.getY() > point.getY()) calc_extmin.setY(point.getY());
        if (calc_extmin.getZ() > point.getZ()) calc_extmin.setZ(point.getZ());
        if (calc_extmax.getX() < point.getX()) calc_extmax.setX(point.getX());
        if (calc_extmax.getY() < point.getY()) calc_extmax.setY(point.getY());
        if (calc_extmax.getZ() < point.getZ()) calc_extmax.setZ(point.getZ());

        return new CadIntPoint((int)Math.round(x),(int)Math.round(y));
    }

    public static CadPoint mtxTransformPoint(Double x, Double y, Double z)
    {
        double ptx = m00 * x + m01 * y + m02 * z + m03;
        double pty = m10 * x + m11 * y + m12 * z + m13;
        double ptz = m20 * x + m21 * y + m22 * z + m23;

        x = ptx;
        y = pty;
        z = ptz;

        return new CadPoint(x,y,z);
    }

}
