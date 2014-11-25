package model;

/**
 * Interface that goes from the model to the view that enables the model to talk to the view
 */
public interface IM2VAdapter {
	
	/**
	 * The method that tells the view to update
	 */
	public void update();
	
	/**
	 * No-op "null" adapter
	 */
	public static final IM2VAdapter NULL_OBJECT = new IM2VAdapter() {
		public void update() {
		}
	};
}