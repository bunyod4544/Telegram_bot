package uz.mv.util;

import org.apache.commons.lang3.RandomStringUtils;

public class BaseUtil {
    public static String genCode(){
        return RandomStringUtils.random(8,true,true);
    }
}
