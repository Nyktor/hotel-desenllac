/*
 * Autor: Victor Medrano Rodriguez
 * Data de creacio: 27/05/2024
 * Fitxer: Persona.java
 * Descripcio: Classe que representara a cada persona de cada habitacio
 */
package hotel;

import java.util.Random;

import joc.NomsCognomsDefault;

public class Hoste {
	
	private String nom;
	private String cognom;
	private Rol rol;
	private Habitacio habitacio;
	private int nivellEstres; 	// Valor que va del 0 al 60 que simbolitza l'agresivitat que presentara l'hoste i que pot decidir interaccions
	private boolean[] interaccionsFetes = {false, false, false, false}; //[Presentacio, Coartada, Acusacio, Pista demanada]
	private Rol tipusCoartada;	// Diu si el tipus de coartada es d'HOSTE (no te cap pista per donarnos) o de TESTIMONI (ens pot donar alguna pista)
	private String coartada;
	private String[] pistaDita = {"", "", "res"}; //[Tipus de pista, valor, rumor/pista]
	
	private boolean parlatAnteriorment;
	private boolean observat;
	
	// Atributs que permetran distingir sospitosos
	private int edat;
	private String genere;
	private String colorUlls;
	private String colorCabell;
	private String colorPell;
	private float alsada;
	private float pes;
	
	public Hoste(String nom, String cognom, Rol rol) {
		this.nom = nom;
		this.cognom = cognom;
		this.genere = NomsCognomsDefault.obtenirGenere(nom);
		this.rol = rol;
		
		Random r = new Random();
		this.nivellEstres = r.nextInt(61);
		this.parlatAnteriorment = false;
		
		aleatoritzarAtributs();
	}
	
	public Hoste(String nom, String cognom, String genere, Rol rol) {
		this.nom = nom;
		this.cognom = cognom;
		this.genere = genere;
		this.rol = rol;
		
		Random r = new Random();
		this.nivellEstres = r.nextInt(26);
		
		aleatoritzarAtributs();
	}
	
	public String obtenirNom() {
		return this.nom;
	}
	
	public String obtenirCognom() {
		return this.cognom;
	}
	
	public String obtenirNomComplet() {
		return this.nom+" "+this.cognom;
	}
	
	public String obtenirNOMCOMPLET() {
		return this.nom.toUpperCase()+" "+this.cognom.toUpperCase();
	}
	
	public Rol obtenirRol() {
		return this.rol;
	}
	
	public void posarRol(Rol rol) {
		this.rol = rol;
	}
	
	public void assignarHabitacio(Habitacio hab) {
		this.habitacio = hab;
	}
	
	public Habitacio obtenirHabitacio() {
		return this.habitacio;
	}
	
	public void augmentarNivellEstres(int punts) {
		this.nivellEstres += punts;
	}
	
	public Rol obtenirTipusCoartada() {
		return this.tipusCoartada;
	}
	
	public void marcarParlat() {
		this.parlatAnteriorment = true;
	}
	
	public boolean hasParlatAnteriorment() {
		return this.parlatAnteriorment;
	}
	
	public void marcarObservat() {
		this.observat = true;
	}
	
	public boolean hasObservat() {
		return this.observat;
	}

	public String obtenirGenere() {
		return this.genere;
	}

	public int obtenirEdat() {
		return edat;
	}

	public int obtenirEdatAproximada() {
		return Math.round(edat/10)*10;
	}

	public String obtenirColorUlls() {
		return colorUlls;
	}

	public String obtenirColorCabell() {
		return colorCabell;
	}

	public String obtenirColorPell() {
		return colorPell;
	}

	public float obtenirAlsada() {
		return alsada;
	}

	public float obtenirAlsadaAproximada() {
		return (Math.round(alsada*10))/(float)10;
	}

	public float obtenirPes() {
		return pes;
	}

	public float obtenirPesAproximat() {
		return Math.round(pes/10)*10;
	}
	
	public void aleatoritzarAtributs() {
		Random r = new Random();
		
		edat = 20 + r.nextInt(51);
		colorUlls = ConstantsHostes.colorsUlls[r.nextInt(ConstantsHostes.colorsUlls.length)];
		colorCabell = ConstantsHostes.colorsCabell[r.nextInt(ConstantsHostes.colorsCabell.length)];
		colorPell = ConstantsHostes.colorsPell[r.nextInt(ConstantsHostes.colorsPell.length)];
		pes = 50 + r.nextInt(31) + (r.nextBoolean() ? 20 : 0);
		alsada = ((float)(150 + r.nextInt(51)))/((float)100);
	}
	
	/***************************************************
	 * Es defineix el tipus de coartada que es tindra.
	 * Si es un HOSTE, sera de tipus HOSTE, i si es un
	 * TESTIMONI, sera de tipus TESTIMONI. En canvi, si
	 * es un ASSASSI, sera aleatoria entre aquestes dues.
	 ****************************************************/
	public void crearCoartada() {
		Random r = new Random();
		// Es crea una coartada nova...
		String coartada = "";
		
		// Depenent de si es un simple HOSTE
		if(this.rol == Rol.HOSTE) {
			// que, llavors, podra haver estat absent, dormint o sopant
			coartada = ConstantsHostes.coartadesPossiblesHoste[r.nextInt(ConstantsHostes.coartadesPossiblesHoste.length)];
			this.tipusCoartada = Rol.HOSTE;
		
		// O si ha estat TESTIMONI
		}else if(this.rol == Rol.TESTIMONI){
			// que, llavors, podra haver estat xatejant, jugant o, simplement, a l'habitacio
			coartada = ConstantsHostes.coartadesPossiblesTestimoni[r.nextInt(ConstantsHostes.coartadesPossiblesTestimoni.length)];
			this.tipusCoartada = Rol.TESTIMONI;
		
		// En canvi, si ha estat l'ASSASSI...
		}else if(this.rol == Rol.ASSASSI) {
			// S'escull aleatoriament si fingira haver estat testimoni o no
			boolean coartadaDeHoste = r.nextBoolean();
			coartada = (coartadaDeHoste ? ConstantsHostes.coartadesPossiblesHoste[r.nextInt(ConstantsHostes.coartadesPossiblesHoste.length)] : ConstantsHostes.coartadesPossiblesTestimoni[r.nextInt(ConstantsHostes.coartadesPossiblesTestimoni.length)]);
			
			this.tipusCoartada = coartadaDeHoste ? Rol.HOSTE : Rol.TESTIMONI;
		}
		
		this.coartada = coartada;
	}
	
	public String[] obtenirPistaDita() {
		return this.pistaDita;
	}
	
	// Primera interaccio: presentar-se
	public boolean shaPresentat() {
		return this.interaccionsFetes[0];
	}
	// Segona interaccio: explicar que feia a l'hora del crim
	public boolean haExplicatLaCoartada() {
		return this.interaccionsFetes[1];
	}
	// Tercera interaccio: se l'ha acusat
	public boolean haEstatAcusat() {
		return this.interaccionsFetes[2];
	}
	// Quarta interaccio: se l'hi ha demanat una pista
	public boolean seLiHaDemanatPista() {
		return this.interaccionsFetes[3];
	}
	// Metode que permetra marcar que ens ha donat la pista des del joc
	public void marcarHaExplicatCoartada() {
		this.interaccionsFetes[1] = true;
	}
	// Metode que permetra marcar que ens ha donat la pista des del joc
	public void marcarHaDonatPista() {
		this.interaccionsFetes[3] = true;
	}
	
/**********************************************SECCIO ACCIONS QUE PODEN FER ELS HOSTES**********************************************/
	
	/**********************************************************************
	 * Determina com saludara l'hoste quan el detectiu entri a l'habitacio.<br>
	 * Es genera un numero aleatori del 0 al 100. Si l'hoste te un nivell
	 * d'estres major a aquest numero, sera molt agresiu amb el detectiu.<br>
	 * L'assassi mai sera agresiu.
	 **********************************************************************/
	public String saludar(String detectiu) {
		Random r = new Random();
		int aleatori = r.nextInt(101);
		
		String fraseEscollida;
		
		// Opcio pacifica
		if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {
			fraseEscollida = ConstantsHostes.opcionsSaludarSenseEstres[r.nextInt(ConstantsHostes.opcionsSaludarSenseEstres.length)];
		// Opcio agresiva
		}else {
			fraseEscollida = ConstantsHostes.opcionsSaludarAmbEstres[r.nextInt(ConstantsHostes.opcionsSaludarAmbEstres.length)];
		}

		crearCoartada();
		
		return fraseEscollida;
	}
	
	/**********************************************************************
	 * Es presentara i dira la seva edat. Depenent de si ja se li ha preguntat
	 * o no, reaccionara d'una forma o d'una altra. Tambe dependra del seu 
	 * nivell d'agresivitat el com respongui. L'assassi mai respondra agresiu
	 **********************************************************************/
	public String presentarse() {
		Random r = new Random();
		int aleatori = r.nextInt(101);
		String fraseEscollida;
		
		// Si se li ha preguntat a bans
		if(shaPresentat()) {
			// Opcio pacifica
			if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {

				String[] opcions = ConstantsHostes.opcionsPresentacio(nom, cognom, genere, edat, true);
		
				fraseEscollida = opcions[r.nextInt(opcions.length)];
			// Opcio agresiva
			}else {
				fraseEscollida = "Tens una ameba per cervell o que? Putos policies inutils...";
			}
		
		// Si es el primer cop
		}else {
			// Opcio pacifica
			if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {

				String[] opcions = ConstantsHostes.opcionsPresentacio(nom, cognom, genere, edat, false);
		
				fraseEscollida = opcions[r.nextInt(opcions.length)];
			// Opcio agresiva
			}else {
				fraseEscollida = nom+" "+cognom+". "+edat+".";
			}
			
			this.interaccionsFetes[0] = true;
		}
		return fraseEscollida;
	}
	
	/**********************************************************************
	 * Se li preguntara al hoste que feia a l'hora de l'assassinat. Es
	 * tindra en compte de nou el rol, el nivell d'estres i si se li ha
	 * preguntat amb anterioritat
	 **********************************************************************/
	public String dirCoartada() {
		Random r = new Random();
		int aleatori = r.nextInt(101);
		String fraseEscollida;
		
		// Si se li ha preguntat ABANS
		if(haExplicatLaCoartada()) {
			
			// Opcio pacifica
			if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {

				String[] opcions = ConstantsHostes.opcionsDirCoartada(this.coartada, true);
		
				fraseEscollida = opcions[r.nextInt(opcions.length)];
			// Opcio agresiva
			}else {
				fraseEscollida = "Pero voste m'escolta quan li parlo?! Ja li he dit el que estava fent!";
			}
		
		// Si es el PRIMER COP
		}else {
			
			// Opcio pacifica
			if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {

				String[] opcions = ConstantsHostes.opcionsDirCoartada(coartada, false);
		
				fraseEscollida = opcions[r.nextInt(opcions.length)];
			// Opcio agresiva
			}else {
				fraseEscollida = "Estava "+coartada+". Que li importa?";
			}
			
		}
		return fraseEscollida;
	}
	
	/**********************************************************************
	 * Determina com es respondra davant l'acusacio del detectiu.<br>
	 * Es genera un numero aleatori del 0 al 100. Si l'hoste te un nivell
	 * d'estres major a aquest numero, sera molt agresiu amb el detectiu.<br>
	 * L'assassi mai respondra agresivament.
	 **********************************************************************/
	public String respostaAcusacio() {
		Random r = new Random();
		int aleatori = r.nextInt(101);
		String fraseEscollida;
		
		if(haEstatAcusat()) {

			if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {
				fraseEscollida = ConstantsHostes.opcionsAcusacioRepetirSenseEstres[r.nextInt(ConstantsHostes.opcionsAcusacioRepetirSenseEstres.length)];
			}else {
				fraseEscollida = "Deixi de tocar-me els collons i trobi a l'assassi d'un maleit cop!";
			}
		}else {

			if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {
				fraseEscollida = ConstantsHostes.opcionsAcusacioSenseEstres[r.nextInt(ConstantsHostes.opcionsAcusacioSenseEstres.length)];
			}else {
				fraseEscollida = ConstantsHostes.opcionsAcusacioAmbEstres[r.nextInt(ConstantsHostes.opcionsAcusacioAmbEstres.length)];
			}
			
			this.interaccionsFetes[2] = true;
		}
		
		return fraseEscollida;
	}
	/**********************************************************************
	 * Retornara una pista si l'hoste ha declarat que estava a l'hotel a l'hora del crim.<br>
	 * Es genera un numero aleatori del 0 al 100. Si l'hoste te un nivell
	 * d'estres major a aquest numero, sera molt agresiu amb el detectiu, pero
	 * la pista la donara igual. L'assassi mai sera agresiu.<br>
	 **********************************************************************/
	public String donarPista(Hoste assassi) {
		Random r = new Random();
		int aleatori = r.nextInt(101);
		String fraseEscollida;
		
		if(seLiHaDemanatPista()) {
			
			if(this.tipusCoartada == Rol.TESTIMONI) {
				if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {
					fraseEscollida = ConstantsHostes.opcionsDonarPista(this.pistaDita[0], this.pistaDita[1], true)[r.nextInt(ConstantsHostes.opcionsDonarPista(this.pistaDita[0], this.pistaDita[1], true).length)];
				}else {
					fraseEscollida = "I dale: "+this.pistaDita[0]+" "+this.pistaDita[1]+". Miri que sou pesats!";
				}
			}else {
				if(this.pistaDita[2].equals("res")) {
					fraseEscollida = ConstantsHostes.opcionsNoHaVistRes(true)[r.nextInt(ConstantsHostes.opcionsNoHaVistRes(true).length)];
				}else {
					fraseEscollida = ConstantsHostes.opcionsDirRumor(this.pistaDita[0], this.pistaDita[1], true)[r.nextInt(ConstantsHostes.opcionsDirRumor(this.pistaDita[0], this.pistaDita[1], true).length)];
				}
			}
		}else {
			
			// Si s'ha declarat que s'estava a l'hotel a l'hora del crim
			if(this.tipusCoartada == Rol.TESTIMONI) {

				// Es decideix el tipus de pista que es tindra
				this.pistaDita[0] = ConstantsHostes.tipusDePistaPossibles[r.nextInt(ConstantsHostes.tipusDePistaPossibles.length)];
				
				/* I es determina el valor de la pista que es dira
				*  Si qui diu la pista es l'ASSASSI, dira una MENTIDA NECESSARIAMENT
				*  Es a dir, s'aleatoritzara la resposta mentre coincideixi amb la real
				*/
				String valorPista = "";
				
				switch(this.pistaDita[0]) {
					case "color d'ulls"->{
						if(this.rol == Rol.ASSASSI) {
							do {
								
								valorPista = ConstantsHostes.colorsUlls[r.nextInt(ConstantsHostes.colorsUlls.length)];
								
							}while(valorPista.equals(assassi.obtenirColorUlls()));
						}else {
							valorPista = assassi.obtenirColorUlls();
						}
					}
					
					case "color de cabell" ->{
						if(this.rol == Rol.ASSASSI) {
							do {
								
								valorPista = ConstantsHostes.colorsCabell[r.nextInt(ConstantsHostes.colorsCabell.length)];
								
							}while(valorPista.equals(assassi.obtenirColorCabell()));
						}else {
							valorPista = assassi.obtenirColorCabell();
						}
					}
					case "color de pell" ->{
						if(this.rol == Rol.ASSASSI) {
							do {
								
								valorPista = ConstantsHostes.colorsPell[r.nextInt(ConstantsHostes.colorsPell.length)];
								
							}while(valorPista.equals(assassi.obtenirColorPell()));
						}else {
							valorPista = assassi.obtenirColorPell();
						}
						
					}
					case "genere"->{
						if(this.rol == Rol.ASSASSI) {
							valorPista = assassi.obtenirGenere().equals("home") ? "dona" : "home";
						}else {
							valorPista = assassi.obtenirGenere();
						}
						
					}
					case "alsada"->{
						if(this.rol == Rol.ASSASSI) {
							do {
								
								valorPista = ""+(Math.round(((float)(150 + r.nextInt(51)))/((float)100)*10))/(float)10;
								
							}while(valorPista.equals(""+assassi.obtenirAlsadaAproximada()));
						}else {
							valorPista = ""+assassi.obtenirAlsadaAproximada();
						}
						
					}
					case "pes" ->{
						if(this.rol == Rol.ASSASSI) {
							do {
								
								valorPista = ""+Math.round((50 + r.nextInt(71))/10)*10;
								
							}while(valorPista.equals(""+assassi.obtenirPesAproximat()));
						}else {
							valorPista = ""+assassi.obtenirPesAproximat();
						}
						
					}
					case "edat" ->{
						if(this.rol == Rol.ASSASSI) {
							do {
								
								valorPista = ""+Math.round((20 + r.nextInt(51)));
								
							}while(valorPista.equals(""+assassi.obtenirEdatAproximada()));
						}else {
							valorPista = ""+assassi.obtenirEdatAproximada();
						}
						
					}
					
					default ->{
						valorPista = null;
					}
				}
				
				this.pistaDita[1] = valorPista;
				this.pistaDita[2] = "pista";
				

				if(aleatori > this.nivellEstres || this.rol == Rol.ASSASSI) {
					fraseEscollida = ConstantsHostes.opcionsDonarPista(this.pistaDita[0], this.pistaDita[1], false)[r.nextInt(ConstantsHostes.opcionsDonarPista(this.pistaDita[0], this.pistaDita[1], false).length)];
				}else {
					fraseEscollida = "Nomes dire: "+this.pistaDita[0]+" "+this.pistaDita[1]+". Ara, vagi-se'n.";
				}
			
			// Si no estava a l'hotel
			}else {
				int aleatoriDirRumor = r.nextInt(101); // Numero aleatori del 0 al 100

				// 30% de probabilitats de que digui un rumor
				if(aleatoriDirRumor >= 70) {
					int aleatoriRumorReal = r.nextInt(2); // Amb un 50% de probabilitats de que sigui real vs 50% que sigui fals

					// Es decideix el tipus de pista que es tindra
					this.pistaDita[0] = ConstantsHostes.tipusDePistaPossibles[r.nextInt(ConstantsHostes.tipusDePistaPossibles.length)];
					
					String valorPista = "";
					
					/*
					 * Si surt 1, l'hoste dira informacio veridica.
					 * Si surt 0, l'hoste dira informacio inventada.
					 * En ambdos casos es marcara la informacio com a "rumor"
					 */
					switch(this.pistaDita[0]) {
						case "color d'ulls"->{
							if(aleatoriRumorReal == 0) {
								do {
									
									valorPista = ConstantsHostes.colorsUlls[r.nextInt(ConstantsHostes.colorsUlls.length)];
									
								}while(valorPista.equals(assassi.obtenirColorUlls()));
							}else {
								valorPista = assassi.obtenirColorUlls();
							}
						}
						
						case "color de cabell" ->{
							if(aleatoriRumorReal == 0) {
								do {
									
									valorPista = ConstantsHostes.colorsCabell[r.nextInt(ConstantsHostes.colorsCabell.length)];
									
								}while(valorPista.equals(assassi.obtenirColorCabell()));
							}else {
								valorPista = assassi.obtenirColorCabell();
							}
						}
						case "color de pell" ->{
							if(aleatoriRumorReal == 0) {
								do {
									
									valorPista = ConstantsHostes.colorsPell[r.nextInt(ConstantsHostes.colorsPell.length)];
									
								}while(valorPista.equals(assassi.obtenirColorPell()));
							}else {
								valorPista = assassi.obtenirColorPell();
							}
							
						}
						case "genere"->{
							if(aleatoriRumorReal == 0) {
								valorPista = assassi.obtenirGenere().equals("home") ? "dona" : "home";
							}else {
								valorPista = assassi.obtenirGenere();
							}
							
						}
						case "alsada"->{
							if(aleatoriRumorReal == 0) {
								do {
									
									valorPista = ""+(Math.round(((float)(150 + r.nextInt(51)))/((float)100)*10))/(float)10;
									
								}while(valorPista.equals(""+assassi.obtenirAlsadaAproximada()));
							}else {
								valorPista = ""+assassi.obtenirAlsadaAproximada();
							}
							
						}
						case "pes" ->{
							if(aleatoriRumorReal == 0) {
								do {
									
									valorPista = ""+Math.round((50 + r.nextInt(71))/10)*10;
									
								}while(valorPista.equals(""+assassi.obtenirPesAproximat()));
							}else {
								valorPista = ""+assassi.obtenirPesAproximat();
							}
							
						}
						case "edat" ->{
							if(aleatoriRumorReal == 0) {
								do {
									
									valorPista = ""+(Math.round((20 + r.nextInt(51)/10)*10));
									
								}while(valorPista.equals(""+assassi.obtenirEdatAproximada()));
							}else {
								valorPista = ""+assassi.obtenirEdatAproximada();
							}
							
						}
						
						default ->{
							valorPista = null;
						}
					}
					
					this.pistaDita[1] = valorPista;
					this.pistaDita[2] = "rumor";
					
					fraseEscollida = ConstantsHostes.opcionsDirRumor(this.pistaDita[0], this.pistaDita[1], false)[r.nextInt(ConstantsHostes.opcionsDirRumor(this.pistaDita[0], this.pistaDita[1], false).length)];
				}else {
					// Si no diu cap rumor, simplement admetera no haver vist res
					this.pistaDita[2] = "res";
					fraseEscollida = ConstantsHostes.opcionsNoHaVistRes(true)[r.nextInt(ConstantsHostes.opcionsNoHaVistRes(true).length)];
				}
				
			}
		}
		
		return fraseEscollida;
	}


	/**********************************************************************
	 * Determina com es reaccionara a l'arrest final, en cas que s'acusi
	 * a l'hoste. Hi ha una reaccio diferent si encerten i si no.
	 **********************************************************************/
	public String reaccionarArrestFinal(String detectiu, boolean encertat) {
		Random r = new Random();
		
		String fraseEscollida = ConstantsHostes.opcionsArrestFinal(detectiu, encertat)[r.nextInt(ConstantsHostes.opcionsArrestFinal(detectiu, encertat).length)];
		
		return fraseEscollida;
	}
	
}
