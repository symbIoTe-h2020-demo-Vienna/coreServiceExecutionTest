package eu.h2020.symbiote.executors.models;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jab
 */
public enum RDFFormat {
    Turtle("TURTLE"),
    NTriples("NTRIPLES"),
    RDFXML("RDFXML"),
    N3("N3"),
    JSONLD("JSONLD");

    private final String name;

    RDFFormat(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
