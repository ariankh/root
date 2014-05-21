package com.fau.amos.team2.WoundManagement;


import java.util.List;

import com.fau.amos.team2.WoundManagement.model.Employee;
import com.fau.amos.team2.WoundManagement.provider.EmployeeProvider;
import com.fau.amos.team2.WoundManagement.provider.Environment;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class UserLoginView extends NavigationView {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private Button loginButton;
	@AutoGenerated
	private PasswordField passwordField;
	@AutoGenerated
	private TextField usernameField;

	private JPAContainer<Employee> employees;
	
	private static EmployeeProvider employeeProvider = 
			EmployeeProvider.getInstance();

	/**
	 * Creates an instance of LoginView in order to login.
	 * 
	 * If user login correct creates a new View
	 * @see com.fau.amos.team2.WoundManagement.LoggedInView
	 */
	public UserLoginView() {
		HorizontalLayout main = new HorizontalLayout();
		VerticalLayout fieldsandbutton = new VerticalLayout();

		final OptionGroup logingroup = new OptionGroup(MessageResources.getString("pleaseChoose") + ":"); //$NON-NLS-1$
		
		//das aendern der Caption aendert nicht die Item-ID der optiongroup- mode explicit
		logingroup.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		//aenderung innerhalb der logingroup werden direkt mit dem server kommuniziert 
		//keine verzoegerung bei valuechange-events
		logingroup.setImmediate(true);

		usernameField = new TextField();
		usernameField.setValue(""); //$NON-NLS-1$
		usernameField.setCaption(MessageResources.getString("username") + ":"); //$NON-NLS-1$
		
		fieldsandbutton.addComponent(usernameField);

		passwordField = new PasswordField();
		passwordField.setCaption(MessageResources.getString("PIN") + ":"); //$NON-NLS-1$
		passwordField.setValue(""); //$NON-NLS-1$
		
		fieldsandbutton.addComponent(passwordField);

		loginButton = new Button();
		loginButton.setCaption(MessageResources.getString("login")); //$NON-NLS-1$
		loginButton.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				validateData();
			}
		});
		
		fieldsandbutton.addComponent(loginButton);

		// Die Buttons/Felder auf der linken Seite, das fieldsandbutton sind hinzugefuegt
		// jetzt fehlt noch die rechte Seite- optiongroup zum auswaehlen des
		// Nutzers
		// das eintippen der Daten im rechten Menue ist aber auch noch moeglich!

		// Datenbankabfrage, wieviele nutzer gibt es sowie referenz auf deren
		// IDs

		List<Employee> employees = employeeProvider.getAllItems();
		
		// hinzufuegen der Employee items
		for (Employee employee : employees) {

			// fuege jeden employee in die Liste ein, Item-ID = datenbank ID
			// auch das setzten der Caption ueberschreibt diese nicht!
			logingroup.addItem(employee);
			logingroup.setItemCaption(employee, employee.getFirstName() + " " + employee.getLastName()); //$NON-NLS-1$
		}

		// sobald etwas selektiert wird, moechten wir das wissen! //und der setimmediate
		// deshalb valueChangelistener
		//dieser fuegt die entsprechende Abkuerzung in das usernamefield ein

		logingroup.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				Employee employee = (Employee)logingroup.getValue();

				String selectedloginname = employee.getAbbreviation();
				usernameField.setValue(selectedloginname);
				usernameField.setVisible(false);
				passwordField.focus();
			}
		});

		main.addComponent(fieldsandbutton);
		main.addComponent(logingroup);

		setContent(main);
	}

	/**
	 * Validate user input with data in the database
	 * 
	 * @return true if user fits password in the database, false otherwise
	 */
	private void validateData() {
		String username = usernameField.getValue();
		String password = passwordField.getValue();
		
		Environment.INSTANCE.loginEmployee(username, password);
		boolean correctdata = Environment.INSTANCE.getCurrentEmployee() != null;
		
		//Falls korrekte daten:
		if (correctdata) {
			Notification.show(MessageResources.getString("correctData")); //$NON-NLS-1$

			NavigationView next = new LoggedInView();
			getNavigationManager().navigateTo(next);
		} else {
			Notification.show(MessageResources.getString("incorrectData")); //$NON-NLS-1$

			this.passwordField.setValue(""); //$NON-NLS-1$
		}
	}

}