/**
 * (c) caroso-de 2022
 */
package com.itemis.maven.plugins.unleash.util.predicates;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.itemis.maven.aether.ArtifactCoordinates;

import edu.emory.mathcs.backport.java.util.Arrays;

public class IsMatchingCoordinatesTest {

	@Test
	public void apply_emptyrefs_matching() {
		boolean matches = new IsMatchingCoordinates(new ArrayList<String>())
				.apply(new ArtifactCoordinates("g", "a", "v"));
		assertFalse(matches);
	}

	@Test
	public void apply_gav_matching() {
		boolean matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:a:v" }))
				.apply(new ArtifactCoordinates("g", "a", "v"));
		assertTrue(matches);
	}

	@Test
	public void apply_gav_nonmatching() {
		boolean matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:a:v" }))
				.apply(new ArtifactCoordinates("g", "x", "v"));
		assertFalse(matches);
	}

	@Test
	public void apply_ga_matching() {
		boolean matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:a" }))
				.apply(new ArtifactCoordinates("g", "a", "v"));
		
		assertTrue(matches);
		matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:a:*" }))
				.apply(new ArtifactCoordinates("g", "a", "v"));
		assertTrue(matches);		
	}

	@Test
	public void apply_ga_nonmatching() {
		boolean matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:a" }))
				.apply(new ArtifactCoordinates("g", "x", "v"));
		
		assertFalse(matches);
		matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:a:*" }))
				.apply(new ArtifactCoordinates("g", "y", "v"));
		assertFalse(matches);	
	}

	@Test
	public void apply_g_matching() {
		boolean matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:*" }))
				.apply(new ArtifactCoordinates("g", "a", "v"));
		
		assertTrue(matches);
	}

	@Test
	public void apply_g_nonmatching() {
		boolean matches = new IsMatchingCoordinates(Arrays.asList(new String[] { "g:*" }))
				.apply(new ArtifactCoordinates("x", "a", "v"));
		
		assertFalse(matches);
	}

}
