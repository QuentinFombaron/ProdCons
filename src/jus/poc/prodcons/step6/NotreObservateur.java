package jus.poc.prodcons.step6;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class NotreObservateur {
    private boolean InitOK;

    public NotreObservateur() {
        this.InitOK = false;
    }

    public void init(int nbProd, int nbCons, int nbBuffer) throws ControlException {
        if (nbProd > 0 && nbCons > 0 && nbBuffer > 0) {
            this.InitOK = true;
        }
        else {
            throw new ControlException(this.getClass(), "init");
        }
    }

    public void productionMessage(_Producteur prod, Message message, int tempsDeTraitement) throws ControlException {
        if (prod == null || message == null || tempsDeTraitement == 0 || !this.InitOK) {
            throw new ControlException(this.getClass(), "productionMessage");
        }
    }

    public void depotMessage(_Producteur prod, Message message) throws ControlException {
        if (prod == null || message == null || !this.InitOK) {
            throw new ControlException(this.getClass(), "depotMessage");
        }
    }

    public void retraitMessage(_Consommateur conso, Message message) throws ControlException {
        if (conso == null || message == null || !this.InitOK) {
            throw new ControlException(this.getClass(), "retraitMessage");
        }
    }

    public void consommationMessage(_Consommateur conso, Message message, int tempsDeTraitement) throws ControlException {
        if (conso == null || message == null || tempsDeTraitement == 0 || !this.InitOK) {
            throw new ControlException(this.getClass(), "consommationMessage");
        }
    }

    public void newProducteur(_Producteur prod) throws ControlException {
        if (prod == null || !this.InitOK) {
            throw new ControlException(this.getClass(), "newProducteur");
        }
    }

    public void newConsommateur(_Consommateur conso) throws ControlException {
        if (conso == null || !this.InitOK) {
            throw new ControlException(this.getClass(), "newConsommateur");
        }
    }
}
