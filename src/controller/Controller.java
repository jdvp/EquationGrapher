package controller;
import java.awt.EventQueue;
import java.awt.Graphics;

import model.IM2VAdapter;
import model.Model;
import view.IV2MAdapter;
import view.View;


public class Controller{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3922672287928066483L;
	/**
	 * 
	 */
	private Model model = new Model(IM2VAdapter.NULL_OBJECT);
	private View view = new View(IV2MAdapter.NULL_OBJECT);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Controller controller = new Controller();
					controller.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void start() {
		model.start();
		view.start();
	}

	/**
	 * Create the frame.
	 */
	public Controller() {
		model = new Model(new IM2VAdapter(){

			@Override
			public void update() {
				view.update();
			}
			
		});
		view = new View(new IV2MAdapter(){

			@Override
			public void updateGraph(Graphics g) {
				model.updateGraph(g);
			}


			@Override
			public void calculate(int lx, int ux, int ly, int uy,
					String equation, int px, int py) {
				model.calculate(lx, ux, ly, uy, equation);
				
			}


			@Override
			public void calculateAxes(int lx, int ux, int ly, int uy,
					String equation, int windowX, int windowY) {
				model.calculateAxes(lx, ux, ly, uy, windowX,windowY);
			}
			
		});
	}

}
