package uz.mv.config;

import java.util.regex.Pattern;

public class Emojis {
    public static final String UNDOV = "❗";
    public static final String BLUE_RHOMBUS = "\uD83D\uDD39";
    public static final String PHONE = "\uD83D\uDCDE";
    public static final String[] numbers = new String[]{"0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};
    public static final String previous = "⬅️";
    public static final String next = "➡️";
    public static final String disk = "\uD83D\uDCBE";
    public static final String download = "\uD83D\uDCE5";

    public static void main(String[] args) {
        System.out.println(Pattern.matches("([0-9]{2}(.|_)[0-9]{2}(.|_)[0-9]{4})","16_09.1999"));
    }
}


