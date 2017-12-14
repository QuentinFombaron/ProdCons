package jus.poc.prodcons.step1;

import jus.poc.prodcons.*;

import javax.rmi.ssl.SslRMIClientSocketFactory;
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
        while ((this.buffer.taille() != 0) || (!this.producteurList.isEmpty())) {
            //System.out.println("isEmpty -> "+this.producteurList.isEmpty());
            //System.out.println("taille -> "+this.buffer.taille()+"\n");
            int timer = this.consommationAlea.next() * 1000;
            try {
                this.buffer.get(this);
                sleep(timer);
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
            this.nbConsome++;
        }
        System.exit(0);
    }

    @Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub
		return 0;
	}
}
