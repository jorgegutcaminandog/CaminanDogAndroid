package mx.com.caminandog;

public class MensajesCaminandog {
    String ultimo_texto_mensaje;
    long hora_ultimo_msj;
    boolean mensaje_sin_responder;

    MensajesCaminandog(){}

    public String getUltimo_texto_mensaje() {
        return ultimo_texto_mensaje;
    }

    public void setUltimo_texto_mensaje(String ultimo_texto_mensaje) {
        this.ultimo_texto_mensaje = ultimo_texto_mensaje;
    }

    public long getHora_ultimo_msj() {
        return hora_ultimo_msj;
    }

    public void setHora_ultimo_msj(long hora_ultimo_msj) {
        this.hora_ultimo_msj = hora_ultimo_msj;
    }

    public boolean isMensaje_sin_responder() {
        return mensaje_sin_responder;
    }

    public void setMensaje_sin_responder(boolean mensaje_sin_responder) {
        this.mensaje_sin_responder = mensaje_sin_responder;
    }
}
