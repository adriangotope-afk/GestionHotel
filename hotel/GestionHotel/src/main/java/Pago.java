/**
 * Representa un pago relacionado con los servicios del hotel.
 */
public class Pago {
    // Atributos
    private int idPago;
    private String fecha;
    private double valor;
    private String metodoPago; // Efectivo, Tarjeta o Transferencia
    private String estado;     // Pendiente o Confirmado

    // Constructor
    /**
     * Construye un objeto de tipo Pago.
     * @param idPago parámetro utilizado por esta operación
     * @param fecha parámetro utilizado por esta operación
     * @param valor parámetro utilizado por esta operación
     * @param metodoPago parámetro utilizado por esta operación
     */
    public Pago(int idPago, String fecha, double valor, String metodoPago) {
        this.idPago = idPago;
        this.fecha = fecha;
        this.valor = valor;
        this.metodoPago = metodoPago;
        this.estado = "Pendiente";
    }

    // Métodos
    /**
     * Registra o muestra la realización del pago.
     */
    public void realizarPago() {
        System.out.println("Pago registrado por $" + valor + " (" + metodoPago + "). Estado: Pendiente.");
    }

    /**
     * Confirma el pago.
     */
    public void confirmarPago() {
        estado = "Confirmado";
        System.out.println("Pago confirmado.");
    }

    /**
     * Muestra la información del pago.
     */
    public void consultarPago() {
        System.out.println("Pago #" + idPago + " | " + fecha + " | $" + valor
                + " | " + metodoPago + " | " + estado);
    }
}
