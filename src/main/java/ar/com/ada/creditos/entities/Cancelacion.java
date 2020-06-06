package ar.com.ada.creditos.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cancelacion")

public class Cancelacion {

    @Id
    @Column(name = "cancelacion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cancelacionId;

    @Column(name = "fecha_cancelacion")
    private Date fechaCancelacion;
    private BigDecimal importe;
    private int cuota;

    @ManyToOne
    @JoinColumn(name = "prestamo_id", referencedColumnName = "prestamo_id")
    private Prestamo prestamo;

}