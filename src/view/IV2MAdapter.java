package view;

import java.awt.Graphics;

/**
 * The interface of the adapter from the view to the model that enables the view to talk to the model.
 */
public interface IV2MAdapter {

	/*
	 * Calls the update method for the model
	 * 
	 * @param g, Graphics object 
	 */
	public void updateGraph(Graphics g);

	/**
	 * No-op singleton implementation of IV2MAdapter 
	 */
	public static final IV2MAdapter NULL_OBJECT = new IV2MAdapter () {	  
	    
		public void updateGraph(Graphics g) {
	    }
	  

		@Override
		public void calculate(int lx, int ux, int ly, int uy, String equation, int px, int py) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void calculateAxes(int lx, int ux, int ly, int uy, String equation, int px, int py) {
			// TODO Auto-generated method stub
			
		}
	};


	public void calculate(int lx, int ux, int ly, int uy, String equation, int px, int py);


	public void calculateAxes(int lx, int ux, int ly, int uy, String equation,
			int windowX, int windowY);

}