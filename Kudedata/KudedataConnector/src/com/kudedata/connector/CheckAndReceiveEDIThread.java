package com.kudedata.connector;

/**
 * @author 106800
 * llama al servicio ofrecido por el middleware kudedata para comprobar si hay mensajes EDI aprobados pendientes de recibir y si es así los rebibe y almacena
 * en una carpeta para su posterior procesamiento
 */
public class CheckAndReceiveEDIThread implements Runnable{
	
	@Override
    public void run() {
        while(true){
        	checkAndSenMessage();
        }
    }

	private void checkAndSenMessage() {
		
		
	}

}
