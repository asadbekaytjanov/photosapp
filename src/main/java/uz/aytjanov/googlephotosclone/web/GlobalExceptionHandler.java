package uz.aytjanov.googlephotosclone.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        HttpStatusCode statusCode = ex.getStatusCode();
        int status = statusCode.value();

        String error = (statusCode instanceof HttpStatus hs) ? hs.name() : "ERROR";
        String message = ex.getReason() != null ? ex.getReason() : "Request failed";

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status,
                "error", error,
                "message", message,
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(statusCode).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(HttpServletRequest request) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", 500,
                "error", "INTERNAL_SERVER_ERROR",
                "message", "Unexpected server error",
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(500).body(body);
    }
}