import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class LexicalAnalyzer {

    public static SurlyDatabase db = new SurlyDatabase();

	/* Parses the given file into individual commands
		and passes each to the appropriate parser */
    public void run(String fileName) throws FileNotFoundException{
      File file = new File(fileName);
      Scanner scan = new Scanner(file);

      /*
      LinkedList<Attribute> catalogSchema = new LinkedList<>();
      catalogSchema.add(new Attribute("RELATION", "CHAR", 13));
      catalogSchema.add(new Attribute("ATTRIBUTES", "NUM", 10));
      Relation catalog = new Relation("CATALOG", catalogSchema);
      db.createRelation(catalog);
      */

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
        Relation newRel = rp.parseRelation();
        db.createRelation(newRel);
        //exeCmd("INSERT CATALOG " + newRel.getName() + " " + newRel.getSchema().size() + ";");
      }
      else if (cmdName.equals("INSERT"))
      {
        InsertParser ip = new InsertParser(cmdParams);

        String relName = scan.next();           // relation name

        Relation currRel = db.getRelation(relName);

        if (currRel != null)  // check that relation exists
        {
          Tuple newTup = ip.parseTuple();
          currRel.insert(newTup);
        }
        else
        {
          System.out.println("ERROR (INSERT): RELATION NOT FOUND");
        }
      }
      else if (cmdName.equals("PRINT"))
      {
        PrintParser pp = new PrintParser(cmdParams);
        String[] rels = pp.parseRelationNames();
        
        for (String relName : rels)
        {
          db.getRelation(relName).print();
        }
      }
      else if (cmdName.equals("DESTROY"))
      {
        if (scan.next().equals("CATALOG"))
        {
          System.out.println("ERROR (DESTROY): CANNOT DESTROY RELATION (CATALOG)");
          scan.close();
          return;
        }

        DestroyParser dp = new DestroyParser(cmdParams);
        Relation currRel = db.getRelation(dp.parseRelationName());

        if (currRel != null)
        {
          db.destroyRelation(currRel.getName());
        }
        else
        {
          System.out.println("ERROR (DESTROY): RELATION NOT FOUND");
        }

      }
      else if (cmdName.equals("DELETE"))
      {
        if (scan.next().equals("CATALOG"))
        {
          System.out.println("ERROR: CANNOT DELETE RELATION (CATALOG)");
          scan.close();
          return;
        }

        DeleteParser dp = new DeleteParser(cmdParams);
        Relation currRel = db.getRelation(dp.parseRelationName());

        if (currRel != null)
        {
          currRel.delete();
        }
        else
        {
          System.out.println("ERROR (DELETE): RELATION NOT FOUND");
        }
      }

      scan.close();
    }
}
