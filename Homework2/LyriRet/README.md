# LyriRet
Nel progetto si distinguono 3 classi:
- [FileUtility](./src/main/java/com/github/davidegattini/FileUtility.java). Questa classe viene usata per avere a disposizioni metodi d'utilità per recuperare tutti i file in una directory (e quindi anche nelle sue subdirectory) e per recuperare il contenuto di un file testuale, dato il suo path.
- [Indexer](./src/main/java/com/github/davidegattini/Indexer.java). L'indexer permette di eseguire l'indicizzazione dei documenti memorizzati nella cartella [lyrics](../lyrics)
- [Searcher](./src/main/java/com/github/davidegattini/Searcher.java). Eseguito il main, viene chiesto all'utente di inserire input da tastiera per elaborare la query e restituire i documenti che la soddisfano. Esempi di query `filename: "sky full"` oppure `content: love`


## Analyzer
Per il campo **filename** si è scelto l'uso di un [**StandarsAnalyzer**](https://lucene.apache.org/core/9_0_0/core/org/apache/lucene/analysis/standard/StandardAnalyzer.html) così caratterizzato:
- *Tokenizer*
	- **WhiteSpaceTokenizer** 
- *Token filter*
	- **LowerCaseFilter**
	- **WordDelimiterGraphFilter**
La scelta deriva dalla volontà di permettere all'utente la ricerca delle canzoni tramite il loro titolo, ignorando le maiuscole presenti e la punteggiatura (che solitamente non vengono ricercate).

Anche per il campo **content** viene applicato lo **StandardAnalyzer**:
- *Tokenizer*
	- **WhiteSpaceTokenizer** 
- *Token filter*
	- **LowerCaseFilter**
	- **WordDelimiterGraphFilter**
	- stop words = {*of*, *an*, *a*, *the*, *for*}

L'adozione di un analyzer di questo tipo per il lyrics delle canzoni nel corpus, permette di evitare l'indicizzazione di termini molto comuni che saranno ignorati in fase di ricerca poiché si punta ad offrire all'utente il maggior numero possibile di documenti, garantendo efficienza nell'indicizzazione. Quindi non pensiamo alle maiuscole, né tanto meno ai vari delimitatori. 

Infine per la **query**, poiché si è usato il query parser, è stato necessario utilizzare un ulteriore analyzer e la scelta è ricaduta sempre sullo **StandardAnalyzer**, per cui non sono state definite delle stop word per permettere all'utente di inserire una phrase query contenente questi termini qualora ricordasse il titolo della canzone di cui vuole il testo.

## \#File Indicizzati e tempi d'indicizzazione
I file che sono stati indicizzati sono stati recuperati tramite l'uso dell'API [lyricsGenius](https://lyricsgenius.readthedocs.io/en/master/), con cui sono stati ottenuti i lyrics di 100 canzoni. 

L'indicizzazione è di \~350ms. <!--tabella latex-->

## Query usare per il testing
Per il campo *filename*:
1. `filename: "A Sky Full Of Stars"` 
2. `filename: "a sky full of stars"` 
3. `filename: -sky +full`
4. `filename: LOVE`
5. `filename: The Scie`

Si noti che qualora siano cercate delle stringhe con lettere maiuscole, il sistema le ignora grazie all'analyzer usato (vedi sopra). In più, non essendo usate le stopWords, parole come *The* fanno matching con tanti documenti, ma grazie al Ranking dei documenti vengono proposti i documenti che più soddisfano la query. Infine, grazie all'uso del query parser, si possono eseguire ricerca di alcune parole la cui presenza è obbligatoria o che sono proibite (nella terza query, rispettivamente `full` e `sky`)

Per il campo *content*:
1. `content: "love you"`
2. `content: fix`
3. `content: "Everything you want a dream away"`
4. `content: "I could not stop that you now know"`
5. `content: I could not stop`
