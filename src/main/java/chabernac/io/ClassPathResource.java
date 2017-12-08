package chabernac.io;

import java.io.InputStream;

public class ClassPathResource implements IResource {
	private final String location;

	public ClassPathResource(String location) {
		this.location = location;
	}

	@Override
	public InputStream getInputStream() {
		return getClass().getResourceAsStream(location);
	}
}
