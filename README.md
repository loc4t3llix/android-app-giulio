# Calendar Day Widget (Android)

Questa app legge il calendario del telefono e controlla se oggi esistono eventi **all-day** con titolo:

- `MAMMA` (Evento A)
- `PAPI` (Evento B)

In base al risultato aggiorna un widget quadrato con colore pieno:

- `MAMMA` (Evento A) -> rosa
- `PAPI` (Evento B) -> azzurro
- nessun evento -> trasparente
- nessun permesso calendario -> grigio scuro

## Come configurare i titoli evento

Apri `CalendarStatusWidgetProvider.kt` e modifica:

- `EVENT_A_TITLE`
- `EVENT_B_TITLE`

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

## Build Release APK con GitHub

E disponibile anche il workflow manuale `Build Release APK`.

1. Vai su Actions e avvia `Build Release APK`.
2. Se non configuri i secret di firma, il workflow carica `app-release-unsigned-apk`.
3. Se configuri i secret, il workflow firma l'APK e carica `app-release-signed-apk`.

Secret richiesti per la firma (Repository Settings -> Secrets and variables -> Actions):

- `SIGNING_KEYSTORE_BASE64` (keystore `.jks` codificato in base64)
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`

## Note

- L'app legge solo eventi `all-day`.
- Se entrambi gli eventi sono presenti nello stesso giorno, ha priorita `EVENTO_A`.
