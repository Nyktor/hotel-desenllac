/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 27/05/2024
 * Fitxer: Habitacio.java
 * Descripcio: Classe que contindra tota la informacio relacionada amb les habitacions 
 */

package hotel;

import java.util.ArrayList;
import java.util.List;

public class Habitacio {
	
	private String ID;
	private List<Hoste> hostes = new ArrayList<>();
	private boolean entratAnteriorment;
	
	public Habitacio(String ID) {
		this.ID = ID;
		this.entratAnteriorment = false;
	}
	
	public String obtenirID() {
		return this.ID;
	}
	
	public void marcarEntrar() {
		this.entratAnteriorment = true;
	}
	
	public boolean hasEntratAnteriorment() {
		return this.entratAnteriorment;
	}
	
	public int obtenirNumeroPlanta() {
		return Integer.valueOf(ID.substring(0, 1));
	}
	
	public int obtenirNumeroPorta() {
		return Integer.valueOf(ID.substring(1));
	}
	
	public boolean estaPlena() {
		return hostes.size() == 4;
	}
	
	public boolean afegirHoste(Hoste hoste) {
		if(estaPlena()) return false;
		else {
			this.hostes.add(hoste);
			return true;
		}
	}
	
	public Hoste obtenirHoste(String nomComplet) {
		Hoste hoste = null;
		boolean trobat = false;
		int i = 0;
		
		while(!trobat && i < hostes.size()) {
			if(hostes.get(i).obtenirNomComplet().equals(nomComplet)) {
				hoste = hostes.get(i);
				trobat = true;
			}else i++;
		}
		
		return hoste;
	}
	
	public int numeroHostesAllotjats() {
		return this.hostes.size();
	}
	
	public List<Hoste> obtenirLlistaHostes(){
		return this.hostes;
	}

}
