package model.mediabehavior.moviebehavior;

import model.MediaProduct;
import model.mediabehavior.IMediaBehavior;

public class MoviePlay implements IMediaBehavior {

	@Override
	public void perform(MediaProduct p) {
		System.out.println("Playing");
	}

}
