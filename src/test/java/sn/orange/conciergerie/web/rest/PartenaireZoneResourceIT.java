package sn.orange.conciergerie.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.orange.conciergerie.domain.PartenaireZoneAsserts.*;
import static sn.orange.conciergerie.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
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
import sn.orange.conciergerie.domain.Partenaire;
import sn.orange.conciergerie.domain.PartenaireZone;
import sn.orange.conciergerie.domain.Zone;
import sn.orange.conciergerie.repository.PartenaireZoneRepository;
import sn.orange.conciergerie.service.PartenaireZoneService;
import sn.orange.conciergerie.service.dto.PartenaireZoneDTO;
import sn.orange.conciergerie.service.mapper.PartenaireZoneMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PartenaireZoneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartenaireZoneResourceIT {

    private static final String ENTITY_API_URL = "/api/partenaire-zones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartenaireZoneRepository partenaireZoneRepository;

    @Mock
    private PartenaireZoneRepository partenaireZoneRepositoryMock;

    @Autowired
    private PartenaireZoneMapper partenaireZoneMapper;

    @Mock
    private PartenaireZoneService partenaireZoneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartenaireZoneMockMvc;

    private PartenaireZone partenaireZone;

    private PartenaireZone insertedPartenaireZone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartenaireZone createEntity(EntityManager em) {
        PartenaireZone partenaireZone = new PartenaireZone();
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        partenaireZone.setPartenaire(partenaire);
        // Add required entity
        Zone zone;
        if (TestUtil.findAll(em, Zone.class).isEmpty()) {
            zone = ZoneResourceIT.createEntity();
            em.persist(zone);
            em.flush();
        } else {
            zone = TestUtil.findAll(em, Zone.class).getFirst();
        }
        partenaireZone.setZone(zone);
        return partenaireZone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartenaireZone createUpdatedEntity(EntityManager em) {
        PartenaireZone updatedPartenaireZone = new PartenaireZone();
        // Add required entity
        Partenaire partenaire;
        if (TestUtil.findAll(em, Partenaire.class).isEmpty()) {
            partenaire = PartenaireResourceIT.createUpdatedEntity();
            em.persist(partenaire);
            em.flush();
        } else {
            partenaire = TestUtil.findAll(em, Partenaire.class).getFirst();
        }
        updatedPartenaireZone.setPartenaire(partenaire);
        // Add required entity
        Zone zone;
        if (TestUtil.findAll(em, Zone.class).isEmpty()) {
            zone = ZoneResourceIT.createUpdatedEntity();
            em.persist(zone);
            em.flush();
        } else {
            zone = TestUtil.findAll(em, Zone.class).getFirst();
        }
        updatedPartenaireZone.setZone(zone);
        return updatedPartenaireZone;
    }

    @BeforeEach
    void initTest() {
        partenaireZone = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPartenaireZone != null) {
            partenaireZoneRepository.delete(insertedPartenaireZone);
            insertedPartenaireZone = null;
        }
    }

    @Test
    @Transactional
    void createPartenaireZone() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);
        var returnedPartenaireZoneDTO = om.readValue(
            restPartenaireZoneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireZoneDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PartenaireZoneDTO.class
        );

        // Validate the PartenaireZone in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPartenaireZone = partenaireZoneMapper.toEntity(returnedPartenaireZoneDTO);
        assertPartenaireZoneUpdatableFieldsEquals(returnedPartenaireZone, getPersistedPartenaireZone(returnedPartenaireZone));

        insertedPartenaireZone = returnedPartenaireZone;
    }

    @Test
    @Transactional
    void createPartenaireZoneWithExistingId() throws Exception {
        // Create the PartenaireZone with an existing ID
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartenaireZoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireZoneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartenaireZones() throws Exception {
        // Initialize the database
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);

        // Get all the partenaireZoneList
        restPartenaireZoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaireZone.getId().toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartenaireZonesWithEagerRelationshipsIsEnabled() throws Exception {
        when(partenaireZoneServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPartenaireZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partenaireZoneServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartenaireZonesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partenaireZoneServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPartenaireZoneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(partenaireZoneRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPartenaireZone() throws Exception {
        // Initialize the database
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);

        // Get the partenaireZone
        restPartenaireZoneMockMvc
            .perform(get(ENTITY_API_URL_ID, partenaireZone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partenaireZone.getId().toString()));
    }

    @Test
    @Transactional
    void getNonExistingPartenaireZone() throws Exception {
        // Get the partenaireZone
        restPartenaireZoneMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPartenaireZone() throws Exception {
        // Initialize the database
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partenaireZone
        PartenaireZone updatedPartenaireZone = partenaireZoneRepository.findById(partenaireZone.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPartenaireZone are not directly saved in db
        em.detach(updatedPartenaireZone);
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(updatedPartenaireZone);

        restPartenaireZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partenaireZoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partenaireZoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartenaireZoneToMatchAllProperties(updatedPartenaireZone);
    }

    @Test
    @Transactional
    void putNonExistingPartenaireZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaireZone.setId(UUID.randomUUID());

        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partenaireZoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partenaireZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartenaireZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaireZone.setId(UUID.randomUUID());

        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireZoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(partenaireZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartenaireZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaireZone.setId(UUID.randomUUID());

        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireZoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(partenaireZoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartenaireZoneWithPatch() throws Exception {
        // Initialize the database
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partenaireZone using partial update
        PartenaireZone partialUpdatedPartenaireZone = new PartenaireZone();
        partialUpdatedPartenaireZone.setId(partenaireZone.getId());

        restPartenaireZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaireZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartenaireZone))
            )
            .andExpect(status().isOk());

        // Validate the PartenaireZone in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartenaireZoneUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPartenaireZone, partenaireZone),
            getPersistedPartenaireZone(partenaireZone)
        );
    }

    @Test
    @Transactional
    void fullUpdatePartenaireZoneWithPatch() throws Exception {
        // Initialize the database
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partenaireZone using partial update
        PartenaireZone partialUpdatedPartenaireZone = new PartenaireZone();
        partialUpdatedPartenaireZone.setId(partenaireZone.getId());

        restPartenaireZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaireZone.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPartenaireZone))
            )
            .andExpect(status().isOk());

        // Validate the PartenaireZone in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartenaireZoneUpdatableFieldsEquals(partialUpdatedPartenaireZone, getPersistedPartenaireZone(partialUpdatedPartenaireZone));
    }

    @Test
    @Transactional
    void patchNonExistingPartenaireZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaireZone.setId(UUID.randomUUID());

        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenaireZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partenaireZoneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partenaireZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartenaireZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaireZone.setId(UUID.randomUUID());

        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireZoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partenaireZoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartenaireZone() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partenaireZone.setId(UUID.randomUUID());

        // Create the PartenaireZone
        PartenaireZoneDTO partenaireZoneDTO = partenaireZoneMapper.toDto(partenaireZone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenaireZoneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(partenaireZoneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartenaireZone in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartenaireZone() throws Exception {
        // Initialize the database
        insertedPartenaireZone = partenaireZoneRepository.saveAndFlush(partenaireZone);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partenaireZone
        restPartenaireZoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, partenaireZone.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partenaireZoneRepository.count();
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

    protected PartenaireZone getPersistedPartenaireZone(PartenaireZone partenaireZone) {
        return partenaireZoneRepository.findById(partenaireZone.getId()).orElseThrow();
    }

    protected void assertPersistedPartenaireZoneToMatchAllProperties(PartenaireZone expectedPartenaireZone) {
        assertPartenaireZoneAllPropertiesEquals(expectedPartenaireZone, getPersistedPartenaireZone(expectedPartenaireZone));
    }

    protected void assertPersistedPartenaireZoneToMatchUpdatableProperties(PartenaireZone expectedPartenaireZone) {
        assertPartenaireZoneAllUpdatablePropertiesEquals(expectedPartenaireZone, getPersistedPartenaireZone(expectedPartenaireZone));
    }
}
