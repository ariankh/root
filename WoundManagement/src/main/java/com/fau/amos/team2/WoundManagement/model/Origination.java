package com.fau.amos.team2.WoundManagement.model;

import java.util.HashMap;
import java.util.Map;

public enum Origination {

	NULL(0),
	HAEUSLICH(1),
	KRANKENHAUS_EINZUG(2),
	ANDERE_EINRICHTUNG(3),
	KRANKENHAUS_RUECKKEHR(4), 
	SONSTIGE_VOR_HEIMAUFENTHALT(5), 
	EIGENE_EINRICHTUNG(6), 
	URLAUB(7), 
	AMBULANTE_OP(8), 
	SONSTIGE_WAEHREND_HEIMAUFENTHALT(9);
	
	private final int value;
	
	private Origination(int i){
		this.value = i;
	}
	
	private static Map<Integer, Origination> map = new HashMap<Integer, Origination>();

    static {
        for (Origination originationEnum : Origination.values()) {
            map.put(originationEnum.value, originationEnum);
        }
    }
    
    public static Origination valueOf(int originationNo) {
        return map.get(originationNo);
    }
	
	public String toString(){
		switch (this){
		case NULL: return "";
		case HAEUSLICH: return "Häuslich";
		case KRANKENHAUS_EINZUG: return "Krankenhaus/Einzug";
		case ANDERE_EINRICHTUNG: return "Andere Einrichtung";
		case KRANKENHAUS_RUECKKEHR: return "Krankenhaus/Rückkehr";
		case SONSTIGE_VOR_HEIMAUFENTHALT: return "Sonstige vor Heimaufenthalt";
		case EIGENE_EINRICHTUNG: return "Eigene Einrichtung";
		case URLAUB: return "Urlaub";
		case AMBULANTE_OP: return "Ambulante OP";
		case SONSTIGE_WAEHREND_HEIMAUFENTHALT: return "Sonstige während Heimaufenthalt";
		}
		return "";
	}
	
	public String toFullString(){
		if (this == NULL){
			return this.toString();
		}
		return this.value + " - " + this.toString();
	}
	
	public int getValue(){
		return this.value;
	}
}
