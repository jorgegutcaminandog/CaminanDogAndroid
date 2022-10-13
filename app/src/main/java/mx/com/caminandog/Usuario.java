package mx.com.caminandog;

public class Usuario {
    public String uid;
    public String email;
    public String nombre;
    public  String apellido_Paterno;
    public  String apellido_Materno;
    public  String dia;
    public   String telefono1;
    public   String telefono2;
    public   String mes;
    public  String anio;
    public  double latitud;
    public   double longitud;
    public   String menteraste;
    public String nombreRecuperandog;
    public  String telefonoRecuperandog;
    public String direccion;
    String correoRecuperandog;
    String mensajeRecuperandog;


    public Usuario(){}


    public Usuario(String uid, String email, String nombre, String apellido_Paterno, String apellido_Materno, String dia, String telefono1, String telefono2, String mes, String anio) {
        this.uid = uid;
        this.email = email;
        this.nombre = nombre;
        this.apellido_Paterno = apellido_Paterno;
        this.apellido_Materno = apellido_Materno;
        this.dia = dia;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.mes = mes;
        this.anio = anio;

    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getMenteraste() {
        return menteraste;
    }

    public void setMenteraste(String menteraste) {
        this.menteraste = menteraste;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido_Paterno() {
        return apellido_Paterno;
    }

    public String getApellido_Materno() {
        return apellido_Materno;
    }

    public String getDia() {
        return dia;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public String getMes() {
        return mes;
    }

    public String getAnio() {
        return anio;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido_Paterno(String apellido_Paterno) {
        this.apellido_Paterno = apellido_Paterno;
    }

    public void setApellido_Materno(String apellido_Materno) {
        this.apellido_Materno = apellido_Materno;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }


    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNombreRecuperandog() {
        return nombreRecuperandog;
    }

    public void setNombreRecuperandog(String nombreRecuperandog) {
        this.nombreRecuperandog = nombreRecuperandog;
    }

    public String getTelefonoRecuperandog() {
        return telefonoRecuperandog;
    }

    public void setTelefonoRecuperandog(String telefonoRecuperandog) {
        this.telefonoRecuperandog = telefonoRecuperandog;
    }

    public String getCorreoRecuperandog() {
        return correoRecuperandog;
    }

    public void setCorreoRecuperandog(String correoRecuperandog) {
        this.correoRecuperandog = correoRecuperandog;
    }

    public String getMensajeRecuperandog() {
        return mensajeRecuperandog;
    }

    public void setMensajeRecuperandog(String mensajeRecuperandog) {
        this.mensajeRecuperandog = mensajeRecuperandog;
    }
}
