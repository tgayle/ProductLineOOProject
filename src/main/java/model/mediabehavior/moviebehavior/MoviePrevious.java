package model.mediabehavior.moviebehavior;

import model.MediaProduct;
import model.mediabehavior.IMediaBehavior;

public class MoviePrevious implements IMediaBehavior {

	@Override
	public void perform(MediaProduct p) {
		System.out.println("Previous");
	}

}
