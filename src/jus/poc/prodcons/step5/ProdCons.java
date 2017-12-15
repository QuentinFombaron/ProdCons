package jus.poc.prodcons.step5;

import jus.poc.prodcons.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ProdCons implements Tampon {
    private Queue<Message> buffer;
    private int bufferTailleMax;
    private LinkedList<Producteur> producteurList;
    private Observateur observateur;

    private Lock lock;
    private Condition notFull;
    private Condition notEmpty;

    /**
     * Constructeur de ProdCons
     * @param bufferTailleMax Définit la taille du buffer
     */

    public ProdCons (int bufferTailleMax, LinkedList<Producteur> producteurList, Observateur observateur) {
        this.buffer = new LinkedList<Message>();
        this.bufferTailleMax = bufferTailleMax;
        this.producteurList = producteurList;
        this.observateur = observateur;

        this.lock = new ReentrantLock();
        this.notFull  = lock.newCondition();
        this.notEmpty = lock.newCondition();
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
    public void put(_Producteur producteur, Message message) throws Exception, InterruptedException {
        lock.lock();
        try {
            while (this.enAttente() == 0) {
                notFull.await();
            }
            this.buffer.add(message);
            this.observateur.depotMessage(producteur, message);
            System.out.println("Producteur " + producteur.identification() + " produit le message : " + message.toString());
            System.out.println("--> Buffer : " + this.buffer + "\n");
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Récupérer un message dans le buffer
     * @param consommateur Objet du Consommateur qui récupère le message dans le buffer
     **/

    /* NOTE: Certains consommateurs restent bloqués dans le wait() dans le cas où ils sont déjà dans le wait() lorsque
    le dernier producteur s'éteind et plus aucun message n'est disponible dans le buffer */

    @SuppressWarnings("Duplicates")
    @Override
    public  Message get(_Consommateur consommateur) throws Exception, InterruptedException {
        lock.lock();
        try {
            while (this.buffer.isEmpty() && !(producteurList.isEmpty())) {
                notEmpty.await();
            }
            Message message_r = this.buffer.poll();
            this.observateur.retraitMessage(consommateur, message_r);
            System.out.println("Consommateur" + consommateur.identification() + " consomme le message : " + message_r.toString());
            System.out.println("--> Buffer : " + this.buffer + "\n");
            notFull.signalAll();
            return message_r;
        } finally {
            lock.unlock();
        }
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