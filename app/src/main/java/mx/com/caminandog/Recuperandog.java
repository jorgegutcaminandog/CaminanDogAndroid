package mx.com.caminandog;




public class Recuperandog {
    String idPerro, QR, servicio, lectura, primeraLectura, reportado;
    long numero_codigo,fechaVencimiento;



    public Recuperandog() {
    }

    public String getPrimeraLectura() {
        return primeraLectura;
    }

    public void setPrimeraLectura(String primeraLectura) {
        this.primeraLectura = primeraLectura;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public long getNumero_codigo() {
        return numero_codigo;
    }

    public void setNumero_codigo(long numero_codigo) {
        this.numero_codigo = numero_codigo;
    }

    public String getIdPerro() {
        return idPerro;
    }

    public void setIdPerro(String idPerro) {
        this.idPerro = idPerro;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getReportado() {
        return reportado;
    }

    public void setReportado(String reportado) {
        this.reportado = reportado;
    }

    public long getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}
