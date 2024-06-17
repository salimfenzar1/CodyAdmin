# CodyAdmin: Beheer en Toevoeging van Stellingen voor Cody

## Introductie

Welkom bij de CodyAdmin-app! Deze applicatie is ontwikkeld om zorgprofessionals en beheerders in staat te stellen nieuwe stellingen toe te voegen en te beheren die worden gebruikt in de Cody-app. CodyAdmin communiceert met een online database om de stellingen up-to-date te houden en zorgt ervoor dat zorgprofessionals altijd toegang hebben tot relevante en actuele stellingen.

## Projectoverzicht

CodyAdmin is een cruciaal onderdeel van het Cody-project, dat zich richt op het ondersteunen van zorgprofessionals bij het voeren van moeilijke gesprekken. Met deze beheertool kunnen nieuwe stellingen eenvoudig worden toegevoegd, inclusief beschrijvingen en afbeeldingen.

## Functies

CodyAdmin biedt de volgende belangrijke functies:
- **Stellingen Toevoegen:** Voeg nieuwe stellingen toe met een beschrijving, categorie, intensiteitsniveau en afbeelding.
- **Stellingen Beheren:** Beheer bestaande stellingen en houd de database actueel.

## Doelstellingen

Het doel van deze app is om:
- Beheerders een eenvoudige en intuïtieve manier te bieden om stellingen toe te voegen en te beheren.
- Ervoor te zorgen dat de Cody-app altijd up-to-date is met relevante stellingen.
- De interactie en ondersteuning voor zorgprofessionals te verbeteren door continu nieuwe en relevante stellingen te bieden.

## Installatie

Volg deze stappen om de CodyAdmin-app te installeren en te starten:

1. **Kloon de repository:**
   ```sh
   git clone https://github.com/jouw-gebruikersnaam/codyAdmin-project.git
   
2. **Open het project in Android Studio:**
   Start Android Studio en selecteer "Open an existing Android Studio project".
   Navigeer naar de locatie waar je de repository hebt gekloond en selecteer de projectmap.

3. **Build de app:**
   Klik op "Build" in het menu en selecteer "Make Project".

4. **Run de app:**
   Sluit je Android-apparaat aan of gebruik een emulator.
   Klik op "Run" en selecteer je apparaat.

## Gebruik
Stellingen Toevoegen
1. **Open de CodyAdmin-app.**
2. **Vul de beschrijving, categorie en intensiteitsniveau van de stelling in.**
3. **Kies een afbeelding door op de knop "Afbeelding kiezen" te klikken.**
4. **Klik op "Voeg stelling toe" om de stelling op te slaan in de online database.**
   
Stellingen Beheren
1. **Open de lijst met bestaande stellingen.**
2. **Selecteer een stelling om te verwijderen.**
3. **Pas de benodigde wijzigingen aan en sla deze op om de database bij te werken.**
4. **Zodra je een stelling hebt verwijderd word deze niet direct in de Cody app verwijderd, om dit te bereiken moet je de opslag van de cody app wissen.**
 
## Toestemmingen
De CodyAdmin-app vraagt om de volgende toestemmingen:

- **Leestoegang tot media:** Toestemming om afbeeldingen te kiezen vanuit de mediabibliotheek.
- **Voor Android 13 en hoger:** READ_MEDIA_IMAGES
- **Voor eerdere versies:** READ_EXTERNAL_STORAGE
  
## Branch Conventies

Voor een efficiënte en veilige samenwerking hanteren we de volgende conventies voor het pushen van code:

- **Development Branch:** Alle nieuwe functies en wijzigingen moeten eerst naar de development branch worden gepusht. Zorg ervoor dat je code goed getest is voordat je een pull request indient.
- **Main Branch:** De main branch bevat de stabiele versie van de code. Pushen naar de main branch is alleen toegestaan vanuit de development branch na code review en goedkeuring van het team.
