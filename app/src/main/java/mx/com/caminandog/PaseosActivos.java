package mx.com.caminandog;

class PaseosActivos {
    public String order_id,perros,tipo,operation_date,foto_paseador, nombre_paseador, estatus, perrosNombre,ultimo_msj_texto;
    public int num_perros,tiempo_paseo;
    public long timestamp, ultimo_msj_timestamp;
    boolean mensaje;

    public PaseosActivos() {
    }

    public PaseosActivos(String order_id, String perros, String tipo) {
        this.order_id = order_id;
        this.perros = perros;
        this.tipo = tipo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getOperation_date() {
        return operation_date;
    }

    public void setOperation_date(String operation_date) {
        this.operation_date = operation_date;
    }

    public String getFoto_paseador() {
        return foto_paseador;
    }

    public void setFoto_paseador(String foto_paseador) {
        this.foto_paseador = foto_paseador;
    }

    public String getNombre_paseador() {
        return nombre_paseador;
    }

    public void setNombre_paseador(String nombre_paseador) {
        this.nombre_paseador = nombre_paseador;
    }

    public int getNum_perros() {
        return num_perros;
    }

    public void setNum_perros(int num_perros) {
        this.num_perros = num_perros;
    }

    public int getTiempo_paseo() {
        return tiempo_paseo;
    }

    public void setTiempo_paseo(int tiempo_paseo) {
        this.tiempo_paseo = tiempo_paseo;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPerros() {
        return perros;
    }

    public void setPerros(String perros) {
        this.perros = perros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMensaje() {
        return mensaje;
    }

    public void setMensaje(boolean mensaje) {
        this.mensaje = mensaje;
    }

    public String getPerrosNombre() {
        return perrosNombre;
    }

    public void setPerrosNombre(String perrosNombre) {
        this.perrosNombre = perrosNombre;
    }

    public String getUltimo_msj_texto() {
        return ultimo_msj_texto;
    }

    public void setUltimo_msj_texto(String ultimo_msj_texto) {
        this.ultimo_msj_texto = ultimo_msj_texto;
    }

    public long getUltimo_msj_timestamp() {
        return ultimo_msj_timestamp;
    }

    public void setUltimo_msj_timestamp(long ultimo_msj_timestamp) {
        this.ultimo_msj_timestamp = ultimo_msj_timestamp;
    }
}

