package model.mediabehavior.audiobehavior;

import model.MediaProduct;
import model.mediabehavior.IMediaBehavior;

public class AudioNext implements IMediaBehavior {

	@Override
	public void perform(MediaProduct p) {
		System.out.println(p.getName() + " is playing the next media.");
	}

}
