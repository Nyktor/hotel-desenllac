package hotel;

import java.util.ArrayList;
import java.util.List;

public class Hotel {

	private int gentAllotjada;
	
	private Habitacio[][] habitacions;
	
	public Hotel(byte plantes, byte habitacionsPerPlanta) {

		habitacions = new Habitacio[plantes][habitacionsPerPlanta];
		
		for(int i = 0; i < plantes; i++) {
			for(int j = 0; j < habitacionsPerPlanta; j++) {
				habitacions[i][j] = new Habitacio(""+(i+1) + (j < 9 ? "0"+(j+1) : (j+1)));
			}
		}
	}
	
	/*************************************************
	 * Obtenir habitacio per planta i numero de porta.
	 *************************************************/
	public Habitacio obtenirHabitacio(int planta, int numero) {
		return this.habitacions[planta][numero];
	}

	/*************************************************************
	 * Obtenir habitacio per ID (3 digits, numero de planta+porta)<br>
	 * <i>Exemples: "102", "210", "106"</i>
	 *************************************************************/
	public Habitacio obtenirHabitacioPerID(String ID) {
		if(ID.length() != 3) return null;
		else {
			try {
				int planta = Integer.valueOf(ID.substring(0, 1));
				int porta = Integer.valueOf(ID.substring(1));
				
				return this.habitacions[planta-1][porta-1];
			}catch(Exception e) {
				return null;
			}
		}
	}
	
	public int numeroPlantes() {
		return this.habitacions.length;
	}
	
	public int habitacionsPerPlanta() {
		return this.habitacions[0].length;
	}
	
	public int obtenirQuantitatGentAllotjada() {
		return this.gentAllotjada;
	}
	
	public void posarGentAllotjada(int quantitat) {
		this.gentAllotjada = quantitat;
	}
	
	/**************************************************************
	 * Recorre cada planta i cada habitacio, i retorna
	 * una llista amb tots els hostes de l'hotel.<br>
	 * <b>IMPORTANT: Es nomes descriptiva. No utilitzar-la per a
	 * els hostes reals ja que no surtira cap efecte.
	 **************************************************************/
	public List<Hoste> llistaSospitosos(){
		List<Hoste> llistaSospitosos = new ArrayList<>();
		
		for(int i = 0; i < habitacions.length; i++) {
			for(int j = 0; j < habitacions[i].length; j++) {
				for(Hoste h : habitacions[i][j].obtenirLlistaHostes()) {
					llistaSospitosos.add(h);
				}
			}
		}
		
		return llistaSospitosos;
	}
	
	/**************************************************************
	 * Augmenta el nivell d'estres dels hostes en general.
	 **************************************************************/
	public void augmentarNivellEstresGeneral(int quantitat){
		
		for(int i = 0; i < habitacions.length; i++) {
			for(int j = 0; j < habitacions[i].length; j++) {
				for(Hoste h : habitacions[i][j].obtenirLlistaHostes()) {
					h.augmentarNivellEstres(quantitat);
				}
			}
		}
		
	}

	
	/**************************************************
	 * Metode que permet comprobar si un nom existeix a
	 * la llista dels hostes de l'hotel
	 ***************************************************/
	public Hoste obtenirHostePerNom(String nomComplet) {
		
		Hoste hoste = null;
		boolean trobat = false;
		int i = 0;
		
		while(i < llistaSospitosos().size() && !trobat) {
			
			if(llistaSospitosos().get(i).obtenirNomComplet().equals(nomComplet)) {
				trobat = true;
				hoste = llistaSospitosos().get(i);
			}
			else i++;
		}
		
		return hoste;
	}
	
	/***********************************************************
	 * Imprimeix el mapa de la planta 0-indexada indicada, 
	 * amb la quantitat de persones allotjades a cada habitacio
	 ***********************************************************/
	public void imprimirPlanta(int planta) {
		System.out.println("Planta "+(planta+1)+"\n"
						   + "---------");
		for(int i = 0; i < habitacions[planta].length-1; i+=2) {
			System.out.print("-["+habitacions[planta][i].obtenirID()+"("+habitacions[planta][i].numeroHostesAllotjats()+")]-");
		}
		System.out.println("\n");
		for(int i = 1; i < habitacions[planta].length; i+=2) {
			System.out.print("-["+habitacions[planta][i].obtenirID()+"("+habitacions[planta][i].numeroHostesAllotjats()+")]-");
		}
		System.out.println("\n");
	}

}
