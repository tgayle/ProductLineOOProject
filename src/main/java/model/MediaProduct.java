package model;

import model.mediabehavior.IMediaBehavior;

public abstract class MediaProduct extends Product implements MultimediaControl {	
	
	protected IMediaBehavior play;
	protected IMediaBehavior stop;
	protected IMediaBehavior previous;
	protected IMediaBehavior next;
	
	public MediaProduct(String name, String manufacturer) {
		super(name, manufacturer);
	}
	
	public void setPlay(IMediaBehavior play) {
		this.play = play;
	}
	
	public void setStop(IMediaBehavior stop) {
		this.stop = stop;
	}
	
	public void setPrevious(IMediaBehavior prev) {
		this.previous = prev;
	}
	
	public void setNext(IMediaBehavior next) {
		this.next = next;
	}
	
	public void play() {
		play.perform(this);
	}
	public void stop() {
		stop.perform(this);
	}
	public void next() {
		next.perform(this);
	}
	public void previous() {
		previous.perform(this);
	}

}
