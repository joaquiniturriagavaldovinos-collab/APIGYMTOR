package ApiGymorEjecucion.Api.presentation.exception;

import ApiGymorEjecucion.Api.domain.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 *
 * FILOSOFÍA:
 * - Devolver información útil al cliente (frontend/API consumers)
 * - Loggear información técnica completa en servidor
 * - NO ocultar errores de negocio (son esperados y útiles)
 * - SÍ ocultar detalles técnicos peligrosos (stacktraces, SQL, paths)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ===== VALIDACIÓN =====

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Campos inválidos: " + errores,  // ← Info útil para el cliente
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "JSON inválido",
                "El cuerpo de la solicitud no es un JSON válido o tiene el formato incorrecto",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ===== REGLAS DE NEGOCIO (Devolver mensaje completo) =====

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Solicitud inválida",
                ex.getMessage(),  // ← "El RUT ya está registrado", útil para el usuario
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Operación no permitida",
                ex.getMessage(),  // ← "El cliente ya está desactivado", útil
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // ===== EXCEPCIONES DE DOMINIO (Información de negocio) =====

    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePedidoNoEncontrado(
            PedidoNoEncontradoException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Pedido no encontrado",
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleStockInsuficiente(
            StockInsuficienteException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Stock insuficiente",
                ex.getMessage(),  // ← "Solo quedan 5 unidades del producto X"
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleTransicionInvalida(
            TransicionEstadoInvalidaException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Transición de estado inválida",
                ex.getMessage(),  // ← "No se puede pasar de ENTREGADO a PENDIENTE"
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(EstadoFinalException.class)
    public ResponseEntity<ErrorResponse> handleEstadoFinal(
            EstadoFinalException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Estado final alcanzado",
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(PagoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handlePagoInvalido(
            PagoInvalidoException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Pago inválido",
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ===== SERIALIZACIÓN JSON =====

    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<ErrorResponse> handleJsonSerializationError(
            InvalidDefinitionException ex,
            HttpServletRequest request) {

        // Log completo en servidor (para debugging)
        System.err.println("⚠ ERROR DE SERIALIZACIÓN JSON:");
        System.err.println("   Tipo: " + ex.getType());
        System.err.println("   Detalle: " + ex.getMessage());

        // Respuesta al cliente (sin stacktrace, pero útil)
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error de serialización",
                "Error al procesar la respuesta. Posible ciclo en relaciones de datos.",
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // ===== ERRORES INESPERADOS =====

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericError(
            Exception ex,
            HttpServletRequest request) {

        // ===== LOG COMPLETO EN SERVIDOR (debugging) =====
        System.err.println(" ERROR INESPERADO:");
        System.err.println("   Tipo: " + ex.getClass().getName());
        System.err.println("   Mensaje: " + ex.getMessage());
        System.err.println("   Path: " + request.getRequestURI());
        ex.printStackTrace();  // ← Stacktrace SOLO en logs

        // ===== RESPUESTA AL CLIENTE (sin stacktrace) =====
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getClass().getSimpleName(),  // ← Tipo de error (útil)
                ex.getMessage() != null ? ex.getMessage() : "Error inesperado en el servidor",  // ← Mensaje descriptivo
                LocalDateTime.now(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}