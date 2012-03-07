package net.cipol.api;

import java.io.File;

public interface HomeService {
	
	File getHome();

    File getFile(String path);
    
}
