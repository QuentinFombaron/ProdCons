package jus.poc.prodcons.step1;

import jus.poc.prodcons.*;

import java.util.LinkedList;

public class Producteur  extends Acteur implements _Producteur {
    private ProdCons buffer;
    private Aleatoire consommationAlea;
    private int nbProduit;
    private int nbMessageAProduire;
    private LinkedList<Producteur> producteurList;
    /*
	protected Producteur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
	}
	*/

	// type = 1
	protected Producteur(int type, Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
                         int nbMessageAProduire, ProdCons buffer, LinkedList<Producteur> producteurList) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessageAProduire = nbMessageAProduire;
        this.buffer = buffer;
        this.consommationAlea = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.nbProduit = 0;
        this.producteurList = producteurList;
	}


	@Override
	public void run() {
        while (this.nbProduit < this.nbMessageAProduire) {
            Message newMessage = new MessageX(this);
            int timer = this.consommationAlea.next();
            try {
                sleep(timer);
                this.buffer.put(this, newMessage);
                this.nbProduit++;
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

	@Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return 0;
	}

}
