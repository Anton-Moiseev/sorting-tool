package sorting.utils;

public class PrintUtils {
    public static void totalElements(String typeName,
                                     int total) {
        System.out.printf("Total %ss: %d.\n", typeName, total);
    }

    public static void elementAndTimes(String element,
                                       int numberOfTimes,
                                       int percentage) {
        System.out.printf("%s: %d time(s), %d%%.\n",
                element, numberOfTimes, percentage);
    }

    public static void sortedData(String sortedListString) {
        System.out.printf("Sorted data: %s\n", sortedListString);
    }

    public static void noSortingType() {
        System.out.println("No sorting type defined!");
    }

    public static void noDataType() {
        System.out.println("No data type defined!");
    }

    public static void notLong(String element) {
        System.out.printf("\"%s\" is not a long. It will be skipped.\n", element);
    }

    public static void notValidParameter(String parameter) {
        System.out.printf("\"%s\" is not a valid parameter. It will be skipped.\n", parameter);
    }
}
