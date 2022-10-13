package mx.com.caminandog;

public class ModelDatosPipiPopo {
    Integer pipi, popo;
    String foto, idPerro, nombre;

    public ModelDatosPipiPopo() {
    }

    public ModelDatosPipiPopo(Integer pipi, Integer popo, String foto, String idPerro, String nombre) {
        this.pipi = pipi;
        this.popo = popo;
        this.foto = foto;
        this.idPerro = idPerro;
        this.nombre = nombre;
    }

    public Integer getPipi() {
        return pipi;
    }

    public void setPipi(Integer pipi) {
        this.pipi = pipi;
    }

    public Integer getPopo() {
        return popo;
    }

    public void setPopo(Integer popo) {
        this.popo = popo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdPerro() {
        return idPerro;
    }

    public void setIdPerro(String idPerro) {
        this.idPerro = idPerro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
