package org.petstar.dto;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Tech-Pro
 */
public class ReporteDiario {
    private Date dia;
    private BigDecimal plan_molido;

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public BigDecimal getPlan_molido() {
        return plan_molido;
    }

    public void setPlan_molido(BigDecimal plan_molido) {
        this.plan_molido = plan_molido;
    }
}
