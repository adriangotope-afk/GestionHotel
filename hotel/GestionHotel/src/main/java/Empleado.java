/**
 * Representa a un empleado del hotel y contiene sus datos de acceso e información personal.
 */
public class Empleado {
    // Atributos
    private int idEmpleado;
    private String nombre;
    private String documento;
    private String telefono;
    private String cargo;
    private String usuario;
    private String contrasena;

    // Constructor
    /**
     * Construye un objeto de tipo Empleado.
     * @param idEmpleado parámetro utilizado por esta operación
     * @param nombre parámetro utilizado por esta operación
     * @param documento parámetro utilizado por esta operación
     * @param telefono parámetro utilizado por esta operación
     * @param cargo parámetro utilizado por esta operación
     * @param usuario parámetro utilizado por esta operación
     * @param contrasena parámetro utilizado por esta operación
     */
    public Empleado(int idEmpleado, String nombre, String documento, String telefono,
                    String cargo, String usuario, String contrasena) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.cargo = cargo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Getters
    /**
     * Obtiene el identificador del empleado.
     * @return resultado de la operación
     */
    public int getIdEmpleado() { return idEmpleado; }
    /**
     * Obtiene el nombre.
     * @return resultado de la operación
     */
    public String getNombre() { return nombre; }
    /**
     * Obtiene el cargo del empleado.
     * @return resultado de la operación
     */
    public String getCargo() { return cargo; }
    /**
     * Obtiene el nombre de usuario.
     * @return resultado de la operación
     */
    public String getUsuario() { return usuario; }

    // Métodos
    /**
     * Valida las credenciales de acceso del empleado.
     * @param usuario parámetro utilizado por esta operación
     * @param contrasena parámetro utilizado por esta operación
     * @return resultado de la operación
     */
    public boolean iniciarSesion(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    /**
     * Actualiza los datos indicados.
     * @param telefono parámetro utilizado por esta operación
     */
    public void actualizarDatos(String telefono) {
        this.telefono = telefono;
        System.out.println("Datos actualizados.");
    }

    /**
     * Muestra la información del elemento.
     */
    public void mostrarInformacion() {
        System.out.println("ID: " + idEmpleado + " | " + nombre + " | Doc: " + documento
                + " | Tel: " + telefono + " | Cargo: " + cargo + " | Usuario: " + usuario);
    }
}
