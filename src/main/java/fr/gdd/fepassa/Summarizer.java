package fr.gdd.fepassa;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Quad;

public abstract class Summarizer {

    public Summarizer(int... args) { }
    
    public abstract Node summarize(Node node);

    public abstract Triple summarize(Triple triple);

    public abstract Quad summarize(Quad quad);


}
