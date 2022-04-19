import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexicalAnalyzer {
	/* Parses the given file into individual commands
		and passes each to the appropriate parser */
    public void run(String fileName) throws FileNotFoundException{
      File file = new File(fileName);
      Scanner scan = new Scanner(file);
      String currTotCmd = "";
      String currTok;

      while (scan.hasNext())
      {
        currTok = scan.next();

        if (currTok.charAt(0) == '#')
        {
          scan.nextLine();
          continue;
        }
        
        currTotCmd = currTotCmd.concat(" ".concat(currTok));
        if (currTok.contains(";"))
        {
            exeCmd(currTotCmd);
            currTotCmd = "";
        }
      }

      scan.close();
    }

    private static void exeCmd(String cmd)
    {
      Scanner scan = new Scanner(cmd);
      String cmdName = scan.next();
      String cmdParams = cmd.substring(cmdName.length()+2, cmd.length()-1);

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
