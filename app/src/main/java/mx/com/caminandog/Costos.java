package mx.com.caminandog;

public class Costos {
    double envio, costoAndroid, vigencia;
    String tipo;

    Costos (){}

    public double getEnvio() {
        return envio;
    }

    public void setEnvio(double envio) {
        this.envio = envio;
    }

    public double getCostoAndroid() {
        return costoAndroid;
    }

    public void setCostoAndroid(double costoAndroid) {
        this.costoAndroid = costoAndroid;
    }

    public double getVigencia() {
        return vigencia;
    }

    public void setVigencia(double vigencia) {
        this.vigencia = vigencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
