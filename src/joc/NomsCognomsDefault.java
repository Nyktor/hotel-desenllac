/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 29/05/2024
 * Fitxer: NomsCognomsDefault.java
 * Descripcio: Classe que conte noms i cognoms predeterminats que utilitzara el propi joc
 */
package joc;

public class NomsCognomsDefault {
	
	public static final String[][] noms = {{"Frank", "home"}, {"Ernest", "home"}, {"Juan", "home"}, {"Marta", "dona"}, {"Oriol", "home"}, {"Raquel", "dona"}, {"Yolanda", "dona"}, {"Raul", "home"}, 
										   {"Begonya", "dona"}, {"David", "home"}, {"Xavier", "home"}, {"Marc", "home"}, {"Javi", "home"}, {"Angel", "home"},
										   {"Pol", "home"}, {"Sergio", "home"}, {"Aitor", "home"}, {"Paula", "dona"}, {"Ivan", "home"}, {"Alba", "dona"}, {"Lidia", "dona"}};
	
	public static final String[] cognoms = {"Denoument", "Lagunas", "Sorolla", "Casanova", "Mesa", "Ferrera", "Martin",
											"Barbero", "Garcia", "Ruiz", "Diaz", "Martin", "Malaguilla",
											"Moreno", "Vilches", "Mas", "Douton", "Puertas", "Montedeoca", "Iglesias"};

	public static String obtenirGenere(String nom) {
		int i = 0;
		boolean trobat = false;
		String genere = null;
		
		while(!trobat && i < noms.length) {
			if(noms[i][0].equals(nom)) {
				trobat = true;
				genere = noms[i][1];
			}else i++;
		}
		
		return genere;
	}
}
