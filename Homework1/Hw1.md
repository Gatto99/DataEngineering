# La soluzione Data-Centric

Andrew NG, globalmente riconosciuto nel mondo dell'intelligenza artificiale, ha identificato un grande cambiamento in questo ambito: sostiene che l'uso dei modelli *data-centric*, per cui è sufficiente avere a disposizione pochi dati ma di qualità, sia una soluzione migliore rispetto ad avere un dataset con molti dati, usato per addestrare un modello che successivamente si pensa a migliorare per ottenere risultati migliori (approccio *model-centric*). Quindi, secondo Andrew NG, collezionare sempre più dati risulta costoso e meno efficace che occuparsi dell'ingegnerizzazione dei dati (che quindi prevede la pulizia di quest'ultimi) e, poiché è un approccio già adottato, per evitare che rimanga solo un'intuizione, crede necessario che esso diventi sistematico.

<!--## Foundation models
Sono modelli che vengono addestrati su una vasta quantità di dati e di cui si può fare un tuning per applicazioni specifiche. Ma non è banale verificare che siano equi e liberi da bias. Più in generale risultano efficiaci per alcuni problemi, ma non per altri. 

Inoltre, alcuni domini applicativi sollevano problemi di scalabilità perché se dovessimo costruire dei foundation models per la computer vision, si dovrebbero processare una gran quantità di immagini. -->


L'utilizzo di quei modelli che Andrew NG chiama *foundation models*, potrebbe risultare vantaggioso qualora si disponesse di una quantità di dati sufficiente per addestrare il modello e renderlo utile ed efficace per differenti tasks. Però non sempre questo requisito viene soddisfatto, infatti come A. sottolinea, ci sono realtà in cui il dataset a disposizione è povero, quindi è conveniente e produttivo seguire l'approccio *data-centric*. Di contro, è necessario trovare del personale qualificato, che lo stesso Andrew NG chiama MLOps, che deve curarsi della qualità dei dati per bilanciare una bassa quantità di dati con una buona qualità degli stessi, ma molto spesso questo lavoro viene già fatto poiché è comunque prevista l'acquisizione dei dati e la loro organizzazione, in modo che siano utilizzabili e coerenti.

<!--## Da big data a good data 
Migliorare il modello (e dunque il codice), è stato il paradigma con cui i problemi sono stati via via risolti, ma per alcuni domini applicativi, è più produttivo migliorare i dati. 
Questo cambiamento risulta fondamentale ed utile per quelle aziende che non hanno a disposizione un grande data sets su cui addestrare i modelli, per cui averne pochi ma attentamente progettati può essere sufficiente. 

In questo caso si parla di prendere dei modelli già addestrati che potranno essere rifiniti attraverso la scelta di un giusto sottoinsieme di dati che è permessa dall'uso di strumenti idonei che permettono di etichettare queste immagini consistentemente. Invece se utilizzassimo solo tanti dati che poi risultano rumorosi, allora si ignora il problema e si collezionano più dati per ricoprirli. Ma è più efficiente ricoprire questi casi dando in pasto al modello dei dati specifici che permettono di risolvere l'inconsistenza dovuta al rumore. Se il bias è per un certo sottoinsieme, cambiare tutta l'archietettura per eliminarlo, è difficile. Quindi è necessario avere strumenti che permettano di accorgersi che il bias sia relativo ad un subset dei dati così da attirare l'attenzione. 
Collezionare tanti dati per tutto può essere costoso

Questi modelli data-centric non possono essere la soluzione assoluta, ma devono essere accompagnati da strumenti che danno la possibilià alle aziende, di avere strumenti per ingegnerizzare i dati e sfruttare la loro conoscenza del dominio per costruire il modello adatto.-->


E' dunque furbo ed efficace risolvere i problemi specifici cercando di dare in pasto al modello dati specifici con cui colmare l'inconsistenza dovuta ad un addestramento errato. Quindi continuare a raccogliere un quantità indefinita di dati (a volte non disponibili) potrebbe risultar essere una perdita di tempo e risorse che potremmo impiegare a migliorare dati che già abbiamo e conosciamo. Se ci si dovesse domandare cosa fosse meglio, se migliorare il modello o i dati, penserei a migliorare i dati: a parità di dati, migliorare il modello potrebbe sì risolvere il problema in maniera più o meno efficace, ma il modello adottato potrebbe comunque comportarsi diversamente da quello che ci aspettiamo, poiché, a causa del rumore con cui mal addestriamo l'architettura (qualsiasi essa sia), non sarà efficace quanto lo **stesso modello** che alimentiamo con gli **stessi dati** ma privati del rumore stesso.

<!--## Dati sintetici
Potrebbero essere utili ma userebbe metodologie che risultano più facili come il data augmentation che prevede di applicare trasformazioni reali a dati per ottenerne degli altri ingrandendo il data set a disposizione.-->

(Fonte: [Andrew Ng's IEEE Spectrum Interview](https://spectrum.ieee.org/andrew-ng-data-centric-ai))
