import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class DeleteWhereParser {

    String input;
    SurlyDatabase db;

    Relation r;

    public DeleteWhereParser(String input, SurlyDatabase db) {
        this.input = input;
        this.db = db;
        String[] splitInput = input.split(" ");
        this.r = db.getRelation(splitInput[0]);
    }

    public void parseDeleteTuples() {
        String cmdString = getCmdStrings(input);


        ArrayList<String[]> queriesCmds = parseString(cmdString);

//        System.out.println("Queries Commands ArrayList: ");
//        for(String[] ss :queriesCmds) {
//            System.out.println(Arrays.toString(ss));
//        }
//        System.out.println();

        ArrayList<Tuple> tuplesToDelete = new ArrayList<>();

        for(String[] eachCmd : queriesCmds) {
            System.out.println(Arrays.toString(eachCmd) + "AAAAA");
            ArrayList<Tuple> tempTuples = runQuery(eachCmd);
            for(int i = 0; i < tempTuples.size(); i ++) {
                tuplesToDelete.add(tempTuples.get(i));
            }
        }

        for(int i = 0; i < tuplesToDelete.size(); i ++) {
            r.getTuples().remove(tuplesToDelete.get(i));
        }



    }

    private static ArrayList<String[]> parseString(String str) {

        ArrayList<String[]> arrL = new ArrayList<>();
        String[] preParse = str.split(" or ");

        for(String s : preParse) {
            arrL.add(s.split(" and "));
        }

        return arrL;
    }

    public ArrayList<Tuple> runQuery(String[] ss) {
        ArrayList<Tuple> tempTuples = new ArrayList<>();

        String firstCmd = ss[0];

        String[] firstSplitCmd = firstCmd.split(" ");

        System.out.println(Arrays.toString(firstSplitCmd));
        String firstAttributeName = firstSplitCmd[0];
        String firstOperator = firstSplitCmd[1];
        String firstAttributeValue = firstSplitCmd[2];

        LinkedList<Tuple> tuplesInRelation = r.getTuples();

        // run through all the tuples and add the tuples that fit the first command
        for(int i = 0; i < tuplesInRelation.size(); i ++) {
            System.out.println(i);
            Tuple curTuple = tuplesInRelation.get(i);
            switch (firstOperator) {
                case ">":
                    if (Integer.parseInt(curTuple.getValue(firstAttributeName)) > Integer.parseInt(firstAttributeValue)) {
                        tempTuples.add(curTuple);
                    } else continue;
                    break;
                case "<":
                    if (Integer.parseInt(curTuple.getValue(firstAttributeName)) < Integer.parseInt(firstAttributeValue)) {
                        tempTuples.add(curTuple);
                    } else continue;
                    break;
                // implement different case for string and integer
                case "=":
                    if(isNumeric(firstAttributeValue)) {
                        if (Integer.parseInt(curTuple.getValue(firstAttributeName)) == Integer.parseInt(firstAttributeValue)) {
                            tempTuples.add(curTuple);
                        } else continue;
                    } else {
                        if (curTuple.getValue(firstAttributeName).equals(firstAttributeValue)) {
                            tempTuples.add(curTuple);
                        } else continue;
                    }

                    break;
                // implement different case for string and integer
                case "!=":
                    if(isNumeric(firstAttributeValue)) {
                        if (Integer.parseInt(curTuple.getValue(firstAttributeName)) != Integer.parseInt(firstAttributeValue)) {
                            tempTuples.add(curTuple);
                        } else continue;
                    } else {
                        if (!curTuple.getValue(firstAttributeName).equals(firstAttributeValue)) {
                            tempTuples.add(curTuple);
                        } else continue;
                    }
                    break;
                case ">=":
                    if (Integer.parseInt(curTuple.getValue(firstAttributeName)) >= Integer.parseInt(firstAttributeValue)) {
                        tempTuples.add(curTuple);
                    } else continue;
                    break;
                case "<=":
                    if (Integer.parseInt(curTuple.getValue(firstAttributeName)) <= Integer.parseInt(firstAttributeValue)) {
                        tempTuples.add(curTuple);
                    } else continue;
                    break;
                default:
                    System.out.println("Agony, torment even.");
            }
        }

        // run through the rest of the commands
        for(int i = 1; i < ss.length; i++) {

            String[] splitCmd = ss[i].split(" ");
            String attributeName = splitCmd[0];
            String operator = splitCmd[1];
            String attributeValue = splitCmd[2];

            // run through all of the tuples that fit the first command (0 index), and then remove them from the returned tuple list if they dont fit all the requirements
            for(int j = 0; j < tempTuples.size(); j ++) {
                Tuple curTuple = tempTuples.get(i);
                // operations have to be reversed to act as an overlap
                switch (operator) {
                    case ">":
                        if (Integer.parseInt(curTuple.getValue(attributeName)) <= Integer.parseInt(attributeValue)) {
                            tempTuples.add(j, null);
                        } else continue;
                        break;
                    case "<":
                        if (Integer.parseInt(curTuple.getValue(attributeName)) >= Integer.parseInt(attributeValue)) {
                            tempTuples.add(j, null);
                        } else continue;
                        break;
                    case "=":
                        if(isNumeric(firstAttributeValue)) {
                            if (Integer.parseInt(curTuple.getValue(firstAttributeName)) != Integer.parseInt(firstAttributeValue)) {
                                tempTuples.add(curTuple);
                            } else continue;
                        } else {
                            if (!curTuple.getValue(firstAttributeName).equals(firstAttributeValue)) {
                                tempTuples.add(curTuple);
                            } else continue;
                        }
                        break;
                    case "!=":
                        if(isNumeric(firstAttributeValue)) {
                            if (Integer.parseInt(curTuple.getValue(firstAttributeName)) == Integer.parseInt(firstAttributeValue)) {
                                tempTuples.add(curTuple);
                            } else continue;
                        } else {
                            if (curTuple.getValue(firstAttributeName).equals(firstAttributeValue)) {
                                tempTuples.add(curTuple);
                            } else continue;
                        }
                        break;
                    case ">=":
                        if (Integer.parseInt(curTuple.getValue(attributeName)) < Integer.parseInt(attributeValue)) {
                            tempTuples.add(j, null);
                        } else continue;
                        break;
                    case "<=":
                        if (Integer.parseInt(curTuple.getValue(attributeName)) > Integer.parseInt(attributeValue)) {
                            tempTuples.add(j, null);
                        } else continue;
                        break;
                    default:
                        System.out.println("Agony, torment even.");
                }
            }
        }

        ArrayList<Tuple> output = new ArrayList<>();

        // return all the elements in the original tempTuples that weren't set to null
        for(int i = 0; i < tempTuples.size(); i ++) {
            if(tempTuples.get(i) != null) {
                output.add(tempTuples.get(i));
            }
        }

        return output;
    }

    private static String getCmdStrings(String str) {
        String output = "";
        String[] splitInput = str.split(" ");
        splitInput = Arrays.copyOfRange(splitInput, 2, splitInput.length);
        for(String s : splitInput) {
            output += s + " ";
        }

        return output.trim();
    }

    private static boolean isNumeric(String numAsString) {
        int testInt;

        try {
            testInt = Integer.parseInt(numAsString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
