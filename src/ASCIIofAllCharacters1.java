public class ASCIIofAllCharacters1 {
    public static void main(String[] args) {
//        int i = 0;
//
//        while(i <= 255)
//        {
//            System.out.println(" The ASCII value of " + (char)i + "  =  " + i);
//            i++;
//        }

        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 10 ; c++) {
                System.out.println(String.format("R=%d,C=%-2d,Address=%s", r, c, getExcelColumnName((c+1)*3 + 3)));
            }
        }
    }

    private static String getExcelColumnName(int columnNumber) {
        int dividend = columnNumber;
        String columnName = "";
        int modulo;

        while (dividend > 0) {
            modulo = (dividend - 1) % 26;
            columnName = String.format("%s%s", (char) (65 + modulo), columnName);
            dividend = (dividend - modulo) / 26;
        }

        return columnName;
    }
}