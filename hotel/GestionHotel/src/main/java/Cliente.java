import java.util.ArrayList;

/**
 * Representa a un cliente y permite administrar sus datos y consultar sus reservas.
 */
public class Cliente {
    // Atributos
    private int idCliente;
    private String nombre;
    private String documento;
    private String telefono;
    private String correo;

    // Constructor
    /**
     * Construye un objeto de tipo Cliente.
     * @param idCliente parámetro utilizado por esta operación
     * @param nombre parámetro utilizado por esta operación
     * @param documento parámetro utilizado por esta operación
     * @param telefono parámetro utilizado por esta operación
     * @param correo parámetro utilizado por esta operación
     */
    public Cliente(int idCliente, String nombre, String documento, String telefono, String correo) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters
    /**
     * Obtiene el identificador del cliente.
     * @return resultado de la operación
     */
    public int getIdCliente() { return idCliente; }
    /**
     * Obtiene el nombre.
     * @return resultado de la operación
     */
    public String getNombre() { return nombre; }
    /**
     * Obtiene el documento de identificación.
     * @return resultado de la operación
     */
    public String getDocumento() { return documento; }

    // Métodos
    /**
     * Registra un cliente en el sistema.
     */
    public void registrarCliente() {
        System.out.println("Cliente registrado: " + nombre);
    }

    /**
     * Actualiza los datos indicados.
     * @param telefono parámetro utilizado por esta operación
     * @param correo parámetro utilizado por esta operación
     */
    public void actualizarDatos(String telefono, String correo) {
        this.telefono = telefono;
        this.correo = correo;
        System.out.println("Datos actualizados.");
    }

    /**
     * Muestra las reservas asociadas al cliente.
     * @param reservas parámetro utilizado por esta operación
     */
    public void consultarReservas(ArrayList<Reserva> reservas) {
        boolean hay = false;
        for (Reserva r : reservas) {
            if (r.getCliente().getIdCliente() == this.idCliente) {
                r.mostrarInformacion();
                hay = true;
            }
        }
        if (!hay) {
            System.out.println("Este cliente no tiene reservas.");
        }
    }

    /**
     * Muestra la información del elemento.
     */
    public void mostrarInformacion() {
        System.out.println("ID: " + idCliente + " | " + nombre + " | Doc: " + documento
                + " | Tel: " + telefono + " | Correo: " + correo);
    }
}
