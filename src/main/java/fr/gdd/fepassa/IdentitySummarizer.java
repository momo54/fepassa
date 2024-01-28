package fr.gdd.fepassa;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Quad;

public class IdentitySummarizer extends Summarizer {

    public IdentitySummarizer(int... args) {
        super(args);
    }

    @Override
    public Node summarize(Node node) {
        return node;    
    }

    @Override
    public Triple summarize(Triple triple) {
        return triple;
    }

    @Override
    public Quad summarize(Quad quad) {
        return quad;
    }

    
}
