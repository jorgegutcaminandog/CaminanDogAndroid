package mx.com.caminandog;

class Configuracion {

    int diasRecuperandog,mesesRecuperandog;
    String foto_costos;
    boolean mediaHora;

    public Configuracion() {
    }

    public int getMesesRecuperandog() {
        return mesesRecuperandog;
    }

    public void setMesesRecuperandog(int mesesRecuperandog) {
        this.mesesRecuperandog = mesesRecuperandog;
    }

    public String getFoto_costos() {
        return foto_costos;
    }

    public void setFoto_costos(String foto_costos) {
        this.foto_costos = foto_costos;
    }

    public int getDiasRecuperandog() {
        return diasRecuperandog;
    }

    public void setDiasRecuperandog(int diasRecuperandog) {
        this.diasRecuperandog = diasRecuperandog;
    }

    public boolean isMediaHora() {
        return mediaHora;
    }

    public void setMediaHora(boolean mediaHora) {
        this.mediaHora = mediaHora;
    }
}
