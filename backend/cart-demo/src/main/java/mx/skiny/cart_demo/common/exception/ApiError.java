package mx.skiny.cart_demo.common.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiError (
    OffsetDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    Map<String, Object> details)
{
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(OffsetDateTime.now(), status, error, message, path, null);
    }

    public ApiError withDetails(Map<String, Object> details) {
        return new ApiError(timestamp, status, error, message, path, details);
    }
}
