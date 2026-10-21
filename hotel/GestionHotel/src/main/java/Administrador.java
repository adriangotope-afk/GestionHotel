import java.util.ArrayList;

/**
 * Administrador del sistema, encargado de gestionar habitaciones, empleados y reportes.
 */
public class Administrador extends Empleado {

    /**
     * Construye un objeto de tipo Administrador.
     * @param idEmpleado parámetro utilizado por esta operación
     * @param nombre parámetro utilizado por esta operación
     * @param documento parámetro utilizado por esta operación
     * @param telefono parámetro utilizado por esta operación
     * @param usuario parámetro utilizado por esta operación
     * @param contrasena parámetro utilizado por esta operación
     */
    public Administrador(int idEmpleado, String nombre, String documento, String telefono,
                         String usuario, String contrasena) {
        super(idEmpleado, nombre, documento, telefono, "Administrador", usuario, contrasena);
    }

    /**
     * Agrega una habitación al hotel o a la lista indicada.
     * @param habitaciones parámetro utilizado por esta operación
     * @param nueva parámetro utilizado por esta operación
     */
    public void agregarHabitacion(ArrayList<Habitacion> habitaciones, Habitacion nueva) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == nueva.getNumero()) {
                System.out.println("Ya existe una habitación con ese número.");
                return;
            }
        }
        habitaciones.add(nueva);
        System.out.println("Habitación registrada.");
    }

    /**
     * Elimina la habitación indicada por su número.
     * @param habitaciones parámetro utilizado por esta operación
     * @param numero parámetro utilizado por esta operación
     */
    public void eliminarHabitacion(ArrayList<Habitacion> habitaciones, int numero) {
        for (int i = 0; i < habitaciones.size(); i++) {
            Habitacion h = habitaciones.get(i);
            if (h.getNumero() == numero) {
                if (!h.getEstado().equals("Disponible")) {
                    System.out.println("Solo se puede eliminar una habitación disponible.");
                    return;
                }
                habitaciones.remove(i);
                System.out.println("Habitación eliminada.");
                return;
            }
        }
        System.out.println("No existe esa habitación.");
    }

    /**
     * Agrega un empleado a la lista.
     * @param empleados parámetro utilizado por esta operación
     * @param nuevo parámetro utilizado por esta operación
     */
    public void agregarEmpleado(ArrayList<Empleado> empleados, Empleado nuevo) {
        for (Empleado e : empleados) {
            if (e.getUsuario().equals(nuevo.getUsuario())) {
                System.out.println("Ese usuario ya existe. Elige otro.");
                return;
            }
        }
        empleados.add(nuevo);
        System.out.println("Empleado registrado: " + nuevo.getNombre() + " (" + nuevo.getCargo() + ")");
    }

    /**
     * Permite administrar los empleados registrados.
     * @param empleados parámetro utilizado por esta operación
     */
    public void administrarEmpleados(ArrayList<Empleado> empleados) {
        System.out.println("\n--- LISTA DE EMPLEADOS ---");
        for (Empleado e : empleados) {
            e.mostrarInformacion(); // cada tipo de empleado usa su propia versión
        }
    }

    /**
     * Consulta información para los reportes del hotel.
     * @param clientes parámetro utilizado por esta operación
     * @param habitaciones parámetro utilizado por esta operación
     * @param reservas parámetro utilizado por esta operación
     */
    public void consultarReportes(ArrayList<Cliente> clientes, ArrayList<Habitacion> habitaciones,
                                  ArrayList<Reserva> reservas) {
        int disponibles = 0, ocupadas = 0, mantenimiento = 0, limpieza = 0;
        for (Habitacion h : habitaciones) {
            if (h.getEstado().equals("Disponible")) disponibles++;
            else if (h.getEstado().equals("Ocupada")) ocupadas++;
            else if (h.getEstado().equals("Mantenimiento")) mantenimiento++;
            else if (h.getEstado().equals("Limpieza")) limpieza++;
        }

        int activas = 0, enCurso = 0, finalizadas = 0, canceladas = 0;
        double valorVigentes = 0;
        for (Reserva r : reservas) {
            if (r.getEstado().equals("Activa")) {
                activas++;
                valorVigentes += r.calcularTotal();
            } else if (r.getEstado().equals("En curso")) {
                enCurso++;
                valorVigentes += r.calcularTotal();
            } else if (r.getEstado().equals("Finalizada")) {
                finalizadas++;
            } else if (r.getEstado().equals("Cancelada")) {
                canceladas++;
            }
        }

        System.out.println("\n===== REPORTE DEL HOTEL =====");
        System.out.println("Clientes registrados: " + clientes.size());
        System.out.println("Habitaciones: " + habitaciones.size());
        System.out.println("  Disponibles: " + disponibles);
        System.out.println("  Ocupadas: " + ocupadas);
        System.out.println("  En mantenimiento: " + mantenimiento);
        System.out.println("  En limpieza: " + limpieza);
        System.out.println("Reservas activas: " + activas);
        System.out.println("Reservas en curso: " + enCurso);
        System.out.println("Reservas finalizadas: " + finalizadas);
        System.out.println("Reservas canceladas: " + canceladas);
        System.out.println("Valor de reservas vigentes (activas y en curso): $" + valorVigentes);
    }
}
