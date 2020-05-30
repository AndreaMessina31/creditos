package ar.com.ada.creditos.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;



// Entity indica que esa clase actuara como entidad
//Table indica a que tabla va a persistirse ese objeto
@Entity
@Table(name="prestamo")

public class Prestamo {

 @Id// Es una PK
 @Column(name="prestamo_id")
 @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prestamoId;
    
    private BigDecimal importe;
    private Date fecha;
    @Column(name= "cuota")
    private int cuotas;
    @Column(name="fecha_alta")
    private Date fechaAlta;
    @Column(name="fecha_modificacion")
    private Date fechaModificacion;

    @ManyToOne
    @JoinColumn(name="cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date i) {
        this.fecha = i;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date date) {
        this.fechaAlta = date;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.cliente.getPrestamos().add(this);
    }

    
    


}