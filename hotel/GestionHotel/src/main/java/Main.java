import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Punto de entrada del sistema de gestión hotelera y controlador de sus menús principales.
 */
public class Main {

    static Scanner sc = new Scanner(System.in);
    static Hotel hotel;
    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<Habitacion> habitaciones;
    static ArrayList<Empleado> empleados;
    static ArrayList<Reserva> reservas = new ArrayList<>();

    /**
     * Inicia y ejecuta el sistema de gestión hotelera.
     * @param args parámetro utilizado por esta operación
     */
    public static void main(String[] args) {
        hotel = cargarHotel();
        if (hotel == null) {
            hotel = crearHotel();
            guardarHotel();
        }
        habitaciones = hotel.getHabitaciones();
        empleados = hotel.getEmpleados();

        cargarHabitaciones();

        if (empleados.isEmpty()) {
            crearAdministradorInicial();
        }

        boolean salir = false;
        while (!salir) {
            Empleado actual = iniciarSesion();

            if (actual == null) {
                salir = true;
            } else if (actual instanceof Administrador) {
                menuAdministrador((Administrador) actual);
            } else if (actual instanceof Recepcionista) {
                menuRecepcionista((Recepcionista) actual);
            } else {
                menuLimpieza();
            }
        }
        System.out.println("Hasta pronto.");
    }

    // ================= LECTURA CON VALIDACIÓN =================
    // Texto: no vacío y sin ";" (se usa para separar datos en los archivos)
    static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = sc.nextLine().trim();
            if (t.isEmpty()) {
                System.out.println("Este dato no puede estar vacío.");
            } else if (t.contains(";")) {
                System.out.println("No se permite el carácter ;");
            } else {
                return t;
            }
        }
    }

    // Número entero: si escriben letras, repite la pregunta
    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero válido.");
            }
        }
    }

    static int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int n = leerEntero(mensaje);
            if (n >= min && n <= max) {
                return n;
            }
            System.out.println("Elige un número entre " + min + " y " + max + ".");
        }
    }

    // Precio: número mayor que cero
    static double leerPrecio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double precio = Double.parseDouble(sc.nextLine().trim());
                if (precio > 0) {
                    return precio;
                }
                System.out.println("El precio debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("Escribe un precio válido (solo números).");
            }
        }
    }

    // Documento: solo dígitos, entre 5 y 12
    static String leerDocumento(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = sc.nextLine().trim();
            if (t.matches("\\d{5,12}")) {
                return t;
            }
            System.out.println("El documento debe tener solo números (entre 5 y 12 dígitos).");
        }
    }

    // Teléfono: solo dígitos, entre 7 y 10
    static String leerTelefono(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = sc.nextLine().trim();
            if (t.matches("\\d{7,10}")) {
                return t;
            }
            System.out.println("El teléfono debe tener solo números (entre 7 y 10 dígitos).");
        }
    }

    // Correo: validación simple
    static String leerCorreo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = sc.nextLine().trim();
            if (t.contains("@") && t.contains(".") && !t.contains(" ") && !t.contains(";")) {
                return t;
            }
            System.out.println("Correo no válido (ejemplo: nombre@correo.com).");
        }
    }

    // Contraseña: mínimo 4 caracteres
    static String leerContrasena(String mensaje) {
        while (true) {
            String t = leerTexto(mensaje);
            if (t.length() >= 4) {
                return t;
            }
            System.out.println("La contraseña debe tener mínimo 4 caracteres.");
        }
    }

    // Fecha en formato dd/mm/aaaa
    static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String t = sc.nextLine().trim();
            try {
                return LocalDate.parse(t, Reserva.FORMATO);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Usa el formato dd/mm/aaaa (ejemplo: 25/12/2026).");
            }
        }
    }

    static LocalDate leerFechaEntrada() {
        while (true) {
            LocalDate entrada = leerFecha("Fecha de entrada (dd/mm/aaaa): ");
            if (entrada.isBefore(LocalDate.now())) {
                System.out.println("La fecha de entrada no puede ser anterior a hoy.");
            } else {
                return entrada;
            }
        }
    }

    static LocalDate leerFechaSalida(LocalDate entrada) {
        while (true) {
            LocalDate salida = leerFecha("Fecha de salida (dd/mm/aaaa): ");
            if (!salida.isAfter(entrada)) {
                System.out.println("La salida debe ser posterior a la entrada.");
            } else {
                return salida;
            }
        }
    }

    static String leerTipoHabitacion() {
        System.out.println("1. Sencilla");
        System.out.println("2. Doble");
        System.out.println("3. Suite");
        int op = leerEnteroEnRango("Tipo de habitación: ", 1, 3);
        if (op == 1) return "Sencilla";
        if (op == 2) return "Doble";
        return "Suite";
    }

    static String leerMetodoPago() {
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. Transferencia");
        int op = leerEnteroEnRango("Método de pago: ", 1, 3);
        if (op == 1) return "Efectivo";
        if (op == 2) return "Tarjeta";
        return "Transferencia";
    }

    // ================= HOTEL =================
    static Hotel crearHotel() {
        System.out.println("===== PRIMERA VEZ: DATOS DEL HOTEL =====");
        String nombre = leerTexto("Nombre del hotel: ");
        String direccion = leerTexto("Dirección: ");
        String telefono = leerTelefono("Teléfono: ");
        return new Hotel(nombre, direccion, telefono);
    }

    static void guardarHotel() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("hotel.txt"));
            pw.println(hotel.getNombre() + ";" + hotel.getDireccion() + ";" + hotel.getTelefono());
            pw.close();
        } catch (IOException e) {
            System.out.println("No se pudo guardar el hotel: " + e.getMessage());
        }
    }

    static Hotel cargarHotel() {
        File archivo = new File("hotel.txt");
        if (!archivo.exists()) {
            return null;
        }
        try {
            Scanner lector = new Scanner(archivo);
            Hotel h = null;
            if (lector.hasNextLine()) {
                String[] p = lector.nextLine().split(";");
                h = new Hotel(p[0], p[1], p[2]);
            }
            lector.close();
            return h;
        } catch (Exception e) {
            return null;
        }
    }

    // ================= SESIÓN =================
    static void crearAdministradorInicial() {
        System.out.println("===== PRIMERA VEZ: CREAR ADMINISTRADOR =====");
        String nombre = leerTexto("Nombre: ");
        String documento = leerDocumento("Documento: ");
        String telefono = leerTelefono("Teléfono: ");
        String usuario = leerTexto("Usuario: ");
        String contrasena = leerContrasena("Contraseña (mínimo 4 caracteres): ");

        hotel.registrarEmpleado(new Administrador(1, nombre, documento, telefono, usuario, contrasena));
        System.out.println("Administrador creado.");
    }

    static Empleado iniciarSesion() {
        while (true) {
            System.out.println("\n===== " + hotel.getNombre() + " - INICIAR SESIÓN =====");
            System.out.print("Usuario (o escribe salir): ");
            String usuario = sc.nextLine().trim();
            if (usuario.equals("salir")) {
                return null;
            }
            System.out.print("Contraseña: ");
            String contrasena = sc.nextLine();

            for (Empleado e : empleados) {
                if (e.iniciarSesion(usuario, contrasena)) {
                    System.out.println("Bienvenido, " + e.getNombre() + " (" + e.getCargo() + ")");
                    return e;
                }
            }
            System.out.println("Usuario o contraseña incorrectos.");
        }
    }

    // ================= MENÚS =================
    static void menuAdministrador(Administrador admin) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== MENÚ ADMINISTRADOR =====");
            System.out.println("1. Registrar habitación");
            System.out.println("2. Eliminar habitación");
            System.out.println("3. Ver habitaciones");
            System.out.println("4. Cambiar estado de una habitación");
            System.out.println("5. Registrar empleado");
            System.out.println("6. Ver empleados");
            System.out.println("7. Ver reportes");
            System.out.println("8. Ver clientes");
            System.out.println("9. Ver reservas");
            System.out.println("10. Ver datos del hotel");
            System.out.println("0. Cerrar sesión");
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1: registrarHabitacion(); break;
                case 2: eliminarHabitacion(admin); break;
                case 3: listarHabitaciones(); break;
                case 4: cambiarEstadoHabitacion(); break;
                case 5: registrarEmpleado(admin); break;
                case 6: admin.administrarEmpleados(empleados); break;
                case 7: admin.consultarReportes(clientes, habitaciones, reservas); break;
                case 8: listarClientes(); break;
                case 9: listarReservas(); break;
                case 10: hotel.mostrarInformacion(); break;
                case 0: System.out.println("Sesión cerrada."); break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    static void menuRecepcionista(Recepcionista rec) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== MENÚ RECEPCIÓN =====");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Ver clientes");
            System.out.println("3. Ver habitaciones");
            System.out.println("4. Ver habitaciones disponibles");
            System.out.println("5. Crear reserva");
            System.out.println("6. Ver reservas");
            System.out.println("7. Modificar reserva");
            System.out.println("8. Cancelar reserva");
            System.out.println("9. Registrar pago de una reserva");
            System.out.println("10. Confirmar pago de una reserva");
            System.out.println("11. Ver reservas de un cliente");
            System.out.println("12. Registrar entrada (check-in)");
            System.out.println("13. Registrar salida (check-out)");
            System.out.println("0. Cerrar sesión");
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1: registrarCliente(rec); break;
                case 2: listarClientes(); break;
                case 3: listarHabitaciones(); break;
                case 4: consultarDisponibles(); break;
                case 5: crearReserva(rec); break;
                case 6: listarReservas(); break;
                case 7: modificarReserva(); break;
                case 8: cancelarReserva(rec); break;
                case 9: registrarPago(); break;
                case 10: confirmarPago(); break;
                case 11: reservasDeCliente(); break;
                case 12: registrarEntrada(rec); break;
                case 13: registrarSalida(rec); break;
                case 0: System.out.println("Sesión cerrada."); break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    static void menuLimpieza() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n===== MENÚ LIMPIEZA =====");
            System.out.println("1. Ver habitaciones por limpiar");
            System.out.println("2. Marcar habitación como limpia");
            System.out.println("0. Cerrar sesión");
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1: listarPorLimpiar(); break;
                case 2: marcarComoLimpia(); break;
                case 0: System.out.println("Sesión cerrada."); break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    // ================= BÚSQUEDAS Y CONSULTAS =================
    static Cliente buscarCliente(int id) {
        for (Cliente c : clientes) {
            if (c.getIdCliente() == id) return c;
        }
        return null;
    }

    static Cliente buscarClientePorDocumento(String documento) {
        for (Cliente c : clientes) {
            if (c.getDocumento().equals(documento)) return c;
        }
        return null;
    }

    static Habitacion buscarHabitacion(int numero) {
        for (Habitacion h : habitaciones) {
            if (h.getNumero() == numero) return h;
        }
        return null;
    }

    static Reserva buscarReserva(int id) {
        for (Reserva r : reservas) {
            if (r.getIdReserva() == id) return r;
        }
        return null;
    }

    // ¿La habitación tiene una reserva activa o en curso?
    static boolean tieneReservasVigentes(Habitacion hab) {
        for (Reserva r : reservas) {
            if (r.getHabitacion().getNumero() == hab.getNumero()
                    && (r.getEstado().equals("Activa") || r.getEstado().equals("En curso"))) {
                return true;
            }
        }
        return false;
    }

    // ¿La habitación tiene un huésped alojado ahora?
    static boolean reservaEnCurso(Habitacion hab) {
        for (Reserva r : reservas) {
            if (r.getHabitacion().getNumero() == hab.getNumero() && r.getEstado().equals("En curso")) {
                return true;
            }
        }
        return false;
    }

    // ¿Las fechas se cruzan con otra reserva de la misma habitación?
    static boolean hayCruce(Habitacion hab, LocalDate entrada, LocalDate salida, Reserva ignorar) {
        for (Reserva r : reservas) {
            if (r == ignorar) continue;
            if (r.getHabitacion().getNumero() == hab.getNumero() && r.ocupaFechas(entrada, salida)) {
                System.out.println("La habitación ya tiene la reserva #" + r.getIdReserva() + " en esas fechas.");
                return true;
            }
        }
        return false;
    }

    // ================= EMPLEADOS =================
    static void registrarEmpleado(Administrador admin) {
        System.out.println("\n--- REGISTRAR EMPLEADO ---");
        System.out.println("1. Recepcionista");
        System.out.println("2. Administrador");
        System.out.println("3. Personal de limpieza");
        int tipo = leerEnteroEnRango("Tipo de empleado: ", 1, 3);

        int id = empleados.size() + 1;
        String nombre = leerTexto("Nombre: ");
        String documento = leerDocumento("Documento: ");
        String telefono = leerTelefono("Teléfono: ");
        String usuario = leerTexto("Usuario: ");
        String contrasena = leerContrasena("Contraseña (mínimo 4 caracteres): ");

        Empleado nuevo;
        if (tipo == 1) {
            System.out.println("1. Mañana");
            System.out.println("2. Tarde");
            System.out.println("3. Noche");
            int t = leerEnteroEnRango("Turno: ", 1, 3);
            String turno;
            if (t == 1) turno = "Mañana";
            else if (t == 2) turno = "Tarde";
            else turno = "Noche";
            nuevo = new Recepcionista(id, nombre, documento, telefono, usuario, contrasena, turno);
        } else if (tipo == 2) {
            nuevo = new Administrador(id, nombre, documento, telefono, usuario, contrasena);
        } else {
            nuevo = new Empleado(id, nombre, documento, telefono, "Limpieza", usuario, contrasena);
        }
        admin.agregarEmpleado(empleados, nuevo);
    }

    // ================= CLIENTES =================
    static void registrarCliente(Recepcionista rec) {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        int id = clientes.size() + 1;
        String nombre = leerTexto("Nombre: ");
        String documento = leerDocumento("Documento: ");

        Cliente existente = buscarClientePorDocumento(documento);
        if (existente != null) {
            System.out.println("Ya existe un cliente con ese documento: " + existente.getNombre()
                    + " (ID " + existente.getIdCliente() + ").");
            return;
        }

        String telefono = leerTelefono("Teléfono: ");
        String correo = leerCorreo("Correo: ");

        rec.registrarCliente(clientes, new Cliente(id, nombre, documento, telefono, correo));
    }

    static void listarClientes() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Cliente c : clientes) {
                c.mostrarInformacion();
            }
        }
    }

    static void reservasDeCliente() {
        System.out.println("\n--- RESERVAS DE UN CLIENTE ---");
        Cliente c = buscarCliente(leerEntero("ID del cliente: "));
        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        c.consultarReservas(reservas);
    }

    // ================= HABITACIONES =================
    static void registrarHabitacion() {
        System.out.println("\n--- REGISTRAR HABITACIÓN ---");
        int numero = leerEntero("Número de habitación: ");
        if (numero <= 0) {
            System.out.println("El número debe ser mayor que 0.");
            return;
        }
        if (buscarHabitacion(numero) != null) {
            System.out.println("Ya existe una habitación con ese número.");
            return;
        }

        String tipo = leerTipoHabitacion();
        double precio = leerPrecio("Precio por noche: ");

        hotel.agregarHabitacion(new Habitacion(numero, tipo, precio));
        guardarHabitaciones();
    }

    static void eliminarHabitacion(Administrador admin) {
        System.out.println("\n--- ELIMINAR HABITACIÓN ---");
        int numero = leerEntero("Número de habitación: ");
        Habitacion h = buscarHabitacion(numero);
        if (h != null && tieneReservasVigentes(h)) {
            System.out.println("No se puede eliminar: la habitación tiene reservas activas o en curso.");
            return;
        }
        admin.eliminarHabitacion(habitaciones, numero);
        guardarHabitaciones();
    }

    static void listarHabitaciones() {
        System.out.println("\n--- LISTA DE HABITACIONES ---");
        hotel.consultarHabitaciones();
    }

    static void consultarDisponibles() {
        System.out.println("\n--- HABITACIONES DISPONIBLES ---");
        boolean hay = false;
        for (Habitacion h : habitaciones) {
            if (h.consultarDisponibilidad()) {
                h.mostrarInformacion();
                hay = true;
            }
        }
        if (!hay) {
            System.out.println("No hay habitaciones disponibles.");
        }
    }

    static void cambiarEstadoHabitacion() {
        System.out.println("\n--- CAMBIAR ESTADO ---");
        Habitacion h = buscarHabitacion(leerEntero("Número de la habitación: "));
        if (h == null) {
            System.out.println("No existe esa habitación.");
            return;
        }
        System.out.println("1. Disponible");
        System.out.println("2. Ocupada");
        System.out.println("3. Mantenimiento");
        System.out.println("4. Limpieza");
        int op = leerEnteroEnRango("Nuevo estado: ", 1, 4);

        if (reservaEnCurso(h) && op != 2) {
            System.out.println("La habitación tiene un huésped alojado. Registra primero la salida.");
            return;
        }

        if (op == 1) h.cambiarEstado("Disponible");
        else if (op == 2) h.cambiarEstado("Ocupada");
        else if (op == 3) h.cambiarEstado("Mantenimiento");
        else h.cambiarEstado("Limpieza");

        guardarHabitaciones();
        System.out.println("Estado actualizado.");
    }

    // ================= LIMPIEZA =================
    static void listarPorLimpiar() {
        System.out.println("\n--- HABITACIONES POR LIMPIAR ---");
        boolean hay = false;
        for (Habitacion h : habitaciones) {
            if (h.getEstado().equals("Limpieza")) {
                h.mostrarInformacion();
                hay = true;
            }
        }
        if (!hay) {
            System.out.println("No hay habitaciones pendientes de limpieza.");
        }
    }

    static void marcarComoLimpia() {
        System.out.println("\n--- MARCAR COMO LIMPIA ---");
        Habitacion h = buscarHabitacion(leerEntero("Número de la habitación: "));
        if (h == null) {
            System.out.println("No existe esa habitación.");
            return;
        }
        if (!h.getEstado().equals("Limpieza")) {
            System.out.println("Esa habitación no está pendiente de limpieza.");
            return;
        }
        h.cambiarEstado("Disponible");
        guardarHabitaciones();
        System.out.println("Habitación " + h.getNumero() + " lista y disponible.");
    }

    // ================= RESERVAS =================
    static void crearReserva(Recepcionista rec) {
        System.out.println("\n--- CREAR RESERVA ---");
        listarClientes();
        Cliente cliente = buscarCliente(leerEntero("ID del cliente: "));
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        listarHabitaciones();
        Habitacion hab = buscarHabitacion(leerEntero("Número de habitación: "));
        if (hab == null) {
            System.out.println("Habitación no encontrada.");
            return;
        }
        if (hab.getEstado().equals("Mantenimiento")) {
            System.out.println("La habitación está en mantenimiento.");
            return;
        }

        LocalDate entrada = leerFechaEntrada();
        LocalDate salida = leerFechaSalida(entrada);

        if (hayCruce(hab, entrada, salida, null)) {
            return;
        }

        Reserva nueva = new Reserva(reservas.size() + 1, cliente, hab, entrada, salida);
        rec.realizarReserva(reservas, nueva);
        System.out.println("Noches: " + nueva.getNoches() + " | Total a pagar: $" + nueva.calcularTotal());
    }

    static void listarReservas() {
        System.out.println("\n--- LISTA DE RESERVAS ---");
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas.");
        } else {
            for (Reserva r : reservas) {
                r.mostrarInformacion();
            }
        }
    }

    static void modificarReserva() {
        System.out.println("\n--- MODIFICAR RESERVA ---");
        Reserva r = buscarReserva(leerEntero("ID de la reserva: "));
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        if (!r.getEstado().equals("Activa")) {
            System.out.println("Solo se pueden modificar reservas activas.");
            return;
        }
        LocalDate entrada = leerFechaEntrada();
        LocalDate salida = leerFechaSalida(entrada);

        if (hayCruce(r.getHabitacion(), entrada, salida, r)) {
            return;
        }
        r.modificarReserva(entrada, salida);
    }

    static void cancelarReserva(Recepcionista rec) {
        System.out.println("\n--- CANCELAR RESERVA ---");
        Reserva r = buscarReserva(leerEntero("ID de la reserva: "));
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        if (!r.getEstado().equals("Activa")) {
            System.out.println("Solo se pueden cancelar reservas activas. Estado actual: " + r.getEstado());
            return;
        }
        rec.cancelarReserva(r);
    }

    // ================= ENTRADA Y SALIDA =================
    static void registrarEntrada(Recepcionista rec) {
        System.out.println("\n--- REGISTRAR ENTRADA ---");
        listarReservas();
        Reserva r = buscarReserva(leerEntero("ID de la reserva: "));
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        if (rec.registrarEntrada(r)) {
            guardarHabitaciones();
        }
    }

    static void registrarSalida(Recepcionista rec) {
        System.out.println("\n--- REGISTRAR SALIDA ---");
        listarReservas();
        Reserva r = buscarReserva(leerEntero("ID de la reserva: "));
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        if (rec.registrarSalida(r)) {
            guardarHabitaciones();
        }
    }

    // ================= PAGOS =================
    static void registrarPago() {
        System.out.println("\n--- REGISTRAR PAGO ---");
        Reserva r = buscarReserva(leerEntero("ID de la reserva: "));
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        if (!r.getEstado().equals("Activa") && !r.getEstado().equals("En curso")) {
            System.out.println("Solo se puede pagar una reserva activa o en curso. Estado actual: " + r.getEstado());
            return;
        }
        if (r.getPago() != null) {
            System.out.println("Esta reserva ya tiene un pago registrado.");
            return;
        }

        String fecha = LocalDate.now().format(Reserva.FORMATO); // fecha automática: hoy
        String metodo = leerMetodoPago();

        Pago pago = new Pago(r.getIdReserva(), fecha, r.calcularTotal(), metodo);
        r.setPago(pago);
        pago.realizarPago();
    }

    static void confirmarPago() {
        System.out.println("\n--- CONFIRMAR PAGO ---");
        Reserva r = buscarReserva(leerEntero("ID de la reserva: "));
        if (r == null) {
            System.out.println("Reserva no encontrada.");
            return;
        }
        if (r.getPago() == null) {
            System.out.println("Esta reserva no tiene pago registrado.");
            return;
        }
        r.getPago().confirmarPago();
    }

    // ================= PERSISTENCIA =================
    static void guardarHabitaciones() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("habitaciones.txt"));
            for (Habitacion h : habitaciones) {
                pw.println(h.getNumero() + ";" + h.getTipo() + ";" + h.getPrecio() + ";" + h.getEstado());
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
        }
    }

    static void cargarHabitaciones() {
        File archivo = new File("habitaciones.txt");
        if (!archivo.exists()) {
            return; // primera vez: no hay nada que cargar
        }
        try {
            Scanner lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                try {
                    String[] p = linea.split(";");
                    Habitacion h = new Habitacion(Integer.parseInt(p[0]), p[1], Double.parseDouble(p[2]));
                    h.cambiarEstado(p[3]);
                    habitaciones.add(h);
                } catch (Exception e) {
                    System.out.println("Línea ignorada en habitaciones.txt: " + linea);
                }
            }
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
        }
    }
}
