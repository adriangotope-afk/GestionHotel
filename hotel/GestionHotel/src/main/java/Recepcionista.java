import java.util.ArrayList;

/**
 * Representa al recepcionista y las operaciones de atención a clientes y reservas.
 */
public class Recepcionista extends Empleado {
    // Atributo propio
    private String turno;

    // Constructor: super(...) llama al constructor de Empleado
    /**
     * Construye un objeto de tipo Recepcionista.
     * @param idEmpleado parámetro utilizado por esta operación
     * @param nombre parámetro utilizado por esta operación
     * @param documento parámetro utilizado por esta operación
     * @param telefono parámetro utilizado por esta operación
     * @param usuario parámetro utilizado por esta operación
     * @param contrasena parámetro utilizado por esta operación
     * @param turno parámetro utilizado por esta operación
     */
    public Recepcionista(int idEmpleado, String nombre, String documento, String telefono,
                         String usuario, String contrasena, String turno) {
        super(idEmpleado, nombre, documento, telefono, "Recepcionista", usuario, contrasena);
        this.turno = turno;
    }

    // Métodos propios
    /**
     * Registra un cliente en el sistema.
     * @param clientes parámetro utilizado por esta operación
     * @param nuevo parámetro utilizado por esta operación
     */
    public void registrarCliente(ArrayList<Cliente> clientes, Cliente nuevo) {
        clientes.add(nuevo);
        nuevo.registrarCliente();
    }

    /**
     * Añade una reserva a la lista recibida.
     * @param reservas parámetro utilizado por esta operación
     * @param nueva parámetro utilizado por esta operación
     */
    public void realizarReserva(ArrayList<Reserva> reservas, Reserva nueva) {
        reservas.add(nueva);
        nueva.crearReserva();
    }

    /**
     * Cambia el estado de la reserva a cancelada.
     * @param r parámetro utilizado por esta operación
     */
    public void cancelarReserva(Reserva r) {
        r.cancelarReserva();
    }

    // Devuelve true si la entrada se registró (para saber si hay que guardar)
    /**
     * Registra la entrada del huésped según el estado de la reserva.
     * @param r parámetro utilizado por esta operación
     * @return resultado de la operación
     */
    public boolean registrarEntrada(Reserva r) {
        if (!r.getEstado().equals("Activa")) {
            System.out.println("Solo se puede registrar la entrada de una reserva activa. Estado actual: "
                    + r.getEstado());
            return false;
        }

        Habitacion h = r.getHabitacion();
        if (!h.consultarDisponibilidad()) {
            System.out.println("La habitación " + h.getNumero() + " no está disponible (" + h.getEstado() + ").");
            return false;
        }

        if (r.getPago() == null) {
            System.out.println("Aviso: la reserva aún no tiene pago registrado.");
        }

        h.cambiarEstado("Ocupada");
        r.setEstado("En curso");
        System.out.println("Entrada registrada. Cliente: " + r.getCliente().getNombre()
                + " | Habitación " + h.getNumero() + " ocupada.");
        return true;
    }

    // Devuelve true si la salida se registró (para saber si hay que guardar)
    /**
     * Registra la salida del huésped según el estado de la reserva.
     * @param r parámetro utilizado por esta operación
     * @return resultado de la operación
     */
    public boolean registrarSalida(Reserva r) {
        if (!r.getEstado().equals("En curso")) {
            System.out.println("Solo se puede registrar la salida de una reserva en curso. Estado actual: "
                    + r.getEstado());
            return false;
        }

        Habitacion h = r.getHabitacion();
        h.cambiarEstado("Limpieza");
        r.setEstado("Finalizada");
        System.out.println("Salida registrada. Habitación " + h.getNumero() + " pasa a limpieza.");
        System.out.println("Total de la estadía: $" + r.calcularTotal());

        if (r.getPago() == null) {
            System.out.println("Aviso: la reserva no tiene pago registrado.");
        }
        return true;
    }

    // Sobrescribe el método de Empleado para agregar el turno
    @Override
    /**
     * Muestra la información del elemento.
     */
    public void mostrarInformacion() {
        super.mostrarInformacion();
        System.out.println("   Turno: " + turno);
    }
}
