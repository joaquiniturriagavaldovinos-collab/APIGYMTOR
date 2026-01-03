package ApiGymorEjecucion.Api.presentation.exception;


import ApiGymorEjecucion.Api.domain.exception.EstadoFinalException;
import ApiGymorEjecucion.Api.domain.exception.PedidoNoEncontradoException;
import ApiGymorEjecucion.Api.domain.exception.TransicionEstadoInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Manejador global de excepciones para la API REST.
 * Convierte excepciones de dominio en respuestas HTTP apropiadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando no se encuentra un pedido
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNoEncontrado(
            PedidoNoEncontradoException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Pedido No Encontrado",
                ex.getMessage(),
                getPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de transiciones de estado inválidas
     * HTTP 409 - Conflict
     */
    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleTransicionInvalida(
            TransicionEstadoInvalidaException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Transición de Estado Inválida",
                ex.getMessage(),
                getPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones cuando se intenta modificar un pedido en estado final
     * HTTP 409 - Conflict
     */
    @ExceptionHandler(EstadoFinalException.class)
    public ResponseEntity<ErrorResponse> handleEstadoFinal(
            EstadoFinalException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Estado Final",
                ex.getMessage(),
                getPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de argumentos ilegales (validaciones de negocio)
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud Inválida",
                ex.getMessage(),
                getPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de estado ilegal
     * HTTP 422 - Unprocessable Entity
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Estado Inválido",
                ex.getMessage(),
                getPath(request)
        );

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Maneja cualquier otra excepción no capturada
     * HTTP 500 - Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error Interno del Servidor",
                "Ocurrió un error inesperado. Por favor contacte al administrador.",
                getPath(request)
        );

        // En producción, loguear la excepción completa
        ex.printStackTrace();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extrae el path de la petición HTTP
     */
    private String getPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}