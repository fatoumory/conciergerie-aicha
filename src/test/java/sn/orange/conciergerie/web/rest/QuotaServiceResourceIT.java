package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.QuotaServiceAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.orange.conciergerie.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import sn.orange.conciergerie.domain.EligibiliteService;
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.domain.enumeration.PeriodeQuota;
import sn.orange.conciergerie.domain.enumeration.UniteQuota;
import sn.orange.conciergerie.repository.QuotaServiceRepository;
import sn.orange.conciergerie.service.dto.QuotaServiceDTO;
import sn.orange.conciergerie.service.mapper.QuotaServiceMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link QuotaServiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuotaServiceResourceIT {

    private static final BigDecimal DEFAULT_LIMITE = new BigDecimal(0);
    private static final BigDecimal UPDATED_LIMITE = new BigDecimal(1);

    private static final UniteQuota DEFAULT_UNITE = UniteQuota.UNITE;
    private static final UniteQuota UPDATED_UNITE = UniteQuota.ACCES;

    private static final PeriodeQuota DEFAULT_PERIODE = PeriodeQuota.JOUR;
    private static final PeriodeQuota UPDATED_PERIODE = PeriodeQuota.SEMAINE;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.parse("2023-12-06");

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.parse("2023-12-06");

    private static final String ENTITY_API_URL = "/api/quota-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuotaServiceRepository quotaServiceRepository;

    @Autowired
    private QuotaServiceMapper quotaServiceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotaServiceMockMvc;

    private QuotaService quotaService;

    private QuotaService insertedQuotaService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotaService createEntity(EntityManager em) {
        QuotaService quotaService = new QuotaService()
            .limite(DEFAULT_LIMITE)
            .unite(DEFAULT_UNITE)
            .periode(DEFAULT_PERIODE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        // Add required entity
        EligibiliteService eligibiliteService;
        if (TestUtil.findAll(em, EligibiliteService.class).isEmpty()) {
            eligibiliteService = EligibiliteServiceResourceIT.createEntity(em);
            em.persist(eligibiliteService);
            em.flush();
        } else {
            eligibiliteService = TestUtil.findAll(em, EligibiliteService.class).getFirst();
        }
        quotaService.setEligibiliteService(eligibiliteService);
        return quotaService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotaService createUpdatedEntity(EntityManager em) {
        QuotaService updatedQuotaService = new QuotaService()
            .limite(UPDATED_LIMITE)
            .unite(UPDATED_UNITE)
            .periode(UPDATED_PERIODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        // Add required entity
        EligibiliteService eligibiliteService;
        if (TestUtil.findAll(em, EligibiliteService.class).isEmpty()) {
            eligibiliteService = EligibiliteServiceResourceIT.createUpdatedEntity(em);
            em.persist(eligibiliteService);
            em.flush();
        } else {
            eligibiliteService = TestUtil.findAll(em, EligibiliteService.class).getFirst();
        }
        updatedQuotaService.setEligibiliteService(eligibiliteService);
        return updatedQuotaService;
    }

    @BeforeEach
    void initTest() {
        quotaService = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuotaService != null) {
            quotaServiceRepository.delete(insertedQuotaService);
            insertedQuotaService = null;
        }
    }

    @Test
    @Transactional
    void createQuotaService() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);
        var returnedQuotaServiceDTO = om.readValue(
            restQuotaServiceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuotaServiceDTO.class
        );

        // Validate the QuotaService in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuotaService = quotaServiceMapper.toEntity(returnedQuotaServiceDTO);
        assertQuotaServiceUpdatableFieldsEquals(returnedQuotaService, getPersistedQuotaService(returnedQuotaService));

        insertedQuotaService = returnedQuotaService;
    }

    @Test
    @Transactional
    void createQuotaServiceWithExistingId() throws Exception {
        // Create the QuotaService with an existing ID
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotaServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLimiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotaService.setLimite(null);

        // Create the QuotaService, which fails.
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        restQuotaServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUniteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotaService.setUnite(null);

        // Create the QuotaService, which fails.
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        restQuotaServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotaService.setPeriode(null);

        // Create the QuotaService, which fails.
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        restQuotaServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotaService.setDateDebut(null);

        // Create the QuotaService, which fails.
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        restQuotaServiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotaServices() throws Exception {
        // Initialize the database
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);

        // Get all the quotaServiceList
        restQuotaServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotaService.getId().toString())))
            .andExpect(jsonPath("$.[*].limite").value(hasItem(sameNumber(DEFAULT_LIMITE))))
            .andExpect(jsonPath("$.[*].unite").value(hasItem(DEFAULT_UNITE.toString())))
            .andExpect(jsonPath("$.[*].periode").value(hasItem(DEFAULT_PERIODE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getQuotaService() throws Exception {
        // Initialize the database
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);

        // Get the quotaService
        restQuotaServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, quotaService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotaService.getId().toString()))
            .andExpect(jsonPath("$.limite").value(sameNumber(DEFAULT_LIMITE)))
            .andExpect(jsonPath("$.unite").value(DEFAULT_UNITE.toString()))
            .andExpect(jsonPath("$.periode").value(DEFAULT_PERIODE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuotaService() throws Exception {
        // Get the quotaService
        restQuotaServiceMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuotaService() throws Exception {
        // Initialize the database
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotaService
        QuotaService updatedQuotaService = quotaServiceRepository.findById(quotaService.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotaService are not directly saved in db
        em.detach(updatedQuotaService);
        updatedQuotaService
            .limite(UPDATED_LIMITE)
            .unite(UPDATED_UNITE)
            .periode(UPDATED_PERIODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(updatedQuotaService);

        restQuotaServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotaServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotaServiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuotaServiceToMatchAllProperties(updatedQuotaService);
    }

    @Test
    @Transactional
    void putNonExistingQuotaService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaService.setId(UUID.randomUUID());

        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotaServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotaServiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotaServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotaService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaService.setId(UUID.randomUUID());

        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotaServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotaService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaService.setId(UUID.randomUUID());

        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaServiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotaServiceWithPatch() throws Exception {
        // Initialize the database
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotaService using partial update
        QuotaService partialUpdatedQuotaService = new QuotaService();
        partialUpdatedQuotaService.setId(quotaService.getId());

        partialUpdatedQuotaService.limite(UPDATED_LIMITE).dateFin(UPDATED_DATE_FIN);

        restQuotaServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotaService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotaService))
            )
            .andExpect(status().isOk());

        // Validate the QuotaService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotaServiceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuotaService, quotaService),
            getPersistedQuotaService(quotaService)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuotaServiceWithPatch() throws Exception {
        // Initialize the database
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotaService using partial update
        QuotaService partialUpdatedQuotaService = new QuotaService();
        partialUpdatedQuotaService.setId(quotaService.getId());

        partialUpdatedQuotaService
            .limite(UPDATED_LIMITE)
            .unite(UPDATED_UNITE)
            .periode(UPDATED_PERIODE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restQuotaServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotaService.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotaService))
            )
            .andExpect(status().isOk());

        // Validate the QuotaService in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotaServiceUpdatableFieldsEquals(partialUpdatedQuotaService, getPersistedQuotaService(partialUpdatedQuotaService));
    }

    @Test
    @Transactional
    void patchNonExistingQuotaService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaService.setId(UUID.randomUUID());

        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotaServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotaServiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotaServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotaService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaService.setId(UUID.randomUUID());

        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotaServiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotaService() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaService.setId(UUID.randomUUID());

        // Create the QuotaService
        QuotaServiceDTO quotaServiceDTO = quotaServiceMapper.toDto(quotaService);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaServiceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quotaServiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotaService in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotaService() throws Exception {
        // Initialize the database
        insertedQuotaService = quotaServiceRepository.saveAndFlush(quotaService);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quotaService
        restQuotaServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotaService.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quotaServiceRepository.count();
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

    protected QuotaService getPersistedQuotaService(QuotaService quotaService) {
        return quotaServiceRepository.findById(quotaService.getId()).orElseThrow();
    }

    protected void assertPersistedQuotaServiceToMatchAllProperties(QuotaService expectedQuotaService) {
        assertQuotaServiceAllPropertiesEquals(expectedQuotaService, getPersistedQuotaService(expectedQuotaService));
    }

    protected void assertPersistedQuotaServiceToMatchUpdatableProperties(QuotaService expectedQuotaService) {
        assertQuotaServiceAllUpdatablePropertiesEquals(expectedQuotaService, getPersistedQuotaService(expectedQuotaService));
    }
}
