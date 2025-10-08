package net.vxoidpvp.localization.api.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(String key) {
        super("Message not found: " + key);
    }

    public MessageNotFoundException(String key, String locale) {
        super("Message not found: " + key + " in locale: " + locale);
    }
}
