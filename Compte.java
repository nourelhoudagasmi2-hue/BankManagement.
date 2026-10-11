public abstract class Compte {

    private int numero;
    private double solde;
    private Client proprietaire;

    public Compte(int numero, double solde, Client proprietaire) {
        this.numero = numero;
        this.solde = solde;
        this.proprietaire = proprietaire;
    }

    public int getNumero() {
        return numero;
    }

    public double getSolde() {
        return solde;
    }

    public Client getProprietaire() {
        return proprietaire;
    }

    public void deposer(double montant)
        throws MontantInvalideException {if (montant <= 0) {
    throw new MontantInvalideException(
        "Le montant du dépôt doit être positif."
    );
     }

        solde += montant;

        System.out.println("Dépôt effectué : " + montant + " €");
    }

    protected void modifierSolde(double nouveauSolde) {
        solde = nouveauSolde;
    }

    public abstract void retirer(double montant)
        throws SoldeInsuffisantException,MontantInvalideException;

    public abstract void afficherType();

    public void afficherCompte() {

        System.out.println("Numéro : " + numero);
        System.out.println("Solde : " + solde);
        System.out.println(
            "Propriétaire : "
            + proprietaire.getPrenom()
            + " "
            + proprietaire.getNom()
        );
    }
}