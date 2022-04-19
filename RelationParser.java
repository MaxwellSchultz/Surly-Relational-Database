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
        String relName = scan.next();

        scan.close();

        return relName;
    }
	
	/* Parses and returns the number of attributes to create */
    public int parseAttributeCount() {
        Scanner scan = new Scanner(this.input);
        String currTok;
        int numOfAtt = 0;
        scan.next();
        currTok = scan.next();

        if (currTok.charAt(0) == '(')
        {
            while(scan.hasNext())
            {
                currTok = scan.next();
                if (currTok.contains(","))
                    ++numOfAtt;
            }
        }

        if (currTok.charAt(currTok.length()-1) == ')')
            numOfAtt++;
        else
            numOfAtt = -1;

        scan.close();
        
        return numOfAtt;
    }
}
