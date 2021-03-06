package minecrafttransportsimulator.jsondefs;

public class JSONPoleComponent extends AJSONMultiModel<JSONPoleComponent.PoleGeneral>{

    public class PoleGeneral extends AJSONMultiModel<JSONPoleComponent.PoleGeneral>.General{
    	public String type;
    	public float radius;
    	public TextLine[] textLines;
    }
    
    public class TextLine{
    	public byte characters;
    	public float xPos;
    	public float yPos;
    	public float zPos;
    	public float scale;
    	public String color;
    }
}