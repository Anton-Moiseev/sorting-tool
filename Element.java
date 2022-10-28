package sorting;

class Element implements Comparable<Element> {
    private final String value;
    private final int numberOfTimes;
    private final DataType dataType;

    Element(String value, int numberOfTimes, DataType dataType) {
        this.value = value;
        this.numberOfTimes = numberOfTimes;
        this.dataType = dataType;
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(Element otherElement) {
        if (numberOfTimes > otherElement.getNumberOfTimes()) {
            return 1;
        } else if (numberOfTimes < otherElement.getNumberOfTimes()) {
            return -1;
        } else {
            if (dataType == DataType.number) {
                long value1 = Long.parseLong(value);
                long value2 = Long.parseLong(otherElement.getValue());
                return Long.compare(value1, value2);
            } else {
                return value.compareTo(otherElement.getValue());
            }
        }
    }
}
