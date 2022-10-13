package mx.com.caminandog;

public class ModeloTracking {
    public double latitud;
    public double longitud;

    public ModeloTracking() {
    }

    public ModeloTracking(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
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
}
