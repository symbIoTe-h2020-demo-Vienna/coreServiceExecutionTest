/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.h2020.symbiote.DEPRICATEDexecutors.models;

import java.math.BigInteger;

/**
 *
 * @author jab
 */
public class Ontology {

    public static final String ROOT_URI = "http://www.example.com/symbIoTe/";

    public static final int CORE_MODEL_ID = -1;

    /**
     * Graphs
     */
    public static final String METADATA_GRAPH = ROOT_URI + "metadata";
    public static final String MAPPING_GRAPH = ROOT_URI + "mappings";
    public static final String PLATFORMS_GRAPH = ROOT_URI + "platforms";
    public static final String MODELS_GRAPH = ROOT_URI + "models";

    /**
     * Classes
     */
    public static final String PLATFORM = ROOT_URI + "Platform";
    public static final String MAPPING = ROOT_URI + "Mapping";

    /**
     * Predicates
     */
    public static final String FROM = ROOT_URI + "from";
    public static final String TO = ROOT_URI + "to";
    public static final String USES = ROOT_URI + "uses";

    /**
     * Imported
     */
    public static final String IS_A = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

    private Ontology() {

    }

    public static String getModelGraphURI(BigInteger modelId) {
        return MODELS_GRAPH + "/" + modelId;
    }

    public static String getModelGraphURI(String modelId) {
        return MODELS_GRAPH + "/" + modelId;
    }

    public static String getPlatformGraphURI(BigInteger platformId) {
        return PLATFORMS_GRAPH + "/" + platformId;
    }

    public static String getMappingGraphURI(BigInteger mappingId) {
        return MAPPING_GRAPH + "/" + mappingId;
    }

    public static String getPlatformMetadata(BigInteger platformId, BigInteger modelId) {
        return "<" + getPlatformGraphURI(platformId) + "> <" + IS_A + "> <" + PLATFORM + "> ." + "\n"
                + "<" + getPlatformGraphURI(platformId) + "> <" + USES + "> <" + getModelGraphURI(modelId) + "> .";
    }

    public static String getMappingMetadata(BigInteger modelId1, BigInteger modelId2, BigInteger mappingId) {
        return "<" + getMappingGraphURI(mappingId) + "> <" + IS_A + "> <" + MAPPING + "> ." + "\n"
                + "<" + getMappingGraphURI(mappingId) + "> <" + FROM + "> <" + getModelGraphURI(modelId1) + "> . \n"
                + "<" + getMappingGraphURI(mappingId) + "> <" + TO + "> <" + getModelGraphURI(modelId2) + "> .";
    }
}
