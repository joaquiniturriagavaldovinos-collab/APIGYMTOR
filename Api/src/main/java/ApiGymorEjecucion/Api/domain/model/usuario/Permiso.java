package ApiGymorEjecucion.Api.domain.model.usuario;

/**
 * Permisos del sistema
 * Definen acciones atómicas que pueden ser asignadas a roles
 */
public enum Permiso {

    // =========================
    // PEDIDOS
    // =========================
    CREAR_PEDIDO,
    VER_PEDIDO,
    VER_PEDIDOS,
    CANCELAR_PEDIDO,
    PREPARAR_PEDIDO,
    DESPACHAR_PEDIDO,

    // =========================
    // PRODUCTOS
    // =========================
    CREAR_PRODUCTO,
    VER_PRODUCTO,
    VER_PRODUCTOS,
    EDITAR_PRODUCTO,
    ELIMINAR_PRODUCTO,
    GESTIONAR_STOCK,

    // =========================
    // CLIENTES
    // =========================
    VER_CLIENTE,
    VER_CLIENTES,
    CREAR_CLIENTE,
    EDITAR_CLIENTE,
    ELIMINAR_CLIENTE,

    // =========================
    // SUSCRIPCIONES
    // =========================
    CREAR_SUSCRIPCION,
    VER_SUSCRIPCION,
    VER_SUSCRIPCIONES,
    RENOVAR_SUSCRIPCION,
    CANCELAR_SUSCRIPCION,
    SUSPENDER_SUSCRIPCION,

    // =========================
    // PAGOS
    // =========================
    REGISTRAR_PAGO,
    VER_PAGO,
    VER_PAGOS,
    REEMBOLSAR_PAGO,

    // =========================
    // USUARIOS Y ROLES
    // =========================
    CREAR_USUARIO,
    VER_USUARIO,
    VER_USUARIOS,
    EDITAR_USUARIO,
    DESACTIVAR_USUARIO,

    CREAR_ROL,
    VER_ROL,
    VER_ROLES,
    EDITAR_ROL,
    ELIMINAR_ROL,

    // =========================
    // REPORTES
    // =========================
    VER_REPORTES,
    EXPORTAR_REPORTES,

    // =========================
    // SISTEMA / ADMIN
    // =========================
    CONFIGURAR_SISTEMA,
    AUDITAR_EVENTOS
}
