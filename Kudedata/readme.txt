KUDEDATA contiene dos proyectos:

KudedataConnector
A desplegar en las m�quinas en las que est�n los ERPS.
Lee continuamente ficheros EDI y los env�a al middleware para ser procesados (encriptados sobre https)

KudedataMiddleware
A desplegar en la nube.
Servicio que recibe ficheros EDI y los almacena para ser procesados.
A partir de aqu� habr� dos posibilidades:
	- Que las transacciones EDI se ejecuten directamente
	- Que las transacciones EDI tengan que ser validadas a trav�s de la extranet.