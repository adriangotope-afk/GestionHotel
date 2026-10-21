/**
 * Representa una habitación del hotel, incluyendo su tipo, precio y estado de disponibilidad.
 */
public class Habitacion {
    // Atributos
    private int numero;
    private String tipo;
    private double precio;
    private String estado; // Disponible, Ocupada o Mantenimiento

    // Constructor
    /**
     * Construye un objeto de tipo Habitacion.
     * @param numero parámetro utilizado por esta operación
     * @param tipo parámetro utilizado por esta operación
     * @param precio parámetro utilizado por esta operación
     */
    public Habitacion(int numero, String tipo, double precio) {
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.estado = "Disponible"; // toda habitación nueva empieza disponible
    }

    // Getters
    /**
     * Obtiene el número de habitación.
     * @return resultado de la operación
     */
    public int getNumero() { return numero; }
    /**
     * Obtiene el tipo de habitación.
     * @return resultado de la operación
     */
    public String getTipo() { return tipo; }
    /**
     * Obtiene el precio por noche.
     * @return resultado de la operación
     */
    public double getPrecio() { return precio; }
    /**
     * Obtiene el estado actual.
     * @return resultado de la operación
     */
    public String getEstado() { return estado; }

    // Métodos
    /**
     * Indica si la habitación está disponible.
     * @return resultado de la operación
     */
    public boolean consultarDisponibilidad() {
        return estado.equals("Disponible");
    }

    /**
     * Cambia el estado de la habitación.
     * @param nuevoEstado parámetro utilizado por esta operación
     */
    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /**
     * Muestra la información del elemento.
     */
    public void mostrarInformacion() {
        System.out.println("Habitación " + numero + " | " + tipo + " | $" + precio + " | " + estado);
    }
}
