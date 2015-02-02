<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<body style="height: 82px; ">

		<center style="height: 63px; ">
			<h1>Orden</h1>
		</center>
		<table style="width: 848px; " border="1" align="center">
			<tr>
				<td style="width: 162px; ">Número de orden:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/segment/element[@Id='BGM02']" />
				</td>
				<td style="width: 161px; ">Número de contrato:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/segment/element[@Id='PAI08']/subelement[@Sequence='2']" />
				</td>
			</tr>
			<tr style="width: 334px; ">
				<td>Identificativo del comprador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02']/segment/element[@Id='NAD02']/subelement[@Sequence='1'][1]" />
				</td>
				<td>VAT del comprador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02']/loop[@Id='SG03']/segment[@Id='RFF']/element[@Id='RFF01']/subelement[@Sequence='2']" />
				</td>
			</tr>
			<xsl:variable name="fechaContrato"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][2]/element[@Id='DTM01']/subelement[@Sequence='2']" />
			<xsl:variable name="fechaMensaje"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][1]/element[@Id='DTM01']/subelement[@Sequence='2']" />
			<tr>
				<td style="width: 162px; ">Fecha de contrato:</td>
				<td>
					<xsl:value-of
						select="concat(
                      substring($fechaContrato, 7, 2),
                      '/',
                      substring($fechaContrato, 5, 2),
                      '/',
                      substring($fechaContrato, 1, 4)
                      )" />
				</td>
				<td>Fecha del mensaje:</td>
				<td>
					<xsl:value-of
						select="concat(
                      substring($fechaMensaje, 7, 2),
                      '/',
                      substring($fechaMensaje, 5, 2),
                      '/',
                      substring($fechaMensaje, 1, 4)
                      )" />
				</td>
			</tr>

			<tr>
				<td>Contacto para la orden:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02']/loop[@Id='SG05']/segment/element[@Id='CTA02']/subelement[@Sequence='2']" />
				</td>
				<td>Número de teléfono del contacto:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02']/loop[@Id='SG05']/segment/element[@Id='COM01']/subelement[@Sequence='1']" />
				</td>
			</tr>
			<tr>
				<td>Identificativo del comprador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02']/segment[@Id='NAD']/element[@Id='NAD02']/subelement[@Sequence='1']" />
				</td>
				<td>Instrucciones de pago:</td>
				<td style="width: 159px; ">
					<xsl:value-of
						select="ediroot/interchange/group/transaction/segment/element[@Id='PAI01']/subelement[@Sequence='3']" />
				</td>
			</tr>
			<tr>
				<td>Identificativo del suministrador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02'][2]/segment[@Id='NAD']/element[@Id='NAD02']/subelement[@Sequence='1']" />
				</td>
				<td>VAT del suministrador:</td>
				<td>
					<xsl:value-of
						select="ediroot/interchange/group/transaction/loop[@Id='SG02'][2]/loop[@Id='SG03']/segment/element[@Id='RFF01']/subelement[@Sequence='2']" />
				</td>
			</tr>
		</table>
		<br></br>
			<h1>Detalle de la orden:</h1>
			<table>
				<tr style="font-weight: bold;">
					<td style="width: 112px; ">Id. Producto</td>
					<td style="width: 116px; ">Tipo producto</td>
					<td style="width: 81px; ">Cantidad</td>
					<td style="width: 92px; ">Precio Neto</td>
					<td style="width: 37px; ">IVA</td>
					<td style="width: 161px; ">Fecha envío solicitada</td>
				</tr>
				<xsl:for-each select="ediroot/interchange/group/transaction/loop[@Id='SG28']">
				<xsl:variable name="fechaEnvioSolicitada"
				select="loop[@Id='SG37']/segment[@Id='DTM']/element[@Id='DTM01']/subelement[@Sequence='2']" />
				<tr>
					<td><xsl:value-of
						select="segment[@Id='LIN']/element[@Id='LIN03']/subelement[@Sequence='1']" /></td><!-- Id. Producto -->
					<td><xsl:value-of
						select="segment[@Id='IMD']/element[@Id='IMD03']/subelement[@Sequence='1']" /></td><!-- Tipo producto -->
					<td><xsl:value-of
						select="segment[@Id='QTY']/element[@Id='QTY01']/subelement[@Sequence='2']" /></td><!-- Cantidad -->
					<td><xsl:value-of
						select="loop[@Id='SG32']/segment[@Id='PRI']/element[@Id='PRI01']/subelement[@Sequence='2']" /></td><!-- Precio Neto -->
					<td><xsl:value-of
						select="loop[@Id='SG38']/segment[@Id='TAX']/element[@Id='TAX05']/subelement[@Sequence='4']" /></td><!-- IVA -->
					<td><xsl:value-of
						select="concat(
                      substring($fechaEnvioSolicitada, 7, 2),
                      '/',
                      substring($fechaEnvioSolicitada, 5, 2),
                      '/',
                      substring($fechaEnvioSolicitada, 1, 4)
                      )" /></td><!-- Fecha envío solicitada -->
				</tr>
				</xsl:for-each>
			</table>
	</body>
</html>