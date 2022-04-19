import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexicalAnalyzer {
	/* Parses the given file into individual commands
		and passes each to the appropriate parser */
    public void run(String fileName) throws FileNotFoundException{
      File file = new File(fileName);
      Scanner scan = new Scanner(file);
      String currTotCmd = "";                                 // stores total command until semicolon is found
      String currTok;                                         // current token in command parse

      while (scan.hasNext())                                  // while more tokens are avalible
      {
        currTok = scan.next();                                // skip whitespace to first token

        if (currTok.charAt(0) == '#')                         // if first char of first token is comment syntax
        {
          scan.nextLine();                                    // then skip line
          continue;
        }
        
        currTotCmd = currTotCmd.concat(" ".concat(currTok));  // if not a command, begin adding tokens to currTotCmd
        if (currTok.contains(";"))                            // if semicolon is hit
        {
            exeCmd(currTotCmd);                               // test command and attempt to execute
            currTotCmd = "";                                  // reset currTotCmd for next command
        }
      }

      scan.close();
    }

    private static void exeCmd(String cmd)
    {
      Scanner scan = new Scanner(cmd);                        // setup scanner to tokenize command
      String cmdName = scan.next();                           // first token should be command name
      String cmdParams = cmd.substring(cmdName.length()+2, cmd.length()-1); // remove command name to get string only containing paramenters

      if (cmdName.equals("RELATION"))
      {
        RelationParser rp = new RelationParser(cmdParams);
        int attCount = rp.parseAttributeCount();
        if (attCount > 0)
          System.out.println("Creating " + rp.parseRelationName() + " with " + rp.parseAttributeCount() + " attributes");
        else
          System.out.println("Incorrect syntax for RELATION command. Please use \"()");
      }
      else if (cmdName.equals("INSERT"))
      {
        InsertParser ip = new InsertParser(cmdParams);
        int attCount = ip.parseAttributeCount();
        if (attCount > 1)
          System.out.println("Inserting " + ip.parseAttributeCount() + " attributes to " + ip.parseRelationName());
        else
          System.out.println("Inserting " + ip.parseAttributeCount() + " attribute to " + ip.parseRelationName());
      }
      else if (cmdName.equals("PRINT"))
      {
        PrintParser pp = new PrintParser(cmdParams);
        String[] rels = pp.parseRelationNames();
        int len = rels.length;
        int i = 0;

        if (len > 1)
          System.out.print("Printing " + rels.length + " relations: ");
        else
          System.out.print("Printing " + rels.length + " relation: ");

        for (; i < rels.length - 1; i++)
          System.out.print(rels[i] + ", ");

        System.out.println(rels[i] + ".");
      }

      scan.close();
    }
}
