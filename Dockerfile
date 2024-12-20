# Étape 1 : Construire l'application
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Configurer le répertoire de travail
WORKDIR /app

# Copier uniquement le fichier pom.xml et télécharger les dépendances
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copier le reste du projet dans le conteneur
COPY . . 

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Créer l'image finale
FROM openjdk:21

# Configurer le répertoire de travail
WORKDIR /app

# Copier le fichier JAR depuis l'étape de build
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port utilisé par l'application
EXPOSE 8443

# Définir les variables d'environnement pour Java
ENV JAVA_OPTS=""

# Commande d'entrée
ENTRYPOINT ["java", "-jar", "app.jar"]