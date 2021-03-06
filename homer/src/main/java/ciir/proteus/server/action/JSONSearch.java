package ciir.proteus.server.action;

import ciir.proteus.system.DocumentAnnotator;
import ciir.proteus.system.ProteusSystem;
import ciir.proteus.users.error.DBError;
import ciir.proteus.util.ListUtil;
import ciir.proteus.util.QueryUtil;
import java.util.ArrayList;
import org.lemurproject.galago.core.retrieval.ScoredDocument;
import org.lemurproject.galago.core.retrieval.query.Node;
import org.lemurproject.galago.core.retrieval.query.StructuredQuery;
import org.lemurproject.galago.utility.Parameters;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class JSONSearch implements JSONHandler {

    private final ProteusSystem system;
    public static final Logger log = Logger.getLogger(JSONSearch.class.getName());

    public JSONSearch(ProteusSystem sys) {
        this.system = sys;
    }

    @Override
    public Parameters handle(String method, String path, Parameters reqp) throws DBError {
        String query = reqp.getAsString("q");
        String kind = reqp.get("kind", system.defaultKind);
        int numResults = (int) reqp.get("n", 10);
        int skipResults = (int) reqp.get("skip", 0);
        String userid = reqp.get("userid", "-1");

        List<String> labels = new ArrayList<String>(); // empty list
        List<String> resList = null;
        if (reqp.containsKey("labels")) {
            labels = reqp.getAsList("labels", String.class);
            // we pass in labels on the URL so it's possible that someone could share
            // a URL with you that has THEIR tags. So we get the same results, we'll use the
            // "labelOwner" to get the labels. 
            resList = system.userdb.getResourcesForLabels(Integer.parseInt(reqp.get("labelOwner", userid)), labels); // get all
            log.info("We have labels: " + labels.toString());
        } else {
            // if we're searching by labels display ALL so only check if we 
            // don't have labels
            if (numResults > 1000) {
                throw new IllegalArgumentException("Let's not put too many on a page...");
            }
        }

        Node pquery = null;

        // it's possible for the query to be empty IF we're searching just by labels
        if (!query.isEmpty()) {
            pquery = StructuredQuery.parse(query);
        }

        Parameters qp = Parameters.instance();
        qp.put("requested", numResults + skipResults);
        Parameters response = Parameters.instance();

        List<Parameters> results = Collections.emptyList();
        List<ScoredDocument> docs = null;
        // if we're searching for labels 
        if (labels.isEmpty()) {
            docs = ListUtil.drop(system.search(kind, pquery, qp), skipResults);
            if (!docs.isEmpty()) {
                results = DocumentAnnotator.annotate(this.system, kind, docs, pquery, reqp);
            }
        } else {
            docs = new ArrayList<ScoredDocument>();
            for (String id : resList) {
                docs.add(new ScoredDocument(id, 0, 0.0));
            }
            if (!docs.isEmpty()) {
                log.info(docs.toString());
                // only retrieve snippits if we have a query
                reqp.set("snippets", (pquery != null));
                reqp.set("tags", true);
                // remove the param that says how many to get
                reqp.remove("n");
                results = DocumentAnnotator.annotate(this.system, kind, docs, pquery, reqp);
            }
        }

        response.set("results", results);
        if (pquery != null) {
            response.set("parsedQuery", pquery.toString());
            response.set("queryTerms", QueryUtil.queryTerms(pquery));
        }

        return response;
    }

}
