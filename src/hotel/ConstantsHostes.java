/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 02/06/2024
 * Fitxer: ConstantsHostes.java
 * Descripcio: Classe que contindra constants d'aleatoritzacio i referents a les interaccions dels hostes.
 * 			   (feta per no saturar cadascun dels objectes Hoste amb llistes inacabables de variables)
 */
package hotel;

public class ConstantsHostes {
	
	public static final String[] colorsUlls = {"blaus", "verds", "marrons"};
	public static final String[] colorsCabell = {"negre", "marro", "ros"};
	public static final String[] colorsPell = {"blanca", "morena", "negra"};
	public static final String[] coartadesPossiblesHoste = {"absent de l'hotel", "comprant al Mercadona", "sopant fora", "prenent algo"};
	public static final String[] coartadesPossiblesTestimoni = {"jugant a l'habitacio", "xatejant en l'habitacio", "a l'habitacio"};
	
	public static final String[] tipusDePistaPossibles = {"color d'ulls", "color de cabell", "color de pell", "genere", "alsada", "pes", "edat"};
	
	public static final String[] opcionsSaludarSenseEstres = {"Hola, com estem?", 
															   "Bona nit, detectiu.", 
															   "Encantat de coneixer-lo.",
															   "Bones!",
															   "Hola, detectiu. Volem colaborar."};
	
	public static final String[] opcionsSaludarAmbEstres = {"Ja era hora!", 
															 "Els de la policia sou una colla d'inutils.", 
															 "Vagi-se'n a la merda!",
															 "Torni per on ha entrat!",
															 "No el volem aqui."};
	
	public static final String[] opcionsAcusacioSenseEstres = {"Impossible. Jo no coneixia a la victima de res.", 
															   "Com puc haver estat jo? Si me n'he assabentat fa uns minuts que havien matat a algu.", 
															   "Es impossible que hagi estat jo. No en sabia res.",
															   "No he estat jo. Tant de bo trobi a l'assassi quan abans..."};
	
	public static final String[] opcionsAcusacioAmbEstres = {"Calli's la boca! Voste no sap res!", 
															 "Com gosa acusar-me d'aquesta manera?!", 
															 "No penso ajudar-li, desgraciat.",
															 "Que coi diu?! Vagi-se'n ja d'una vegada!",
															 "Els de la policia cada cop sou mes estupids."};
	
	public static final String[] opcionsAcusacioRepetirSenseEstres = {"Ja li he dit que no.", 
																	  "Un altre cop? Ja li he explicat que no he estat jo!", 
																	  "Que no. No l'he matat jo.",
																	  "Si us plau, aquesta conversa ja la hem tingut."};

	/********************************************************
	 * Dialeg per quan es presenta l'hoste
	 ********************************************************/
	public static final String[] opcionsPresentacio(String nom, String cognom, String genere, int edat, boolean repetir){
		if(repetir) {
			String[] opcions = {"Com ja he dit, m dic "+nom+" "+cognom+" i tinc "+edat+" anys.", 
							    "Ja s'ha oblidat? Soc "+(genere.equals("home") ? "el" : "la")+nom+" "+cognom+", i vaig fer"+edat+" recentment.",
							    "Li torno a repetir: "+nom+" "+cognom+", "+edat+" anys.",
								"No li tornare a repetir."};
			return opcions;
		}else {
			String[] opcions = {"Em dic "+nom+" "+cognom+" i tinc "+edat+" anys.", 
							    "Soc "+(genere.equals("home") ? "el" : "la")+" "+nom+" "+cognom+", i vaig fer "+edat+" fa poc!",
							    "El meu nom es "+nom+" "+cognom+" i tinc "+edat+", quasi "+(edat+1)+".",
								nom+" "+cognom+". Fare "+(edat+1)+" d'aqui a poc."};
			return opcions;
		}
	}

	/*****************************************************************
	 * Dialeg amb que ens presenta amb la seva coartada
	 *****************************************************************/
	public static final String[] opcionsDirCoartada(String coartada, boolean repetir) {
		if(repetir) {
			String[] opcions = {"Com ja li he dit, estava "+coartada+".",
								"Repeteixo: crec que em trobava "+coartada+".",
								"Estava "+coartada+". No m'ho ha preguntat abans?"
			};
			
			return opcions;
		}else {
			String[] opcions = {"Recordo que sobre aquella hora em trobava "+coartada+".",
								"Crec que estava "+coartada+".",
								"Em trobava "+coartada+", si no recordo malament..."
			};
			
			return opcions;
		}
	}
	

	/********************************************************
	 * Dialeg per quan es presenta
	 ********************************************************/
	public static final String[] opcionsNoHaVistRes(boolean repetir){
		if(repetir) {
			String[] opcions = {"Ja li ho he dit: no he vist res.", 
							    "De veritat, no tinc cap informacio util.",
							    "No se absolutament res, de debo!",
								"Que no se res!"};
			return opcions;
		}else {
			String[] opcions = {"Ho lamento molt, no he vist res.", 
							    "Estava fora de l'hotel. No en se res.",
							    "No crec que li pugui dir res util.",
								"Mil disculpes, no puc ajudar-lo."};
			return opcions;
		}
	}

	/*****************************************************************
	 * Dialeg mitjancant el qual el testimoni ens ofereix una pista
	 *****************************************************************/
	public static final String[] opcionsDonarPista(String tipusPista, String valorPista, boolean repetir) {
		
		String pista = switch(tipusPista){
		
			case "genere"->{
				yield "era "+(valorPista.equals("home") ? "un" : "una")+" "+valorPista;
			}
			case "alsada"->{
				yield "mesurava uns "+valorPista+" m d'alsada";
			}
			case "pes" ->{
				yield "pel tipus de cos, pesaria uns "+valorPista+"kg";
			}
			case "edat" ->{
				yield "tindria uns "+valorPista+" anys";
			}
			default ->{
				yield "tenia " + switch(tipusPista) {
					case "color d'ulls" ->{
						yield "els ulls";
					}
					case "color de pell" ->{
						yield "la pell";
					}
					case "color de cabell"->{
						yield "el cabell";
					}
					default ->{
						yield null; // Perque tiri un error
					}
				
				}+" "+valorPista;
			}
		};
		
		if(repetir) {
			
			String[] opcions = {"Repeteixo: "+pista+". Es el que vaig veure.",
								"Ja li he dit que nomes vaig veure que "+pista+".",
								"Tornem-hi: "+pista+". Apunti-ho be."
			};
			
			return opcions;
			
		}else {
			
			String[] opcions = {"Vaig veure a algu... em va semblar que "+pista+".",
								"Vaig veure a algu que corria pels passadissos. Potser "+pista+"?",
								"No estic molt segur, pero em sembla que qui vaig veure "+pista+"."
			};
			
			return opcions;
		
		}
	}
	

	/*****************************************************************
	 * Dialeg mitjancant el qual un HOSTE ens explica el rumor que ha sentit.
	 * Pot ser real o no.
	 *****************************************************************/
	public static final String[] opcionsDirRumor(String tipusPista, String valorPista, boolean repetir) {
		
		String pista = switch(tipusPista){
		
			case "genere"->{
				yield "era "+(valorPista.equals("home") ? "un" : "una")+" "+valorPista;
			}
			case "alsada"->{
				yield "mesurava uns "+valorPista+" m d'alsada";
			}
			case "pes" ->{
				yield "pel tipus de cos, pesaria uns "+valorPista+"kg";
			}
			case "edat" ->{
				yield "tindria uns "+valorPista+" anys";
			}
			default ->{
				yield "tenia " + switch(tipusPista) {
					case "color d'ulls" ->{
						yield "els ulls";
					}
					case "color de pell" ->{
						yield "la pell";
					}
					case "color de cabell"->{
						yield "el cabell";
					}
					default ->{
						yield null; // Perque tiri un error
					}
				
				}+" "+valorPista;
			}
		};
		
		if(repetir) {
			
			String[] opcions = {"Nomes he sentit que "+pista+".",
								"Ja li he dit que el que m'han dit es que "+pista+".",
								"Torne'm-hi: "+pista+". Es el que he sentit, pero potser es un rumor."
			};
			
			return opcions;
			
		}else {
			
			String[] opcions = {"No he vist res, pero es rumoreja que l'assassi "+pista+".",
								"He sentit que deien que l'autor del crim "+pista+". Sera veritat?",
								"M'han dit que "+pista+". Si m'ho han dit, sera cert, oi?",
								"Diuen que l'assassi "+pista+". Tot i que potser es nomes un rumor."
			};
			
			return opcions;
		
		}
	}

	/********************************************************
	 * Dialeg per quan es presenta
	 ********************************************************/
	public static final String[] opcionsArrestFinal(String detectiu, boolean encertat){
		if(encertat) {
			String[] opcions = {"Jo el maleeixo, detectiu "+detectiu+"! Ens veurem a l'infern!", 
							    "No!! No ho entenen, no saben el que em devia!!",
							    "Deixi'm anar!! Deixi'n-me!! S'arrepentiran!!",
								"Que es pensen que fan?! Deixin-me anar!"};
			return opcions;
		}else {
			String[] opcions = {"No, no! Soc innocent! Ho prometo!", 
							    "Si us plau, no! Jo no he fet res!!",
							    "Ho juro, jo no he estat! Deixin-me, si us plau!",
								"S'equivoca, "+detectiu+"! Jo no soc l'assassi! Nooo!"};
			return opcions;
		}
	}

}
