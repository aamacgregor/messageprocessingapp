package com.aamacgregor.messageprocessor.report.detail;

import com.aamacgregor.messageprocessor.exception.ColumnWidthMismatchException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Responsible for creating an Ascii table from the supplied table name, column labels and data
 */
class ReportTableGenerator {

    private static final String ROW_SEPARATOR = "-";
    private static final String COLUMN_SEPARATOR = "|";

    private final String tableName;
    private final List<String> columnLabels;
    private final List<List<String>> rows = new LinkedList<>();

    /**
     * Constructs a ReportTableGenerator instance
     *
     * @param tableName    the name of the table being generated
     * @param columnLabels the labels for the columns of the table
     */
    public ReportTableGenerator(String tableName, String... columnLabels) {
        this.tableName = tableName;
        this.columnLabels = Arrays.asList(columnLabels);
    }

    /**
     * Adds a row of data, if the number of items does not match the number of column labels passed
     * to the constructor then a ColumnWidthMismatchException exception will be thrown.
     *
     * @param items the data for one row in the table
     */
    public void addRow(Object... items) {
        validateNumberOfItems(items.length);
        rows.add(Stream.of(items)
                .map(Object::toString)
                .collect(Collectors.toList()));
    }

    /**
     * Generates the ascii table from the given data
     *
     * @return the generated ascii table.
     */
    public String generate() {
        List<Integer> columnWidths = calculateColumnWidths();

        String tableHeader = generateFormattedTableRow(columnLabels, columnWidths);
        String fullWidthRowSeparator = new String(new char[tableHeader.length() - 1])
                .replace("\0", ROW_SEPARATOR) + "\n";


        StringBuilder builder = new StringBuilder();
        builder.append(tableName)
                .append("\n")
                .append(fullWidthRowSeparator)
                .append(tableHeader)
                .append(fullWidthRowSeparator);

        rows.stream()
                .map(row -> generateFormattedTableRow(row, columnWidths))
                .forEach(builder::append);
        builder.append(fullWidthRowSeparator);

        return builder.toString();
    }

    private String generateFormattedTableRow(List<String> data, List<Integer> columnWidths) {
        int numberOfColumns = columnLabels.size();
        return COLUMN_SEPARATOR + IntStream.range(0, numberOfColumns)
                .mapToObj(i -> rightPad(data.get(i), columnWidths.get(i)))
                .collect(Collectors.joining(COLUMN_SEPARATOR)) + COLUMN_SEPARATOR + "\n";
    }

    private String rightPad(String str, int columnWidth) {
        return String.format("%1$-" + columnWidth + "s", str);
    }

    private List<Integer> calculateColumnWidths() {
        return IntStream.range(0, columnLabels.size())
                .map(this::calculateColumnWidth)
                .boxed()
                .collect(Collectors.toList());
    }

    private int calculateColumnWidth(int columnIndex) {
        int columnLabelWidth = columnLabels.get(columnIndex).length();

        List<String> columnData = rows.stream()
                .map(row -> row.get(columnIndex))
                .collect(Collectors.toList());

        int columnDataWidth = columnData.stream()
                .map(String::length)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        return Math.max(columnLabelWidth, columnDataWidth);
    }

    private void validateNumberOfItems(int numberOfItems) {
        if (numberOfItems != columnLabels.size()) {
            throw new ColumnWidthMismatchException();
        }
    }
}
