package sorting;

enum DataType {
    number("\\S+"),
    word("\\S+"),
    line(".+");

    private final String pattern;

    DataType(String pattern) {
        this.pattern = pattern;
    }

    String getPattern() {
        return pattern;
    }
}
