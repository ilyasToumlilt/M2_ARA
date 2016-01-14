# M2_ARA - Algorithmique Réparties Avancée

Pour les quelques TPs qu'on a eu ...

###TP-01

#####Q1:
J'ai rajouté une nouvelle version, helloWorldRing pour l'implémentation
de l'anneau, et donc une nouvelle cible au Makefile : make run_ring <br/>

Pour la reproductibilité de la simulation, j'ai défini un random seed dans
config_file_ring.cfg

#####Q2:
J'ai récupéré les sources dans opt/, et casté les HWTransport en MatrixTransport dans
HelloWorld.java

#####Q3:
Je génére une faute sur le noeud 8 quand le noeud 7 reçoit le jeton helloWorld,
comme toute logique l'aurait prédit, l'exécution s'arrête à la réception du
noeud 7.