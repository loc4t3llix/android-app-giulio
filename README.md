# Calendar Day Widget (Android)

Questa app legge il calendario del telefono e controlla se oggi esistono eventi **all-day** con titolo configurabile dall'utente per:

- Evento A
- Evento B

In base al risultato aggiorna un widget quadrato con colore pieno configurabile dall'utente:

- Evento A -> colore scelto dall'utente
- Evento B -> colore scelto dall'utente
- nessun evento -> colore scelto dall'utente
- nessun permesso calendario -> colore scelto dall'utente

## Come configurare eventi e colori

Apri l'app e dalla schermata principale:

1. Inserisci i nomi di Evento A e Evento B.
2. Premi **Salva impostazioni**.
3. Usa i pulsanti colore per aprire il selettore interattivo (Hue/Saturazione/Luminosità).
4. Salva il colore desiderato: il widget si aggiorna subito.

## Uso

1. Apri il progetto in Android Studio.
2. Esegui l'app su telefono/emulatore.
3. Concedi il permesso calendario dalla schermata principale.
4. Aggiungi il widget "Calendar Day Widget" alla home.

## Build APK con GitHub

Puoi ottenere l'APK anche senza Android Studio usando GitHub Actions.

1. Crea un repository GitHub e carica questo progetto.
2. Verifica che il branch si chiami `main` o `master`.
3. Apri la tab Actions nel repository.
4. Esegui il workflow `Build Android APK` (oppure fai push su `main`/`master`).
5. Al termine apri il run completato e scarica l'artifact `app-debug-apk`.
6. Dentro l'artifact trovi `app-debug.apk`.

## Pubblicare anche su GitHub Releases

Usa lo stesso workflow `Build Android APK` in modalita manuale:

1. Vai su Actions e avvia `Build Android APK`.
2. Imposta input `publish_release` su `true`.
3. Il workflow builda l'APK debug, carica l'artifact `app-debug-apk` e pubblica lo stesso file anche in `Releases`.
4. Ogni run crea una release con tag `apk-<run_id>`.

## Note

- L'app legge solo eventi `all-day`.
- Se entrambi gli eventi sono presenti nello stesso giorno, ha priorità Evento A.

## Sicurezza Firebase

`app/google-services.json` contiene credenziali/proprieta di progetto e non deve essere pubblico.

1. Il file e ignorato da Git (`.gitignore`).
2. In CI (GitHub Actions) il file viene creato dal secret `GOOGLE_SERVICES_JSON`.

Configura il secret in GitHub:

1. Repository -> Settings -> Secrets and variables -> Actions.
2. Crea il secret `GOOGLE_SERVICES_JSON`.
3. Incolla il contenuto completo del tuo `google-services.json`.
