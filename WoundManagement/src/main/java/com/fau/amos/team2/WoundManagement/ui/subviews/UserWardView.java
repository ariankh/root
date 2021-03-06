package com.fau.amos.team2.WoundManagement.ui.subviews;

import java.util.ArrayList;
import java.util.List;

import com.fau.amos.team2.WoundManagement.model.Employee;
import com.fau.amos.team2.WoundManagement.model.Ward;
import com.fau.amos.team2.WoundManagement.provider.EmployeeProvider;
import com.fau.amos.team2.WoundManagement.provider.WardProvider;
import com.fau.amos.team2.WoundManagement.resources.MessageResources;
import com.fau.amos.team2.WoundManagement.ui.SessionedPopover;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

@Theme("wm-responsive")
@PreserveOnRefresh
@SuppressWarnings("serial")
public class UserWardView extends SessionedPopover {
	
	private static WardProvider wardProvider = 
			WardProvider.getInstance();
	
	private static EmployeeProvider employeeProvider = 
			EmployeeProvider.getInstance();

	public UserWardView() {
				
		setClosable(true);
		setModal(true);

		setCaption(MessageResources.getString("changeWard"));
		final Employee user = getEnvironment().getCurrentEmployee(); 
		
		VerticalLayout layout = new VerticalLayout();
		
		final OptionGroup wardGroup = new OptionGroup();
		
		List<Ward> wards = wardProvider.getAllItems();
		for (Ward ward : wards) {
			wardGroup.addItem(ward.getId());
			wardGroup.setItemCaption(ward.getId(), ward.getCharacterisation());
		}
		
		wardGroup.addItem(-1);
		wardGroup.setItemCaption(-1, MessageResources.getString("allPatients"));
				
		wardGroup.select(user.getCurrentWard().getId());
		
		Button changeWardButton = new Button(MessageResources.getString("changeWard"));
		changeWardButton.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				Ward newWard;
				
				if ((int)wardGroup.getValue() == -1)
					newWard = null;
				else
					newWard = wardProvider.getByID(wardGroup.getValue());
				
				if (newWard != null) {
					user.setCurrentWard(newWard);
					employeeProvider.update(user);
				
					fireWardChangeEvent(newWard);
				}
				else
					fireWardChangeEvent();
				
				UserWardView.this.close();
			}
			
		});
		
		layout.addComponent(wardGroup);
		layout.addComponent(changeWardButton);
		
		setContent(layout);
		
	}

		
	
	public List<WardChangeListener> wardChangeListeners = null;
	
	public interface WardChangeListener {
		public void wardChanged(WardChangeEvent event);
	}
    
    public class WardChangeEvent {
		final Ward ward;
		
		public WardChangeEvent() {
			this.ward = null;
		}
		
		public WardChangeEvent(Ward ward) {
			this.ward = ward;
		}

		public Ward getWard(){
			return this.ward;
		}

	}
    
    public void fireWardChangeEvent() {
    	if (wardChangeListeners != null) {
    		WardChangeEvent event = new WardChangeEvent();
    		for (WardChangeListener listener : wardChangeListeners)
				listener.wardChanged(event);
    	}
    }
    
    public void fireWardChangeEvent(Ward ward) {
    	
		if (wardChangeListeners != null) {
			WardChangeEvent event = new WardChangeEvent(ward);
			for (WardChangeListener listener : wardChangeListeners)
				listener.wardChanged(event);
		}
	}
    
    public void addWardChangeListener(WardChangeListener listener) {
        if (wardChangeListeners == null) {
            wardChangeListeners = new ArrayList<WardChangeListener>();
        }
        wardChangeListeners.add(listener);
    }

    public void removeWardChangeListener(WardChangeListener listener) {
        if (wardChangeListeners != null) {
        	wardChangeListeners.remove(listener);
        }
    }

}
