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