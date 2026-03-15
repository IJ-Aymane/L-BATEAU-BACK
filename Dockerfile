# --- Étape 1 : Build ---
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copie des fichiers Maven pour le cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Téléchargement des dépendances
RUN ./mvnw dependency:go-offline

# Copie du code source et compilation
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- Étape 2 : Exécution ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# On utilise un wildcard (*) pour copier le jar généré
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
