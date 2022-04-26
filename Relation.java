import java.util.LinkedList;

public class Relation {
    private String name; /* name of the relation */
	private LinkedList<Attribute> schema;	/* Schema of the relation */
	private LinkedList<Tuple> tuples;	/* Tuples stored on the relation */
	final int paddingWidth = 2;

	public Relation(String name, LinkedList<Attribute> schema)
	{
		this.name = name;
		this.schema = schema;
	}

	public String testPrinter() // test printer
	{
		return this.name;
	}

	/* Formats and prints the relation's name, schema, and tuples */
	public void print() {
		
		Attribute currAtt;
		int totalLen;
		int innerLength = 0;
		int numOfVertBars = -1;  

		for (int i = 0; i < schema.size(); ++i)
		{
			currAtt = schema.get(i);

			innerLength += currAtt.getLength();
			innerLength += paddingWidth;
			numOfVertBars++; 
		}

		totalLen = innerLength + numOfVertBars + paddingWidth;

		printStars(totalLen);
		System.out.println();
		System.out.print("| ");
		System.out.print(this.name);
		printSpace(innerLength + numOfVertBars - this.name.length() - 1);
		System.out.println("|");
		printLines(totalLen);
		System.out.println();

		for (int i = 0; i < schema.size(); ++i)
		{
			currAtt = schema.get(i);

			System.out.print("| ");
			System.out.print(currAtt.getName());
			printSpace(currAtt.getLength()-currAtt.getName().length()-1);
			System.out.println("|");
		}

		printLines(totalLen);

	}

	/* Adds the specified tuple to the relation */
    public void insert(Tuple tuple) {

    }

	/* Remove all tuples from the relation */
	public void delete() {
		
	}

	private void printSpace(int nums)
	{
		for (int i = 0; i < nums; ++i)
		{
			System.out.print(" ");
		}
	}

	private void printStars(int nums)
	{
		for (int i = 0; i < nums; ++i)
		{
			System.out.print("*");
		}
	}

	private void printLines(int nums)
	{
		for (int i = 0; i < nums; ++i)
		{
			System.out.print("-");
		}
	}
}
