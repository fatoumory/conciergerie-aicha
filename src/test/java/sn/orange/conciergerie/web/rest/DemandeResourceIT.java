package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.DemandeAsserts.*;
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
import sn.orange.conciergerie.domain.Client;
import sn.orange.conciergerie.domain.CodePromo;
import sn.orange.conciergerie.domain.Demande;
import sn.orange.conciergerie.domain.ServiceConciergerie;
import sn.orange.conciergerie.domain.StatutDemande;
import sn.orange.conciergerie.domain.TypeDemande;
import sn.orange.conciergerie.repository.DemandeRepository;
import sn.orange.conciergerie.service.DemandeService;
import sn.orange.conciergerie.service.dto.DemandeDTO;
import sn.orange.conciergerie.service.mapper.DemandeMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link DemandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeResourceIT {

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.ofEpochMilli(1701862474321L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DemandeRepository demandeRepository;

    @Mock
    private DemandeRepository demandeRepositoryMock;

    @Autowired
    private DemandeMapper demandeMapper;

    @Mock
    private DemandeService demandeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeMockMvc;

    private Demande demande;

    private Demande insertedDemande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createEntity(EntityManager em) {
        Demande demande = new Demande().dateCreation(DEFAULT_DATE_CREATION).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).getFirst();
        }
        demande.setClient(client);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        demande.setService(serviceConciergerie);
        // Add required entity
        TypeDemande typeDemande;
        if (TestUtil.findAll(em, TypeDemande.class).isEmpty()) {
            typeDemande = TypeDemandeResourceIT.createEntity();
            em.persist(typeDemande);
            em.flush();
        } else {
            typeDemande = TestUtil.findAll(em, TypeDemande.class).getFirst();
        }
        demande.setTypeDemande(typeDemande);
        // Add required entity
        StatutDemande statutDemande;
        if (TestUtil.findAll(em, StatutDemande.class).isEmpty()) {
            statutDemande = StatutDemandeResourceIT.createEntity();
            em.persist(statutDemande);
            em.flush();
        } else {
            statutDemande = TestUtil.findAll(em, StatutDemande.class).getFirst();
        }
        demande.setStatut(statutDemande);
        return demande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createUpdatedEntity(EntityManager em) {
        Demande updatedDemande = new Demande().dateCreation(UPDATED_DATE_CREATION).description(UPDATED_DESCRIPTION);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).getFirst();
        }
        updatedDemande.setClient(client);
        // Add required entity
        ServiceConciergerie serviceConciergerie;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            serviceConciergerie = ServiceConciergerieResourceIT.createUpdatedEntity(em);
            em.persist(serviceConciergerie);
            em.flush();
        } else {
            serviceConciergerie = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        updatedDemande.setService(serviceConciergerie);
        // Add required entity
        TypeDemande typeDemande;
        if (TestUtil.findAll(em, TypeDemande.class).isEmpty()) {
            typeDemande = TypeDemandeResourceIT.createUpdatedEntity();
            em.persist(typeDemande);
            em.flush();
        } else {
            typeDemande = TestUtil.findAll(em, TypeDemande.class).getFirst();
        }
        updatedDemande.setTypeDemande(typeDemande);
        // Add required entity
        StatutDemande statutDemande;
        if (TestUtil.findAll(em, StatutDemande.class).isEmpty()) {
            statutDemande = StatutDemandeResourceIT.createUpdatedEntity();
            em.persist(statutDemande);
            em.flush();
        } else {
            statutDemande = TestUtil.findAll(em, StatutDemande.class).getFirst();
        }
        updatedDemande.setStatut(statutDemande);
        return updatedDemande;
    }

    @BeforeEach
    void initTest() {
        demande = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDemande != null) {
            demandeRepository.delete(insertedDemande);
            insertedDemande = null;
        }
    }

    @Test
    @Transactional
    void createDemande() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);
        var returnedDemandeDTO = om.readValue(
            restDemandeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DemandeDTO.class
        );

        // Validate the Demande in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDemande = demandeMapper.toEntity(returnedDemandeDTO);
        assertDemandeUpdatableFieldsEquals(returnedDemande, getPersistedDemande(returnedDemande));

        insertedDemande = returnedDemande;
    }

    @Test
    @Transactional
    void createDemandeWithExistingId() throws Exception {
        // Create the Demande with an existing ID
        insertedDemande = demandeRepository.saveAndFlush(demande);
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        demande.setDateCreation(null);

        // Create the Demande, which fails.
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandes() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandesWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(demandeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDemande() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get the demande
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demande.getId().toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getDemandesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        UUID id = demande.getId();

        defaultDemandeFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllDemandesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where dateCreation equals to
        defaultDemandeFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllDemandesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where dateCreation in
        defaultDemandeFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllDemandesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where dateCreation is not null
        defaultDemandeFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where description equals to
        defaultDemandeFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDemandesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where description in
        defaultDemandeFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDemandesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where description is not null
        defaultDemandeFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllDemandesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where description contains
        defaultDemandeFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDemandesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        // Get all the demandeList where description does not contain
        defaultDemandeFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDemandesByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            demandeRepository.saveAndFlush(demande);
            client = ClientResourceIT.createEntity(em);
        } else {
            client = TestUtil.findAll(em, Client.class).getFirst();
        }
        em.persist(client);
        em.flush();
        demande.setClient(client);
        demandeRepository.saveAndFlush(demande);
        UUID clientId = client.getId();
        // Get all the demandeList where client equals to clientId
        defaultDemandeShouldBeFound("clientId.equals=" + clientId);

        // Get all the demandeList where client equals to UUID.randomUUID()
        defaultDemandeShouldNotBeFound("clientId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllDemandesByServiceIsEqualToSomething() throws Exception {
        ServiceConciergerie service;
        if (TestUtil.findAll(em, ServiceConciergerie.class).isEmpty()) {
            demandeRepository.saveAndFlush(demande);
            service = ServiceConciergerieResourceIT.createEntity(em);
        } else {
            service = TestUtil.findAll(em, ServiceConciergerie.class).getFirst();
        }
        em.persist(service);
        em.flush();
        demande.setService(service);
        demandeRepository.saveAndFlush(demande);
        UUID serviceId = service.getId();
        // Get all the demandeList where service equals to serviceId
        defaultDemandeShouldBeFound("serviceId.equals=" + serviceId);

        // Get all the demandeList where service equals to UUID.randomUUID()
        defaultDemandeShouldNotBeFound("serviceId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllDemandesByTypeDemandeIsEqualToSomething() throws Exception {
        TypeDemande typeDemande;
        if (TestUtil.findAll(em, TypeDemande.class).isEmpty()) {
            demandeRepository.saveAndFlush(demande);
            typeDemande = TypeDemandeResourceIT.createEntity();
        } else {
            typeDemande = TestUtil.findAll(em, TypeDemande.class).getFirst();
        }
        em.persist(typeDemande);
        em.flush();
        demande.setTypeDemande(typeDemande);
        demandeRepository.saveAndFlush(demande);
        UUID typeDemandeId = typeDemande.getId();
        // Get all the demandeList where typeDemande equals to typeDemandeId
        defaultDemandeShouldBeFound("typeDemandeId.equals=" + typeDemandeId);

        // Get all the demandeList where typeDemande equals to UUID.randomUUID()
        defaultDemandeShouldNotBeFound("typeDemandeId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllDemandesByStatutIsEqualToSomething() throws Exception {
        StatutDemande statut;
        if (TestUtil.findAll(em, StatutDemande.class).isEmpty()) {
            demandeRepository.saveAndFlush(demande);
            statut = StatutDemandeResourceIT.createEntity();
        } else {
            statut = TestUtil.findAll(em, StatutDemande.class).getFirst();
        }
        em.persist(statut);
        em.flush();
        demande.setStatut(statut);
        demandeRepository.saveAndFlush(demande);
        UUID statutId = statut.getId();
        // Get all the demandeList where statut equals to statutId
        defaultDemandeShouldBeFound("statutId.equals=" + statutId);

        // Get all the demandeList where statut equals to UUID.randomUUID()
        defaultDemandeShouldNotBeFound("statutId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllDemandesByCodePromoIsEqualToSomething() throws Exception {
        CodePromo codePromo;
        if (TestUtil.findAll(em, CodePromo.class).isEmpty()) {
            demandeRepository.saveAndFlush(demande);
            codePromo = CodePromoResourceIT.createEntity();
        } else {
            codePromo = TestUtil.findAll(em, CodePromo.class).getFirst();
        }
        em.persist(codePromo);
        em.flush();
        demande.setCodePromo(codePromo);
        demandeRepository.saveAndFlush(demande);
        UUID codePromoId = codePromo.getId();
        // Get all the demandeList where codePromo equals to codePromoId
        defaultDemandeShouldBeFound("codePromoId.equals=" + codePromoId);

        // Get all the demandeList where codePromo equals to UUID.randomUUID()
        defaultDemandeShouldNotBeFound("codePromoId.equals=" + UUID.randomUUID());
    }

    private void defaultDemandeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDemandeShouldBeFound(shouldBeFound);
        defaultDemandeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDemandeShouldBeFound(String filter) throws Exception {
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDemandeShouldNotBeFound(String filter) throws Exception {
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDemande() throws Exception {
        // Get the demande
        restDemandeMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDemande() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the demande
        Demande updatedDemande = demandeRepository.findById(demande.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDemande are not directly saved in db
        em.detach(updatedDemande);
        updatedDemande.dateCreation(UPDATED_DATE_CREATION).description(UPDATED_DESCRIPTION);
        DemandeDTO demandeDTO = demandeMapper.toDto(updatedDemande);

        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDemandeToMatchAllProperties(updatedDemande);
    }

    @Test
    @Transactional
    void putNonExistingDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(UUID.randomUUID());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(UUID.randomUUID());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(UUID.randomUUID());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande.dateCreation(UPDATED_DATE_CREATION);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDemandeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDemande, demande), getPersistedDemande(demande));
    }

    @Test
    @Transactional
    void fullUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande.dateCreation(UPDATED_DATE_CREATION).description(UPDATED_DESCRIPTION);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDemandeUpdatableFieldsEquals(partialUpdatedDemande, getPersistedDemande(partialUpdatedDemande));
    }

    @Test
    @Transactional
    void patchNonExistingDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(UUID.randomUUID());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(UUID.randomUUID());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(demandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemande() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        demande.setId(UUID.randomUUID());

        // Create the Demande
        DemandeDTO demandeDTO = demandeMapper.toDto(demande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(demandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemande() throws Exception {
        // Initialize the database
        insertedDemande = demandeRepository.saveAndFlush(demande);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the demande
        restDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, demande.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return demandeRepository.count();
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

    protected Demande getPersistedDemande(Demande demande) {
        return demandeRepository.findById(demande.getId()).orElseThrow();
    }

    protected void assertPersistedDemandeToMatchAllProperties(Demande expectedDemande) {
        assertDemandeAllPropertiesEquals(expectedDemande, getPersistedDemande(expectedDemande));
    }

    protected void assertPersistedDemandeToMatchUpdatableProperties(Demande expectedDemande) {
        assertDemandeAllUpdatablePropertiesEquals(expectedDemande, getPersistedDemande(expectedDemande));
    }
}
