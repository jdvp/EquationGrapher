package model;

import java.awt.Graphics;
import java.util.ArrayList;

public class Model {

	private String equation;
	private ArrayList<Object> eq;
	private int pixels[][];
	private ArrayList<Double> xPixels;
	private IM2VAdapter myAdapter;
	private int width;
	private int height;
	
	private Integer globalX = 1;
	private Integer globalY = globalX.intValue();
	
	public Model(IM2VAdapter nullObject) {
		myAdapter = nullObject;
	}

	public void start() {
		
	}

	
	public void calculate(int lx, int ux, int ly, int uy, String inEq, int px, int py) {
		
		calculateAxes(lx,ux,ly,uy,inEq,px,py);
		reduceEquation();
		removeExtraCharacters();
		initXPixels(lx, ux);
		doEquation(ly, uy);
		myAdapter.update();
		
	}

	public void calculateAxes(int lx, int ux, int ly, int uy, String inEq, int px, int py)
	{
		width = px;
		height = py;
		
		equation = inEq;
		pixels = new int[px][py];
		
		for(int x = 0; x < px; x++)
			for(int y = 0; y < py; y++)
				pixels[x][y] = 0;
		
		int xAxis = width / (ux - lx) * Math.abs(lx);
		int yAxis = height / (uy - ly) * Math.abs(uy);
		
		for(int x = 0; x<width; x++)
			pixels[x][yAxis] = 1;
		
		for(int y = 0; y<height; y++)
			pixels[xAxis][y] = 1;
	}
	public void updateGraph(Graphics g) {
		if(pixels != null)
		{
			for(int x = 0 ; x < width; x++)
				for(int y = 0; y < height; y++)
					if(pixels[x][y] == 1)
						g.drawLine(x, y, x, y);
		}
	}

	public void reduceEquation()
	{
		eq = new ArrayList<Object>();
		int num = 0;
		for(char a : equation.toCharArray())
		{
			int me = a;
			if(me>=48 && me<=57)
			{
				num*=10;
				num += Integer.parseInt(""+a);
				
			}
			else
			{
				if(num!=0)
					eq.add(num);
				num=0;
				eq.add(a);
			}
		}
		if(num!=0)
			eq.add(num);
	}
	
	public void removeExtraCharacters()
	{
		ArrayList<Object> tempEq = new ArrayList<Object>();
		for(Object a : eq)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				String me = "" + (char) a;
				if(me.matches("[x+-/*^]"))
				{
					tempEq.add(a);
				}
			}
			else if(a.getClass().toString().equalsIgnoreCase("class java.lang.Integer"))
				tempEq.add(a);
		}
		
		eq = tempEq;
	}
	
	public void initXPixels(int lx, int ux)
	{
		xPixels = new ArrayList<Double>();
		
		for(int x = 0; x < width; x++)
		{
			xPixels.add(lx + (((ux - lx)/(width * 1.0))*x));
		}
	}
	
	public void doEquation(int ly, int uy)
	{
	
		int closestToZeroIndex = -1;
		double closestVal = Double.POSITIVE_INFINITY;
		
		//Erase any other markings on the chart
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
				pixels[x][y] = 0;
			
			if(Math.abs(xPixels.get(x)) < closestVal)
			{
				closestVal = Math.abs(xPixels.get(x));
				closestToZeroIndex = x;
			}
		}
		
		//If there is no zero in the x range, grpahing will not work properly
		//Therefore, we set the lowest value in the x range to zero
		xPixels.set(closestToZeroIndex, 0.0);
		
		easyPrint(xPixels);
		
		
		//For every x value, calculate a y value
		for(int x = 0; x < width; x++)
		{
			double myVal = xPixels.get(x);
			double yVal = runEquation(myVal);
			
			easyPrint(myVal, yVal);
			
			if(yVal == 0)
			{
				for(int a = 0; a < height; a++)
					pixels[x][a] =  1;
			}
			
			//Make sure y is graphable and then graph it
			if(yVal >= ly && yVal <= uy)
			{
				int myPixelVal = valueToPixel(yVal, ly, uy);
				if(yVal == 0)
				{
					for(int b = 0; b < width; b++)
						pixels[b][myPixelVal] = 1;
				}
				if(myPixelVal >=0 && myPixelVal < height)
				{
					pixels[x][myPixelVal] = 1;
				}
			}
		}
		
	}
	
	public int valueToPixel(double value, int ly, int uy)
	{
		//easyPrint(value, ly, uy);
		return height - (int)((height * (value - 1.0 * ly))/(1.0 *uy - 1.0 * ly));
	}
	
	public double runEquation(double xVal)
	{
		ArrayList<Object> temp = new ArrayList<Object>();
		
		//Replace x with its value
		for(Object a: eq)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				if((char)a == 'x')
					temp.add(xVal);
				else
					temp.add(a);
			}
			else if(a.getClass().toString().equalsIgnoreCase("class java.lang.Integer"))
			{
				Double intToDouble = ((Integer) a) * 1.0;
				temp.add(intToDouble);
			}
		}
		
		//easyPrint(temp);
		
		ArrayList<Integer> indexesToRemove = new ArrayList<Integer>();
		
		//Parse commands
		int ind =0;
		for(Object a : temp)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				if((char) a == '^')
				{
					temp.set(ind, Math.pow(((double) temp.get(ind-1)), (double) temp.get(ind + 1)));
					indexesToRemove.add(ind-1);
					indexesToRemove.add(ind+1);
				}
			}
			ind++;
		}
		
		for(int a = 0; a < indexesToRemove.size(); a++)
		{
			temp.remove((int)indexesToRemove.get(a));
			for(int b = a + 1; b < indexesToRemove.size(); b++)
				indexesToRemove.set(b, indexesToRemove.get(b) - 1);
		}
		
		//easyPrint(temp);
		indexesToRemove = new ArrayList<Integer>();
		
		//Parse commands
		ind =0;
		for(Object a : temp)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				if((char) a == '*')
				{
					temp.set(ind, (double) temp.get(ind-1)*(double) temp.get(ind + 1));
					indexesToRemove.add(ind-1);
					indexesToRemove.add(ind+1);
				}
			}
			ind++;
		}
		
		for(int a = 0; a < indexesToRemove.size(); a++)
		{
			temp.remove((int)indexesToRemove.get(a));
			for(int b = a + 1; b < indexesToRemove.size(); b++)
				indexesToRemove.set(b, indexesToRemove.get(b) - 1);
		}
		
		indexesToRemove = new ArrayList<Integer>();
		
		//Parse commands
		ind =0;
		for(Object a : temp)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				if((char) a == '/')
				{
					temp.set(ind, (double) temp.get(ind-1)/(double) temp.get(ind + 1));
					indexesToRemove.add(ind-1);
					indexesToRemove.add(ind+1);
				}
			}
			ind++;
		}
		
		for(int a = 0; a < indexesToRemove.size(); a++)
		{
			temp.remove((int)indexesToRemove.get(a));
			for(int b = a + 1; b < indexesToRemove.size(); b++)
				indexesToRemove.set(b, indexesToRemove.get(b) - 1);
		}
		
		
		indexesToRemove = new ArrayList<Integer>();
		
		//Parse commands
		ind =0;
		for(Object a : temp)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				if((char) a == '+')
				{
					temp.set(ind, (double) temp.get(ind-1)+(double) temp.get(ind + 1));
					indexesToRemove.add(ind-1);
					indexesToRemove.add(ind+1);
				}
			}
			ind++;
		}
		
		for(int a = 0; a < indexesToRemove.size(); a++)
		{
			temp.remove((int)indexesToRemove.get(a));
			for(int b = a + 1; b < indexesToRemove.size(); b++)
				indexesToRemove.set(b, indexesToRemove.get(b) - 1);
		}
		
		indexesToRemove = new ArrayList<Integer>();
		
		//Parse commands
		ind =0;
		for(Object a : temp)
		{
			if(a.getClass().toString().equalsIgnoreCase("class java.lang.Character"))
			{
				if((char) a == '-')
				{
					temp.set(ind, (double) temp.get(ind-1)-(double) temp.get(ind + 1));
					indexesToRemove.add(ind-1);
					indexesToRemove.add(ind+1);
				}
			}
			ind++;
		}
		
		for(int a = 0; a < indexesToRemove.size(); a++)
		{
			temp.remove((int)indexesToRemove.get(a));
			for(int b = a + 1; b < indexesToRemove.size(); b++)
				indexesToRemove.set(b, indexesToRemove.get(b) - 1);
		}
		
		return (double) temp.get(0);
	}
	
	private void easyPrint(Object ... args)
	{
		String retVal = "";
		for(Object a: args)
		{
			retVal += a.toString() + " ";
		}
		System.out.println(retVal);
	}

}
