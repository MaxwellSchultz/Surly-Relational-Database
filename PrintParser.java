import java.util.LinkedList;
import java.util.Scanner;

public class PrintParser {
    /* Reference to the input string being parsed */
    private String input;

	/* Constructor to initialize the input field */
    public PrintParser(String input) {
		this.input = input;
	}
	
	/* Parses and returns the names the relations to print */
    public String[] parseRelationNames() {
        Scanner scan = new Scanner(this.input);
        LinkedList<String> relNames = new LinkedList<>();
        String currTok;

        while (scan.hasNext())
        {
            currTok = scan.next();
            if (currTok.charAt(currTok.length() - 1) == ',')
            {
                currTok = currTok.substring(0, currTok.length()-1);
            }
            relNames.add(currTok);
        }

        scan.close();

        return relNames.toArray(new String[relNames.size()]);
    }
}
