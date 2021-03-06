package minecrafttransportsimulator.jsondefs;

public class JSONDecor extends AJSONMultiModel<JSONDecor.DecorGeneral>{

    public class DecorGeneral extends AJSONMultiModel<JSONDecor.DecorGeneral>.General{
    	public String type;
    	public float width;
    	public float height;
    	public float depth;
    	public TextLine[] textLines;
    }
    
    public class TextLine{
    	public float xPos;
    	public float yPos;
    	public float zPos;
    	public float scale;
    	public String color;
    }
}