![Last Build](https://github.com/pyreverte/MiniJeuAndroid/actions/workflows/gradle.yml/badge.svg)

# MiniJeuAndroid

[Dépôt GitHub](https://github.com/pyreverte/MiniJeuAndroid)

## Contributeurs

![](https://avatars.githubusercontent.com/u/19470393?s=30&v=4)
[Pierre-Yves Reverte](https://github.com/pyreverte)

![](https://avatars.githubusercontent.com/u/46321901?s=30&v=4)
[Jacob Simmonds](https://github.com/JakeSimmo)

## Instructions

### Pré-requis

Une tablette ou un appareil Android virtuel avec minimum la Android 6.0 (Marshmallow, API 23)

### Exécution

Démarrer le projet dans Android Studio suffit à l'exécution de l'application. Il est préférable que l'appareil soit connecté à internet pour pouvoir exploiter les fonctionnalités de consultation et d'enregistrement de score.

## Déroulement du Jeu

L'objectif est d'empêcher un cube de sortir de l'écran de la tablette, pour cela le joueur a deux possibilités :

- Toucher le cube ce qui va le faire repartir dans une direction aléatoire

## Fin de partie

La partie se termine lorsque la balle atteint le bord de l'écran.

## Fonctionnalités

- Visualisation du plus haut score sur le menu de l'application
- Le joueur peut enregistrer son score après chaque partie et 

## Implémentation des capteurs

### Luminomètre

Lorsque la luminosité est au dessous (resp. au dessus) d'une certaine mesure l'application passe en dark mode (resp. light mode)

### Ecran tactile

Lorsque le joueur touche la balle, le score augmente de 1 et la balle prend une nouvelle direction.


