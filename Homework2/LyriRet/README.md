# LyriRet
Nel progetto si distinguono 3 classi:
- [FileUtility](./src/main/java/com/github/davidegattini/FileUtility.java). Questa classe viene usata per avere a disposizioni metodi d'utilità per recuperare tutti i file in una directory (e quindi anche nelle sue subdirectory) e per recuperare il contenuto di un file testuale, dato il suo path.
- [Indexer](./src/main/java/com/github/davidegattini/Indexer.java). L'indexer permette di eseguire l'indicizzazione dei documenti memorizzati nella cartella [lyrics](../lyrics)
- [Query](./src/main/java/com/github/davidegattini/Query.java). Eseguito il main, viene chiesto all'utente di inserire input da tastiera per elaborare la query e restituire i documenti che la soddisfano. Esempi di query `filename: "sky full"` oppure `content: love`


## Analyzer
Per il campo **filename** si è scelto l'uso di un Custom analyzer caratterizzato dal **WhiteSpaceTokenizer**, dal **LowerCaseFilter** e dal **WordDelimiterGraphFilter** perché ...

Per il campo **content** è stato utilizzato un [**StandarsAnalyzer**]() per permettere all'utente di ottenere più documenti possibili poiché si suppone che chi stia cercando un testo di una canzone, non lo conosce alla perfezione ma si ricorda qualche parola. Si evita che debba ricordarsi di scrivere le maiuscole e sopratutto la punteggiatura

## \#FileIndicizzati e tempi d'indicizzazione
I file che sono stati indicizzati sono stati recuperati tramite l'uso dell'API [lyricsGenius](), con cui sono stati ottenuti i lyrics di 100 canzoni. 

L'indicizzazione è di \~350ms.

## Query usare per il testing
Per il campo *filename*:
1. `filename: "A Sky Full Of Stars"` 
2. `filename: "a sky full of stars"` 
3. `filename: full`
4. `filename: LOVE`
5. `filename: The Scie`

Si noti che qualora siano cercate delle stringhe con lettere maiuscole, il sistema le ignora grazie all'analyzer usato (vedi sopra). In più, non essendo usate le stopWords, parole come *The* fanno matching con tanti documenti, ma grazie al Ranking dei documenti vengono proposti i documenti che più soddisfano la query.

Per il campo *content*:
1. `content: "love you"`
2. `content: fix`
3. `content: "Everything you want a dream away"`
4. `content: "I could not stop that you now know"`
5. `content: I could not stop`
