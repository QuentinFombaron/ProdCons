package jus.poc.prodcons.step1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

import java.util.LinkedList;
import java.util.Queue;


public class ProdCons implements Tampon {
    private Queue<Message> buffer;
    private int bufferTailleMax;
    private LinkedList<Producteur> producteurList;

    /**
     * Constructeur de ProdCons
     * @param bufferTailleMax Définit la taille du buffer
     */

    public ProdCons (int bufferTailleMax, LinkedList<Producteur> producteurList) {
        this.buffer = new LinkedList<Message>();
        this.bufferTailleMax = bufferTailleMax;
        this.producteurList = producteurList;
    }

    /**
     * Getter du buffer
     * @return La liste représentant le buffer
     */

    public Queue<Message> getTampon () {
        return buffer;
    }

    /**
     * Ajouter un message dans le buffer
     * @param producteur Objet du Producteur qui ajoute le message dans le buffer
     * @param message Message a ajouter dans le buffer
     **/

    @SuppressWarnings("Duplicates")
    @Override
    public synchronized void put(_Producteur producteur, Message message) throws Exception, InterruptedException {
        while (this.enAttente() == 0) {
            try {
                wait();
            } catch (Exception ignored) {}
        }

        this.buffer.add(message);
        System.out.println("Producteur "+producteur.identification()+" produit le message : "+message.toString());
        System.out.println("--> Buffer : "+this.buffer+"\n");
        notifyAll();
    }

    /**
     * Récupérer un message dans le buffer
     * @param consommateur Objet du Consommateur qui récupère le message dans le buffer
     **/

    @SuppressWarnings("Duplicates")
    @Override
    public synchronized Message get(_Consommateur consommateur) throws Exception, InterruptedException {
        while (this.buffer.isEmpty() && !(producteurList.isEmpty())) {
            try {
                wait();
            } catch (Exception ignored) {}
        }

        Message message_r = this.buffer.poll();

        System.out.println("Consommateur" + consommateur.identification() + " consomme le message : " + message_r.toString());
        System.out.println("--> Buffer : " + this.buffer + "\n");
        notifyAll();

        return message_r;
    }

    /**
     * @return Le nombre de message qu'il est possible de mettre dans le buffer
     */

    @Override
    public int enAttente() {
        return this.bufferTailleMax - this.buffer.size();
    }

    /**
     * @return La taille actuelle du buffer (le nombre de message disponible)
     */

    @Override
    public int taille() {
        return this.buffer.size();
    }
}