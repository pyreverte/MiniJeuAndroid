![Last Build](https://github.com/pyreverte/MiniJeuAndroid/actions/workflows/gradle.yml/badge.svg)

# MiniJeuAndroid

## Contributeurs

![](https://avatars.githubusercontent.com/u/19470393?s=30&v=4)
[Pierre-Yves Reverte](https://github.com/pyreverte)

![](https://avatars.githubusercontent.com/u/46321901?s=30&v=4)
[Jacob Simmonds](https://github.com/JakeSimmo)

## Déroulement du Jeu

L'objectif est d'empêcher un cube de sortir de l'écran de la tablette, pour cela le joueur a deux possibilités :

- Toucher le cube ce qui va le faire repartir dans une direction aléatoire
- Déplacer la tablette pour "suivre" le cube

## Fin de partie

La partie se termine lorsque la balle atteint le bord de l'écran.

## Implémentation des capteurs

### Luminomètre

Lorsque la luminosité est au dessous (resp. au dessus) d'une certaine mesure l'application passe en dark mode (resp. light mode)

### Accélérometre

Lorsque l'utilisateur déplace la tablette sur le plan x/y cela déplace l'environnement du jeu sur cette axe.

Exemple : si l'utilisateur déplace la tablette vers le sud-est est que la balle se déplace aussi dans cette direction elle fera du surplace sur l'écran.

### Ecran tactile

Lorsque le joueur touche la balle, le score augmente de 1 et la balle prend une nouvelle direction.
