package data;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import script.Script;

import bGUI.BEditor;
import bGUI.BGameCanvas;
/**
 * 
 * @author Chidozie Nwobilor
 *
 */
public class BShape implements BModelListener{
	public static final int KNOB_SIZE = 10;
	public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, (int) 1.0);
	public static final Point DEFAULT_START = new Point(0,0);
	public static final int DEFAULT_HEIGHT = 100;
	public static final int DEFAULT_WIDTH = 100;
	
	

	
	
	private List<Point> knobs;
	private BShapeModel model;
	
	public BShape(){
		setModel(new BShapeModel());
		this.getModel().addListener(this);
		setName("");
		setText("");
		setImageName("none");
		setHidden(false);
		setMovable(true);
		setBounds(new Rectangle(DEFAULT_START.x, DEFAULT_START.y, DEFAULT_WIDTH, DEFAULT_HEIGHT));
		knobs = new ArrayList<Point>();
		getKnobs();
		setFont(BShape.DEFAULT_FONT);
	}
	/**
	 * standard constructor except that the shape does not get added
	 * as a listener to the model
	 * @param model
	 * @param r
	 */
	public BShape(BShapeModel model, Rectangle r) {
		setModel(model);
		setName("");
		setText("");
		setImageName("");
		setHidden(false);
		setMovable(true);
		setBounds(r);
		knobs = new ArrayList<Point>();
		getKnobs();
		setFont(this.model.getFont());
	}
	/**
	 * sets the model for the shape class.  MUST be called first before any changes are made
	 * @param shapeModel
	 */
	public void setModel(BShapeModel shapeModel) {
		model = shapeModel;
	}
	public BShapeModel getModel(){
		return model;
	}
	public void draw(Graphics g){
		Shape clip = g.getClip();
		Rectangle r = model.getBounds();
		g.setClip(r);
		if(model.getText().equals("")) DrawImage(g, r);
		else DrawText(g, r);
		g.setClip(clip);
	}
	private Font computeFont(Font f, Graphics g, Rectangle bounds){
		double size = 1.0;
		Font curr = f.deriveFont(f.getStyle(), (int)size);
		Font prev = curr;
		while(true){
			FontMetrics fm = g.getFontMetrics(curr);
			if(fm.getHeight() >  bounds.height){
				fm = g.getFontMetrics(prev);
				bounds.height -= fm.getDescent();
				return prev;
			}
			size = (size * 1.10) + 1;
			prev = curr;
			curr = f.deriveFont(f.getStyle(), (int)size);
		}
	}
	private void DrawText(Graphics g, Rectangle r) {
		Font f = computeFont(model.getFont(), g, r);
		g.setColor(getColor());
		g.setFont(f);
		g.drawString(model.getText(), r.x, r.y + r.height);
	}
	private void DrawImage(Graphics g, Rectangle r) {
		Image image = model.getData().getImage(getImageName());
		if(getImageName().equals("") || image == null){
			g.setColor(model.getColor());
			g.fillRect(r.x, r.y, r.width, r.height);
		}else{
			g.drawImage(image, r.x, r.y, r.width, r.height, null);
		}
	}
	public void move(Point delta){
		if(getMovable()) model.move(delta);
	}
	public void move(int x, int y){
		this.move(new Point(x, y));
	}
	public void resize(Point anchor, Point newPoint){
		if(getMovable()) model.adjust(anchor, newPoint);
	}
	/**
	 * returns the corners of the bounds of the object
	 * @return list of Points
	 */
	public List<Point> getKnobs(){
		knobs.clear();
		Rectangle r = model.getBounds();
		knobs.add(new Point(r.x, r.y));
		knobs.add(new Point(r.x + r.width, r.y));
		knobs.add(new Point(r.x, r.y + r.height));
		knobs.add(new Point(r.x + r.width, r.y + r.height));
		return knobs;
	}
	/**
	 * currently returns the anchor point for the given point. Does not determine if point is in knob
	 * or not
	 * @param pt
	 * @return anchor point
	 */
	public Point getAnchorForPoint(Point pt){
		Point result = null;
		getKnobs();
		if(InKnobRegion(pt, knobs.get(0))) result = knobs.get(3);
		else if(InKnobRegion(pt, knobs.get(1))) result = knobs.get(2);
		else if(InKnobRegion(pt, knobs.get(2))) result = knobs.get(1);
		else if(InKnobRegion(pt, knobs.get(3))) result = knobs.get(0);
		return result;
	}
	private boolean InKnobRegion(Point pt, Point center) {
		Rectangle r = new Rectangle(center.x - BShape.KNOB_SIZE / 2, center.y - BShape.KNOB_SIZE / 2, BShape.KNOB_SIZE, BShape.KNOB_SIZE);
		return r.contains(pt);
	}
	public Rectangle getBigBounds(){
		Rectangle r = model.getBounds();
		r.height += KNOB_SIZE;
		r.width += KNOB_SIZE;
		r.x -= KNOB_SIZE / 2;
		r.y -= KNOB_SIZE / 2;
		return r;
	}
	public Rectangle getBounds(){
		return model.getBounds();
	}
	public void setBounds(Rectangle newBounds){
		model.setBounds(newBounds);
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		model.setName(name);
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return model.getName();
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		model.setText(text);
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return model.getText();
	}
	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		model.setImageName(imageName);
	}
	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return model.getImageName();
	}
	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		model.setHidden(hidden);
	}
	/**
	 * @return the hidden
	 */
	public boolean getHidden() {
		return model.getHidden();
	}
	/**
	 * sets whether or not the shape is movable
	 * @param movable the movable to set
	 */
	public void setMovable(boolean movable) {
		model.setMovable(movable);
	}
	/**
	 * @return the movable
	 */
	public boolean getMovable() {
		return model.getMovable();
	}
	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		model.setFont(font);
	}
	/**
	 * @return the font
	 */
	public Font getFont() {
		return model.getFont();
	}
	/**
	 * sets the bold on the font on and off
	 * @param makeBold
	 */
	public void setFontBold(boolean makeBold){
		Font font = model.getFont();
		if(makeBold){
			if(font.isItalic()){
				setFont(font.deriveFont(Font.BOLD + Font.ITALIC));
			}else{
				setFont(font.deriveFont(Font.BOLD));
			}
		}else{
			if(font.isItalic()){
				setFont(font.deriveFont(Font.ITALIC));
			}else{
				setFont(font.deriveFont(Font.PLAIN));
			}
		}
	}
	/**
	 * sets the italic on the font on and off
	 * @param makeItalic
	 */
	public void setFontItalics(boolean makeItalic){
		Font font = model.getFont();
		if(makeItalic){
			if(font.isBold()){
				setFont(font.deriveFont(Font.BOLD + Font.ITALIC));
			}else{
				setFont(font.deriveFont(Font.ITALIC));
			}
		}else{
			if(font.isBold()){
				setFont(font.deriveFont(Font.BOLD));
			}else{
				setFont(font.deriveFont(Font.PLAIN));
			}
		}
	}
	/**
	 * returns a map of scripts
	 * @return model scripts
	 */
	public Map<String, String> getScript(){
		return model.getScripts();
	}
	/**
	 * adds script to the model. cannot add scripts with same triggers to the map.  last one added
	 * will count
	 * @param script
	 */
	public void addScript(String script){
		model.addScript(script);
	}
	/**
	 * creates a scaled copy of the shape so that the shape retains it's original bounds and draws
	 * that instead.
	 * @param shape
	 */
	public static BShape drawScaled(BShape shape, Graphics g, Rectangle r){
		Rectangle scaledBounds = GetScaledBounds(r, shape.getBounds());
		BShape temp = new BShape(shape.getModel().clone(), scaledBounds);
		temp.draw(g);
		return temp;
	}
	public static BShape getScaled(BShape shape, Rectangle r){
		Rectangle scaledBounds = GetScaledBounds(r, shape.getBounds());
		BShape temp = new BShape(shape.getModel().clone(), scaledBounds);
		temp.setColor(shape.getColor());
		temp.setFont(shape.getFont());
		temp.setHidden(shape.getHidden());
		temp.setImageName(shape.getImageName());
		temp.setName(shape.getName());
		temp.setText(shape.getText());
		return temp;
	}
	public static Rectangle GetScaledBounds(BShape shape, Rectangle r){
		return GetScaledBounds(r, shape.getBounds());
	}
	/*
	 * (non-javadoc)
	 * returns a scaled version of the original bounds
	 */
	private static Rectangle GetScaledBounds(Rectangle r, Rectangle origBounds) {
		double scale = (double)r.height/ origBounds.height;
		return new Rectangle(r.x, r.y, ((int) (origBounds.width * scale)), r.height);
	}
	public void modelChanged(BShapeModel model) {
		// TODO Auto-generated method stub
	}
	public void setFont(String fontname){
		Font font = new Font(fontname, Font.PLAIN, (int) 1.0);
		model.setFont(font);
	}
	public void setColor(Color color){
		model.setColor(color);
	}
	public Color getColor(){
		return model.getColor();
	}
	public void doScript(String s, BGameCanvas canvas) {
		String script = this.getScript().get(s);
		if(script != null) Script.execute(script, canvas);
	}
	/**
	 * determines whether on not the selected shape can be dropped on the Shape
	 * @param selected
	 * @return
	 */
	public boolean canRecieve(BShape selected) {
		return model.canRecieve(selected);
	}
	public void removeListener(BModelListener l) {
		model.removeListener(l);
	}
	public void addListener(BModelListener l) {
		model.addListener(l);
	}
	
	public String toString() {
	    return model.toString();
	}
	public String getScriptBlock(){
		return model.getScriptBlock();
	}
	public BShape clone(){
		BShape clone = new BShape();
		clone.setBounds((Rectangle) this.getBounds().clone());
		clone.setColor(this.getColor());
		clone.setFont(this.getFont());
		clone.setHidden(getHidden());
		clone.setMovable(this.getMovable());
		clone.setImageName(this.getImageName());
		clone.setModel(this.model.clone());
		clone.setText(getText());
		clone.setName(getName());
		return clone;
	}
	public boolean listenerContains(BModelListener l) {
		return model.containsListener(l);
	}
}
