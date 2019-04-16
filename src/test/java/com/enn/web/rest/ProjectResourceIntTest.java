package com.enn.web.rest;

import com.enn.JhipsterSampleApplicationApp;

import com.enn.domain.Project;
import com.enn.repository.ProjectRepository;
import com.enn.service.ProjectService;
import com.enn.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import java.util.List;


import static com.enn.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ARTIFACT = "AAAAAAAAAA";
    private static final String UPDATED_ARTIFACT = "BBBBBBBBBB";

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CREATE_TIME = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_TIME = "BBBBBBBBBB";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restProjectMockMvc;

    private Project project;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectResource projectResource = new ProjectResource(projectService);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity() {
        Project project = new Project()
            .name(DEFAULT_NAME)
            .groupId(DEFAULT_GROUP_ID)
            .artifact(DEFAULT_ARTIFACT)
            .version(DEFAULT_VERSION)
            .tenantId(DEFAULT_TENANT_ID)
            .createTime(DEFAULT_CREATE_TIME);
        return project;
    }

    @Before
    public void initTest() {
        projectRepository.deleteAll();
        project = createEntity();
    }

    @Test
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testProject.getArtifact()).isEqualTo(DEFAULT_ARTIFACT);
        assertThat(testProject.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testProject.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testProject.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.save(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID.toString())))
            .andExpect(jsonPath("$.[*].artifact").value(hasItem(DEFAULT_ARTIFACT.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.toString())));
    }
    
    @Test
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.save(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID.toString()))
            .andExpect(jsonPath("$.artifact").value(DEFAULT_ARTIFACT.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.toString()));
    }

    @Test
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        updatedProject
            .name(UPDATED_NAME)
            .groupId(UPDATED_GROUP_ID)
            .artifact(UPDATED_ARTIFACT)
            .version(UPDATED_VERSION)
            .tenantId(UPDATED_TENANT_ID)
            .createTime(UPDATED_CREATE_TIME);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testProject.getArtifact()).isEqualTo(UPDATED_ARTIFACT);
        assertThat(testProject.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testProject.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testProject.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = new Project();
        project1.setId("id1");
        Project project2 = new Project();
        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);
        project2.setId("id2");
        assertThat(project1).isNotEqualTo(project2);
        project1.setId(null);
        assertThat(project1).isNotEqualTo(project2);
    }
}
