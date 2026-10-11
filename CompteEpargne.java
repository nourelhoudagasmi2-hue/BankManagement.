public class CompteEpargne extends Compte {

    private double tauxInteret;

    public CompteEpargne(
            int numero,
            double solde,
            Client proprietaire,
            double tauxInteret) {

        super(numero, solde, proprietaire);

        this.tauxInteret = tauxInteret;
    }

    @Override
public void retirer(double montant)
        throws SoldeInsuffisantException,
               MontantInvalideException {

        if (montant <= 0) {
            throw new MontantInvalideException(
                "Le montant du retrait doit être positif."
            );
        }

        if (montant <= getSolde()) {

            modifierSolde(getSolde() - montant);

            System.out.println(
                "Retrait effectué : " + montant + " €"
            );

        } else {

    throw new SoldeInsuffisantException(
        "Solde insuffisant."
    );
}

        }

    public void appliquerInterets() {

        double interets = getSolde() * tauxInteret / 100;

        modifierSolde(getSolde() + interets);

        System.out.println(
            "Intérêts appliqués : " + interets + " €"
        );
    }

    @Override
    public void afficherType() {
        System.out.println("Type : Compte épargne");
    }
    public double getTauxInteret() {
    return tauxInteret;
}
}