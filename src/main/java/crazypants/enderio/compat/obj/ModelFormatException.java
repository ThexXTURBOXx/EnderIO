package crazypants.enderio.compat.obj;

public class ModelFormatException extends RuntimeException {
    public ModelFormatException() {
    }

    public ModelFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelFormatException(String message) {
        super(message);
    }

    public ModelFormatException(Throwable cause) {
        super(cause);
    }
}
