KUDEDATA contiene 4 proyectos:

KudedataConnector
A desplegar en las m�quinas en las que est�n los ERPS.
Lee continuamente ficheros EDI y los env�a al middleware para ser procesados cifrado sobre https.
Tambi�n comprueba periodicamente si hay mensajes para la empresa en curso mediante la invocaci�n a un servicio rest.


KudedataMiddleware
A desplegar en la nube.
Servicio que recibe ficheros EDI y los almacena para ser procesados.


XSLTransformer
En la nube.
Transforma el fichero EDI a XML y despu�s aplica XSLT generanco HTML leible por el usuario.
Los ficheros html ser�n los que se carguen en Alfresco y los usuarios podr�n leer y aprobar.

alfrescoConnector
sube los ficheros html generados al alfresco community trial version 5.0 via la api rest que ofrece.

