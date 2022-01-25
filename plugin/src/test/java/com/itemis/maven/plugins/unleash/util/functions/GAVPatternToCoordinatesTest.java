/**
 * (c) caroso-de 2022
 */
package com.itemis.maven.plugins.unleash.util.functions;

import static org.junit.Assert.*;

import org.junit.Test;

import com.itemis.maven.aether.ArtifactCoordinates;


public class GAVPatternToCoordinatesTest {


	@Test
	public void apply_gav() {
		ArtifactCoordinates coordinates = new GAVPatternToCoordinates().apply("g:a:v");
		assertEquals("g", coordinates.getGroupId());
		assertEquals("a", coordinates.getArtifactId());
		assertEquals("v", coordinates.getVersion());
	}

	@Test
	public void apply_ga() {
		ArtifactCoordinates coordinates = new GAVPatternToCoordinates().apply("g:a");
		assertEquals("g", coordinates.getGroupId());
		assertEquals("a", coordinates.getArtifactId());
		assertEquals(null, coordinates.getVersion());
	}	
	
	@Test
	public void apply_g() {
		ArtifactCoordinates coordinates = new GAVPatternToCoordinates().apply("g");
		assertEquals("g", coordinates.getGroupId());
		assertEquals(null, coordinates.getArtifactId());
		assertEquals(null, coordinates.getVersion());
	}		
	
}
