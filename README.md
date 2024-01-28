* Need FedShop virtuoso data https://zenodo.org/records/8339384 (https://zenodo.org/records/8339384/files/fedshop-virtuoso.db?download=1)
  * download and install in ./fedshop
* Start Virtuoso (at least 7.2):
```
virtuoso-t +foreground +configfile ./fedshop/virtuoso.ini
```
* Generate Summaries :
```
mvn exec:java -Dexec.mainClass="fr.gdd.fepassa.GenerateSummaries" -Dexec.args="fedup -e ./fedshop/endpoints50.txt -m 0 -o ./fedshop/summary50.nq"
```
* create summaries as Apache Jena  datasets
```
mvn exec:java -Dexec.mainClass="fr.gdd.fepassa.GenerateSummaries" -Dexec.args="jenaload -s ./fedshop/summary50.nq  -o ./jenadata50"
```
* repeat previous step with endpoints100.txt and endpoints200.txt
* Launch :
```
mvn javafx:run
```



