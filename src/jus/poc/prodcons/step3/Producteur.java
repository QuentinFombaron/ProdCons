package jus.poc.prodcons.step3;

import jus.poc.prodcons.*;

import java.util.LinkedList;

public class Producteur  extends Acteur implements _Producteur {
    private ProdCons buffer;
    private Aleatoire consommationAlea;
    private int noProduit;
    private int nbMessageAProduire;
    private LinkedList<Producteur> producteurList;

	// type d'un Producteur = 1
	protected Producteur(int type, Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
                         int nbMessageAProduire, ProdCons buffer, LinkedList<Producteur> producteurList) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessageAProduire = nbMessageAProduire;
        this.buffer = buffer;
        this.consommationAlea = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.noProduit = 1;
        this.producteurList = producteurList;
	}

    @SuppressWarnings("Duplicates")
	@Override
	public void run() {
        while (this.noProduit <= this.nbMessageAProduire) {
            Message newMessage = new MessageX(this);
            int timer = this.consommationAlea.next() * 1000;
            try {
                sleep(1000);
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
