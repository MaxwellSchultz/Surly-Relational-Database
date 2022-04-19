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
        String relName = scan.next();

        scan.close();

        return relName;
    }
	
	/* Parses and returns the number of attributes to insert */
    public int parseAttributeCount() {
        Scanner scan = new Scanner(this.input);
        String currTok;
        int numOfAtt = 0;
        scan.next();

        while (scan.hasNext())
        {
            currTok = scan.next();
            if (currTok.charAt(0) == '\'')
            {
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
