package jus.poc.prodcons.step5;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Producteur;

public class MessageX implements Message {
    private _Producteur producteur;
    private int message;


    public MessageX (_Producteur producteur) {
        this.message = producteur.nombreDeMessages();
        this.producteur = producteur;
    }

    @Override
    public String toString() {
        return "Prod"+this.producteur.identification()+"("+this.message+")";
    }
}