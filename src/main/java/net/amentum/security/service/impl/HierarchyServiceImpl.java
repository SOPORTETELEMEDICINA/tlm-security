package net.amentum.security.service.impl;

import net.amentum.security.exception.ExceptionServiceCode;
import net.amentum.security.exception.HierarchyException;
import net.amentum.security.model.Profile;
import net.amentum.security.model.ProfileHasBoss;
import net.amentum.security.model.ProfileHasBossId;
import net.amentum.security.model.TicketType;
import net.amentum.security.persistence.ProfileHasBossRepository;
import net.amentum.security.persistence.ProfileRepository;
import net.amentum.security.persistence.TicketTypeRepository;
import net.amentum.security.service.HierarchyService;
import net.amentum.security.views.HierarchyRequestView;
import net.amentum.security.views.ProfileHierarchyView;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  by marellano.
 */

@Service
@Transactional(readOnly = true)
public class HierarchyServiceImpl implements HierarchyService {

    private Logger logger = LoggerFactory.getLogger(HierarchyServiceImpl.class);

    private ProfileHasBossRepository hasBossRepository;

    private TicketTypeRepository ticketTypeRepository;

    private ProfileRepository profileRepository;

    @Autowired
    public void setProfileRepository(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Autowired
    public void setTicketTypeRepository(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Autowired
    public void setHasBossRepository(ProfileHasBossRepository hasBossRepository) {
        this.hasBossRepository = hasBossRepository;
    }

    /**
     * {@inheritDoc}
     * */
    @Transactional(readOnly = false,rollbackFor = {HierarchyException.class})
    @Override
    public void addOrUpdateHierarchy(HierarchyRequestView hierarchyView) throws HierarchyException {
        try{
            Profile boss = profileRepository.findOne(hierarchyView.getIdBoss());
            List<Long> notSaveList = new ArrayList<>();
            List<ProfileHasBoss> profileHasBosses = new ArrayList<>();

            Specifications<ProfileHasBoss> spec = Specifications.where(
                    (root, query, cb) ->{
                        Join<ProfileHasBoss, ProfileHasBossId> pk = root.join("pk");
                        Join<ProfileHasBossId, Profile> profileJoin = pk.join("boss");
                        Predicate or = null;
                        return cb.and(
                                cb.equal(profileJoin.get("profileId"),hierarchyView.getIdBoss())
                        );
                    }
            );

            if(spec != null){
                profileHasBosses = hasBossRepository.findAll(spec);
                for (ProfileHasBoss phb: profileHasBosses) {
                    if(!existRelationInDatabase(phb.getProfile().getProfileId(), hierarchyView.getIdProfiles())){
                        hasBossRepository.delete(phb);
                    }else{
                        notSaveList.add(phb.getProfile().getProfileId());
                    }
                }

                for (Long idProfile: hierarchyView.getIdProfiles()) {
                    if(!notSaveProfile(idProfile, notSaveList)){
                        ProfileHasBoss newHierarchy = new ProfileHasBoss();
                        newHierarchy.setProfileBoss(boss);
                        newHierarchy.setProfile(profileRepository.findOne(idProfile));
                        hasBossRepository.save(newHierarchy);
                    }
                }


            }else{
                for (Long idProfile: hierarchyView.getIdProfiles()) {
                    ProfileHasBoss profileHasBoss = new ProfileHasBoss();
                    profileHasBoss.setProfile(profileRepository.findOne(idProfile));
                    profileHasBoss.setProfileBoss(boss);
                    hasBossRepository.save(profileHasBoss);
                }
            }

        } catch (DataIntegrityViolationException dte) {
            HierarchyException uae = new HierarchyException("No fue posible insertar la jerarquía, ya se encuentra uno con la misma relación.", HierarchyException.LAYER_DAO, HierarchyException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.USER + "- Error al insertar nueva jerarquía - CODE: {} - {}",uae.getExceptionCode(),hierarchyView,dte);
            throw uae;
        } catch (ConstraintViolationException dte) {
            HierarchyException uae = new HierarchyException("No fue posible insertar la jerarquía.", HierarchyException.LAYER_DAO, HierarchyException.ACTION_INSERT);
            uae.addError("- Ocurrio un error al insertar la jerarquía: {}");
            logger.error("- Error al insertar la jerarquía. - CODE: {} - {}",uae.getExceptionCode(),hierarchyView,dte);
            throw dte;
        }catch (Exception ex) {
            HierarchyException uae = new HierarchyException("No fue posible insertar la jerarquía", HierarchyException.LAYER_DAO, HierarchyException.ACTION_INSERT);
            uae.addError("Ocurrio un error al insertar la jerarquía: {}");
            logger.error("Error al insertar la jerarquía - CODE: {} - {}",uae.getExceptionCode(),hierarchyView,ex);
            throw ex;
        }
    }

    private Boolean existRelationInDatabase(Long idProfile, List<Long> idProfilesView){
        Boolean exist = false;
        for (Long idView :idProfilesView){
            if(idView == idProfile){
                exist = true;
            }
        }
        return exist;
    }

    private Boolean notSaveProfile(Long idProfile, List<Long> notSaveList){
        Boolean notSave = false;
        for (Long id: notSaveList) {
            if(id == idProfile){
                notSave = true;
            }
        }
        return notSave;
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public List<ProfileHierarchyView> getHierarchy(Long idProfileBoss) throws HierarchyException {
        try{
            List<ProfileHierarchyView> profileViews = new ArrayList<>();
            List<ProfileHasBoss> profileHasBossList = getProfileBoss(idProfileBoss);
            List<Long> allBoss = new ArrayList<>();
            if(profileHasBossList != null && profileHasBossList.size() > 0){
                for (ProfileHasBoss p :profileHasBossList) {
                    Boolean haveBoss = Boolean.TRUE;
                    Long idBoss = p.getProfileBoss().getProfileId();
                    allBoss.add(idBoss);
                    while (haveBoss){
                        List<ProfileHasBoss> phbList = getProfileBoss(idBoss);
                        if(phbList != null && phbList.size() >0){
                            for (ProfileHasBoss p2: phbList ) {
                                if(p2 != null){
                                    idBoss = p2.getProfileBoss().getProfileId();
                                    allBoss.add(idBoss);
                                }else{
                                    haveBoss = Boolean.FALSE;
                                }
                            }
                        }else{
                            haveBoss = Boolean.FALSE;
                        }
                    }
                }
            }
            List<Profile> allProfiles = profileRepository.findAllOrOrderByProfileId();
            List<ProfileHasBoss> selectedTrue = getAllProfilesAssigned(idProfileBoss);
            if(allProfiles != null && allProfiles.size() >0){
                for (Profile profile: allProfiles) {
                    if(!removeBoss(profile.getProfileId(), allBoss)){
                        ProfileHierarchyView view = new ProfileHierarchyView();
                        view.setIdPerfil(profile.getProfileId());
                        view.setNamePerfil(profile.getProfileName());
                        if(addLikeTrue(profile.getProfileId(), selectedTrue)){
                            view.setActive(Boolean.TRUE);
                        }else{
                            view.setActive(Boolean.FALSE);
                        }
                        profileViews.add(view);
                    }
                }
            }
            logger.info("Se obtuvo la informacion correctamente se encontro: {} elementos",profileViews.size());
            logger.debug("Se obtuvo la informacion correctamente se encontraron los siguientes elementos: {}",profileViews);
            return profileViews;
        } catch (DataIntegrityViolationException dte) {
            HierarchyException he = new HierarchyException("No fue posible obtener la jerarquía.", HierarchyException.LAYER_DAO, HierarchyException.ACTION_INSERT);
            logger.error(ExceptionServiceCode.USER + "- Error al obtener jerarquía - CODE: {} ",he.getExceptionCode(),dte);
            throw he;
        } catch (ConstraintViolationException dte) {
            HierarchyException he = new HierarchyException("No fue posible obtener la jerarquía. - 2 -", HierarchyException.LAYER_DAO, HierarchyException.ACTION_INSERT);
            he.addError("- Ocurrio un error al obtener la jerarquía - 2 -: {}");
            logger.error("- Error al obtener la jerarquía - 2 -. - CODE: {} ",he.getExceptionCode(),dte);
            throw he;
        }catch (Exception ex) {
            HierarchyException he = new HierarchyException("No fue posible obtener la jerarquía - 3 -", HierarchyException.LAYER_DAO, HierarchyException.ACTION_INSERT);
            he.addError("Ocurrio un error al obtener la jerarquía - 3 -: {}");
            logger.error("Error al obtener la jerarquía - 3 - - CODE: {} ",he.getExceptionCode(),ex);
            throw he;
        }
    }


    private List<ProfileHasBoss> getProfileBoss(Long idProfile){
        List<ProfileHasBoss> profileHasBossList = new ArrayList<>();

        Specifications<ProfileHasBoss> spec = Specifications.where(
                (root, query, cb) ->{
                    Join<ProfileHasBoss, ProfileHasBossId> pk = root.join("pk");
                    Join<ProfileHasBossId, Profile> profileJoin = pk.join("profileId");
                    Predicate or = null;
                    return cb.and(
                            cb.equal(profileJoin.get("profileId"),idProfile)
                    );
                }
        );
        profileHasBossList = hasBossRepository.findAll(spec);
        return profileHasBossList;
    }

    private List<ProfileHasBoss> getAllProfilesAssigned(Long idProfile){
        List<ProfileHasBoss> selectedTrue = new ArrayList<>();

        Specifications<ProfileHasBoss> spec = Specifications.where(
                (root, query, cb) ->{
                    Join<ProfileHasBoss, ProfileHasBossId> pk = root.join("pk");
                    Join<ProfileHasBossId, Profile> profileJoin = pk.join("boss");
                    Predicate or = null;
                    return cb.and(
                            cb.equal(profileJoin.get("profileId"),idProfile)
                    );
                }
        );
        selectedTrue = hasBossRepository.findAll(spec);
        return selectedTrue;
    }

    private Boolean removeBoss (Long idProfile, List<Long> allBoss){
        Boolean remove = Boolean.FALSE;
        if(allBoss != null && allBoss.size() >0){
            for (Long id: allBoss) {
                if(idProfile == id){
                    remove = Boolean.TRUE;
                }
            }
        }
        return remove;
    }

    private Boolean addLikeTrue(Long idProfile, List<ProfileHasBoss> selectedTrue){
        Boolean likeTrue = Boolean.FALSE;
        if(selectedTrue != null && selectedTrue.size() > 0){
            for (ProfileHasBoss b :selectedTrue) {
                if(b.getProfile().getProfileId() == idProfile){
                    likeTrue = Boolean.TRUE;
                }
            }
        }
        return likeTrue;
    }
}
