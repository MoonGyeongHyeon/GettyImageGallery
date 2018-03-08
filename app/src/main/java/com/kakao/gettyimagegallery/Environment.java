package com.kakao.gettyimagegallery;

/**
 * Created by khan.moon on 2018. 3. 8..
 */

public class Environment {
    public static boolean showLog = true;
    public static String baseUrl = "http://www.gettyimagesgallery.com/";
    public static String mainUrl = "collections/archive/slim-aarons.aspx/";

    private static Type type = Type.DEVELOP;

    public static void configure() {
        switch (type) {
            case DEVELOP:
                showLog = true;
                baseUrl = "http://www.gettyimagesgallery.com/";
                mainUrl = "collections/archive/slim-aarons.aspx/";
                break;
            case PRODUCTION:
                showLog = false;
                baseUrl = "http://www.gettyimagesgallery.com/";
                mainUrl = "collections/archive/slim-aarons.aspx/";
                break;
        }
    }

    public enum Type {
        DEVELOP,
        PRODUCTION
    }
}
