import java.util.ArrayList;

/**
 * Administra la información general del hotel, sus habitaciones y sus empleados.
 */
public class Hotel {
    // Atributos
    private String nombre;
    private String direccion;
    private String telefono;
    private ArrayList<Habitacion> habitaciones; // el hotel contiene habitaciones
    private ArrayList<Empleado> empleados;      // el hotel tiene empleados

    // Constructor
    /**
     * Construye un objeto de tipo Hotel.
     * @param nombre parámetro utilizado por esta operación
     * @param direccion parámetro utilizado por esta operación
     * @param telefono parámetro utilizado por esta operación
     */
    public Hotel(String nombre, String direccion, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.habitaciones = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }

    // Getters
    /**
     * Obtiene el nombre.
     * @return resultado de la operación
     */
    public String getNombre() { return nombre; }
    /**
     * Obtiene la dirección del hotel.
     * @return resultado de la operación
     */
    public String getDireccion() { return direccion; }
    /**
     * Obtiene el teléfono registrado.
     * @return resultado de la operación
     */
    public String getTelefono() { return telefono; }
    /**
     * Obtiene la lista de habitaciones del hotel.
     * @return resultado de la operación
     */
    public ArrayList<Habitacion> getHabitaciones() { return habitaciones; }
    /**
     * Obtiene la lista de empleados del hotel.
     * @return resultado de la operación
     */
    public ArrayList<Empleado> getEmpleados() { return empleados; }

    // Métodos
    /**
     * Agrega una habitación al hotel o a la lista indicada.
     * @param nueva parámetro utilizado por esta operación
     */
    public void agregarHabitacion(Habitacion nueva) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == nueva.getNumero()) {
                System.out.println("Ya existe una habitación con ese número.");
                return;
            }
        }
        habitaciones.add(nueva);
        System.out.println("Habitación agregada al hotel.");
    }

    /**
     * Registra un empleado en el hotel.
     * @param nuevo parámetro utilizado por esta operación
     */
    public void registrarEmpleado(Empleado nuevo) {
        for (Empleado e : empleados) {
            if (e.getUsuario().equals(nuevo.getUsuario())) {
                System.out.println("Ese usuario ya existe. Elige otro.");
                return;
            }
        }
        empleados.add(nuevo);
        System.out.println("Empleado registrado en el hotel: " + nuevo.getNombre());
    }

    /**
     * Muestra las habitaciones registradas.
     */
    public void consultarHabitaciones() {
        if (habitaciones.isEmpty()) {
            System.out.println("No hay habitaciones registradas.");
        } else {
            for (Habitacion h : habitaciones) {
                h.mostrarInformacion();
            }
        }
    }

    /**
     * Muestra la información del elemento.
     */
    public void mostrarInformacion() {
        System.out.println("\n--- DATOS DEL HOTEL ---");
        System.out.println("Nombre: " + nombre);
        System.out.println("Dirección: " + direccion);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Habitaciones: " + habitaciones.size());
        System.out.println("Empleados: " + empleados.size());
    }
}
