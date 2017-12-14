package jus.poc.prodcons.step2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;


public class ProdCons implements Tampon {
    private Queue<Message> buffer;
    private int bufferTailleMax;
    private Semaphore semConsommateur;
    private Semaphore semProducteur;
    private LinkedList<Producteur> producteurList;

    /**
     * Constructeur de ProdCons
     * @param bufferTailleMax Définit la taille du buffer
     */

    public ProdCons (int bufferTailleMax, LinkedList<Producteur> producteurList) {
        this.buffer = new LinkedList<Message>();
        this.bufferTailleMax = bufferTailleMax;
        this.semConsommateur = new Semaphore(1, true);
        this.semProducteur = new Semaphore(1, true);
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
    public void put(_Producteur producteur, Message message) throws Exception, InterruptedException {
        semProducteur.acquire();
        synchronized (this) {
            while (this.enAttente() == 0) {
                try {
                    wait();
                } catch (Exception ignored) {
                }
            }

            this.buffer.add(message);
            System.out.println("Producteur " + producteur.identification() + " produit le message : " + message.toString());
            System.out.println("--> Buffer : " + this.buffer + "\n");
            notifyAll();
        }
        semProducteur.release();
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
        semConsommateur.acquire();
        try {
            synchronized (this) {
                while (this.buffer.isEmpty() && !(producteurList.isEmpty())) {
                    try {
                        wait();
                    } catch (Exception ignored) {
                    }
                }

                Message message_r = this.buffer.poll();
                try {
                    System.out.println("Consommateur" + consommateur.identification() + " consomme le message : " + message_r.toString());
                    System.out.println("--> Buffer : " + this.buffer + "\n");
                    notifyAll();
                } catch (NullPointerException e) {
                    System.out.println("/!\\ Consommateur" + consommateur.identification() + " n'a pas pu obtenir de message malgré sa demande\n");
                }
                return message_r;
            }
        } finally {
            semConsommateur.release();
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