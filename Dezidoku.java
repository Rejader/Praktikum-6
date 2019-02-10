import gdp.stdlib.StdIn;

public class Dezidoku {

    private static int NO_VALUE = -1;
    private static int BOX_WIDGHT = 2;
    private static int BOX_HIGHT = 5;
    private static int RASTER_SIZE = 10;

    public enum Variante {
        normal("Dezidoku"),
        mitDiagonalen("Dezidoku mit Diagonalen");

        private String description;

        Variante(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private Variante v;
    private int[][] raster = new int[RASTER_SIZE][RASTER_SIZE];


    public Dezidoku(Variante v) {
        this.v = v;
  
    }

    public void read() {
		int line;
		for (line = 0; line < RASTER_SIZE; line++) {
			String nl = StdIn.readLine();
			for (int column = 0; column < RASTER_SIZE; column++) {
				char c = nl.charAt(column);
				String value = String.valueOf(c);
				if (value.equals(".")) {
					raster[line][column] = NO_VALUE;
				} else {
					int f = Integer.valueOf(value);
					raster[line][column] = f;
				}
			}

		}
	}

    public void write() {

        System.out.println(v.getDescription());
        String frame = "+-----+-----+-----+-----+-----+";
        System.out.println(frame);

        for (int line = 0; line < RASTER_SIZE; line++) {

            String s = "|";

            for (int column = 0; column < RASTER_SIZE; column++) {

                if (raster[line][column] == NO_VALUE) {
                    s = s + "  ";
                } else {
                    s = s + " " + raster[line][column];
                }
                if ((column + 1) % 2 == 0) {
                    s = s + " |";
                }
            }
            System.out.println(s);
            if (line == 4) {
                System.out.println(frame);
            }
        }
        System.out.println(frame);
    }

    public boolean check() {

        for (int line = 0; line < raster.length; line++) {
            for (int column = 0; column < raster[line].length; column++) {

                if(isValid(raster, line, column)){
                } else{
                    return false;
                }
            }
        }
        return true;
    }

    private boolean lineIsValid(int raster[][], int line) {
        return !checkDuplicates(getLine(raster, line));
    }

    private boolean columnIsValid(int raster[][], int column) {
        return !checkDuplicates(getColumn(raster, column));
    }

    private boolean boxIsValid(int raster[][], int line, int column) {
        return !checkDuplicates(getBox(raster, line, column));
    }

    private boolean diagonalIsValid (int raster[][], int line, int column) {
        return !checkDuplicates(getDiagonal(raster, line, column));
    }

    private boolean checkDuplicates(int[] i) {

        boolean[] b = new boolean[RASTER_SIZE];

        for (int j = 0; j < i.length; j++) {

            if (b[i[j]]) {
                return true;
            } else {
                b[i[j]] = true;
            }
        }
        return false;
    }

    public void solve() {
        if(solve(raster)){
            write();
        } else {
            System.out.println("nicht loesbar :-(");
        }

    }

    private boolean solve(int raster[][]) {

        for (int line = 0; line < RASTER_SIZE; line++) {
            for (int column = 0; column < RASTER_SIZE; column++) {
                if (raster[line][column] == NO_VALUE) {

                    int[] possibleNumbers = getPossibleNumbers(raster, line, column);
                    for (int number : possibleNumbers) {
                        raster[line][column] = number;


                        if (isValid(raster, line, column) && solve(raster)) {
                            return true;
                        }
                        raster[line][column] = NO_VALUE;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private int[] getPossibleNumbers(int raster[][], int line, int column) {

        boolean[] numb = new boolean[RASTER_SIZE];

        int[] l = getLine(raster, line);
        for (int i = 0; i < l.length; i++) {
            numb[l[i]] = true;
        }

        int[] c = getColumn(raster, column);
        for (int i = 0; i < c.length; i++) {
            numb[c[i]] = true;
        }

        int[] b = getBox(raster, line, column);
        for (int i = 0; i < b.length; i++) {
            numb[b[i]] = true;
        }

        if(v.equals(Variante.mitDiagonalen)){
            int[] d = getDiagonal(raster, line, column);
            for (int i = 0; i < d.length; i++) {
                numb[d[i]] = true;
            }
        }

        int count = 0;
        for (int i = 0; i < numb.length; i++) {
            if (!numb[i]) count++;
        }

        int[] numbers = new int[count];
        count = 0;
        for (int i = 0; i < numb.length; i++) {
            if (!numb[i]) {
                numbers[count] = i;
                count++;
            }
        }
        return numbers;
    }

    private int[] getLine(int raster[][], int line) {

        int count = 0;
        for (int collum = 0; collum < RASTER_SIZE; collum++) {
            if (raster[line][collum] != NO_VALUE) {
                count++;
            }
        }

        int[] i = new int[count];
        count = 0;
        for (int collum = 0; collum < RASTER_SIZE; collum++) {

            if (raster[line][collum] != NO_VALUE) {
                i[count] = raster[line][collum];
                count++;
            }
        }
        return i;
    }

    private int[] getColumn(int raster[][], int column) {

        int count = 0;
        for (int line = 0; line < RASTER_SIZE; line++) {

            if (raster[line][column] != NO_VALUE) {
                count++;
            }
        }

        int[] i = new int[count];
        count = 0;
        for (int line = 0; line < RASTER_SIZE; line++) {

            if (raster[line][column] != NO_VALUE) {
                i[count] = raster[line][column];
                count++;
            }
        }
        return i;
    }

    private int[] getBox(int raster[][], int line, int column) {

        int startLine = line / BOX_HIGHT * BOX_HIGHT;
        int endLine = startLine + BOX_HIGHT;
        int startColumn = column / BOX_WIDGHT * BOX_WIDGHT;
        int endColumn = startColumn + BOX_WIDGHT;
        int count = 0;

        for (int i = startLine; i < endLine; i++) {
            for (int j = startColumn; j < endColumn; j++) {
                if (raster[i][j] != NO_VALUE) {
                    count++;
                }
            }
        }

        int[] box = new int[count];
        count = 0;
        for (int i = startLine; i < endLine; i++) {
            for (int j = startColumn; j < endColumn; j++) {
                if (raster[i][j] != NO_VALUE) {
                    box[count] = raster[i][j];
                    count++;
                }
            }
        }
        return box;
    }

    private int[] getDiagonal(int raster[][], int line, int column) {

        int [] numbers;
        int count = 0;
        if (line == column){
            for (int i = 0; i < RASTER_SIZE; i++){
                if (raster[i][i] != NO_VALUE) {
                    count++;
                }
            }
            numbers = new int[count];
            count = 0;
            for (int i = 0; i < RASTER_SIZE; i++){
                if (raster[i][i] != NO_VALUE) {
                    numbers[count] = raster[i][i];
                    count++;
                }
            }
        } else if (line + column == 9){
            for (int i = 0; i < RASTER_SIZE; i++){
                if (raster[i][9-i] != NO_VALUE) {
                    count++;
                }
            }
            numbers = new int[count];
            count = 0;
            for (int i = 0; i < RASTER_SIZE; i++){
                if (raster[i][9-i] != NO_VALUE) {
                    numbers[count] = raster[i][9-i];
                    count++;
                }
            }
        } else {
            numbers = new int[0];
        }
        return numbers;
    }


    private boolean isValid(int raster[][], int line, int column) {

        if(v.equals(Variante.mitDiagonalen)) {
            if (lineIsValid(raster, line) && columnIsValid(raster, column)
                    && boxIsValid(raster, line, column) && diagonalIsValid(raster, line, column)) {
                return true;
            }
        }
        else {
            if (lineIsValid(raster, line) && columnIsValid(raster, column) && boxIsValid(raster, line, column)) {
                return true;
            }
        }
        return false;
    }
}
