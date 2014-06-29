package com.fau.amos.team2.WoundManagement.ui;

import com.fau.amos.team2.WoundManagement.BodyWoundSelector.WoundManager;
import com.fau.amos.team2.WoundManagement.BodyWoundSelector.WoundManager.SelectedWoundChangeEvent;
import com.fau.amos.team2.WoundManagement.BodyWoundSelector.WoundManager.SelectedWoundChangeListener;
import com.fau.amos.team2.WoundManagement.model.Patient;
import com.fau.amos.team2.WoundManagement.model.Wound;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.fau.amos.team2.WoundManagement.ui.subviews.ExistingWound;
import com.fau.amos.team2.WoundManagement.ui.subviews.NewWound;
import com.fau.amos.team2.WoundManagement.ui.subviews.UserBar;
import com.vaadin.addon.responsive.Responsive;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@PreserveOnRefresh
@Theme("wm-responsive")
public class PatientView extends SessionedNavigationView implements SelectedWoundChangeListener {
	private static final long serialVersionUID = -572027045788648039L;
	
	private Patient currentPatient;
	private WoundManager woundManager;
	
	private boolean showCurrentWoundsOnly;
	
	VerticalLayout rightContent = new VerticalLayout();
	VerticalLayout rightContent2;
	
//	public PatientView(Patient patient) {
//		this(patient, true);
//	}
	
	@SuppressWarnings("serial")
	public PatientView() {
		
		getEnvironment().setCurrentWoundDescription(null);
		
		this.currentPatient = getEnvironment().getCurrentPatient();
		this.showCurrentWoundsOnly = getEnvironment().getShowCurrentWoundsOnly();
		
		UserBar userBar = new UserBar(this);
		userBar.addStyleName("userBar");
		userBar.setWidth("100%");		
		
		setCaption(currentPatient.getFirstName() + " " + currentPatient.getLastName());

		final CheckBox showOnlyCurrentWoundsSwitch = new CheckBox(MessageResources.getString("showHealedWounds"));
		
		//final Switch showOnlyCurrentWoundsSwitch = new Switch(MessageResources.getString("currentWoundsOnly"));
		showOnlyCurrentWoundsSwitch.setValue(!getBoolShowCurrentWoundsOnly());
		showOnlyCurrentWoundsSwitch.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				boolean scwo = !showOnlyCurrentWoundsSwitch.getValue();
				setBoolShowCurrentWoundsOnly(scwo);
//				getNavigationManager().navigateTo(
//					new PatientView(currentPatient, getBoolShowCurrentWoundsOnly()));
				getEnvironment().setShowCurrentWoundsOnly(scwo);
//				if (Page.getCurrent().getUriFragment().equals("patient-false")){
//					Page.getCurrent().setUriFragment("patient", true);
//				} else {
//					Page.getCurrent().setUriFragment("patient-false", true);
//				}		
				Page.getCurrent().setUriFragment("");
				Page.getCurrent().setUriFragment("patient", true);
			}
			
		});
		showOnlyCurrentWoundsSwitch.setImmediate(true);
		
		createWoundManager();
		
		Label spacing = new Label("");
		spacing.addStyleName("spacingLabel");
		
		final GridLayout content = new GridLayout(3, 4);
		content.addStyleName("grid");
		content.addComponent(userBar, 0, 0, 2, 0);
		
		final VerticalLayout switchSpacePic = new VerticalLayout();
		switchSpacePic.addComponents(showOnlyCurrentWoundsSwitch, 
					spacing, woundManager.getWoundSelector());
		content.addComponent(switchSpacePic, 0, 1, 0, 3);
		
		int wdth = UI.getCurrent().getPage().getBrowserWindowWidth();  
		int hght = UI.getCurrent().getPage().getBrowserWindowHeight();
		
		if (wdth > hght) {
			content.addComponent(rightContent, 2, 2);
		}else{
			switchSpacePic.addComponent(rightContent);
		}
		
		Page.getCurrent().addBrowserWindowResizeListener(new Page.BrowserWindowResizeListener() {

			@Override
			public void browserWindowResized(BrowserWindowResizeEvent event) {
				int width = UI.getCurrent().getPage().getBrowserWindowWidth();  
				int height = UI.getCurrent().getPage().getBrowserWindowHeight();
				if (width > height) {
					switchSpacePic.removeComponent(rightContent);
					content.addComponent(rightContent, 2, 2);
					rightContent.addStyleName("patViewRightContent");
					new Responsive(rightContent);
				}else{
					content.removeComponent(rightContent);
					switchSpacePic.addComponent(rightContent);
					rightContent.addStyleName("patViewRightContent");
					new Responsive(rightContent);
				}
				
			}
		});

		new Responsive(switchSpacePic);
		new Responsive(content);
		
		this.prepareSelectedWound(getEnvironment().getCurrentWound());
		
		setContent(content);
		
		Button backButton = new Button("< " + MessageResources.getString("patientSelection"));
		backButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				Page.getCurrent().setUriFragment("patientSelection");
			}
			
		});
		
		setLeftComponent(backButton);
	}

	
	private void createWoundManager() {
		float heightFactor = (getEnvironment().getWindowHeight()-104)/513;
		float widthFactor = getEnvironment().getWindowWidth()/600;
		
		float scaleFactor = widthFactor < heightFactor ? widthFactor : heightFactor;

		if (scaleFactor < 1){
			woundManager = new WoundManager(currentPatient, getBoolShowCurrentWoundsOnly(), scaleFactor);
		} else {
			woundManager = new WoundManager(currentPatient, getBoolShowCurrentWoundsOnly(), 1);
		}
		
		woundManager.addSelectedWoundChangeListener(this);
	}

	@Override
	public void selectedWoundChanged(SelectedWoundChangeEvent event) {
		Wound selectedWound = event.getWound();
		rightContent.removeAllComponents();	
				
		if (selectedWound != null) {
			rightContent.addComponent(new ExistingWound(this, selectedWound));
		}
		else if (event.getWoundPosition() != null) {
			rightContent.addComponent(new NewWound(this, currentPatient, event.getWoundPosition().getBodyLocation()));
		}
	}
	
//	@Override
//	public void onBecomingVisible(){
//		super.onBecomingVisible();
////		this.setNavigationManagerPreviousComponent();
//		this.prepareSelectedWound(getEnvironment().getCurrentWound());
//		Page.getCurrent().setUriFragment("patient");
//	}


//	@Override
//	public void wardChanged(WardChangeEvent event) {
//		this.setNavigationManagerPreviousComponent();
//	}
	
	public void setSelectedWound(Wound wound) {
		if (wound != null && wound.getPatient().getId() == currentPatient.getId()) {
			woundManager.addWound(wound);
			woundManager.setSelectedWound(wound);
		}
		else {
			woundManager.setSelectedWound(null);
		}
//		this.setNavigationManagerPreviousComponent();
	}
	
	public void prepareSelectedWound(Wound wound) {
		if (wound != null && wound.getPatient().getId() == currentPatient.getId()) {
			woundManager.addWound(wound);
			woundManager.setSelectedWound(wound);
		}
		else {
			woundManager.setSelectedWound(null);
		}
	}
	
	public Patient getPatient(){
		return currentPatient;
	}
	
//	public void setNavigationManagerPreviousComponent(){
//		getNavigationManager().setPreviousComponent(new PatientSelectionView());
//	}

	public boolean getBoolShowCurrentWoundsOnly() {
		return this.showCurrentWoundsOnly;
	}
	
	public void setBoolShowCurrentWoundsOnly(Boolean showCurrentWoundsOnly) {
		this.showCurrentWoundsOnly = showCurrentWoundsOnly;
	}

}
