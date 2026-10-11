public class CompteCourant extends Compte {

    private double decouvertAutorise;

    public CompteCourant(
            int numero,
            double solde,
            Client proprietaire,
            double decouvertAutorise) {

        super(numero, solde, proprietaire);

        this.decouvertAutorise = decouvertAutorise;
    }

   
@Override
public void retirer(double montant)
        throws SoldeInsuffisantException , MontantInvalideException {
if (montant <= 0) {
    throw new MontantInvalideException(
        "Le montant du retrait doit être positif."
    );
}


    if (getSolde() - montant >= -decouvertAutorise) {
        modifierSolde(getSolde() - montant);
        System.out.println("Retrait effectué : " + montant + " €");
    } else {
        throw new SoldeInsuffisantException(
            "Découvert autorisé dépassé."
        );
    }
}

    @Override
    public void afficherType() {
        System.out.println("Type : Compte courant");
    }
    public double getDecouvertAutorise() {
    return decouvertAutorise;
}
}
