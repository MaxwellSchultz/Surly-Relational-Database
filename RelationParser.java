import java.util.LinkedList;
import java.util.Scanner;

public class RelationParser {
    /* Reference to the input string being parsed */
    private String input;

	/* Constructor to initialize the input field */
    public RelationParser(String input) {
        this.input = input; 
	}
	
	/* Parses and returns the number of attributes to create */
    public Relation parseRelation() {
        Scanner scan = new Scanner(this.input);
        String currTok;                                 // stores current token
        String relSet = "";                                  // contains values needed for one attribute in schema
        LinkedList<String> relEle = new LinkedList<>(); // stores the elements and their attributes of the schema
        Relation returnRel;                              
        int numOfAtt = 0;                               // stores total number of attributes
        String relName = scan.next();                   // get relation name
        currTok = scan.next();                          // get first attribute

        if (currTok.charAt(0) == '(')                   // test correct relation syntax ("(")
        {
            while(scan.hasNext())                       // while more relations are avalible 
            {
                currTok = scan.next();                  // get net relation
                relSet += currTok + " ";
                if (currTok.contains(","))              // test for correct syntax (",")
                {
                    relSet += currTok.substring(0, currTok.length()-1);
                    relEle.add(relSet);
                }
            }
        }

        returnRel = generateRelation(relName, relEle);

        scan.close();                       // reserve system resources
        
        return returnRel;
    }

    private Relation generateRelation(String relName, LinkedList<String> relEle)
    {
        LinkedList<Attribute> attr = new LinkedList<>(); 
        String name;
        String dataType;
        int length;
        Scanner scan;

        for (int i = 0; i < relEle.size(); ++i)
        {
            scan = new Scanner(relEle.get(i));
            name = scan.next();
            dataType = scan.next();
            System.out.println(scan.next());
            length = 10;

            attr.add(new Attribute());

            attr.get(i).setName(name);
            attr.get(i).setDataType(dataType);
            attr.get(i).setLength(length);
        
            scan.close();
        }

        Relation returnRel = new Relation(relName, attr);

        return returnRel;
    }
}
