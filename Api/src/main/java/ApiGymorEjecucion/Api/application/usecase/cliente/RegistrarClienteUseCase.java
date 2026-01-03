package ApiGymorEjecucion.Api.application.usecase.cliente;

import ApiGymorEjecucion.Api.domain.model.Cliente.Cliente;
import ApiGymorEjecucion.Api.domain.model.Cliente.DireccionEntrega;
import ApiGymorEjecucion.Api.domain.model.Cliente.TipoCliente;
import ApiGymorEjecucion.Api.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

/**
 * Caso de Uso: Registrar Cliente
 *
 * Registra un nuevo cliente en el sistema (minorista o mayorista)
 */
@Service
public class RegistrarClienteUseCase {

    private final ClienteRepository clienteRepository;

    public RegistrarClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Registra un nuevo cliente
     *
     * @param request Datos del cliente a registrar
     * @return Cliente registrado
     */
    public ClienteResponse ejecutar(RegistrarClienteRequest request) {
        // Validar request
        validarRequest(request);

        // Verificar que no exista cliente con ese RUT
        if (clienteRepository.existePorRut(request.getRut())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con el RUT: " + request.getRut()
            );
        }

        // Verificar que no exista cliente con ese email
        if (clienteRepository.existePorEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente registrado con el email: " + request.getEmail()
            );
        }

        // Crear cliente en dominio
        TipoCliente tipo = TipoCliente.valueOf(request.getTipo());
        Cliente cliente = Cliente.crear(
                generarIdCliente(),
                request.getNombre(),
                request.getApellido(),
                request.getEmail(),
                request.getTelefono(),
                request.getRut(),
                tipo
        );

        // Agregar dirección si viene en el request
        if (request.getDireccion() != null) {
            DireccionEntrega direccion = crearDireccionDesdeRequest(request.getDireccion());
            cliente.agregarDireccion(direccion);
        }

        // Persistir
        Cliente clienteGuardado = clienteRepository.guardar(cliente);

        // Retornar response
        return mapearAResponse(clienteGuardado);
    }

    private void validarRequest(RegistrarClienteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (request.getApellido() == null || request.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es requerido");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (request.getRut() == null || request.getRut().isBlank()) {
            throw new IllegalArgumentException("El RUT es requerido");
        }
        if (request.getTipo() == null || request.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de cliente es requerido");
        }
        // Validar formato de email básico
        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
    }

    private String generarIdCliente() {
        // En producción esto podría ser UUID o secuencia de BD
        return "CLI-" + System.currentTimeMillis();
    }

    private DireccionEntrega crearDireccionDesdeRequest(DireccionRequest direccionRequest) {
        return DireccionEntrega.crear(
                direccionRequest.getCalle(),
                direccionRequest.getNumero(),
                direccionRequest.getComuna(),
                direccionRequest.getCiudad(),
                direccionRequest.getRegion(),
                direccionRequest.getCodigoPostal(),
                direccionRequest.getReferencia()
        );
    }

    private ClienteResponse mapearAResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNombre(cliente.getNombre());
        response.setApellido(cliente.getApellido());
        response.setEmail(cliente.getEmail());
        response.setTelefono(cliente.getTelefono());
        response.setRut(cliente.getRut());
        response.setTipo(cliente.getTipo().name());
        response.setTipoDescripcion(cliente.getTipo().getDescripcion());
        response.setActivo(cliente.isActivo());
        response.setFechaRegistro(cliente.getFechaRegistro());

        if (cliente.getDireccionPrincipal() != null) {
            response.setDireccionPrincipal(
                    cliente.getDireccionPrincipal().getDireccionCompleta()
            );
        }

        return response;
    }

    // ===== DTOs INTERNOS =====

    public static class RegistrarClienteRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
        private String rut;
        private String tipo; // "MINORISTA" o "MAYORISTA"
        private DireccionRequest direccion;

        // Getters y Setters
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getRut() {
            return rut;
        }

        public void setRut(String rut) {
            this.rut = rut;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public DireccionRequest getDireccion() {
            return direccion;
        }

        public void setDireccion(DireccionRequest direccion) {
            this.direccion = direccion;
        }
    }

    public static class DireccionRequest {
        private String calle;
        private String numero;
        private String comuna;
        private String ciudad;
        private String region;
        private String codigoPostal;
        private String referencia;

        // Getters y Setters
        public String getCalle() {
            return calle;
        }

        public void setCalle(String calle) {
            this.calle = calle;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public String getComuna() {
            return comuna;
        }

        public void setComuna(String comuna) {
            this.comuna = comuna;
        }

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCodigoPostal() {
            return codigoPostal;
        }

        public void setCodigoPostal(String codigoPostal) {
            this.codigoPostal = codigoPostal;
        }

        public String getReferencia() {
            return referencia;
        }

        public void setReferencia(String referencia) {
            this.referencia = referencia;
        }
    }

    public static class ClienteResponse {
        private String id;
        private String nombre;
        private String apellido;
        private String email;
        private String telefono;
        private String rut;
        private String tipo;
        private String tipoDescripcion;
        private boolean activo;
        private java.time.LocalDateTime fechaRegistro;
        private String direccionPrincipal;

        // Getters y Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getRut() {
            return rut;
        }

        public void setRut(String rut) {
            this.rut = rut;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getTipoDescripcion() {
            return tipoDescripcion;
        }

        public void setTipoDescripcion(String tipoDescripcion) {
            this.tipoDescripcion = tipoDescripcion;
        }

        public boolean isActivo() {
            return activo;
        }

        public void setActivo(boolean activo) {
            this.activo = activo;
        }

        public java.time.LocalDateTime getFechaRegistro() {
            return fechaRegistro;
        }

        public void setFechaRegistro(java.time.LocalDateTime fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
        }

        public String getDireccionPrincipal() {
            return direccionPrincipal;
        }

        public void setDireccionPrincipal(String direccionPrincipal) {
            this.direccionPrincipal = direccionPrincipal;
        }
    }
}