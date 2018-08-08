package org.petstar.dto.ishikawa;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author Tech-Pro
 */
public class PetIshikawa {
    private int id;
    private String que;
    private String donde;
    private String cuando;
    private String como;
    private String problema;
    private Date fecha;
    private String nombre_etad;
    private int id_grupo;
    private int id_etad;
    private String causa_raiz;
    private int solucionado;
    private int recurrente;
    private int analisis;
    private String elaborado;
    private String revisado;
    private String autorizado;
    private int estatus;
    private List<PetIdeas> listIdeas;
    private List<PetConsenso> listConsenso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getDonde() {
        return donde;
    }

    public void setDonde(String donde) {
        this.donde = donde;
    }

    public String getCuando() {
        return cuando;
    }

    public void setCuando(String cuando) {
        this.cuando = cuando;
    }

    public String getComo() {
        return como;
    }

    public void setComo(String como) {
        this.como = como;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombre_etad() {
        return nombre_etad;
    }

    public void setNombre_etad(String nombre_etad) {
        this.nombre_etad = nombre_etad;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public int getId_etad() {
        return id_etad;
    }

    public void setId_etad(int id_etad) {
        this.id_etad = id_etad;
    }

    public String getCausa_raiz() {
        return causa_raiz;
    }

    public void setCausa_raiz(String causa_raiz) {
        this.causa_raiz = causa_raiz;
    }

    public int getSolucionado() {
        return solucionado;
    }

    public void setSolucionado(int solucionado) {
        this.solucionado = solucionado;
    }

    public int getRecurrente() {
        return recurrente;
    }

    public void setRecurrente(int recurrente) {
        this.recurrente = recurrente;
    }

    public int getAnalisis() {
        return analisis;
    }

    public void setAnalisis(int analisis) {
        this.analisis = analisis;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
    }

    public String getRevisado() {
        return revisado;
    }

    public void setRevisado(String revisado) {
        this.revisado = revisado;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public List<PetIdeas> getListIdeas() {
        return listIdeas;
    }

    public void setListIdeas(List<PetIdeas> listIdeas) {
        this.listIdeas = listIdeas;
    }

    public List<PetConsenso> getListConsenso() {
        return listConsenso;
    }

    public void setListConsenso(List<PetConsenso> listConsenso) {
        this.listConsenso = listConsenso;
    }
}
