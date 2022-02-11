package uz.mv.util;

public class Checks {

    public static boolean checkName(String text) {
        return text.matches("[A-Z][A-Za-z ']*");
    }
}
