Début du tour du chasseur :

La vue du hunter s'affiche -> showView()

Hunter joue (Tir) -> Icoordinate coord
    coord -> Coordonnée de la case

Maze reçoit les coordonnée, met à jour la variable hunter avec les nouvelle coordonée
Le Maze vérifie si le monstre est déjà passé dessus
Si true :
    Le Maze vérifie si le monstre est actuellement sur cette case 
    Si true : 
        Le chasseur a gagné
    Sinon :
        Le Maze notifie la vue du chasseur en lui envoyant un new CellEvent(coord, turn, cellinfo)
            coord : Coordonnée de la case
            turn : Tour durant lequel le monstre est passé sur la case
            cellinfo : Cellinfo.MONSTER
Sinon : 
    Le Maze vérifie si la case est un mur
    Si true :
        Le Maze notifie la vue du chasseur en lui envoyant un new CellEvent(coord, turn, cellinfo)
            coord : Coordonnée de la case
            turn -> 0
            cellinfo -> Cellinfo.WALL
    Sinon : 
        Le Maze notifie la vue du chasseur en lui envoyant un new CellEvent(coord, turn, cellinfo)
            coord -> Coordonnée de la case
            turn -> 0
            cellinfo -> Cellinfo.EMPTY

La vue du chasseur contient une variable hunter de type Hunter qui représente ses donnée
La vue du chasseur les mets quand elle reçoit la notification CellEvent(coord, turn, cellinfo) du Maze
Si cellinfo == MONSTER : 
    la variable hunter ajoute dans la map knowCoord les coordonée du monstre et le tour put(coord, turn)
Sinon si cellinfo == WALL :
    la variable hunter met à jour son tableau de boolean avec true à l'endroit qui correspond au coord
Sinon :
    la variable hunter met à jour son tableau de boolean avec false à l'endroit qui correspond au coord
    
Le maze incrémente les tours







coord -> 5,3

