package com.fau.amos.team2.WoundManagement.provider;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.fau.amos.team2.WoundManagement.model.Wound;
import com.fau.amos.team2.WoundManagement.model.WoundDescription;

/**
 * <code>WoundDescriptionProvider</code> manages the access to the
 * Database 'wounddescription', which handles objects
 * of the type <code>WoundDescription</code>
 * 
 * */
public class WoundDescriptionProvider extends ObjectProvider<WoundDescription>{

	private static WoundDescriptionProvider instance;
	
	public WoundDescriptionProvider(Class<WoundDescription> type) {
		super(type);
	}
	
	/**
	 * @return The instance of <code>WoundDescriptionProvider</code> 
	 * */
	public static WoundDescriptionProvider getInstance() {
		if(instance == null) {
			instance = new WoundDescriptionProvider(WoundDescription.class);
		}
		return instance;
	}
	
	public void deleteAll() {
		EntityManager em = container.getEntityProvider().getEntityManager();
		em.getTransaction().begin();
		em.createNamedQuery("WoundDescription.deleteAll").executeUpdate();
		em.getTransaction().commit();
	}
	
	public List<WoundDescription> getAllForWound(Wound wound) {
		EntityManager em = container.getEntityProvider().getEntityManager();
		TypedQuery<WoundDescription> query = em.createNamedQuery("WoundDescription.allForWound", WoundDescription.class);
		query.setParameter("wound", wound);
		List<WoundDescription> resultList = query.getResultList();
		return resultList;
	}
	
	public WoundDescription getNewestForWound(Wound wound) {
		List<WoundDescription> descriptions = this.getAllForWound(wound);
		WoundDescription maxDescription = new WoundDescription();
		Date maxDate = new Date(0);
		for (WoundDescription wd : descriptions){
			if (wd.getDate() != null && wd.getDate().after(maxDate)){
				maxDate = wd.getDate();
				maxDescription = wd;
			}
		}
		return maxDescription;
	}
}
