package org.jboss.tools.openshift.io.internal.ui.dialog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.openshift.io.internal.ui.OpenShiftIOUIActivator;
import org.jboss.tools.openshift.io.internal.ui.processor.DefaultRequestProcessor;
import org.jboss.tools.openshift.io.internal.ui.processor.RequestInfo;
import org.jboss.tools.openshift.io.internal.ui.processor.RequestProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HttpLibBasedLoginDialog extends StatusDialog {

	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;

	private String url;

	private Text usernameField;

	private Text passwordField;
	
	private RequestInfo info;

	public HttpLibBasedLoginDialog(Shell parentShell, String url) {
		super(parentShell);
		this.url = url;
		setTitle("Login to OpenShift.io");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);

		GridLayout layout = (GridLayout) comp.getLayout();
		layout.numColumns = 2;

		Label usernameLabel = new Label(comp, SWT.RIGHT);
		usernameLabel.setText("Username: ");

		usernameField = new Text(comp, SWT.SINGLE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		usernameField.setLayoutData(data);

		Label passwordLabel = new Label(comp, SWT.RIGHT);
		passwordLabel.setText("Password: ");

		passwordField = new Text(comp, SWT.SINGLE | SWT.PASSWORD);
		data = new GridData(GridData.FILL_HORIZONTAL);
		passwordField.setLayoutData(data);

		return comp;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		createButton(parent, RESET_ID, "Reset All", false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == RESET_ID) {
			usernameField.setText("");
			passwordField.setText("");
		} else if (buttonId == IDialogConstants.OK_ID) {
			if (performLogin()) {
				super.buttonPressed(buttonId);
			} else {
				updateStatus(new Status(IStatus.ERROR, OpenShiftIOUIActivator.PLUGIN_ID, "Invalid login"));
			}
		} else {
			super.buttonPressed(buttonId);
		}
	}

	private boolean performLogin() {
		Boolean[] done = new Boolean[1];
		done[0] = Boolean.FALSE;
		HttpClientBuilder builder = HttpClients.custom().addInterceptorFirst(new HttpRequestInterceptor() {

			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				System.out.println("Processing request " + request.getRequestLine().getUri());
				RequestProcessor processor = new DefaultRequestProcessor();
				info = processor.getRequestInfo(request.getRequestLine().getUri(), null);
				done[0] = info != null;
			}
		});
		HttpClient client = builder.build();
		HttpGet httpGet = new HttpGet(url);
		;
		try (CloseableHttpResponse response1 = (CloseableHttpResponse) client.execute(httpGet)) {
			HttpEntity entity1 = response1.getEntity();
			String response = EntityUtils.toString(entity1);
			try {
				Document doc = Jsoup.parse(response);
				Element form = doc.getElementById("kc-form-login");
				if (null != form) {
					String action = form.attr("action");
					HttpPost post = new HttpPost(action);
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("username", usernameField.getText()));
					nvps.add(new BasicNameValuePair("password", passwordField.getText()));
					post.setEntity(new UrlEncodedFormEntity(nvps));
					try (CloseableHttpResponse response2 = (CloseableHttpResponse) client.execute(post)) {
						Header header = response2.getFirstHeader("Location");
						httpGet = new HttpGet(header.getValue());
						CloseableHttpResponse response3 = (CloseableHttpResponse) client.execute(httpGet);
					} catch (Exception e) {
					}
				}
			} catch (UnsupportedEncodingException | RuntimeException e) {
			}
		} catch (IOException e) {
		}
		return done[0];
	}

	public RequestInfo getInfo() {
		return info;
	}
}
