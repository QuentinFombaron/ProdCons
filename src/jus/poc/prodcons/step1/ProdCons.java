package jus.poc.prodcons.step1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

import java.util.LinkedList;
import java.util.Queue;


public class ProdCons implements Tampon {
    private Queue<Message> buffer;
    private int bufferSizeMax;

    public ProdCons (int tamponSizeMax) {
        this.buffer = new LinkedList<Message>();
        this.bufferSizeMax = tamponSizeMax;
    }

    public Queue<Message> getTampon () {
        return buffer;
    }

    @Override
    public synchronized void put(_Producteur producteur, Message message) throws Exception, InterruptedException {
        while (this.enAttente() == 0) {
            try {
                wait();
            } catch (Exception ignored) {}
        }

        this.buffer.add(message);
        System.out.println("Producteur "+producteur.identification()+" a produit le message : "+message.toString());
        System.out.println("Tampon : "+this.buffer);
        notifyAll();
    }

    @Override
    public synchronized Message get(_Consommateur consommateur) throws Exception, InterruptedException {
        while (this.buffer.isEmpty()) {
            try {
                wait();
            } catch (Exception ignored) {}
        }

        Message message_r = this.buffer.poll();
        System.out.println("Consommateur "+consommateur.identification()+" a consommer le message : "+message_r.toString());
        System.out.println("Tampon : "+this.buffer);
        notifyAll();

        return message_r;
    }

    @Override
    public int enAttente() {
        return this.bufferSizeMax - this.buffer.size();
    }

    @Override
    public int taille() {
        return this.bufferSizeMax;
    }
}