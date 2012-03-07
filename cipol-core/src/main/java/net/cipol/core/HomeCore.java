package net.cipol.core;

import java.io.File;

import org.springframework.stereotype.Service;

import net.cipol.api.HomeService;

@Service
public class HomeCore implements HomeService {

    private final File home;

    public HomeCore() {
        home = HomeSupport.home();
    }

    @Override
    public File getHome() {
        return home;
    }

    @Override
    public File getFile(String path) {
        return new File(home, path);
    }

}
