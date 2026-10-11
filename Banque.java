
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;



public class Banque {

    // ==================== ATTRIBUTS ====================

    private ArrayList<Client> clients;
    private ArrayList<Compte> comptes;
    private ArrayList<String> historique;
    // ==================== CONSTRUCTEUR ====================

    public Banque() {
        clients = new ArrayList<>();
        comptes = new ArrayList<>();
        historique = new ArrayList<>();
    }

    // ==================== GESTION DES CLIENTS ====================

    // Ajouter un client
    public void ajouterClient(Client client) {
        if (client == null) {
            System.out.println("Erreur : client invalide.");
            return;
        }

        if (rechercherClient(client.getId()) != null) {
            System.out.println("Erreur : cet ID existe déjà.");
            return;
        }

        clients.add(client);
        System.out.println("Client ajouté avec succès !");
    }

    // Afficher tous les clients
    public void afficherClients() {
        if (clients.isEmpty()) {
            System.out.println("Aucun client.");
            return;
        }

        System.out.println("\n===== LISTE DES CLIENTS =====");

        for (Client client : clients) {
            client.afficherInformations();
            System.out.println("--------------------");
        }
    }

    // Rechercher un client par ID
    public Client rechercherClient(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return client;
            }
        }

        return null;
    }

    // Modifier un client
    public boolean modifierClient(
            int id,
            String nouveauNom,
            String nouveauPrenom,
            String nouvelEmail,
            String nouveauTelephone) {

        Client client = rechercherClient(id);

        if (client == null) {
            return false;
        }

        client.setNom(nouveauNom);
        client.setPrenom(nouveauPrenom);
        client.setEmail(nouvelEmail);
        client.setTelephone(nouveauTelephone);

        return true;
    }

    // Supprimer un client
    public boolean supprimerClient(int id) {
        Client client = rechercherClient(id);

        if (client == null) {
            return false;
        }

        // Ne pas supprimer un client qui possède encore un compte
        for (Compte compte : comptes) {
            if (compte.getProprietaire().getId() == id) {
                System.out.println(
                    "Impossible : ce client possède encore un compte."
                );
                return false;
            }
        }

        clients.remove(client);
        return true;
    }

    // ==================== GESTION DES COMPTES ====================

    // Ajouter un compte
    public void ajouterCompte(Compte compte) {
        if (compte == null) {
            System.out.println("Erreur : compte invalide.");
            return;
        }

        // Vérifier que le numéro de compte est unique
        try {
            rechercherCompte(compte.getNumero());

            System.out.println(
                "Erreur : ce numéro de compte existe déjà."
            );
            return;

        } catch (CompteIntrouvableException e) {
            // Le compte n'existe pas : on peut continuer.
        }

        // Vérifier que le propriétaire est un client enregistré
        Client proprietaire = compte.getProprietaire();

        if (proprietaire == null
                || rechercherClient(proprietaire.getId()) != proprietaire) {
            System.out.println(
                "Erreur : le propriétaire doit être un client enregistré."
            );
            return;
        }

        comptes.add(compte);
        System.out.println("Compte ajouté avec succès !");
    }

    // Afficher tous les comptes
    public void afficherComptes() {
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte.");
            return;
        }

        System.out.println("\n===== LISTE DES COMPTES =====");

        for (Compte compte : comptes) {
            compte.afficherType();
            compte.afficherCompte();
            System.out.println("--------------------");
        }
    }

    // Rechercher un compte par numéro
    public Compte rechercherCompte(int numero)
            throws CompteIntrouvableException {

        for (Compte compte : comptes) {
            if (compte.getNumero() == numero) {
                return compte;
            }
        }

        throw new CompteIntrouvableException(
            "Compte introuvable : numéro " + numero
        );
    }

    // ==================== DEPOT ====================

    public void deposer(int numeroCompte, double montant) {
        try {
            Compte compte = rechercherCompte(numeroCompte);
            compte.deposer(montant);
            enregistrerOperation(
    "Dépôt | Compte : " + numeroCompte
    + " | Montant : " + montant + " €"
  );

        } catch (CompteIntrouvableException
                 | MontantInvalideException e) {

            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // ==================== RETRAIT ====================

    public void retirer(int numeroCompte, double montant) {
        try {
            Compte compte = rechercherCompte(numeroCompte);
            compte.retirer(montant);
            enregistrerOperation(
    "Retrait | Compte : " + numeroCompte
    + " | Montant : " + montant + " €"
 );

        } catch (CompteIntrouvableException
                 | SoldeInsuffisantException
                 | MontantInvalideException e) {

            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // ==================== VIREMENT ====================

    public void virement(
            int numeroSource,
            int numeroDestination,
            double montant) {

        // Vérifier le montant
        if (!Double.isFinite(montant) || montant <= 0) {
            System.out.println("Erreur : montant invalide.");
            return;
        }

        // Vérifier que les comptes sont différents
        if (numeroSource == numeroDestination) {
            System.out.println(
                "Erreur : les deux comptes doivent être différents."
            );
            return;
        }

        try {
            // Rechercher les deux comptes avant toute opération
            Compte source = rechercherCompte(numeroSource);
            Compte destination = rechercherCompte(numeroDestination);

            // Retirer l'argent du compte source
            source.retirer(montant);

            // Déposer l'argent sur le compte destination
            destination.deposer(montant);

            System.out.println("Virement effectué avec succès !");
            enregistrerOperation(
    "Virement | Source : " + numeroSource
    + " | Destination : " + numeroDestination
    + " | Montant : " + montant + " €"
  ) ;

        } catch (CompteIntrouvableException
                 | SoldeInsuffisantException
                 | MontantInvalideException e) {

            System.out.println(
                "Erreur de virement : " + e.getMessage()
            );
        }
            }
    
private void enregistrerOperation(String description) {
    DateTimeFormatter format =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    String date = LocalDateTime.now().format(format);

    historique.add(date + " | " + description);
}

public void afficherHistorique() {
    System.out.println("\n===== HISTORIQUE DES OPERATIONS =====");

    if (historique.isEmpty()) {
        System.out.println("Aucune opération enregistrée.");
        return;
    }

    for (String operation : historique) {
        System.out.println(operation);
    }
}

public void sauvegarderDonnees() {
    Path fichierClients = Paths.get("clients.txt");
    Path fichierComptes = Paths.get("comptes.txt");
    Path fichierHistorique = Paths.get("historique.txt");

    try {
        // Sauvegarder les clients
        List<String> lignesClients = new ArrayList<>();

        for (Client client : clients) {
            lignesClients.add(
                "C;" + client.getId()
                + ";" + client.getNom()
                + ";" + client.getPrenom()
                + ";" + client.getEmail()
                + ";" + client.getTelephone()
            );
        }

        Files.write(fichierClients, lignesClients);

        // Sauvegarder les comptes
        List<String> lignesComptes = new ArrayList<>();

        for (Compte compte : comptes) {
            if (compte instanceof CompteCourant) {
                CompteCourant courant = (CompteCourant) compte;

                lignesComptes.add(
                    "CC;" + compte.getNumero()
                    + ";" + compte.getSolde()
                    + ";" + compte.getProprietaire().getId()
                    + ";" + courant.getDecouvertAutorise()
                );

            } else if (compte instanceof CompteEpargne) {
                CompteEpargne epargne = (CompteEpargne) compte;

                lignesComptes.add(
                    "CE;" + compte.getNumero()
                    + ";" + compte.getSolde()
                    + ";" + compte.getProprietaire().getId()
                    + ";" + epargne.getTauxInteret()
                );
            }
        }

        Files.write(fichierComptes, lignesComptes);

        // Sauvegarder l'historique
        Files.write(fichierHistorique, historique);

        System.out.println("Données sauvegardées avec succès !");

    } catch (IOException e) {
        System.out.println(
            "Erreur pendant la sauvegarde : " + e.getMessage()
        );
    }
}

public void chargerDonnees() {
    Path fichierClients = Paths.get("clients.txt");
    Path fichierComptes = Paths.get("comptes.txt");
    Path fichierHistorique = Paths.get("historique.txt");

    try {
        // Charger les clients en premier
        if (Files.exists(fichierClients)) {
            clients.clear();

            for (String ligne : Files.readAllLines(fichierClients)) {
                String[] t = ligne.split(";", -1);

                if (t.length == 6 && t[0].equals("C")) {
                    Client client = new Client(
                        Integer.parseInt(t[1]),
                        t[2],
                        t[3],
                        t[4],
                        t[5]
                    );

                    clients.add(client);
                }
            }
        }

        // Charger les comptes après les clients
        if (Files.exists(fichierComptes)) {
            comptes.clear();

            for (String ligne : Files.readAllLines(fichierComptes)) {
                String[] t = ligne.split(";", -1);

                if (t.length != 5) {
                    continue;
                }

                int numero = Integer.parseInt(t[1]);
                double solde = Double.parseDouble(t[2]);
                int idClient = Integer.parseInt(t[3]);
                double parametre = Double.parseDouble(t[4]);

                Client proprietaire = rechercherClient(idClient);

                if (proprietaire == null) {
                    System.out.println(
                        "Compte ignoré : propriétaire introuvable."
                    );
                    continue;
                }

                if (t[0].equals("CC")) {
                    comptes.add(
                        new CompteCourant(
                            numero, solde, proprietaire, parametre
                        )
                    );

                } else if (t[0].equals("CE")) {
                    comptes.add(
                        new CompteEpargne(
                            numero, solde, proprietaire, parametre
                        )
                    );
                }
            }
        }

        // Charger l'historique
        if (Files.exists(fichierHistorique)) {
            historique.clear();
            historique.addAll(Files.readAllLines(fichierHistorique));
        }

        System.out.println("Données chargées avec succès !");

    } catch (IOException | NumberFormatException e) {
        System.out.println(
            "Erreur pendant le chargement : " + e.getMessage()
        );
    }
}


}
