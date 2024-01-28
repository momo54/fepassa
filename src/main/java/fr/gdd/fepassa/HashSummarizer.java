package fr.gdd.fepassa;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.core.Var;

public class HashSummarizer extends Summarizer {

    private final int modulo;

    public HashSummarizer(int... args) {
        super(args);
        assert args.length == 1;
        this.modulo = args[0];
    }
    
    @Override
    public Node summarize(Node node) {
        if (node.isURI()) {
            try {
                URI uri = new URI(node.getURI());
                if (this.modulo == 0) {
                    return NodeFactory.createURI("http://" + uri.getHost());
                } else {
                    int hashcode = Math.abs(uri.toString().hashCode());
                    return NodeFactory.createURI("http://" + uri.getHost() + "/" + (hashcode % this.modulo));
                }
            } catch (URISyntaxException e) {
                return NodeFactory.createURI("http://donotcare.com/whatever");
            }
        } else if (node.isLiteral()) {
            return NodeFactory.createLiteral("any");
        } else {
            return Var.alloc(node.getName());
        }
    }

    @Override
    public Triple summarize(Triple triple) {
        Node subject = this.summarize(triple.getSubject());
        Node predicate = triple.getPredicate();
        Node object = this.summarize(triple.getObject());
        // Node object;
        // if (predicate.isURI() && predicate.getURI().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
        //     object = triple.getObject();
        // } else {
        //     object = this.summarize(triple.getObject());
        // }
        return Triple.create(subject, predicate, object);
    }

    @Override
    public Quad summarize(Quad quad) {
        Node graph = quad.getGraph();
        Node subject = this.summarize(quad.getSubject());
        Node predicate = quad.getPredicate();
        Node object = this.summarize(quad.getObject());
        // Node object;
        // if (predicate.isURI() && predicate.getURI().equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
        //     object = quad.getObject();
        // } else {
        //     object = this.summarize(quad.getObject());
        // }
        return Quad.create(graph, subject, predicate, object);
    }

}