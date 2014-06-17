package com.fau.amos.team2.WoundManagement.ui;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fau.amos.team2.WoundManagement.model.Patient;
import com.fau.amos.team2.WoundManagement.model.Ward;
import com.fau.amos.team2.WoundManagement.provider.PatientProvider;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.fau.amos.team2.WoundManagement.ui.UserWardView.WardChangeEvent;
import com.fau.amos.team2.WoundManagement.ui.UserWardView.WardChangeListener;
import com.fau.amos.team2.WoundManagement.ui.subviews.UserBar;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
//added import Ward
import com.vaadin.ui.Table.Align;

@SuppressWarnings("serial")
public class PatientSelectionView extends SessionedNavigationView implements WardChangeListener{
	
	private static PatientProvider patientProvider = PatientProvider.getInstance();
	
	private List<Patient> allPatients;
	private List<Patient> patientsOfWard;
	private List<Patient> patientsForTable;
	private Table table;
	private Container container;
	private Property[][] propertiesOfWard;
	private Property[][] allProperties;
	private Property[][] propertiesForTable;
	
	private OptionGroup optionGroup;

	public PatientSelectionView() 
	{
		
		// ResizeListener
		UI.getCurrent().setImmediate(true);
		UI.getCurrent().setResizeLazy(true);
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void browserWindowResized(BrowserWindowResizeEvent event) {
				getEnvironment().setOrientation();
				UI.getCurrent().requestRepaint();
				//Page.getCurrent().reload();
			}
		});
		
		setCaption(MessageResources.getString("patientSelection"));
		
		Ward currentWard = getEnvironment().getCurrentEmployee().getCurrentWard();
		
		setRightComponent(new UserBar(this));
		
		VerticalComponentGroup verticalGroup = new VerticalComponentGroup();
		
		optionGroup = new OptionGroup(MessageResources.getString("pleaseChoose") + ":"); //$NON-NLS-1$
		optionGroup.addItem("patientsOfWard");
		optionGroup.setItemCaption("patientsOfWard", MessageResources.getString("patientsOfWard"));
		optionGroup.addItem("allPatients");
		optionGroup.setItemCaption("allPatients", MessageResources.getString("allPatients"));
		optionGroup.setImmediate(true);
		optionGroup.setValue("patientsOfWard");
		optionGroup.setMultiSelect(false);
		optionGroup.setNullSelectionAllowed(false);
		
		Panel tablePanel = new Panel();
		tablePanel.setSizeUndefined();
		tablePanel.setImmediate(true);
		
		table = new Table() {
		    @Override
		    protected String formatPropertyValue(Object rowId,
		            Object colId, Property property) {
		        if (property.getType() == Date.class) {
		            SimpleDateFormat df =
		                new SimpleDateFormat("dd.MM.yyyy");
		            if (property.getValue() != null){
			            return df.format((Date)property.getValue());
		            }
		            return "";
		        }
		        return super.formatPropertyValue(rowId, colId, property);
		    }
		};
		
		table.setSelectable(true);
		table.setImmediate(true);
		
		allPatients = patientProvider.getAllItems();
		allProperties = new Property[allPatients.size()][5];
		patientsOfWard = patientProvider.getPatientsOfWard(currentWard);
		propertiesOfWard = new Property[patientsOfWard.size()][5];
		
		patientsForTable = patientsOfWard;
		propertiesForTable = propertiesOfWard;
		
		table.addContainerProperty("name", String.class , null, MessageResources.getString("name"), null , null);
		table.addContainerProperty("birthdate", Date.class, null, MessageResources.getString("birthdate"), null, null);
		table.addContainerProperty("ward", String.class, null, MessageResources.getString("ward"), null, null);
		table.addContainerProperty("room", String.class, null, MessageResources.getString("room"), null, null);
		//table.addContainerProperty("currentWounds", Integer.class, 0, MessageResources.getString("currentWounds"), null, Align.RIGHT);
		table.addContainerProperty("currentWounds", Integer.class, 0, MessageResources.getString("wounds"), null, Align.RIGHT);
		
		if(getEnvironment().isHorizontalLayout()){
			
			table.setColumnWidth("name", 250);
			table.setColumnWidth("birthdate", 170);
			table.setColumnWidth("room", 100);
			table.setColumnWidth("ward", 100);
			table.setColumnWidth("currentWounds", 60);
			
		}else{
			
			float width = getEnvironment().getWindowWidth();
			
			int widthName = (int) (width * 0.35); 
			int widthBday = (int) (width * 0.15); 
			int widthOther = (int) (width * 0.1); 
			int widthWoundN = (int) (width * 0.08); 
			
			table.setColumnWidth("name", widthName);
			table.setColumnWidth("birthdate", widthBday);
			table.setColumnWidth("room", widthOther);
			table.setColumnWidth("ward", widthOther);
			table.setColumnWidth("currentWounds", widthWoundN);
			
		}
		
		container = table.getContainerDataSource();
		
		fillTable(); 
		
		tablePanel.setContent(table);
		tablePanel.getContent().setSizeUndefined();
		
		table.addValueChangeListener(new Property.ValueChangeListener() {
		    public void valueChange(ValueChangeEvent event) {
		    	Object value = table.getValue();
		    	if (value != null){
		    		Patient patient = patientProvider.getByID(value);
		    		NavigationView next = new PatientView(patient);
		    		getNavigationManager().navigateTo(next);
		    	}
		    }

		});
		
		optionGroup.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (optionGroup.getValue().equals("allPatients")){
					patientsForTable = allPatients;
					propertiesForTable = allProperties;
				} else {
					patientsForTable = patientsOfWard;
					propertiesForTable = propertiesOfWard;
				}
				
				container.removeAllItems();
				
				fillTable();
			}
			
		});
			
		verticalGroup.addComponent(optionGroup);
		verticalGroup.addComponent(tablePanel);
			
		setContent(verticalGroup);
		
	}



	@Override
	public void wardChanged(WardChangeEvent event) {
		if (event.getWard() != null){
			patientsOfWard = patientProvider.getPatientsOfWard(event.getWard());
			propertiesOfWard = new Property[patientsOfWard.size()][5];
			
			if (optionGroup.getValue().equals("allPatients")){
				patientsForTable = allPatients;
				propertiesForTable = allProperties;
			} else {
				patientsForTable = patientsOfWard;
				propertiesForTable = propertiesOfWard;
			}
			
			container.removeAllItems();
			
			fillTable();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void fillTable(){
		for (Patient p : patientsForTable){
			table.addItem(p.getId());
			Item item = table.getItem(p.getId());
			propertiesForTable[patientsForTable.indexOf(p)][0] = item.getItemProperty("name");
			propertiesForTable[patientsForTable.indexOf(p)][0].setValue(p.getFirstName() + " " + p.getLastName());
			propertiesForTable[patientsForTable.indexOf(p)][1] = item.getItemProperty("birthdate");
			propertiesForTable[patientsForTable.indexOf(p)][1].setValue(p.getBirthday());
			propertiesForTable[patientsForTable.indexOf(p)][2] = item.getItemProperty("ward");
			propertiesForTable[patientsForTable.indexOf(p)][2].setValue(p.getWard().getCharacterisation());
			propertiesForTable[patientsForTable.indexOf(p)][3] = item.getItemProperty("room");
			propertiesForTable[patientsForTable.indexOf(p)][3].setValue(p.getRoom());
			propertiesForTable[patientsForTable.indexOf(p)][4] = item.getItemProperty("currentWounds");
			propertiesForTable[patientsForTable.indexOf(p)][4].setValue(p.getCurrentWounds().size());
			container.addItem(p.getId());
		}
	}

}

