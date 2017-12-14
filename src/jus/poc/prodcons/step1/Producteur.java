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
        while (this.nbProduit <= this.nbMessageAProduire) {
            Message newMessage = new MessageX(this);
            int timer = this.consommationAlea.next() * 1000;
            try {
                sleep(timer);
                this.buffer.put(this, newMessage);
            } catch (Exception e) {
                e.getMessage();
            }
            this.nbProduit++;
        }
        producteurList.remove(this);
        System.out.println("*** Prod"+this.identification()+" a finit de produire ***\n");
    }

	@Override
	public int nombreDeMessages() {
		return this.nbProduit;
	}

}
