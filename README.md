<h1>PATROUILLE NFC</h1>
<p>L’application Android a besoin que l’on spécifie le serveur du webservice. Pour configurer cela on va dans le package « com.mbds.material.PatrouilleNFC.Utils » et on y trouve la classe Utilitaire.java. On a la constante « SERVEUR » pour configurer cela. Par défaut nous avons déjà uploader les services webs et le BO dans un hébergeur gratuit «  Hostinger » dont voici le lien http://policenationale.esy.es/ 
Pour le cas où le serveur est en panne où ne marche pas, voici la démarche à suivre pour installer un environnement local.
</p>
<ul>
<li>	Installer un serveur web qui supporte PHP 5.5 ou plus coupler, avec un serveur MySQL
</li>
<li>	Créer une base de données « policenationale » et importée le fichier « Policenationale.sql » dans le dossier BDD
</li>
<li>Configurer le web service (c.f Web Service) https://github.com/naokibeats/ServiceNFC </li>
<li>	Ouvrir le code source de l’application, changer le serveur (http://192.168.1.7:8888/WebServiceNFC/index.php/ par exemple) et le déployer sur son smartphone.
</li>
</ul>

<h1>EDIT </h1>
<p> Pour tester le scan il faut écrire dans le tag NFC, en format text plain (avec NFC Tools ou autre applications) un numéro  de  tag de personne  présente dans la  bdd. Exp: 123456789 pour la base actuelle  </p>
