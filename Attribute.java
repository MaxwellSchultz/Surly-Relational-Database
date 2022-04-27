
public class Attribute {
	private String name;	/* name of the attribute */
	private String dataType;	/* data type of the attribute */
	private int length;		/* length of the attribute */

	public Attribute(String name, String dataType, int length)
	{
		this.name = name;
		this.dataType = dataType;
		this.length = length;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public String getDataType() {
		return dataType;
	}

	public int getLength() {
		return length;
	}

}
