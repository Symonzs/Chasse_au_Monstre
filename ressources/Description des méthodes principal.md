Tour de jeu chasseur :

afficherHunterView()
le controller attend que le joueur clique sur une case
récupérer la case cliquée
le controller met à jour le modèle en remplaçant, pour ce tour, la case cliquée par un chasseur
le modèle notifie la vue du changement
la vue récupère le modèle et affiche la nouvelle vue

si la case est vide
    ne pas faire de changement

sinon si la case est un mur
    remplacer, sur sa vue, la case selectionnée par un mur

sinon si la case est un monstre
    remplacer, sur sa vue, la case selectionnée par un monstre
    le chasseur à remporté la partie

sinon si la case a déjà été traversée par le monstre
    remplacer, sur sa vue, la case selectionnée par le numéro du tour où est passé le monstre

