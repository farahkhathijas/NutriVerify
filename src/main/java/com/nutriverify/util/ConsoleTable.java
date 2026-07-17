package com.nutriverify.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight console table renderer using Unicode box-drawing characters.
 */
public final class ConsoleTable {
    private ConsoleTable() {}

    public static String render(List<String[]> rows) {
        if (rows.isEmpty()) {
            return "";
        }

        List<Integer> widths = new ArrayList<>();
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                while (widths.size() <= i) {
                    widths.add(0);
                }
                widths.set(i, Math.max(widths.get(i), row[i].length()));
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("┌");
        for (int i = 0; i < widths.size(); i++) {
            builder.append("─".repeat(widths.get(i) + 2));
            builder.append(i == widths.size() - 1 ? "┐\n" : "┬");
        }

        for (int r = 0; r < rows.size(); r++) {
            String[] row = rows.get(r);
            builder.append("│ ");
            for (int c = 0; c < row.length; c++) {
                builder.append(row[c]);
                builder.append(" ".repeat(widths.get(c) - row[c].length()));
                builder.append(c == row.length - 1 ? " │\n" : " │ ");
            }
            if (r == 0) {
                builder.append("├");
                for (int i = 0; i < widths.size(); i++) {
                    builder.append("─".repeat(widths.get(i) + 2));
                    builder.append(i == widths.size() - 1 ? "┤\n" : "┼");
                }
            }
        }
        builder.append("└");
        for (int i = 0; i < widths.size(); i++) {
            builder.append("─".repeat(widths.get(i) + 2));
            builder.append(i == widths.size() - 1 ? "┘" : "┴");
        }
        return builder.toString();
    }
}
