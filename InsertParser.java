import java.util.Scanner;

public class InsertParser {
    /* Reference to the input string being parsed */
    private String input;

	/* Constructor to initialize the input field */
    public InsertParser(String input) {
        this.input = input;
	}
	
	/* Parses and returns the name of the relation to insert into */
    public String parseRelationName() {
        Scanner scan = new Scanner(this.input); 
        String relName = scan.next();       // first token should contain relation name

        scan.close();

        return relName;
    }
	
	/* Parses and returns the number of attributes to insert */
    public int parseAttributeCount() {
        Scanner scan = new Scanner(this.input);
        String currTok;                             // stores current token
        int numOfAtt = 0;                           // total number of attributes
        scan.next();

        while (scan.hasNext())                      // while more tokens are avalible
        {
            currTok = scan.next();
            if (currTok.charAt(0) == '\'')          // if a string sequence has been hit
            {
                // while more tokens (within string) are avalible and no ending quote has been hit
                while (scan.hasNext() && (currTok.charAt(currTok.length()-1) != '\''))
                {
                    currTok = scan.next();
                }

                numOfAtt++;
            }
            else 
                numOfAtt++;
        }

        scan.close();
        
        return numOfAtt;
    }
}
