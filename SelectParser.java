import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class SelectParser {
    String[] input;
    SurlyDatabase db;

    Relation origR;
    Relation newR;
    String newRelationName;

    public SelectParser(String[] input, SurlyDatabase db, String newRelationName) {
        this.db = db;
        // sets the relation for this object as the first input line
        this.origR = db.getRelation(input[0]);
        this.newRelationName = newRelationName;
    }

    public void parseAddRelation() {

        newR = new Relation(newRelationName, origR.getSchema());
        LinkedList<Tuple> tuplesOfNewRelation = new LinkedList<>();

        LinkedList<Tuple> oldTuples = origR.getTuples();

        // removes all tuples from relation that are in tuplesToAdd
        for(int i = 0; i < oldTuples.size(); i ++) {
            tuplesOfNewRelation.add(oldTuples.get(i));
        }

        newR.setTuples(tuplesOfNewRelation);

        db.createRelation(newR);
    }
}
