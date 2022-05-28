import java.util.LinkedList;

public class ProjectParser {

    String relName;
    String newRelName;
    SurlyDatabase db;
    private LinkedList<String> attrList;
    
    public ProjectParser(String[] cmdParamsArr, String newRelName, SurlyDatabase db)
    {
        this.relName = cmdParamsArr[cmdParamsArr.length-1];
        this.newRelName = newRelName;
        this.db = db;

        this.attrList = new LinkedList<>();

        for (int i = 0; i < cmdParamsArr.length - 2; ++i)
            this.attrList.add(cmdParamsArr[i]);

        if (this.attrList.size() != 1)
        {
            for (int i = 0; i < this.attrList.size()-1; ++i)
                attrList.set(i,attrList.get(i).substring(0, attrList.get(i).length()-1));
        }
    }

    public void projectRelation()
    {
        Relation rel = db.getRelation(this.relName);

        // check relation relation exists
        if (rel == null)
            return;

        // check to maintain rules of temp relation
        if (rel != null)
        {
            if (rel.isTemp())
                db.destroyRelation(this.newRelName);
            else 
                return;
        }

        LinkedList<Attribute> schema = rel.getSchema();
        LinkedList<Tuple> tuples = rel.getTuples();
        int[] ordering = new int[attrList.size()]; 

        LinkedList<Attribute> newSchema = new LinkedList<>();
        LinkedList<Tuple> newTuples = new LinkedList<>();

        int j = 0;

        for (int i = 0; i < schema.size(); ++i)
        {
            if (attrList.contains(schema.get(i)))
            {
                ordering[j] = i;
                j++;
            }
        }

        for (int i = 0; i < ordering.length; ++i)
        {
            newSchema.add(schema.get(ordering[i]));
        }

        LinkedList<AttributeValue> tempVals;
        LinkedList<AttributeValue> currTup;

        for (int i = 0; i < tuples.size(); ++i)
        {
            currTup = tuples.get(i).getValues();
            tempVals = new LinkedList<>();

            for (j = 0; j < ordering.length; ++j)
                tempVals.add(currTup.get(i));

            newTuples.add(new Tuple(tempVals));
        }

        Relation newRel = new Relation(this.newRelName, newSchema, true);
        newRel.setTuples(newTuples);

        db.createRelation(newRel);
    }

}
