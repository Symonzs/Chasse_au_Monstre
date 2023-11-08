package view.play;

import java.util.Comparator;

public class NumberInStringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return extractInt(o1) - extractInt(o2);
    }

    int extractInt(String s) {
        String num = s.replaceAll("\\D", "");
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }

}
