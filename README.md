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

## Note

- L'app legge solo eventi `all-day`.
- Se entrambi gli eventi sono presenti nello stesso giorno, ha priorita `EVENTO_A`.
