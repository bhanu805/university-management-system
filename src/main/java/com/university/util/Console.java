package com.university.util;

import java.util.List;
import java.util.Scanner;

/** Console input and table output helpers. */
public class Console {
    private static final Scanner IN = new Scanner(System.in);

    public static String str(String label) {
        System.out.print(label + ": ");
        return IN.nextLine().trim();
    }

    public static int integer(String label) {
        while (true) {
            try { return Integer.parseInt(str(label)); }
            catch (NumberFormatException e) { System.out.println("  Please enter a valid number."); }
        }
    }

    public static double decimal(String label) {
        while (true) {
            try { return Double.parseDouble(str(label)); }
            catch (NumberFormatException e) { System.out.println("  Please enter a valid number."); }
        }
    }

    public static void table(String[] headers, List<Object[]> rows) {
        if (rows.isEmpty()) { System.out.println("  (no records)"); return; }
        int[] w = new int[headers.length];
        for (int i = 0; i < w.length; i++) w[i] = headers[i].length();
        for (Object[] r : rows)
            for (int i = 0; i < w.length; i++) w[i] = Math.max(w[i], String.valueOf(r[i]).length());
        StringBuilder line = new StringBuilder("+");
        for (int x : w) line.append("-".repeat(x + 2)).append("+");
        System.out.println(line);
        printRow(headers, w);
        System.out.println(line);
        for (Object[] r : rows) printRow(r, w);
        System.out.println(line);
        System.out.println("  " + rows.size() + " row(s)");
    }

    private static void printRow(Object[] r, int[] w) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < w.length; i++)
            sb.append(" ").append(String.format("%-" + w[i] + "s", String.valueOf(r[i]))).append(" |");
        System.out.println(sb);
    }
}
