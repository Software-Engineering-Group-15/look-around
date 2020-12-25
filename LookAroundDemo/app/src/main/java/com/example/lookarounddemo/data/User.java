package com.example.lookarounddemo.data;

/**
 *
 * @ClassName: User
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yiw
 * @date 2015-12-28 下午3:45:04
 *
 */
public class User {
    private static String userID = "02200059";
    private static String name = "Bob";
    private static double la;
    private static double lo;

    public static String getName() {
        return name;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static void setUserID(String userID) {
        User.userID = userID;
    }

    public static double getLa() {
        return la;
    }

    public static void setLa(double la) {
        User.la = la;
    }

    public static double getLo() {
        return lo;
    }

    public static void setLo(double lo) {
        User.lo = lo;
    }
}
