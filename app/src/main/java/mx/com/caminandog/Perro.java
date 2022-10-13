package mx.com.caminandog;

public class Perro {
    public String uidUsuario;
    public String comportamiento;
    public String edad;
    public String nombre;
    public String padecimiento;
    public String raza;
    public String foto;
    public String idPerro;


    public Perro(){}

    public String getUidUsuario() {
        return uidUsuario;
    }

    public String getComportamiento() {
        return comportamiento;
    }

    public String getEdad() {
        return edad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPadecimiento() {
        return padecimiento;
    }

    public String getRaza() {
        return raza;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public void setComportamiento(String comportamiento) {
        this.comportamiento = comportamiento;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPadecimiento(String padecimiento) {
        this.padecimiento = padecimiento;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getIdPerro() {
        return idPerro;
    }

    public void setIdPerro(String idPerro) {
        this.idPerro = idPerro;
    }

    public Perro(String uidUsuario, String comportamiento, String edad, String nombre, String padecimiento, String raza, String foto, String idPerro){
        this.uidUsuario = uidUsuario;
        this.comportamiento = comportamiento;
        this.edad = edad;
        this.nombre = nombre;
        this.padecimiento = padecimiento;
        this.raza = raza;
        this.foto = foto;
        this.idPerro = idPerro;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
