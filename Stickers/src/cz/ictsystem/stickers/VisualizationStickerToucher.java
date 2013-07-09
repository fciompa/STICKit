package cz.ictsystem.stickers;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.widget.ImageView;

class VisualizationStickerToucher{
	private enum Direction{
		MOVE_UP, 
		MOVE_DOWN, 
		MOVE_RIGHT, 
		MOVE_LEFT, 
		SIZE_INCREASE, 
		SIZE_DECREASE, 
		PERSPECTIVE_INCREASE, 
		PERSPECTIVE_DECREASE};	
	
	private boolean mChange;
	private Thread mChangeThread;
	private ImageView mImageView;
	private VisualizationBuilder mBuilder;
	
	public VisualizationStickerToucher(ImageView imageView, VisualizationBuilder builder) {
		mChange = false;
		mChangeThread = null;
		mImageView = imageView;
		mBuilder = builder;
	}
	
	public void moveUp(MotionEvent event){
		change(event, Direction.MOVE_UP);
	}
	
	public void moveDown(MotionEvent event){
		change(event, Direction.MOVE_DOWN);
	}
	
	public void moveRight(MotionEvent event){
		change(event, Direction.MOVE_RIGHT);
	}
	
	public void moveLeft(MotionEvent event){
		change(event, Direction.MOVE_LEFT);
	}
	
	public void sizeIncrease(MotionEvent event){
		change(event, Direction.SIZE_INCREASE);
	}
	
	public void sizeDecrease(MotionEvent event){
		change(event, Direction.SIZE_DECREASE);
	}
	
	public void perspectiveIncrease(MotionEvent event){
		change(event, Direction.PERSPECTIVE_INCREASE);
	}
	
	public void perspectiveDecrease(MotionEvent event){
		change(event, Direction.PERSPECTIVE_DECREASE);
	}
	
	protected void change(MotionEvent event, Direction direction){
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startChange(direction);
			break;
		case MotionEvent.ACTION_UP:
			stopChange();
			break;
		}
	}
	
	
	protected void startChange(final Direction direction){
		mChange = true;
		if(mChangeThread == null || mChangeThread.getState() != Thread.State.RUNNABLE){
			mChangeThread = new Thread(new Runnable() {
		    	public void run() {
		    		int i = 0;
		    		while(mChange){
		    			switch(direction){
			    			case MOVE_UP:
			    				mBuilder.moveUp();
			    				break;
							case MOVE_DOWN:
			    				mBuilder.moveDown();
								break;
							case MOVE_LEFT:
			    				mBuilder.moveLeft();
								break;
							case MOVE_RIGHT:
			    				mBuilder.moveRight();
			    				break;
			    			case SIZE_INCREASE:
			    				mBuilder.sizeIncrease();
			    				break;
							case SIZE_DECREASE:
			    				mBuilder.sizeDecrease();
								break;
							case PERSPECTIVE_INCREASE:
			    				mBuilder.perspectiveIncrease();
								break;
							case PERSPECTIVE_DECREASE:
			    				mBuilder.perspectiveDecrease();
			    				break;
							default:
								break;
		    			}
		    			
		    			i++;
		    			if((i <= 3) || 
		    					(i > 3 && i <= 15 && (i/3) * 3 == i) ||
		    					(i > 15 && (i/10) * 10 == i)){
			    			final Bitmap bitmap= mBuilder.get();
			    			mImageView.post(new Runnable() {
				                public void run() {
				                	mImageView.setImageBitmap(bitmap);
				                }
				            });
		    			}
		    		}
		    		
	    			final Bitmap bitmap = mBuilder.get();
	    			mImageView.post(new Runnable() {
		                public void run() {
		                	mImageView.setImageBitmap(bitmap);
		                }
		            });
		    		
		    		}
		    	});
			mChangeThread.setPriority(Thread.MAX_PRIORITY);
			mChangeThread.start();
		}
	}
	
	protected void stopChange(){
		mChange = false;
	}
}
