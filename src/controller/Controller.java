package controller;
import java.awt.EventQueue;
import java.awt.Graphics;

import model.IM2VAdapter;
import model.Model;
import view.IV2MAdapter;
import view.View;

/**
 * This is the Controller of the system.
 * It is responsible for maintaining the 
 * interactions between the model and view
 * 
 * @author JD Porterfield
 *
 */
public class Controller{

	/**
	 * The model of the system
	 */
	private Model model = new Model(IM2VAdapter.NULL_OBJECT);
	/**
	 * The view of the system
	 */
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

	/**
	 * Start the model and view
	 */
	protected void start() {
		model.start();
		view.start();
	}

	/**
	 * Constructor that creates the model and view
	 * and sets their adapter objects
	 * 
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
					String equation) {
				model.calculate(lx, ux, ly, uy, equation);
				
			}


			@Override
			public void calculateAxes(int lx, int ux, int ly, int uy, int windowX, int windowY) {
				model.calculateAxes(lx, ux, ly, uy, windowX,windowY);
			}
			
		});
	}

}
