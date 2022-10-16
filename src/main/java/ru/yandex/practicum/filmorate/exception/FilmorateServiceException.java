package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FilmorateServiceException extends RuntimeException {
    public FilmorateServiceException() {
        super();
    }

    public FilmorateServiceException(String message) {
        super(message);
    }

    public FilmorateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilmorateServiceException(Throwable cause) {
        super(cause);
    }

    protected FilmorateServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
