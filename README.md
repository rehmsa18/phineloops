#### Les commandes à taper pour compiler et exécuter notre projet.

##### Générateur
###### générer un niveau (h, w, file)
* java -jar phineloops-1.0-jar-with-dependencies.jar --generate hxw --output file

###### générer un niveau avec un nombre maximum de composantes connexes (h, w, file, nb)
* java -jar phineloops-1.0-jar-with-dependencies.jar --generate hxw --output file --nbcc nb
  
##### Checker (file)
* java -jar phineloops-1.0-jar-with-dependencies.jar --check file
  
##### Solver 
###### résoudre un niveau (file, filesolved)
* java -jar phineloops-1.0-jar-with-dependencies.jar --solve file --output filesolved 

###### résoudre un niveau avec un nombre de threads (file, filesolved, nb)
* java -jar phineloops-1.0-jar-with-dependencies.jar --solve file --output filesolved --threads nb
  
##### GUI (file)
* java -jar phineloops-1.0-jar-with-dependencies.jar --gui file
  
##### Solver Alternative
###### résoudre un niveau (file, filesolved)
* java -jar phineloops-1.0-jar-with-dependencies.jar --solvealt file --output filesolved 

###### résoudre un niveau avec un nombre de threads (file, filesolved, nb)
* java -jar phineloops-1.0-jar-with-dependencies.jar --solvealt file --output filesolved --threads nb

###### résoudre un niveau avec un choix de pièce à fixer (file, filesolved)
* java -jar phineloops-1.0-jar-with-dependencies.jar --solvealt file --output filesolved --piece min
* java -jar phineloops-1.0-jar-with-dependencies.jar --solvealt file --output filesolved --piece max

###### résoudre un niveau avec un nombre de threads et choix de pièce (file, filesolved, nb)
* java -jar phineloops-1.0-jar-with-dependencies.jar --solvealt file --output filesolved --piece min --threads nb
  
