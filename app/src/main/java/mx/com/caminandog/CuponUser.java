package mx.com.caminandog;

public class CuponUser {
    boolean activo;
    String categoria, descripcion, id, nombre, unidad;
    int paseos, perros;
    double tiempo;
    double cifra;

    public  CuponUser (){
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public int getPaseos() {
        return paseos;
    }

    public void setPaseos(int paseos) {
        this.paseos = paseos;
    }

    public int getPerros() {
        return perros;
    }

    public void setPerros(int perros) {
        this.perros = perros;
    }

    public double getTiempo() {
        return tiempo;
    }

    public void setTiempo(double tiempo) {
        this.tiempo = tiempo;
    }

    public double getCifra() {
        return cifra;
    }

    public void setCifra(double cifra) {
        this.cifra = cifra;
    }
}
