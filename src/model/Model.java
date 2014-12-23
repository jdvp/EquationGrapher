package model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * This is the Model of the equation. It maintains an integer
 * representation of the space to be drawn, pixel wise
 * 
 * @author JD Porterfield
 *
 */
public class Model {

	/**
	 * A String representation of the equation to be parsed and displayed
	 */
	private String equation;
	/**
	 * An ArrayList representation of the equation to be parsed and displayed
	 */
	private ArrayList<Object> eq = new ArrayList<Object>();
	/**
	 * An array representation of all of the pixels in the view
	 */
	private int pixels[][];
	/**
	 * The double value assigned to each pixel along the x axis
	 */
	private ArrayList<Double> xPixels;
	/**
	 * The adapter the model uses to communicate with the view
	 */
	private IM2VAdapter myAdapter;
	/**
	 * The width of the view in pixels
	 */
	private int width;
	/**
	 * The height of the view in pixels
	 */
	private int height;
	/**
	 * An ArrayList of Points representing the pixel coordinates that the equation touches
	 */
	ArrayList<Point> equationPoints = new ArrayList<Point>();
	
	/**
	 * Instantiates the model and sets the adapter
	 * @param adapter
	 */
	public Model(IM2VAdapter adapter) 
	{
		myAdapter = adapter;
	}

	/**
	 * No-op start method since the model needs input from the
	 * view in order to graph anything
	 */
	public void start() 
	{
	}

	/**
	 * Graphs an equation based upon the many inputs specified by the user
	 * 
	 * @param lx The lower x bound
	 * @param ux The upper x bound
	 * @param ly The lower y bound
	 * @param uy The upper y bound
	 * @param inEq The equation to calculate for
	 */
	public void calculate(int lx, int ux, int ly, int uy, String inEq) 
	{
		equationPoints = new ArrayList<Point>();
		equation = inEq;
		reduceEquation();
		removeExtraCharacters();
		initXPixels(lx, ux);
		doEquation(ly, uy);
		myAdapter.update();
	}

	/**
	 * Used to calculate the axes before an equation is input
	 * 
	 * @param lx The lower x bound
	 * @param ux The upper x bound
	 * @param ly The lower y bound
	 * @param uy The upper y bound
	 * @param px The width of the component to paint on
	 * @param py The height of the component to paint on
	 */
	public void calculateAxes(int lx, int ux, int ly, int uy, int px, int py) 
	{

		equationPoints = new ArrayList<Point>();
		
		//Use the details of the system to calculate which pixels are axes
		width = px;
		height = py;
		
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
	
	/**
	 * Updates the graph based on the array of pixels.
	 * If a pixel value is 1, the pixel is filled.
	 * Otherwise, it is not.
	 * 
	 * @param g The graphics objects to paint onto.
	 */
	public void updateGraph(Graphics g) 
	{
		//Draw Axes
		if(pixels != null) {
			for(int x = 0 ; x < width; x++)
				for(int y = 0; y < height; y++)
					if(pixels[x][y] == 1) 
						g.drawLine(x, y, x, y);
		}
		
		//Draw Equation
		int count = 0;
		for(Point p : equationPoints){
			if(count!=0){
				g.drawLine((int)p.getX(), (int)p.getY(), (int)equationPoints.get(count-1).getX(), (int)equationPoints.get(count-1).getY());
			}
			else{
				g.drawLine((int)p.getX(), (int)p.getY(), (int)p.getX(), (int)p.getY());
			}
			count++;
		}
	}

	/**
	 * Takes the equation arraylist and combines all numbers in 
	 * the arraylist. Since the numbers in the arraylist are input 
	 * characterwise, we need to convert individual input characters
	 * into actual numbers. 
	 */
	public void reduceEquation() 
	{
		eq = new ArrayList<Object>();
		int num = 0;
		for(char a : equation.toCharArray()) {
			int me = a;
			if(me>=48 && me<=57) {
				num*=10;
				num += Integer.parseInt(""+a);
				
				//Allows for zeroes by themselves
				if(num == 0 && me == 48 && eq.lastIndexOf((Integer) 0)!=eq.size()-1)
					eq.add(0);
			}
			else {
				if(num!=0)
					eq.add(num);					
				num=0;
				eq.add(a);
			}
		}
		if(num!=0)
			eq.add(num);
	}
	
	/**
	 * Takes the equation arraylist and removes 
	 * all extraneous characters. That is any character
	 * that is not allowed in the equation.
	 * Further, all extraneous leading zeroes are removed.
	 */
	public void removeExtraCharacters() 
	{
		ArrayList<Object> tempEq = new ArrayList<Object>();
		
		//Remove disallowed characters
		for(Object a : eq) {
			if(isCharacter(a)) {
				String me = "" + (char) a;
				if(me.matches("[x+-/*^()epl]")) {
					tempEq.add(a);
				}
			}
			else if(isInteger(a))
				tempEq.add(a);
		}
		
		//Remove extraneous leading zero
		ArrayList<Object> temp2 = new ArrayList<Object>();
		temp2.add(tempEq.get(0));
		for(int x = 1; x < tempEq.size()-1; x++) {
			if(isInteger(tempEq.get(x))) {
				if(!(tempEq.get(x).equals(0) && (isInteger(tempEq.get(x+1))))) {
					temp2.add(tempEq.get(x));
				}
			}
			else {
				temp2.add(tempEq.get(x));
			}
		}
		temp2.add(tempEq.get(tempEq.size()-1));
		eq = temp2;
	}
	
	/**
	 * Creates an array xPixels that represents the x value for every pixel
	 * that will be utilized on the canvas. If there are 10 values (0-9) and 
	 * 10 pixels for instance, xPixels = {0,1,2,3,4,5,6,7,8,9}
	 * 
	 * @param lx The lower x bound
	 * @param ux The upper x bound
	 */
	public void initXPixels(int lx, int ux) 
	{
		xPixels = new ArrayList<Double>();
		for(int x = 0; x < width; x++) {
			xPixels.add(lx + (((ux - lx)/(width * 1.0))*x));
		}
	}
	
	/**
	 * Graphs the equation
	 * 
	 * @param ly The lower y bound
	 * @param uy The upper y bound
	 */
	public void doEquation(int ly, int uy) 
	{
		int closestToZeroIndex = -1;
		double closestVal = Double.POSITIVE_INFINITY;
		
		//Erase any other markings on the chart
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++)
				pixels[x][y] = 0;
			
			if(Math.abs(xPixels.get(x)) < closestVal) {
				closestVal = Math.abs(xPixels.get(x));
				closestToZeroIndex = x;
			}
		}
		
		//If there is no zero in the x range, grpahing will not work properly
		//Therefore, we set the lowest value in the x range to zero
		xPixels.set(closestToZeroIndex, 0.0);
		
		//Draw Y Axis
		if(!xPixels.get(0).equals(0.0))
			for(int a = 0; a < height; a ++)
				pixels[closestToZeroIndex][a] = 1;
		
		
		//For every x value, calculate a y value
		for(int x = 0; x < width; x++) {
			double myVal = xPixels.get(x);
			double yVal = runEquation(eq, myVal);
			
			//Make sure y is graphable and then graph it
			if(yVal >= ly && yVal <= uy) {
				int myPixelVal = valueToPixel(yVal, ly, uy);
				if(myPixelVal >=0 && myPixelVal < height) {
					//pixels[x][myPixelVal] = 1;
					Point thisPoint = new Point(x, myPixelVal);
					equationPoints.add(thisPoint);
				}
			}
		}
		
		//Draw X Axis
		if(ly < 0 && uy > 0)
			for(int x = 0; x<width; x++)
				pixels[x][valueToPixel(0, ly, uy)] = 1;
	}
	
	/**
	 * Takes an input double and returns the pixel number that corresponds
	 * to the pixel that should display that value
	 * 
	 * @param value The value to convert
	 * @param ly The lower y bound
	 * @param uy The upper y bound
	 * @return The pixel that corresponds to 'value'
	 */
	public int valueToPixel(double value, int ly, int uy) 
	{
		return height - (int)((height * (value - 1.0 * ly))/(1.0 *uy - 1.0 * ly));
	}
	
	/**
	 * Parses the equation for one particular value of x
	 *  
	 * @param xVal The value to determine a y value for
	 * @return y the value that corresponds to f(x)
	 */
	public double runEquation(ArrayList<Object> equation, double xVal) 
	{
		easyPrint(equation);
		ArrayList<Object> temp = new ArrayList<Object>();
		
		//Replace x with its value
		for(Object a: equation) {
			if(isCharacter(a)) {
				if((char)a == 'x')
					temp.add(xVal);
				else
					temp.add(a);
			}
			else if(isInteger(a)) {
				Double intToDouble = ((Integer) a) * 1.0;
				temp.add(intToDouble);
			}
			else {
				temp.add(a);
			}
		}
		
		//Allow for e and pi 
		while(temp.contains('e')) {
			int ind = temp.indexOf('e');
			temp.set(ind, Math.E);
		}
		while(temp.contains('p')) {
			int ind = temp.indexOf('p');
			temp.set(ind, Math.PI);
		}
		
		
		//Deal with parantheses sections
		while(temp.contains('(')) {
			int ind = temp.indexOf('(');
			ArrayList<Object> temp2 = new ArrayList<Object>();
			int lastInd = temp.lastIndexOf(')');
			for(int i = ind+1; i < lastInd; i++){
				temp2.add(temp.get(i));
			}
			
			//Reursively solve inside the parantheses
			temp.set(ind, runEquation(temp2, xVal));
			for(int i = lastInd; i > ind; i--) {
				temp.remove(i);
			}
		}
		
		
		//Calculate ^
		while(temp.contains('^')) {
			int ind = temp.indexOf('^');
			Double a = (Double) temp.get(ind-1);
			Double b = (Double) temp.get(ind+1);
			temp.set(ind, Math.pow(a,b));
			
			//remove the latter number first
			//in order to preserve index order for first
			temp.remove(ind+1);
			temp.remove(ind-1);
		}

		easyPrint("after exp",temp);
		//Calculate ln(x)
		while(temp.contains('l')) {
			int ind = temp.indexOf('l');
			Double a = (Double) temp.get(ind+1);
			temp.set(ind, Math.log(a));
			temp.remove(ind+1);
		}
		
		//Calculate *
		while(temp.contains('*')) {
			int ind = temp.indexOf('*');
			Double a = (Double) temp.get(ind-1);
			Double b = (Double) temp.get(ind+1);
			temp.set(ind, a * b);
			
			//remove the latter number first
			//in order to preserve index order for first
			temp.remove(ind+1);
			temp.remove(ind-1);
		}

		easyPrint("after mult",temp);
		//Calculate *
		while(temp.contains('/')) {
			int ind = temp.indexOf('/');
			Double a = (Double) temp.get(ind-1);
			Double b = (Double) temp.get(ind+1);
			temp.set(ind, a / b);
			
			//remove the latter number first
			//in order to preserve index order for first
			temp.remove(ind+1);
			temp.remove(ind-1);
		}
		
		//Calculate +
		while(temp.contains('+')){
			
			int ind = temp.indexOf('+');
			if(ind!=0){
				Double a = (Double) temp.get(ind-1);
				Double b = (Double) temp.get(ind+1);
				temp.set(ind, a + b);
				
				//remove the latter number first
				//in order to preserve index order for first
				temp.remove(ind+1);
				temp.remove(ind-1);
			}
			else {
				temp.remove(ind);
			}
		}
		easyPrint("after add",temp);
		
		//Calculate -
		while(temp.contains('-'))
		{
			int ind = temp.indexOf('-');
			if(ind != 0) {
				Double a = (Double) temp.get(ind-1);
				Double b = (Double) temp.get(ind+1);
				temp.set(ind, a - b);
				
				//remove the latter number first
				//in order to preserve index order for first
				temp.remove(ind+1);
				temp.remove(ind-1);
			}
			else {
				Double a = (Double) temp.get(ind+1);
				temp.set(ind, a * -1.0);
				temp.remove(ind+1);
			}
		}
		easyPrint("after subtract",temp);
		
		return (double) temp.get(0);
	}
	
//	/**
//	 * A method to make debugging easier.
//	 * 
//	 * @param args The things you want to print
//	 */
	private void easyPrint(Object ... args)
	{
		String retVal = "";
		for(Object a: args)
		{
			retVal += a.toString() + " ";
		}
		System.out.println(retVal);
	}
	
	/**
	 * Determines whether an Object is an integer
	 * 
	 * @param o The object to check
	 * @return
	 */
	private boolean isInteger(Object o)
	{
		return o.getClass().toString().equalsIgnoreCase("class java.lang.Integer");
	}
	
	/**
	 * Determines whether an Object is an integer
	 * 
	 * @param o The object to check
	 * @return
	 */
	private boolean isCharacter(Object o)
	{
		return o.getClass().toString().equalsIgnoreCase("class java.lang.Character");
	}
}
