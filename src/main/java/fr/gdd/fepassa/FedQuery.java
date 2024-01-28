package fr.gdd.fepassa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.dboe.base.file.Location;
import org.apache.jena.query.Dataset;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb2.TDB2Factory;
import org.eclipse.rdf4j.federated.FedXConfig;
import org.eclipse.rdf4j.federated.FedXFactory;
import org.eclipse.rdf4j.federated.repository.FedXRepository;
import org.eclipse.rdf4j.federated.repository.FedXRepositoryConnection;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.gdd.fedup.FedUP;
import fr.gdd.fedup.summary.ModuloOnSuffix;
import fr.gdd.fedup.summary.Summary;

/**
 * Simple test by running on FedShop.
 *
 * Have a virtuoso endpoint running with fedshop200…
 * Virtuoso Open Source Edition (Column Store) (multi threaded)
 * Version 7.2.6.3233-pthreads as of Jun 22 2021 (111d17e5b)
 * Compiled for Mac OS 11 (Apple Silicon) (aarch64-apple-darwin20.5.0)
 *
 * Listening on localhost:5555
 */

public class FedQuery {

    public static Integer PRINTRESULTTHRESHOLD = 10;
    private static final Logger log = LoggerFactory.getLogger(FedQuery.class);

    private static String directory="./jenadata50";

    public static FedUP fedup = new FedUP(new Summary(new ModuloOnSuffix(0),
            Location.create(directory)));
//            .modifyEndpoints(e -> "http://localhost:5555/sparql?default-graph-uri=" + (e.substring(0, e.length() - 1)));

    public static FedXRepository fedx = FedXFactory.newFederation()
            .withConfig(new FedXConfig() // same as FedUP-experiment
                    .withBoundJoinBlockSize(20) // 10+10 or 20+20 ?
                    .withJoinWorkerThreads(20)
                    .withUnionWorkerThreads(20)
                    .withDebugQueryPlan(false))
            .withSparqlEndpoints(List.of()).create();

    public Pair<String, Long> SourceSelection(String originalQuery) {
        long current = System.currentTimeMillis();
        String result = fedup.query(originalQuery);
        long elapsed = System.currentTimeMillis() - current;
        log.info("FedUP took {} ms to perform source selection.", elapsed);
        return new ImmutablePair<>(result, elapsed);
    }

    public MultiSet<BindingSet> ExecuteWithFedX(String serviceQuery) {
        MultiSet<BindingSet> serviceResults = new HashMultiSet<>();

        long elapsed = -1;

        try (FedXRepositoryConnection conn = fedx.getConnection()) {
            long current = System.currentTimeMillis();
            // parsing may take a long long time…
            TupleQuery tq = conn.prepareTupleQuery(serviceQuery);
            try (TupleQueryResult tqRes = tq.evaluate()) {
                while (tqRes.hasNext()) {
                    serviceResults.add(tqRes.next());
                }
            }
            elapsed = System.currentTimeMillis() - current;
            log.info("FedX took {} ms to get {} results.", elapsed, serviceResults.size());
        }

        if (serviceResults.size() <= PRINTRESULTTHRESHOLD) {
            log.debug("Results:\n{}",
                    String.join("\n", serviceResults.entrySet().stream().map(Object::toString).toList()));
        }
        return serviceResults;
    }

    public static List<String> getFederation() {
        Dataset dsg = TDB2Factory.connectDataset(Location.create(directory));
        dsg.begin();
        System.out.println("size of ("+directory+") :"+dsg.asDatasetGraph().size());

        List<String> graphNames = new ArrayList<>();
        dsg.asDatasetGraph().listGraphNodes().forEachRemaining(graphNode -> {
            graphNames.add(graphNode.getURI());
        });
        dsg.end();
        return graphNames;
    }

    public static void setFedup(String directory) {
        FedQuery.directory=directory;
        fedup = new FedUP(new Summary(new ModuloOnSuffix(0),
                Location.create(directory)));
        System.out.println("fedup set to "+directory);
    }

}
