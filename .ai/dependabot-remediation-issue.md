## Contesto
Sono presenti **24 Dependabot alerts aperti** nel repository, quasi tutti in ecosistema Maven e con `manifest_path = settings.gradle.kts`.

Distribuzione severità:
- High: 7
- Medium: 15
- Low: 2

## Problemi rilevati (raggruppati)
### 1) Stack Netty obsoleto (priorità alta)
Alert coinvolti: #1, #2, #7, #14, #15, #16, #18, #19, #20, #23

Pacchetti coinvolti:
- io.netty:netty-handler
- io.netty:netty-codec-http2
- io.netty:netty-codec-http
- io.netty:netty-codec
- io.netty:netty-common

Impatto:
- varie vulnerabilità DoS / DDoS e parsing issues su HTTP/2 e codec HTTP.

Target di fix consigliato:
- allineare tutto il family Netty ad almeno **4.1.129.Final** (copre tutte le soglie minime richieste dagli alert attuali).

### 2) Bouncy Castle vulnerabile (priorità alta)
Alert coinvolti: #8, #9, #10, #13, #17, #22

Pacchetti coinvolti:
- org.bouncycastle:bcprov-jdk18on
- org.bouncycastle:bcpkix-jdk18on

Impatto:
- problemi crypto/DoS e loop/infinite processing in parsing/signature path.

Target di fix consigliato:
- **bcprov-jdk18on >= 1.78**
- **bcpkix-jdk18on >= 1.79**

### 3) Altri componenti ad alto rischio
Alert coinvolti:
- #24 org.bitbucket.b_c:jose4j (high) -> fix >= 0.9.6
- #21 org.jdom:jdom2 (high) -> fix >= 2.0.6.1
- #12 commons-io:commons-io (high) -> fix >= 2.14.0
- #11 com.google.protobuf:protobuf-java (high) -> fix >= 3.25.5

### 4) Componenti medium/low da allineare
Alert coinvolti:
- #5, #6 org.apache.commons:commons-compress -> fix >= 1.26.0
- #3, #4 com.google.guava:guava -> fix >= 32.0.0-android (raccomandato almeno 32.0.1+)

## Causa probabile (da verificare durante remediation)
Dal dependency graph emergono due sorgenti principali:
- dipendenze runtime app (es. Firebase/Play Services)
- dipendenze transitive del toolchain Android/Gradle plugin

Versioni oggi visibili nel graph (esempi):
- netty ~ 4.1.93.Final
- protobuf-java 3.22.3
- commons-io 2.13.0
- commons-compress 1.21
- bcprov/bcpkix 1.77
- jose4j 0.9.5
- jdom2 2.0.6

## Piano di risoluzione proposto
1. Aggiornare dipendenze dirette principali:
- Android Gradle Plugin (attualmente 8.5.2) a una versione più recente supportata dal progetto.
- Firebase BoM (attualmente 33.8.0) a release più recente.

2. Applicare constraint/forcing version temporanei per chiudere i CVE subito (se gli upgrade principali non bastano):
- Netty family -> 4.1.129.Final
- protobuf-java -> 3.25.5+
- commons-io -> 2.14.0+
- commons-compress -> 1.26.0+
- bcprov/bcpkix -> 1.78/1.79+
- jose4j -> 0.9.6+
- jdom2 -> 2.0.6.1+
- guava -> 32.0.1-android+

3. Verifiche obbligatorie post-fix:
- `./gradlew :app:dependencies --configuration releaseRuntimeClasspath` (o equivalente CI)
- `./gradlew assembleDebug` e `assembleRelease`
- controllo regressioni su analytics/runtime networking
- conferma in Security tab della chiusura alert

## Criteri di accettazione
- [ ] Tutti i 24 alert Dependabot risultano `fixed` o eventualmente `dismissed` con motivazione documentata.
- [ ] Nessuna regressione di build o runtime.
- [ ] Versioni finali documentate in changelog/PR description con mapping CVE -> fix.

## Note operative
- Evitare dismiss massivo: preferire fix reale delle versioni.
- Se alcuni alert risultano solo build-time e non sfruttabili nel contesto app, documentare comunque la valutazione rischio e la decisione.
