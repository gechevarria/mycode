package reusability;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ReusabilityHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ReusabilityHandler() {
		Log.writeLog("ReusabilityHandler()");
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {	
		NewReusabilityWizardWindow applicatioWindow = new NewReusabilityWizardWindow();	
		applicatioWindow.open();		
		return null;
	}
}
