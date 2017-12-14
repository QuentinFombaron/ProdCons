package jus.poc.prodcons.step4;

import jus.poc.prodcons.*;

import java.security.spec.ECField;
import java.util.LinkedList;

public class Producteur  extends Acteur implements _Producteur {
    private ProdCons buffer;
    private Aleatoire consommationAlea;
    private Aleatoire nbExemplaire;
    private int noProduit;
    private int nbMessageAProduire;
    private LinkedList<Producteur> producteurList;

	// type d'un Producteur = 1
	protected Producteur(int type, Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
                         int nbMessageAProduire, ProdCons buffer, LinkedList<Producteur> producteurList,
                         int nombreMoyenNbExemplaire, int deviationNombreMoyenNbExemplaire) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessageAProduire = nbMessageAProduire;
        this.buffer = buffer;
        this.consommationAlea = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.nbExemplaire = new Aleatoire(nombreMoyenNbExemplaire, deviationNombreMoyenNbExemplaire);
        this.noProduit = 1;
        this.producteurList = producteurList;
	}

    @SuppressWarnings("Duplicates")
	@Override
	public void run() {
        while (this.noProduit <= this.nbMessageAProduire) {
            int nbExemplaire = this.nbExemplaire.next();
            MessageX newMessage = new MessageX(this, nbExemplaire);
            int timer = this.consommationAlea.next() * 1000;
            try {
                this.observateur.productionMessage(this, newMessage, timer);
                sleep(timer);
                this.buffer.put(this, newMessage);
            } catch (Exception e) {
                e.getMessage();
            }
            this.noProduit++;
        }
        this.producteurList.remove(this);
        System.out.println("*** Prod"+this.identification()+" a finit de produire ***\n");
    }

	@Override
	public int nombreDeMessages() {
		return this.noProduit;
	}

}
