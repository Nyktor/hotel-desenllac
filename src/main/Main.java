/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 27/05/2024
 * Fitxer: Main.java
 * Descripcio: Arxiu principal del joc de l'Hotel Desenllac.
 */

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import joc.Dificultat;
import joc.Joc;
import joc.Jugador;

public class Main {
	
	static final String FITXER_JUGADORS = "jugadors.txt";
	
	static Scanner sc = new Scanner(System.in);
	static Jugador jugadorActual;
	static Dificultat dificultat = Dificultat.FACIL;

	public static void main(String[] args) {
		
		System.out.println("------------------------------------------\n"
					     + "Benvingut/da al Hotel Desenllac, detectiu.\n"
					     + "------------------------------------------");
		
		canviarJugadorActual();
		
		byte opcio = 0;
		
		do {
			
			printMenuInicial();
			opcio = sc.nextByte();
			
			switch(opcio) {
			
				// 1. Iniciar joc >:)
				case 1->{
					Joc joc = new Joc(dificultat);
	
					    //[Punts, cas resolt]
					int[] puntuacioFinal = joc.iniciarJoc(jugadorActual.obtenirNom());
					
					if(puntuacioFinal[1] == 1) {
						System.out.println("\nEnhorabona, detectiu "+jugadorActual.obtenirNom()+". Has resolt el cas.");
						jugadorActual.afegirCasResolt();
						esperar(2);
					}else {
						System.out.println("\nNo has pogut resoldre el cas, "+jugadorActual.obtenirNom()+". Ets una desgracia per la VFD.");
						esperar(2);
					}
					
					System.out.println("\nPunts guanyats: "+puntuacioFinal[0]);
					jugadorActual.afegirPunts(puntuacioFinal[0]);
					
					esperar(2);
				}
				
				// 2. Escollir dificultat
				case 2->{
					String eleccio = "";
					System.out.println("\nLa dificultat actual es "+dificultat);
					do {
						
						System.out.print("Escriu una nova dificultat:\n"
										 + "- Facil\n"
										 + "- Mitjana\n"
										 + "- Dificil\n\n"
										 + ">> ");
						eleccio = sc.next().toLowerCase();
						
						if(!eleccio.equals("facil") && !eleccio.equals("mitjana") && !eleccio.equals("dificil")) {
							tirarError("Escriu 'facil', 'mitjana', o 'dificil', si us plau");
						}
						
					}while(!eleccio.equals("facil") && !eleccio.equals("mitjana") && !eleccio.equals("dificil"));
					
					dificultat = switch(eleccio) {
						case "mitjana" ->{
							yield Dificultat.MITJANA;
						}
						case "dificil" ->{
							yield Dificultat.DIFICIL;
						}
						default ->{
							yield Dificultat.FACIL;
						}
					};
					
					System.out.println("\nS'ha canviat la dificultat a "+eleccio.toUpperCase()+".");
					esperar(1);
					if(dificultat == Dificultat.DIFICIL) {
						System.out.println("Bona sort. La necessitaras :)");
						esperar(1);
					}
				}
				
				// 3. Consultar arxius de la VFD
				case 3->{
					consultarArxiusDeLaVFD();
				}
				
				// 4. Arxius de nom 
				case 4->{
					modificarNomsCognoms();
				}
				
				// 5. Sortir
				case 5->{
					
					boolean aconseguit = guardarJugadors();
					
					if(aconseguit) {
						System.out.println("\nTorna aviat, "+jugadorActual.obtenirNom()+".");
					}else {
						tirarError("No s'ha pogut guardar la informacio a l'arxiu :(.");
						
						char sortirSenseGuardar = 's';
						do {
							
							System.out.print("Vols sortir sense guardar? [s/n]\n>> ");
							sortirSenseGuardar = sc.next().charAt(0);
							
							if(sortirSenseGuardar != 's' && sortirSenseGuardar != 'n') {
								tirarError("Escriu 's' o 'n', si us plau.");
							}
							
						}while(sortirSenseGuardar != 's' && sortirSenseGuardar != 'n');
						
						if(sortirSenseGuardar == 'n') opcio = 0;
					}
				}
				
				default->{
					tirarError("Escull una opcio correcta, si us plau.");
					esperar(1);
				}
			}
		
		}while(opcio != 5);
	}

	
	/**************************************************
	 * Metode que es cridara en premer la opcio 3. Permetra
	 * canviar de jugador, esborrar alguna partida o consultar
	 * les puntuacions ordenades per ranking
	 **************************************************/
	static void consultarArxiusDeLaVFD() {
		byte opcio = 0;
		
		do {
			
			System.out.print("\n++++++++++++++++++++++++++++++++\n"
							   + " Benvingut als arxius de la VFD\n"
							   + "++++++++++++++++++++++++++++++++\n\n"
							   + "1. Consultar ranking de detectius\n"
							   + "2. Canviar de detectiu\n"
							   + "3. Esborrar algun detectiu\n"
							   + "4. Sortir\n\n"
							   + ">> ");
			opcio = sc.nextByte();
			
			switch(opcio) {
				
				// 1. Consultar el ranking de detectius
				case 1-> {
					consultarRanking();
				}
				
				// 2. Canviar de detectiu
				case 2->{

					boolean aconseguit = guardarJugadors();
					char resposta = 's';
					
					if(!aconseguit) {
						tirarError("Hi ha hagut algun error a l'hora de guardar la puntuacio actual.\n"
								 + "Segur que vols continuar? (perdras tota la puntuacio actual).");
						do {
							
							System.out.print(">> ");
							resposta = sc.next().charAt(0);
							
							if(resposta != 's' && resposta != 'n') {
								tirarError("Escriu 's' o 'n', si us plau.");
							}
							
						}while(resposta != 's' && resposta != 'n');
					}
					
					if(resposta == 's') {
						canviarJugadorActual();
					}
				}
				
				// 3. Esborrar algun detectiu
				case 3->{
					esborrarJugador();
				}
				
				// Sortir
				case 4->{}
				
				default->{
					tirarError("Escull una opcio correcta, si us plau.");
				}
			}
			
		}while(opcio != 4);
	}
	
	/**************************************************
	 * Metode que imprimira per pantalla les puntuacions
	 * dels jugadors registrats en ordre de ranking
	 **************************************************/
	static void consultarRanking() {
		boolean esPotGuardar = guardarJugadors();
		List<String[]> ranking = obtenirLlistaOrdenada();
		
		// En cas que el ranking retorni null, vol dir que ha succeit algun error
		if(ranking == null || !esPotGuardar) {
			tirarError("Hi ha hagut un problema intentant ordenar el ranking.");
		}else {
			System.out.println("\n---------------------------\n"
					 		 + "RANKING ACTUAL DE DETECTIUS\n"
					 		 + "----------------------------\n");
			for(int i = 0; i < ranking.size(); i++) {
				System.out.println(ranking.get(i)[0]+": "+ranking.get(i)[2]+" casos resolts, "+ranking.get(i)[3]+" punts.");
				esperar(1);
			}
		}
	}
	
	
	/**************************************************
	 * Metode que llegeix l'arxiu de jugadors i retorna,
	 * en una llista, els jugadors ordenats per puntuacio
	 **************************************************/
	static List<String[]> obtenirLlistaOrdenada(){
		
		try {
			// Primer llegirem l'arxiu sencer per tal de no tocar els altres jugadors
			BufferedReader br = new BufferedReader(new FileReader(FITXER_JUGADORS));
			String linia;
			List<String[]> llistaJugadors = new ArrayList<>();
			
			// Ficar totes les dades dels jugadors a la llista
			while((linia = br.readLine()) != null) {
				llistaJugadors.add(linia.split("-"));
			}
			br.close();
			
			// I utilitzar l'algoritme del Bubble Sort per ordenar la llista;
			boolean fallaTrobada = true;
			int i = 0;
			String[] temp;
			
			// Mentre hagi trobat alguna posicio desordenada...
			while(fallaTrobada) {
				fallaTrobada = false;
				i = 0;
				
				// Iterar tota la llista
				while(i < llistaJugadors.size()-1) {
					if(Integer.valueOf(llistaJugadors.get(i)[3]) < Integer.valueOf(llistaJugadors.get(i+1)[3])) {
						fallaTrobada = true;
						temp = llistaJugadors.get(i);
						llistaJugadors.set(i, llistaJugadors.get(i+1));
						llistaJugadors.set(i+1, temp);
					}
					i++;
				}
			}
			
			return llistaJugadors;
		}catch(Exception e) {
			return null;
		}
		
	}
	
	
	/***********************************************************************
	 * Metode que obtindra la informacio dels arxius noms.txt i cognoms.txt
	 * i permetra tant llistar-los com modificar-los
	 ***********************************************************************/
	static void modificarNomsCognoms() {
		try {
			
			/*
			 * Nom�s accedir a aquesta opcio es carregaran
			 * tant la llista de noms com la de cognoms
			 */
			List<String[]> noms = new ArrayList<String[]>();
			List<String> cognoms = new ArrayList<String>();
			
			BufferedReader br;
			String linia;
			
			
			// Carregar noms
			br = new BufferedReader(new FileReader(Joc.FITXER_NOMS));
			while((linia = br.readLine()) != null) {
				noms.add(linia.split(","));
			}
			br.close();
			
			// Carregar cognoms
			br = new BufferedReader(new FileReader(Joc.FITXER_COGNOMS));
			while((linia = br.readLine()) != null) {
				cognoms.add(linia);
			}
			br.close();
			
			
			byte opcio = 0, opcioSecundaria = 0;
			do {
				
				System.out.print("\n-------------------------------\n"
								 + "Benvingut als registres de noms\n"
								 + "-------------------------------\n\n"
								 + "Que vols consultar?\n"
								 + "1. Noms\n"
								 + "2. Cognoms\n"
								 + "3. Res, sortir\n\n"
								 + ">> ");
				
				opcio = sc.nextByte();
				
				switch(opcio) {
				
					// Consultar noms
					case 1->{
							
						do {
							
							System.out.print("\n--------------------------\n"
										   	 + "Registre seleccionat: NOMS.\n"
										   	 + "---------------------------\n\n"
										   	 + "Escull una opcio:\n"
										   	 + "1. Llistar noms\n"
										   	 + "2. Afegir un nom\n"
										   	 + "3. Esborrar un nom\n"
										   	 + "4. Sortir\n\n"
										   	 + ">> ");
							opcioSecundaria = sc.nextByte();
							
							switch(opcioSecundaria) {
							
								// Llistar noms
								case 1->{
									if(noms.size() == 0) System.out.println("\nNo hi ha cap nom registrat actualment.");
									else {
										int c = 0;
										System.out.println("\nEls noms registrats actualment son:\n");
										
										for(int i = 0; i < noms.size(); i++) {
											System.out.print(noms.get(i)[0]+"("+noms.get(i)[1]+")" + (i == noms.size()-1 ? "" : ", "));
											
											c++;
											if(c == 5) {
												System.out.println();
												c = 0;
											}
										}
									}
									esperar(1);
								}
								
								// Afegir un nou nom
								case 2->{
									String nouNom, nomGenere[] = new String[2];
									char genere = 'm';
									
									System.out.print("\nIntrodueix el nom que vols afegir:\n>> ");
									nouNom = initCap(sc.next());
									
									do {
										System.out.print("\nEs un nom d'home o de dona? [h/d]\n>> ");
										genere = sc.next().charAt(0);
										
										if(genere != 'h' && genere != 'd') {
											tirarError("Escriu 'h' (home) o 'd' (dona), si us plau");
										}
									}while(genere != 'h' && genere != 'd');
									
									nomGenere[0] = nouNom;
									nomGenere[1] = (genere == 'h' ? "home" : "dona");
									
									int i = 0;
									boolean trobat = false;
									
									while(!trobat && i < noms.size()) {
										if(noms.get(i)[0].equals(nomGenere[0])) trobat = true;
										else i++;
									}
									
									if(!trobat) {

										noms.add(nomGenere);
										
										try {
											
											FileWriter fw = new FileWriter(Joc.FITXER_NOMS);
											
											for(String[] s : noms) {
												fw.write(s[0]+","+s[1]+"\n");
											}
											
											fw.close();
											
											System.out.println("\nS'ha afegit el nom "+nouNom+" exitosament als arxius.");
											esperar(1);
											
										}catch(Exception e) {
											tirarError("Hi ha hagut un error intentar guardar el nom.");
										}
										
									}else {
										tirarError("Aquest nom ja existeix!");
									}
									
								}
								
								// Esborrar un nom
								case 3->{
									String nomEsborrar;
									
									System.out.print("\nQuin nom vols esborrar?\n>> ");
									nomEsborrar = initCap(sc.next());
									
									boolean trobat = false;
									int i = 0;
									
									while(i < noms.size() && !trobat) {
										if(noms.get(i)[0].equals(nomEsborrar)) {
											noms.remove(i);
											trobat = true;
										}else i++;
									}
									
									if(trobat) {
										try {
											
											FileWriter fw = new FileWriter(Joc.FITXER_NOMS);
											
											for(String[] s : noms) {
												fw.write(s[0]+","+s[1]+"\n");
											}
											
											fw.close();
											
											System.out.println("\nS'ha esborrat el nom "+nomEsborrar+" correctament de la llista.");
											esperar(1);
											
										}catch(Exception e) {
											tirarError("Hi ha hagut un error intentar guardar els canvis.");
										}
									}
									else System.out.println("\nNo s'ha trobat el nom "+nomEsborrar+" a la llista.");

									esperar(1);
								}
								
								// Sortir
								case 4->{}
								
								default->{
									tirarError("Escull una opcio correcta, si us plau");
								}
							}
							
						}while(opcioSecundaria != 4);
					}
					
					// Consultar cognoms
					case 2->{

						do {
							
							System.out.print("\n--------------------------\n"
										   	 + "Registre seleccionat: COGNOMS.\n"
										   	 + "---------------------------\n\n"
										   	 + "Escull una opcio:\n"
										   	 + "1. Llistar cognoms\n"
										   	 + "2. Afegir un cognom\n"
										   	 + "3. Esborrar un cognom\n"
										   	 + "4. Sortir\n\n"
										   	 + ">> ");
							opcioSecundaria = sc.nextByte();
							
							switch(opcioSecundaria) {
							
								// Llistar cognoms
								case 1->{
									if(cognoms.size() == 0) System.out.println("\nNo hi ha cap cognom registrat actualment.");
									else {
										int c = 0;
										System.out.println("\nEls cognoms registrats actualment son:\n");
										
										for(int i = 0; i < cognoms.size(); i++) {
											System.out.print(cognoms.get(i) + (i == cognoms.size()-1 ? "" : ","));
											
											c++;
											if(c == 5) {
												System.out.println();
												c = 0;
											}
										}
									}
									esperar(1);
								}
								
								// Afegir un nou cognom
								case 2->{
									String nouCognom;

									System.out.print("\nIntrodueix el cognom que vols afegir:\n>> ");
									nouCognom = initCap(sc.next());
									
									int i = 0;
									boolean trobat = false;
									
									while(!trobat && i < cognoms.size()) {
										if(cognoms.get(i).equals(nouCognom)) trobat = true;
										else i++;
									}
									
									if(!trobat) {

										cognoms.add(nouCognom);
										
										try {
											
											FileWriter fw = new FileWriter(Joc.FITXER_COGNOMS);
											
											for(String s : cognoms) {
												fw.write(s+"\n");
											}
											
											fw.close();
											
											System.out.println("\nS'ha afegit el cognom "+nouCognom+" exitosament als arxius.");
											esperar(1);
											
										}catch(Exception e) {
											tirarError("Hi ha hagut un error intentar guardar el cognom.");
										}
									}else {
										tirarError("Aquest cognom ja existeix!");
									}
									
								}
								
								// Esborrar un cognom existent
								case 3->{
									String cognomEsborrar;
									
									System.out.print("\nQuin cognom vols esborrar?\n>> ");
									cognomEsborrar = initCap(sc.next());
									
									boolean trobat = false;
									int i = 0;
									
									while(i < cognoms.size() && !trobat) {
										if(cognoms.get(i).equals(cognomEsborrar)) {
											noms.remove(i);
											trobat = true;
										}else i++;
									}
									
									if(trobat) System.out.println("\nS'ha esborrat el cognom "+cognomEsborrar+" correctament de la llista.");
									else System.out.println("\nNo s'ha trobat el cognom "+cognomEsborrar+" a la llista.");
									
									esperar(1);
								}
								case 4->{}
								
								default->{
									tirarError("Escull una opcio correcta, si us plau");
								}
							}
							
						}while(opcioSecundaria != 4);
					}
					
					// Sortir
					case 3->{}
					
					default ->{
						tirarError("Escull una opcio correcta, si us plau.");
					}
				}
				
			}while(opcio != 3);
		}catch(Exception e) {
			tirarError("Hi ha hagut un error intentant accedir als arxius.");
		}
	}
	
	/**************************************************
	 * Funcio que canviara el jugador actual. Permetra
	 * tant posar el jugador inicial com canviar-lo mes
	 * tard en el codi.
	 **************************************************/
	static void canviarJugadorActual() {
		
		Jugador jugadorCarregat;
		String nom;
		char resposta = 's';
		
		do {
			
			System.out.print("\nIntrodueix el teu nom:\n>> ");
			
			nom = sc.next();
			
			jugadorCarregat = carregarJugador(nom);
			
			// Si el jugador amb el nom proporcionat no existeix al fitxer
			if(jugadorCarregat == null) {
				
				System.out.println("\n[INFO] No s'ha trobat cap detectiu amb el nom "+nom+" al nostre registre.\n"
								   + "       Vols crear una partida nova amb aquest nom? [s/n]\n");
				
				// Preguntar si vol tornar a intentar introduir un altre usuari
				do {
					System.out.print(">> ");
					resposta = sc.next().charAt(0);
					
					if(resposta != 'n' && resposta != 's') {
						tirarError("Introdueix 's' o 'n', si us plau.");
						esperar(1);
					}
				
				}while(resposta != 'n' && resposta != 's');
				
				// Si no vol reintentar, es comencara una partida nova
				if(resposta == 's') {
					
					registrarNouJugador(nom);
				}
			
			// Si el nom proporcionat existeix en el fitxer
			}else {
				
				System.out.println("\n[INFO] S'ha trobat un detectiu al nostre registre amb el nom de "+nom+".\n"
								   + "       Vols carregar el seu perfil? [s/n]");
				
				// Preguntar si vol carregar la puntuacio anterior
				do {
					System.out.print(">> ");
					resposta = sc.next().charAt(0);
					
					if(resposta != 'n' && resposta != 's') {
						tirarError("Introdueix 's' o 'n', si us plau.");
						esperar(1);
					}
				
				}while(resposta != 'n' && resposta != 's');
				
				// En cas que vulgui carregar el perfil anterior
				if(resposta == 's') {
					jugadorActual = jugadorCarregat;
					System.out.println("\n[INFO] S'ha carregat el perfil! Benvingut de nou, detectiu");
				
				// En cas que no, 
				}else {
					System.out.println("\nVols comencar una partida de zero amb el nom "+nom+"? [s/n]");
					
					// Preguntem si vol comencar una partida de zero
					do {
						System.out.print(">> ");
						resposta = sc.next().charAt(0);
						
						if(resposta != 'n' && resposta != 's') {
							tirarError("Introdueix 's' o 'n', si us plau.");
							esperar(1);
						}
					
					}while(resposta != 'n' && resposta != 's');
					
					// Si vol comencar de zero, registrar el nou usuari
					if(resposta == 's') {
						
						registrarNouJugador(nom);

					}
				}
			}
			
		}while(resposta == 'n');
	}
	

	/**************************************************
	 * Funcio que permetra escollir quin jugador esborrar
	 * i esborrar-lo, amb les mesures de seguretat adequades
	 **************************************************/
	static boolean esborrarJugador() {
		boolean status = true;
		
		String nomJugadorEliminar;
		System.out.print("\nQuin es el nom del detectiu que vols eliminar?\n>> ");
		nomJugadorEliminar = sc.next();
		
		List<String[]> llistaJugadors = carregarLlistaJugadors();
		int index = 0;
		boolean trobat = false;
		
		while(index < llistaJugadors.size() && !trobat) {
			if(llistaJugadors.get(index)[0].equals(nomJugadorEliminar)) trobat = true;
			else index++;
		}

		// El troba
		if(trobat) {
			String[] jugadorEliminar = llistaJugadors.get(index);
			System.out.println("\nDetectiu trobat: "+jugadorEliminar[0]+", "+jugadorEliminar[1]+" anys. "+jugadorEliminar[2]+" casos resolts, "+jugadorEliminar[3]+" punts obtinguts.");
			esperar(1);
			
			char segurEliminar = 's';
			
			// Confirmar si vol esborrar
			do {
				
				System.out.print("Segur que vols esborrar el registre de "+jugadorEliminar[0]+"? [s/n]\n>> ");
				segurEliminar = sc.next().charAt(0);
				
				if(segurEliminar != 's' && segurEliminar != 'n') {
					tirarError("Escriu 's' o 'n', si us plau.");
				}
				
				
			}while(segurEliminar != 's' && segurEliminar != 'n');
			
			// Si esta segur que vol eliminar al jugador
			if(segurEliminar == 's') {

				// Intentar esborrar-lo del fitxer, traient-lo de la llista i sobreescrivint l'arxiu
				try {
					llistaJugadors.remove(index);
					
					FileWriter fw = new FileWriter(FITXER_JUGADORS);
	
					for(int i = 0; i < llistaJugadors.size(); i++) {
						fw.write(llistaJugadors.get(i)[0]+"-"+llistaJugadors.get(i)[1]+"-"+llistaJugadors.get(i)[2]+"-"+llistaJugadors.get(i)[3]+"\n");
					}

					fw.close();
					
					System.out.println("\nDetectiu "+jugadorEliminar[0]+" expulsat de la VFD.");
					esperar(1);
				}catch(Exception e) {
					status = false;
				}
			
			// Si no, retornara al menu
			}else {
				System.out.println("\nRetornant al menu...");
				esperar(1);
			}
			
		// No troba a l'usuari
		}else {
			tirarError("No s'ha trobat aquest jugador.");
		}
		
		return status;
	}
	
	/**************************************************
	 * Funcio senzilla que retorna un String amb la
	 * primera lletra majuscules i la resta minuscules
	 **************************************************/
	static String initCap(String s) {
		return s.substring(0, 1).toUpperCase()+s.substring(1).toLowerCase();
	}
	
	/**************************************************
	 * Funcio que es cridara quan es vulgui registrar
	 * un nou usuari en lloc de carregar un del fitxer
	 **************************************************/
	static void registrarNouJugador(String nom){
		int edat = -1;
		do {
			
			System.out.print("\nQuina es la teva edat?\n>> ");
			edat = sc.nextInt();
			
			if(edat < 0) {
				tirarError("Posa una edat positiva, si us plau.");
			}else if(edat > 122) {
				tirarError("La persona registrada que mes anys ha viscut es deia Jeanne Calment\n"
						 + "i va morir als 122 anys i 164 dies. Dubto MOLT que tu tinguis mes.\n"
						 + "Torna-ho a intentar, si us plau.", 5);
			}
			
		}while(edat < 0 || edat > 122);
		
		jugadorActual = new Jugador(nom, edat);		
	}
	
	
	/**************************************************************
	 * Metode que intentara carregar el jugador amb el nom indicat
	 * al joc per tal que sigui el proper jugador actual.<br>
	 * Si no el troba, el metode retornara NULL <br>
	 * El fitxer te una fila per usuari, amb l'estructura:<br><br>
	 * <i style="text-align:center;">Nom-Edat-Casos resolts-Puntuacio total</i>
	 **************************************************************/
	static Jugador carregarJugador(String nomABuscar) {
		Jugador jugador = null;
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(FITXER_JUGADORS));
			String input;
			String[] inputDividit = new String[4];
			boolean aturar = false;
			
			
			while((input = br.readLine()) != null && !aturar) {
				inputDividit = input.split("-");
				
				if(inputDividit[0].equals(nomABuscar)) {
					jugador = new Jugador(inputDividit[0], Integer.valueOf(inputDividit[1]), Integer.valueOf(inputDividit[2]), Integer.valueOf(inputDividit[3]));
					aturar = true;
				}
				
			}
			
			br.close();
			
			return aturar ? jugador : null;
			
		}catch(Exception e) {
			return jugador;
		}
		
	}


	/**************************************************************
	 * Metode que intentara carregar tots els jugadors de l'arxiu
	 * en una llista dinàmica de tamany variable, i la retornara
	 **************************************************************/
	static List<String[]> carregarLlistaJugadors(){
		List<String[]> llistaJugadors = null;
		try {
			// Primer llegirem l'arxiu sencer per tal de no tocar els altres jugadors
			BufferedReader br = new BufferedReader(new FileReader(FITXER_JUGADORS));
			String linia;
			llistaJugadors = new ArrayList<>();
			
			// Ficar totes les dades dels jugadors a la llista
			while((linia = br.readLine()) != null) {
				llistaJugadors.add(linia.split("-"));
			}
			br.close();
		}catch(Exception e) {}
		
		return llistaJugadors;
	}
	
	/**************************************************************
	 * Metode que intentara guardar el jugador a l'arxiu sense
	 * tocar la resta de jugadors actuals.<br>
	 * El fitxer te una fila per usuari, amb l'estructura:<br><br>
	 * <i style="text-align:center;">Nom-Edat-Casos resolts-Puntuacio total</i>
	 **************************************************************/
	static boolean guardarJugadors() {
		try {
			
			List<String[]> llistaJugadors = carregarLlistaJugadors();
			
			// Com la llista es d'un vector d'Strings, necessitarem crear-ne un pel jugador actual
			String[] dadesJugadorActual = {jugadorActual.obtenirNom(), jugadorActual.obtenirEdat()+"", 
										   jugadorActual.obtenirCasosResolts()+"", jugadorActual.obtenirPuntuacio()+""};
			
			boolean trobat = false;
			int i = 0;
			
			// Buscarem per tota la llista si el jugador existia previament, per tal d'actualitzar les seves dades
			while(!trobat && i < llistaJugadors.size()) {
				if(llistaJugadors.get(i)[0].equals(jugadorActual.obtenirNom())) {
					trobat = true;
					llistaJugadors.set(i, dadesJugadorActual);
				}else {
					i++;
				}
			}
			
			// Si no s'ha trobat, vol dir que el jugador era nou.
			// Per tant, tocara simplement afegir-lo
			if(!trobat) llistaJugadors.add(dadesJugadorActual);
			
			// Un cop llegida i modificada la llista de jugadors, sobreescriure l'arxiu sencer
			// Poso aquesta supresswarning perque em deia que no tancava el filewriter*
			@SuppressWarnings("resource") 
			FileWriter fw = new FileWriter(FITXER_JUGADORS);

			for(i = 0; i < llistaJugadors.size(); i++) {
				fw.write(llistaJugadors.get(i)[0]+"-"+llistaJugadors.get(i)[1]+"-"+llistaJugadors.get(i)[2]+"-"+llistaJugadors.get(i)[3]+"\n");
			}

			fw.close(); //*quan literament el tanco aqui, 6 linies mes abaix
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	
	/************************************************
	 * Imprimeix per pantalla el menu inicial del joc
	 ************************************************/
	static void printMenuInicial() {
		System.out.print("\n|\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/|\n"
						 + " Benvingut/da, "+jugadorActual.obtenirNom()+"\n"
					 	 + "|/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\|\n\n"
						 + "1. Iniciar el joc (dificultat: "+dificultat+")\n"
						 + "2. Escollir dificultat\n"
						 + "3. Consultar detectius de la VFD\n"
						 + "4. Accedir als arxius d'hostes\n"
						 + "5. Guardar i sortir\n\n"
						 + ">> ");
	}

	
	/************************************************
	 * Tira un error per pantalla
	 ************************************************/
	public static void tirarError(String error) {
		System.out.println("\nERROR: "+error);
		esperar(1);
	}

	public static void tirarError(String error, int segons) {
		System.out.println("\nERROR: "+error);
		esperar(segons);
	}
	
	
	/************************************************
	 * Atura l'execucio del programa durant els segons indicats
	 ************************************************/
	public static void esperar(int segons) {
		try {
			
			Thread.sleep(segons*1000);
			
		}catch(Exception e) {}
	}

}
