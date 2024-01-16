package util.time;

public class GameTime {
    public static void sleep(int time) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
