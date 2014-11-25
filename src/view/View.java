package view;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Window.Type;

public class View extends JFrame{

	private static final long serialVersionUID = -6902165796914041718L;
	
	private IV2MAdapter myAdapter = IV2MAdapter.NULL_OBJECT;
	private JPanel contentPane;
	private JTextField txtEquation;
	private JTextField lowerXBound;
	private JTextField upperXBound;
	private JTextField lowerYBound;
	private JTextField upperYBound;
	private JPanel pnlDisplay = new JPanel() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -708326350965762632L;

		/**
		 * Overridden paintComponent method to paint a shape in the panel.
		 * 
		 * @param g, The Graphics object to paint on.
		 **/
		public void paintComponent(Graphics g) {
			super.paintComponent(g); // Do everything normally done first,
			// e.g. clear the screen.
			myAdapter.updateGraph(g);
		}
	};;
	private JButton btnGraph;
	
	public View(IV2MAdapter adapter)
	{
		setType(Type.UTILITY);
		setTitle("Equation Grapher");
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				determineAxes();
			}
		});
		initGUI();
		myAdapter = adapter;
	}
	
	public void initGUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel pnlControl = new JPanel();
		contentPane.add(pnlControl, BorderLayout.NORTH);
		
		JLabel lblEnterEquationY = new JLabel("Enter Equation:  y =");
		pnlControl.add(lblEnterEquationY);
		
		txtEquation = new JTextField();
		txtEquation.setToolTipText("Enter your equation here. Tap 'Help' for formatting tips!");
		txtEquation.setText("x^2 + x + 1");
		pnlControl.add(txtEquation);
		txtEquation.setColumns(10);
		
		btnGraph = new JButton("Graph");
		btnGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chart();
			}

		});
		btnGraph.setToolTipText("Click this button when you want to graph the equation!");
		pnlControl.add(btnGraph);
		
		JPanel pnlBounds = new JPanel();
		contentPane.add(pnlBounds, BorderLayout.SOUTH);
		
		JLabel lblXBounds = new JLabel("x bounds:");
		pnlBounds.add(lblXBounds);
		
		lowerXBound = new JTextField();
		lowerXBound.setToolTipText("Enter the lower x bound here.");
		lowerXBound.setText("-10");
		pnlBounds.add(lowerXBound);
		lowerXBound.setColumns(5);
		
		upperXBound = new JTextField();
		upperXBound.setToolTipText("Enter the upper x bound here.");
		upperXBound.setText("10");
		pnlBounds.add(upperXBound);
		upperXBound.setColumns(5);
		
		JLabel lblYBounds = new JLabel("y bounds:");
		pnlBounds.add(lblYBounds);
		
		lowerYBound = new JTextField();
		lowerYBound.setToolTipText("Enter the lower y bound here.");
		lowerYBound.setText("-10");
		pnlBounds.add(lowerYBound);
		lowerYBound.setColumns(5);
		
		upperYBound = new JTextField();
		upperYBound.setToolTipText("Enter the upper y bound here.");
		upperYBound.setText("10");
		pnlBounds.add(upperYBound);
		upperYBound.setColumns(5);
		
		pnlDisplay.setBackground(Color.WHITE);
		contentPane.add(pnlDisplay, BorderLayout.CENTER);
	}

	protected void determineAxes() {
		int lx = Integer.parseInt(lowerXBound.getText());
		int ux = Integer.parseInt(upperXBound.getText());
		int ly = Integer.parseInt(lowerYBound.getText());
		int uy = Integer.parseInt(upperYBound.getText());
		String equation = txtEquation.getText();
		
		int windowX = pnlDisplay.getWidth();
		int windowY = pnlDisplay.getHeight();
		
		myAdapter.calculateAxes(lx,ux,ly,uy,equation, windowX, windowY);		
	}
	
	protected void chart() {
		int lx = Integer.parseInt(lowerXBound.getText());
		int ux = Integer.parseInt(upperXBound.getText());
		int ly = Integer.parseInt(lowerYBound.getText());
		int uy = Integer.parseInt(upperYBound.getText());
		String equation = txtEquation.getText();
		
		int windowX = pnlDisplay.getWidth();
		int windowY = pnlDisplay.getHeight();
		
		myAdapter.calculate(lx,ux,ly,uy,equation, windowX, windowY);		
	}

	public void start() {
		setVisible(true);
	}
	
	public void update()
	{
		pnlDisplay.repaint();
	}
	
	/**
	 * Useful for debugging multiple variables at a time
	 * This is better than having to call println and manually add the
	 * variables with spacing between them
	 * 
	 * @param args The variables to be printed
	 */
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