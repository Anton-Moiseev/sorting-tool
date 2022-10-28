package sorting;

import sorting.utils.PrintUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


class SortingToolProgram {
    private final String[] commandLineArguments;
    private SortingType sortingType;
    private DataType dataType;
    private String outputFileName;
    private Scanner scanner;
    private final List<String> elementsList = new ArrayList<>();

    SortingToolProgram(String[] commandLineArguments) {
        this.commandLineArguments = commandLineArguments;
    }

    private void processCommandLineArguments() {
        List<String> argumentsList = new ArrayList<>(Arrays.asList(commandLineArguments));
        String stringArguments = String.join(" ", argumentsList).trim();
        if (argumentsList.contains("-dataType")) {
            if (!stringArguments.matches(".*-dataType (word|long|line).*")) {
                dataType = DataType.word;
                PrintUtils.noDataType();
            } else {
                int index = argumentsList.indexOf("-dataType");
                String nextElement = argumentsList.get(index + 1);
                switch (nextElement) {
                    case "long":
                        dataType = DataType.number;
                        break;
                    case "line":
                        dataType = DataType.line;
                        break;
                    default:
                        dataType = DataType.word;
                }
            }
        } else {
            dataType = DataType.word;
        }
        if (argumentsList.contains("-sortingType")) {
            if (!stringArguments.matches(".*-sortingType (natural|byCount).*")) {
                sortingType = SortingType.natural;
                PrintUtils.noSortingType();
            } else {
                int index = argumentsList.indexOf("-sortingType");
                String nextElement = argumentsList.get(index + 1);
                if (nextElement.equals("byCount")) {
                    sortingType = SortingType.byCount;
                } else {
                    sortingType = SortingType.natural;
                }
            }
        } else {
            sortingType = SortingType.natural;
        }
        if (argumentsList.contains("-inputFile")) {
            int index = argumentsList.indexOf("-inputFile");
            String inputFileName = argumentsList.get(index + 1);
            argumentsList.remove(index + 1);
            File file = new File(inputFileName);
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            scanner = new Scanner(System.in);
        }
        printWrongArguments(argumentsList);
        if (argumentsList.contains("-outputFile")) {
            int index = argumentsList.indexOf("-outputFile");
            outputFileName = argumentsList.get(index + 1);
            argumentsList.remove(index + 1);
        }
        printWrongArguments(argumentsList);
    }

    private void printWrongArguments(List<String> argumentsList) {
        for (String element : argumentsList) {
            if (!element.matches("(-sortingType|-dataType|natural|" +
                    "byCount|long|word|line|" +
                    "-inputFile|-outputFile)")) {
                PrintUtils.notValidParameter(element);
            }
        }
    }

    void run() {
        processCommandLineArguments();
        addAllElementsToList();
        int totalElements = elementsList.size();
        PrintUtils.totalElements(dataType.name(), totalElements);
        if (sortingType == SortingType.natural) {
            runSortNatural();
        } else {
            runSortByCount();
        }
    }

    private void runSortNatural() {
        String sortedListString;
        if (dataType == DataType.number) {
            sortedListString = elementsList.stream()
                    .mapToLong(Long::parseLong)
                    .sorted()
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(" "))
                    .trim();
        } else {
            sortedListString = elementsList.stream()
                    .sorted()
                    .collect(Collectors.joining(" "))
                    .trim();
        }
        if (outputFileName == null) {
            PrintUtils.sortedData(sortedListString);
        } else {
            File file = new File(outputFileName);
            try (FileWriter writer = new FileWriter(file)) {
                String textToWrite = "Sorted data: " + sortedListString + "\n";
                writer.write(textToWrite);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void runSortByCount() {
        Set<String> setElements = new HashSet<>(elementsList);
        List<Element> elementsObjectsList = new ArrayList<>();
        for (String element : setElements) {
            int numberOfTimes = findNumberOfTimes(element);
            elementsObjectsList.add(new Element(element, numberOfTimes, dataType));
        }
        Collections.sort(elementsObjectsList);
        int totalNumbers = elementsList.size();
        if (outputFileName == null) {
            for (Element elementObject : elementsObjectsList) {
                String value = elementObject.getValue();
                int numberOfTimes = elementObject.getNumberOfTimes();
                int percentage = findPercentage(totalNumbers, numberOfTimes);
                PrintUtils.elementAndTimes(value, numberOfTimes, percentage);
            }
        } else {
            try (FileWriter writer = new FileWriter(outputFileName)) {
                for (Element elementObject : elementsObjectsList) {
                    String value = elementObject.getValue();
                    int numberOfTimes = elementObject.getNumberOfTimes();
                    int percentage = findPercentage(totalNumbers, numberOfTimes);
                    String textToWrite = value + ": " + numberOfTimes + " time(s), " +
                            percentage + "%\n";
                    writer.write(textToWrite);
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
    }

    private void addAllElementsToList() {
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            addElementsFromLineToList(line);
        }
        scanner.close();
    }

    private void addElementsFromLineToList(String line) {
        Pattern pattern = Pattern.compile(dataType.getPattern());
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String element = matcher.group().trim();
            if (dataType == DataType.number) {
                if (!element.matches("-?\\d+")) {
                    PrintUtils.notLong(element);
                    continue;
                }
            }
            elementsList.add(element);
        }
    }

    private int findNumberOfTimes(String elementToMatch) {
        return (int) elementsList.stream()
                .filter(element -> element.equals(elementToMatch))
                .count();
    }

    private int findPercentage(int total, int numberOfTimes) {
        return (int) ((double)numberOfTimes / total * 100);
    }
}
