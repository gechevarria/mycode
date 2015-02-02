<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<body style="height: 82px; ">
<center style="height: 63px; ">
<h1>Albarán</h1>
</center>
<table style="width: 848px; " border="1" align="center">
<tr>
<td style="width: 162px; ">Número de orden:</td>
<td><xsl:value-of
select="ediroot/interchange/group/transaction/segment[@Id='RFF']/element/subelement[@Sequence='2']" /></td>
<xsl:variable name="fechaOrden"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][4]/element[@Id='DTM01']/subelement[@Sequence='2']" />
<td style="width: 161px; ">Fecha de la orden:</td>
<td><xsl:value-of
						select="concat(
                      substring($fechaOrden, 7, 2),
                      '/',
                      substring($fechaOrden, 5, 2),
                      '/',
                      substring($fechaOrden, 1, 4)
                      )" />
</td>
</tr>
<tr style="width: 334px; ">
<td>Identificativo del suministrador:</td>
<td><xsl:value-of
						select="ediroot/interchange/group//segment[@Id='NAD']/element[@Id='NAD02']/subelement[@Sequence='1']" /></td>					
<td>VAT del suministrador:</td>
<td><xsl:value-of
						select="ediroot/interchange/group//segment[@Id='RFF'][2]/element[@Id='RFF01']/subelement[@Sequence='2']" /></td>						
</tr>
<tr>
</tr><tr></tr><tr></tr><tr>
</tr>
</table>
<h1>Detalle de la orden:</h1>
<table>
<tr>
<td><table>
<tr style="font-weight: bold;">
<td style="width: 112px; ">Id. Producto</td>
</tr>

<xsl:for-each select="ediroot/interchange/group/transaction/segment[@Id='LIN']">				
				<tr>
					<td><xsl:value-of
						select="element[@Id='LIN03']/subelement[@Sequence='1']" />
					</td>									
				</tr>
				</xsl:for-each>
			
</table>
</td>
<td>
<table>
<tr style="font-weight: bold;">
<td style="width: 112px; ">Id. Producto</td>
</tr>

<xsl:for-each select="ediroot/interchange/group/transaction/segment[@Id='QTY']">				
				<tr>					
					<td><xsl:value-of
						select="element[@Id='QTY01']/subelement[@Sequence='2']" /></td>					
				</tr>
				</xsl:for-each>
			
</table>
</td>
</tr>
</table>
</body>
</html>
