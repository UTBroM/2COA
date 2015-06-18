package gamepack.data.drawable;

import java.awt.event.MouseEvent;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Rectangle;

import com.sun.xml.internal.bind.v2.runtime.Coordinator;
//Rédigée par le groupe
public class TextArea implements MouseListener, DrawableObject, KeyListener
{
	private String textEntered;
	private Rectangle textArea;
	private Color background;
	private Color foreground;
	private Color foregroundBlock;
	private boolean enteringText;
	
	//constructor with the rectangle parameters
	public TextArea(int x, int y, int w, int h)
	{
		enteringText = false;
		textEntered = "";
		textArea = new Rectangle(x,y,w,h);
		background = new Color(150, 150, 150, 150);
		foreground = new Color(255, 255, 255);
		foregroundBlock = new Color(200, 200, 200);
	}
	
	//check if the x & y are in the rectangle
	private boolean isIn(int x, int y)
	{
		return x >= textArea.getX() && x <= textArea.getX()+textArea.getWidth() && 
				y >= textArea.getY() && y <= textArea.getY()+textArea.getHeight();
	}
	
	//set the position of the rectangle on the screen
	public void setPosition(int x, int y)
	{
		textArea.setLocation(x,y);
	}

	//check if a key is pressed (arg0 = keyCode, arg1 = keyCharacter)
	public void keyPressed(int arg0, char arg1)
	{
		//If we are entering a text, then add the text if it's a letter
		if(enteringText)
			if(textEntered.length() < 15)
				if((arg1 >= 'a' && arg1 <= 'z') || (arg1 >= 'A' && arg1 <= 'Z') )
					textEntered+= arg1;
		
		//if back, remove a letter
		if(arg0 == Input.KEY_BACK)
			if(textEntered.length() >= 1)
				textEntered = textEntered.substring(0, textEntered.length()-1);
		
		//if enter, stop entering text
		if(arg0 == Input.KEY_ENTER)
			enteringText = false;
		
	}

	//draw the area & its text
	public void beDrawn(Graphics g)
	{
		//relief on border
		int relief = 0;
		if(enteringText)
		{
			relief = -2;
			g.setColor(new Color(100,100,100));
		}
		else
		{
			g.setColor(new Color(0,0,0));
		}
		g.fillRoundRect((int)textArea.getX()-relief, (int)textArea.getY()-relief, (int)textArea.getWidth()+relief, (int)textArea.getHeight()+relief, 2);
	
	
		
		//background
		g.setColor(background);
		g.fillRoundRect((int)textArea.getX(), (int)textArea.getY(), (int)textArea.getWidth(), (int)textArea.getHeight(), 2);
	
		//foreground
		if(enteringText)
			g.setColor(foreground);
		else
			g.setColor(foregroundBlock);
		g.drawString(textEntered, getX(), getY());
	}
	
	//check if we clicked in the area
	public void mousePressed(int arg0, int arg1, int arg2)
	{
		final int mouseX = arg1;
		final int mouseY = arg2;
		if(isIn(mouseX,mouseY))
		{
			enteringText = true;
		}
		else
			enteringText = false;
	}

	//return true if the area is active
	public boolean isEnteringText()
	{
		return enteringText;
	}
	
	//get the text in the text area
	public String getText()
	{
		return textEntered;
	}

	public int getX()
	{
		return (int) textArea.getX();
	}
	
	public int getY()
	{
		return (int) textArea.getY();
	}

	
	
	// not used listeners' methods
	
	public void mouseReleased(int arg0, int arg1, int arg2)
	{
	}

	public void mouseWheelMoved(int arg0)
	{
	}
	
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3)
	{
		
	}

	public void mouseDragged(int arg0, int arg1, int arg2, int arg3)
	{
	}

	public void mouseMoved(int arg0, int arg1, int arg2, int arg3)
	{
	}
	
	public void keyReleased(int arg0, char arg1)
	{
	}
	
	public void inputEnded()
	{
	}

	public void inputStarted()
	{
	}

	public boolean isAcceptingInput()
	{
		return true;
	}

	public void setInput(Input arg0)
	{
	}



}
