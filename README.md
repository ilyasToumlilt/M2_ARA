# Projet ARA 2016 - Détection de défaillances

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
noeud 7. <br/>
<br/>
On peut ajuster le nombre de tours du message dans l'anneau via protocol.applicative.myNbStep ( config file ), et le nombre de step du controleur via control.controler.step. <br/>

### Participants :
    Alexandra Hospital - 3401862
    Ilyas     Toumlilt - 3261538

### Organisation des Fichiers

    src/* : contient un répertoire par expérimentation, les expérimentations
    	    demandées sont listées ci-dessous :
    src/ARA_Project_Q1
    src/ARA_Project_Q2_last
    src/ARA_Project_Q2_avg
    src/ARA_Project_Q2_max
    src/ARA_Project_Q3
    src/ARA_Project_Q4
    Makefile : permet de compiler et d'exécuter les expérimentations
    	       réalisées.

### directives du Makefile

    run_project_q1 :
    run_project_q2_last :
    run_project_q2_avg :
    run_project_q2_max :
    run_project_q3 :
    run_project_q4 :

### Détecteur 1 :

    Sources : /src/ARA_Project_Q1 <br/>
    Fichier config : /config_file_project_q1.cfg <br/>
    Directive Make : run_project_q1 <br/>
    Pour ce détecteur on a repris le programme du TP ( HelloWorld ), auquel on a
    rajouté un controleur ( Controler.java ), qui se base sur des arguments du
    fichier de configuration: step, la période de contrôle; et probFailure la
    probabilité de crash d'un noeud, le crash se fait par choix aléatoire et par
    envoi de message KILL au noeud à crasher. Le contrôleur s'arrêtte dès que
    tous les noeuds sont crashés. <br/>
    Pour la détection de fautes, on a rajouté deux types de messages: PING et PONG.
    Chaque noeud maintient un tableau suspicion_array où il stocke sa connaissance
    des noeuds du réseau, à chaque période ( suspectDuration dans le fichier de
    config ), il envoit un PING à tous les processus correctes et attend une réponse
    pendant ce délais, si le processus suspecté répond pendant cette période on
    considère qu'il est correcte, sinon on le considère FAILED, il se peut qu'un
    processus réponde après cette période et dans ce cas là on parlera de fausse
    détection. <br/>

### Détecteur 2 :

##### Last:

      Sources : /src/ARA_Project_Q2_last <br/>
      Fichier Config : /config_file_project_q2_last.cfg <br/>
      Directive Make : run_project_q2_last    <br/>

##### Avg:

      Sources : /src/ARA_Project_Q2_avg <br/>
      Fichier Config : /config_file_project_q2_avg.cfg <br/>
      Directive Make : run_project_q2_avg    <br/>
      
##### Max:

      Sources : /src/ARA_Project_Q2_max <br/>
      Fichier Config : /config_file_project_q2_max.cfg <br/>
      Directive Make : run_project_q2_max    <br/>

##### Commentaires:

      Empty

### Détecteur 3 :

      Sources : /src/ARA_Project_Q3 <br/>
      Fichier Config : /config_file_project_q3.cfg <br/>
      Directive Make : run_project_q3    <br/>

### Détecteur 4 :

    Sources : /src/ARA_Project_Q4 <br/>
    Fichier Config : /config_file_project_q4.cfg <br/>
    Directive Make : run_project_q4    <br/>