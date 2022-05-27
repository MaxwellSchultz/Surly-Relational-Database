import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class LexicalAnalyzer {

    public static SurlyDatabase db = new SurlyDatabase();

  /* Parses the given file into individual commands
		and passes each to the appropriate parser */
    public void run(String fileName) throws FileNotFoundException{
      File file = new File(fileName);
      Scanner scan = new Scanner(file);

      LinkedList<Attribute> catalogSchema = new LinkedList<>();
      catalogSchema.add(new Attribute("RELATION", "CHAR", 13));
      catalogSchema.add(new Attribute("ATTRIBUTES", "NUM", 10));
      Relation catalog = new Relation("CATALOG", catalogSchema);
      db.createRelation(catalog);

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
        
        Relation catalog = db.getRelation("CATALOG");
        LinkedList<AttributeValue> preTuple = new LinkedList<>();
        preTuple.add(new AttributeValue());
        preTuple.getLast().setName("RELATION");
        preTuple.getLast().setValue(newRel.getName());
        preTuple.add(new AttributeValue());
        preTuple.getLast().setName("ATTRIBUTES");
        preTuple.getLast().setValue(Integer.toString(newRel.getSchema().size()));

        Tuple catalogTup = new Tuple(preTuple);
        catalog.insert(catalogTup);
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
        if (scan.next().equals("CATALOG;"))
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
          db.getRelation("CATALOG").removeElement(currRel.getName());
        }
        else
        {
          System.out.println("ERROR (DESTROY): RELATION NOT FOUND");
        }

      }
      else if (cmdName.equals("DELETE"))
      {
        if (scan.next().equals("CATALOG;"))
        {
          System.out.println("ERROR: CANNOT DELETE RELATION (CATALOG)");
          scan.close();
          return;
        }
        String[] cmdWhereCheckArr = cmd.split(" ");
        cmdWhereCheckArr = Arrays.copyOfRange(cmdWhereCheckArr, 1, cmdWhereCheckArr.length - 1);
        if(cmdWhereCheckArr[2].equals("WHERE")) {
          DeleteWhereParser dwp = new DeleteWhereParser(cmdParams, db);
          dwp.parseDeleteTuples();
        } else{
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
      }
      // word is the variable name (assume good input) - MAX HANDLE ERROR HANDLING FOR BAD INPUT LATER PLEASE !!!
      else {
        String[] cmdWhereCheckArr = cmd.split(" ");
        cmdWhereCheckArr = Arrays.copyOfRange(cmdWhereCheckArr, 1, cmdWhereCheckArr.length - 1);
        System.out.println(Arrays.toString(cmdWhereCheckArr));
        if(cmdWhereCheckArr[2].equals("SELECT")) {
          try {
            if(cmdWhereCheckArr[3].equals("WHERE")) {
              SelectWhereParser swp = new SelectWhereParser(cmdParams, db);
            }
          } catch (IndexOutOfBoundsException e) {
            // SelectParser sp = new SelectParser();
          }
        } else if(cmdWhereCheckArr[2].equals("PROJECT")) {
          // run project stuff here max
        }

      }

      scan.close();
    }
}
