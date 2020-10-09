package model.mediabehavior.audiobehavior;

import model.MediaProduct;
import model.mediabehavior.IMediaBehavior;

public class AudioPlay implements IMediaBehavior {

	@Override
	public void perform(MediaProduct p) {
		System.out.println(p.getName() + " is playing.");
	}

}
