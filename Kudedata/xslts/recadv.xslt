<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<body style="height: 82px; ">
<center style="height: 63px; ">
<h1>Confirmación de entrega</h1>
</center>
	<table style="width: 848px;" border="1" align="center">
		<tr>
			<td style="width: 162px;">Identificativo de aviso:</td>
			<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='BGM']/element[@Id='BGM02']" /></td>
			<!-- <td>REC5488*</td> -->
			<td style="width: 161px;">Número de orden:</td>
			<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='RFF'][2]/element[@Id='RFF01']/subelement[@Sequence='2']" /></td>
			<!-- <td>PO12345*</td> -->
		</tr>
		<tr style="width: 334px;">
			<td>Identificativo del comprador:</td>
			<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='NAD']/element[@Id='NAD02']/subelement[@Sequence='1']" /></td>
			<!-- <td>5412345000013*</td> -->
			<td>VAT del comprador:</td>
			<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='RFF'][3]/element[@Id='RFF01']/subelement[@Sequence='2']" /></td>
			<!-- <td>1452216*</td> -->
		</tr>
		<tr>
		<xsl:variable name="fechaMensaje"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][1]/element[@Id='DTM01']/subelement[@Sequence='2']" />
		<xsl:variable name="fechaRecibo"
				select="ediroot/interchange/group/transaction/segment[@Id='DTM'][2]/element[@Id='DTM01']/subelement[@Sequence='2']" />
			<td style="width: 162px;">Fecha del mensaje:</td>
			<!-- <td>20020311*</td> -->
			<td><xsl:value-of
						select="concat(
                      substring($fechaMensaje, 7, 2),
                      '/',
                      substring($fechaMensaje, 5, 2),
                      '/',
                      substring($fechaMensaje, 1, 4)
                      )" /></td>
			<td>Fecha de recibo de mercancía:</td>
			<td><xsl:value-of
						select="concat(
                      substring($fechaRecibo, 7, 2),
                      '/',
                      substring($fechaRecibo, 5, 2),
                      '/',
                      substring($fechaRecibo, 1, 4)
                      )" /></td>
			<!-- <td>20020225*</td> -->
		</tr>		
		<tr>
			<td>Identificativo del suministrador:</td>
			<!-- <td>5410738100005*</td> -->
			<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='NAD'][2]/element[@Id='NAD02']/subelement[@Sequence='1']" />
				</td>
			<td>VAT del suministrador:</td>
			<!-- <td>5448776*</td> -->
			<td><xsl:value-of
						select="ediroot/interchange/group/transaction/segment[@Id='RFF'][4]/element[@Id='RFF01']/subelement[@Sequence='2']" />
				</td>
		</tr>
	</table>	
</body>
</html>
