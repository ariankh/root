package com.fau.amos.team2.WoundManagement.BodyWoundSelector;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.fau.amos.team2.WoundManagement.model.Sex;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Image;

@SuppressWarnings("serial")
public class WoundSelector extends AbsoluteLayout implements ClickListener {

	final String BODY_IMAGE = "body_neuter.png";
	final String BODY_IMAGE_MALE = "body_male.png";
	final String BODY_IMAGE_FEMALE = "body_female.png";
	final int BODY_IMAGE_WIDTH = 600;
	final int BODY_IMAGE_HEIGHT = 513;

	final String SELECTION_INDICATOR = "orb.png";
	final String WOUND_INDICATOR = "wound.png";
	final String WOUND_HEALED_INDICATOR = "woundHealed.png";
	final String WOUND_SELECTION_INDICATOR = "orbsel.png";
	final int INDICATOR_HEIGHT = 24;
	final int INDICATOR_WIDTH = 24;

	private Image selectionIndicator;
	private Image selctedWoundHealedIndicator;
	private Image selectedWoundIndicator;
	private WoundManager woundManager;
	private WoundPosition selectedWoundPosition;
	private Map<WoundPosition, Image> markedWounds = new HashMap<WoundPosition, Image>();

	private Sex sex;
	private Boolean existingWoundSelected = false;
	private float scaleFactor = 1;
	
	public WoundSelector(WoundManager woundManager, Sex sex, float scaleFactor) {
		this.woundManager = woundManager;
		this.scaleFactor = scaleFactor;

		Image backgroundImage;
		
		this.sex = (sex != null) ? sex : Sex.NEUTER;
		if (sex == Sex.FEMALE)
			backgroundImage = getImage(BODY_IMAGE_FEMALE);
		else if (sex == Sex.MALE)
			backgroundImage = getImage(BODY_IMAGE_MALE);
		else 
			backgroundImage = getImage(BODY_IMAGE);

		backgroundImage.addClickListener(this);

		selectionIndicator = getImage(SELECTION_INDICATOR);
		selectionIndicator.setVisible(false);

		selectedWoundIndicator = getImage(WOUND_SELECTION_INDICATOR);
		selectedWoundIndicator.setVisible(false);
		
		selctedWoundHealedIndicator = getImage(WOUND_HEALED_INDICATOR);
		selctedWoundHealedIndicator.setVisible(false); 

		addComponents(backgroundImage, 
				selectionIndicator, selectedWoundIndicator, selctedWoundHealedIndicator);

		setWidth(backgroundImage.getWidth(), Unit.PIXELS);
		setHeight(backgroundImage.getHeight(), Unit.PIXELS);
	}

	private Image getImage(String imageFilename) {
		String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		String filePath = basePath + "/" + imageFilename;
		File imageFile = new File(filePath);
		BufferedImage bufferedImage;
		Image image = new Image(null, new FileResource(imageFile));

		if (imageFilename == BODY_IMAGE || imageFilename == BODY_IMAGE_FEMALE || imageFilename == BODY_IMAGE_MALE) {
			image.setHeight(Math.round(scaleFactor*BODY_IMAGE_HEIGHT), Unit.PIXELS);
			image.setWidth(Math.round(scaleFactor*BODY_IMAGE_WIDTH), Unit.PIXELS);
		}
		else if (imageFilename == SELECTION_INDICATOR 
					|| imageFilename == WOUND_INDICATOR
					|| imageFilename == WOUND_HEALED_INDICATOR
					|| imageFilename == WOUND_SELECTION_INDICATOR) {
			image.setHeight(Math.round(scaleFactor*INDICATOR_HEIGHT), Unit.PIXELS);
			image.setWidth(Math.round(scaleFactor*INDICATOR_WIDTH), Unit.PIXELS);
		}
		else {
			// We need to load the image and get the size since Vaadin Touchkit cannot read the size itself
			try {
				bufferedImage = ImageIO.read(imageFile);
				image.setHeight(Math.round(scaleFactor*bufferedImage.getHeight()), Unit.PIXELS);
				image.setWidth(Math.round(scaleFactor*bufferedImage.getWidth()), Unit.PIXELS);
			} catch (IOException e) {
				// we fail silently and let Vaadin use autosizing
			}
		}

		return image;
	}

	public void click(ClickEvent event) {
		int xPosition = event.getRelativeX();
		int yPosition = event.getRelativeY();

		// Uncomment the following line to show clicked position coordinates.
		//com.vaadin.ui.Notification.show("X " + xPosition + " Y " + yPosition);

		// Get the wound at this position
		WoundPosition woundPosition = woundManager.getWoundPositionAtCoordinates(
				(int) Math.round(xPosition/scaleFactor),
				(int) Math.round(yPosition/scaleFactor));
		
		setSelectedWoundPosition(woundPosition);
	}
	
	public void setSelectedWoundPosition(WoundPosition woundPosition) {
		selectedWoundPosition = woundPosition;
		
		if (woundPosition != null) {
			existingWoundSelected = woundManager.hasWoundAtPosition(selectedWoundPosition);
//			woundManager.setSelectedWoundPosition(selectedWoundPosition);
		}
		woundManager.setSelectedWoundPosition(selectedWoundPosition);
		refreshSelectedWound();
	}

	private void refreshSelectedWound() {
		if (selectedWoundPosition != null && existingWoundSelected) {
			// Removing half the size of the indicator to put the click position in the middle of the indicator
			float correctedXPos = (float) scaleFactor*(selectedWoundPosition.getXPosition() - (selectedWoundIndicator.getWidth() / 2));
			float correctedYPos = (float) scaleFactor*(selectedWoundPosition.getYPosition() - (selectedWoundIndicator.getHeight() / 2));

			ComponentPosition imagePosition = new ComponentPosition();
			imagePosition.setLeft((float)correctedXPos, Unit.PIXELS);
			imagePosition.setTop((float)correctedYPos, Unit.PIXELS);

			setPosition(selectedWoundIndicator, imagePosition);

			selectedWoundIndicator.setVisible(true);
		}
		else {
			selectedWoundIndicator.setVisible(false);
		}

		refreshSelectionIndicator();
	}

	public WoundPosition getSelectedWoundPosition() {
		return selectedWoundPosition;
	}

	public void addWoundAtPosition(WoundPosition woundPosition) {
		if (!markedWounds.containsKey(woundPosition)) {
			
			Image image = getImage(WOUND_INDICATOR);
			
			if (woundManager.getWoundAtWoundPosition(woundPosition).getEndDate() != null) {
				image = getImage(WOUND_HEALED_INDICATOR);
			}
			
			image.addClickListener(woundClickListener);
			image.setDescription(woundPosition.getDescription());
			image.setAlternateText(woundPosition.getDescription());

			// Removing half the size of the indicator to put the click position in the middle of the indicator
			float correctedXPos = (float) scaleFactor*(woundPosition.getXPosition() - (image.getWidth() / 2));
			float correctedYPos = (float) scaleFactor*(woundPosition.getYPosition() - (image.getHeight() / 2));

			ComponentPosition imagePosition = new ComponentPosition();
			imagePosition.setLeft((float)correctedXPos, Unit.PIXELS);
			imagePosition.setTop((float)correctedYPos, Unit.PIXELS);
			image.setData(woundPosition);

			markedWounds.put(woundPosition, image);
			addComponent(image);
			setPosition(image, imagePosition);
		}
	}

	private ClickListener woundClickListener = new ClickListener() {

		@Override
		public void click(ClickEvent event) {
			Object sourceData = ((Image)event.getSource()).getData();
			if (sourceData != null) {
				WoundPosition woundPosition = (WoundPosition)sourceData;
				selectedWoundPosition = woundPosition;
				existingWoundSelected = true;
				
				woundManager.setSelectedWoundPosition(selectedWoundPosition);
				
				refreshSelectedWound();
			}
		}
	};

	public void removeWoundAtPosition(WoundPosition woundPosition) {
		if (markedWounds.containsKey(woundPosition)) {
			Image image = markedWounds.get(woundPosition);
			markedWounds.remove(woundPosition);
			removeComponent(image);
		}
	}

	private void refreshSelectionIndicator() {
		if (selectedWoundPosition == null || existingWoundSelected) {
			selectionIndicator.setVisible(false);
			return;
		}

		// Removing half the size of the indicator to put the click position in the middle of the indicator
		float correctedXPos = (float) scaleFactor*(selectedWoundPosition.getXPosition() - (selectionIndicator.getWidth() / 2));
		float correctedYPos = (float) scaleFactor*(selectedWoundPosition.getYPosition() - (selectionIndicator.getHeight() / 2));

		ComponentPosition newPosition = new ComponentPosition();
		newPosition.setLeft(correctedXPos, Unit.PIXELS);
		newPosition.setTop(correctedYPos, Unit.PIXELS);

		selectionIndicator.setVisible(true);
		setPosition(selectionIndicator, newPosition);
	}
	
	public Sex getSex() {
		return this.sex;
	}
}