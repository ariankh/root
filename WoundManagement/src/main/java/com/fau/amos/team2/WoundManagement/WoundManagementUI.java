package com.fau.amos.team2.WoundManagement;

/*
import javax.naming.Context;
import javax.naming.InitialContext;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;*/

import java.util.Iterator;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

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
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * The UI's "main" class
 * 
 * @author ???
 */
@SuppressWarnings("serial")
@Widgetset("com.fau.amos.team2.WoundManagement.gwt.AppWidgetSet")
@Theme("touchkit")
public class WoundManagementUI extends UI {

	private static EmployeeProvider employeeProvider;
	private static PatientProvider patientProvider;
	private static WoundProvider woundProvider;
	private static WoundTypeProvider woundTypeProvider;
	private static WoundLevelProvider woundLevelProvider;
	private static WardProvider wardProvider;
	
	private static WoundType testWoundType1;
	private static WoundLevel testWoundLevel1;
	private static Wound testWound1;
	private static Wound testWound2;
	
	static void initData() {
		employeeProvider = EmployeeProvider.getInstance();
		patientProvider = PatientProvider.getInstance();
		woundProvider = WoundProvider.getInstance();
		woundTypeProvider = WoundTypeProvider.getInstance();
		woundLevelProvider = WoundLevelProvider.getInstance();
		wardProvider = WardProvider.getInstance();
		
		testWoundType1 = new WoundType();
		testWoundLevel1 = new WoundLevel();
		testWound1 = new Wound();
		testWound2 = new Wound();
		
		//empty tables
		woundProvider.deleteAll();
		woundLevelProvider.deleteAll();
		woundTypeProvider.deleteAll();
		employeeProvider.deleteAll();
		patientProvider.deleteAll();
		wardProvider.deleteAll();
		
		for (int i = 0; i < 3; i++) {
			Ward ward = new Ward();
			ward.setCharacterisation("Station " + (i+1));
			WardProvider.getInstance().add(ward);
			
			Employee testUser = new Employee();
			testUser.setWorkingWard(ward);
			testUser.setCurrentWard(ward);

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
		
		for (int i = 0; i < 3; i++) {

			
			Patient testPatient = new Patient();
			testPatient.setSensoID(1);
			testPatient.setAccomodation('c');
			testPatient.setKeyword("keyword");
			testPatient.setRoom("room");

			switch (i) {
				case 0:
					testPatient.setFirstName("Doerte");
					testPatient.setLastName("Daeumler");
					testPatient.setBirthday(java.sql.Date.valueOf("1956-03-12"));
					testPatient.setEntryDate(java.sql.Date.valueOf("2014-04-11"));
					testPatient.setGender("f");
					testPatient.setTitle("Dr.");
					break;
					
				case 1:
					testPatient.setFirstName("Egon");
					testPatient.setLastName("Erhardt");
					testPatient.setBirthday(java.sql.Date.valueOf("1957-04-13"));
					testPatient.setEntryDate(java.sql.Date.valueOf("2014-04-12"));
					testPatient.setGender("m");
					testPatient.setTitle("Prof.");
					break;
					
				case 2:
					testPatient.setFirstName("Fritz");
					testPatient.setLastName("Fischer");
					testPatient.setBirthday(java.sql.Date.valueOf("1958-05-14"));
					testPatient.setEntryDate(java.sql.Date.valueOf("2014-04-13"));
					testPatient.setGender("n");
					break;
			}
			
			PatientProvider.getInstance().add(testPatient);
		}
		
		Iterator<Patient> iterator = patientProvider.getAllItems().iterator();
		Patient firstPatient = iterator.next();
		Patient secondPatient = iterator.next();
		
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
		testWound1.setPatient(firstPatient);
		
		woundProvider.add(testWound1);
		
		testWound2.setBodyLocation("linke Wade");
		testWound2.setBodyLocationCode(15);
		//testWound2.setCureEmployee(firstEmployee);
		testWound2.setDecubitusId(11);
		testWound2.setDepth(4);
		testWound2.setDescription("Ich bin eine Bemerkung.");
		//testWound2.setEndDate(java.sql.Date.valueOf("2014-05-22"));
		testWound2.setOrigination(2);
		testWound2.setRecordingDate(java.sql.Date.valueOf("2014-04-22"));
		testWound2.setRecordingEmployee(firstEmployee);
		testWound2.setSize1(2);
		testWound2.setSize2(3);
		testWound2.setWoundType(testWoundType1);
		testWound2.setWoundLevel(testWoundLevel1);
		testWound2.setPatient(secondPatient);
		
		woundProvider.add(testWound2);
		
		//firstPatient.getWounds().add(testWound1);
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
