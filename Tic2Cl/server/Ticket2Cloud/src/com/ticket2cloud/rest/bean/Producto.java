/**
 * @author G.M Echevarria
 * 
 */
package com.ticket2cloud.rest.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author G.M Echevarria
 *
 */
@XmlRootElement(name = "producto")
public class Producto {
	private Integer orden;
	private String nombre;
	private Integer numeroUnidadesProducto;
	private Float pesoKgProducto;
	private Float ivaAplicadoProducto;
	private Float precioSinIvaProducto;
	private Float precioConIvaProducto;
	private Float precioSinIvaProductoKg;
	private Float precioConIvaProductoKg;	
	private Float cuotaIva;
	private Float pesoKg;
	/**
	 * @return the orden
	 */
	public Integer getOrden() {
		return orden;
	}
	/**
	 * @param orden the orden to set
	 */
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the numeroUnidadesProducto
	 */
	public Integer getNumeroUnidadesProducto() {
		return numeroUnidadesProducto;
	}
	/**
	 * @param numeroUnidadesProducto the numeroUnidadesProducto to set
	 */
	public void setNumeroUnidadesProducto(Integer numeroUnidadesProducto) {
		this.numeroUnidadesProducto = numeroUnidadesProducto;
	}
	/**
	 * @return the pesoKgProducto
	 */
	public Float getPesoKgProducto() {
		return pesoKgProducto;
	}
	/**
	 * @param pesoKgProducto the pesoKgProducto to set
	 */
	public void setPesoKgProducto(Float pesoKgProducto) {
		this.pesoKgProducto = pesoKgProducto;
	}
	/**
	 * @return the ivaAplicadoProducto
	 */
	public Float getIvaAplicadoProducto() {
		return ivaAplicadoProducto;
	}
	/**
	 * @param ivaAplicadoProducto the ivaAplicadoProducto to set
	 */
	public void setIvaAplicadoProducto(Float ivaAplicadoProducto) {
		this.ivaAplicadoProducto = ivaAplicadoProducto;
	}
	/**
	 * @return the precioSinIvaProducto
	 */
	public Float getPrecioSinIvaProducto() {
		return precioSinIvaProducto;
	}
	/**
	 * @param precioSinIvaProducto the precioSinIvaProducto to set
	 */
	public void setPrecioSinIvaProducto(Float precioSinIvaProducto) {
		this.precioSinIvaProducto = precioSinIvaProducto;
	}
	/**
	 * @return the precioConIvaProducto
	 */
	public Float getPrecioConIvaProducto() {
		return precioConIvaProducto;
	}
	/**
	 * @param precioConIvaProducto the precioConIvaProducto to set
	 */
	public void setPrecioConIvaProducto(Float precioConIvaProducto) {
		this.precioConIvaProducto = precioConIvaProducto;
	}
	/**
	 * @return the precioSinIvaProductoKg
	 */
	public Float getPrecioSinIvaProductoKg() {
		return precioSinIvaProductoKg;
	}
	/**
	 * @param precioSinIvaProductoKg the precioSinIvaProductoKg to set
	 */
	public void setPrecioSinIvaProductoKg(Float precioSinIvaProductoKg) {
		this.precioSinIvaProductoKg = precioSinIvaProductoKg;
	}
	/**
	 * @return the precioConIvaProductoKg
	 */
	public Float getPrecioConIvaProductoKg() {
		return precioConIvaProductoKg;
	}
	/**
	 * @param precioConIvaProductoKg the precioConIvaProductoKg to set
	 */
	public void setPrecioConIvaProductoKg(Float precioConIvaProductoKg) {
		this.precioConIvaProductoKg = precioConIvaProductoKg;
	}
	/**
	 * @return the cuotaIva
	 */
	public Float getCuotaIva() {
		return cuotaIva;
	}
	/**
	 * @param cuotaIva the cuotaIva to set
	 */
	public void setCuotaIva(Float cuotaIva) {
		this.cuotaIva = cuotaIva;
	}	
}
