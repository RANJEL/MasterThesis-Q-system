package cz.komix.qsystem.backend.logic.usecases;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FEQueryEntityType;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FESearchCriteria;
import cz.komix.qsystem.backend.security.user.LoggedUser;

import java.util.List;

/**
 * Interface containing more complex use cases, that covers multiple steps.
 *
 * @author Jan Lejnar
 */
public interface IQSystemUseCases {
    /**
     * UC PP0102, steps 1 - 6
     *
     * @param reason
     * @param searchCriteria
     * @return
     * @throws ISAAARequestFailed
     */
    StandardQueryResponseData dispatchStandardQuery(String reason, StandardQueryType searchCriteria) throws ISAAARequestFailed, ISAAAInterfaceNotFound;

    /**
     * UC PP0103, steps 3 - 5
     *
     * @param reason
     * @param AAAID
     * @return
     * @throws ISAAARequestFailed
     * @throws ISAAAInterfaceNotFound
     */
    StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID) throws ISAAARequestFailed, ISAAAInterfaceNotFound;

    /**
     * UC PP0103, steps 3 - 5
     *
     * @param reason
     * @param AAAID
     * @param dataId
     * @return
     * @throws ISAAARequestFailed
     * @throws ISAAAInterfaceNotFound
     */
    String dispatchLoadAttachments(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound;

    /**
     * UC PP0103, steps 3 - 5
     *
     * @param reason
     * @param AAAID
     * @param dataId
     * @return
     * @throws ISAAARequestFailed
     * @throws ISAAAInterfaceNotFound
     */
    String dispatchLoadLinks(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound;

    /**
     * UC PP0201 step 2, PP0202 step 2, PP0203 step 2, PP0204 step 2, PP0205 step 2, PP0206 step 2
     *
     * @param userName
     * @param adminBookmark
     */
    void logEntryIntoAdministration(String userName, String adminBookmark);

    /**
     * UC PP0206 steps 6-7
     */
    void setPreferredISAAAInterface(String isAAAInterfaceName);


    /**
     * UC PP0101 steps 2-3
     * <p>
     * Method that wraps call of IAppFeatures.sortSearchCriteriaByOrder(IAppFeatures.getAllSearchCriteria(..))
     * and adds property queryModifiers next to allowedQueryModifiers that has all sorted queryModifiers,
     * but also where each query modifier contains attribute determining
     * if FE should disable checkbox or not, based on ICT and logged user.
     *
     * @param queryEntityTypeCategory
     * @param queryEntityType
     * @return
     */
    List<FESearchCriteria> getAllSearchCriteriaWithCheckedInfo(
            String queryEntityTypeCategory,
            String queryEntityType);

    /**
     * Wraps call of IAppFeatures.sortQueryEntitiesByOrder(IAppFeatures.getAllSingleCategoryQueryEntities(..))
     * but adds property isSelectable, that is set to false if user can't perform query on related query entity
     *
     * @return
     */
    List<FEQueryEntityType> getAllSingleCategoryQueryEntities();

    List<FEQueryEntityType> getAllMultiCategoryQueryEntities();

    /**
     * UC PP0401
     *
     * @return
     */
    LoggedUser getLoggedUser();
}
