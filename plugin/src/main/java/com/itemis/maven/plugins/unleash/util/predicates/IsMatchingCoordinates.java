/**
 * (c) caroso-de 2022
 */
package com.itemis.maven.plugins.unleash.util.predicates;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.itemis.maven.aether.ArtifactCoordinates;
import com.itemis.maven.plugins.unleash.util.functions.GAVPatternToCoordinates;

public class IsMatchingCoordinates implements Predicate<ArtifactCoordinates> {

	private final Set<ArtifactCoordinates> referencePatterns;

	public IsMatchingCoordinates(List<String> someAllowedSnapshots) {
		List<String> allowedSnapshots = someAllowedSnapshots != null ? someAllowedSnapshots : new ArrayList<>();

		referencePatterns = Sets.newHashSet(Collections2.transform(allowedSnapshots, new GAVPatternToCoordinates()));
	}

	@Override
	public boolean apply(ArtifactCoordinates coordinates) {
		for (ArtifactCoordinates referencePattern : referencePatterns) {
			if (matchesPattern(coordinates, referencePattern)) {
				return true;
			}
		}
		return false;
	}

	private boolean matchesPattern(ArtifactCoordinates aCoordinates, ArtifactCoordinates aReferencePattern) {
		String tReferenceGroupId = aReferencePattern.getGroupId();
		String tGroupId = aCoordinates.getGroupId();

		String tReferenceArtifactId = aReferencePattern.getArtifactId();
		String tArtifactId = aCoordinates.getArtifactId();
		String tReferenceVersion = aReferencePattern.getVersion();
		String tVersion = aCoordinates.getVersion();

		boolean tGroupIdMatches = wildcardOrLiteralMatch(tReferenceGroupId, tGroupId);
		boolean tArtifactIdMatches = tReferenceArtifactId == null
				|| wildcardOrLiteralMatch(tReferenceArtifactId, tArtifactId);
		boolean tVersionMatches = tVersion == null || wildcardOrLiteralMatch(tReferenceVersion, tVersion);

		return tGroupIdMatches && tArtifactIdMatches && tVersionMatches;
	}

	private boolean wildcardOrLiteralMatch(String tLeft, String tRight) {
		boolean atLeastOneIsNull = tLeft == null || tRight == null;
		return atLeastOneIsNull || Objects.equal(tLeft, tRight) || Objects.equal(tLeft, "*")
				|| Objects.equal(tRight, "*");
	}

}
