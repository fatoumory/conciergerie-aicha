package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.JournalAuditAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.JournalAudit;
import sn.orange.conciergerie.repository.JournalAuditRepository;
import sn.orange.conciergerie.service.dto.JournalAuditDTO;
import sn.orange.conciergerie.service.mapper.JournalAuditMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link JournalAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JournalAuditResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OBJET = "BBBBBBBBBB";

    private static final UUID DEFAULT_OBJET_ID = UUID.randomUUID();
    private static final UUID UPDATED_OBJET_ID = UUID.randomUUID();

    private static final String DEFAULT_CORRELATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CORRELATION_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ACTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACTION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/journal-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private JournalAuditRepository journalAuditRepository;

    @Autowired
    private JournalAuditMapper journalAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJournalAuditMockMvc;

    private JournalAudit journalAudit;

    private JournalAudit insertedJournalAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JournalAudit createEntity() {
        return new JournalAudit()
            .action(DEFAULT_ACTION)
            .typeObjet(DEFAULT_TYPE_OBJET)
            .objetId(DEFAULT_OBJET_ID)
            .correlationId(DEFAULT_CORRELATION_ID)
            .dateAction(DEFAULT_DATE_ACTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JournalAudit createUpdatedEntity() {
        return new JournalAudit()
            .action(UPDATED_ACTION)
            .typeObjet(UPDATED_TYPE_OBJET)
            .objetId(UPDATED_OBJET_ID)
            .correlationId(UPDATED_CORRELATION_ID)
            .dateAction(UPDATED_DATE_ACTION);
    }

    @BeforeEach
    void initTest() {
        journalAudit = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedJournalAudit != null) {
            journalAuditRepository.delete(insertedJournalAudit);
            insertedJournalAudit = null;
        }
    }

    @Test
    @Transactional
    void createJournalAudit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);
        var returnedJournalAuditDTO = om.readValue(
            restJournalAuditMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            JournalAuditDTO.class
        );

        // Validate the JournalAudit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedJournalAudit = journalAuditMapper.toEntity(returnedJournalAuditDTO);
        assertJournalAuditUpdatableFieldsEquals(returnedJournalAudit, getPersistedJournalAudit(returnedJournalAudit));

        insertedJournalAudit = returnedJournalAudit;
    }

    @Test
    @Transactional
    void createJournalAuditWithExistingId() throws Exception {
        // Create the JournalAudit with an existing ID
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJournalAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isBadRequest());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journalAudit.setAction(null);

        // Create the JournalAudit, which fails.
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        restJournalAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeObjetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journalAudit.setTypeObjet(null);

        // Create the JournalAudit, which fails.
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        restJournalAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCorrelationIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journalAudit.setCorrelationId(null);

        // Create the JournalAudit, which fails.
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        restJournalAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        journalAudit.setDateAction(null);

        // Create the JournalAudit, which fails.
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        restJournalAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJournalAudits() throws Exception {
        // Initialize the database
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);

        // Get all the journalAuditList
        restJournalAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journalAudit.getId().toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].typeObjet").value(hasItem(DEFAULT_TYPE_OBJET)))
            .andExpect(jsonPath("$.[*].objetId").value(hasItem(DEFAULT_OBJET_ID.toString())))
            .andExpect(jsonPath("$.[*].correlationId").value(hasItem(DEFAULT_CORRELATION_ID)))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(DEFAULT_DATE_ACTION.toString())));
    }

    @Test
    @Transactional
    void getJournalAudit() throws Exception {
        // Initialize the database
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);

        // Get the journalAudit
        restJournalAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, journalAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journalAudit.getId().toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.typeObjet").value(DEFAULT_TYPE_OBJET))
            .andExpect(jsonPath("$.objetId").value(DEFAULT_OBJET_ID.toString()))
            .andExpect(jsonPath("$.correlationId").value(DEFAULT_CORRELATION_ID))
            .andExpect(jsonPath("$.dateAction").value(DEFAULT_DATE_ACTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJournalAudit() throws Exception {
        // Get the journalAudit
        restJournalAuditMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJournalAudit() throws Exception {
        // Initialize the database
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journalAudit
        JournalAudit updatedJournalAudit = journalAuditRepository.findById(journalAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedJournalAudit are not directly saved in db
        em.detach(updatedJournalAudit);
        updatedJournalAudit
            .action(UPDATED_ACTION)
            .typeObjet(UPDATED_TYPE_OBJET)
            .objetId(UPDATED_OBJET_ID)
            .correlationId(UPDATED_CORRELATION_ID)
            .dateAction(UPDATED_DATE_ACTION);
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(updatedJournalAudit);

        restJournalAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journalAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(journalAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedJournalAuditToMatchAllProperties(updatedJournalAudit);
    }

    @Test
    @Transactional
    void putNonExistingJournalAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalAudit.setId(UUID.randomUUID());

        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journalAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(journalAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJournalAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalAudit.setId(UUID.randomUUID());

        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(journalAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJournalAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalAudit.setId(UUID.randomUUID());

        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJournalAuditWithPatch() throws Exception {
        // Initialize the database
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journalAudit using partial update
        JournalAudit partialUpdatedJournalAudit = new JournalAudit();
        partialUpdatedJournalAudit.setId(journalAudit.getId());

        partialUpdatedJournalAudit
            .action(UPDATED_ACTION)
            .typeObjet(UPDATED_TYPE_OBJET)
            .correlationId(UPDATED_CORRELATION_ID)
            .dateAction(UPDATED_DATE_ACTION);

        restJournalAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournalAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJournalAudit))
            )
            .andExpect(status().isOk());

        // Validate the JournalAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJournalAuditUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedJournalAudit, journalAudit),
            getPersistedJournalAudit(journalAudit)
        );
    }

    @Test
    @Transactional
    void fullUpdateJournalAuditWithPatch() throws Exception {
        // Initialize the database
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the journalAudit using partial update
        JournalAudit partialUpdatedJournalAudit = new JournalAudit();
        partialUpdatedJournalAudit.setId(journalAudit.getId());

        partialUpdatedJournalAudit
            .action(UPDATED_ACTION)
            .typeObjet(UPDATED_TYPE_OBJET)
            .objetId(UPDATED_OBJET_ID)
            .correlationId(UPDATED_CORRELATION_ID)
            .dateAction(UPDATED_DATE_ACTION);

        restJournalAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJournalAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedJournalAudit))
            )
            .andExpect(status().isOk());

        // Validate the JournalAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertJournalAuditUpdatableFieldsEquals(partialUpdatedJournalAudit, getPersistedJournalAudit(partialUpdatedJournalAudit));
    }

    @Test
    @Transactional
    void patchNonExistingJournalAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalAudit.setId(UUID.randomUUID());

        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJournalAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, journalAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(journalAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJournalAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalAudit.setId(UUID.randomUUID());

        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(journalAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJournalAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        journalAudit.setId(UUID.randomUUID());

        // Create the JournalAudit
        JournalAuditDTO journalAuditDTO = journalAuditMapper.toDto(journalAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJournalAuditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(journalAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JournalAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJournalAudit() throws Exception {
        // Initialize the database
        insertedJournalAudit = journalAuditRepository.saveAndFlush(journalAudit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the journalAudit
        restJournalAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, journalAudit.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return journalAuditRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected JournalAudit getPersistedJournalAudit(JournalAudit journalAudit) {
        return journalAuditRepository.findById(journalAudit.getId()).orElseThrow();
    }

    protected void assertPersistedJournalAuditToMatchAllProperties(JournalAudit expectedJournalAudit) {
        assertJournalAuditAllPropertiesEquals(expectedJournalAudit, getPersistedJournalAudit(expectedJournalAudit));
    }

    protected void assertPersistedJournalAuditToMatchUpdatableProperties(JournalAudit expectedJournalAudit) {
        assertJournalAuditAllUpdatablePropertiesEquals(expectedJournalAudit, getPersistedJournalAudit(expectedJournalAudit));
    }
}
