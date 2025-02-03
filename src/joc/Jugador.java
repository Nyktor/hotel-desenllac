/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 27/05/2024
 * Fitxer: Jugador.java
 * Descripcio: Arxiu que conte tota la informacio dels jugadors
 */
package joc;

public class Jugador {
	
	private String nom;
	private int edat;
	private int casosResolts;
	private int puntuacio;
	
	public Jugador(String nom, int edat) {
		this.nom = nom;
		this.edat = edat;
		this.casosResolts = 0;
		this.puntuacio = 0;
	}
	
	public Jugador(String nom, int edat, int casosResolts, int puntuacio) {
		this.nom = nom;
		this.edat = edat;
		this.casosResolts = casosResolts;
		this.puntuacio = puntuacio;
	}
	
	public String obtenirNom() {
		return this.nom;
	}
	
	public void canviarNom(String nouNom) {
		this.nom = nouNom;
	}
	
	public int obtenirEdat() {
		return this.edat;
	}
	
	public int obtenirCasosResolts() {
		return this.casosResolts;
	}
	
	public void afegirCasResolt() {
		this.casosResolts++;
	}
	
	public int obtenirPuntuacio() {
		return this.puntuacio;
	}
	
	public void afegirPunts(int puntsAfegir) {
		this.puntuacio+=puntsAfegir;
	}

}
