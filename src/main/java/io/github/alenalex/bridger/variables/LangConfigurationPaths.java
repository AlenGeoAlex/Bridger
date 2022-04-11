package io.github.alenalex.bridger.variables;

public enum LangConfigurationPaths {
    PREFIX("prefix")
    ;
    private final String path;

    LangConfigurationPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
