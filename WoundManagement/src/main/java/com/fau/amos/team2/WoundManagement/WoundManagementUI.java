package com.fau.amos.team2.WoundManagement;

/*
import javax.naming.Context;
import javax.naming.InitialContext;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;*/

import java.util.Locale;

import com.fau.amos.team2.WoundManagement.model.Employee;
import com.fau.amos.team2.WoundManagement.model.Patient;
import com.fau.amos.team2.WoundManagement.model.Ward;
import com.fau.amos.team2.WoundManagement.model.Wound;
import com.fau.amos.team2.WoundManagement.model.WoundLevel;
import com.fau.amos.team2.WoundManagement.model.WoundType;
import com.fau.amos.team2.WoundManagement.provider.EmployeeProvider;
import com.fau.amos.team2.WoundManagement.provider.Environment;
import com.fau.amos.team2.WoundManagement.provider.PatientProvider;
import com.fau.amos.team2.WoundManagement.provider.WardProvider;
import com.fau.amos.team2.WoundManagement.provider.WoundLevelProvider;
import com.fau.amos.team2.WoundManagement.provider.WoundProvider;
import com.fau.amos.team2.WoundManagement.provider.WoundTypeProvider;
import com.fau.amos.team2.WoundManagement.provider.exceptions.DuplicateEmployeeException;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The UI's "main" class
 * 
 * @author ???
 */
@SuppressWarnings("serial")
@Widgetset("com.fau.amos.team2.WoundManagement.gwt.AppWidgetSet")
@Theme("touchkit")
public class WoundManagementUI extends UI {

	private static EmployeeProvider employeeProvider = 
			EmployeeProvider.getInstance();
	private static PatientProvider patientProvider = 
			PatientProvider.getInstance();
	private static WoundProvider<Wound> woundProvider = 
			(WoundProvider<Wound>) WoundProvider.getInstance();
	private static WoundTypeProvider<WoundType> woundTypeProvider = 
			(WoundTypeProvider<WoundType>) WoundTypeProvider.getInstance();
	private static WoundLevelProvider<WoundLevel> woundLevelProvider = 
			(WoundLevelProvider<WoundLevel>) WoundLevelProvider.getInstance();
	
		private static WoundType testWoundType1 = new WoundType();
	private static WoundLevel testWoundLevel1 = new WoundLevel();
	private static Patient testPatient1 = new Patient();
	private static Wound testWound1 = new Wound();
	
	private static boolean isInitialized = false;
	
	static void initData() {
		// curiosly breaks the connection
		//if(isInitialized)
		//	return;
		//isInitialized = true;
		
		for (int i = 0; i < 3; i++) {
			Ward ward = new Ward();
			ward.setCharacterisation("Station " + (i+1));
			WardProvider.getInstance().add(ward);
			
			Employee testUser = new Employee();
			testUser.setWorkingWard(ward);

			switch (i) {
				case 0:
					testUser.setFirstName("Adam");
					testUser.setLastName("Arbeit");
					testUser.setAbbreviation("testuser1");
					testUser.setQualificationNumber(1111);
					break;
					
				case 1:
					testUser.setFirstName("Bernd");
					testUser.setLastName("Bond");
					testUser.setAbbreviation("testuser2");
					testUser.setQualificationNumber(2222);
					testUser.setWorkingWard(ward);
					break;
					
				case 2:
					testUser.setFirstName("Christina");
					testUser.setLastName("Charles");
					testUser.setAbbreviation("testuser3");
					testUser.setQualificationNumber(3333);
					break;
			}
			
			EmployeeProvider.getInstance().add(testUser);
		}
		
		Employee firstEmployee = employeeProvider.getAllItems().iterator().next();
		
		testWoundType1.setBodyLocationRequired(false);
		testWoundType1.setClassification("Senso6 Dekubitus");
		testWoundType1.setLevel('p');
		testWoundType1.setType('d');
		testWoundType1.setSizeIsRequired(true);
		testWoundType1.setBodyLocationRequired(true);
		
		woundTypeProvider.add(testWoundType1);
		
		testWoundLevel1.setAbbreviation("1");
		testWoundLevel1.setCharacterisation("Grad 1 - Nicht wegdrückbare Rötung");
		testWoundLevel1.setDescription("Nicht wegdrückbare Rötung");
		testWoundLevel1.setLevel(1);
		testWoundLevel1.setWoundType(testWoundType1);
		
		woundLevelProvider.add(testWoundLevel1);
		
		testPatient1.setFirstName("Doerte");
		testPatient1.setLastName("Daeumler");
		testPatient1.setSensoID(1);
		testPatient1.setAccomodation('c');
		testPatient1.setBirthday(java.sql.Date.valueOf("1956-03-12"));
		testPatient1.setEntryDate(java.sql.Date.valueOf("2014-04-11"));
		testPatient1.setGender("f");
		testPatient1.setKeyword("keyword");
		testPatient1.setRoom("room");
		testPatient1.setTitle("Dr.");
		
		patientProvider.add(testPatient1);
		
		testWound1.setBodyLocation("Brustbein");
		testWound1.setBodyLocationCode(64);
		testWound1.setCureEmployee(firstEmployee);
		testWound1.setDecubitusId(10);
		testWound1.setDepth(3);
		testWound1.setDescription("Ich bin eine Bemerkung. Ich bin eine Bemerkung. Ich bin eine Bemerkung. Ich bin eine Bemerkung. Ich bin eine Bemerkung. Ich bin eine Bemerkung. Ich bin eine Bemerkung. ");
		testWound1.setEndDate(java.sql.Date.valueOf("2014-05-12"));
		testWound1.setOrigination(1);
		testWound1.setRecordingDate(java.sql.Date.valueOf("2014-04-12"));
		testWound1.setRecordingEmployee(firstEmployee);
		testWound1.setSize1(1);
		testWound1.setSize2(2);
		testWound1.setWoundType(testWoundType1);
		testWound1.setWoundLevel(testWoundLevel1);
		testWound1.setPatient(testPatient1);
		
		woundProvider.add(testWound1);
		
		testPatient1.getWounds().add(testWound1);
	}
	// END INIT //

	@Override
	protected void init(VaadinRequest request) {	
		Locale currentLocale;
		currentLocale = Locale.GERMAN;
		//currentLocale = Locale.ENGLISH;

		MessageResources.setLocale(currentLocale);

		NavigationManager manager = new NavigationManager();

		if (Environment.INSTANCE.getCurrentEmployee() != null) {
			manager.setCurrentComponent(new LoggedInView());
		}
		else {
			manager.setCurrentComponent(new StartMenuView());
		}
		setContent(manager);
		getPage().setTitle("Wound Management");
	}
}
