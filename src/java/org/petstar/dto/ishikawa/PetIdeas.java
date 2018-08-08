package org.petstar.dto.ishikawa;

import org.petstar.dto.CatalogosDTO;

/**
 *
 * @author Tech-Pro
 */
public class PetIdeas {
    private int id_idea;
    private int id_ishikawa;
    private int id_eme;
    private String idea;
    private CatalogosDTO eme;
    private PetPorques porques;

    public int getId_idea() {
        return id_idea;
    }

    public void setId_idea(int id_idea) {
        this.id_idea = id_idea;
    }

    public int getId_ishikawa() {
        return id_ishikawa;
    }

    public void setId_ishikawa(int id_ishikawa) {
        this.id_ishikawa = id_ishikawa;
    }

    public int getId_eme() {
        return id_eme;
    }

    public void setId_eme(int id_eme) {
        this.id_eme = id_eme;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public CatalogosDTO getEme() {
        return eme;
    }

    public void setEme(CatalogosDTO eme) {
        this.eme = eme;
    }

    public PetPorques getPorques() {
        return porques;
    }

    public void setPorques(PetPorques porques) {
        this.porques = porques;
    }
}
