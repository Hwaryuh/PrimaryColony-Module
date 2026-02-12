package studio.semicolon.prc.core.module.exception;

public class StructureLoadException extends RuntimeException {
    private final String structureKey;

    public StructureLoadException(String structureKey) {
        super("Failed to load structure: " + structureKey);
        this.structureKey = structureKey;
    }

    public String getStructureKey() {
        return structureKey;
    }
}