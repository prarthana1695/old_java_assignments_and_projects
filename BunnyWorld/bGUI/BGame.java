package bGUI;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

import script.Script;


import data.BDataModel;
import data.BPage;
import data.BPageModel;
import data.BShape;
/**
 * 
 * @author Chidozie Nwobilor
 *
 */
public class BGame extends JFrame{
	private static final String FIRST_PAGE = "page1";
	private BGameCanvas canvas;
	private String gametitle;
	private BDataModel data;
	private BEditor editor;
	
	/**
	 * constructs BGame using an input title and datamodel
	 * @param gametitle
	 * @param model
	 */
	public BGame(String gametitle, BDataModel model, BEditor editor){
		super(gametitle);
		setGametitle(gametitle);
		setData(model);
		setCanvas(new BGameCanvas(model));
		canvas.flipTo(new BPage(data.getPage(FIRST_PAGE)));
		this.add(canvas);
		this.editor = editor;
		pack();
	
	    setVisible(true);
	    addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent event) {
    			handleClosing();
    		}
        });
	}
	protected void handleClosing() {
		if(editor != null) this.editor.setVisible(true);
		else setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * @param gametitle the gametitle to set
	 */
	public void setGametitle(String gametitle) {
		this.gametitle = gametitle;
	}
	/**
	 * @return the gametitle
	 */
	public String getGametitle() {
		return gametitle;
	}
	/**
	 * @param canvas the canvas to set
	 */
	public void setCanvas(BGameCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * @return the canvas
	 */
	public BGameCanvas getCanvas() {
		return canvas;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(BDataModel data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public BDataModel getData() {
		return data;
	}
	private static BDataModel setUpDataModel(){
		BDataModel data = new BDataModel();
		data.addResource(new File("resources/bunny.jpeg"));
		data.addResource(new File("resources/horray.au"));
		
		BShape shape = new BShape();
		shape.setName("shape1");
		shape.setImageName("bunny.jpeg");
		data.addShape(shape.getModel());
		
		BShape shape3 = new BShape();
		shape3.setName("shape3");
		shape3.setColor(Color.pink);
		shape3.setHidden(true);
		shape3.setMovable(false);
		shape3.setBounds(new Rectangle(200, 200, BShape.DEFAULT_WIDTH, BShape.DEFAULT_HEIGHT));
		shape3.addScript("on click goto page3");
		data.addShape(shape3.getModel());
		
		BShape shape4 = new BShape();
		shape4.setName("shape4");
		shape4.setColor(Color.BLUE);
		data.addShape(shape4.getModel());
		
		BShape shape2 = new BShape();
		shape2.setName("shape2");
		shape2.setColor(Color.red);
		shape2.setBounds(new Rectangle(10, 10, BShape.DEFAULT_WIDTH, BShape.DEFAULT_HEIGHT));
		shape2.setMovable(false);
		shape2.addScript("on drop shape1 goto page2 hide shape1 on drop shape4 show shape3 hide shape4 on click beep play sam");
		data.addShape(shape2.getModel());
		
		
		BPageModel page = new BPageModel();
		page.setName(BGame.FIRST_PAGE);
		page.addShape(shape3);
		page.addShape(shape2);
		page.addShape(shape);
		
		
		
		BShape shape5 = new BShape();
		shape5.setName("shape5");
		shape5.setMovable(false);
		shape5.setColor(Color.ORANGE);
		shape5.addScript("on click goto page1");
		shape5.setBounds(new Rectangle(100, 100, 50, 50));
		data.addShape(shape5.getModel());
		
		BPageModel page2 = new BPageModel();
		page2.setName("page2");
		page2.addShape(shape4);
		page2.addShape(shape5);
		
		BShape shape6 = new BShape();
		shape6.setName("shape6");
		shape6.setText("WIN!");
		shape6.setMovable(false);
		shape6.addScript("on enter play horray.au");
		data.addShape(shape6.getModel());
		
		BPageModel page3 = new BPageModel();
		page3.setName("page3");
		page3.addShape(shape6);
		
		data.addPage(page);
		data.addPage(page2);
		data.addPage(page3);
		data.script = new Script(data);
		return data;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BDataModel data = setUpDataModel();
		BGame game = new BGame("Test", data, null);
	}
}
