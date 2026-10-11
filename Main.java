
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        Banque banque = new Banque();
        banque.chargerDonnees();
        int choix;

        do {
            System.out.println("\n================================");
            System.out.println("       BANK MANAGEMENT");
            System.out.println("================================");
            System.out.println("1.  Ajouter un client");
            System.out.println("2.  Afficher les clients");
            System.out.println("3.  Rechercher un client");
            System.out.println("4.  Modifier un client");
            System.out.println("5.  Supprimer un client");
            System.out.println("6.  Créer un compte courant");
            System.out.println("7.  Créer un compte épargne");
            System.out.println("8.  Afficher les comptes");
            System.out.println("9.  Rechercher un compte");
            System.out.println("10. Effectuer un dépôt");
            System.out.println("11. Effectuer un retrait");
            System.out.println("12. Effectuer un virement");
            System.out.println("13. Appliquer les intérêts");
            System.out.println("14. Consulter l'historique");
            System.out.println("15. Sauvegarder les données");
            System.out.println("16. Recharger les données");
            System.out.println("0.  Quitter");
            System.out.println("================================");

            choix = lireInt("Votre choix : ");

            switch (choix) {

                case 1:
                    ajouterClient(banque);
                    break;

                case 2:
                    banque.afficherClients();
                    break;

                case 3:
                    rechercherClient(banque);
                    break;

                case 4:
                    modifierClient(banque);
                    break;

                case 5:
                    supprimerClient(banque);
                    break;

                case 6:
                    creerCompteCourant(banque);
                    break;

                case 7:
                    creerCompteEpargne(banque);
                    break;

                case 8:
                    banque.afficherComptes();
                    break;

                case 9:
                    rechercherCompte(banque);
                    break;

                case 10:
                    int numeroDepot = lireInt("Numéro du compte : ");
                    double montantDepot = lireDouble("Montant du dépôt : ");
                    banque.deposer(numeroDepot, montantDepot);
                    break;

                case 11:
                    int numeroRetrait = lireInt("Numéro du compte : ");
                    double montantRetrait = lireDouble("Montant du retrait : ");
                    banque.retirer(numeroRetrait, montantRetrait);
                    break;

                case 12:
                    int source = lireInt("Numéro du compte source : ");
                    int destination = lireInt("Numéro du compte destination : ");
                    double montantVirement = lireDouble("Montant du virement : ");

                    banque.virement(source, destination, montantVirement);
                    break;

                case 13:
                    appliquerInterets(banque);
                    break;
                case 14:
                    banque.afficherHistorique();
                    break;
                    case 15:
                    banque.sauvegarderDonnees();
                    break;

                case 16:
                  banque.chargerDonnees();
                  break;

                case 0:
                    System.out.println("Merci d'avoir utilisé Bank Management !");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 0);
        
        banque.sauvegarderDonnees();
        sc.close();
        
    }

    // ==================== SAISIE SECURISEE ====================

    private static int lireInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Erreur : entrez un nombre entier.");
            }
        }
    }

    private static double lireDouble(String message) {
        while (true) {
            System.out.print(message);

            try {
                String valeur = sc.nextLine().trim().replace(',', '.');
                double nombre = Double.parseDouble(valeur);

                if (!Double.isFinite(nombre)) {
                    System.out.println("Erreur : nombre invalide.");
                    continue;
                }

                return nombre;

            } catch (NumberFormatException e) {
                System.out.println("Erreur : entrez un nombre valide.");
            }
        }
    }

    private static String lireTexte(String message) {
        while (true) {
            System.out.print(message);
            String texte = sc.nextLine().trim();

            if (!texte.isEmpty()) {
                return texte;
            }

            System.out.println("Ce champ ne peut pas être vide.");
        }
    }

    // ==================== CLIENTS ====================

    private static void ajouterClient(Banque banque) {
        System.out.println("\n--- Ajouter un client ---");

        int id = lireInt("ID : ");
        String nom = lireTexte("Nom : ");
        String prenom = lireTexte("Prénom : ");
        String email = lireTexte("Email : ");
        String telephone = lireTexte("Téléphone : ");

        Client client = new Client(id, nom, prenom, email, telephone);
        banque.ajouterClient(client);
    }

    private static void rechercherClient(Banque banque) {
        int id = lireInt("ID du client à rechercher : ");
        Client client = banque.rechercherClient(id);

        if (client != null) {
            client.afficherInformations();
        } else {
            System.out.println("Client introuvable.");
        }
    }

    private static void modifierClient(Banque banque) {
        int id = lireInt("ID du client à modifier : ");

        Client client = banque.rechercherClient(id);

        if (client == null) {
            System.out.println("Client introuvable.");
            return;
        }

        String nom = lireTexte("Nouveau nom : ");
        String prenom = lireTexte("Nouveau prénom : ");
        String email = lireTexte("Nouvel email : ");
        String telephone = lireTexte("Nouveau téléphone : ");

        if (banque.modifierClient(id, nom, prenom, email, telephone)) {
            System.out.println("Client modifié avec succès !");
        } else {
            System.out.println("Modification impossible.");
        }
    }

    private static void supprimerClient(Banque banque) {
        int id = lireInt("ID du client à supprimer : ");

        if (banque.supprimerClient(id)) {
            System.out.println("Client supprimé avec succès !");
        } else {
            System.out.println("Suppression impossible ou client introuvable.");
        }
    }

    // ==================== CREATION DES COMPTES ====================

    private static Client choisirProprietaire(Banque banque) {
        int id = lireInt("ID du propriétaire : ");
        Client client = banque.rechercherClient(id);

        if (client == null) {
            System.out.println("Client introuvable. Créez d'abord ce client.");
        }

        return client;
    }

    private static void creerCompteCourant(Banque banque) {
        System.out.println("\n--- Créer un compte courant ---");

        Client proprietaire = choisirProprietaire(banque);

        if (proprietaire == null) {
            return;
        }

        int numero = lireInt("Numéro du compte : ");
        double solde = lireDouble("Solde initial (positif ou nul) : ");
        double decouvert = lireDouble("Découvert autorisé : ");

        if (solde < 0 || decouvert < 0) {
            System.out.println("Le solde initial et le découvert doivent être positifs ou nuls.");
            return;
        }

        Compte compte = new CompteCourant(
            numero, solde, proprietaire, decouvert
        );

        banque.ajouterCompte(compte);
    }

    private static void creerCompteEpargne(Banque banque) {
        System.out.println("\n--- Créer un compte épargne ---");

        Client proprietaire = choisirProprietaire(banque);

        if (proprietaire == null) {
            return;
        }

        int numero = lireInt("Numéro du compte : ");
        double solde = lireDouble("Solde initial (positif ou nul) : ");
        double taux = lireDouble("Taux d'intérêt (%) : ");

        if (solde < 0 || taux < 0) {
            System.out.println("Le solde et le taux doivent être positifs ou nuls.");
            return;
        }

        Compte compte = new CompteEpargne(
            numero, solde, proprietaire, taux
        );

        banque.ajouterCompte(compte);
    }

    private static void rechercherCompte(Banque banque) {
        int numero = lireInt("Numéro du compte à rechercher : ");

        try {
            Compte compte = banque.rechercherCompte(numero);
            compte.afficherType();
            compte.afficherCompte();

        } catch (CompteIntrouvableException e) {
            System.out.println(e.getMessage());
        }
    }

    // ==================== INTERETS ====================

    private static void appliquerInterets(Banque banque) {
        int numero = lireInt("Numéro du compte épargne : ");

        try {
            Compte compte = banque.rechercherCompte(numero);

            if (compte instanceof CompteEpargne) {
                ((CompteEpargne) compte).appliquerInterets();
            } else {
                System.out.println(
                    "Ce compte n'est pas un compte épargne."
                );
            }

        } catch (CompteIntrouvableException e) {
            System.out.println(e.getMessage());
        }
    }
}
