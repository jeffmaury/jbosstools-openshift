package org.jboss.tools.openshift.osio.internal.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.openshift.osio.internal.ui.processor.DefaultRequestProcessor;
import org.jboss.tools.openshift.osio.internal.ui.processor.RequestInfo;
import org.jboss.tools.openshift.osio.internal.ui.processor.RequestProcessor;

public class LoginDialog extends Dialog {

	private String url;
	private Browser browser;
	private RequestInfo info;
	
	private static final RequestProcessor processor = new DefaultRequestProcessor();

	public LoginDialog(Shell parentShell, String url) {
		super(parentShell);
		this.url = url;
	}

	public LoginDialog(IShellProvider parentShell, String url) {
		super(parentShell);
		this.url = url;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Close", true);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Point getInitialSize() {
		Shell parent = getParentShell();
		return new Point(parent.getSize().x /2, parent.getSize().y /2);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(container);

		browser = new Browser(container, SWT.BORDER);
		browser.setText("Loading");
		Browser.clearSessions();
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(browser);

		final ProgressBar progressBar = new ProgressBar(container, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).applyTo(progressBar);

		ProgressListener progressListener = new ProgressListener() {
			@Override
			public void changed(ProgressEvent event) {
				if (event.total <= 0)
					return;
				int ratio = event.current * 100 / event.total;
				progressBar.setSelection(ratio);
			}

			@Override
			public void completed(ProgressEvent event) {
				progressBar.setSelection(0);
				System.out.println("URL=" + browser.getUrl());
				info = processor.getRequestInfo(browser.getUrl(), browser.getText());
				if (null != info) {
					close();
				}
			}
		};
		browser.addProgressListener(progressListener);
		setURL(url);
		return container;
	}

	public void setURL(String url) {
		this.url = url;
		browser.setUrl(url);
	}


}
