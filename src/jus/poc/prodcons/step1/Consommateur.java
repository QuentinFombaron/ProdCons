package jus.poc.prodcons.step1;

import jus.poc.prodcons.*;

import java.util.LinkedList;

public class Consommateur extends Acteur implements _Consommateur{
    private ProdCons buffer;
    private Aleatoire consommationAlea;
    private int nbConsome;
    private LinkedList<Producteur> producteurList;

    /*
	protected Consommateur(int type, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
	}
	*/

	//type = 2
    protected Consommateur(int type, Observateur observateur, int moyenneTempsDeTraitement,
                           int deviationTempsDeTraitement, ProdCons buffer, LinkedList<Producteur> producteurList) throws ControlException {
        super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.buffer = buffer;
        this.consommationAlea = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
        this.nbConsome = 0;
        this.producteurList = producteurList;
    }


    @Override
    public void run() {
        while ((this.buffer.enAttente() != 0) || (!producteurList.isEmpty())) {
            int timer = this.consommationAlea.next();
            try {
                System.out.println(this.toString() + "  " + this.buffer.get(this).toString());
                sleep(timer);
                this.nbConsome++;
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    @Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return 0;
	}
}
