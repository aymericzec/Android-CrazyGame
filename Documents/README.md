
# CrazyGame: Documentation utilisateur

CrazyGame est une application androïd qui propose un ensemble de mini jeux multijoueur en ligne.


## Démarrage de l'application

CrazyGame ne demande pas d'inscription pour pouvoir jouer, en effet vous pouvez jouer anonymement sans utiliser de pseudo. 
Au démarrage du jeu, on voit afficher la liste des jeux de CrazyGame. En glissant vers la gauche on peut accéder à nos scores et en glissant vers la droite on accède au classement général.

## Lancement d'un jeux
Si vous souhaitez lancer un jeu, il suffit de cliquer sur le jeu en question. Ensuite vous serez mis en attente d'un adversaire. Il n'est pas possible de changer le jeu sélectionné tant qu'aucun adversaire n'a pas été trouvé.

## Morpion
Notre jeu du morpion est un jeu de morpion classique. A son lancement, l'un des deux joueurs doit commencer. Vous avez un message en bas de l'écran pour vous indiquer si c'est votre tour ou celui de votre adversaire.
Les deux joueurs jouent à tour de rôle pour poser un pion sur la grille (l'un a des ronds, l'autre des croix). Le premier qui arrive à aligner trois de ses pions gagne !
Une fois la partie terminée, un message vous indique si vous avez gagné ou perdu puis vous pourrez retourner au menu principal qui contient la liste des jeux.

## MixWord
Ce jeu consiste à retrouver un mot plus vite que son adversaire. Au début du jeu vous avez un ensemble de cases vides, et un ensemble de lettre qui forme un mot mélangé. Le but est de reconstituer le mot le plus rapidement possible. Pour cela il faut cliquer sur la lettre que vous souhaiter placer puis recliquer dessus pour le enlever. Toutes les lettres doivent être utilisées.
Une fois que vous pensez avoir trouvé le mot il faut valider la réponse. Dans ce cas, votre adversaire recevra une notification comme quoi vous avez été le plus rapide, si votre réponse est fausse vous pourrez recommencer.
A la fin du jeu vous retournez au menu principal.

## ShakeGame
Ce jeu consiste à secouer votre téléphone le plus rapidement possible dans un temps imparti. Au lancement du jeu vous avez un premier décompte de 3 secondes avant que le jeu ne démarre. Ensuite vous avez 10 secondes pour secouer votre téléphone le plus rapidement possible.
A la fin de ce temps vous recevrez un message avec votre score et celui de votre adversaire pour savoir qui a gagné. Nous calculons la moyenne du secouage.

## Vos scores
Vos scores sont constitué du total de vos parties pour chaque jeux et du total de vos parties gagner pour chaque jeux.

## Classement générale
Le classement général liste l'ensemble des jeux en les triant en fonction du nombre de parties jouées par l’ensemble des joueur de CrazyGame.


## Attention

Pour éxécuter le serveur, il est important de configurer l'ip et le port. Pour le client Android il suffit de modifier dans SearchGame et le Service. Pour le serveur il faut modifier dans le json config.

