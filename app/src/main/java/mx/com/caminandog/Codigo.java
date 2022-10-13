package mx.com.caminandog;

public class Codigo {
    String id_perro,id_codigo;
    long numero_codigo,timestamp;

    public Codigo(){}

    public String getId_perro() {
        return id_perro;
    }

    public void setId_perro(String id_perro) {
        this.id_perro = id_perro;
    }

    public String getId_codigo() {
        return id_codigo;
    }

    public void setId_codigo(String id_codigo) {
        this.id_codigo = id_codigo;
    }

    public long getNumero_codigo() {
        return numero_codigo;
    }

    public void setNumero_codigo(long numero_codigo) {
        this.numero_codigo = numero_codigo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
