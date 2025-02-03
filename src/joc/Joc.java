/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 27/05/2024
 * Fitxer: Joc.java
 * Descripcio: Classe que controlara tot el que estigui relacionat amb la partida.
 */
package joc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import hotel.Habitacio;
import hotel.Hoste;
import hotel.Hotel;
import hotel.Rol;
import main.Main;

public class Joc {
	
	/******************************************************************
	 * Aquest conjunt de variables controlaran el tema dels arxius
	 * relacionats relacionats amb els noms i cognoms de les persones.
	 ******************************************************************/
	public static final String FITXER_NOMS = "noms.txt";
	public static final String FITXER_COGNOMS = "cognoms.txt";
	public static List<String[]> llistaNoms = new ArrayList<>();
	public static List<String> llistaCognoms = new ArrayList<>();

	private Dificultat dificultat;
	private Scanner sc = new Scanner(System.in);
	private Calendar horaJocInici;	// NOTA: ambdues hores
	private Calendar horaJocActual;	//       son FICTICIES
	private Hotel hotel;
	private Hoste victima;
	private Hoste assassi;
	private boolean crimResolt;
	
	public Joc(Dificultat dif) {
		
		this.dificultat = dif;
		hotel = switch(dif) {
		
			case FACIL->{
				yield new Hotel((byte)2, (byte)6);
			}
		
			case MITJANA->{
				yield new Hotel((byte)3, (byte)8);
			}
		
			case DIFICIL->{
				yield new Hotel((byte)4, (byte)10);
			}
		
		};
		
	}

	
	/***********************************************************************************************************************
	 * El metode mes important de tot el programa. Controla tot el flux del joc i retorna un vector de dues posicions: <br>
	 * La <b>primera</b> posicio retorna la quantitat de punts. Fer les seguents activitats dona punts:<br>
	 * <ul>
	 * 	<li>Interrogar als sospitosos</li>
	 * 	<li>Resoldre correctament el crim</li>
	 * 	<li>Obtenir pistes</li>
	 * </ul>
	 *	A mes a mes, s'obtindran encara mes punts quan:
	 * <ul>
	 * 	<li>Es trigui menys temps en resoldre el crim</li>
	 * 	<li>Es resolgui el crim amb menys pistes</li>
	 * </ul>
	 * La <b>segona</b> posicio retorna si el crim ha estat resolt o no.
	 ***********************************************************************************************************************/
	public int[] iniciarJoc(String detectiu) {
		/*******************************************************
		 * S'intenten carregar els noms i cognoms personalitzats
		 *******************************************************/
		byte status = carregarNomsCognoms();
		switch(status) {
			case 2->{
				System.out.println("\n[INFO]     No s'han pogut carregar els noms dels arxius.\n"
								   + "           Es jugara amb els noms predeterminats.");
			}
			case 1->{
				System.out.println("\n[INFO]     No s'han pogut carregar els cognoms dels arxius.\n"
								   + "           Es jugara amb els cognoms predeterminats.");
			}
			case 0->{
				System.out.println("\n[INFO]     No s'han pogut carregar ni els noms ni els cognoms.\n"
								   + "           Tocara jugar amb la configuracio per default.");
			}
			default->{
				System.out.println("\n[INFO]     Tot ha carregat correctament! Bona sort, detectiu.");
			}
		}
		Main.esperar(1);
		System.out.println("[RECORDA]: Nomes menteix qui te quelcom a amagar.\n");
		Main.esperar(2);

		/*******************************************************
		 * Es criden tots els metodes per a preparar el joc en si
		 *******************************************************/
		prepararHostes();
		prepararCrim();

		// Es marca una hora de inici, a les 2 AM clavades.
		horaJocInici = Calendar.getInstance();
		horaJocInici.set(Calendar.HOUR_OF_DAY, 2);
		horaJocInici.set(Calendar.MINUTE, 0);
		horaJocInici.set(Calendar.SECOND, 0);
		
		// I una hora actual, que sera la del joc (el dialeg inicial comenca 2 minuts abans)
		horaJocActual = Calendar.getInstance();
		horaJocActual.set(Calendar.HOUR_OF_DAY, 1);
		horaJocActual.set(Calendar.MINUTE, 58);
		horaJocActual.set(Calendar.SECOND, 0);
		
		/*********************
		 * I COMENCA EL JOC!!
		 *********************/
		
		crimResolt = false;
		
		String DETECTIU = detectiu.toUpperCase();
		int[] puntuacioFinal = {0, 0}; // [Numero punts, cas resolt] 
		int plantaActual = 0;
		int opcio = 0;
		
		char saltarDialegInicial = 's';
		
		do {
			
			System.out.print("Vols SALTAR-TE el dialeg introductori? [s/n]\n>> ");
			saltarDialegInicial = sc.next().charAt(0);
			
			if(saltarDialegInicial != 's' && saltarDialegInicial != 'n') {
				System.out.println("\nERROR: Escriu 's' o 'n', si us plau.");
			}
			
		}while(saltarDialegInicial != 's' && saltarDialegInicial != 'n');
		
		if(saltarDialegInicial == 'n') {

			
			intervencio("NARRADOR", "(Esta plovent a bots i barrals. Entres a l'hotel i eixuges el paraigues).", 3);
			intervencio("NARRADOR", "(A dins t'espera una dona d'uns cincuanta anys, recta i seriosa, amb el cabell curt i algunes canes).", 3);
			intervencio("RECEPCIONISTA", "Gracies per venir, detectiu "+detectiu+". La gent es comencava a posar molt tensa.", 3);
			intervencio(DETECTIU, "Es el meu treball. Qui es la victima?", 2);
			intervencio("RECEPCIONISTA", victima.obtenirNomComplet()+". "+(victima.obtenirGenere().equals("home")? "Un home" : "Una dona")+" de "+victima.obtenirEdat()+" anys, de pell "+victima.obtenirColorPell()+" i de "+victima.obtenirAlsada()+" metres d'alcada amb "+victima.obtenirPes()+"kg de pes.", 5);
			
			if(victima.obtenirAlsada() > 1.90) {
				intervencio(DETECTIU, (victima.obtenirGenere().equals("home")?"Un gegant" : "Una geganta")+". Em pregunto com han aconseguit matar-l"+(victima.obtenirGenere().equals("home")? "o" : "a")+".");
				intervencio("RECEPCIONISTA", "...", 2);
			}else if(victima.obtenirAlsada() < 1.60) {
				intervencio(DETECTIU, victima.obtenirAlsada()+" metres d'alcada nomes? Com "+(victima.obtenirGenere().equals("home")?"l'" : "la ")+"heu trobat? Amb una lupa?",2);
				intervencio("RECEPCIONISTA", "...", 2);
			}else if(victima.obtenirColorPell().equals("negra") || victima.obtenirColorPell().equals("morena")) {
				intervencio(DETECTIU, "Pell "+victima.obtenirColorPell()+", eh? S'ha descartat un crim d'odi racial?");
				intervencio("RECEPCIONISTA", "La policia no s'ha volgut mullar.");
				intervencio(DETECTIU, "Com no... Son una colla de figaflors, eh?");
				intervencio("RECEPCIONISTA", "...", 2);
			}else if(victima.obtenirPes() > 90) {
				intervencio(DETECTIU, victima.obtenirPes()+"kg? Deixi'm endevinar, en una habitacio amb llit king-sized?", 3);
				intervencio("RECEPCIONISTA", "...", 2);
			}else {
				intervencio(DETECTIU, victima.obtenirNomComplet()+"? Jo tenia una exparella que es deia aixi. Tant de bo sigui "+(victima.obtenirGenere().equals("home")? "ell" : "ella")+"...", 2);
				intervencio("RECEPCIONISTA", "...", 2);
			}
			horaJocActual.add(Calendar.MINUTE, 1);
			intervencio(DETECTIU, "Era per alleujar una mica les tensions. A on la han trobat?", 2);
			intervencio("RECEPCIONISTA", "A l'habitacio numero "+victima.obtenirHabitacio().obtenirID()+", planta "+victima.obtenirHabitacio().obtenirNumeroPlanta()+".", 3);
			intervencio(DETECTIU, "Segueix alla, oi?", 1);
			intervencio("RECEPCIONISTA", "Si. La policia no vol tocar res fins que l'hagi evaluat un forense.", 3);
			intervencio(DETECTIU, "I estem segurs que hi son tots els sospitosos?", 2);
			intervencio("RECEPCIONISTA", "Correcte. Els "+hotel.obtenirQuantitatGentAllotjada()+" hostes que es trobaven allotjats al moment de l'assassinat son aqui... Be, "+(hotel.obtenirQuantitatGentAllotjada()-1)+", sense comptar la victima.", 5);
			intervencio(DETECTIU, "D'acord. Anem per feina, doncs.", 2);
			intervencio("RECEPCIONISTA", "Gracies de nou, "+detectiu+". Tingui aquesta llibreta. Conte la llista d'hostes i unes pagines en blanc per si vol apuntar pistes. Li fara servei.", 5);
			intervencio("NARRADOR", "(La senyora et dona una llibreta amb una llista de les habitacions i els seus corresponents hostes).", 3);
			
			horaJocActual.add(Calendar.MINUTE, 1);
		}else {
			intervencio("RECEPCIONISTA", "La victima es diu "+victima.obtenirNomComplet()+". "+(victima.obtenirGenere().equals("home")? "Un home" : "Una dona")+" de "+victima.obtenirEdat()+" anys, de pell "+victima.obtenirColorPell()+" i de "+victima.obtenirAlsada()+" metres d'alcada amb "+victima.obtenirPes()+"kg de pes.", 3);
			intervencio(DETECTIU, "Anem per feina, doncs.");
			intervencio("RECEPCIONISTA", "Es troba a l'habitacio numero "+victima.obtenirHabitacio().obtenirID()+", planta "+victima.obtenirHabitacio().obtenirNumeroPlanta()+". Tingui aquesta llibreta.", 2);
			horaJocActual.add(Calendar.MINUTE, 2);
		}

		Llibreta llibreta = new Llibreta(hotel, victima);
		int horaReferencia = horaJocActual.get(Calendar.HOUR);
				
		while(!crimResolt && opcio != 6) {
			
			// Augmentar el nivell d'estres general a cada hora que passi
			if(horaJocActual.get(Calendar.HOUR) > horaReferencia) {
				hotel.augmentarNivellEstresGeneral(1);
				horaReferencia = horaJocActual.get(Calendar.HOUR);
			}
			
			if(opcio != 1) {
				System.out.println();
				hotel.imprimirPlanta(plantaActual);
				Main.esperar(1);
			}
			
			intervencio(DETECTIU, "Que faig, ara?", 1);
			System.out.print("\n1. Veure el mapa de l'hotel\n"
							 + "2. Entrar a una habitacio\n"
							 + "3. Canviar de planta\n"
							 + "4. Obrir llibreta de pistes\n"
							 + "5. Inculpar\n"
							 + "6. Sortir del joc\n\n"
							 + ">> ");
			opcio = sc.nextInt();
			
			switch(opcio) {
				
				// 1. Veure el mapa de la planta actual
				case 1->{
					System.out.println("\n--------------------------\n"
									   + "MAPA DE L'HOTEL DESENLLAC\n"
									   + "--------------------------");
					for(int i = 0; i < hotel.numeroPlantes(); i++) {
						hotel.imprimirPlanta(i);
					}
					System.out.print("Escriu qualsevol cosa per a continuar: ");
					sc.next();
					
					horaJocActual.add(Calendar.MINUTE, 1);
				}
				
				// 2. Entrar a una habitacio
				case 2->{
					String IDhabitacio;
					System.out.println();
					intervencio(DETECTIU, "A quina habitacio hauria d'entrar...?", 0);
					System.out.print(">> ");
					IDhabitacio = sc.next();
					
					if(hotel.obtenirHabitacioPerID(IDhabitacio) == null) {
						intervencio(DETECTIU, "Tinc menys neurones que cabells, aquesta habitacio no existeix...");
					}else {
						Habitacio habitacio = hotel.obtenirHabitacioPerID(IDhabitacio);
						if(habitacio.obtenirNumeroPlanta() != plantaActual+1) {
							intervencio(DETECTIU, "No, per a entrar en aquesta habitacio hauria d'anar a la planta "+habitacio.obtenirNumeroPlanta());
						}else {
							if(habitacio.numeroHostesAllotjats() == 0) {
								intervencio("NARRADOR", "(Truques a la porta i obres. Com ja sabies, aqui no hi ha ningu).");
								intervencio("NARRADOR", "(Tanques la porta humiliadament, mirant que ningu no t'hagi vist).");
								horaJocActual.add(Calendar.MINUTE, 1);
							}else {
								if(habitacio.obtenirID().equals(victima.obtenirHabitacio().obtenirID()) && habitacio.numeroHostesAllotjats() == 1) {
									intervencio("NARRADOR", "(Truques a la porta i obres. Et trobes amb el cadaver solitari de"+(victima.obtenirGenere().equals("home")? "l" : " la")+" "+victima.obtenirNomComplet()+").", 3);
									intervencio("NARRADOR", "(Tanques la porta, ja que dubtes seriosament que et pugi dir alguna cosa de valor).", 3);
									horaJocActual.add(Calendar.MINUTE, 1);
								}else {
									intervencio("NARRADOR", "(Truques a la porta i obres. T'esper"+(habitacio.numeroHostesAllotjats() == 1 ? "a" : "en")+" "+habitacio.numeroHostesAllotjats()+" person"+(habitacio.numeroHostesAllotjats() == 1 ? "a" : "es")+" a dins).");
									if(habitacio.obtenirID().equals(victima.obtenirHabitacio().obtenirID())) {
										intervencio(DETECTIU, "(Semblen tenses. Potser perque tenen un cadaver palid i fred al costat?).", 3);
									}
									
									if(habitacio.hasEntratAnteriorment()) {
										intervencio(DETECTIU, "Soc jo altre cop. Necessito parlar una mica mes amb "+(habitacio.numeroHostesAllotjats()==1 ? "tu" : "vosaltres")+".");
									}else {
										habitacio.marcarEntrar();
										intervencio(DETECTIU, "Soc el detectiu "+detectiu+". Vinc a "+(habitacio.numeroHostesAllotjats()==1 ? "fer-li" : "fer-los")+" unes preguntes.");
										for(int i = 0; i < habitacio.obtenirLlistaHostes().size(); i++) {
											if(habitacio.obtenirLlistaHostes().get(i).obtenirNomComplet().equals(victima.obtenirNomComplet())) {
												intervencio(victima.obtenirNOMCOMPLET(), "(No diu res, ja que esta mort"+(victima.obtenirGenere().equals("home")? "" : "a")+").");
											}else intervencio("HOSTE "+(i+1), habitacio.obtenirLlistaHostes().get(i).saludar(detectiu));
										}
									}

									horaJocActual.add(Calendar.MINUTE, 1);
									byte opcioHabitacio = 0;
									do {
										/*
										 * NOTA: Nomes apareixeran els noms dels hostes un
										 * cop haguem parlat amb ells i s'hagin presentat.
										 * Si algu no s'ha presentat, apareixera com HOSTE (numero)
										 */
										System.out.println("\n");
										intervencio(DETECTIU, "Amb qui hauria de parlar?", 0);
										System.out.println("[HABITACIO "+habitacio.obtenirID()+"]");
										for(int i = 0; i < habitacio.obtenirLlistaHostes().size(); i++){
											System.out.println((i+1)+". "+(habitacio.obtenirLlistaHostes().get(i).shaPresentat() || habitacio.obtenirLlistaHostes().get(i).obtenirRol() == Rol.VICTIMA ? habitacio.obtenirLlistaHostes().get(i).obtenirNOMCOMPLET() : "HOSTE "+(i+1)));
										}
										System.out.println((habitacio.numeroHostesAllotjats()+1)+". Amb ningu, sortir");
										System.out.print(">> ");
										opcioHabitacio = sc.nextByte();
										
										if(opcioHabitacio < 1 || opcioHabitacio > habitacio.numeroHostesAllotjats()+1) {
											
											intervencio(DETECTIU, "No, no, concentra't, "+detectiu+"...");
											
										}else if(opcioHabitacio != habitacio.numeroHostesAllotjats()+1) {
											Hoste interrogat = habitacio.obtenirLlistaHostes().get(opcioHabitacio-1);

											System.out.println("");
											if(interrogat.obtenirNomComplet().equals(victima.obtenirNomComplet())){
												intervencio(DETECTIU, "No crec que la victima tingui moltes ganes de parlar...");
											}else {
												if(interrogat.hasParlatAnteriorment()) {
													intervencio("NARRADOR", "T'emportes un altre cop a"+(interrogat.obtenirGenere().equals("home")? "l" : " la")+" "+(interrogat.shaPresentat() ? interrogat.obtenirNomComplet() : "HOSTE "+opcioHabitacio)+".");
													intervencio(DETECTIU, "Necessito parlar amb voste un altre cop");
												}else {
													intervencio("NARRADOR", "T'emportes a"+(interrogat.obtenirGenere().equals("home")? "l" : " la")+" "+(interrogat.shaPresentat() ? interrogat.obtenirNomComplet() : "HOSTE "+opcioHabitacio)+" a parlar en privat.");
													intervencio(DETECTIU, "Li vull fer un parell de preguntes...");
												}

												byte opcioHoste = 0;
												
												do {
													System.out.println();
													intervencio("NARRADOR","Que li preguntes a"+(interrogat.obtenirGenere().equals("home")? "l" : " la")+" "+
																								 	(interrogat.shaPresentat() ? interrogat.obtenirNomComplet() : "HOSTE "+opcioHabitacio)+"?", 0);
													System.out.print("\n1. (Observar-li)\n"
																   + "2. El nom\n"
																   + "3. El que feia a l'hora del crim\n"
																   + "4. Si ha estat l'assassi\n"
																   + "5. Si ens pot dir alguna cosa que serveixi de pista\n"
																   + "6. Res, sortir\n\n"
																   + ">> ");
													opcioHoste = sc.nextByte();
													
													switch(opcioHoste) {
													
														// S'observen els detalls de l'hoste
														case 1->{
															intervencio("NARRADOR", "(Observes a la persona que tens davant)", 1);
															intervencio("NARRADOR", "(Es "+(interrogat.obtenirGenere().equals("home")? "un home" : "una dona")+" d'uns "+interrogat.obtenirEdatAproximada()+" anys. "
																				  + "Te la pell "+interrogat.obtenirColorPell()+", el cabell "+interrogat.obtenirColorCabell()+" i els ulls "+interrogat.obtenirColorUlls()+". "
																				  + "Fara uns "+interrogat.obtenirAlsadaAproximada()+"m d'alcada i pesara uns "+interrogat.obtenirPesAproximat()+"kg).", 4);
															// Marcar que l'has observat
															if(!interrogat.hasObservat()) interrogat.marcarObservat(); 
															horaJocActual.add(Calendar.MINUTE, 1);
														}
													
														// Se li pregunta el nom
														case 2->{
															if(interrogat.shaPresentat()) {
																intervencio(DETECTIU, "Repeteixi-me el seu nom i edat, si es tan amable.");
															}else {
																intervencio(DETECTIU, "Digui'm el seu nom i edat, si us plau.");
																puntuacioFinal[0] += 50;
															}
															
															intervencio(interrogat.obtenirNOMCOMPLET(), interrogat.presentarse(), 2);
															horaJocActual.add(Calendar.MINUTE, 1);
														}
													
														// Que feia a l'hora del crim
														case 3->{
															if(interrogat.haExplicatLaCoartada()) {
																intervencio(DETECTIU, "Que ha dit que feia a l'hora del crim?");
															}else {
																intervencio(DETECTIU, "Que estava fent quan la victima va estar assassinada?");
																puntuacioFinal[0] += 50;
															}
															
															intervencio((interrogat.shaPresentat() ? interrogat.obtenirNOMCOMPLET() : "HOSTE "+opcioHabitacio), interrogat.dirCoartada(), 3);
															
															if(!interrogat.haExplicatLaCoartada()) {
																interrogat.marcarHaExplicatCoartada();
																if(interrogat.obtenirTipusCoartada() == Rol.TESTIMONI) {
																	intervencio(DETECTIU, "En altres paraules, es trobava a l'hotel.");
																	intervencio((interrogat.shaPresentat() ? interrogat.obtenirNOMCOMPLET() : "HOSTE "+opcioHabitacio), "Si.");
																}else {
																	intervencio(DETECTIU, "O sigui, voste no hi era present a l'hotel l'hora del crim.");
																	intervencio((interrogat.shaPresentat() ? interrogat.obtenirNOMCOMPLET() : "HOSTE "+opcioHabitacio), "Exacte.");
																}
															}
															horaJocActual.add(Calendar.MINUTE, 1);
														}

														// Es prova d'acusar-lo
														case 4->{
															if(interrogat.haEstatAcusat()) {
																intervencio(DETECTIU, "Esta segur que no ha estat voste l'assasi"+(victima.obtenirGenere().equals("home")? "" : "na")+"?");
															}else {
																intervencio(DETECTIU, "Ha assassinat voste a"+(victima.obtenirGenere().equals("home")? "l" : " la")+" "+victima.obtenirNom()+"?");
																puntuacioFinal[0] += 50;
															}
															
															intervencio((interrogat.shaPresentat() ? interrogat.obtenirNOMCOMPLET() : "HOSTE "+opcioHabitacio), interrogat.respostaAcusacio(), 3);
															horaJocActual.add(Calendar.MINUTE, 1);
														}

														// Es prova si et pot donar alguna pista
														case 5->{
															if(interrogat.seLiHaDemanatPista()) {
																intervencio(DETECTIU, "Esta segur de la informacio sobre el crim que m'ha dit?");
															}else {
																intervencio(DETECTIU, "Hi ha alguna pista sobre l'assassi que em pugui donar?");
															}
															
															intervencio((interrogat.shaPresentat() ? interrogat.obtenirNOMCOMPLET() : "HOSTE "+opcioHabitacio), interrogat.donarPista(assassi), 2);
															
															// Informacio nova
															if(!interrogat.seLiHaDemanatPista()) {
																// No ens diu res
																if(interrogat.obtenirPistaDita()[2].equals("res")) {
																	intervencio(DETECTIU, "Entesos. Gracies de tota manera.",1);
																// Ens dona un rumor
																}else if(interrogat.obtenirPistaDita()[2].equals("rumor")) {
																	intervencio(DETECTIU, "Un rumor... Prenc nota, tot i aixi. Moltes gracies.");
																	llibreta.afegirPista(interrogat, interrogat.obtenirPistaDita()[0], interrogat.obtenirPistaDita()[1], interrogat.obtenirPistaDita()[2]);
																// Ens dona una pista real.
																}else {
																	intervencio(DETECTIU, "Interessant. Ho anotare a la meva llibreta. Moltes gracies.");
																	llibreta.afegirPista(interrogat, interrogat.obtenirPistaDita()[0], interrogat.obtenirPistaDita()[1], interrogat.obtenirPistaDita()[2]);
																}
																
																puntuacioFinal[0] += 200;
																interrogat.marcarHaDonatPista();
															}
															
															horaJocActual.add(Calendar.MINUTE, 2);
														}
													
														// Sortir
														case 6->{
															System.out.println();
															intervencio(DETECTIU, "D'acord, ja estariem. Moltes gracies"+(interrogat.shaPresentat() ? ", "+interrogat.obtenirNom() : "")+".");
															horaJocActual.add(Calendar.MINUTE, 1);
														}
														
														default ->{
															intervencio("NARRADOR", "(No entens com el jugador que et controla es tan tocapilotes).");
														}
													}
												
												}while(opcioHoste != 6);
											}
											
										}
										
									}while(opcioHabitacio != habitacio.numeroHostesAllotjats()+1);
								}
							}
						}
					}
				}
				
				// 3. Canviar de planta
				case 3->{
					byte plantaCanviar = 0;
					do {
						
						intervencio(DETECTIU, "A quina planta hauria d'anar...? [1 - "+hotel.numeroPlantes()+"]", 0);
						System.out.print(">> ");
						plantaCanviar = (byte)(sc.nextByte()-1);
						
						if(plantaCanviar < 0 || plantaCanviar > hotel.numeroPlantes()) {
							intervencio(DETECTIU, "Sere imbecil? Aquesta planta no existeix...");
						}
						
					}while(plantaCanviar < 0 || plantaCanviar > hotel.numeroPlantes());
					
					if(plantaCanviar == plantaActual) intervencio(DETECTIU, "Mira que soc tap de bassa, ja estic a la planta +"+plantaActual+"...");
					else {
						// Augmenta el temps segons les plantes que haguem hagut de moure'ns
						horaJocActual.add(Calendar.MINUTE, 2 * Math.abs(plantaActual - plantaCanviar));
						plantaActual = plantaCanviar;
					}
				}
				
				// 4. Revisar pistes
				case 4->{
					byte opcioLlibreta = 0;
					
					do {
						
						System.out.print("\nApartats de la llibreta:\n"
										 + "1. Informacio d'habitacions i hostes\n"
										 + "2. Dades sobre la victima\n"
										 + "3. Pistes de l'assassi\n"
										 + "4. Tancar-la\n\n"
										 + ">> ");
						
						opcioLlibreta = sc.nextByte();
						
						switch(opcioLlibreta) {
							// 1. Llista d'habitacions i hostes
							case 1->{
								
								llibreta.imprimirLlistaHostes();

								System.out.print("\nEscriu qualsevol cosa per a continuar: ");
								sc.next();
								horaJocActual.add(Calendar.MINUTE, 2);
							}

							// 2. Dades sobre la vitcima
							case 2->{
								String dataMort = horaJocInici.get(Calendar.DAY_OF_MONTH)+" de "+switch(horaJocInici.get(Calendar.MONTH)) {
									case 0->{
										yield "Gener";
									}
									case 1->{
										yield "Febrer";
									}
									case 2->{
										yield "Marc";
									}
									case 3->{
										yield "Abril";
									}
									case 4->{
										yield "Maig";
									}
									case 5->{
										yield "Juny";
									}
									case 6->{
										yield "Juliol";
									}
									case 7->{
										yield "Agost";
									}
									case 8->{
										yield "Setembre";
									}
									case 9->{
										yield "Octubre";
									}
									case 10->{
										yield "Novembre";
									}
									default ->{
										yield "Desembre";
									}
								}+", a les 0:00.";
								llibreta.imprimirPistesVictima(dataMort);
								
								System.out.print("\nEscriu qualsevol cosa per a continuar: ");
								sc.next();
								
								horaJocActual.add(Calendar.MINUTE, 1);
							}

							// 3. Pistes de l'assassi
							case 3->{
								llibreta.imprimirPistesAssassi();
								
								System.out.print("\nEscriu qualsevol cosa per a continuar: ");
								sc.next();
								horaJocActual.add(Calendar.MINUTE, 2);
							}
							
							// Tancar llibreta
							case 4->{}
							
							default->{
								intervencio(DETECTIU, "No, no hi ha opcio "+opcioLlibreta+"...");
							}
						}
						
					}while(opcioLlibreta != 4);
				}
				
				// 5. Inculpar a algu
				case 5->{
					if(plantaActual != 0) intervencio(DETECTIU, "He de baixar a la planta 1 per comunicar-li-ho a la recepcionista.");
					else {
						String nom, cognom, nomComplet;
						System.out.println();
						intervencio(DETECTIU, "El tinc, senyora.",1);
						intervencio("RECEPCIONISTA", "A l'assassi?! L'ha enxampat?! Qui ha estat?!");
						intervencio(DETECTIU, "L'assassi es diu de nom...",1);
						System.out.print(">> ");
						nom = sc.next();
						intervencio(DETECTIU, "... i de cognom...",1);
						System.out.print(">> ");
						cognom = sc.next();
						
						nomComplet = nom.substring(0, 1).toUpperCase()+nom.substring(1).toLowerCase()+" "+cognom.substring(0, 1).toUpperCase()+cognom.substring(1).toLowerCase();
						
						if(hotel.obtenirHostePerNom(nomComplet) != null) {
							Hoste acusat = hotel.obtenirHostePerNom(nomComplet);
	
							intervencio(DETECTIU, nomComplet+". "+(acusat.obtenirGenere().equals("home")? "El" : "La")+" "+acusat.obtenirNom()+" es l'assassi"+(acusat.obtenirGenere().equals("home") ? "" : "na" )+".");
							intervencio("RECEPCIONISTA", (acusat.obtenirGenere().equals("home")? "L'" : "La")+" "+acusat.obtenirGenere()+" de cabell "+acusat.obtenirColorCabell()+" i d'ulls "+acusat.obtenirColorUlls()+" de la "+acusat.obtenirHabitacio().obtenirID()+"?!");
							intervencio(DETECTIU, "Correcte.");
							
							// Si s'acusa a la victima
							if(acusat.obtenirNomComplet().equals(victima.obtenirNomComplet())) {
								intervencio("RECEPCIONISTA", "Pero voste es tonto o es menja els mocs?? "+(acusat.obtenirGenere().equals("home")? "El" : "La")+" "+acusat.obtenirNom()+" es la victima!!", 3);
								intervencio(DETECTIU, "Ups...", 2);
								intervencio("RECEPCIONISTA", "(et colpeja amb un paper de diari enrotllat tan fort que perds el coneixement mitja hora)", 3);
								horaJocActual.add(Calendar.MINUTE, 30);
							}else {
								/*
								 * ES TRUCA A LA POLICIA I S'ARRESTA A QUI HA ESTAT ACUSAT
								 */
								boolean encertat = acusat.obtenirNomComplet().equals(assassi.obtenirNomComplet());
								intervencio("RECEPCIONISTA", "Trucare a la policia immediatament!", 2);
								horaJocActual.add(Calendar.MINUTE, 30);
								intervencio("NARRADOR", "(La policia arriba i arresten a"+(acusat.obtenirGenere().equals("home")? "l" : " la")+" "+acusat.obtenirNom()+")", 2);
								intervencio(acusat.obtenirNOMCOMPLET(), acusat.reaccionarArrestFinal(detectiu, encertat), 3);
								intervencio("POLICIA 1", "Tira cap aqui, hostia!");
								intervencio("NARRADOR", "(El policia "+(acusat.obtenirGenere().equals("home")? "el" : "la")+" fa entrar al cotxe, tanca la porta amb un cop sec, i se "+(acusat.obtenirGenere().equals("home")? "l'" : "la ")+"emporten a comissaria).", 4);
								intervencio("POLICIA 2", "Gracies pel seu treball, detectiu "+detectiu+".");
								intervencio(DETECTIU, "Un plaer. Es la meva vocacio.");
								
								// Si la has cagat
								if(!encertat) {
									hotel.obtenirHabitacioPerID(acusat.obtenirHabitacio().obtenirID()).obtenirLlistaHostes().remove(acusat);
									hotel.obtenirHabitacioPerID(victima.obtenirHabitacio().obtenirID()).obtenirLlistaHostes().remove(victima);
									horaJocActual.add(Calendar.DAY_OF_MONTH, 2);
									horaJocActual.set(Calendar.HOUR_OF_DAY, 1);
									horaJocActual.set(Calendar.MINUTE, 0);
									System.out.println();
									intervencio("NARRADOR", "(Passen dos dies, i reps una trucada)");
									intervencio("MEMBRE DE LA VFD", DETECTIU+"!! TROS DE QUONIAM!! DESVIRGAGALLINES!! TAP DE BASSA!! PER QUE COLLONS LI PAGUEM?!", 4);
									intervencio(DETECTIU, "Com...?");
									intervencio("MEMBRE DE LA VFD", "La ha cagat!! "+(acusat.obtenirGenere().equals("home")? "El" : "La")+" "+acusat.obtenirNomComplet()+" no era l'assassi"+(acusat.obtenirGenere().equals("home") ? "" : "na" )+"!!");
									intervencio(DETECTIU, "COM DIU...?!");
									intervencio("MEMBRE DE LA VFD", "Hem tornat a citar als hostes a l'Hotel Desenllac!! Torni a anar i, aquest cop, asseguri-se'n de trobar a l'assassi, o l'acomiadarem!", 3);
									intervencio("NARRADOR", "(Et vesteixes rapidament i en una hora hi tornes a ser a l'hotel).");
									intervencio("NARRADOR", "(Aquest cop, sense "+(acusat.obtenirGenere().equals("home")? "el" : "la")+" "+acusat.obtenirNomComplet()+" present, ni tampoc la victima).");
									hotel.augmentarNivellEstresGeneral(40); // S'estressen bastant
									horaJocActual.add(Calendar.HOUR_OF_DAY, 1);
								}else {
									crimResolt = true;
								}
							}
						}else {
							intervencio("RECEPCIONISTA", nomComplet+"? Que borda, voste? No tenim a ningu a l'hotel amb aquest nom.", 3);
							intervencio(DETECTIU, "Si, disculpi, m'haure confos...");
							intervencio("RECEPCIONISTA", "(et colpeja amb un paper de diari enrotllat diversos cops)", 3);
							horaJocActual.add(Calendar.MINUTE, 5);
						}
					
					}
				}
				
				// Sortir del joc
				case 6->{
					char segur = 'n';
					do {
						
						System.out.print("\nEstas segur que vols sortir del joc? [s/n]\n>> ");
						segur = sc.next().charAt(0);
						
						if(segur != 's' && segur != 'n') {
							Main.tirarError("Escriu 's' o 'n', si us plau");
						}
					}while(segur != 's' && segur != 'n');
					
					if(segur == 'n') opcio = 0;
					else {
						System.out.println("");
						intervencio("RECEPCIONISTA", "Ei!! On va, "+detectiu+"?? Torni cap aqui!!");
						intervencio(DETECTIU, "Toco el dos, Smokey!!");
					}
				}
				
				// DEBUGGING: Obtenir el nom i la habitacio de l'assassi
				case 143->{
					System.out.println(assassi.obtenirNomComplet()+" "+assassi.obtenirHabitacio().obtenirID());
				}
				
				default->{
					intervencio(DETECTIU, "No, aquesta no es una opcio...");
				}
			}
			
		}
		
		if(crimResolt) {
			/*
			 * Aqui es fa el calcul final de la puntuacio extra.
			 * Quant mes temps s'hagi tardat en resoldre el crim, menys punts es tindran.
			 * La dificultat del joc fa de multiplicador.
			 */
			Calendar tempsFinal = Calendar.getInstance();
			tempsFinal.set(Calendar.MONTH, horaJocActual.get(Calendar.MONTH));
			tempsFinal.set(Calendar.DAY_OF_MONTH, horaJocActual.get(Calendar.DAY_OF_MONTH));
			tempsFinal.set(Calendar.HOUR_OF_DAY, horaJocActual.get(Calendar.HOUR_OF_DAY));
			tempsFinal.set(Calendar.MINUTE, horaJocActual.get(Calendar.MINUTE)-30);
			
			int mesFinal = horaJocInici.get(Calendar.MONTH);
			int diaFinal = horaJocInici.get(Calendar.DAY_OF_MONTH);
			int horaFinal = horaJocInici.get(Calendar.HOUR_OF_DAY);
			int minutFinal = horaJocInici.get(Calendar.MINUTE);
			
			tempsFinal.add(Calendar.MONTH, -mesFinal);
			tempsFinal.add(Calendar.DAY_OF_MONTH, -diaFinal+1);
			tempsFinal.add(Calendar.HOUR_OF_DAY, -horaFinal);
			tempsFinal.add(Calendar.MINUTE, -minutFinal);
			
			// Augmentar la quantitat de punts en funcio de la dificultat
			int multiplicador = switch(dificultat) {
				case FACIL->{
					yield 1;
				}
				case MITJANA->{
					yield 2;
				}	
				case DIFICIL->{
					yield 4;
				}
			};
			
			// Si tardes més d'un mes, no et mereixes punts
			if(tempsFinal.get(Calendar.MONTH) == 0) {
				puntuacioFinal[0] += 10000 - (2500 * (tempsFinal.get(Calendar.DAY_OF_MONTH)-1)) - (100 * tempsFinal.get(Calendar.HOUR_OF_DAY)) - (5 * tempsFinal.get(Calendar.MINUTE));
				puntuacioFinal[0] *= multiplicador;
			}
			
			
			puntuacioFinal[1] = 1;
		}
		
		return puntuacioFinal;
	}

	/******************************************
	 * Metode que retorna un String que mostra
	 * el dia i la hora de forma mes estetica
	 ******************************************/
	private String mostrarHora(Calendar c) {
		int dia = c.get(Calendar.DAY_OF_MONTH);
		int mes = c.get(Calendar.MONTH)+1;
		int hora = c.get(Calendar.HOUR_OF_DAY);
		int minut = c.get(Calendar.MINUTE);
		
		return "["+dia+"/"+mes+", "+(hora < 10 ? "0"+hora : hora)+":"+(minut < 10 ? "0"+minut : minut)+"]";
	}
	
	/*****************************************************
	 * Metodes simples que permeten parlar a un personatge
	 *****************************************************/
	private void intervencio(String personatge, String text) {
		System.out.println(mostrarHora(horaJocActual)+" "+personatge+": "+text);
		Main.esperar(2);
	}
	private void intervencio(String personatge, String text, int temps) {
		System.out.println(mostrarHora(horaJocActual)+" "+personatge+": "+text);
		Main.esperar(temps);
	}
	
	/************************************************************
	 * Metode que prepara tota l'escena del crim, escollint
	 * una victima, un assassi i els testimonis entre els hostes
	 ************************************************************/
	private void prepararCrim() {
		/*
		 *  S'obte un hoste aleatori dins de la llista de sospitosos
		 *  com a assassi, i un altre com a victima. Despres, es
		 *  declaren a un 20% dels hostes com a testimonis
		 */
		Random r = new Random();
		Hoste assassi, victima;
		
		// Assegurant, obviament, que no son la mateixa persona
		do {
			
			assassi = hotel.llistaSospitosos().get(r.nextInt(hotel.llistaSospitosos().size()));
			victima = hotel.llistaSospitosos().get(r.nextInt(hotel.llistaSospitosos().size()));
		
		}while(assassi.equals(victima));
		
		// Li canviem els seus respectius status
		hotel.obtenirHabitacioPerID(assassi.obtenirHabitacio().obtenirID()).obtenirHoste(assassi.obtenirNomComplet()).posarRol(Rol.ASSASSI);
		hotel.obtenirHabitacioPerID(victima.obtenirHabitacio().obtenirID()).obtenirHoste(victima.obtenirNomComplet()).posarRol(Rol.VICTIMA);
		
		// Les fiquem com a variables locals (ens facilitara molt la feina)
		this.assassi = assassi;
		this.victima = victima;
		
		// I es declaren testimonis a un 20% dels hostes
		for(int i = 0; i < (hotel.obtenirQuantitatGentAllotjada()*20/100); i++) {
			int index = 0;
			
			// Obtenim l'index d'un hoste de la lliste, assegurant-nos que es un HOSTE (es a dir, ni TESTIMONI, ni VICTIMA ni ASSASSI)
			do{
				index = r.nextInt(hotel.llistaSospitosos().size());
			}while(hotel.llistaSospitosos().get(index).obtenirRol() != Rol.HOSTE);
			
			Hoste nouTestimoni = hotel.llistaSospitosos().get(index);
			hotel.obtenirHabitacioPerID(nouTestimoni.obtenirHabitacio().obtenirID()).obtenirHoste(nouTestimoni.obtenirNomComplet()).posarRol(Rol.TESTIMONI);
		}
	}
	
	
	/*******************************************************
	 * Es preparara l'hotel, assignant hostes a les seves habitacions
	 *******************************************************/
	private void prepararHostes() {
		// Es decideix una quantitat de persones a l'hotel en funcio de la dificultat i una mica d'aleatorietat (ja que pot estar molt ple o poc)
		Random r = new Random();
		int tamanyLlista = (int) (hotel.numeroPlantes() * hotel.habitacionsPerPlanta() * (r.nextFloat()+r.nextInt(2)+1));
		
		// Es crea un hoste diferent per cada posicio de la llista
		for(int i = 0; i < tamanyLlista; i++) {
			
			String nom[] = {"",""}, cognom = "";
			Hoste hoste = null;
			
			
			// Sempre comprovant que el nom sigui UNIC
			do {
				
				nom = obtenirNomAleatori();
				cognom = obtenirCognomAleatori();
				
				hoste = new Hoste(nom[0], cognom, nom[1], Rol.HOSTE);
				
			}while(hotel.obtenirHostePerNom(hoste.obtenirNomComplet()) != null);
			
			// Es busca una habitacio que no estigui plena
			int planta, porta;
			Habitacio hab = null;
			do {
				
				planta = r.nextInt(hotel.numeroPlantes());
				porta = r.nextInt(hotel.habitacionsPerPlanta());
				
				hab = hotel.obtenirHabitacio(planta, porta);
				
			}while(hab.estaPlena());
			
			// I se li assigna tant l'habitacio al hoste com l'hoste a l'habitacio
			hoste.assignarHabitacio(hab);
			hotel.obtenirHabitacio(planta, porta).afegirHoste(hoste);
		}
		
		hotel.posarGentAllotjada(tamanyLlista);
	}
	
	/**************************************************************
	 * Metodes que s'han de cridar despres d'haver carregat els 
	 * noms i cognoms tant dels arxius com dels predeterminats
	 **************************************************************/
	private String[] obtenirNomAleatori() {
		Random r = new Random();
		
		return llistaNoms.get(r.nextInt(llistaNoms.size()));
	}
	private String obtenirCognomAleatori() {
		Random r = new Random();
		
		return llistaCognoms.get(r.nextInt(llistaCognoms.size()));
		
	}
	
	
	/*****************************************************************************************
	 * Metode que inicialitzara els noms i cognoms i retornara un codi en funcio del que s'hagi pogut fer:<br>
	 * (3): S'han carregat els noms i els cognoms dels fitxers exitosament<br>
	 * (2): Nomes s'han pogut carregar els cognoms<br>
	 * (1): Nomes s'han pogut carregar els noms<br>
	 * (0): No s'han pogut carregar ni els noms ni els cognoms.
	 *****************************************************************************************/
	public byte carregarNomsCognoms() {
		byte noms = carregarNoms() ? (byte)1 : (byte)0;
		byte cognoms = carregarCognoms() ? (byte)2 : (byte)0;

		// Afegeix tambe els noms predeterminats
		for(String[] s : NomsCognomsDefault.noms) {
			llistaNoms.add(s);
		}
		// i els cognoms predeterminats
		for(String s : NomsCognomsDefault.cognoms) {
			llistaCognoms.add(s);
		}
		
		return (byte)(noms+cognoms);
	}
	
	
	/*******************************************************************
	 * Metodes que carregaran els noms i els cognoms dels
	 * respectius arxius dins de les seves llistes corresponents,
	 * a mes dels predeterminats dins la classe NomsCognomsDefault
	 ******************************************************************/
	private boolean carregarNoms() {
		try {

			BufferedReader br = new BufferedReader(new FileReader(Joc.FITXER_NOMS));
			String nom;
			
			while((nom = br.readLine()) != null) {
				llistaNoms.add(nom.split(","));
			}
			
			br.close();

			return true;
			
		}catch(Exception e) {
			Main.tirarError("\nHa succeit algun error llegint la llista de noms\n");
			return false;
		}
	}
	private boolean carregarCognoms() {
		try {

			BufferedReader br = new BufferedReader(new FileReader(Joc.FITXER_COGNOMS));
			String cognom;
			
			while((cognom = br.readLine()) != null) {
				llistaCognoms.add(cognom);
			}
			
			br.close();
			
			return true;
			
		}catch(Exception e) {
			Main.tirarError("\nHa succeit algun error llegint la llista de cognoms\n");
			return false;
		}
	}
	
	
}
