package jus.poc.prodcons.step6;

import jus.poc.prodcons.*;

import java.util.LinkedList;

public class Consommateur extends Acteur implements _Consommateur{
    private ProdCons buffer;
    private Aleatoire consommationAlea;
    private int nbConsome;
    private LinkedList<Producteur> producteurList;

	//type d'un Consommateur = 2
    protected Consommateur(int type, Observateur observateur, int moyenneTempsDeTraitement,
                           int deviationTempsDeTraitement, ProdCons buffer, LinkedList<Producteur> producteurList) throws ControlException {
        super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.buffer = buffer;
        this.consommationAlea = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.nbConsome = 0;
        this.producteurList = producteurList;
    }

    @SuppressWarnings("Duplicates")
    @Override
    /* NOTE: Lorsque le thread est dans le sleep(), reçoit-il les NotifyAll() ?*/
    public void run() {
        while ((this.buffer.taille() != 0) || (!this.producteurList.isEmpty())) {
            int timer = this.consommationAlea.next() * 1000;
            try {
                System.out.println("Consommateur"+this.identification()+" demande un message\n");
                Message message_r = this.buffer.get(this);
                this.observateur.consommationMessage(this, message_r, timer);
                sleep(timer);
            } catch (Exception e) {
                System.out.println("/!\\ Consommateur" + this.identification() + " n'a pas pu obtenir de message malgré sa demande\n");
            }
            this.nbConsome++;
        }
        System.out.println("*** Consommateur"+this.identification()+" n'a plus rien à consommer ***\n");
    }

    @Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return 0;
	}
}
