Si maven2 est installé (sur le systeme ou en plugin Eclipse) et fonctionne alors on peut (avec eclipse) importer facilement :

	Fichier -> import -> existing Maven Projects -> selection du dossier contenant pom.xml .
	et voila (ça télécharge les libs automatiquement).

Sinon

	File -> import -> existing project  -> selection du dossier (contenant pom.xml)
	puis il faut redefinir le classpath (ie le jdk).
	si il demande des libs alors il faut ajouter dans le classpath "Libs.jar" (situer dans le dossier contenant pom.xml)