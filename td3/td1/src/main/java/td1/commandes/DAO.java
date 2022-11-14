package td1.commandes;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static td1.commandes.Categorie.*;
import td1.paires.Paire;

public class DAO {
    private List<Commande> commandes;

    private DAO(Commande c1, Commande ...cs) {
        commandes = new ArrayList<>();
        commandes.add(c1);
        commandes.addAll(List.of(cs));
    }

    public static DAO instance = null;

    public static final DAO instance() {
        if (instance == null) {
            Produit camembert = new Produit("Camembert", 4.0, NORMAL);
            Produit yaourts = new Produit("Yaourts", 2.5, INTERMEDIAIRE);
            Produit masques = new Produit("Masques", 25.0, REDUIT);
            Produit gel = new Produit("Gel", 5.0, REDUIT);
            Produit tournevis = new Produit("Tournevis", 4.5, NORMAL);
            //
            Commande c1 = new Commande()
                .ajouter(camembert, 1)
                .ajouter(yaourts, 6);
            Commande c2 = new Commande()
                .ajouter(masques, 2)
                .ajouter(gel, 10)
                .ajouter(camembert, 2)
                .ajouter(masques, 3);
            //
            instance = new DAO(c1,c2);
        }
        return instance;
    }

    /**
     * liste de toutes les commandes
     */
    public List<Commande> commandes() { return commandes; }

    /**
     * ensemble des différents produits commandés
     */
    public Set<Produit> produits() {
        /*
             solution alternative :

            public Set<Produit> produits() {
                Set<Commande> commandes= new HashSet<Commande>(commandes());
                Set<String> retour=new HashSet<String>();
                for(Commande object : set )
                {
                    retour.add(object.fst());
                }
                return retour;
                 
          
         */
        return commandes.stream()
                .flatMap(c -> c.lignes().stream())  // on effectue un flatmapping des éléments de commandes
                .map(Paire::fst)  // méthode intermédiaire qui applique la fonction fst de la classe paire
                .collect(Collectors.toSet()); //méthode finale qui renvoie un set du nom de chaque commande
    }

    /**
     * liste des commandes vérifiant un prédicat
     */
    public List<Commande> selectionCommande(Predicate<Commande> p) {
        return commandes.stream()
            .filter(p) // on filtre les commandes par une predicat p donné en parametres
            .collect(Collectors.toList());// méthode finale qui retourne une liste des commandes ayant vérifié un prédicat
    }

    /**
     * liste des commandes dont au moins une ligne vérifie un prédicat
     */
    public List<Commande> selectionCommandeSurExistanceLigne(Predicate<Paire<Produit,Integer>> p) {
        return commandes.stream()
            .filter(c -> c.lignes().stream().anyMatch(p)) // on filtre chaque ligne des commandes, on garde toutes les commandes ou au moins une ligne vérifie un prédicat p passé en parametres
            .collect(Collectors.toList());    // méthode finale qui retourne une liste des commandes 
    }

    /**
     * ensemble des différents produits commandés vérifiant un prédicat
     */
    public Set<Produit> selectionProduits(Predicate<Produit> p) {
        return produits()
            .stream()
            .filter(p)// on filtre les produits par un prédicat p donné en parametres
            .collect(Collectors.toSet());// méthode finaale qui retourne un set des produits ayant vérifié le prédicat p
    }

}
