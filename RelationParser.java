import java.util.Scanner;

public class RelationParser {
    /* Reference to the input string being parsed */
    private String input;

	/* Constructor to initialize the input field */
    public RelationParser(String input) {
        this.input = input; 
	}
	
	/* Parses and returns the name of the relation to create */
    public String parseRelationName() {
        Scanner scan = new Scanner(this.input); 
        String relName = scan.next();       // first token should contain relation name

        scan.close();

        return relName;
    }
	
	/* Parses and returns the number of attributes to create */
    public int parseAttributeCount() {
        Scanner scan = new Scanner(this.input);
        String currTok;                     // stores current token
        int numOfAtt = 0;                   // stores total number of attributes
        scan.next();                        // skip relation name
        currTok = scan.next();              // get first attribute

        if (currTok.charAt(0) == '(')       // test correct relation syntax ("(")
        {
            while(scan.hasNext())           // while more relations are avalible 
            {
                currTok = scan.next();      // get net relation
                if (currTok.contains(","))  // test for correct syntax (",")
                    ++numOfAtt;
            }
        }

        if (currTok.charAt(currTok.length()-1) == ')') // test for correct syntax (")")
            numOfAtt++;
        else
            numOfAtt = -1;                  // if syntax is incorrect, return error state

        scan.close();                       // reserve system resources
        
        return numOfAtt;
    }
}
