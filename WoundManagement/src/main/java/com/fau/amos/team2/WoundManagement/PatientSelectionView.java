package com.fau.amos.team2.WoundManagement;

import java.util.ResourceBundle;

import com.fau.amos.team2.WoundManagement.model.Employee;
//added import Ward
import com.fau.amos.team2.WoundManagement.model.Ward;
import com.fau.amos.team2.WoundManagement.provider.EmployeeProvider;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Label.ValueChangeEvent;

/**
 * View to see patients of selected ward
 * @author Eugen
 * @param <event>
 */
@SuppressWarnings("serial")
public class PatientSelectionView extends NavigationView {
	
	public PatientSelectionView(final ResourceBundle messages) 
	{
		
		setCaption(messages.getString("patientSelection"));
		
		CssLayout content = new CssLayout();
		
		NativeSelect wpview = new NativeSelect (messages.getString("pleaseSelectPatient") + ": ");
		
		/**
		 * the actual db-based way to go:
		 * wpview.addItem(patients.get(ward.id) == ward.get(event.value));
		*/
		
		//creates six "patients"
		for (int i=0; i<6; i++)
		{
			wpview.addItem(i);
			wpview.setItemCaption(i, messages.getString("patient") + " " +i);
		}
			
		//a selection must occur... 
		wpview.setNullSelectionAllowed(false);
		//...therefore legal to set '-1' by default
		wpview.setValue(-1);
		wpview.setImmediate(true);
		
		wpview.addValueChangeListener(new ValueChangeListener() 
		{
            public void valueChange(final ValueChangeEvent event) 
            {
                final String valueString = String.valueOf(event.getProperty().getValue());
            }

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) 
			{
				getNavigationManager().navigateTo(new WardPatientView(messages, event.getProperty().getValue()));
			}
        });
		
		VerticalComponentGroup box = new VerticalComponentGroup();
		box.addComponent( new Label(messages.getString("patients") + ": ") );
		box.addComponent( wpview );
		
		NavigationButton allPatientsButton = new NavigationButton(messages.getString("allPatients"));
		allPatientsButton.addClickListener(new NavigationButtonClickListener()
		{
			@Override
			public void buttonClick(NavigationButtonClickEvent event) 
			{
				getNavigationManager().navigateTo(new PatientSelectionView(messages));
			}
		});
		box.addComponent(allPatientsButton);
		
		content.addComponent(box);
		setContent(content);
		
	}

	//Standard PatientView
//	public PatientSelectionView()
//	{
//		CssLayout content = new CssLayout();
//
//		setCaption("Patient information");
//		
//		Employee e = EmployeeProvider.getInstance().getByFirstName("Adam");
//		
//		VerticalComponentGroup box = new VerticalComponentGroup();
//		box.addComponent(new Label("Patient: "+ e.getFirstName() + " " + e.getLastName()));
//		box.addComponent(new Label("username: "+ e.getAbbreviation()));
//		
//		content.addComponent(box);
//		setContent(content);
//	}

}

