package net.vxoidpvp.localization.api.exception;

public class LocaleNotFoundException extends RuntimeException {

    public LocaleNotFoundException(String locale) {
        super("Locale not found: " + locale);
    }

    public LocaleNotFoundException(String locale, Throwable cause) {
        super("Locale not found: " + locale, cause);
    }
}
