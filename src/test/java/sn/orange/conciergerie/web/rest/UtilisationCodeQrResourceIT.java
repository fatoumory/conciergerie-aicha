package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.UtilisationCodeQrAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.CodeQrService;
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.UtilisationCodeQr;
import sn.orange.conciergerie.repository.UtilisationCodeQrRepository;
import sn.orange.conciergerie.service.UtilisationCodeQrService;
import sn.orange.conciergerie.service.dto.UtilisationCodeQrDTO;
import sn.orange.conciergerie.service.mapper.UtilisationCodeQrMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link UtilisationCodeQrResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilisationCodeQrResourceIT {

    private static final Instant DEFAULT_DATE_UTILISATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UTILISATION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/utilisation-code-qrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtilisationCodeQrRepository utilisationCodeQrRepository;

    @Mock
    private UtilisationCodeQrRepository utilisationCodeQrRepositoryMock;

    @Autowired
    private UtilisationCodeQrMapper utilisationCodeQrMapper;

    @Mock
    private UtilisationCodeQrService utilisationCodeQrServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisationCodeQrMockMvc;

    private UtilisationCodeQr utilisationCodeQr;

    private UtilisationCodeQr insertedUtilisationCodeQr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisationCodeQr createEntity(EntityManager em) {
        UtilisationCodeQr utilisationCodeQr = new UtilisationCodeQr().dateUtilisation(DEFAULT_DATE_UTILISATION);
        // Add required entity
        CodeQrService codeQrService;
        if (TestUtil.findAll(em, CodeQrService.class).isEmpty()) {
            codeQrService = CodeQrServiceResourceIT.createEntity(em);
            em.persist(codeQrService);
            em.flush();
        } else {
            codeQrService = TestUtil.findAll(em, CodeQrService.class).getFirst();
        }
        utilisationCodeQr.setCodeQrService(codeQrService);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        utilisationCodeQr.setPartenaire(partenaire);
        return utilisationCodeQr;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisationCodeQr createUpdatedEntity(EntityManager em) {
        UtilisationCodeQr updatedUtilisationCodeQr = new UtilisationCodeQr().dateUtilisation(UPDATED_DATE_UTILISATION);
        // Add required entity
        CodeQrService codeQrService;
        if (TestUtil.findAll(em, CodeQrService.class).isEmpty()) {
            codeQrService = CodeQrServiceResourceIT.createUpdatedEntity(em);
            em.persist(codeQrService);
            em.flush();
        } else {
            codeQrService = TestUtil.findAll(em, CodeQrService.class).getFirst();
        }
        updatedUtilisationCodeQr.setCodeQrService(codeQrService);
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createUpdatedEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        updatedUtilisationCodeQr.setPartenaire(partenaire);
        return updatedUtilisationCodeQr;
    }

    @BeforeEach
    void initTest() {
        utilisationCodeQr = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedUtilisationCodeQr != null) {
            utilisationCodeQrRepository.delete(insertedUtilisationCodeQr);
            insertedUtilisationCodeQr = null;
        }
    }

    @Test
    @Transactional
    void createUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);
        var returnedUtilisationCodeQrDTO = om.readValue(
            restUtilisationCodeQrMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisationCodeQrDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UtilisationCodeQrDTO.class
        );

        // Validate the UtilisationCodeQr in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUtilisationCodeQr = utilisationCodeQrMapper.toEntity(returnedUtilisationCodeQrDTO);
        assertUtilisationCodeQrUpdatableFieldsEquals(returnedUtilisationCodeQr, getPersistedUtilisationCodeQr(returnedUtilisationCodeQr));

        insertedUtilisationCodeQr = returnedUtilisationCodeQr;
    }

    @Test
    @Transactional
    void createUtilisationCodeQrWithExistingId() throws Exception {
        // Create the UtilisationCodeQr with an existing ID
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisationCodeQrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisationCodeQrDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateUtilisationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisationCodeQr.setDateUtilisation(null);

        // Create the UtilisationCodeQr, which fails.
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        restUtilisationCodeQrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisationCodeQrDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilisationCodeQrs() throws Exception {
        // Initialize the database
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);

        // Get all the utilisationCodeQrList
        restUtilisationCodeQrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisationCodeQr.getId().toString())))
            .andExpect(jsonPath("$.[*].dateUtilisation").value(hasItem(DEFAULT_DATE_UTILISATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisationCodeQrsWithEagerRelationshipsIsEnabled() throws Exception {
        when(utilisationCodeQrServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisationCodeQrMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilisationCodeQrServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisationCodeQrsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(utilisationCodeQrServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisationCodeQrMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(utilisationCodeQrRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUtilisationCodeQr() throws Exception {
        // Initialize the database
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);

        // Get the utilisationCodeQr
        restUtilisationCodeQrMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisationCodeQr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisationCodeQr.getId().toString()))
            .andExpect(jsonPath("$.dateUtilisation").value(DEFAULT_DATE_UTILISATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUtilisationCodeQr() throws Exception {
        // Get the utilisationCodeQr
        restUtilisationCodeQrMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilisationCodeQr() throws Exception {
        // Initialize the database
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisationCodeQr
        UtilisationCodeQr updatedUtilisationCodeQr = utilisationCodeQrRepository.findById(utilisationCodeQr.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtilisationCodeQr are not directly saved in db
        em.detach(updatedUtilisationCodeQr);
        updatedUtilisationCodeQr.dateUtilisation(UPDATED_DATE_UTILISATION);
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(updatedUtilisationCodeQr);

        restUtilisationCodeQrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisationCodeQrDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisationCodeQrDTO))
            )
            .andExpect(status().isOk());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtilisationCodeQrToMatchAllProperties(updatedUtilisationCodeQr);
    }

    @Test
    @Transactional
    void putNonExistingUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisationCodeQr.setId(UUID.randomUUID());

        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisationCodeQrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisationCodeQrDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisationCodeQrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisationCodeQr.setId(UUID.randomUUID());

        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationCodeQrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisationCodeQrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisationCodeQr.setId(UUID.randomUUID());

        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationCodeQrMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisationCodeQrDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisationCodeQrWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisationCodeQr using partial update
        UtilisationCodeQr partialUpdatedUtilisationCodeQr = new UtilisationCodeQr();
        partialUpdatedUtilisationCodeQr.setId(utilisationCodeQr.getId());

        partialUpdatedUtilisationCodeQr.dateUtilisation(UPDATED_DATE_UTILISATION);

        restUtilisationCodeQrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisationCodeQr.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisationCodeQr))
            )
            .andExpect(status().isOk());

        // Validate the UtilisationCodeQr in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisationCodeQrUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtilisationCodeQr, utilisationCodeQr),
            getPersistedUtilisationCodeQr(utilisationCodeQr)
        );
    }

    @Test
    @Transactional
    void fullUpdateUtilisationCodeQrWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisationCodeQr using partial update
        UtilisationCodeQr partialUpdatedUtilisationCodeQr = new UtilisationCodeQr();
        partialUpdatedUtilisationCodeQr.setId(utilisationCodeQr.getId());

        partialUpdatedUtilisationCodeQr.dateUtilisation(UPDATED_DATE_UTILISATION);

        restUtilisationCodeQrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisationCodeQr.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisationCodeQr))
            )
            .andExpect(status().isOk());

        // Validate the UtilisationCodeQr in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisationCodeQrUpdatableFieldsEquals(
            partialUpdatedUtilisationCodeQr,
            getPersistedUtilisationCodeQr(partialUpdatedUtilisationCodeQr)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisationCodeQr.setId(UUID.randomUUID());

        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisationCodeQrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisationCodeQrDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisationCodeQrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisationCodeQr.setId(UUID.randomUUID());

        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationCodeQrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisationCodeQrDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisationCodeQr() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisationCodeQr.setId(UUID.randomUUID());

        // Create the UtilisationCodeQr
        UtilisationCodeQrDTO utilisationCodeQrDTO = utilisationCodeQrMapper.toDto(utilisationCodeQr);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationCodeQrMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(utilisationCodeQrDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisationCodeQr in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisationCodeQr() throws Exception {
        // Initialize the database
        insertedUtilisationCodeQr = utilisationCodeQrRepository.saveAndFlush(utilisationCodeQr);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utilisationCodeQr
        restUtilisationCodeQrMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisationCodeQr.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utilisationCodeQrRepository.count();
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

    protected UtilisationCodeQr getPersistedUtilisationCodeQr(UtilisationCodeQr utilisationCodeQr) {
        return utilisationCodeQrRepository.findById(utilisationCodeQr.getId()).orElseThrow();
    }

    protected void assertPersistedUtilisationCodeQrToMatchAllProperties(UtilisationCodeQr expectedUtilisationCodeQr) {
        assertUtilisationCodeQrAllPropertiesEquals(expectedUtilisationCodeQr, getPersistedUtilisationCodeQr(expectedUtilisationCodeQr));
    }

    protected void assertPersistedUtilisationCodeQrToMatchUpdatableProperties(UtilisationCodeQr expectedUtilisationCodeQr) {
        assertUtilisationCodeQrAllUpdatablePropertiesEquals(
            expectedUtilisationCodeQr,
            getPersistedUtilisationCodeQr(expectedUtilisationCodeQr)
        );
    }
}
