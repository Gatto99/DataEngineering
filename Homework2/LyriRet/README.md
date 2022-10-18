# LyriRet
Nel progetto si distinguono 3 classi:
- [FileUtility](./src/main/java/com/github/davidegattini/FileUtility.java). Questa classe viene usata per avere a disposizioni metodi d'utilità per recuperare tutti i file in una directory (e quindi anche nelle sue subdirectory) e per recuperare il contenuto di un file testuale, dato il suo path.
- [Indexer](./src/main/java/com/github/davidegattini/Indexer.java). L'indexer permette di eseguire l'indicizzazione dei documenti memorizzati nella cartella [lyrics](../lyrics)
- [Query](./src/main/java/com/github/davidegattini/Query.java). Eseguito il main, viene chiesto all'utente di inserire input da tastiera per elaborare la query e restituire i documenti che la soddisfano. Esempi di query `filename: "sky full"` oppure `content: love`


## Analyzer
Per il campo **filename** si è scelto l'uso di uno [**StandardAnalyzer**]() perché ... 

Per il campo **content** è stato utilizzato un [**EnglishAnalyzer**]() per permettere all'utente di ottenere più documenti possibili poiché si suppone che chi stia cercando un testo di una canzone, non lo conosce alla perfezione ma si ricorda qualche parola. Per aumentare il numero di documenti ottenuti si è scelto l'uso di un analyzer inglese poiché esegue lo stemming delle parole oltre al lower...

## \#FileIndicizzati e tempi d'indicizzazione
I file che sono stati indicizzati sono stati recuperati tramite l'uso dell'API [lyricsGenius](), con cui sono stati ottenuti i lyrics di 100 canzoni. 

L'indicizzazione è di \~350ms.

## Query usare per il testing