KUDEDATA contiene 4 proyectos:

KudedataConnector
A desplegar en las máquinas en las que están los ERPS.
Lee continuamente ficheros EDI y los envía al middleware para ser procesados cifrado sobre https.
También comprueba periodicamente si hay mensajes para la empresa en curso mediante la invocación a un servicio rest.


KudedataMiddleware
A desplegar en la nube.
Servicio que recibe ficheros EDI y los almacena para ser procesados.


XSLTransformer
En la nube.
Transforma el fichero EDI a XML y después aplica XSLT generanco HTML leible por el usuario.
Los ficheros html serán los que se carguen en Alfresco y los usuarios podrán leer y aprobar.

alfrescoConnector
sube los ficheros html generados al alfresco community trial version 5.0 via la api rest que ofrece.

