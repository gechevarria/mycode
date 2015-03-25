/**
 * @author G.M Echevarria
 * 
 */
package com.tickloud.beans;

import java.sql.Date;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "facturasimplificada")
public class FacturaSimplificada {
	private String emisor;
	private Long numeroFactura;
	private Long numeroSerie;
	private String fechaHoraFactura;		
	private String idCliente;
	private Float importeTotalSinIVA;
	private Float importeTotalConIVA;
	private Float cuotaIVA;
	private Long codigoBarras;
	private Integer numeroProductos;		
	private ArrayList<Producto> arrayProductos = new ArrayList<Producto>();
	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}
	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	/**
	 * @return the numeroFactura
	 */
	public Long getNumeroFactura() {
		return numeroFactura;
	}
	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(Long numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	/**
	 * @return the numeroSerie
	 */
	public Long getNumeroSerie() {
		return numeroSerie;
	}
	/**
	 * @param numeroSerie the numeroSerie to set
	 */
	public void setNumeroSerie(Long numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	/**
	 * @return the fechaHoraFactura
	 */
	public String getFechaHoraFactura() {
		return fechaHoraFactura;
	}
	/**
	 * @param fechaHoraFactura the fechaHoraFactura to set
	 */
	public void setFechaHoraFactura(String fechaHoraFactura) {
		this.fechaHoraFactura = fechaHoraFactura;
	}
	/**
	 * @return the idCliente
	 */
	public String getIdCliente() {
		return idCliente;
	}
	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	/**
	 * @return the importeTotalSinIVA
	 */
	public Float getImporteTotalSinIVA() {
		return importeTotalSinIVA;
	}
	/**
	 * @param importeTotalSinIVA the importeTotalSinIVA to set
	 */
	public void setImporteTotalSinIVA(Float importeTotalSinIVA) {
		this.importeTotalSinIVA = importeTotalSinIVA;
	}
	/**
	 * @return the importeTotalConIVA
	 */
	public Float getImporteTotalConIVA() {
		return importeTotalConIVA;
	}
	/**
	 * @param importeTotalConIVA the importeTotalConIVA to set
	 */
	public void setImporteTotalConIVA(Float importeTotalConIVA) {
		this.importeTotalConIVA = importeTotalConIVA;
	}
	/**
	 * @return the cuotaIVA
	 */
	public Float getCuotaIVA() {
		return cuotaIVA;
	}
	/**
	 * @param cuotaIVA the cuotaIVA to set
	 */
	public void setCuotaIVA(Float cuotaIVA) {
		this.cuotaIVA = cuotaIVA;
	}
	/**
	 * @return the codigoBarras
	 */
	public Long getCodigoBarras() {
		return codigoBarras;
	}
	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(Long codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	/**
	 * @return the numeroProductos
	 */
	public Integer getNumeroProductos() {
		return numeroProductos;
	}
	/**
	 * @param numeroProductos the numeroProductos to set
	 */
	public void setNumeroProductos(Integer numeroProductos) {
		this.numeroProductos = numeroProductos;
	}
	/**
	 * @return the arrayProductos
	 */
	public ArrayList<Producto> getArrayProductos() {
		return arrayProductos;
	}
	/**
	 * @param arrayProductos the arrayProductos to set
	 */
	public void setArrayProductos(ArrayList<Producto> arrayProductos) {
		this.arrayProductos = arrayProductos;
	}	
		
	
	
}
