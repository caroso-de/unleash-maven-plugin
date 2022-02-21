/**
 * (c) caroso-de 2022
 */
package de.caroso.maven.plugins.unleash.util.functions;

import com.google.common.base.Function;

import com.itemis.maven.aether.ArtifactCoordinates;

/**
 * @author <a href="mailto:oss@caroso.de">Carsten Rohde</a> 
*/
public class GAVPatternToCoordinates implements Function<String, ArtifactCoordinates> {

	@Override
	public ArtifactCoordinates apply(String gav) {
		String[] tokens = gav.split(":");
		switch (tokens.length) {
		case 1:
			return new ArtifactCoordinates(tokens[0], null, null);
		case 2:
			return new ArtifactCoordinates(tokens[0], tokens[1], null);
		case 3:
		default:
			return new ArtifactCoordinates(tokens[0], tokens[1], tokens[2]);
		}
	}

}
