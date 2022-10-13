package mx.com.caminandog;

public class Mensaje {
    boolean mensajeUsuario, mensajePaseador;

    public Mensaje() {}

    public boolean isMensajeUsuario() {
        return mensajeUsuario;
    }

    public void setMensajeUsuario(boolean mensajeUsuario) {
        this.mensajeUsuario = mensajeUsuario;
    }

    public boolean isMensajePaseador() {
        return mensajePaseador;
    }

    public void setMensajePaseador(boolean mensajePaseador) {
        this.mensajePaseador = mensajePaseador;
    }
}
