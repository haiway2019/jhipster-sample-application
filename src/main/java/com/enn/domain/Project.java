package com.enn.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Project.
 */
@Document(collection = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("group_id")
    private String groupId;

    @Field("artifact")
    private String artifact;

    @Field("version")
    private String version;

    @Field("tenant_id")
    private String tenantId;

    @Field("create_time")
    private String createTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public Project groupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifact() {
        return artifact;
    }

    public Project artifact(String artifact) {
        this.artifact = artifact;
        return this;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public String getVersion() {
        return version;
    }

    public Project version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Project tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Project createTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", groupId='" + getGroupId() + "'" +
            ", artifact='" + getArtifact() + "'" +
            ", version='" + getVersion() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            "}";
    }
}
