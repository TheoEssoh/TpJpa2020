- commencer le projet par créer le nom (mydatabase) de la base de donnée dans votre serveur de base de données,
  ensuite pour se connecter à la base de données il comme mot de pass: "" et comme root :root.
  vous pouvez le modifier dans persistence.xml. il s'agit d'une BD mysql.
  -Executez JpaTest pour créer les tables de la base de données. 
- Ensuite lancer jetty en cliquant sur le lanceur jetty étant bien sur selectionné.
en suite lancer "http://localhost:8080/myform.html" pour entrer les informations démandées puis envoyer.
- lancer "http://localhost:8080/insert.html" puis entrez les infos démandées puis lancer également, ces informations
les informations entrée dans le formulaire seront enregistrées dans la BD. pour les visionner lancer:
  http://localhost:8080/Servlet_BD_Display.
