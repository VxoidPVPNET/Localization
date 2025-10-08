package net.vxoidpvp.localization.api.exception;

public class LocaleFileException extends RuntimeException {

    public LocaleFileException(String message) {
        super(message);
    }

    public LocaleFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
