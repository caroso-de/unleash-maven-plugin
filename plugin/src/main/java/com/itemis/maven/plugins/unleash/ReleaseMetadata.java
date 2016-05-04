package com.itemis.maven.plugins.unleash;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.RepositoryUtils;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.repository.RemoteRepository;
import org.w3c.dom.Document;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itemis.maven.aether.ArtifactCoordinates;
import com.itemis.maven.plugins.unleash.util.ReleaseUtil;
import com.itemis.maven.plugins.unleash.util.functions.ProjectToXmlDocument;

@Singleton
// TODO add serialization of metadata as a reporting feature!
public class ReleaseMetadata {
  @Inject
  private MavenProject project;
  @Inject
  @Named("tagNamePattern")
  private String tagNamePattern;
  @Inject
  @Named("reactorProjects")
  private List<MavenProject> reactorProjects;

  private Map<ReleasePhase, String> scmRevisions;
  private Map<ReleasePhase, Set<ArtifactCoordinates>> artifactCoordinates;
  private String scmTagName;
  private RemoteRepository deploymentRepository;
  private Map<MavenProject, Document> cachedPomDocs;

  private ReleaseMetadata() {
    int numPhases = ReleasePhase.values().length;
    this.scmRevisions = Maps.newHashMapWithExpectedSize(numPhases);
    this.artifactCoordinates = Maps.newHashMapWithExpectedSize(numPhases);
    for (ReleasePhase phase : ReleasePhase.values()) {
      this.artifactCoordinates.put(phase, Sets.<ArtifactCoordinates> newHashSet());
    }
  }

  @PostConstruct
  public void init() {
    this.scmTagName = ReleaseUtil.getTagName(this.tagNamePattern, this.project);

    // setting the artifact version to a release version temporarily since the dist repository is checks for a snapshot
    // version of the artifact. Maybe this can be implemented in a different manner but then we would have to setup the
    // repository manually
    org.apache.maven.artifact.Artifact projectArtifact = this.project.getArtifact();
    String oldVersion = projectArtifact.getVersion();
    projectArtifact.setVersion("1");

    // getting the remote repo
    this.deploymentRepository = RepositoryUtils.toRepo(this.project.getDistributionManagementArtifactRepository());

    // resetting the artifact version
    projectArtifact.setVersion(oldVersion);

    // caching of the parsed pom documents for later reversal in case of failure
    this.cachedPomDocs = Maps.toMap(this.reactorProjects, ProjectToXmlDocument.INSTANCE);
  }

  public void setScmRevision(String scmRevision, ReleasePhase phase) {
    this.scmRevisions.put(phase, scmRevision);
  }

  public String getScmRevision(ReleasePhase phase) {
    return this.scmRevisions.get(phase);
  }

  public void addArtifactCoordinates(ArtifactCoordinates coordinates, ReleasePhase phase) {
    this.artifactCoordinates.get(phase).add(coordinates);
  }

  public Map<ReleasePhase, ArtifactCoordinates> getArtifactCoordinatesByPhase(String groupId, String artifactId) {
    Map<ReleasePhase, ArtifactCoordinates> result = Maps.newHashMapWithExpectedSize(this.artifactCoordinates.size());
    for (ReleasePhase phase : this.artifactCoordinates.keySet()) {
      for (ArtifactCoordinates coordinates : this.artifactCoordinates.get(phase)) {
        if (Objects.equal(coordinates.getArtifactId(), artifactId)
            && Objects.equal(coordinates.getGroupId(), groupId)) {
          result.put(phase, coordinates);
          break;
        }
      }
    }
    return result;
  }

  public String getScmTagName() {
    return this.scmTagName;
  }

  public RemoteRepository getDeploymentRepository() {
    return this.deploymentRepository;
  }

  public Document getCachedDocument(MavenProject p) {
    return this.cachedPomDocs.get(p);
  }
}
