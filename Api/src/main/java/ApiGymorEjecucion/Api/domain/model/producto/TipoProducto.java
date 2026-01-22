package ApiGymorEjecucion.Api.domain.model.producto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tipos de productos que comercializa GYMOR
 * Incluye reglas de negocio específicas por tipo
 */
public enum TipoProducto {
    DISCO("Disco de Pesas", true, false, "KG"),
    MAQUINA("Máquina de Ejercicio", false, true, "UNIDAD"),
    ACCESORIO("Accesorio", false, false, "UNIDAD"),
    BARRA("Barra", true, false, "UNIDAD"),
    RACK("Rack", false, true, "UNIDAD"),
    CARDIO("Equipo Cardiovascular", false, true, "UNIDAD"),
    SUPLEMENTO("Suplemento", false, false, "UNIDAD");

    private final String descripcion;
    private final boolean fabricadoPorGymor;
    private final boolean requiereDespachoEspecial;
    private final String unidadMedida;

    TipoProducto(String descripcion, boolean fabricadoPorGymor,
                 boolean requiereDespachoEspecial, String unidadMedida) {
        this.descripcion = descripcion;
        this.fabricadoPorGymor = fabricadoPorGymor;
        this.requiereDespachoEspecial = requiereDespachoEspecial;
        this.unidadMedida = unidadMedida;
    }

    // ===== QUERIES - REGLAS DE NEGOCIO =====

    /**
     * Indica si el producto es fabricado por GYMOR
     */
    public boolean esFabricado() {
        return fabricadoPorGymor;
    }

    /**
     * Indica si el producto requiere despacho especial
     * (productos de gran tamaño o peso)
     */
    public boolean requiereDespachoEspecial() {
        return requiereDespachoEspecial;
    }

    /**
     * Indica si el producto requiere instalación
     */
    public boolean requiereInstalacion() {
        return this == MAQUINA || this == RACK || this == CARDIO;
    }

    /**
     * Indica si el producto puede venderse fraccionado
     */
    public boolean permiteFraccionamiento() {
        return this == DISCO || this == ACCESORIO;
    }

    /**
     * Indica si el producto tiene garantía extendida
     */
    public boolean tieneGarantiaExtendida() {
        return this == MAQUINA || this == CARDIO;
    }

    /**
     * Obtiene los días de garantía estándar
     */
    public int getDiasGarantia() {
        switch (this) {
            case MAQUINA:
            case CARDIO:
                return 365; // 1 año
            case RACK:
            case BARRA:
                return 180; // 6 meses
            case DISCO:
            case ACCESORIO:
                return 90;  // 3 meses
            case SUPLEMENTO:
                return 0;   // Sin garantía
            default:
                return 0;
        }
    }

    /**
     * Obtiene el peso estimado para cálculos de despacho (en kg)
     */
    public int getPesoEstimadoKg() {
        switch (this) {
            case MAQUINA:
                return 150;
            case RACK:
                return 100;
            case CARDIO:
                return 80;
            case BARRA:
                return 20;
            case DISCO:
                return 10;
            case ACCESORIO:
                return 2;
            case SUPLEMENTO:
                return 1;
            default:
                return 0;
        }
    }

    /**
     * Verifica si el producto requiere capacitación para uso
     */
    public boolean requiereCapacitacion() {
        return this == MAQUINA || this == CARDIO;
    }

    /**
     * Verifica si el producto es de alta rotación
     */
    public boolean esAltaRotacion() {
        return this == SUPLEMENTO || this == ACCESORIO;
    }

    /**
     * Obtiene la categoría de almacenamiento
     */
    public CategoriaAlmacenamiento getCategoriaAlmacenamiento() {
        if (requiereDespachoEspecial) {
            return CategoriaAlmacenamiento.BODEGA_ESPECIAL;
        } else if (this == SUPLEMENTO) {
            return CategoriaAlmacenamiento.ALMACEN_CLIMATIZADO;
        } else {
            return CategoriaAlmacenamiento.BODEGA_GENERAL;
        }
    }

    // ===== MÉTODOS ESTÁTICOS =====

    /**
     * Obtiene todos los tipos fabricados por GYMOR
     */
    public static List<TipoProducto> getFabricados() {
        return Arrays.stream(values())
                .filter(TipoProducto::esFabricado)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los tipos que requieren despacho especial
     */
    public static List<TipoProducto> getConDespachoEspecial() {
        return Arrays.stream(values())
                .filter(TipoProducto::requiereDespachoEspecial)
                .collect(Collectors.toList());
    }

    /**
     * Busca un tipo por descripción (case-insensitive)
     */
    public static TipoProducto porDescripcion(String descripcion) {
        return Arrays.stream(values())
                .filter(t -> t.getDescripcion().equalsIgnoreCase(descripcion))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tipo de producto no encontrado: " + descripcion));
    }

    // ===== GETTERS =====

    public String getDescripcion() {
        return descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    // ===== ENUM AUXILIAR =====

    public enum CategoriaAlmacenamiento {
        BODEGA_GENERAL("Bodega General"),
        BODEGA_ESPECIAL("Bodega Especial - Productos Grandes"),
        ALMACEN_CLIMATIZADO("Almacén Climatizado");

        private final String descripcion;

        CategoriaAlmacenamiento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name(), descripcion);
    }
}