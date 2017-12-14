package jus.poc.prodcons.step1;


import java.util.*;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {
	//Déclaration des variables pour chaque champ du fichier option.xml pour la méthode init()
    private int nbProd, nbCons, nbBuffer, tempsMoyenProduction, deviationTempsMoyenProduction,
            tempsMoyenConsommation, deviationTempsMoyenConsommation, nombreMoyenDeProduction,
            deviationNombreMoyenDeProduction, nombreMoyenNbExemplaire, deviationNombreMoyenNbExemplaire;

    private Map<String, Integer> XML_Values = new HashMap<String, Integer>();

    private Aleatoire nbRandomMessage;

    private LinkedList<Consommateur> consommateurList;
    private LinkedList<Producteur> producteurList;

    private ProdCons buffer;

    public TestProdCons(Observateur observateur){
        super(observateur);
        this.consommateurList = new LinkedList<Consommateur>();
        this.producteurList = new LinkedList<Producteur>();

        try {
            init("jus/poc/prodcons/options/option.xml");
        } catch (Exception e) {
            e.getMessage();
        }
        this.buffer = new ProdCons(12);
        this.nbRandomMessage = new Aleatoire(this.nombreMoyenDeProduction, this.deviationNombreMoyenDeProduction);

    }

    /*****************************************************************************************/

    public Map<String, Integer> getXML_Values() {
        return this.XML_Values;
    }

    public int getXML_Value(String name_p) {
        for(Map.Entry<String, Integer> entry : this.XML_Values.entrySet()) {
            String name = entry.getKey();
            Integer value = entry.getValue();
            if (name.equals(name_p)) {
                return value;
            }
        }
        return -1;
    }

    private void addXML_Values(String name, int value) {
        this.XML_Values.put(name, value);
    }

    /*****************************************************************************************/
	
	private static void PrintXML(Map<String, Integer> XML_Values) {
        System.out.println("*** Contenu du fichier option.xml ***");
        for(Map.Entry<String, Integer> entry : XML_Values.entrySet()) {
            String name = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(name+" : "+value);
        }
		System.out.println("*************************************");
	}
	
	/**
	* Retreave the parameters of the application.
	* @param file the final name of the file containing the options. 
	**/
	
	protected void init(String file) {
		Properties properties = new Properties(); 
		try {
			properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String key;
		int value;
		Class<?> thisOne = getClass();
		for(Map.Entry<Object,Object> entry : properties.entrySet()) {
			key = (String)entry.getKey();
			value = Integer.parseInt((String)entry.getValue()); 
			try {
				thisOne.getDeclaredField(key).set(this,value);
				//Ajout des données XML dans la Map XML_Values
                addXML_Values(key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}

    protected void run() throws Exception{
        //Lecture du fichier option.xml
        init("jus/poc/prodcons/options/option.xml");

        //Affichage des données importées du fichier option.xml
        PrintXML(this.XML_Values);


        /*
        ProdCons prodCons = new ProdCons(getXML_Value("nbBuffer"));

        Producteur prod1 = new Producteur(1, observateur, getXML_Value("tempsMoyenProduction"),
                getXML_Value("deviationTempsMoyenProduction"));

        Consommateur cons1 = new Consommateur(2, observateur, getXML_Value("tempsMoyenConsommation"),
                getXML_Value("deviationTempsMoyenConsommation"));
        Consommateur cons2 = new Consommateur(2, observateur, getXML_Value("tempsMoyenConsommation"),
                getXML_Value("deviationTempsMoyenConsommation"));


        Message message1 = new MessageX(prod1, "Blabla");
        Message message2 = new MessageX(prod1, "Blublu");

        prodCons.put(prod1, message1);
        prodCons.put(prod1, message2);
        prodCons.get(cons1);
        prodCons.get(cons2);
        */

        // Création de nbProd Producteur(s)
        for (int i = 0; i < this.nbProd; i++) {
            int nbMessageAProduire = this.nbRandomMessage.next();
            Producteur nProducteur = new Producteur(1, this.observateur, this.tempsMoyenProduction,
                    this.deviationTempsMoyenProduction, nbMessageAProduire, this.buffer, this.producteurList);
            this.producteurList.add(nProducteur);
            nProducteur.start();
        }
        //  Création de nbCons consommateur(s)
        for (int i = 0; i < this.nbCons; i++) {
            Consommateur nConsommateur = new Consommateur(2, this.observateur, this.tempsMoyenConsommation,
                    this.deviationTempsMoyenConsommation, this.buffer, this.producteurList);
            this.consommateurList.add(nConsommateur);
            nConsommateur.start();
        }


    }

    public static void main(String[] args){
        new TestProdCons(new Observateur()).start();
    }
}
