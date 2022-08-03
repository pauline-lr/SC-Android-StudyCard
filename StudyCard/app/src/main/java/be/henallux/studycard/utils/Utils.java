package be.henallux.studycard.utils;

public class Utils {
    public static String getFrontCardTextToList(String frontCardText, int position) {
        String textFrontCard = frontCardText.substring(0, Math.min(frontCardText.length(), 20));
        if (frontCardText.length() > 20)
            textFrontCard = textFrontCard + "...";
        textFrontCard = (position + 1) + " - " + textFrontCard;
        return textFrontCard;
    }
}
