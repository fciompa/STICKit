package cz.ictsystem.stickers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import cz.ictsystem.stickers.data.Sticker;

/**
 * Visualization builder produce visualization bitmap. Visualization bitmap is 
 * combined from background bitmap, sticker bitmap and properties how to sticker 
 * insert into visualization.  
 * 
 * @author frantisek.ciompa
 *
 */
public class VisualizationBuilder {
	
	/**
	 * Number of steps used for controlling by button 
	 */
	private final static int STEPS_NUMBER = 100; 
	
	/**
	 * Resolution (X) of build visualization bitmap 
	 */
	private int mWidth; 
	
	/**
	 * Resolution (Y) of build visualization bitmap 
	 */
	private int mHeight;

	/**
	 * Sticker contained in the visualization 
	 */
	private Sticker mSticker;
	
	/**
	 * Visualization data  
	 */
	private Visualization mVisualization;
	
	public VisualizationBuilder(Point size){
		mWidth = size.x;
		mHeight = size.y;
		mSticker = new Sticker();
		mVisualization = new Visualization();
	}

	public VisualizationBuilder(Context context, Point size, Visualization visualization){
		mWidth = size.x;
		mHeight = size.y;
		mVisualization = visualization;
		mSticker = new Sticker(context, mVisualization.getStickerId());
	}
	
	public VisualizationBuilder setVisualization(Context context, Cursor cursor){
		mVisualization.set(context, cursor);
		mSticker = new Sticker(context, mVisualization.getStickerId());
		return this;
	}
	
	public VisualizationBuilder setSticker(Sticker sticker){
		mVisualization.setStickerId(sticker.getId());
		mSticker = sticker;
		if(!sticker.getEditableColor()){
			//Sticker doesn't have possibility for color changing, so it is set to default color 
			mVisualization.setColor();
		}
		return this;
	}
	
	public Sticker getSticker(){
		return mSticker;
	}
	
	public VisualizationBuilder setStickerPosition(Visualization visualization){
		mVisualization = visualization;
		return this;
	}
	
	public Visualization getVisualization(){
		return mVisualization;
	}
	
	public VisualizationBuilder sizeIncrease(){
		changeSize(1);
		return this;
	}

	public VisualizationBuilder sizeDecrease(){
		changeSize(-1);
		return this;
	}
	
	private void changeSize(int index) {
		int newSize = mVisualization.getSize() + index * 100 / STEPS_NUMBER;
		if(newSize >= 10 && newSize <= 200){
			mVisualization.setSize(newSize);
		}
	}

	public VisualizationBuilder moveUp(){
		changeY(-1);
		return this;
	}

	public VisualizationBuilder moveDown(){
		changeY(1);
		return this;
	}

	private void changeY(int index){
		int yStemp = 100/STEPS_NUMBER;
		int newY = mVisualization.getY() + index * yStemp;
		if((index == 1 && newY <= 100) ||
				(index == -1 && newY >= -100)){
			mVisualization.setY(newY);
		}
	}
	
	public VisualizationBuilder moveRight(){
		changeX(1);
		return this;
	}
	
	public VisualizationBuilder moveLeft(){
		changeX(-1);
		return this;
	}

	private void changeX(int index){
		int xStemp = 100/STEPS_NUMBER;
		int newX = mVisualization.getX() + index * xStemp;
		if((index == 1 && newX  <= 100) ||
				(index == -1 && newX >= -100)){
			mVisualization.setX(newX);
		}
	}
	
	public VisualizationBuilder perspectiveIncrease(){
		changePerspective(1);
		return this;
	}
	
	public VisualizationBuilder perspectiveDecrease(){
		changePerspective(-1);
		return this;
	}

	private void changePerspective(int index){
		int newPerspective = mVisualization.getPerspective() - index * 100 / STEPS_NUMBER;
		if(newPerspective >= -80 && newPerspective <= 80){
			mVisualization.setPerspective(newPerspective);
		}
	}
	
	public VisualizationBuilder flipSticker(){
		mVisualization.setFliped(!mVisualization.getFliped());
		return this;
	}
	
	public Bitmap get(){
		Bitmap visualization = null;
		if (mVisualization.getBackground() != null){
			Bitmap backroundBitmap = Utils.getBitmapFromBlob(mVisualization.getBackground(), mWidth, mHeight);
			Bitmap stickerBitmap = Utils.getBitmapFromBlob(mSticker.getImage(), mWidth, mHeight);
			visualization = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
			
			Canvas canvas = new Canvas(visualization);
			canvas.drawBitmap(getBackgroundModifiedForVisualization(backroundBitmap), 0, 0, null);

			if (mSticker.getImage() != null){
				float stickerX = mWidth/2 + (mWidth * (mVisualization.getX()/100f))/2;
				float stickerY = mHeight/2 + (mHeight * (mVisualization.getY()/100f))/2;
				Bitmap modifiedSticker = getStickerModifiedForVisualization(stickerBitmap);
				float stickerXLeftUp = stickerX - modifiedSticker.getWidth()/2;
				float stickerYLeftUp = stickerY - modifiedSticker.getHeight()/2;
				canvas.drawBitmap(modifiedSticker, stickerXLeftUp, stickerYLeftUp, getStickerPaint());
			}
		}
		
		return visualization;
	}

	private Bitmap getBackgroundModifiedForVisualization(Bitmap backgroundBitmap) {
		float[] src = new float[] { //x y
				0, 0, //left top
				backgroundBitmap.getWidth(), 0, //right top 
				backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), //right bottom 
				0, backgroundBitmap.getHeight() // left bottom
				};
		
		float ratioWidth = Float.valueOf(mWidth)/Float.valueOf(backgroundBitmap.getWidth());
		float ratioHeight = Float.valueOf(mHeight)/Float.valueOf(backgroundBitmap.getHeight());
		
		float corection = ratioWidth > ratioHeight ? ratioWidth : ratioHeight;
		
		float newWidth = backgroundBitmap.getWidth() * corection;
		float newHeight = backgroundBitmap.getHeight() * corection;
		
		float[] dst = new float[] {
				0, 0, 
				newWidth, 0,
				newWidth, newHeight,
				0, newHeight 
				};
		
		Matrix matrix = new Matrix();
		matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
		
		return Bitmap.createBitmap(backgroundBitmap, 0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), matrix, true); 
	}
	private Bitmap getStickerModifiedForVisualization(Bitmap stickerBitmap) {
		Bitmap stickerBitmapFliped = null;

		if (mVisualization.getFliped()){
			stickerBitmapFliped = makeFlip(stickerBitmap);
		}
		
		stickerBitmapFliped = stickerBitmapFliped == null ? stickerBitmap : stickerBitmapFliped;
		Bitmap stickerBitmapFlipedPerspective = makePerspective(stickerBitmapFliped); 
		return stickerBitmapFlipedPerspective;
	}
	
	private Bitmap makeFlip(Bitmap stickerOriginalBitmap){
		float[] src = new float[] { //x y
				0, 0, //left top
				stickerOriginalBitmap.getWidth(), 0, //right top 
				stickerOriginalBitmap.getWidth(), stickerOriginalBitmap.getHeight(), //right bottom 
				0, stickerOriginalBitmap.getHeight() // left bottom
				};
		
		float[] dst = new float[] {
				stickerOriginalBitmap.getWidth(), 0, 
				0, 0,
				0, stickerOriginalBitmap.getHeight(),
				stickerOriginalBitmap.getWidth(), stickerOriginalBitmap.getHeight() 
				};
		
		Matrix matrix = new Matrix();
		matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
		
		return Bitmap.createBitmap(stickerOriginalBitmap, 0, 0, stickerOriginalBitmap.getWidth(), stickerOriginalBitmap.getHeight(), matrix, true); //Flip is for full size of sticker 
	}

	private Bitmap makePerspective(Bitmap originalStickerBitmap) {
		float[] src = new float[] { //x y
				0, 0, //left top
				originalStickerBitmap.getWidth(), 0, //right top 
				originalStickerBitmap.getWidth(), originalStickerBitmap.getHeight(), //right bottom 
				0, originalStickerBitmap.getHeight() // left bottom
				};

		
		float newWidth = originalStickerBitmap.getWidth() * mVisualization.getSize()/100;
		float newHeigh = originalStickerBitmap.getHeight() * mVisualization.getSize()/100;
		
		float perspectiveCorectionWidth = ((newWidth/100) * mVisualization.getPerspective())/2;
		float perspectiveCorectionHeight = ((newHeigh/100) * mVisualization.getPerspective())/6;
		
		float[] dst;
		if(perspectiveCorectionHeight>= 0){
			dst = new float[] { 
					0, 0, 
					newWidth - perspectiveCorectionWidth, perspectiveCorectionHeight, 
					newWidth - perspectiveCorectionWidth, newHeigh - perspectiveCorectionHeight, 
					0, newHeigh 
					};
		} else {
			perspectiveCorectionHeight = perspectiveCorectionHeight * -1;
			perspectiveCorectionWidth = perspectiveCorectionWidth * -1;
			dst = new float[] { 
					0, perspectiveCorectionHeight, 
					newWidth - perspectiveCorectionWidth, 0, 
					newWidth - perspectiveCorectionWidth, newHeigh, 
					0, newHeigh  - perspectiveCorectionHeight
					};
			
		}
		
		Matrix matrix = new Matrix();
		matrix.setPolyToPoly(src, 0, dst, 0, src.length >> 1);
		
		return Bitmap.createBitmap(originalStickerBitmap, 0, 0, originalStickerBitmap.getWidth(), originalStickerBitmap.getHeight(), matrix, true);
	}

	private Paint getStickerPaint() {
		Paint paint = null;
		if(mVisualization.getColor() != 0){
			paint = new Paint();
			paint.setColorFilter(new LightingColorFilter(Color.BLACK, mVisualization.getColor()));
		}
		return paint;
	}
	
}
