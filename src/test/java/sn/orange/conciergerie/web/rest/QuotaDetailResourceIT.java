package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.QuotaDetailAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.orange.conciergerie.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.orange.conciergerie.IntegrationTest;
import sn.orange.conciergerie.domain.QuotaDetail;
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.domain.Zone;
import sn.orange.conciergerie.repository.QuotaDetailRepository;
import sn.orange.conciergerie.service.QuotaDetailService;
import sn.orange.conciergerie.service.dto.QuotaDetailDTO;
import sn.orange.conciergerie.service.mapper.QuotaDetailMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link QuotaDetailResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuotaDetailResourceIT {

    private static final BigDecimal DEFAULT_LIMITE = new BigDecimal(0);
    private static final BigDecimal UPDATED_LIMITE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/quota-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuotaDetailRepository quotaDetailRepository;

    @Mock
    private QuotaDetailRepository quotaDetailRepositoryMock;

    @Autowired
    private QuotaDetailMapper quotaDetailMapper;

    @Mock
    private QuotaDetailService quotaDetailServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuotaDetailMockMvc;

    private QuotaDetail quotaDetail;

    private QuotaDetail insertedQuotaDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotaDetail createEntity(EntityManager em) {
        QuotaDetail quotaDetail = new QuotaDetail().limite(DEFAULT_LIMITE);
        // Add required entity
        QuotaService quotaService;
        if (TestUtil.findAll(em, QuotaService.class).isEmpty()) {
            quotaService = QuotaServiceResourceIT.createEntity(em);
            em.persist(quotaService);
            em.flush();
        } else {
            quotaService = TestUtil.findAll(em, QuotaService.class).getFirst();
        }
        quotaDetail.setQuotaService(quotaService);
        // Add required entity
        Zone zone;
        if (TestUtil.findAll(em, Zone.class).isEmpty()) {
            zone = ZoneResourceIT.createEntity();
            em.persist(zone);
            em.flush();
        } else {
            zone = TestUtil.findAll(em, Zone.class).getFirst();
        }
        quotaDetail.setZone(zone);
        return quotaDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuotaDetail createUpdatedEntity(EntityManager em) {
        QuotaDetail updatedQuotaDetail = new QuotaDetail().limite(UPDATED_LIMITE);
        // Add required entity
        QuotaService quotaService;
        if (TestUtil.findAll(em, QuotaService.class).isEmpty()) {
            quotaService = QuotaServiceResourceIT.createUpdatedEntity(em);
            em.persist(quotaService);
            em.flush();
        } else {
            quotaService = TestUtil.findAll(em, QuotaService.class).getFirst();
        }
        updatedQuotaDetail.setQuotaService(quotaService);
        // Add required entity
        Zone zone;
        if (TestUtil.findAll(em, Zone.class).isEmpty()) {
            zone = ZoneResourceIT.createUpdatedEntity();
            em.persist(zone);
            em.flush();
        } else {
            zone = TestUtil.findAll(em, Zone.class).getFirst();
        }
        updatedQuotaDetail.setZone(zone);
        return updatedQuotaDetail;
    }

    @BeforeEach
    void initTest() {
        quotaDetail = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedQuotaDetail != null) {
            quotaDetailRepository.delete(insertedQuotaDetail);
            insertedQuotaDetail = null;
        }
    }

    @Test
    @Transactional
    void createQuotaDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);
        var returnedQuotaDetailDTO = om.readValue(
            restQuotaDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaDetailDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuotaDetailDTO.class
        );

        // Validate the QuotaDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuotaDetail = quotaDetailMapper.toEntity(returnedQuotaDetailDTO);
        assertQuotaDetailUpdatableFieldsEquals(returnedQuotaDetail, getPersistedQuotaDetail(returnedQuotaDetail));

        insertedQuotaDetail = returnedQuotaDetail;
    }

    @Test
    @Transactional
    void createQuotaDetailWithExistingId() throws Exception {
        // Create the QuotaDetail with an existing ID
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotaDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaDetailDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLimiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        quotaDetail.setLimite(null);

        // Create the QuotaDetail, which fails.
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        restQuotaDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaDetailDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuotaDetails() throws Exception {
        // Initialize the database
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);

        // Get all the quotaDetailList
        restQuotaDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotaDetail.getId().toString())))
            .andExpect(jsonPath("$.[*].limite").value(hasItem(sameNumber(DEFAULT_LIMITE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuotaDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(quotaDetailServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restQuotaDetailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quotaDetailServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuotaDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quotaDetailServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restQuotaDetailMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quotaDetailRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuotaDetail() throws Exception {
        // Initialize the database
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);

        // Get the quotaDetail
        restQuotaDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, quotaDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quotaDetail.getId().toString()))
            .andExpect(jsonPath("$.limite").value(sameNumber(DEFAULT_LIMITE)));
    }

    @Test
    @Transactional
    void getNonExistingQuotaDetail() throws Exception {
        // Get the quotaDetail
        restQuotaDetailMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuotaDetail() throws Exception {
        // Initialize the database
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotaDetail
        QuotaDetail updatedQuotaDetail = quotaDetailRepository.findById(quotaDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuotaDetail are not directly saved in db
        em.detach(updatedQuotaDetail);
        updatedQuotaDetail.limite(UPDATED_LIMITE);
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(updatedQuotaDetail);

        restQuotaDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotaDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotaDetailDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuotaDetailToMatchAllProperties(updatedQuotaDetail);
    }

    @Test
    @Transactional
    void putNonExistingQuotaDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaDetail.setId(UUID.randomUUID());

        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotaDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quotaDetailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotaDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuotaDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaDetail.setId(UUID.randomUUID());

        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quotaDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuotaDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaDetail.setId(UUID.randomUUID());

        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quotaDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuotaDetailWithPatch() throws Exception {
        // Initialize the database
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotaDetail using partial update
        QuotaDetail partialUpdatedQuotaDetail = new QuotaDetail();
        partialUpdatedQuotaDetail.setId(quotaDetail.getId());

        partialUpdatedQuotaDetail.limite(UPDATED_LIMITE);

        restQuotaDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotaDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotaDetail))
            )
            .andExpect(status().isOk());

        // Validate the QuotaDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotaDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQuotaDetail, quotaDetail),
            getPersistedQuotaDetail(quotaDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateQuotaDetailWithPatch() throws Exception {
        // Initialize the database
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quotaDetail using partial update
        QuotaDetail partialUpdatedQuotaDetail = new QuotaDetail();
        partialUpdatedQuotaDetail.setId(quotaDetail.getId());

        partialUpdatedQuotaDetail.limite(UPDATED_LIMITE);

        restQuotaDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuotaDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuotaDetail))
            )
            .andExpect(status().isOk());

        // Validate the QuotaDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuotaDetailUpdatableFieldsEquals(partialUpdatedQuotaDetail, getPersistedQuotaDetail(partialUpdatedQuotaDetail));
    }

    @Test
    @Transactional
    void patchNonExistingQuotaDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaDetail.setId(UUID.randomUUID());

        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuotaDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quotaDetailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotaDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuotaDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaDetail.setId(UUID.randomUUID());

        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quotaDetailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuotaDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        quotaDetail.setId(UUID.randomUUID());

        // Create the QuotaDetail
        QuotaDetailDTO quotaDetailDTO = quotaDetailMapper.toDto(quotaDetail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuotaDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quotaDetailDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuotaDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuotaDetail() throws Exception {
        // Initialize the database
        insertedQuotaDetail = quotaDetailRepository.saveAndFlush(quotaDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the quotaDetail
        restQuotaDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, quotaDetail.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return quotaDetailRepository.count();
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

    protected QuotaDetail getPersistedQuotaDetail(QuotaDetail quotaDetail) {
        return quotaDetailRepository.findById(quotaDetail.getId()).orElseThrow();
    }

    protected void assertPersistedQuotaDetailToMatchAllProperties(QuotaDetail expectedQuotaDetail) {
        assertQuotaDetailAllPropertiesEquals(expectedQuotaDetail, getPersistedQuotaDetail(expectedQuotaDetail));
    }

    protected void assertPersistedQuotaDetailToMatchUpdatableProperties(QuotaDetail expectedQuotaDetail) {
        assertQuotaDetailAllUpdatablePropertiesEquals(expectedQuotaDetail, getPersistedQuotaDetail(expectedQuotaDetail));
    }
}
