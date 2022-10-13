package mx.com.caminandog;

public class Paseador {

    public int estatus;
    public int cantidadPerros;
    public String categoria;
    public String idPaseador;
    public String direcfoto;
    public String nombre;
    public String celular;
    public String apellidopa, apellidoma;


    public Paseador() {
    }

    public Paseador(int estatus , int cantidadPerros , String categoria , String idPaseador) {
        this.estatus = estatus;
        this.cantidadPerros = cantidadPerros;
        this.categoria = categoria;
        this.idPaseador = idPaseador;
    }

    public String getDirecfoto() {
        return direcfoto;
    }

    public void setDirecfoto(String direcfoto) {
        this.direcfoto = direcfoto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getCantidadPerros() {
        return cantidadPerros;
    }

    public void setCantidadPerros(int cantidadPerros) {
        this.cantidadPerros = cantidadPerros;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIdPaseador() {
        return idPaseador;
    }

    public void setIdPaseador(String idPaseador) {
        this.idPaseador = idPaseador;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getApellidopa() {
        return apellidopa;
    }

    public void setApellidopa(String apellidopa) {
        this.apellidopa = apellidopa;
    }

    public String getApellidoma() {
        return apellidoma;
    }

    public void setApellidoma(String apellidoma) {
        this.apellidoma = apellidoma;
    }
}


