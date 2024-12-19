# Étape 1 : Construire l'application
FROM maven:3.9.4-eclipse-temurin-17 AS builder

# Configurer le répertoire de travail
WORKDIR /app

# Copier uniquement le fichier pom.xml et télécharger les dépendances
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copier le reste du projet dans le conteneur
COPY . . 

# Construire l'application
RUN mvn clean package -DskipTests

# Étape 2 : Image pour exécuter l'application
FROM openjdk:17-jdk-slim

# Configurer le répertoire de travail
WORKDIR /app

# Copier le fichier JAR depuis l'étape de build
COPY --from=builder /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8443

# Commande d'entrée
ENTRYPOINT ["java", "-jar", "app.jar"]
