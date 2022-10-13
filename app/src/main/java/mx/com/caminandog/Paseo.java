package mx.com.caminandog;

public class Paseo {
    public String operation_date;
    //String hora;
    public String categoria;
    public String perros;
    public Long num_perros;
    public String id_paseador;
    public String id_usr;
    public Double amount;
    public String id;
    public String order_id;
    public Long tiempo_paseo;
    public Double calificacion;
    public String status;
    public long inicio , fin;
    public long actualusuario;
    public String vistocalif;
    double latitud;
    double longitud;
    String direccion;
    public long timestamp;
    public String perrosNombre;


    public Paseo(){}

    public Paseo(String operation_date, String categoria, String perros, Long num_perros, String id_paseador, String id_usr, Double amount, String id, String order_id, Long tiempo_paseo, Double calificacion, String status) {
        this.operation_date = operation_date;
        this.categoria = categoria;
        this.perros = perros;
        this.num_perros = num_perros;
        this.id_paseador = id_paseador;
        this.id_usr = id_usr;
        this.amount = amount;
        this.id = id;
        this.order_id = order_id;
        this.tiempo_paseo = tiempo_paseo;
        this.calificacion = calificacion;
        this.status = status;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getVistocalif() {
        return vistocalif;
    }

    public void setVistocalif(String vistocalif) {
        this.vistocalif = vistocalif;
    }

    public long getInicio() {
        return inicio;
    }

    public void setInicio(long inicio) {
        this.inicio = inicio;
    }

    public long getActualusuario() {
        return actualusuario;
    }

    public void setActualusuario(long actualusuario) {
        this.actualusuario = actualusuario;
    }

    public String getOperation_date() {
        return operation_date;
    }

    public void setOperation_date(String operation_date) {
        this.operation_date = operation_date;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPerros() {
        return perros;
    }

    public void setPerros(String perros) {
        this.perros = perros;
    }

    public Long getNum_perros() {
        return num_perros;
    }

    public void setNum_perros(Long num_perros) {
        this.num_perros = num_perros;
    }

    public String getId_paseador() {
        return id_paseador;
    }

    public void setId_paseador(String id_paseador) {
        this.id_paseador = id_paseador;
    }

    public String getId_usr() {
        return id_usr;
    }

    public void setId_usr(String id_usr) {
        this.id_usr = id_usr;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Long getTiempo_paseo() {
        return tiempo_paseo;
    }

    public void setTiempo_paseo(Long tiempo_paseo) {
        this.tiempo_paseo = tiempo_paseo;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getFin() {
        return fin;
    }

    public void setFin(long fin) {
        this.fin = fin;
    }

    public String getPerrosNombre() {
        return perrosNombre;
    }

    public void setPerrosNombre(String perrosNombre) {
        this.perrosNombre = perrosNombre;
    }
}
