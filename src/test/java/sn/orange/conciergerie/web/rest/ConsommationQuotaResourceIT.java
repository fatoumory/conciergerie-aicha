package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.ConsommationQuotaAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.orange.conciergerie.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.domain.ConsommationQuota;
import sn.orange.conciergerie.domain.QuotaDetail;
import sn.orange.conciergerie.domain.QuotaService;
import sn.orange.conciergerie.repository.ConsommationQuotaRepository;
import sn.orange.conciergerie.service.ConsommationQuotaService;
import sn.orange.conciergerie.service.dto.ConsommationQuotaDTO;
import sn.orange.conciergerie.service.mapper.ConsommationQuotaMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ConsommationQuotaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ConsommationQuotaResourceIT {

    private static final BigDecimal DEFAULT_QUANTITE = new BigDecimal(0);
    private static final BigDecimal UPDATED_QUANTITE = new BigDecimal(1);
    private static final BigDecimal SMALLER_QUANTITE = new BigDecimal(0 - 1);

    private static final Instant DEFAULT_DATE_CONSOMMATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CONSOMMATION = Instant.ofEpochMilli(1701862474321L);

    private static final String ENTITY_API_URL = "/api/consommation-quotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsommationQuotaRepository consommationQuotaRepository;

    @Mock
    private ConsommationQuotaRepository consommationQuotaRepositoryMock;

    @Autowired
    private ConsommationQuotaMapper consommationQuotaMapper;

    @Mock
    private ConsommationQuotaService consommationQuotaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsommationQuotaMockMvc;

    private ConsommationQuota consommationQuota;

    private ConsommationQuota insertedConsommationQuota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsommationQuota createEntity(EntityManager em) {
        ConsommationQuota consommationQuota = new ConsommationQuota()
            .quantite(DEFAULT_QUANTITE)
            .dateConsommation(DEFAULT_DATE_CONSOMMATION);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).getFirst();
        }
        consommationQuota.setClient(client);
        // Add required entity
        QuotaService quotaService;
        if (TestUtil.findAll(em, QuotaService.class).isEmpty()) {
            quotaService = QuotaServiceResourceIT.createEntity(em);
            em.persist(quotaService);
            em.flush();
        } else {
            quotaService = TestUtil.findAll(em, QuotaService.class).getFirst();
        }
        consommationQuota.setQuotaService(quotaService);
        return consommationQuota;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsommationQuota createUpdatedEntity(EntityManager em) {
        ConsommationQuota updatedConsommationQuota = new ConsommationQuota()
            .quantite(UPDATED_QUANTITE)
            .dateConsommation(UPDATED_DATE_CONSOMMATION);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).getFirst();
        }
        updatedConsommationQuota.setClient(client);
        // Add required entity
        QuotaService quotaService;
        if (TestUtil.findAll(em, QuotaService.class).isEmpty()) {
            quotaService = QuotaServiceResourceIT.createUpdatedEntity(em);
            em.persist(quotaService);
            em.flush();
        } else {
            quotaService = TestUtil.findAll(em, QuotaService.class).getFirst();
        }
        updatedConsommationQuota.setQuotaService(quotaService);
        return updatedConsommationQuota;
    }

    @BeforeEach
    void initTest() {
        consommationQuota = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedConsommationQuota != null) {
            consommationQuotaRepository.delete(insertedConsommationQuota);
            insertedConsommationQuota = null;
        }
    }

    @Test
    @Transactional
    void createConsommationQuota() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);
        var returnedConsommationQuotaDTO = om.readValue(
            restConsommationQuotaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommationQuotaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsommationQuotaDTO.class
        );

        // Validate the ConsommationQuota in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConsommationQuota = consommationQuotaMapper.toEntity(returnedConsommationQuotaDTO);
        assertConsommationQuotaUpdatableFieldsEquals(returnedConsommationQuota, getPersistedConsommationQuota(returnedConsommationQuota));

        insertedConsommationQuota = returnedConsommationQuota;
    }

    @Test
    @Transactional
    void createConsommationQuotaWithExistingId() throws Exception {
        // Create the ConsommationQuota with an existing ID
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsommationQuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommationQuotaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consommationQuota.setQuantite(null);

        // Create the ConsommationQuota, which fails.
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        restConsommationQuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommationQuotaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateConsommationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        consommationQuota.setDateConsommation(null);

        // Create the ConsommationQuota, which fails.
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        restConsommationQuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommationQuotaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsommationQuotas() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList
        restConsommationQuotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consommationQuota.getId().toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(sameNumber(DEFAULT_QUANTITE))))
            .andExpect(jsonPath("$.[*].dateConsommation").value(hasItem(DEFAULT_DATE_CONSOMMATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsommationQuotasWithEagerRelationshipsIsEnabled() throws Exception {
        when(consommationQuotaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsommationQuotaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(consommationQuotaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllConsommationQuotasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(consommationQuotaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restConsommationQuotaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(consommationQuotaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getConsommationQuota() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get the consommationQuota
        restConsommationQuotaMockMvc
            .perform(get(ENTITY_API_URL_ID, consommationQuota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consommationQuota.getId().toString()))
            .andExpect(jsonPath("$.quantite").value(sameNumber(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.dateConsommation").value(DEFAULT_DATE_CONSOMMATION.toString()));
    }

    @Test
    @Transactional
    void getConsommationQuotasByIdFiltering() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        UUID id = consommationQuota.getId();

        defaultConsommationQuotaFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite equals to
        defaultConsommationQuotaFiltering("quantite.equals=" + DEFAULT_QUANTITE, "quantite.equals=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite in
        defaultConsommationQuotaFiltering("quantite.in=" + DEFAULT_QUANTITE + "," + UPDATED_QUANTITE, "quantite.in=" + UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite is not null
        defaultConsommationQuotaFiltering("quantite.specified=true", "quantite.specified=false");
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite is greater than or equal to
        defaultConsommationQuotaFiltering(
            "quantite.greaterThanOrEqual=" + DEFAULT_QUANTITE,
            "quantite.greaterThanOrEqual=" + UPDATED_QUANTITE
        );
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite is less than or equal to
        defaultConsommationQuotaFiltering("quantite.lessThanOrEqual=" + DEFAULT_QUANTITE, "quantite.lessThanOrEqual=" + SMALLER_QUANTITE);
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite is less than
        defaultConsommationQuotaFiltering("quantite.lessThan=" + UPDATED_QUANTITE, "quantite.lessThan=" + DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuantiteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where quantite is greater than
        defaultConsommationQuotaFiltering("quantite.greaterThan=" + SMALLER_QUANTITE, "quantite.greaterThan=" + DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByDateConsommationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where dateConsommation equals to
        defaultConsommationQuotaFiltering(
            "dateConsommation.equals=" + DEFAULT_DATE_CONSOMMATION,
            "dateConsommation.equals=" + UPDATED_DATE_CONSOMMATION
        );
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByDateConsommationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where dateConsommation in
        defaultConsommationQuotaFiltering(
            "dateConsommation.in=" + DEFAULT_DATE_CONSOMMATION + "," + UPDATED_DATE_CONSOMMATION,
            "dateConsommation.in=" + UPDATED_DATE_CONSOMMATION
        );
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByDateConsommationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        // Get all the consommationQuotaList where dateConsommation is not null
        defaultConsommationQuotaFiltering("dateConsommation.specified=true", "dateConsommation.specified=false");
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            consommationQuotaRepository.saveAndFlush(consommationQuota);
            client = ClientResourceIT.createEntity(em);
        } else {
            client = TestUtil.findAll(em, Client.class).getFirst();
        }
        em.persist(client);
        em.flush();
        consommationQuota.setClient(client);
        consommationQuotaRepository.saveAndFlush(consommationQuota);
        UUID clientId = client.getId();
        // Get all the consommationQuotaList where client equals to clientId
        defaultConsommationQuotaShouldBeFound("clientId.equals=" + clientId);

        // Get all the consommationQuotaList where client equals to UUID.randomUUID()
        defaultConsommationQuotaShouldNotBeFound("clientId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuotaServiceIsEqualToSomething() throws Exception {
        QuotaService quotaService;
        if (TestUtil.findAll(em, QuotaService.class).isEmpty()) {
            consommationQuotaRepository.saveAndFlush(consommationQuota);
            quotaService = QuotaServiceResourceIT.createEntity(em);
        } else {
            quotaService = TestUtil.findAll(em, QuotaService.class).getFirst();
        }
        em.persist(quotaService);
        em.flush();
        consommationQuota.setQuotaService(quotaService);
        consommationQuotaRepository.saveAndFlush(consommationQuota);
        UUID quotaServiceId = quotaService.getId();
        // Get all the consommationQuotaList where quotaService equals to quotaServiceId
        defaultConsommationQuotaShouldBeFound("quotaServiceId.equals=" + quotaServiceId);

        // Get all the consommationQuotaList where quotaService equals to UUID.randomUUID()
        defaultConsommationQuotaShouldNotBeFound("quotaServiceId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllConsommationQuotasByQuotaDetailIsEqualToSomething() throws Exception {
        QuotaDetail quotaDetail;
        if (TestUtil.findAll(em, QuotaDetail.class).isEmpty()) {
            consommationQuotaRepository.saveAndFlush(consommationQuota);
            quotaDetail = QuotaDetailResourceIT.createEntity(em);
        } else {
            quotaDetail = TestUtil.findAll(em, QuotaDetail.class).getFirst();
        }
        em.persist(quotaDetail);
        em.flush();
        consommationQuota.setQuotaDetail(quotaDetail);
        consommationQuotaRepository.saveAndFlush(consommationQuota);
        UUID quotaDetailId = quotaDetail.getId();
        // Get all the consommationQuotaList where quotaDetail equals to quotaDetailId
        defaultConsommationQuotaShouldBeFound("quotaDetailId.equals=" + quotaDetailId);

        // Get all the consommationQuotaList where quotaDetail equals to UUID.randomUUID()
        defaultConsommationQuotaShouldNotBeFound("quotaDetailId.equals=" + UUID.randomUUID());
    }

    private void defaultConsommationQuotaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConsommationQuotaShouldBeFound(shouldBeFound);
        defaultConsommationQuotaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsommationQuotaShouldBeFound(String filter) throws Exception {
        restConsommationQuotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consommationQuota.getId().toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(sameNumber(DEFAULT_QUANTITE))))
            .andExpect(jsonPath("$.[*].dateConsommation").value(hasItem(DEFAULT_DATE_CONSOMMATION.toString())));

        // Check, that the count call also returns 1
        restConsommationQuotaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsommationQuotaShouldNotBeFound(String filter) throws Exception {
        restConsommationQuotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsommationQuotaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsommationQuota() throws Exception {
        // Get the consommationQuota
        restConsommationQuotaMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsommationQuota() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consommationQuota
        ConsommationQuota updatedConsommationQuota = consommationQuotaRepository.findById(consommationQuota.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsommationQuota are not directly saved in db
        em.detach(updatedConsommationQuota);
        updatedConsommationQuota.quantite(UPDATED_QUANTITE).dateConsommation(UPDATED_DATE_CONSOMMATION);
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(updatedConsommationQuota);

        restConsommationQuotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consommationQuotaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consommationQuotaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsommationQuotaToMatchAllProperties(updatedConsommationQuota);
    }

    @Test
    @Transactional
    void putNonExistingConsommationQuota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommationQuota.setId(UUID.randomUUID());

        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsommationQuotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consommationQuotaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consommationQuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsommationQuota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommationQuota.setId(UUID.randomUUID());

        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommationQuotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consommationQuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsommationQuota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommationQuota.setId(UUID.randomUUID());

        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommationQuotaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consommationQuotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsommationQuotaWithPatch() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consommationQuota using partial update
        ConsommationQuota partialUpdatedConsommationQuota = new ConsommationQuota();
        partialUpdatedConsommationQuota.setId(consommationQuota.getId());

        partialUpdatedConsommationQuota.quantite(UPDATED_QUANTITE);

        restConsommationQuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsommationQuota.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsommationQuota))
            )
            .andExpect(status().isOk());

        // Validate the ConsommationQuota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsommationQuotaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConsommationQuota, consommationQuota),
            getPersistedConsommationQuota(consommationQuota)
        );
    }

    @Test
    @Transactional
    void fullUpdateConsommationQuotaWithPatch() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consommationQuota using partial update
        ConsommationQuota partialUpdatedConsommationQuota = new ConsommationQuota();
        partialUpdatedConsommationQuota.setId(consommationQuota.getId());

        partialUpdatedConsommationQuota.quantite(UPDATED_QUANTITE).dateConsommation(UPDATED_DATE_CONSOMMATION);

        restConsommationQuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsommationQuota.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsommationQuota))
            )
            .andExpect(status().isOk());

        // Validate the ConsommationQuota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsommationQuotaUpdatableFieldsEquals(
            partialUpdatedConsommationQuota,
            getPersistedConsommationQuota(partialUpdatedConsommationQuota)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConsommationQuota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommationQuota.setId(UUID.randomUUID());

        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsommationQuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consommationQuotaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consommationQuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsommationQuota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommationQuota.setId(UUID.randomUUID());

        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommationQuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consommationQuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsommationQuota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consommationQuota.setId(UUID.randomUUID());

        // Create the ConsommationQuota
        ConsommationQuotaDTO consommationQuotaDTO = consommationQuotaMapper.toDto(consommationQuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsommationQuotaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consommationQuotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsommationQuota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsommationQuota() throws Exception {
        // Initialize the database
        insertedConsommationQuota = consommationQuotaRepository.saveAndFlush(consommationQuota);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consommationQuota
        restConsommationQuotaMockMvc
            .perform(delete(ENTITY_API_URL_ID, consommationQuota.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consommationQuotaRepository.count();
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

    protected ConsommationQuota getPersistedConsommationQuota(ConsommationQuota consommationQuota) {
        return consommationQuotaRepository.findById(consommationQuota.getId()).orElseThrow();
    }

    protected void assertPersistedConsommationQuotaToMatchAllProperties(ConsommationQuota expectedConsommationQuota) {
        assertConsommationQuotaAllPropertiesEquals(expectedConsommationQuota, getPersistedConsommationQuota(expectedConsommationQuota));
    }

    protected void assertPersistedConsommationQuotaToMatchUpdatableProperties(ConsommationQuota expectedConsommationQuota) {
        assertConsommationQuotaAllUpdatablePropertiesEquals(
            expectedConsommationQuota,
            getPersistedConsommationQuota(expectedConsommationQuota)
        );
    }
}
