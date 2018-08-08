package org.petstar.dto;

/**
 *
 * @author Tech-Pro
 */
public class UserSonarhDTO {
    private int Activo;
    private int NumEmpleado;
    private String Nombre;
    private String Paterno;
    private String Materno;
    private int id_grupo;
    private String Grupo;
    private String Area;

    public int getActivo() {
        return Activo;
    }

    public void setActivo(int Activo) {
        this.Activo = Activo;
    }

    public int getNumEmpleado() {
        return NumEmpleado;
    }

    public void setNumEmpleado(int NumEmpleado) {
        this.NumEmpleado = NumEmpleado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getPaterno() {
        return Paterno;
    }

    public void setPaterno(String Paterno) {
        this.Paterno = Paterno;
    }

    public String getMaterno() {
        return Materno;
    }

    public void setMaterno(String Materno) {
        this.Materno = Materno;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getGrupo() {
        return Grupo;
    }

    public void setGrupo(String Grupo) {
        this.Grupo = Grupo;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }
}
