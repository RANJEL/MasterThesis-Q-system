package cz.komix.qsystem.backend.logic.isaaa;

import cz.komix.qsystem.backend.exception.ISAAARequestFailed;

/**
 * List of methods that any {@link AbstractISAAAAppInterface} should somehow implement,
 * because those will be used by FE.
 *
 * @author Jan Lejnar
 */
public interface IIsaaaMessagesDispatcher {
    StandardQueryResponseData dispatchStandardQuery(String reason, StandardQueryType searchCriteria) throws ISAAARequestFailed;

    StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID) throws ISAAARequestFailed;

    StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID, SubsetCriteriaType subsetCriteriaType) throws ISAAARequestFailed;

    String dispatchLoadAttachments(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed;

    String dispatchLoadLinks(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed;
}
