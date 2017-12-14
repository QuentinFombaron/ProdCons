package jus.poc.prodcons.step4;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message {
    private _Producteur producteur;
    private int message;
    private int nbExemplaire;

    public void setNbExemplaire(int nbExemplaire) {
        this.nbExemplaire = nbExemplaire;
    }

    public int getNbExemplaire() {
        return nbExemplaire;
    }

    public MessageX (_Producteur producteur, int nbExemplaire) {
        this.message = producteur.nombreDeMessages();
        this.producteur = producteur;
        this.nbExemplaire = nbExemplaire;
    }

    @Override
    public String toString() {
        return "Prod"+this.producteur.identification()+"(no"+this.message+"-"+this.nbExemplaire+")";
    }
}