package eu.artist.migration.mdt.reusability;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CLabel;

public class LoginWindow {

	
	protected Shell shell;
	private String strConfigurationFilePath="";
	private Text text;
	private Text text_1;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LoginWindow window = new LoginWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(319, 225);
		shell.setText("Login");
		
		CLabel lblLabel = new CLabel(shell, SWT.NONE);
		lblLabel.setBounds(47, 28, 61, 21);
		lblLabel.setText("User:");
		
		CLabel lblPassword = new CLabel(shell, SWT.NONE);
		lblPassword.setText("Password:");
		lblPassword.setBounds(47, 73, 61, 21);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(161, 28, 76, 21);
		
		text_1 = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		text_1.setBounds(161, 73, 76, 21);
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.setBounds(47, 126, 75, 25);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setBounds(162, 126, 75, 25);
		btnCancel.setText("Cancel");

	}
}
