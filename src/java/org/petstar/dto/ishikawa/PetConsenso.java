package org.petstar.dto.ishikawa;

/**
 *
 * @author Tech-Pro
 */
public class PetConsenso {
    private int id_detalle;
    private int id_pregunta;
    private int id_ishikawa;
    private int respuesta;

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(int id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public int getId_ishikawa() {
        return id_ishikawa;
    }

    public void setId_ishikawa(int id_ishikawa) {
        this.id_ishikawa = id_ishikawa;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }
}
