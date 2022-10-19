package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecordAlreadyExistException extends FilmorateServiceException {
    public RecordAlreadyExistException() {
        super();
    }

    public RecordAlreadyExistException(String message) {
        super(message);
    }

    public RecordAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordAlreadyExistException(Throwable cause) {
        super(cause);
    }

    protected RecordAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
