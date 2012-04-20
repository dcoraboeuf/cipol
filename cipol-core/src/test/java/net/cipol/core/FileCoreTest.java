package net.cipol.core;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class FileCoreTest {
	
	private FileCore core;
	
	@Before
	public void before() {
		core = new FileCore(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void extractIdFromFileName_null () {
		core.extractIdFromFileName(String.class, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void extractIdFromFileName_totally_incorrect () {
		core.extractIdFromFileName(String.class, "xxx");
	}
	
	@Test
	public void extractIdFromFileName_ok () {
		String id = core.extractIdFromFileName(String.class, "java.lang.String.test.json");
		assertEquals("test", id);
	}
	
	@Test
	public void extractIdFromFileName_uuid () {
		String uuid = UUID.randomUUID().toString();
		String id = core.extractIdFromFileName(String.class, "java.lang.String." + uuid + ".json");
		assertEquals(uuid, id);
	}

}
