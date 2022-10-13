package mx.com.caminandog;

public class Cupon {
    String descripcion, nombre, cantidad_uso, categoria, estatus, id, tipo_usuario, unidad_descuento;
    long fecha_creacion, fecha_inicia, fecha_termina;
    double monto_descuento, monto_minimo;
    int paseos, perros, tiempo;

    public Cupon() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad_uso() {
        return cantidad_uso;
    }

    public void setCantidad_uso(String cantidad_uso) {
        this.cantidad_uso = cantidad_uso;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public String getUnidad_descuento() {
        return unidad_descuento;
    }

    public void setUnidad_descuento(String unidad_descuento) {
        this.unidad_descuento = unidad_descuento;
    }

    public long getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(long fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public long getFecha_inicia() {
        return fecha_inicia;
    }

    public void setFecha_inicia(long fecha_inicia) {
        this.fecha_inicia = fecha_inicia;
    }

    public long getFecha_termina() {
        return fecha_termina;
    }

    public void setFecha_termina(long fecha_termina) {
        this.fecha_termina = fecha_termina;
    }

    public double getMonto_descuento() {
        return monto_descuento;
    }

    public void setMonto_descuento(double monto_descuento) {
        this.monto_descuento = monto_descuento;
    }

    public double getMonto_minimo() {
        return monto_minimo;
    }

    public void setMonto_minimo(double monto_minimo) {
        this.monto_minimo = monto_minimo;
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

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
}
