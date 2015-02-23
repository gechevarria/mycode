<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<body style="height: 82px; ">
		<center style="height: 63px; ">
			<h1>Factura</h1>
		</center>
		<table style="width: 848px;" border="1" align="center">
			<tr>
				<td style="width: 162px;">Identificativo de factura:</td>				
				<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment/element[@Id='BGM02']" /></td>
				<td style="width: 161px;">Número
					de orden:
				</td>
				<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='RFF']/element[@Id='RFF01']/subelement[@Sequence='2']" /></td>				
			</tr>
			<tr style="width: 334px;">
				<td>Identificativo del comprador:</td>				
				<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='NAD']/element[@Id='NAD02']/subelement[@Sequence='1']" /></td>
				<td>VAT del comprador:</td>
				<!-- <td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='RFF']/element[@Id='RFF01']/subelement[@Sequence='2'][1]" /></td> -->
				<xsl:for-each select="ediroot/interchange/group/transaction/segment[@Id='RFF']/element[@Id='RFF01']">
											
				<xsl:if test="(subelement='VA') and (not(position()=last()))">				
				<td><xsl:value-of
						select="subelement[2]"></xsl:value-of></td>
				</xsl:if>
				</xsl:for-each>						
			</tr>
			<xsl:variable name="fechaMensaje"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][1]/element[@Id='DTM01']/subelement[@Sequence='2']" />
			<xsl:variable name="fechaReferencia"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][2]/element[@Id='DTM01']/subelement[@Sequence='2']" />	
			<tr>
				<td style="width: 162px;">Fecha de referencia:</td>
				<td><xsl:value-of
						select="concat(
                      substring($fechaReferencia, 7, 2),
                      '/',
                      substring($fechaReferencia, 5, 2),
                      '/',
                      substring($fechaReferencia, 1, 4)
                      )" /></td>
				<td>Fecha del mensaje:</td>
				<td><xsl:value-of
						select="concat(
                      substring($fechaMensaje, 7, 2),
                      '/',
                      substring($fechaMensaje, 5, 2),
                      '/',
                      substring($fechaReferencia, 1, 4)
                      )" /></td>
			</tr>

			<tr>
				<td>Moneda:</td>
				<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='CUX']/element[@Id='CUX01']/subelement[@Sequence='2']" />
				</td>
				<td>Instrucciones de pago:</td>
				<td>
				<xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='PAI']/element[@Id='PAI01']/subelement[@Sequence='3']" />
				</td>
			</tr>
			<tr>
				<td>Identificativo del suministrador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='NAD'][2]/element[@Id='NAD02']/subelement[@Sequence='1']" />
				</td>
				<td>VAT del suministrador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='RFF'][4]/element[@Id='RFF01']/subelement[@Sequence='2']" />
				</td>
			</tr>		
		</table>
		<br></br>
			<h1>Detalle de la factura:</h1>
			<table>
				<tr style="font-weight: bold;">
					<td style="width: 112px; ">Id. Producto</td>
					<td style="width: 81px; ">Cantidad</td>
					<td style="width: 92px; ">Precio Neto</td>
					<td style="width: 37px; ">IVA</td>
					<td style="width: 161px; ">Total</td>
				</tr>
				<xsl:for-each select="ediroot/interchange/group/transaction/segment[@Id='LIN']">
				<xsl:variable name="cantidad" select="following-sibling::*[1]/element[@Id='QTY01']/subelement[@Sequence='2']"/>
				<xsl:variable name="precioNeto" select="following-sibling::*/element[@Id='MOA01']/subelement[@Sequence='2']"/>	
				<xsl:variable name="tasa" select="following-sibling::*/element[@Id='TAX05']/subelement[@Sequence='4']"/>
				<tr>
					<!-- <td>4000862141404*</td> -->
					<td><xsl:value-of
						select="element[@Id='LIN03']/subelement[@Sequence='1']" />
					</td>								
					<!-- <td><xsl:value-of select="$quantity/element[@Id='QTY01']/subelement[@Sequence='2']"/> -->
					<td><xsl:value-of select="$cantidad"/>					
					</td>
					<td><xsl:value-of select="$precioNeto"/>	
					</td>					
					<td><xsl:value-of select="$tasa"/>
					</td>
					<td><xsl:value-of select="$cantidad * ($precioNeto + ($precioNeto * $tasa div 100))"/>
					</td>					
				</tr>
				</xsl:for-each>
			</table>
	</body>

</html>