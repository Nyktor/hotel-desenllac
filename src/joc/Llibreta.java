/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 1/06/2024
 * Fitxer: Llibreta.java
 * Descripcio: Classe que permetra emmagatzemar totes les pistes que el detectiu vagi trobant
 */
package joc;

import java.util.ArrayList;
import java.util.List;

import hotel.Habitacio;
import hotel.Hoste;
import hotel.Hotel;
import hotel.Rol;

public class Llibreta {
	
	/*
	 * Classe interna pista: nomes s'utilitzara
	 * dins la classe Llibreta, servira d'struct
	 * per contenir tant qui ha dit la pista com la
	 * informacio que ha donat
	 */
	class Pista{
		Hoste hoste;
		String tipusPista;
		String valorPista;
		String validesaPista; // Pista vs Rumor
		
		public Pista(Hoste hoste, String tipusPista, String valorPista, String validesaPista) {
			this.hoste = hoste;
			this.tipusPista = tipusPista;
			this.valorPista = valorPista;
			this.validesaPista = validesaPista;
		}
		
	}
	
	private int numeroPistes;
	private List<Pista> pistes = new ArrayList<Pista>();
	private Hotel hotel;
	private Hoste victima;
	
	public Llibreta(Hotel hotel, Hoste victima) {
		this.numeroPistes = 0;
		this.hotel = hotel;
		this.victima = victima;
	}

	public int obtenirNumeroPistes() {
		return numeroPistes;
	}
	
	public void afegirPista(Hoste h, String tipus, String valor, String validesa) {
		Pista p = new Pista(h, tipus, valor, validesa);
		pistes.add(p);
		
		numeroPistes++;
	}
	
	public void imprimirLlistaHostes() {
		System.out.println("\nHOSTES TOTALS: "+hotel.llistaSospitosos().size());
		for(int i = 0; i < hotel.numeroPlantes(); i++) {
			System.out.println("PLANTA "+(i+1)+":");
			for(int j = 0; j < hotel.habitacionsPerPlanta(); j++) {
				Habitacio h = hotel.obtenirHabitacio(i, j);
				System.out.print("Habitacio "+h.obtenirID()+": ");
				if(h.numeroHostesAllotjats() == 0) {
					System.out.println("(buida)");
				}else {
					System.out.println();
					for(Hoste hoste : h.obtenirLlistaHostes()) {
						System.out.print("- "+hoste.obtenirNomComplet());
						
						// Nomes podras anotar les caracteristiques de l'hoste si l'has observat i s'ha presentat
						if(hoste.hasObservat() && hoste.shaPresentat()) {
							System.out.println(" ("+hoste.obtenirEdat()+" anys, "+hoste.obtenirAlsadaAproximada()+"m, "+hoste.obtenirPesAproximat()+"kg, PELL "+hoste.obtenirColorPell()+", CABELL "+hoste.obtenirColorCabell()+", ULLS "+hoste.obtenirColorUlls()+")");
						}else if(hoste.obtenirRol() == Rol.VICTIMA){
							System.out.println(" (victima)");
						}else {
							System.out.println();
						}
					}
				}
			}
		}
	}

	public void imprimirPistesVictima(String dataMort) {
		System.out.println("\nINFORMACIO DE LA VICTIMA:\n"
							+ ""+victima.obtenirNomComplet()+", "+victima.obtenirEdat()+" anys. Assassina"+(victima.obtenirGenere().equals("home") ? "t" : "da")+" el "+dataMort+"\n"
						    + victima.obtenirPes()+"kg, "+victima.obtenirAlsada()+"m d'alcada. Pell "+victima.obtenirColorPell()+", ulls "+victima.obtenirColorUlls()+" i cabell "+victima.obtenirColorCabell()+".\n"
				    		+ "Assassina"+(victima.obtenirGenere().equals("home") ? "t" : "da")+" a la planta "+victima.obtenirHabitacio().obtenirNumeroPlanta()+", habitacio numero "+victima.obtenirHabitacio().obtenirID()+".");
		
	}
	
	public void imprimirPistesAssassi() {
		System.out.println("\nPISTES ACTUALS SOBRE L'ASSASSI:");
		if(numeroPistes == 0) System.out.println("(Aquesta seccio esta buida, de moment).");
		else{
			for(Pista p : pistes) {
				System.out.println(p.hoste.obtenirNomComplet()+" (Habitacio "+(p.hoste.obtenirHabitacio().obtenirID())+") ha "+(p.validesaPista.equals("rumor")? "sentit el RUMOR" : "DECLARAT que")+" que l'assassi tindria "+p.tipusPista+" "+p.valorPista+".");
			}
		}
	}

}
