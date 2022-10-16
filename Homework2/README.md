Con il progetto seguente è stato realizzato l'homework2, cui consegna: 

`1. Scrivere un programma java che indicizza i file ".txt" contenuti in una directory del proprio laptop. In particolare, si devono considerare due campi (e quindi creare due indici): il nome del file, il contenuto del file. Per ciascun campo utilizzare un analyzer appropriato.`

`2. Scrivere un programma Java che legge una query da console, interroga l'indice e stampa il risultato. Usare una semplice sintassi per la query (ad es., una query inizia con la parola chiave nome o contenuto seguita da una sequenza di termini (eventualmente racchiusi tra virgolette per esprimere una phrase quer)`

`3. Testare il sistema con una decina di query diverse`

Il progetto comprende le seguenti cartelle:
- [Progetto Java](./LyriRet/). Il programma, una volta eseguito, permette all'utente di eseguire una ricerca tra i file nella [cartella dei lyrics](./lyrics/)
- [Lyrics](./lyrics/). In questa cartella si può accedere ai lyrics in formato *.txt* delle canzoni di un certo artista (e.g. *Coldplay*)
- [Script Python](). Con questo script è possibile, tramite il file di configurazione, definire il nome dell'artista cui vogliamo estrarre i lyrics delle canzoni (tramite lo script *main.ipynb*).

Si noti che per eseguire una query è necessario che l'utente inserisca prima il campo di ricerca (i.e. *filename* oppure *content*), successivamente ":" ed infine la query.
Di seguito degli esempi: 
- *filename: Full*
- *content: "stars"*
