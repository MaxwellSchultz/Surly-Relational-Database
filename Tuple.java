import java.util.LinkedList;

public class Tuple {
    private LinkedList<AttributeValue> values;  /* Values of each attribute in the tuple */

    public Tuple(LinkedList<AttributeValue> values) {
        this.values = values;
    }

    /* Returns the value of the specified attribute */
    public String getValue(String attributeName) {
        for(int i = 0; i < values.size(); i++) {
            if(values.get(i).getName().equals(attributeName)) {
                return values.get(i).getValue();
            }
        }

        return "ERROR";
    }
}
