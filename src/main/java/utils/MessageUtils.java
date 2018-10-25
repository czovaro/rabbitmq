package utils;

public class MessageUtils {

    public static String getMessage(String[] strings){
        if(strings.length < 1) {
            return "Hello World!";
        }
        return joinStrings(strings, " ", 1);
    }

    public static String getSeverity(String strings[]){
        if(strings.length < 1){
            return "info";
        }
        return strings[0];
    }

    public static String getRouting(String[] strings){
        if (strings.length < 1){
            return "anonymous.info";
        }
        return strings[0];
    }

    public static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0) return "";
        if (length < startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }

        return words.toString();
    }
}
