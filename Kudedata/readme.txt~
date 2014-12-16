KUDEDATA contiene dos proyectos:

KudedataConnector
A desplegar en las máquinas en las que están los ERPS.
Lee continuamente ficheros EDI y los envía al middleware para ser procesados (encriptados sobre https)

KudedataMiddleware
A desplegar en la nube.
Servicio que recibe ficheros EDI y los almacena para ser procesados.
A partir de aquí habrá dos posibilidades:
	- Que las transacciones EDI se ejecuten directamente
	- Que las transacciones EDI tengan que ser validadas a través de la extranet.

XSLTransformer
En la nube.
Transforma el fichero EDI a XML y después aplica XSLT generanco HTML leible por el usuario.
Los ficheros html serán los que se carguen en Alfresco y los usuarios podrán leer y aprobar.