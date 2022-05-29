package pl.lonux.clientapplication.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestControllerAdvice
public class ExceptionHandlerImpl {

    @ExceptionHandler({ NullPointerException.class })
    public final Object handleNullPointerException(final NullPointerException exception) {
        return new Response(
            "Someting went wrong",
            exception.getLocalizedMessage()
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    static class Response {
        private String responseTitle;
        private String exceptionContent;
    }
}