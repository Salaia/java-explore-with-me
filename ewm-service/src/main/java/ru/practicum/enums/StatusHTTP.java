package ru.practicum.enums;

// TODO Это конечно хороший enum, но в Spring он уже есть, HttpStatus называется.
public enum StatusHTTP {
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404),
    CONFLICT(409);

    private final int statusCode;

    StatusHTTP(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

}
