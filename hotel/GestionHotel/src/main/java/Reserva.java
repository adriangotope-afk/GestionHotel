import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

/**
 * Representa una reserva de habitación, sus fechas, estado, cliente y pago asociado.
 */
public class Reserva {
    // Formato de fecha para todo el programa (STRICT rechaza fechas como 31/02)
    public static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    // Atributos
    private int idReserva;
    private Cliente cliente;       // asociación con Cliente
    private Habitacion habitacion; // asociación con Habitacion
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String estado;         // Activa, En curso, Finalizada o Cancelada
    private Pago pago;             // asociación con Pago (puede ser null)

    // Constructor
    /**
     * Construye un objeto de tipo Reserva.
     * @param idReserva parámetro utilizado por esta operación
     * @param cliente parámetro utilizado por esta operación
     * @param habitacion parámetro utilizado por esta operación
     * @param fechaEntrada parámetro utilizado por esta operación
     * @param fechaSalida parámetro utilizado por esta operación
     */
    public Reserva(int idReserva, Cliente cliente, Habitacion habitacion,
                   LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = "Activa";
        this.pago = null;
    }

    // Getters y setters
    /**
     * Obtiene el identificador de la reserva.
     * @return resultado de la operación
     */
    public int getIdReserva() { return idReserva; }
    /**
     * Obtiene el cliente asociado a la reserva.
     * @return resultado de la operación
     */
    public Cliente getCliente() { return cliente; }
    /**
     * Obtiene la habitación asociada a la reserva.
     * @return resultado de la operación
     */
    public Habitacion getHabitacion() { return habitacion; }
    /**
     * Obtiene el estado actual.
     * @return resultado de la operación
     */
    public String getEstado() { return estado; }
    /**
     * Obtiene el pago asociado a la reserva.
     * @return resultado de la operación
     */
    public Pago getPago() { return pago; }
    /**
     * Asocia un pago a la reserva.
     * @param pago parámetro utilizado por esta operación
     */
    public void setPago(Pago pago) { this.pago = pago; }
    /**
     * Actualiza el estado de la reserva.
     * @param estado parámetro utilizado por esta operación
     */
    public void setEstado(String estado) { this.estado = estado; }

    // Las noches se calculan con las fechas
    /**
     * Calcula la cantidad de noches de la reserva.
     * @return resultado de la operación
     */
    public int getNoches() {
        return (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    // Una reserva vigente (Activa o En curso) choca con otro rango de fechas si se cruzan
    /**
     * Comprueba si la reserva ocupa el intervalo de fechas indicado.
     * @param entrada parámetro utilizado por esta operación
     * @param salida parámetro utilizado por esta operación
     * @return resultado de la operación
     */
    public boolean ocupaFechas(LocalDate entrada, LocalDate salida) {
        boolean vigente = estado.equals("Activa") || estado.equals("En curso");
        return vigente && entrada.isBefore(fechaSalida) && salida.isAfter(fechaEntrada);
    }

    // Métodos
    /**
     * Muestra la confirmación de creación de la reserva.
     */
    public void crearReserva() {
        System.out.println("Reserva " + idReserva + " creada para " + cliente.getNombre());
    }

    /**
     * Cambia el estado de la reserva a cancelada.
     */
    public void cancelarReserva() {
        estado = "Cancelada";
        System.out.println("Reserva " + idReserva + " cancelada.");
    }

    /**
     * Actualiza las fechas de la reserva.
     * @param nuevaEntrada parámetro utilizado por esta operación
     * @param nuevaSalida parámetro utilizado por esta operación
     */
    public void modificarReserva(LocalDate nuevaEntrada, LocalDate nuevaSalida) {
        fechaEntrada = nuevaEntrada;
        fechaSalida = nuevaSalida;
        System.out.println("Reserva modificada.");
    }

    /**
     * Calcula el costo total de la reserva.
     * @return resultado de la operación
     */
    public double calcularTotal() {
        return habitacion.getPrecio() * getNoches();
    }

    /**
     * Muestra la información del elemento.
     */
    public void mostrarInformacion() {
        System.out.println("Reserva #" + idReserva + " | Cliente: " + cliente.getNombre()
                + " | Hab: " + habitacion.getNumero()
                + " | " + fechaEntrada.format(FORMATO) + " a " + fechaSalida.format(FORMATO)
                + " | " + getNoches() + " noches | Total: $" + calcularTotal()
                + " | " + estado);
        if (pago != null) {
            System.out.print("   -> ");
            pago.consultarPago();
        } else {
            System.out.println("   -> Pago: sin registrar");
        }
    }
}
