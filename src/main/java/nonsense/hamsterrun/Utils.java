package nonsense.hamsterrun;

public class Utils {
    public static void clear(int[][] map, int i) {
        for(int x = 0; x< map.length; x++){
            for(int y=0; y<map[x].length; y++) {
                map[x][y] = i;
            }
        }
    }

    public static void column(int[][] map, int y,  int i) {
        for(int x = 0; x< map.length; x++){
                map[x][y] = i;
        }
    }

    public static void row(int[][] map, int x, int i) {
            for(int y=0; y<map[x].length; y++) {
                map[x][y] = i;
        }
    }

    public static String toString(int[][] map, Character ch1, Character ch2) {
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x< map.length; x++){
            for(int y=0; y<map[x].length; y++) {
                if (map[x][y]==0) {
                    if (ch1 == null) {
                        sb.append("" + map[x][y]);
                    } else {
                        sb.append("" + ch1);
                    }
                } else {
                    if (ch2 == null) {
                        sb.append("" + map[x][y]);
                    } else {
                        sb.append("" + ch2);
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
